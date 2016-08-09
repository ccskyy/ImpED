package com.yorker.imped.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.yorker.imped.util.DBUtil;
import com.yorker.imped.util.XINEConstants;
import com.yorker.imped.util.XINEUtil;

public class MySQLDBConnectionFactory  extends AbstractDBConnectionFactory{

	private static final Logger logger = Logger.getLogger(MySQLDBConnectionFactory.class);

	private String host;
	private String username;
	private String password;
	private long port;
	
	public void setDBSuffix(String prefix){
	}
	
	public MySQLDBConnectionFactory(String host, long port,String username,String password){
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}
	
	public static Connection getConnection(String host,long port,String username,String password) {
		Connection conn = null;
		String URL = "jdbc:mysql://" + host + ":" + port ;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, username,password);
		} catch (SQLException e) {
			logger.error("Got error while creating mysql connection." + XINEUtil.getErrorStackTrace(e));
		}catch (Exception e) {
			logger.error("Got error while creating mysql connection." + XINEUtil.getErrorStackTrace(e));
		}
		return conn;
	}
	
	public void getConnection() {
		if (host != null && username != null && password != null && port != 0){
			connection =  getConnection(host,port,username,password);
		}
	}

	@Override
	public void closeConnection() {
		DBUtil.closeConnection(connection,null,null);
	}
	
	public List<String> getSchemas(String filter) {
		List<String> schemas = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String sql = "SELECT TRIM(UPPER(D.NAME)) AS DATABASE_NAME FROM hive.DBS D";
		try{
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setFetchSize(XINEConstants.REC_FETCH_SIZE);
		rs = preparedStatement.executeQuery();
		schemas = new ArrayList<String>();
		while (rs.next()) {
			schemas.add(rs.getString("DATABASE_NAME"));
		}
		}catch (Exception e) {
			logger.error("Exception occured while getting getSchemas()" + e.getMessage());
		} finally {
			DBUtil.closeConnection(connection,rs,preparedStatement);
			logger.debug("Exiting getSchemas method ....");
		}
		return schemas;
	}


	@Override
	public Object executeStatement(String sql, String error, Object param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addRecords(String sql, List<List<Object>> list, String error) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Object> getAllRecords(String sql, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMsg() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
