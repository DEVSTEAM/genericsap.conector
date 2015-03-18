package com.cimait.microcontainer;
 
import java.sql.Connection;
import org.apache.log4j.Logger;
import org.apache.log4j.Appender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Level;
 
import com.cimait.Util.OracleDBConnection;
import com.cimait.Util.PostgresSQLDBConnection;
import com.cimait.runtime.Environment;

public abstract class GenericTransaction implements Runnable {

	protected static String classReference;
	protected static String id;
	protected static Logger log;
	protected String passBD;
	
	protected static Connection setOracleConnectionOracle(String db, String pathXml){
		Connection conn;		
		//boolean resultado = false;
		OracleDBConnection odbc = new OracleDBConnection();		
		odbc.setDataBase(Environment.c.getString(pathXml+".database"));
		//System.out.println("Driver:"+Environment.c.getString(pathXml+".driver"));
		odbc.setDriver(Environment.c.getString(pathXml+".driver"));
		odbc.setUrl(Environment.c.getString(pathXml+"."+db+".url"));
		odbc.setUser(Environment.c.getString(pathXml+"."+db+".user"));
		odbc.setPassword(Environment.c.getString(pathXml+"."+db+".password"));
		//odbc.setPassword(passBD);
		odbc.setId(id);		
		conn = (Connection)odbc.getInstance();
		//if (conn != null)
		//resultado = true;
		return conn;
	}
	protected static Connection setConnectionPostgreSQL(String db, String pathXml){
		Connection conn;		
		//boolean resultado = false;
		PostgresSQLDBConnection odbc = new PostgresSQLDBConnection();	
		System.out.println("Database:"+Environment.c.getString(pathXml+"."+db+".database"));
		odbc.setDataBase(Environment.c.getString(pathXml+".database"));
		//System.out.println("Driver:"+Environment.c.getString(pathXml+".driver"));
		odbc.setDriver(Environment.c.getString(pathXml+".driver"));
		System.out.println("driver:"+Environment.c.getString(pathXml+".driver"));
		odbc.setUrl(Environment.c.getString(pathXml+"."+db+".url"));
		System.out.println("url:"+Environment.c.getString(pathXml+"."+db+".url"));
		odbc.setUser(Environment.c.getString(pathXml+"."+db+".user"));
		System.out.println("user:"+Environment.c.getString(pathXml+"."+db+".user"));
		odbc.setPassword(Environment.c.getString(pathXml+"."+db+".password"));
		System.out.println("password:"+Environment.c.getString(pathXml+"."+db+".password"));
		//odbc.setPassword(passBD);
		odbc.setId(id);		
		conn = (Connection)odbc.getInstance();
		//if (conn != null)
		//resultado = true;
		return conn;
	}
	
	protected static Connection setConnectionSQLServer(String db, String pathXml){
		Connection conn;		
		//boolean resultado = false;
		PostgresSQLDBConnection odbc = new PostgresSQLDBConnection();		
		odbc.setDataBase(Environment.c.getString(pathXml+".database"));
		odbc.setDriver(Environment.c.getString(pathXml+".driver"));
		odbc.setUrl(Environment.c.getString(pathXml+"."+db+".url"));
		odbc.setUser(Environment.c.getString(pathXml+"."+db+".user"));
		odbc.setPassword(Environment.c.getString(pathXml+"."+db+".password"));	
		conn = (Connection)odbc.getInstance();
		return conn;
	}
	
	protected void setLogger(){
		Environment.log.info(new StringBuffer().append("Setting Logger "+id+".log for "+classReference).toString());
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%d %X{thread-id} %m%n");
		DailyRollingFileAppender appender = new DailyRollingFileAppender();
		appender.setFile("./logs/"+id+".log");
		appender.setDatePattern("'.'yyyy-MM-dd");
		appender.setLayout(layout);
		appender.activateOptions();
		appender.setName(id);
		log = Logger.getLogger(id);
		log.addAppender(appender);
		log.setLevel(Level.DEBUG);
	}
	
	protected void closeLogger(){
		Appender appender = log.getAppender(id);
		log.removeAppender(appender);
	}
	
	protected long freeMemory(){
		System.gc();
		return Runtime.getRuntime().freeMemory();
	}
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
