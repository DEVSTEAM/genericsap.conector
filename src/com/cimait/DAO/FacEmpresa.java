package com.cimait.DAO;

import java.util.ArrayList;

public class FacEmpresa {
	
	private String ruc;
	private String razonSocial;
	private String razonComercial;
	private String direccionMatriz;
	private int contribEspecial;
	private String obligContabilidad;
	private String pathCompGenerados;
	private String pathLogoEmpresa;
	private String pathFirma;
	private String emailEnvio;
	private String marcaAgua;
	private String pathMarcaAgua;
	private String fechaResolucionContribEspecial;
	private ArrayList<FacEstablecimiento> ListEstablecimiento;
	
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getRazonComercial() {
		return razonComercial;
	}
	public void setRazonComercial(String razonComercial) {
		this.razonComercial = razonComercial;
	}
	public String getDireccionMatriz() {
		return direccionMatriz;
	}
	public void setDireccionMatriz(String direccionMatriz) {
		this.direccionMatriz = direccionMatriz;
	}
	public int getContribEspecial() {
		return contribEspecial;
	}
	public void setContribEspecial(int contribEspecial) {
		this.contribEspecial = contribEspecial;
	}
	public String getObligContabilidad() {
		return obligContabilidad;
	}
	public void setObligContabilidad(String obligContabilidad) {
		this.obligContabilidad = obligContabilidad;
	}
	public String getPathCompGenerados() {
		return pathCompGenerados;
	}
	public void setPathCompGenerados(String pathCompGenerados) {
		this.pathCompGenerados = pathCompGenerados;
	}
	public String getPathLogoEmpresa() {
		return pathLogoEmpresa;
	}
	public void setPathLogoEmpresa(String pathLogoEmpresa) {
		this.pathLogoEmpresa = pathLogoEmpresa;
	}
	public String getPathFirma() {
		return pathFirma;
	}
	public void setPathFirma(String pathFirma) {
		this.pathFirma = pathFirma;
	}
	public String getEmailEnvio() {
		return emailEnvio;
	}
	public void setEmailEnvio(String emailEnvio) {
		this.emailEnvio = emailEnvio;
	}
	public String getMarcaAgua() {
		return marcaAgua;
	}
	public void setMarcaAgua(String marcaAgua) {
		this.marcaAgua = marcaAgua;
	}
	public String getPathMarcaAgua() {
		return pathMarcaAgua;
	}
	public void setPathMarcaAgua(String pathMarcaAgua) {
		this.pathMarcaAgua = pathMarcaAgua;
	}

	public String getFechaResolucionContribEspecial() {
		return fechaResolucionContribEspecial;
	}
	public void setFechaResolucionContribEspecial(
			String fechaResolucionContribEspecial) {
		this.fechaResolucionContribEspecial = fechaResolucionContribEspecial;
	}
	public ArrayList<FacEstablecimiento> getListEstablecimiento() {
		return ListEstablecimiento;
	}
	public void setListEstablecimiento(
			ArrayList<FacEstablecimiento> listEstablecimiento) {
		ListEstablecimiento = listEstablecimiento;
	}	
}
