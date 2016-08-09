package com.yorker.imped.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;

import com.yorker.imped.exceptions.XINEException;

public class XINEUtil {
	private static final Logger logger = Logger.getLogger(XINEUtil.class);
	private static Properties envProperties = null;
	private static Map<String,Properties> envMap = null;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

	static {
		String fileName = null;
		FileInputStream fis = null;
		String envName = System.getProperty("xine.env");
		String envType = System.getProperty("xine.env.type");
		Properties env = null;

		if (envName != null){
			fileName = "xine." + envName + ".properties";
		}
		try {
			if (fileName != null){
				System.out.println("Loading xine environment properties....." + fileName);
				envProperties = new Properties();
				if (System.getProperty("XINE_HOME_DIR") != null){
					fis = new FileInputStream(System.getProperty("XINE_HOME_DIR") + "/xine.common.properties");
					envProperties.load(fis);
					fis.close();
					fis = new FileInputStream(System.getProperty("XINE_HOME_DIR") + "/" + fileName);
					envProperties.load(fis);
				}else{
					envProperties.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.common.properties"));
					envProperties.load(XINEUtil.class.getClassLoader().getResourceAsStream(fileName));
				}
				System.out.println("Loaded xine environment properties....."+envProperties);
			}
			if (envType != null){
				envMap = Collections.synchronizedMap(new HashMap<String,Properties>());
				envProperties = new Properties();
				envProperties.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.common.properties"));
				switch (envType){
					case "DEV" :
						env = new Properties();
						env.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.DV1.properties"));
						envMap.put("DV1", env);
						env = new Properties();
						env.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.DV2.properties"));
						envMap.put("DV2", env);
						env = new Properties();
						env.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.DV3.properties"));
						envMap.put("DV3", env);
						env = new Properties();
						env.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.DV4.properties"));
						envMap.put("DV4", env);
						envProperties.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.DEV.properties"));
						break;
					case "TEST" :
						env = new Properties();
						env.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.TS1.properties"));
						envMap.put("TS1", env);
						env = new Properties();
						env.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.TS2.properties"));
						envMap.put("TS2", env);
						env = new Properties();
						env.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.TS3.properties"));
						envMap.put("TS3", env);
						env = new Properties();
						env.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.TST.properties"));
						envMap.put("TST", env);
						envProperties.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.TEST.properties"));
						break;
					case "PRODUCTION":
						env = new Properties();
						env.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.PROD1.properties"));
						envMap.put("PROD1", env);
						envProperties.load(XINEUtil.class.getClassLoader().getResourceAsStream("xine.PRODUCTION.properties"));
				}
				System.out.println("Loaded xine environment properties....."+envMap);
			}
		} catch (FileNotFoundException e) {
			System.out.println("xine environment file not found ....");
			e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("Not able to load xine environment file ....");
			e.printStackTrace();
		}finally{
			close(fis, null, null);
		}
	}

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
		return (getTempDirectory() + "/table_recds_" + XINEUtil.getCurrentTimeStamp().getTime() + "_" + fileSuffix + ".out");
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
	public static FileOutputStream getTempFile(String dataFile) {
		FileOutputStream out = null;
		try{
			File file = new File(dataFile);
		    out = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
		}catch(Exception e){
			logger.error("Exception occured while creating file. " + e.getMessage());
		}
		return out;
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

	public static String triggerWorkFlow(String systemCall,String logFile) {
		int exitCode = 1;
		String errorList = "";
		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", systemCall + " > " + logFile);
			Process shell = pb.start();
			exitCode = shell.waitFor();
			System.out.println("SystemCall:: " + systemCall + "  ===> Is it succeded::" + exitCode);
			if (exitCode > 0){
				errorList = readFile(logFile);
				if (errorList.isEmpty()){
					errorList = "Log file is empty. But workflow execution return code:" + exitCode;
				}
			}
		}
		catch (Exception e) {
			logger.error("Exception occured while invoking triggerWorkFlow ::" + e.getMessage());
			errorList ="Exception occured while invoking triggerWorkFlow ::" + e.getMessage();
		}
		return errorList;
	}

