package com.yorker.imped.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.yorker.imped.util.ImpedUtil;
import com.yorker.imped.vo.ExcelResponse;

/**
 * The Class ExcelBuilder.
 */
public class ExcelBuilder extends AbstractExcelView {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

	}
	
	/**
	 * Builds the excel document.
	 *
	 * @param model the model
	 * @param workbook the workbook
	 * @param request the request
	 * @param response the response
	 * @throws Exception the exception
	 */
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(model != null && !model.isEmpty()){
			ExcelResponse list = (ExcelResponse) model.get("response");
			buildXL4Data(list,workbook);
		}
	}
	
	/**
	 * Builds the XL 4 data.
	 *
	 * @param list the list
	 * @param workbook the workbook
	 */
	private void buildXL4Data(ExcelResponse list, HSSFWorkbook workbook) {
		try {
		    HSSFSheet sheet = workbook.createSheet("data");
		    int colNum = (Integer) list.getResult();
		    List<List<Object>> splittedList = split(list.getExcelValues(),colNum);
		    int rownum = 0;
		    for(List<Object> rowVals : splittedList){
		    	Row row = sheet.createRow(rownum++);
		    	int cellNum = 0;
		    	for(Object obj : rowVals){
	                Cell cell = row.createCell(cellNum++);
	                if(obj instanceof Timestamp) {
	                	if(list.getResponseCode().equals("ORACLE")) {
	                		cell.setCellValue((Timestamp)obj);
	                	} else if(list.getResponseCode().equals("TERADATA")) {
	                		cell.setCellValue((String)obj.toString());
	                	} else {
	                		cell.setCellValue((String)obj.toString());
	                	}
	                }
	                else if(obj instanceof Boolean)
	                    cell.setCellValue((Boolean)obj);
	                else if(obj instanceof String)
	                    cell.setCellValue((String)obj);
	                else if(obj instanceof Double)
	                    cell.setCellValue((Double)obj);
	                else if(obj instanceof Integer)
	                    cell.setCellValue((Integer)obj);
	                else if(obj instanceof BigDecimal)
	                	cell.setCellValue(((BigDecimal) obj).doubleValue());
	                else {
	                	if(obj != null)
		                	cell.setCellValue(obj.toString());
	                }
	            }
		    }
		}catch (Exception e) {
			logger.error("Error occurred in buildXL4Data(). Error details :: "+ ImpedUtil.getErrorStackTrace(e));
	    }
	}
	
	/**
	 * Split.
	 *
	 * @param list the list
	 * @param length the length
	 * @return the list
	 */
	public static List<List<Object>> split(List<Object> list, final int length) {
	    List<List<Object>> parts = new ArrayList<List<Object>>();
	    final int size = list.size();
	    for (int i = 0; i < size; i += length) {
	        parts.add(new LinkedList<Object>(
	            list.subList(i, Math.min(size, i + length)))
	        );
	    }
	    return parts;
	}
	
}
