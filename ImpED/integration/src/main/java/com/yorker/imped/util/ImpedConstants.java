package com.yorker.imped.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImpedConstants {
	//public static final int REC_FETCH_SIZE = 500;
	public static final String STR_REF = "REF#";
	public static final Map<String, String> ORACLE_DEFLT_VALUES;
	static {
		ORACLE_DEFLT_VALUES = new HashMap<String, String>();
		ORACLE_DEFLT_VALUES.put("VARCHAR2", "UNKNOWN");
		ORACLE_DEFLT_VALUES.put("CLOB", "UNKNOWN");
	}
	public static final Map<String, String> TD_DEFLT_VALUES;
	static {
		TD_DEFLT_VALUES = new HashMap<String, String>();
		TD_DEFLT_VALUES.put("VARCHAR", "UNKNOWN");
	}
	public static final Map<String, String> ERROR_CODES;
	static {
		ERROR_CODES = new HashMap<String, String>();
		ERROR_CODES.put("ERROR-101", "WEB_SC_EXCEPTION");
		ERROR_CODES.put("ERROR-500", "ROOT_EXCEPTION");
		ERROR_CODES.put("ERROR-501", "FILE_NOT_FOUND");
		ERROR_CODES.put("ERROR-502", "SQL_NOT_PROPER");
		ERROR_CODES.put("ERROR-503", "PARENT_KEY_NOT_FOUND");
		ERROR_CODES.put("ERROR-504", "SUBJ_AREA_ALREADY_EXIST");
		ERROR_CODES.put("ERROR-505", "APPLICATION_ALREADY_EXIST");
		ERROR_CODES.put("ERROR-506", "MODIFY_APPLICATION_FAILED");
		ERROR_CODES.put("ERROR-507", "ENV_ALREADY_EXIST");
		ERROR_CODES.put("ERROR-508", "MODIFY_ENV_FAILED");
		ERROR_CODES.put("ERROR-509", "DB_SCHEMA_ALREADY_EXIST");
		ERROR_CODES.put("ERROR-510", "DB_SCHEMAS_ARE_IN_USE");
		ERROR_CODES.put("ERROR-511", "META_DATA_COLLECTION_FAILED");
		ERROR_CODES.put("ERROR-512", "ROLE_ALREADY_EXIST");
		ERROR_CODES.put("ERROR-513", "MODIFY_PROJECT_FAILED");
		ERROR_CODES.put("ERROR-514", "PROJECT_ALREADY_EXIST");
		ERROR_CODES.put("ERROR-515", "BATCH_ALREADY_RUNNING");
	}

	public static final Map<String, String> ERROR_CODES_RESOL;
	static {
		ERROR_CODES_RESOL = new HashMap<String, String>();
		ERROR_CODES.put("ERROR-101", "WEB_SC_EXCEPTION");
		ERROR_CODES_RESOL.put("ERROR-500", "ROOT_EXCEPTION");
		ERROR_CODES_RESOL.put("ERROR-501", "FAST LOAD installation required");
		ERROR_CODES_RESOL.put("ERROR-502", "XINE mapping sql is in-valid");
		ERROR_CODES_RESOL.put("ERROR-503", "Manager id is not found in XINE users list. Please enter valid manager id.");
		ERROR_CODES_RESOL.put("ERROR-504", "Subject area is already exists. Please provide new name.");
		ERROR_CODES_RESOL.put("ERROR-505", "Application already exists. Please provide new name.");
		ERROR_CODES_RESOL.put("ERROR-506", "Application modification failed due to technical error.");
		ERROR_CODES_RESOL.put("ERROR-507", "Environment is already exists. Please provide new name.");
		ERROR_CODES_RESOL.put("ERROR-508", "Environment modification failed due to technical error.");
		ERROR_CODES_RESOL.put("ERROR-509", "{0} schema(s) are used by child tables. Please select them to submit request.");
		ERROR_CODES_RESOL.put("ERROR-510", "Schemas are already in-use");
		ERROR_CODES_RESOL.put("ERROR-511", "Metadata collection failed.");
		ERROR_CODES_RESOL.put("ERROR-512", "{0} role(s) used by child tables. Please un-select them.");
		ERROR_CODES_RESOL.put("ERROR-513", "Project modification failed due to technical error.");
		ERROR_CODES_RESOL.put("ERROR-514", "Project already exists. Please provide new name.");
		ERROR_CODES_RESOL.put("ERROR-515", "Previous Batch Id {0} is : {1}. Hence cant trigger new batch.");
		ERROR_CODES_RESOL.put("ERROR-516", "{0} Target table(s) are already present.Please enter different name.");
		ERROR_CODES_RESOL.put("ERROR-517", "{0} Target column(s) are already present.Please enter different name.");
		ERROR_CODES_RESOL.put("ERROR-518", "Target Connection Name not found in the Job Group. Please add it.");
		ERROR_CODES_RESOL.put("ERROR-519", "Excel Sheet should not contain Boolean Values");
		ERROR_CODES_RESOL.put("ERROR-520", "Excel Sheet not found");
		ERROR_CODES_RESOL.put("ERROR-521", "Excel Sheet should contain atleast two rows");
		ERROR_CODES_RESOL.put("ERROR-522", "Excel Sheet should contain header line");
		ERROR_CODES_RESOL.put("ERROR-523", "Excel Sheet should contain data line");
		ERROR_CODES_RESOL.put("ERROR-524", "{0} subject area(s) used by child tables. Please un-select them.");
		ERROR_CODES_RESOL.put("ERROR-525", "{0} Application(s) used by child tables. Please un-select them.");
		ERROR_CODES_RESOL.put("ERROR-526", "1st row should be String fields");
		ERROR_CODES_RESOL.put("ERROR-527", "Got Error while executing create statement sql.");
		ERROR_CODES_RESOL.put("ERROR-528", "Got Error while generating scripts.");
		ERROR_CODES_RESOL.put("ERROR-529", "Project Name already exists. Please choose another name.");
	}
	public static final List<String> OPS_ROLES;
	static {
		OPS_ROLES = new ArrayList<String>();
		OPS_ROLES.add("ADMINISTRATOR");
		OPS_ROLES.add("OPERATION_LEAD");
		OPS_ROLES.add("DUTY_LEAD");
		OPS_ROLES.add("OPERATION_MANAGER");
	}
}
