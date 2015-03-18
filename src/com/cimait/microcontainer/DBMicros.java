package com.cimait.microcontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cimait.DAO.FacEmpresa;

public class DBMicros {

	public static void getPuntoEmision(Connection conDatabase, String ruc, String resort){
		Connection Con = conDatabase;	
		ResultSet Rs= null;
		PreparedStatement pst = null;		
		try{    	
	    	String sql = " Select * from PUNTO_EMISION " ; //'OROGYE'
	    	pst = Con.prepareStatement(sql);
	    	
	    	Rs= pst.executeQuery();
	    	while (Rs.next()){ 
	    		
	    		System.out.println();
	    		
	    	}
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
				Rs.close();
		    	pst.close();
		    	Con.close();
	    	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	
	
}
