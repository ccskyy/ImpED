package com.yorker.imped.connection;

public class DBConnectionFactoryProducer {

	public static AbstractDBConnectionFactory getDBConnectionFactory(String hostType, String dbUser, String password) {
		AbstractDBConnectionFactory  f = null;
		String host = null;
		int port = 0;
		String sid = null;
		String service = null;
		/*for(ParameterVO vo : hostVO.getParamList()){
			if (vo.getParamName().equals("HOST_NAME")) host = vo.getParamValue();
			if (vo.getParamName().equals("PORT")) port = Integer.parseInt(vo.getParamValue());
			if (vo.getParamName().equals("SID")) sid = vo.getParamValue();
			if (vo.getParamName().equals("SERVICE_NAME")) service = vo.getParamValue();
		}*/
		switch (hostType.toUpperCase()){
			case "ORACLE" :
				f = new OracleDBConnectionFactory(host,port,sid,dbUser,password,service);
				break;
			case "TERADATA" :
				f = new TeradataDBConnectionFactory(host,dbUser,password);
				break;
			case "HIVE-HS1" :
			case "HIVE-HS2" :
				f = new HiveDBConnectionFactory(host,port,dbUser,password,hostType);
				break;
			case "MYSQL" :
				f = new MySQLDBConnectionFactory(host,port,dbUser,password);
				break;
			case "MS-SQL-SERVER" :
				f = new MSSQLServerDBConnectionFactory(host,port,dbUser,password);
		}
		return f;
	}
	
}