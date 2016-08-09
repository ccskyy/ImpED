package com.yorker.imped.connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.yorker.imped.util.DBUtil;
import com.yorker.imped.util.ImpedConstants;
import com.yorker.imped.util.ImpedUtil;

public class TeradataDBConnectionFactory extends AbstractDBConnectionFactory {

	private static final Logger logger = Logger.getLogger(TeradataDBConnectionFactory.class);
	
	private String host;
	private String username;
	private String password;
	private String suffix;
	private String errorMsg;
	private String authenticationType;
	
	public void setAuthenticationType(String type){
		this.authenticationType = type;
	}
	
	public void setDBSuffix(String suffix){
		this.suffix = suffix;
	}
	
	public TeradataDBConnectionFactory(String host,String username,String password){
		this.host = host;
		this.username = username;
		this.password = password;
	}
	
	public void getConnection() {
		if (host != null && username != null && password != null){
			if ("LDAP".equals(authenticationType)){
				connection = DBUtil.getTeradataConnectionUsingLDAP(host, username, password);
			}else{
				connection = DBUtil.getTeradataConnection(host, username, password);
			}
		}
	}
	
	public void closeConnection() {
		 DBUtil.closeConnection(connection,null,null);
	}
	
	public Object executeStatement(String execStep,String error,Object param) {
		PreparedStatement ps = null;
		Statement st = null;
		ResultSet rs = null;
		Object result = null;
		try{
			ps = connection.prepareStatement(execStep);
			if (execStep.trim().toUpperCase().startsWith("SELECT ")){
				 st =  connection.createStatement();
				 rs =  st.executeQuery(execStep);
			   System.out.println("Executing select statement:");
			   while (rs.next()) {
				   result = rs.getObject(1);
			   }
			}else if (execStep.trim().toUpperCase().startsWith("CALL ")) {
				ps.execute();
			} else if (execStep.trim().toUpperCase().startsWith("INSERT ")) {
				ps.executeUpdate();
			} else {
				ps.executeUpdate();
			}
		}catch (Exception e) {
			long time = new Date().getTime();
			logger.error("Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + ImpedUtil.getErrorStackTrace(e));
			error = "Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + e.getMessage();
			this.errorMsg = error;
		}catch (Error e) {
			long time = new Date().getTime();
			logger.error("Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + ImpedUtil.getErrorStackTrace(e));
			error = "Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + e.getMessage();
			this.errorMsg = error;
		}  finally {
			DBUtil.closeConnection(null, rs, ps, st);
		}
		return result;
	}
	
	public int addRecords(String sql, List<List<Object>> recs,String strTime) {
		PreparedStatement ps = null;
		int[] recCnt = null;
		try{
			sql = sql.replace("CURRENT_TIMESTAMP", "TO_TIMESTAMP('"+strTime+"','yyyy-mm-dd hh24:mi:ss')");
			ps = connection.prepareStatement(sql);
			int colCnt = 1;
			for (List<Object> row : recs){
				colCnt = 1;
				System.out.println("Row ==>  " + row);
				for (Object col : row){
					ps.setObject(colCnt, col);
					colCnt++;
				}
				ps.addBatch();
			}
			recCnt = ps.executeBatch();
		}catch (Exception e) {
			long time = new Date().getTime();
			logger.error("Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + ImpedUtil.getErrorStackTrace(e));
			this.errorMsg = "Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + e.getMessage();
		}catch (Error e) {
			long time = new Date().getTime();
			logger.error("Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + ImpedUtil.getErrorStackTrace(e));
			this.errorMsg = "Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + e.getMessage();
		}  finally {
			DBUtil.closeConnection(null, null, ps);
		}
		return recCnt.length;
	}
	
	public List<Object> getAllRecords(String execStep,int Size){
		PreparedStatement ps = null;
		List<Object> list = null;
		Statement st = null;
		ResultSet rs = null;
		try{
			ps = connection.prepareStatement(execStep);
			list = new LinkedList<Object>();
			if (execStep.trim().toUpperCase().startsWith("SELECT ")){
				 st =  connection.createStatement();
				 rs =  st.executeQuery(execStep);
			   System.out.println("Executing select statement:");
			   while (rs.next()) {
				   for(int i=1;i<=Size;i++){
					   list.add(rs.getObject(i));
				   }
			   }
			}
		}catch (SQLException e) {
			System.out.println("Error occured while getAllRecords sql :" + e.getMessage());
		} finally {
			DBUtil.closeConnection(null, null, ps);
		}
		return list;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

}
