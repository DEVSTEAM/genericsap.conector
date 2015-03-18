package com.cimait.DAO;

import java.util.ArrayList;

public class FacEstablecimiento {

	private String ruc;
	private String codEstablecimiento;
	private String dirEstablecimiento;
	private String correo;
	private String mensaje;
	private String pathAnexo;
	private String local;
	
	
	private ArrayList<FacPuntoEmision> ListPuntoEmisiones;
	
	public FacEstablecimiento(){
	this.ruc = ""; 
	this.codEstablecimiento = "";
	this.dirEstablecimiento = "";
	this.correo = "";
	this.mensaje = "";
	this.pathAnexo = "";
	this.local = "";	
	
	}
	
	public FacEstablecimiento(String ruc, String codEstablecimiento,
							  String dirEstablecimiento,String correo,
							  String mensaje, String pathAnexo,
							  String local
							  ){
		  this.ruc = ruc; 
		  this.codEstablecimiento = codEstablecimiento;
		  this.dirEstablecimiento = dirEstablecimiento;
		  this.correo = correo;
		  this.mensaje = mensaje;
		  this.pathAnexo = pathAnexo;
		  this.local = local;	
		  
		  ListPuntoEmisiones = null;
	}
	
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public String getCodEstablecimiento() {
		return codEstablecimiento;
	}
	public void setCodEstablecimiento(String codEstablecimiento) {
		this.codEstablecimiento = codEstablecimiento;
	}
	public String getDirEstablecimiento() {
		return dirEstablecimiento;
	}
	public void setDirEstablecimiento(String dirEstablecimiento) {
		this.dirEstablecimiento = dirEstablecimiento;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getPathAnexo() {
		return pathAnexo;
	}
	public void setPathAnexo(String pathAnexo) {
		this.pathAnexo = pathAnexo;
	}
		
	public String getLocal() {
		return local;
	}

	public void setDLocal(String local) {
		this.local = local;
	}

	public ArrayList<FacPuntoEmision> getListPuntoEmisiones() {
		return ListPuntoEmisiones;
	}
	public void setListPuntoEmisiones(ArrayList<FacPuntoEmision> listPuntoEmisiones) {
		ListPuntoEmisiones = listPuntoEmisiones;
	}
}
