package com.yorker.imped.form;

import java.io.Serializable;
import java.util.Set;

public class LoginForm implements Serializable {

	private static final long serialVersionUID = -684172968516491666L;
	private String login_username;
	private String login_password;
	private String userFullName;
	private String projectName;
	private String projEnvName;
	private String currentUIName;
	private String mappingType;
	private String internalMappingFlag;
	private Set<String> featureCodes;
	private String envType;

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getCurrentUIName() {
		return currentUIName;
	}

	public void setCurrentUIName(String currentUIName) {
		this.currentUIName = currentUIName;
	}

	public String getMappingType() {
		return mappingType;
	}

	public void setMappingType(String mappingType) {
		this.mappingType = mappingType;
	}

	public String getInternalMappingFlag() {
		return internalMappingFlag;
	}

	public void setInternalMappingFlag(String internalMappingFlag) {
		this.internalMappingFlag = internalMappingFlag;
	}

	public String getProjEnvName() {
		return projEnvName;
	}

	public void setProjEnvName(String projEnvName) {
		this.projEnvName = projEnvName;
	}

	public Set<String> getFeatureCodes() {
		return featureCodes;
	}

	public void setFeatureCodes(Set<String> featureCodes) {
		this.featureCodes = featureCodes;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getLogin_username() {
		return login_username;
	}

	public void setLogin_username(String login_username) {
		this.login_username = login_username;
	}

	public String getLogin_password() {
		return login_password;
	}

	public void setLogin_password(String login_password) {
		this.login_password = login_password;
	}

	public String getEnvType() {
		return envType;
	}

	public void setEnvType(String envType) {
		this.envType = envType;
	}

}
