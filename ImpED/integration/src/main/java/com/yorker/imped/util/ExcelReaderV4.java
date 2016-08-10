package com.yorker.imped.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.util.StringUtil;
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

/**
 * The Class ExcelReaderV4.
 */
public class ExcelReaderV4 {

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(ExcelReaderV4.class);
	
	/** The V otable. */
	private List<ExcelMetadataVO> VOtable = null;
	
	/**
	 * Gets the v otable.
	 *
	 * @return the v otable
	 */
	public List<ExcelMetadataVO> getVOtable() {
		return VOtable;
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
				for (int j = 0; j < minColumns; j++) {
					output.append(',');
				}
				output.append('\n');
			}
		}

		/**
		 * Start row.
		 *
		 * @param rowNum the row num
		 */
		public void startRow(int rowNum) {
			// If there were gaps, output the missing rows
			outputMissingRows(rowNum - currentRow - 1);
			// Prepare for this row
			firstCellOfRow = true;
			currentRow = rowNum;
			currentCol = -1;
		}

		public void endRow(int rowNum) {
			// Ensure the minimum number of columns
			for (int i = currentCol; i < minColumns; i++) {
				output.append(',');
			}
			output.append('\n');
		}

		/**
		 * Cell.
		 *
		 * @param cellReference the cell reference
		 * @param formattedValue the formatted value
		 * @param comment the comment
		 */
		public void cell(String cellReference, String formattedValue, XSSFComment comment) {
			if (firstCellOfRow) {
				firstCellOfRow = false;
			} else {
				output.append(',');
			}
			ExcelMetadataVO vo ;
			if (currentRow == 0){
				vo = new ExcelMetadataVO();
				vo.setExcelColumnName(formattedValue);
				VOtable.add(vo);
			}else{
				// Did we miss any cells?
				int thisCol = (new CellReference(cellReference)).getCol();
				int missedCols = thisCol - currentCol - 1;
				for (int i = 0; i < missedCols; i++) {
					output.append(',');
				}
				currentCol = thisCol;
				try {
					vo = VOtable.get(thisCol);
					try {
						Double.parseDouble(formattedValue);
						output.append(formattedValue);
						if (vo.getDataType() == null){
							vo.setDataType("NUMBER");
						}
						if (vo.getLength() != null){
							if (Integer.parseInt(vo.getLength()) < formattedValue.length()){
								vo.setLength(String.valueOf(formattedValue.length()));
							}
						}else{
							vo.setLength(String.valueOf(formattedValue.length()));
						}
						
					} catch (NumberFormatException e) {
						output.append('"');
						output.append(formattedValue);
						output.append('"');
						vo.setDataType("TEXT");
						getMetadataProfiling(vo,formattedValue);
						/*if(DateUtil.isDate(formattedValue)) {
							vo.setDataType("DATE");
							//getMetadataProfiling(vo,formattedValue);
						} else {
							vo.setDataType("TEXT");
							getMetadataProfiling(vo,formattedValue);
						}*/
						System.out.println("Col Name :: " +vo.getExcelColumnName() + " Length :: " + vo.getLength());
					}
				} catch(IndexOutOfBoundsException e){
					
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		/**
		 * Gets the metadata profiling.
		 *
		 * @param vo the vo
		 * @param formattedValue the formatted value
		 * @return the metadata profiling
		 */
		private void getMetadataProfiling(ExcelMetadataVO vo, String formattedValue) {
			if (vo.getLength() != null){
				if (Integer.parseInt(vo.getLength()) < formattedValue.length())
					if(StringUtil.hasMultibyte(formattedValue)){
						vo.setLength(String.valueOf(formattedValue.length() * 3));
					} else {
						vo.setLength(String.valueOf(formattedValue.length()));
					}
			}else{
				if(StringUtil.hasMultibyte(formattedValue)){
					vo.setLength(String.valueOf(formattedValue.length() * 3));
				} else {
					vo.setLength(String.valueOf(formattedValue.length()));
				}
			}
		}

		/**
		 * Header footer.
		 *
		 * @param text the text
		 * @param isHeader the is header
		 * @param tagName the tag name
		 */
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

	/** The xlsx package. */
	private final OPCPackage xlsxPackage;

	/** Number of columns to read starting with leftmost. */
	private final int minColumns = 10;

	/** Destination for data. */
	private final PrintStream output;

	/**
	 * Creates a new XLSX -> CSV converter.
	 *
	 * @param pkg            The XLSX package to process
	 * @param output            The PrintStream to output the CSV to
	 */
	public ExcelReaderV4(OPCPackage pkg, PrintStream output) {
		this.xlsxPackage = pkg;
		this.output = output;
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
		VOtable = new ArrayList<ExcelMetadataVO>();
		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
		XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
		StylesTable styles = xssfReader.getStylesTable();
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		int index = 0;
		while (iter.hasNext()) {
			InputStream stream = iter.next();
			String sheetName = iter.getSheetName();
			this.output.println();
			this.output.println(sheetName + " [index=" + index + "]:");
			processSheet(styles, strings, new SheetToCSV(), stream);
			stream.close();
			++index;
			break;
		}
	}

	/**
	 * Load excel metadata.
	 *
	 * @param in the in
	 * @return the list
	 * @throws InvalidFormatException the invalid format exception
	 * @throws ImpedServiceException the Imped service exception
	 */
	public static List<ExcelMetadataVO> loadExcelMetadata(InputStream in) throws InvalidFormatException, ImpedServiceException {
		List<ExcelMetadataVO> VOtable = null;
		try {
			OPCPackage p = OPCPackage.open(in);
			ExcelReaderV4 xlsx2csv = new ExcelReaderV4(p, System.out);
			try {
				xlsx2csv.process();
				VOtable = xlsx2csv.getVOtable();
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
		return VOtable;
	}
}