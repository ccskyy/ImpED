package com.yorker.imped.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class DBUtil {

	private static final Logger logger = Logger.getLogger(DBUtil.class);
	
	private DataSource dataSource;

	private DataSource teraDataSource;

	private Map<String,DataSource> dsMaps;
	
	private static Map<String, Integer> map;
	private static Properties sqlProperties = null;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static SimpleDateFormat tdDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/*static {
		String fileName = "sql.properties";
		try {
			if (fileName != null){
				System.out.println("Loading sql properties....." + fileName);
				sqlProperties = new Properties();
				sqlProperties.load(XINEUtil.class.getClassLoader().getResourceAsStream(fileName));
				System.out.println("Loaded sql properties....."+sqlProperties);
			}
		} catch (FileNotFoundException e) {
			System.out.println("sql properties file not found ....");
		} 
		catch (IOException e) {
			System.out.println("Not able to load sql properties file ....");
		}
	}
	
	static{
		map = new HashMap<String, Integer>();
		Field[] fields = java.sql.Types.class.getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				String name = fields[i].getName();
				Integer value = (Integer) fields[i].get(null);
				map.put(name, value);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		map.put("TIMESTAMP(0)", Types.TIMESTAMP);
		System.out.println("MAP::::" + map);
  }*/
	
	public DataSource getDataSource(String key){
		return dsMaps.get(key);
	}
	
	public DataSource getTeraDataSource() {
		return teraDataSource;
	}

	public Map<String, DataSource> getDsMaps() {
		return dsMaps;
	}

	public void setDsMaps(Map<String, DataSource> dsMaps) {
		this.dsMaps = dsMaps;
	}

	public void setTeraDataSource(DataSource teraDataSource) {
		this.teraDataSource = teraDataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public static String getSqlProperty(String key){
		return sqlProperties.getProperty(key);
	}
	public static String convertToStrng(Timestamp dt){
	    return dateFormat.format(dt);
	}
	
	public static String convertTimeToStrng4TD(){
	    return tdDateFormat.format(getCurrentTimeStamp());
	}
	
	public static Timestamp convertToTimestamp(String dt){
	    Date parsedDate = null;
		try {
			parsedDate = dateFormat.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return new Timestamp(parsedDate.getTime());
	}
	public static Timestamp getCurrentTimeStamp() {
		Date today = new Date();
		return new Timestamp(today.getTime());
	}
	public static int getDBType(String dbType){
		String tmpDbType = dbType;
		if (dbType.equals("VARCHAR2")){
			tmpDbType = "VARCHAR";
		}else if (dbType.equals("NUMBER")){
			tmpDbType = "INTEGER";
		}
		return map.get(tmpDbType);
	}
	
	public static Connection getOracleConnection(String host,int port,String sid,String username,String password){
		Connection conn = null;
		Driver driver = null;
		Properties properties = null;
		String URL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
		try {
			properties = new Properties();
			properties.put("user", username);
			properties.put("password", password);
			//properties.put("oracle.jdbc.allowedLogonVersion", 8);
			driver = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver",true, DBUtil.class.getClassLoader()).newInstance();
			conn = driver.connect(URL, properties);
		} catch (SQLException e) {
			logger.error("Got error while creating oracle connection." + XINEUtil.getErrorStackTrace(e));
		} catch (Exception e) {
			logger.error("Got error while creating oracle connection." + XINEUtil.getErrorStackTrace(e));
		}
		return conn;
	}
	
	public static Connection getOracleConnectionForServiceName(String host,int port,String serviceName,String username,String password){
		Connection conn = null;
		Driver driver = null;
		Properties properties = null;
		String URL = "jdbc:oracle:thin:@" + host + ":" + port + "/"	+ serviceName;
		try {
			properties = new Properties();
			properties.put("user", username);
			properties.put("password", password);
			//properties.put("oracle.jdbc.allowedLogonVersion", 8);
			driver = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver",true, DBUtil.class.getClassLoader()).newInstance();
			conn = driver.connect(URL, properties);
		} catch (SQLException e) {
			logger.error("Got error while creating oracle connection." + XINEUtil.getErrorStackTrace(e));
		} catch (Exception e) {
			logger.error("Got error while creating oracle connection." + XINEUtil.getErrorStackTrace(e));
		}
		return conn;
	}
	
	public static Connection getTeradataConnection(String host,String username,String password){
		Connection conn = null;
		String URL = "jdbc:teradata://" + host + "/TMODE=BTET,CHARSET=UTF8";
		try {
			Class.forName("com.teradata.jdbc.TeraDriver");
			conn = DriverManager.getConnection(URL, username,password);
		} catch (SQLException e) {
			logger.error("Got error while creating teradata connection." + XINEUtil.getErrorStackTrace(e));
		}catch (Exception e) {
			logger.error("Got error while creating teradata connection." + XINEUtil.getErrorStackTrace(e));
		}
		return conn;
	}
	
	public static Connection getTeradataConnection(String host,String username,String password,String charset){
		Connection conn = null;
		String URL = "jdbc:teradata://" + host + "/TMODE=BTET,CHARSET=UTF8";
		if (charset != null && !charset.isEmpty()){
			URL = "jdbc:teradata://" + host + "/TMODE=BTET,CHARSET="+charset;
		}
		try {
			Class.forName("com.teradata.jdbc.TeraDriver");
			conn = DriverManager.getConnection(URL, username,password);
		} catch (SQLException e) {
			logger.error("Got error while creating teradata connection." + XINEUtil.getErrorStackTrace(e));
		}catch (Exception e) {
			logger.error("Got error while creating teradata connection." + XINEUtil.getErrorStackTrace(e));
		}
		return conn;
	}
	
	public static Connection getTeradataConnectionUsingLDAP(String host,String username,String password){
		Connection conn = null;
		String URL = "jdbc:teradata://" + host + "/TMODE=BTET,CHARSET=UTF8,LOGMECH=LDAP";
		try {
			Class.forName("com.teradata.jdbc.TeraDriver");
			conn = DriverManager.getConnection(URL, username,password);
		} catch (SQLException e) {
			logger.error("Got error while creating teradata connection." + XINEUtil.getErrorStackTrace(e));
		}catch (Exception e) {
			logger.error("Got error while creating teradata connection." + XINEUtil.getErrorStackTrace(e));
		}
		return conn;
	}
	
	public static void closeConnection(Connection con,ResultSet rs,PreparedStatement ps){
		if (rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Error occured while closing the ResultSet. Error Message::" + e.getMessage());
			}
		}
		if (ps != null){
			try {
				ps.close();
			} catch (SQLException e) {
				logger.error("Error occured while closing the PreparedStatement. Error Message::" + e.getMessage());
			}
		}
		if (con != null){
			try {
				con.close();
				con = null;
			} catch (SQLException e) {
				logger.error("Error occured while closing the connection. Error Message::" + e.getMessage());
			}
		}
	}
	public static void closeConnection(Connection con,ResultSet rs,PreparedStatement ps,Statement stmt){
		if (rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Error occured while closing the ResultSet. Error Message::" + e.getMessage());
			}
		}
		if (stmt != null){
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("Error occured while closing the PreparedStatement. Error Message::" + e.getMessage());
			}
		}
		if (ps != null){
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println("Closing PreparedStatement");
				logger.error("Error occured while closing the PreparedStatement. Error Message::" + e.getMessage());
			}
		}
		if (con != null){
			try {
				con.close();
			} catch (SQLException e) {
				logger.error("Error occured while closing the connection. Error Message::" + e.getMessage());
			}
		}
	}

	public static Connection getHiveConnection(String host,long port,String username,String password) {
		Connection conn = null;
		String URL = "jdbc:hive://" + host + ":" + port;
		try {
			Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
			conn = DriverManager.getConnection(URL, username,password);
		} catch (SQLException e) {
			System.out.println("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
			logger.error("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
		}catch (Exception e) {
			System.out.println("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
			logger.error("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
		}
		return conn;
	}
	
	public static Connection getHiveHS1Connection(String host,long port,String username,String password) {
		Connection conn = null;
		String URL = "jdbc:hive://" + host + ":" + port + "/";
		try {
			Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
			conn = DriverManager.getConnection(URL, username,password);
		} catch (SQLException e) {
			System.out.println("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
			logger.error("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
		}catch (Exception e) {
			System.out.println("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
			logger.error("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
		}
		return conn;
	}
	
	public static Connection getHiveHS2Connection(String host,long port,String username,String password) {
		Connection conn = null;
		String URL = "jdbc:hive2://" + host + ":" + port;
		try {
			Class.forName("org.apache.hive.jdbc.HiveDriver");
			conn = DriverManager.getConnection(URL, username,password);
		} catch (SQLException e) {
			System.out.println("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
			logger.error("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
		}catch (Exception e) {
			System.out.println("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
			logger.error("Got error while creating hive connection." + XINEUtil.getErrorStackTrace(e));
		}
		return conn;
	}
	
	public static Connection getMySqlConnection(String host,long port,String username,String password) {
		Connection conn = null;
		String URL = "jdbc:mysql://" + host + ":" + port ;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, username,password);
		} catch (SQLException e) {
			System.out.println("Got error while creating mysql connection." + XINEUtil.getErrorStackTrace(e));
			logger.error("Got error while creating mysql connection." + XINEUtil.getErrorStackTrace(e));
		}catch (Exception e) {
			System.out.println("Got error while creating mysql connection." + XINEUtil.getErrorStackTrace(e));
			logger.error("Got error while creating mysql connection." + XINEUtil.getErrorStackTrace(e));
		}
		return conn;
	}
	
	public static Connection getMSSqlServerConnection(String host,long port,String username,String password) {
		Connection conn = null;
		String URL = "jdbc:sqlserver://" + host;
		if (port > 0){
			URL = "jdbc:sqlserver://" + host + ":" + port ;
		}
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(URL, username,password);
		}catch (Exception e) {
			logger.error("Got error while creating sql server connection." + XINEUtil.getErrorStackTrace(e));
		}
		return conn;
	}
}
