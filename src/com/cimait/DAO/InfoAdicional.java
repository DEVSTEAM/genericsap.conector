package com.cimait.DAO;

public class InfoAdicional {
	
	private String nombre;
	private String valor;
	
	public InfoAdicional(){
		nombre = "";
		valor = "";
	}
	
	public InfoAdicional(String ls_nombre, String ls_valor){
		nombre = ls_nombre;
		valor = ls_valor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
}
