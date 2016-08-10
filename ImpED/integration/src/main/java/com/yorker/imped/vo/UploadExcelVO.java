package com.yorker.imped.vo;

import java.sql.Timestamp;
import java.util.List;

public class UploadExcelVO extends BaseVO {

	private String tableName;
	private String viewName;
	private String purpose;
	private String loadOption;
	private List<ExcelMetadataVO> metadata;
	private String fileName;
	private String sequence;
	private String newOROld;
	private String startTime;
	private Timestamp endTime;
	private String dmlType;
	private String consoleLog;
	private int recordsProccessed;
	private String groupName;
	private String projectName;
	private String userId;
	private String replaceTime;
	private String noOfColumns;
	private String hostType;
	private String fieldsTerminatedBy;
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getLoadOption() {
		return loadOption;
	}

	public void setLoadOption(String loadOption) {
		this.loadOption = loadOption;
	}

	public List<ExcelMetadataVO> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<ExcelMetadataVO> metadata) {
		this.metadata = metadata;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getSequence() {
		return sequence;
	}

	public void setNewOROld(String newOROld) {
		this.newOROld = newOROld;
	}

	public String getNewOROld() {
		return newOROld;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setDmlType(String dmlType) {
		this.dmlType = dmlType;
	}

	public String getDmlType() {
		return dmlType;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public String getConsoleLog() {
		return consoleLog;
	}

	public void setConsoleLog(String consoleLog) {
		this.consoleLog = consoleLog;
	}

	public int getRecordsProccessed() {
		return recordsProccessed;
	}

	public void setRecordsProccessed(int recordsProccessed) {
		this.recordsProccessed = recordsProccessed;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReplaceTime() {
		return replaceTime;
	}

	public void setReplaceTime(String replaceTime) {
		this.replaceTime = replaceTime;
	}

	public String getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(String noOfColumns) {
		this.noOfColumns = noOfColumns;
	}

	public String getHostType() {
		return hostType;
	}

	public void setHostType(String hostType) {
		this.hostType = hostType;
	}

	public String getFieldsTerminatedBy() {
		return fieldsTerminatedBy;
	}

	public void setFieldsTerminatedBy(String fieldsTerminatedBy) {
		this.fieldsTerminatedBy = fieldsTerminatedBy;
	}

}
