package com.yorker.imped.vo;

import java.sql.Timestamp;

public class BaseVO {

	private Long id;
	private String createdByUser;
	private String createdByUserFullName;
	private Timestamp createdDateTime;
	private String lastUpdatedByUser;
	private Timestamp lastUpdatedDateTime;
	private int pageNumber;
	private int pagesAvailable;
	private String responseCode;
	private String responseDesc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedByUser() {
		return createdByUser;
	}

	public String getLastUpdatedByUser() {
		return lastUpdatedByUser;
	}

	public void setLastUpdatedByUser(String lastUpdatedByUser) {
		this.lastUpdatedByUser = lastUpdatedByUser;
	}

	public Timestamp getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}

	public void setLastUpdatedDateTime(Timestamp lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}

	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPagesAvailable() {
		return pagesAvailable;
	}

	public void setPagesAvailable(int pagesAvailable) {
		this.pagesAvailable = pagesAvailable;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDesc() {
		return responseDesc;
	}

	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}

	public String getCreatedByUserFullName() {
		return createdByUserFullName;
	}

	public void setCreatedByUserFullName(String createdByUserFullName) {
		this.createdByUserFullName = createdByUserFullName;
	}

}