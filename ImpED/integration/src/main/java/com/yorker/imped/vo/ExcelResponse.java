package com.yorker.imped.vo;

import java.util.List;

public class ExcelResponse extends ImpedResponse {

	private List<Object> excelValues;

	public void setExcelValues(List<Object> excelValues) {
		this.excelValues = excelValues;
	}

	public List<Object> getExcelValues() {
		return excelValues;
	}

}
