package com.cimait.Util;
 
//import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.cimait.microcontainer.DBConnection;
import com.cimait.runtime.Environment;

public class PostgresSQLDBConnection extends DBConnection {
	
	private String connectionError;
	private String classReference="PostgresSQLDBConnection->";
	 
	public Object getInstance() {
		Connection con = null;
		try {
			Class.forName(getDriver());
			Environment.log.debug(new StringBuffer().append(classReference).append(getUser()).toString());
			con = DriverManager.getConnection(getUrl(), getUser(), getPassword());
			con.setAutoCommit(true);
			/*if (getId() != null){
				CallableStatement cs = con.prepareCall("{call dbms_application_info.set_client_info(?) }");
				cs.setString(1,getId());
				cs.execute();
				cs.close();
			}*/
			/*
		} catch (SQLException e_sql) {
			e_sql.printStackTrace();
			Environment.log.error(new StringBuffer().append(classReference).append(e_sql.getMessage()).toString());
			StackTraceElement[] ste = e_sql.getStackTrace();
			for (StackTraceElement i : ste)
				Environment.log.error(new StringBuffer().append(classReference).append(i.toString()).toString());
		} catch (ClassNotFoundException e_cnf) {
			Environment.log.error(new StringBuffer().append(classReference).append(e_cnf.getMessage()).toString());
			StackTraceElement[] ste = e_cnf.getStackTrace();
			for (StackTraceElement i : ste)
				Environment.log.error(new StringBuffer().append(classReference).append(i.toString()).toString());
		*/} catch (Exception e) {
			e.printStackTrace();
			Environment.log.error(new StringBuffer().append(classReference).append(e.getMessage()).toString());
			StackTraceElement[] ste = e.getStackTrace();
			for (StackTraceElement i : ste)
				Environment.log.error(new StringBuffer().append(classReference).append(i.toString()).toString());
		}
		return con;
	}
	
	public String getConnectionError(){
		return connectionError;
	}
}
