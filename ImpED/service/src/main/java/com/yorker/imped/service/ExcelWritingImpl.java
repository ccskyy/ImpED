package com.yorker.imped.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.yorker.imped.exceptions.ImpedServiceException;
import com.yorker.imped.vo.ExcelMetadataVO;
import com.yorker.imped.vo.UploadExcelVO;
import com.yorker.imped.connection.AbstractDBConnectionFactory;

/**
 * The Class ExcelWritingImpl.
 */
public class ExcelWritingImpl {

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(ExcelWritingImpl.class);
	
	/** The xlsx package. */
	private final OPCPackage xlsxPackage;
	
	/** The vo. */
	private UploadExcelVO vo;
	
	/** The factory. */
	private AbstractDBConnectionFactory factory;
	
	/** The insert sql. */
	private String insertSQL;
	
	/** The col vs def. */
	private Map<String, String> colVSDef;
	
	/** The batch size. */
	private final short batchSize = 500;

	/** Number of columns to read starting with leftmost. */
	private short minColumns = 1;
	
	/** The row list. */
	List<List<Object>> rowList = new ArrayList<List<Object>>();
	
	/** The column list. */
	List<Object> columnList = null;

	/** Destination for data. */
	private final PrintStream output;

	/**
	 * Creates a new XLSX -> CSV converter.
	 *
	 * @param pkg            The XLSX package to process
	 * @param output            The PrintStream to output the CSV to
	 * @param vo the vo
	 * @param factory the factory
	 * @param insertSQL the insert sql
	 * @param colVSDef the col vs def
	 */
	public ExcelWritingImpl(OPCPackage pkg, PrintStream output, UploadExcelVO vo, AbstractDBConnectionFactory factory, String insertSQL, Map<String, String> colVSDef) {
		this.xlsxPackage = pkg;
		this.output = output;
		this.vo = vo;
		this.factory = factory;
		this.insertSQL = insertSQL;
		this.colVSDef = colVSDef;
	}
	
	/**
	 * The Class SheetToCSV.
	 */
	private class SheetToCSV implements SheetContentsHandler {
		
		/** The first cell of row. */
		private boolean firstCellOfRow = false;
		
		/** The current row. */
		private int currentRow = -1;
		
		/** The current col. */
		private int currentCol = -1;
		
		/**
		 * Output missing rows.
		 *
		 * @param number the number
		 */
		private void outputMissingRows(int number) {
			for (int i = 0; i < number; i++) {
				for (int j = 0; j <= minColumns; j++) {
					//output.append(',');
					try{
						if(vo.getMetadata().get(j).getCheckedFlag().equals("Y")) {
							if (j > 0 && vo.getMetadata().get(j) != null){
								ExcelMetadataVO eVO = vo.getMetadata().get(j);
								columnList.add(colVSDef.get(eVO.getChangeDataType()));
							}
						}
					} catch(IndexOutOfBoundsException e) {
						
					}
				}
				//output.append('\n');
				rowList.add(columnList);
			}
		}

		public void startRow(int rowNum) {
			// initialize the column list and add it to row list if current row num is > 0
			if (rowNum > 0){
				columnList = new ArrayList<Object>();
				rowList.add(columnList);
			}
			// If there were gaps, output the missing rows
			outputMissingRows(rowNum - currentRow - 1);
			// Prepare for this row
			firstCellOfRow = true;
			currentRow = rowNum;
			currentCol = -1;
		}

		public void endRow(int rowNum) {
			if (currentRow == 0) return;
			//System.out.println("Completed Row " + currentRow);
			// Ensure the minimum number of columns
			for (int i = currentCol; i <= minColumns; i++) {
				//output.append(',');
				try {
					if(vo.getMetadata().get(currentCol).getCheckedFlag().equals("Y")) {
						if (i > 0 && vo.getMetadata().get(i) != null){
							ExcelMetadataVO eVO = vo.getMetadata().get(i);
							eVO.setDefValue(true);
							//System.out.println("Position : " + i + " Change type to : " + eVO.getChangeDataType() +  " Default value : " + colVSDef.get(eVO.getChangeDataType()));
							columnList.add(colVSDef.get(eVO.getChangeDataType()));
						}
					}
				} catch(IndexOutOfBoundsException e) {
					
				}
			}
			//output.append('\n');
			if ((currentRow % batchSize) == 0) {
				factory.addRecords(insertSQL, rowList,vo.getStartTime());
				if(factory.getErrorMsg() != null && !factory.getErrorMsg().isEmpty()) {
					try {
						throw new ImpedServiceException("Excel sheet error",factory.getErrorMsg());
					} catch (ImpedServiceException e) {
						e.printStackTrace();
					}
				}
				rowList.clear();
			}
		}

