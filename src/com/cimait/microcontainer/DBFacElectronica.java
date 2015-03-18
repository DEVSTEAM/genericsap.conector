package com.cimait.microcontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.cimait.DAO.FacEmpresa;
import com.cimait.DAO.FacEstablecimiento;
import com.cimait.DAO.FacPuntoEmision;
import com.cimait.runtime.ConexionBase;
import com.cimait.runtime.Hilo;

public class DBFacElectronica {

	public static FacEmpresa getRucEmpresa(Connection conDatabase, String ruc){
		Connection Con = conDatabase;	
		ResultSet Rs= null;
		PreparedStatement pst = null;
		FacEmpresa empresa = null;	
		try {
			if ((Con.isClosed())||Con==null)
			Con = ConexionBase.getConnectionBD(Con,"fac_electronica", "PostgreSQL");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{    	
	    	String sql = " Select RazonSocial, RazonComercial, DireccionMatriz, " +
	    				 " ContribEspecial, CASE WHEN ObligContabilidad='S' THEN 'SI' ELSE 'NO' END  ObligContabilidad,  " +
	    				 " PathCompGenerados, " +
	    				 " FechaResolucionContribEspecial " +
	    				 " from fac_empresa " + 
	    				 " where Ruc = ? and isActive = 'Y' ";  //'0990293511001'	    				  
	    	pst = Con.prepareStatement(sql);
	    	pst.setString(1, ruc);
	    	Rs= pst.executeQuery();
	    	while (Rs.next()){ 
	    		empresa = new FacEmpresa();
	    		empresa.setRuc(ruc);
	    		
	    		empresa.setRazonSocial(Rs.getString(1));
	    		empresa.setRazonComercial(Rs.getString(2));
	    		empresa.setDireccionMatriz(Rs.getString(3));
	    		empresa.setContribEspecial(Integer.parseInt(Rs.getString(4)));
	    		empresa.setObligContabilidad(Rs.getString(5));
	    		empresa.setPathCompGenerados(Rs.getString(6));
	    		empresa.setFechaResolucionContribEspecial(Rs.getString(7));	    		
	    		
	    	}
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
				Rs.close();
		    	pst.close();
		    	//Con.close();
	    	} catch (SQLException e) {
				e.printStackTrace();
			}

		}
    	return empresa;
	}
	
