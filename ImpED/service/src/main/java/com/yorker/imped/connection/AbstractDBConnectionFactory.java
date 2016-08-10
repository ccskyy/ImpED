package com.yorker.imped.connection;

import java.sql.Connection;
import java.util.List;

public abstract class AbstractDBConnectionFactory {
	public Connection connection = null;
	public abstract void getConnection();
	public abstract void closeConnection();
	public abstract int addRecords(String sql,List<List<Object>> list,String error);
	public abstract List<Object> getAllRecords(String sql, int size);
	public abstract String getErrorMsg();
	public abstract Object executeStatement(String execStep,String error,Object param);
}