		public void cell(String cellReference, String formattedValue, XSSFComment comment) {
			if (firstCellOfRow) {
				firstCellOfRow = false;
			} 
			if (currentRow == 0)  return;
			// Did we miss any cells?
			int thisCol = (new CellReference(cellReference)).getCol();
			int missedCols = thisCol - currentCol - 1;
			if (missedCols > 0){
				List<Object> temp = new ArrayList<Object>();
				for (int i = 1; i <= missedCols; i++) {
					//output.append(',');
					int pos = thisCol - i;
					try {
						if(vo.getMetadata().get(pos).getCheckedFlag().equals("Y")) {
							if (vo.getMetadata().get(pos) != null){
								ExcelMetadataVO eVO = vo.getMetadata().get(pos);
								eVO.setDefValue(true);
								//System.out.println(" Excel col. :" + eVO.getExcelColumnName() + " data type : " + eVO.getChangeDataType());
								temp.add(colVSDef.get(eVO.getChangeDataType()));
							}
						}
					} catch (IndexOutOfBoundsException e) {
						
					}
				}
				for (int i = (temp.size() - 1); i >= 0; i--){
					columnList.add(temp.get(i));
				}
			}
			currentCol = thisCol;
			// Number or string?
			if(vo.getMetadata().get(currentCol).getCheckedFlag().equals("Y")) {
				try {
					//System.out.println(" columnList  :" + columnList);
					columnList.add(Double.parseDouble(formattedValue));
				} catch (NumberFormatException e) {
					columnList.add(formattedValue);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		public void headerFooter(String text, boolean isHeader, String tagName) {
			// Skip, no headers or footers in CSV
		}

		/**
		 * Cell.
		 *
		 * @param arg0 the arg0
		 * @param arg1 the arg1
		 */
		public void cell(String arg0, String arg1) {
			// TODO Auto-generated method stub
		}

		/**
		 * End row.
		 */
		public void endRow() {
			// TODO Auto-generated method stub
		}
	}

	/**
	 * Parses and shows the content of one sheet using the specified styles and
	 * shared-strings tables.
	 *
	 * @param styles the styles
	 * @param strings the strings
	 * @param sheetHandler the sheet handler
	 * @param sheetInputStream the sheet input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 */
	public void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetHandler,
			InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException {
		DataFormatter formatter = new DataFormatter();
		InputSource sheetSource = new InputSource(sheetInputStream);
		try {
			XMLReader sheetParser = SAXHelper.newXMLReader();
			ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
			sheetParser.setContentHandler(handler);
			sheetParser.parse(sheetSource);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
		}
	}

	/**
	 * Initiates the processing of the XLS workbook file to CSV.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws OpenXML4JException the open xm l4 j exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 */
	public void process() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
		XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
		StylesTable styles = xssfReader.getStylesTable();
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		//int index = 0;
		while (iter.hasNext()) {
			InputStream stream = iter.next();
			//String sheetName = iter.getSheetName();
			//this.output.println(sheetName + " [index=" + index + "]:");
			minColumns = (short) (Integer.parseInt(vo.getNoOfColumns()) - 1) ; 
			processSheet(styles, strings, new SheetToCSV(), stream);
			if (!rowList.isEmpty()) {
				 //System.out.println(" rowList  :" + rowList);
				 factory.addRecords(insertSQL, rowList,vo.getStartTime());
				 if(factory.getErrorMsg() != null && !factory.getErrorMsg().isEmpty()) {
					try {
						throw new ImpedServiceException("Excel sheet error",factory.getErrorMsg());
					} catch (ImpedServiceException e) {
						e.printStackTrace();
					}
				 }
				 rowList.clear();
			}
			stream.close();
			//++index;
			break;
		}
	}

	/**
	 * Insert excel data.
	 *
	 * @param in the in
	 * @param vo the vo
	 * @param factory the factory
	 * @param insertSQL the insert sql
	 * @param colVSDef the col vs def
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ImpedServiceException the Imped service exception
	 */
	public static void insertExcelData(InputStream in, UploadExcelVO vo, AbstractDBConnectionFactory factory, String insertSQL, Map<String, String> colVSDef) throws InvalidFormatException, ImpedServiceException {
		try {
			OPCPackage p = OPCPackage.open(in);
			ExcelWritingImpl xlsx2csv = new ExcelWritingImpl(p, System.out, vo, factory, insertSQL, colVSDef);
			try {
				System.out.println("insertSQL :" + insertSQL);
				xlsx2csv.process();
			} catch (OpenXML4JException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			p.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}