	public static FacEstablecimiento getEstablecimiento(Connection conDatabase, 
														String ruc,
														String local){
		
		Connection Con = conDatabase;	
		ResultSet Rs= null;
		PreparedStatement pst = null;
		FacEstablecimiento establecimiento = null;						
		try {
			if ((conDatabase.isClosed())||conDatabase==null)
				conDatabase = ConexionBase.getConnectionBD(conDatabase,"Invoice", "SQLServer");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{    	
	    	String sql = " Select CodEstablecimiento, DireccionEstablecimiento,  " +
	    				 " Correo, Mensaje, PathAnexo, local " +
	    				 " from fac_establecimiento " + 
	    				 " where Ruc = ? and isActive in('Y','1')  " +
	    				 " and local = ? ";  //'0990293511001'	    				  
	    	pst = Con.prepareStatement(sql);
	    	pst.setString(1, ruc);
	    	pst.setString(2, local);
	    	Rs= pst.executeQuery();
	    	
	    	while (Rs.next()){ 
	    		establecimiento = new FacEstablecimiento(ruc,
	    												 Rs.getString(1),
	    												 Rs.getString(2),
	    												 Rs.getString(3),
	    												 Rs.getString(4),
	    												 Rs.getString(5),
	    												 Rs.getString(6));	    		
	    	}
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
				Rs.close();
		    	pst.close();
		    	//Con.close();		    	
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
		}
		return establecimiento;
	}

	
	
	public static ArrayList<FacEstablecimiento> getListEstablecimiento(Connection conDatabase, String ruc){
		Connection Con = conDatabase;	
		ResultSet Rs= null;
		PreparedStatement pst = null;
		ArrayList<FacEstablecimiento> listEstablecimiento = new ArrayList<FacEstablecimiento>();
		FacEstablecimiento establecimiento = null;				
		try {
			if ((conDatabase.isClosed())||conDatabase==null)
				conDatabase = ConexionBase.getConnectionBD(conDatabase,"fac_electronica", "PostgreSQL");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{    	
	    	String sql = " Select \"CodEstablecimiento\", \"DireccionEstablecimiento\",  " +
	    				 " \"Correo\", \"Mensaje\", \"PathAnexo\", \"local\" " +
	    				 " from fac_establecimiento " + 
	    				 " where \"Ruc\" = ? and \"isActive\" in('Y','1')  " +
	    				 " and \"CodEstablecimiento\" = '002' "
	    				 

	    				 ;  //'0990293511001'	    				  
	    	pst = Con.prepareStatement(sql);
	    	pst.setString(1, ruc);
	    	Rs= pst.executeQuery();
	    	
	    	while (Rs.next()){ 
	    		establecimiento = new FacEstablecimiento(ruc,Rs.getString(1),Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6));
	    		establecimiento.setListPuntoEmisiones(getListPuntoEmision(Con, ruc, Rs.getString(1),establecimiento.getLocal()));	    		
	    		listEstablecimiento.add(establecimiento);
	    	}
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
				Rs.close();
		    	pst.close();
		    	//Con.close();		    	
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
		}
		return listEstablecimiento;
	}

	public static FacPuntoEmision getPuntoEmision(Connection conDatabase, 
												  String ruc, 
												  String establecimiento, 
												  String caja,
												  String local){
		Connection Con = conDatabase;	
		ResultSet Rs= null;
		PreparedStatement pst = null;
		FacPuntoEmision puntoEmision = null;		
		try {
			if ((conDatabase.isClosed())||conDatabase==null)
				conDatabase = ConexionBase.getConnectionBD(conDatabase,"fac_electronica", "PostgreSQL");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{    	
	    	String sql = " Select \"CodEstablecimiento\",\"CodPuntEmision\",\"TipoDocumento\", " + 
	    				 " CASE WHEN \"TipoAmbiente\"='D' THEN '1' ELSE '2' END TipoAmbiente,\"Secuencial\",\"FormaEmision\",\"caja\" " +
	    				 " from fac_punto_emision where \"Ruc\" = ? and \"isActive\" in('Y','1') and \"CodEstablecimiento\" = ? and \"caja\" = ?  ";
    				  
	    	pst = Con.prepareStatement(sql);
	    	pst.setString(1, ruc);
	    	pst.setString(2, establecimiento);
	    	pst.setString(3, caja);
	    	Rs= pst.executeQuery();
	    	while (Rs.next()){ 
	    		puntoEmision = new FacPuntoEmision(ruc,Rs.getString(1),Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7),caja);
	    		
	    	}
		}catch(Exception e){
			puntoEmision = null;
	    	e.printStackTrace();
		}finally {
	    	try {
				Rs.close();
		    	pst.close();
		    	//Con.close();		    	
	    	} catch (SQLException e) {
				e.printStackTrace();
			}	    	
		}
		return puntoEmision;
	}	
	
	public static ArrayList<FacPuntoEmision> getListPuntoEmision(Connection conDatabase, String ruc, String establecimiento, String local){
		Connection Con = conDatabase;	
		ResultSet Rs= null;
		PreparedStatement pst = null;
		ArrayList<FacPuntoEmision> listPuntoEmision = new ArrayList<FacPuntoEmision>();
		FacPuntoEmision puntoEmision = null;
		try {
			if ((conDatabase.isClosed())||conDatabase==null)
				conDatabase = ConexionBase.getConnectionBD(conDatabase,"fac_electronica", "PostgreSQL");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{    	
	    	String sql = " Select \"CodEstablecimiento\",\"CodPuntEmision\",\"TipoDocumento\", " + 
	    				 " CASE WHEN \"TipoAmbiente\"='D' THEN '1' ELSE '2' END TipoAmbiente,\"Secuencial\",\"FormaEmision\",\"caja\" " +
	    				 " from fac_punto_emision where \"Ruc\" = ? and \"isActive\" in('Y','1') and \"CodEstablecimiento\" = ? and \"caja\" = '10'  " +
	    				 "  " ; //0990293511001
    				  
	    	pst = Con.prepareStatement(sql);
	    	pst.setString(1, ruc);
	    	pst.setString(2, establecimiento);
	    	Rs= pst.executeQuery();
	    	while (Rs.next()){ 
	    		puntoEmision = new FacPuntoEmision(ruc,Rs.getString(1),Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7),local);
	    		listPuntoEmision.add(puntoEmision);
	    	}
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
				Rs.close();
		    	pst.close();
		    	//Con.close();		    	
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
		}
		return listPuntoEmision;
	}
	
	
	public static ArrayList<FacPuntoEmision> getListPuntoEmision(Connection conDatabase, String ruc, String local){
		Connection Con = conDatabase;	
		ResultSet Rs= null;
		PreparedStatement pst = null;
		ArrayList<FacPuntoEmision> listPuntoEmision = new ArrayList<FacPuntoEmision>();
		FacPuntoEmision puntoEmision = null;				
		try {
			if ((conDatabase.isClosed())||conDatabase==null)
				conDatabase = ConexionBase.getConnectionBD(conDatabase,"fac_electronica", "PostgreSQL");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{    	
	    	String sql = " Select \"CodEstablecimiento\",\"CodPuntEmision\",\"TipoDocumento\", " + 
	    				 " CASE WHEN \"TipoAmbiente\"='D' THEN '1' ELSE '2' END TipoAmbiente,\"Secuencial\",\"FormaEmision\",\"local\" " +
	    				 " from fac_punto_emision where \"Ruc\" = ? and \"isActive\" in('Y','1') "; //0990293511001
    				  //and \"CodEstablecimiento\" =  ?
	    	pst = Con.prepareStatement(sql);
	    	pst.setString(1, ruc);
	    	
	    	Rs= pst.executeQuery();
	    	while (Rs.next()){ 
	    		puntoEmision = new FacPuntoEmision(ruc,Rs.getString(1),Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7),local);
	    		listPuntoEmision.add(puntoEmision);
	    	}
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
				Rs.close();
		    	pst.close();
		    	//Con.close();		    	
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
		}
		return listPuntoEmision;
	}
	
	public static void setSecuenciaEstablecimiento(Connection conDatabase, String ruc, String establecimiento, String secuencialOpera){
		Connection Con = conDatabase;	
		PreparedStatement pst = null;		
		try {
			if ((conDatabase.isClosed())||conDatabase==null)
				conDatabase = ConexionBase.getConnectionBD(conDatabase,"fac_electronica", "PostgreSQL");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}				
		try{    	
	    	String sql = " update fac_establecimiento set \"secuenciaOpera\" = ? " + 
	    				 " where \"Ruc\" = ? and \"CodEstablecimiento\" = ? and \"isActive\" in('Y','1')  "; //0990293511001
    				  
	    	pst = Con.prepareStatement(sql);
	    	pst.setString(1, secuencialOpera);
	    	pst.setString(2, ruc);
	    	pst.setString(3, establecimiento);
	    	pst.executeUpdate();
	    	
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
		    	pst.close();
		    	//Con.close();		    	
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
		}
	}
	
	public static void insertFalloExtraccion(Connection conDatabase, 
											 String ruc, 
											 String IdMovimiento, 
											 String fecha, 
											 String secuencial, 
											 String tipoDocumento, 
											 String local, 
											 String caja, 
											 String mensaje) throws Exception{
		Connection Con = conDatabase;	
		PreparedStatement pst = null;		
		try {
			if ((conDatabase.isClosed())||conDatabase==null)
				conDatabase = ConexionBase.getConnectionBD(conDatabase,"Invoice", "SQLServer");
	    	String sql = " insert into fac_fallos_extraccion " +
	    			     " (ruc,IdMovimiento,fecha,Secuencial,tipoDocumento,local,caja,mensaje) " +
	    			     " values(?,?,?,?,?,?,?,?)"; //0990293511001
	    	pst = Con.prepareStatement(sql);
	    	//String ruc, String IdMovimiento, String fecha, String secuencial, String tipoDocumento, String local, String caja
	    	pst.setString(1, ruc);
	    	pst.setString(2, IdMovimiento);
	    	pst.setString(3, fecha);
	    	pst.setString(4, secuencial);
	    	pst.setString(5, tipoDocumento);
	    	pst.setString(6, local);
	    	pst.setString(7, caja);
	    	pst.setString(8, mensaje);
	    	pst.executeUpdate();
		}catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception("ERRORDATABASE,insertFalloExtraccion Error al Insertar el Fallo en la extraccion el movimiento::"+IdMovimiento+"->"+e.toString());
		}finally {
	    	try {
		    	pst.close();
		    	//Con.close();		    	
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void setSecuenciaPuntoEmision(Connection conDatabase, String ruc, String establecimiento,String puntoEmision, String secuencialOpera){
		Connection Con = conDatabase;	
		PreparedStatement pst = null;
		try {
			if ((conDatabase.isClosed())||conDatabase==null)
				conDatabase = ConexionBase.getConnectionBD(conDatabase,"fac_electronica", "PostgreSQL");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
						
		try{    	
	    	String sql = " update fac_punto_Emision set \"Secuencial\" = ? " + 
	    				 " where \"Ruc\" = ? and \"CodEstablecimiento\" = ? and \"isActive\" in('Y','1') and \"CodPuntEmision\" = ?  "; //0990293511001
    				  
	    	pst = Con.prepareStatement(sql);
	    	pst.setInt(1, Integer.parseInt(secuencialOpera));
	    	pst.setString(2, ruc);
	    	pst.setString(3, establecimiento);
	    	pst.setString(4, puntoEmision);
	    	pst.executeUpdate();
	    	
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
		    	pst.close();
		    	//Con.close();		    	
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
		}
	}
	
	public static String getSecuenciaPuntoEmision(Connection conDatabase, String ruc, String establecimiento,String puntoEmision){
		Connection Con = conDatabase;	
		ResultSet Rs= null;
		PreparedStatement pst = null;
		String secuencial = "";
		try {
			if ((conDatabase.isClosed())||conDatabase==null)
				conDatabase = ConexionBase.getConnectionBD(conDatabase,"fac_electronica", "PostgreSQL");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
						
		try{    	
	    	String sql = " select \"Secuencial\" from  fac_punto_Emision " + 
	    				 " where \"Ruc\" = ? and \"CodEstablecimiento\" = ? and \"isActive\" in('Y','1') and \"CodPuntEmision\" = ?  "; //0990293511001
    				  
	    	pst = Con.prepareStatement(sql);
	    	
	    	pst.setString(1, ruc);
	    	pst.setString(2, establecimiento);
	    	pst.setString(3, puntoEmision);
	    	Rs = pst.executeQuery();
	    	//int li_execute = pst.executeUpdate();
	    	while(Rs.next()){
	    		secuencial = Rs.getString(1);
	    	}
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
		    	pst.close();
		    	//Con.close();		    	
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return secuencial;
	}
	
}
