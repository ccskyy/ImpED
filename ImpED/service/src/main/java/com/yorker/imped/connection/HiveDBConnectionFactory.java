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
import com.yorker.imped.util.FileUtil;
import com.yorker.imped.util.ImpedConstants;
import com.yorker.imped.util.ImpedUtil;

public class HiveDBConnectionFactory  extends AbstractDBConnectionFactory{
	
	private static final Logger logger = Logger.getLogger(HiveDBConnectionFactory.class);

	private String host;
	private String username;
	private String password;
	private long port;
	private String hostType;
	private String errorMsg;
	private String fileDir;
	private String fileName;

	public void setDBSuffix(String prefix){
	}
	
	public HiveDBConnectionFactory(String host, long port,String username,String password,String hostType){
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
		this.hostType = hostType;
	}
	
	public void getConnection() {
		if (host != null && username != null && password != null && port != 0 && hostType != null){
			switch(hostType) {
				case "HIVE-HS1" : 
					connection = DBUtil.getHiveHS1Connection(host, port, username, password); 
					break;
				case "HIVE-HS2" : 
					connection = DBUtil.getHiveHS2Connection(host, port, username, password); 
					break;
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	@Override
	public void closeConnection() {
		DBUtil.closeConnection(connection,null,null);
		
	}

	@Override
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
		try{
			sql = sql.replace("CURRENT_TIMESTAMP", strTime);
			sql = sql.toLowerCase();
			if(fileDir != null && fileName != null) {
				String content = getInsertScripts(sql,recs);
				FileUtil.append2File(fileDir+"/"+fileName, content);
			}
		}catch (Exception e) {
			long time = new Date().getTime();
			logger.error("Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + ImpedUtil.getErrorStackTrace(e));
			this.errorMsg = "Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + e.getMessage();
		}catch (Error e) {
			long time = new Date().getTime();
			logger.error("Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + ImpedUtil.getErrorStackTrace(e));
			this.errorMsg = "Error occured while executing sql : " + ImpedConstants.STR_REF + time + ":" + e.getMessage();
		}  
		return 0;
	}

	private String getInsertScripts(String sql, List<List<Object>> recs) {
		StringBuilder sb = new StringBuilder();
		String insertSQL = null;
		for (List<Object> row : recs){
			insertSQL = sql;
			for (Object col : row){
				/*if(col instanceof String) {
					insertSQL = insertSQL.replaceFirst("\\?", "'"+col.toString()+"'");
				} else {
					insertSQL = insertSQL.replaceFirst("\\?", col.toString());
				}*/
				insertSQL = insertSQL.replaceFirst("\\?", col.toString());
			}
			insertSQL= insertSQL.replace("'", "");
			insertSQL = insertSQL.substring(insertSQL.lastIndexOf("(")+2, insertSQL.lastIndexOf(")")-1);
			sb.append(insertSQL).append("\n");
		}
		return sb.toString();
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
			System.out.println("Error occured while getAllRecords HIVE sql :" + e.getMessage());
		} finally {
			DBUtil.closeConnection(null, null, ps);
		}
		return list;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

}
