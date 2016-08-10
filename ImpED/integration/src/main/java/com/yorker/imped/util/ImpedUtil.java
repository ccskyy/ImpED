package com.yorker.imped.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;

import com.yorker.imped.exceptions.ImpedException;

public class ImpedUtil {
	
	private static final Logger logger = Logger.getLogger(ImpedUtil.class);
	private static Properties envProperties = null;
	private static Map<String,Properties> envMap = null;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

	public static String getHostName(){
		 String hostName = "";
		try {
			InetAddress iAddress = InetAddress.getLocalHost();
			hostName = iAddress.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	     return hostName;
	}

	public static String convertTimeToStrng(){
	    return dateFormat.format(getCurrentTimeStamp());
	}

	public static Timestamp convertStringToDate(String s) throws ParseException {
		 Date dt = dateFormat.parse(s);
		 dt.getTime();
		 return getCurrentTimeStamp(dt.getTime());
	}

	public static String convertDateToString(String s) throws ParseException{
		Date date = (Date)formatter.parse(s);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String formatedDate = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" +  cal.get(Calendar.YEAR);
		return formatedDate;
	}

	public static String getEnvProperty(String key){
		return envProperties.getProperty(key);
	}

	public static String getEnvProperty(String envName,String key){
		return envMap.get(envName).getProperty(key);
	}

	public static String[] split(String str,String delimeter){
		String[] result = null;
		if (str != null && delimeter != null){
			result = str.split(delimeter);
		}
		return result;
	}

	public static void createFolder(String dir){
		File f = new File(dir);
		if (!f.exists()){
			f.mkdirs();
		}
	}

	public static boolean isFileExist(String dir){
		File f = new File(dir);
		return f.exists();
	}

	public static Timestamp getCurrentTimeStamp() {
		Date today = new Date();
		return new Timestamp(today.getTime());
	}

	public static Timestamp getCurrentTimeStamp(long millisec) {
		return new Timestamp(millisec);
	}

	public static String getTempDirectory() {
	  return System.getProperty("java.io.tmpdir");
	}

	public static String getTempFileName(String fileSuffix) {
		return (getTempDirectory() + "/table_recds_" + ImpedUtil.getCurrentTimeStamp().getTime() + "_" + fileSuffix + ".out");
	}

	public static String getErrorStackTrace(Exception exception) {
		StringWriter sw = new StringWriter();
		exception.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public static String getErrorStackTrace(Error error) {
		StringWriter sw = new StringWriter();
		error.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public static String formatNumber(String pattern,Object number){
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat.format(number);
	}
	
	public static String generateExpressionForGrp(String exp,Map<String, String> mappingGrpVSStatus){
		String jsExpression = null;
		char[] tokens = null;
		StringBuffer charSet = null;
		Stack<Character> valueSet = null;
		String status = null;
		if(exp != null){
			tokens = exp.toCharArray();
			charSet = new StringBuffer();
			valueSet = new Stack<Character>();
			for (int i = 0; i < tokens.length; i++){
				 if (tokens[i] == '('){
					 charSet.append(tokens[i]);
					 valueSet.clear();
				 }else if (tokens[i] == ')'){
						 status = mappingGrpVSStatus.get(valueSet.toString());
						 if (status.equals("COMPLETE")){
							 charSet.append("true");
						 }else{
							 charSet.append("false");
						 }
						 charSet.append(tokens[i]);
						 valueSet.clear();
				 }else{
					 valueSet.push(tokens[i]);
				 }
			}
		}
		System.out.println("Mapping Group Expression ::" + exp + "  jsExpression::" + jsExpression);
		return jsExpression;
	}

	public static String generateNewExpression(String exp,Map<Long, Long> oldIdVsNewId){
		boolean numFlagStart = false;
		boolean numFlagEnd = false;
		char[] tokens = null;
		StringBuffer charSet = null;
		StringBuffer valueSet = null;
		Long status = null;
		if(exp != null){
			tokens = exp.toCharArray();
			charSet = new StringBuffer();
			valueSet = new StringBuffer();
			for (int i = 0; i < tokens.length; i++){
				 if (tokens[i] >= '0' && tokens[i] <= '9'){
					 if (!numFlagStart){
						 numFlagStart = true;
						 numFlagEnd = false;
					 }
					 valueSet.append(tokens[i]);
				 }else{
					 if (numFlagStart){
						 numFlagStart = false;
						 numFlagEnd = true;
					 }
					 if (numFlagEnd){
						 status = oldIdVsNewId.get(Long.parseLong(valueSet.toString()));
						 charSet.append(status);
						 valueSet = new StringBuffer();
						 numFlagEnd = false;
					 }
					 charSet.append(tokens[i]);
				 }
			}
		}
		System.out.println("Mapping ID Expression ::" + exp + "  new Expression::" + charSet);
		return charSet.toString();
	}

	public static String generateExpression(String exp,Map<Long, String> mappingIdVSStatus){
		boolean numFlagStart = false;
		boolean numFlagEnd = false;
		char[] tokens = null;
		StringBuffer charSet = null;
		StringBuffer valueSet = null;
		String status = null;
		if(exp != null){
			tokens = exp.toCharArray();
			charSet = new StringBuffer();
			valueSet = new StringBuffer();
			for (int i = 0; i < tokens.length; i++){
				 if (tokens[i] >= '0' && tokens[i] <= '9'){
					 if (!numFlagStart){
						 numFlagStart = true;
						 numFlagEnd = false;
					 }
					 valueSet.append(tokens[i]);
				 }else{
					 if (numFlagStart){
						 numFlagStart = false;
						 numFlagEnd = true;
					 }
					 if (numFlagEnd){
						 status = mappingIdVSStatus.get(Long.parseLong(valueSet.toString()));
						 if (status != null && status.equals("COMPLETE")){
							 charSet.append("true");
						 }else{
							 charSet.append("false");
						 }
						 valueSet = new StringBuffer();
						 numFlagEnd = false;
					 }
					 charSet.append(tokens[i]);
				 }
			}
		}
		System.out.println("Mapping ID Expression ::" + exp + "  jsExpression::" + charSet);
		return charSet.toString();
	}
	
	public static boolean evaluateExpression(String exp){
		Boolean flag = false;
		ScriptEngineManager mgr = new ScriptEngineManager();
	    ScriptEngine engine = mgr.getEngineByName("JavaScript");
	    try {
			flag = (Boolean) engine.eval(exp);
		} catch (ScriptException e) {
			logger.error("Could not able to evaluate expression :: " + exp + " Error message::" + getErrorStackTrace(e));
		}
		return flag.booleanValue();
	}
	
	public static void close(FileInputStream fstream,DataInputStream in,BufferedReader br) {
		try {
			if (br != null){
				br.close();
			}
			if (in != null){
				in.close();
			}
			if (fstream != null){
				fstream.close();
			}
		}
		 catch (IOException e) {
				logger.error("Exception occured while closing out file. Error message:" + getErrorStackTrace(e));
		 }
	 }

	public static Properties readPropsFile(String propFile) throws ImpedException {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			fis = new FileInputStream(propFile);
			props.load(fis);

		} catch (Exception e) {
			throw new ImpedException(e.getMessage(),ImpedConstants.ERROR_CODES_RESOL.get("ERROR-501"));
		}
		return props;
	}

	public static String readFile(String logFile) throws ImpedException {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(logFile)))
		{
			String strLine;
			while ((strLine = br.readLine()) != null) {
				sb.append(strLine).append("\n");
			}

		} catch (IOException e) {
			throw new ImpedException(e.getMessage(),ImpedConstants.ERROR_CODES_RESOL.get("ERROR-501"));
		}
		return sb.toString();
	}

	public static String getErrorMessage(Object[] message) {
		String error = message[0].toString();
		return error.substring(error.indexOf(": ORA-"), error.length());
	}

	public static String getConnectionErrorMessage(Object[] message) {
		String lines[] = message[0].toString().split("\\r?\\n");
		return lines[0];
	}
	
}
