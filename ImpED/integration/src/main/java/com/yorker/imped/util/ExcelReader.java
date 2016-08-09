package com.yorker.imped.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yorker.imped.exceptions.ImpedServiceException;
import com.yorker.imped.vo.ExcelMetadataVO;
import com.yorker.imped.vo.ExcelResponse;

public class ExcelReader {

    //variables used for excel interface
    /*private Workbook workbook = null;
    private FormulaEvaluator evaluator = null;
    private int batchsize = 3;
    private int lastRowNum;
    private int lastCellNum;*/
    private static final Logger logger = Logger.getLogger(ExcelReader.class);
	//loads the metadata needed for the UI to local memory
    //also creates a workbook object that is hereafter used to access the excel document.
	public static List<ExcelMetadataVO> loadExcelMetadata(InputStream inputStream) throws InvalidFormatException , ImpedServiceException{
		Sheet sheet = null;
	    Row dataRow = null;
	    Row headerRow = null;
	    Cell headerCell = null;
	    Workbook workbook = null;
	    List<ExcelMetadataVO> VOtable = null;
		try {
			workbook = new XSSFWorkbook(inputStream);
			FormulaEvaluator evaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
		    int lastRowNum = 0;
		    int lastCellNum = 0;
		    sheet = workbook.getSheetAt(0);
		    if(sheet == null) {
		    	throw new ImpedServiceException("ERROR-520", "Excel Sheet not found");
		    }
		    if(sheet.getPhysicalNumberOfRows() < 2) {
		    	throw new ImpedServiceException("ERROR-521", "Excel Sheet should contain atleast two rows");
		    }
	        lastRowNum = sheet.getPhysicalNumberOfRows();
	        headerRow = sheet.getRow(0);
	        if(headerRow == null){
	        	throw new ImpedServiceException("ERROR-522", "Excel Sheet should contain header line");
	        }
	        dataRow = sheet.getRow(1);
	        if(dataRow == null) {
	        	throw new ImpedServiceException("ERROR-523", "Excel Sheet should contain data line");
	        }
	        lastCellNum = headerRow.getLastCellNum();
	        System.out.println("Excel sheet is: " + lastRowNum + " Rows by " + lastCellNum + " Columns");
	        //set up header file
	    	VOtable = new LinkedList<ExcelMetadataVO>();
	    	StringBuffer sb = new StringBuffer();
	        for(int i = 0; i < lastCellNum; ++i) {
	            headerCell = headerRow.getCell(i);
	        	System.out.println("processing column: " + readCellasString(headerCell, evaluator) + " metadata");
	            /*if(dataCell == null) {
	            	System.out.println("continue");
	            	continue;
	            }*/
	            ExcelMetadataVO columnData = setColumnData(sheet, i, headerCell, evaluator, lastRowNum,sb);
	            columnData.setNoOfRows(String.valueOf(lastRowNum-1));
	            columnData.setNoOfCols(String.valueOf(lastCellNum));
	            VOtable.add(columnData);
	        }
		} catch(ImpedServiceException e){
			logger.error("Could not able to show sample data. Error : " + ImpedUtil.getErrorStackTrace(e));
			throw new ImpedServiceException(e.getErrorCode(),e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
        return VOtable;
	}
    
    private static ExcelMetadataVO setColumnData(Sheet sheet, Integer columnNum, Cell headerCell, FormulaEvaluator evaluator, int lastRowNum,StringBuffer errors)
    		throws InvalidFormatException, ImpedServiceException
    {
    	Row row = sheet.getRow(1);
    	ExcelMetadataVO colData = null;
    	Cell dataCell = row.getCell(columnNum);
    	if(dataCell == null)
    	{
			colData = verifyBlank(sheet, columnNum, evaluator);
	    	colData.setExcelColumnName(readCellasString(headerCell, evaluator));
			return colData;
    	}
    	int cellID = dataCell.getCellType();
    	//System.out.println("cellID: " + cellID);
    	switch(cellID)
    	{
			case 0:
			    if (HSSFDateUtil.isCellDateFormatted(dataCell)) {
	        		colData = verifyDate(sheet, columnNum, evaluator,errors);
			    }
			    else if ((dataCell.getNumericCellValue() == Math.floor(dataCell.getNumericCellValue()))
	    		&& !Double.isInfinite(dataCell.getNumericCellValue())) {
			    	colData = verifyBigInt(sheet, columnNum, evaluator,errors);
			    	
	    		}
	    		else {
	    			colData = verifyNumber(sheet, columnNum, evaluator,errors);
				}
			break;
			case 1:
	        	colData = verifyText(sheet, columnNum, evaluator,errors);
			break;
			case 2:
	    		double outvalue = evaluator.evaluate(dataCell).getNumberValue();
	    		if (outvalue == Math.floor(outvalue)
	    		&& !Double.isInfinite(outvalue)) {
	    			colData = verifyBigInt(sheet, columnNum, evaluator,errors);
	    		}
	    		else {
	    			colData = verifyNumber(sheet, columnNum, evaluator,errors);
	    		}
			break;
			case 3:
				colData = verifyBlank(sheet, columnNum, evaluator);
			break;
			case 4:
				throw new ImpedServiceException("ERROR-519", "Excel Sheet should not contain Boolean Values");
				//colData = verifyBoolean(sheet, columnNum, evaluator);
			case 5:
				//TODO: deal with error
			break;
    	}
    	colData.setExcelColumnName(headerCell.getStringCellValue());
    	return colData;
    }
    
    private static ExcelMetadataVO verifyDate(Sheet sheet, Integer columnNum, FormulaEvaluator evaluator, StringBuffer errors)
    		throws InvalidFormatException
    {
    	ExcelMetadataVO colData = new ExcelMetadataVO();
		colData.setDataType("DATE");
    	System.out.println("Loading Date data");
    	int lastRowNum = sheet.getLastRowNum() + 1;
    	Row row = null;
    	Cell cell = null;
    	for(int i = 1; i < lastRowNum; ++i)
    	{
    		row = sheet.getRow(i);
    		cell = row.getCell(columnNum);
    		if(cell == null || cell.getCellType() == 3)
    		{
    			continue;
    		}
    		if (cell.getCellType() != 0){
    			errors.append("Row No:").append(i).append(" Column No.:").append(columnNum).append(" expecting DATE datatype");
    			colData.setMisMatchedDataTypes(errors.toString());
    		}
    	}
    	return colData;
    }
    
    private static ExcelMetadataVO verifyBigInt(Sheet sheet, Integer columnNum, FormulaEvaluator evaluator,StringBuffer errors)
    	    throws InvalidFormatException
    {
    	ExcelMetadataVO colData = new ExcelMetadataVO();
    	System.out.println("Loading Bigint data");
		colData.setDataType("BIGINT");
		colData.setPrecision("0");
    	int lastRowNum = sheet.getLastRowNum() + 1;
    	Row row = null;
    	Cell cell = null;
    	for(int i = 1; i < lastRowNum; ++i)
    	{
    		row = sheet.getRow(i);
    		cell = row.getCell(columnNum);
    		if(cell == null || cell.getCellType() == 3)
    		{
    			continue;
    		}
    		if (cell.getCellType() != 0){
    			errors.append("Row No:").append(i).append(" Column No.:").append(columnNum).append(" expecting BIGINT datatype").append("\n");
    			colData.setMisMatchedDataTypes(errors.toString());
    		}
    		if (cell.getCellType() == 0){
    			int intValue = (int)cell.getNumericCellValue();
        		String numAsString = Integer.toString(intValue);
        		if(numAsString.length() > Integer.parseInt(colData.getPrecision())) {
            		colData.setPrecision("" + numAsString.length());
        		}
    		}
    	}
    	return colData;
    }
    
    private static ExcelMetadataVO verifyNumber(Sheet sheet, Integer columnNum, FormulaEvaluator evaluator,StringBuffer errors)
    	    throws InvalidFormatException
    {
    	ExcelMetadataVO colData = new ExcelMetadataVO();
    	System.out.println("Loading Number data");
		colData.setDataType("NUMBER");
		colData.setPrecision("0");
		colData.setScale("0");
		double doubleValue;
    	int lastRowNum = sheet.getLastRowNum() + 1;
    	Row row = null;
    	Cell cell = null;
    	for(int i = 1; i < lastRowNum; ++i)
    	{
    		row = sheet.getRow(i);
    		cell = row.getCell(columnNum);
    		if(cell == null || cell.getCellType() == 3)
    		{
    			continue;
    		}
    		else if(cell.getCellType() == 2)
    		{
	    		doubleValue = evaluator.evaluate(cell).getNumberValue();
    		}
    		if (!(cell.getCellType() == 0))
    		{
    			colData = verifyText(sheet, columnNum, evaluator,errors);
    			return colData;
    		}
    		else
    		{
        		doubleValue = (double)cell.getNumericCellValue();
    		}
    		if (cell.getCellType() != 0){
    			errors.append("Row No:").append(i).append(" Column No.:").append(columnNum).append(" expecting NUMBER datatype");
    			colData.setMisMatchedDataTypes(errors.toString());
    		}
    		String numAsString = Double.toString(doubleValue);
    		int decimalPlaces = numAsString.length() - numAsString.indexOf('.') - 1;
    		if(numAsString.length() > Double.parseDouble(colData.getPrecision()))
    		{
        		colData.setPrecision("" + (numAsString.length() - 1));
    		}
    		if(decimalPlaces > Integer.parseInt(colData.getScale()))
    		{
    			colData.setScale("" + decimalPlaces);
    		}
    	}
    	return colData;
    }
    
    private static ExcelMetadataVO verifyText(Sheet sheet, Integer columnNum, FormulaEvaluator evaluator,StringBuffer errors)
    	    throws InvalidFormatException
    {
    	ExcelMetadataVO colData = new ExcelMetadataVO();
    	System.out.println("Loading Text data");
		colData.setDataType("TEXT");
		colData.setLength("0");
    	int lastRowNum = sheet.getLastRowNum() + 1;
    	Row row = null;
    	Cell cell = null;
    	for(int i = 1; i < lastRowNum; ++i)
    	{
    		row = sheet.getRow(i);
    		cell = row.getCell(columnNum);
    		if(cell == null || cell.getCellType() == 3)
    		{
    			continue;
    		}
    		if (cell.getCellType() != 1){
    			errors.append("Row No:").append(i).append(" Column No.:").append(columnNum).append(" expecting TEXT datatype");
    			colData.setMisMatchedDataTypes(errors.toString());
    		}
    		if(readCellasString(cell, evaluator).length() > Integer.parseInt(colData.getLength()))
    		{
        		colData.setLength(String.valueOf(readCellasString(cell, evaluator).length()));
    		}
    	}
    	return colData;
    }
    
    private static ExcelMetadataVO verifyBlank(Sheet sheet, Integer columnNum, FormulaEvaluator evaluator)
    	    throws InvalidFormatException, ImpedServiceException
    {
    	ExcelMetadataVO colData = new ExcelMetadataVO();
    	System.out.println("Loading Blank data");
		colData.setDataType("BLANK");
    	
    	return colData;
    }
    
   /* private static ExcelMetadataVO verifyBoolean(Sheet sheet, Integer columnNum, FormulaEvaluator evaluator)
    	    throws InvalidFormatException
    {
    	ExcelMetadataVO colData = new ExcelMetadataVO();
    	System.out.println("Loading Boolean data");
		colData.setDataType("BOOLEAN");
    	int lastRowNum = sheet.getLastRowNum() + 1;
    	Row row = null;
    	Cell cell = null;
    	for(int i = 1; i < lastRowNum; ++i)
    	{
    		row = sheet.getRow(i);
    		cell = row.getCell(columnNum);
    		if(cell == null || cell.getCellType() == 3)
    		{
    			continue;
    		}
    		if (!(cell.getCellType() == 4))
    		{
    			colData = verifyText(sheet, columnNum, evaluator);
    		}
    	}
    	return colData;
    }*/
    
    private static String readCellasString(Cell cell, FormulaEvaluator evaluator)
    {
    	if(cell == null)
    	{
    		return "";
    	}
    	int cellID = cell.getCellType();
    	String out = "";
    	switch(cellID)
    	{
			case 0:
			    if (HSSFDateUtil.isCellDateFormatted(cell)) {
	        		out = cell.getDateCellValue().toString();
			    }
			    else if ((cell.getNumericCellValue() == Math.floor(cell.getNumericCellValue()))
	    		&& !Double.isInfinite(cell.getNumericCellValue())) {
		    		Integer intValue = (int)cell.getNumericCellValue();
		    		out = intValue.toString();
	    		}
	    		else {
		    		Double doubleValue = cell.getNumericCellValue();
	    			out = doubleValue.toString();
				}
			break;
			case 1:
	        	out = cell.getStringCellValue();
			break;
			case 2:
	    		double outvalue = evaluator.evaluate(cell).getNumberValue();
	    		if (outvalue == Math.floor(outvalue)
	    		&& !Double.isInfinite(outvalue)) {
		    		Integer intValue = (int)cell.getNumericCellValue();
		    		out = intValue.toString();
	    		}
	    		else {
		    		Double doubleValue = cell.getNumericCellValue();
	    			out = doubleValue.toString();
	    		}
			break;
			case 3:
				out = "";
			break;
			case 4:
				out = ((Boolean)cell.getBooleanCellValue()).toString();
			break;
			case 5:
				//TODO: deal with error
			break;
    	}
    	return out;
    }
	
	public ExcelResponse showExcelSampleData(InputStream inputStream) throws ImpedServiceException {
		ExcelResponse res = new ExcelResponse();
		List<Object> values = new ArrayList<Object>();
		Sheet sheet = null;
	    Workbook workbook = null;
	    int colNum = 0;
	    byte maxRecords = 0;
		try{
			workbook = new XSSFWorkbook(inputStream);
			sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				maxRecords ++;
				if(maxRecords == 50){
					break;
				}
				// avoid first row as it is header
	            if (row.getRowNum() == 0) {
	            	res.setResult(row.getLastCellNum());
					colNum = row.getLastCellNum();
	            }
	            for (int count = 0; count < colNum; count++) {
	            	Cell cell = row.getCell(count, Row.RETURN_BLANK_AS_NULL);
	            	// whenever we get blank cell value, we avoid it and continues the loop
		            if (cell == null) {
		            	values.add(" ");
		            	continue;
		            }
		            switch (cell.getCellType()) {
		            	case Cell.CELL_TYPE_STRING:
		            	    values.add(cell.getStringCellValue());
		                	break;
		            	case Cell.CELL_TYPE_NUMERIC:
		            		if (HSSFDateUtil.isCellDateFormatted(cell)) {
		        		    	values.add(ImpedUtil.convertDateToString(cell.getDateCellValue().toString()));
		        		    } else {
		        		    	values.add(cell.getNumericCellValue());
		    				}
		        		    break;
		            	case Cell.CELL_TYPE_BOOLEAN:
		            		throw new ImpedServiceException("ERROR-519", "Excel Sheet should not contain Boolean Values");
		             }
		        }
		    }
			res.setExcelValues(values);
		}catch(ImpedServiceException e){
			logger.error("Could not able to show sample data. Error : " + ImpedUtil.getErrorStackTrace(e));
			throw new ImpedServiceException(e.getErrorCode(),e.getMessage());
		}catch(Exception e){
			logger.error("Could not able to show sample data. Error : " + ImpedUtil.getErrorStackTrace(e));
		}
		return res;
	}
	
}