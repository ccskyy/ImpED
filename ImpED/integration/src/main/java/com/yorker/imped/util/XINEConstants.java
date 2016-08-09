package com.yorker.imped.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XINEConstants {
	public static final long SOURCE_MAX_ROW_CNT = 60000;
	public static final String YES = "YES";
	public static final String NO = "NO";
	public static final String DYNAMIC = "DYNAMIC";
	public static final int MAX_ROWS_SUBSET = 10000;
	public static final int MAX_THREAD = 5;
	public static final int MAX_SLEEP_FOR_RUNNING_THREAD = 30000;
	public static final String TIDAL = "TIDAL";
	public static final String EXTRACT_FROM_TD_TO_HDFS = "EXTRACT FROM TERADATA TO HDFS";
	public static final String EXTRACT_FROM_MS_SQL_SERVER_TO_HDFS = "EXTRACT FROM MS-SQL-SERVER TO HDFS";
	public static final String SRC_2_STAGE = "SOURCE TO STAGE";
	public static final String WORK_2_3NF = "WORK TO 3NF";
	public static final String STAGE_2_WORK = "STAGE TO WORK";
	public static final String ACC_2_STAGE = "ACCUMULATION TO STAGE";
	public static final String ACC_2_ETL = "ACCUMULATION TO ETL ONLY";
	public static final String STAGE_2_ETL = "STAGE TO ETL ONLY";
	public static final String SRC_2_HISTORY = "SOURCE TO HISTORY";
	public static final String SRC_2_FLAT_FILE = "SOURCE TO FLAT FILE";
	public static final String STAGE_2_3NF = "STAGE TO 3NF";
	public static final int REC_FETCH_SIZE = 500;
    public static final String DEF_LAST_EXT_DT = "01/01/1900 00:00:00";
    public static final String DEF_LAST_EXT_DT_ACC2STG = "1900-01-01 00:00:00";
    public static final String STAGE = "$$STGDB";
    public static final String ERRDB = "$$ERRDB";
    public static final String MGRSTGDB = "$$MGRSTGDB";
    public static final String NRTACCUMVWDB = "$$NRTACCUMVWDB";
    public static final String NRTACCUMDB = "$$NRTACCUMDB";
    public static final String ETLONLYDB = "$$ETLONLYDB";
    public static final String ETLVWDB = "$$ETLVWDB";
    public static final String EDWTD_BATCH_ID = "$$BATCH_ID";
    public static final String EDWTD_MAPPING_ID = "$$MAPPING_ID";
    public static final String EDWTD_STEP_NUMBER = "$$STEP_NUMBER";
    public static final String EDWTD_ACTION_CODE = "$$ACTION_CODE";
    public static final String EDWTD_CREATE_DATETIME = "$$CREATE_DATETIME";
    public static final String EDWTD_GLOBAL_NAME = "$$GLOBAL_NAME";
    public static final String XINE_VIRTUAL_HOST ="$$XINE_VIRTUAL_HOST";
    public static final String TD_SHARED_FOLDER ="$$TD_SHARED_FOLDER";
    public static final String XN_SHELL_SCRIPT_HOME ="$$XN_SHELL_SCRIPT_HOME";
    public static final String NF = "$$3NFDB";
    public static final String WORK = "$$WORKDB";
    public static final String AUTO = "AUTO";
    public static final String STR_REF = "REF#";
    public static final Map<String, Integer> HOST_TYPES;
	static {
		HOST_TYPES = new HashMap<String, Integer>();
		HOST_TYPES.put("ORACLE", 1);
		HOST_TYPES.put("TERADATA", 2);
		HOST_TYPES.put("HIVE-HS1", 3);
		HOST_TYPES.put("HIVE-HS2", 4);
		HOST_TYPES.put("MYSQL", 5);
		HOST_TYPES.put("MS-SQL-SERVER", 6);
	}
    public static final Map<String, Integer> ETL_TOOLS;
	static {
		ETL_TOOLS = new HashMap<String, Integer>();
		ETL_TOOLS.put("SHELL SCRIPT", 1);
		ETL_TOOLS.put("PYTHON", 2);
		ETL_TOOLS.put("BTEQ", 3);
		ETL_TOOLS.put("PL/SQL", 4);
		ETL_TOOLS.put("ORACLE SQL", 4);
		ETL_TOOLS.put("TERADATA SQL", 5);
		ETL_TOOLS.put("HIVEQL-HS1", 6);
		ETL_TOOLS.put("HIVEQL-HS2", 6);
		ETL_TOOLS.put("DRILL", 6);
		ETL_TOOLS.put("FTP", 7);
		ETL_TOOLS.put("SFTP", 8);
		ETL_TOOLS.put("HDPDL-DEP", 9);
		ETL_TOOLS.put("EDWTD-DEP", 9);
	}
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
	public static final String XINE_ENV;
	static{
		XINE_ENV = System.getProperty("XINE_ENV") == null ? "DEV" :  System.getProperty("XINE_ENV");
	}
	public static final Map<String, String> JOB_EXEC_STATUS;
	static {
		JOB_EXEC_STATUS = new HashMap<String, String>();
		JOB_EXEC_STATUS.put("HOLD", "HOLD");
		JOB_EXEC_STATUS.put("SKIP", "SKIP");
		JOB_EXEC_STATUS.put("PENDING", "PENDING");
		JOB_EXEC_STATUS.put("RUNNING", "RUNNING");
		JOB_EXEC_STATUS.put("COMPLETE", "COMPLETE");
		JOB_EXEC_STATUS.put("FAILED", "FAILED");
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
