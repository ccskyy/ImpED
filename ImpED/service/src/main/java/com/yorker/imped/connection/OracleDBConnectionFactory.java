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

public class OracleDBConnectionFactory extends AbstractDBConnectionFactory {
	
	private static final Logger logger = Logger.getLogger(OracleDBConnectionFactory.class);

	private String host;
	private int port;
	private String sid;
	private String username;
	private String password;
	private String service;
	private String errorMsg;
	
	public void setDBSuffix(String prefix){
	}
	
	public OracleDBConnectionFactory(String host,int port,String sid,String username,String password,String service){
		this.host = host;
		this.port = port;
		this.sid = sid;
		this.username = username;
		this.password = password;
		this.service = service;
	}
	public void getConnection() {
		if (host != null && port != 0 && sid != null && username != null && password != null){
		    connection = DBUtil.getOracleConnection(host,port,sid,username,password);
		}else if (host != null && port != 0 && service != null && username != null && password != null){
			connection = DBUtil.getOracleConnectionForServiceName(host,port,service,username,password);
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
			if (execStep.toUpperCase().startsWith("SELECT ")){
			   st =  connection.createStatement();
			   rs =  st.executeQuery(execStep);
			   System.out.println("Executing select statement:");
			   while (rs.next()) {
				   result = rs.getObject(1);
			   }
			}else if (execStep.startsWith("CALL ")) {
				ps.execute();
			} else if (execStep.startsWith("INSERT ")) {
				ps.executeUpdate();
			} else {
				if (execStep.indexOf("?") != -1){
					if (param != null){
						ps.setObject(1, param);
						System.out.println("executeStatement ......Given parameter::" + param);
					}else{
						errorMsg = "Parameters are empty. Sql execution is failed..";
					}
				}
				ps.executeUpdate();
			}
		}catch (Exception e) {
			long time = new Date().getTime();
			logger.error("Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + ImpedUtil.getErrorStackTrace(e));
			errorMsg = "Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + e.getMessage();
		}catch (Error e) {
			long time = new Date().getTime();
			logger.error("Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + ImpedUtil.getErrorStackTrace(e));
			errorMsg = "Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + e.getMessage();
		} finally {
			DBUtil.closeConnection(null, rs, ps, st);
		}
		return result;
	}
	
	@Override
	public int addRecords(String sql, List<List<Object>> recs, String strTime) {
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
				//ps.executeUpdate();
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
	
	@Override
	public List<Object> getAllRecords(String sql, int size) {
		PreparedStatement ps = null;
		List<Object> list = null;
		Statement st = null;
		ResultSet rs = null;
		try{
			ps = connection.prepareStatement(sql);
			list = new LinkedList<Object>();
			if (sql.trim().toUpperCase().startsWith("SELECT ")){
				 st =  connection.createStatement();
				 rs =  st.executeQuery(sql);
			   System.out.println("Executing select statement:");
			   while (rs.next()) {
				   for(int i=1;i<=size;i++){
					   list.add(rs.getObject(i));
				   }
			   }
			}
		}catch (SQLException e) {
			System.out.println("Error occured while getAllRecords ORACLE sql :" + e.getMessage());
		} finally {
			DBUtil.closeConnection(null, null, ps);
		}
		return list;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
	
}