	public static String triggerShellScript(String systemCall,String logFile) {
		int exitCode = 1;
		String errorList = "";
		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", systemCall + " > " + logFile);
			Process shell = pb.start();
			exitCode = shell.waitFor();
			System.out.println("SystemCall:: " + systemCall + "  ===> Is it succeded::" + exitCode);
			if (exitCode > 0){
				errorList = readFile(logFile);
				if (errorList.isEmpty()){
					errorList = "Log file is empty. But Shell script return code:" + exitCode;
				}
			}
		}
		catch (Exception e) {
			logger.error("Exception occured while invoking triggerShellScript ::" + e.getMessage());
			errorList = "Exception occured while invoking triggerShellScript ::" + e.getMessage();
		}
		return errorList;
	}

	/**
	 * Utility api to trigger given shell script
	 * @param systemCall - shell script
	 */
	public static void triggerShellScript(String systemCall,boolean isSynchronous) {
		try {
			int exitCode = 1;
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", systemCall);
			Process shell = pb.start();
			if (isSynchronous) {
				exitCode = shell.waitFor();
				System.out.println("Mail triggering ==> Is it succeded::" + exitCode);
			}
		} catch (IOException e) {
			logger.error("Exception occured while invoking triggerShellScript ::" + e.getMessage());
		}
		catch (Exception e) {
			logger.error("Exception occured while invoking triggerShellScript ::" + e.getMessage());
		}
	}

	public static void triggerEmail(String body,String subject,String toAddress,String ccAddress,boolean textBasedMail) {
		String cmd = null;
		if (textBasedMail){
			if (body != null && subject != null && toAddress != null){
				if (ccAddress != null && !ccAddress.isEmpty()){
					cmd = "echo \"" + body + "\" | mail -s \"" + subject + "\" -c " + ccAddress + " " + toAddress;

				}else {
					cmd = "echo \"" + body + "\" | mail -s \"" + subject + "\" " + toAddress;
				}

			}
		}else{
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			sb.append("echo \"From: no-reply@cisco.com\"\n");
			sb.append("echo \"To: ").append(toAddress).append("\"\n");
			if(ccAddress != null) {
				sb.append("echo \"Cc: ").append(ccAddress).append("\"\n");
			}
			sb.append("echo \"Subject:").append(subject).append("\"\n");
			sb.append("echo \"Content-Type: text/html\"\n");
			sb.append("echo \"MIME-Version: 1.0\"\n");

			sb.append("echo \"").append(body).append("\"\n");
			sb.append(") | /usr/sbin/sendmail -t");
			cmd = sb.toString();
		}
		triggerShellScript(cmd,true);
	}

	public static void triggerEmailWithAttachment(String body,String subject,String toAddress,String fileAttachment) {
		String cmd = null;
		StringBuilder sb = new StringBuilder();
		sb.append("v_mailpart=\"$(uuidgen)/$(hostname)\"").append("\n");
		sb.append("echo \"To: ").append(toAddress).append("\n");
		sb.append("From: no-reply@cisco.com").append("\n");
		sb.append("Subject: ").append(subject).append("\n");
		sb.append("Content-Type: multipart/mixed; boundary=\"$v_mailpart\"").append("\n");
		sb.append("MIME-Version: 1.0").append("\n\n");
		sb.append("This is a multi-part message in MIME format.").append("\n");
		sb.append("--$v_mailpart").append("\n");
		sb.append("Content-Type: text/html").append("\n");
		sb.append("Content-Disposition: inline").append("\n\n");
		sb.append(body).append("\n");
		sb.append("--$v_mailpart").append("\n");
		sb.append("Content-Type: text/plain; name=ErrorLog.txt").append("\n");
		sb.append("Content-Disposition: attachment; filename=ErrorLog.txt").append("\n\n");
		sb.append("`cat ").append(fileAttachment).append("`").append("\n");
		sb.append("--$v_mailpart--\" | /usr/sbin/sendmail -t");
		cmd = sb.toString();
		triggerShellScript(cmd,true);
	}
	
	public static String triggerPython(String systemCall,String logFile) {
		int exitCode = 1;
		StringBuffer errorList = new StringBuffer();
		try {
			ProcessBuilder pb = new ProcessBuilder("python", systemCall + " > " + logFile);
			Process shell = pb.start();
			exitCode = shell.waitFor();
			logger.debug("SystemCall:: " + systemCall + "  ===> Is it succeded::" + exitCode);
			System.out.println("SystemCall:: " + systemCall + "  ===> Is it succeded::" + exitCode);
			if (exitCode > 0){
				errorList.append(readFile(logFile));
			}
		} 
		catch (Exception e) {
			logger.error("Exception occured while invoking triggerPython ::"
					+ e.getMessage());
			errorList.append("Exception occured while invoking triggerPython ::"
					+ e.getMessage());
		}
		return errorList.toString();
	}

	public static String triggerFastload(String ctrlFile, String logFile) {
		int exitCode = 1;
		StringBuffer errorList = new StringBuffer();
		try {
			String cmd = null;
			String osName = System.getProperty("os.name");
			if (osName != null && osName.contains("Windows")) {
				cmd = "cmd /c fastload < " + ctrlFile + " > " + logFile;
			} else {
				cmd = "sh -c \"fastload < " + ctrlFile + " > " + logFile + "\"";
			}
			logger.debug("FastLoad command : " + cmd);
			Process p = Runtime.getRuntime().exec(cmd);
			logger.debug("after invoking process.....");
			exitCode = p.waitFor();
			logger.debug("Is it succeded::" + exitCode);

			if (exitCode > 0){
				errorList.append(readFile(logFile));
			}
		}
		catch (Exception e) {
			logger.error("Exception occured while invoking fastload ::"
					+ e.getMessage());
			errorList.append("Exception occured while invoking fastload ::"
					+ e.getMessage());
		}
		return errorList.toString();
	}

	public static String triggerTPT(String envName,String ctrlFile, String logFile) {
		int exitCode = 1;
		String cmd = null;
		StringBuffer errorList = new StringBuffer();
		try {
			triggerShellScript("rm " + getEnvProperty("xine.TD.checkpoint.file"),true);
			cmd = "tbuild -f " + ctrlFile + " -v " + XINEUtil.getEnvProperty(envName,"xine.TD.jobvariable.folder") + "/job_variable_file.txt";
			System.out.println("Command " + cmd + " > " + logFile);
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd + " > " + logFile);
			Process shell = pb.start();
			exitCode = shell.waitFor();
			logger.debug("after invoking process.....return code:" + exitCode);
			System.out.println("after invoking process.....return code:" + exitCode);
			if (exitCode > 0){
				errorList.append(readFile(logFile));
			}
		}
		catch (Exception e) {
			logger.error("Exception occured while invoking triggerTPT ::"
					+ e.getMessage());
			errorList.append("Exception occured while invoking triggerTPT ::"
					+ e.getMessage());
		}
		return errorList.toString();
	}

	public static String triggerSqlLoader(String ctrlFile,String paramFile, String logFile) {
		int exitCode = 1;
		String cmd = null;
		StringBuffer errorList = new StringBuffer();
		try {
			cmd = getEnvProperty("$$XN_SHELL_SCRIPT_HOME") + "/triggerSQLLoader.sh " + ctrlFile + " " + paramFile;
			System.out.println("Command " + cmd + " > " + logFile);
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd + " > " + logFile);
			Process shell = pb.start();
			exitCode = shell.waitFor();
			logger.debug("after invoking process.....return code:" + exitCode);
			System.out.println("after invoking process.....return code:" + exitCode);
			if (exitCode > 0){
				errorList.append(readFile(logFile));
			}
		}
		catch (Exception e) {
			logger.error("Exception occured while invoking triggerSqlLoader ::"
					+ e.getMessage());
			errorList.append("Exception occured while invoking triggerSqlLoader ::"
					+ e.getMessage());
		}
		return errorList.toString();
	}

	public static Properties readPropsFile(String propFile) throws XINEException {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			fis = new FileInputStream(propFile);
			props.load(fis);

		} catch (Exception e) {
			throw new XINEException(e.getMessage(),XINEConstants.ERROR_CODES.get("ERROR-500"));
		}
		return props;
	}

	public static String readFile(String logFile) throws XINEException {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(logFile)))
		{
			String strLine;
			while ((strLine = br.readLine()) != null) {
				sb.append(strLine).append("\n");
			}

		} catch (IOException e) {
			throw new XINEException(e.getMessage(),XINEConstants.ERROR_CODES.get("ERROR-501"),XINEConstants.ERROR_CODES_RESOL.get("ERROR-501"));
		}
		return sb.toString();
	}

	private static long fetchRowsAppliedFromSqlLoaderLog(String logFile) throws XINEException {
		long recTransferred = -1;
		try (BufferedReader br = new BufferedReader(new FileReader(logFile)))
		{
			String strLine, records;
			while ((strLine = br.readLine()) != null) {
				if (strLine.indexOf(" logical record count") != -1){
					int pos = strLine.indexOf(" logical record count");
					records = strLine.substring(pos+22);
					records = records.trim();
					recTransferred = new Long(records);
					break;
				}
			}

		} catch (Exception e) {
			throw new XINEException(e.getMessage(),XINEConstants.ERROR_CODES.get("ERROR-501"),XINEConstants.ERROR_CODES_RESOL.get("ERROR-501"));
		}
		return recTransferred;
	}
	public static long fetchRowsAppliedFromLog(String hostType,String logFile) throws XINEException{
		long recTransferred = -1;
		switch (hostType){
			case "TERADATA" :
				recTransferred = fetchRowsAppliedFromTPTLog(logFile);
				break;
			case "ORACLE" :
				recTransferred = fetchRowsAppliedFromSqlLoaderLog(logFile);
				break;
		}
		return recTransferred;
	}

	private static long fetchRowsAppliedFromTPTLog(String logFile) throws XINEException {
		long recTransferred = -1;
		try (BufferedReader br = new BufferedReader(new FileReader(logFile)))
		{
			String strLine, records;
			while ((strLine = br.readLine()) != null) {
				// for fastload case. or sql inserter
				if (strLine.startsWith("LOADOPERATOR: Total Rows Applied:") || strLine.startsWith("SQL_INSERTER: Total Rows Applied:")){
					records = strLine.substring(34);
					records = records.trim();
					recTransferred = new Long(records);
					break;
				}
				// for stream case. if no. of records <= 10 K
				if (strLine.startsWith("STREAMOPERATOR: Rows Inserted:") || strLine.startsWith("STREAMOperator: Rows Inserted:")){
					records = strLine.substring(31);
					records = records.trim();
					recTransferred = new Long(records);
					break;
				}
				// for export case. teradata to hadoop
				if (strLine.startsWith("Export_SOURCE_SYSTEM_CODE: Total Rows Exported:")){
					records = strLine.substring(48);
					records = records.trim();
					recTransferred = new Long(records);
					break;
				}
			}

		} catch (IOException e) {
			throw new XINEException(e.getMessage(),XINEConstants.ERROR_CODES.get("ERROR-501"),XINEConstants.ERROR_CODES_RESOL.get("ERROR-501"));
		}
		return recTransferred;
	}

	/**
	 * Write file.
	 *
	 * @param processfile the path where you want to create file
	 * @param fileContent the file content
	 * @throws XINEException the XINE exception
	 */
	public static void writeFile(String processfile, String fileContent) throws XINEException {
		File file = new File(processfile);
		try (FileOutputStream fop = new FileOutputStream(file)) {
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = fileContent.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			throw new XINEException(e.getMessage(),XINEConstants.ERROR_CODES.get("ERROR-500"));
		}
	}
	
	public static String triggerBteq(String systemCall, String logFile) {
		int exitCode = 1;
		StringBuffer errorList = new StringBuffer();
		try {
			ProcessBuilder pb = new ProcessBuilder("sh","-c", systemCall + " > " + logFile);
			System.out.println("SystemCall:: " + systemCall);
			Process shell = pb.start();
			logger.debug("after invoking process.....");
			System.out.println("after invoking process.....");
			exitCode = shell.waitFor();
			logger.debug("Is it succeded::" + exitCode);
			System.out.println("Is it succeded::" + exitCode);
			if (exitCode > 0){
				errorList.append(readFile(logFile));
			}
		}
		catch (Exception e) {
			logger.error("Exception occured while invoking triggerBteq ::" + e.getMessage());
			errorList.append("Exception occured while invoking triggerBteq ::" + e.getMessage());
		}
		return errorList.toString();
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
