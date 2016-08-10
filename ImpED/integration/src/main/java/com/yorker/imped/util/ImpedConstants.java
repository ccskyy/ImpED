package com.yorker.imped.util;

import java.util.HashMap;
import java.util.Map;

public class ImpedConstants {
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

	public static final Map<String, String> ERROR_CODES_RESOL;
	static {
		ERROR_CODES_RESOL = new HashMap<String, String>();
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
}
