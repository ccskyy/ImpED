package com.yorker.imped.vo;

public class ExcelMetadataVO {

	private String excelColumnName;
	private String changeColumnName;
	private String dataType;
	private String changeDataType;
	private String uniqueName;
	private String length;
	private String precision;
	private String scale;
	private String fileName;
	private String position;
	private boolean defValue;
	private String misMatchedDataTypes;
	private String noOfRows;
	private String noOfCols;
	private String checkedFlag;
	private String regColumn;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String i) {
		this.length = i;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String i) {
		this.precision = i;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public void setExcelColumnName(String excelColumnName) {
		this.excelColumnName = excelColumnName;
	}

	public String getExcelColumnName() {
		return excelColumnName;
	}

	public String getChangeColumnName() {
		return changeColumnName;
	}

	public void setChangeColumnName(String changeColumnName) {
		this.changeColumnName = changeColumnName;
	}

	public String getChangeDataType() {
		return changeDataType;
	}

	public void setChangeDataType(String changeDataType) {
		this.changeDataType = changeDataType;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition() {
		return position;
	}

	public void setDefValue(boolean defValue) {
		this.defValue = defValue;
	}

	public boolean getDefValue() {
		return defValue;
	}

	public String getMisMatchedDataTypes() {
		return misMatchedDataTypes;
	}

	public void setMisMatchedDataTypes(String misMatchedDataTypes) {
		this.misMatchedDataTypes = misMatchedDataTypes;
	}

	public String getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(String noOfRows) {
		this.noOfRows = noOfRows;
	}

	public String getNoOfCols() {
		return noOfCols;
	}

	public void setNoOfCols(String noOfCols) {
		this.noOfCols = noOfCols;
	}

	public String getCheckedFlag() {
		return checkedFlag;
	}

	public void setCheckedFlag(String checkedFlag) {
		this.checkedFlag = checkedFlag;
	}

	public String getRegColumn() {
		return regColumn;
	}

	public void setRegColumn(String regColumn) {
		this.regColumn = regColumn;
	}

}