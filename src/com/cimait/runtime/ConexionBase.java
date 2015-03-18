package com.cimait.runtime;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.cimait.Util.OracleDBConnection;
import com.cimait.Util.PostgresSQLDBConnection;
import com.cimait.Util.Util;

public class ConexionBase {
	
	protected static String classReference;
	protected static String id;
	protected static Logger log;
	protected String passBD;
	
	// VPI - GBA
	/// Conexión por Thread Local: Son como globales pero ThreadSafe y no requieren Sincronización y por ende no generan Overhead
	//public static final ThreadLocal<Connection> DBManager = new ThreadLocal<Connection>();
	public static final ThreadLocal<Connection> DBManager = new ThreadLocal<Connection>(){
		  //Fallback a una conexión en caso de Error....
		  @Override protected Connection initialValue(){
		   Connection con = null;
		   try {
		    //con = getConexionBD();
			   con =  getConnectionBD(con,"Empresa", "SQLServer");
		   } catch (Exception e) {
		    System.out.println("Error al obtener la conexión fallback hacia la base");
		    e.printStackTrace();
		   }
		   return con;
		  }
		 };
	
	// VPI - GBA - INI 
	/// Conexión por Thread Local: Son como globales pero ThreadSafe y no requieren Sincronización y por ende no generan Overhead
	public static void init() {
		try {
			DBManager.set(getConnectionBD(null,"Empresa", "SQLServer"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void cerrarConexionBD() {
		if (DBManager.get() != null) {
			try {
				DBManager.get().close();
				DBManager.remove();
			} catch (Exception e) {
				System.out
						.println("Error al cerrar la conexión a la base para el Thread: "
								+ Thread.currentThread().getName());
				e.printStackTrace();
			}
		}
	}
	/**************************************************************************************
	** Function:	 getConnectionBD
	** Description:  Funcion que crea una conexion a la base de datos especificada
	** Return:		 Retorna > Objeto Connection -> exito.  En caso de error -> null.
	**************************************************************************************/
	/*
	public static Connection getConnectionBD(Connection datase, String ls_BD, String Database)
	{
		Connection respuesta = null;
		int intento=0;
		String g_msg = "";
		try {
		if (datase == null){
			//conecta por primera vez
			intento=0;
			do{
				try{
					System.out.print("\nServiceEmpresa::EmpresaManager->getConnectionBD :: Conectandose a Base de Datos " + ls_BD);					
					if (Database.equals("Oracle")){
						respuesta = setOracleConnectionOracle(ls_BD,"Empresa.oracle");
					}
					if (Database.equals("PostgreSQL")){
						respuesta = setConnectionPostgreSQL(ls_BD,"Empresa.postgreSQL");
					}
					if (Database.equals("SyBase")){
						respuesta = setOracleConnectionOracle(ls_BD,"Empresa.syBase");
					}
					if (Database.equals("SQLServer")){
						respuesta = setConnectionSQLServer(ls_BD,"Empresa.DB");
					}
					if (respuesta == null){
						intento++;
					}
				}catch(Exception e){
					intento++;
					System.out.print("ServiceEmpresa::EmpresaManager->getConnectionBD :: BD: "+ ls_BD +" | Error de Conexion "+e.toString()+" Reintentando...");
				}
			}while((respuesta == null)&&(intento<3));
		}
		else{
			respuesta = datase;
		}
	    /* excedio intentos de conexion 
		if ((intento >= 3) && (respuesta == null))
	    {
			System.out.print("ServiceEmpresa::Empresa->getConnectionBD::** Error conexion a Base de Datos " + ls_BD + ": Mas de 3 intentos de conexion a Base de Datos\n");
			//log.debug(new StringBuffer().append("ServiceEmpresa::").append(classReference).append("->getConnectionBD").append(" :: ").append("** Error socket: Mas de 3 intentos de conexion a Base de Datos\n"));
			/*try{
				respuesta.close();
			}catch(Exception e){			
			}*	
		    g_msg = "ServiceEmpresa::ERROR DE CONEXION. Mas de 3 intentos de conexion a Base de Datos ("+ ls_BD +").";
		    Util.enviaEmailSoporteService("message_service_error", g_msg);	
		}
		}catch(Exception e){			
			//log.debug(new StringBuffer().append("ServiceEmpresa :: ").append(classReference).append("->getConnectionBD").append(" :: ").append(e.toString()));
			System.out.print("ServiceEmpresa::EmpresaManager->getConnectionBD :: " + e.toString());
			g_msg = "ServiceEmpresa: ERROR GENERAL: Conexion a Base de Datos " + ls_BD+" -->> "+e.getMessage();
			Util.enviaEmailSoporteService("message_service_error", g_msg);
		}		
		return respuesta; 
    }
	*/
	/**************************************************************************************
	** Function:	 getConnectionBD
	** Description:  Funcion que crea una conexion a la base de datos especificada
	** Return:		 Retorna > Objeto Connection -> exito.  En caso de error -> null.
	**************************************************************************************/
	public static Connection getConnectionBD(Connection datase,String ls_BD, String Database)
	{
		Connection respuesta = null;
		int intento=0;
		String g_msg = "";
		try {
				
		intento=0;
		if (datase == null){
			do{
				try{
					System.out.print("\nServiceEmpresa::EmpresaManager->getConnectionBD :: Conectandose a Base de Datos " + ls_BD);					
					if (Database.equals("Oracle")){
						respuesta = setOracleConnectionOracle(ls_BD,"Empresa.oracle");
					}
					if (Database.equals("PostgreSQL")){
						respuesta = setConnectionPostgreSQL(ls_BD,"Empresa.postgreSQL");
					}
					if (Database.equals("SyBase")){
						respuesta = setOracleConnectionOracle(ls_BD,"Empresa.syBase");
					}
					if (Database.equals("SQLServer")){
						respuesta = setConnectionSQLServer(ls_BD,"Empresa.DB");
					}
					if (respuesta == null){
						intento++;
					}
				}catch(Exception e){
					intento++;
					System.out.print("ServiceEmpresa::EmpresaManager->getConnectionBD :: BD: "+ ls_BD +" | Error de Conexion "+e.toString()+" Reintentando...");
				}
			 //VPI espero unos segundos en cada intento
				if((respuesta == null))
					Thread.sleep(10000);	
			}while((respuesta == null)&&(intento<3));
		}else{
				respuesta = datase;
		}
		if ((intento >= 3) && (respuesta == null))
	    {
			System.out.print("ServiceEmpresa::Empresa->getConnectionBD::** Error conexion a Base de Datos " + ls_BD + ": Mas de 3 intentos de conexion a Base de Datos\n");	
		    g_msg = "ServiceEmpresa::ERROR DE CONEXION. Mas de 3 intentos de conexion a Base de Datos ("+ ls_BD +").";
		    Util.enviaEmailSoporteService("message_service_error", g_msg);	
		}
		}catch(Exception e){			
			System.out.print("ServiceEmpresa::EmpresaManager->getConnectionBD :: " + e.toString());
			g_msg = "ServiceEmpresa: ERROR GENERAL: Conexion a Base de Datos " + ls_BD;
			Util.enviaEmailSoporteService("message_service_error", g_msg);
		}		
		return respuesta; 
    }
	
	protected static Connection setOracleConnectionOracle(String db, String pathXml){
		Connection conn;		
		OracleDBConnection odbc = new OracleDBConnection();		
		odbc.setDataBase(Environment.c.getString(pathXml+".database"));
		odbc.setDriver(Environment.c.getString(pathXml+".driver"));
		odbc.setUrl(Environment.c.getString(pathXml+"."+db+".url"));
		odbc.setUser(Environment.c.getString(pathXml+"."+db+".user"));
		odbc.setPassword(Environment.c.getString(pathXml+"."+db+".password"));
		odbc.setId(id);		
		conn = (Connection)odbc.getInstance();
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
	
	protected static Connection setConnectionSQLServer(String db, String pathXml) {
		Connection conn;
		// boolean resultado = false;
		PostgresSQLDBConnection odbc = new PostgresSQLDBConnection();
		odbc.setDataBase(Environment.c.getString(pathXml + ".database"));
		odbc.setDriver(Environment.c.getString(pathXml + ".driver"));
		odbc.setUrl(Environment.c.getString(pathXml + "." + db + ".url"));
		odbc.setUser(Environment.c.getString(pathXml + "." + db + ".user"));
		odbc.setPassword(Environment.c.getString(pathXml + "." + db + ".password"));
		conn = (Connection) odbc.getInstance();
		return conn;
	}

}
