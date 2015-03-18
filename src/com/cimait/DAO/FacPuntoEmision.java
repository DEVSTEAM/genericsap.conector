package com.cimait.DAO;

public class FacPuntoEmision {
	private String ruc;
	private String codEstablecimiento;
	private String codPuntoEmision;
	private String tipoDocumento;
	private String tipoAmbiente;
	private String secuencial;
	private String formaEmision;
	private String Establecimiento;
	private String Caja;	
	public FacPuntoEmision(){
	this.ruc = "";
	this.codEstablecimiento = "";
	this.codPuntoEmision = "";
	this.tipoDocumento = "";
	this.tipoAmbiente = "";
	this.secuencial = "";
	this.formaEmision = "";
	this.Caja = "";
	}
	public FacPuntoEmision(String ruc,
						   String codEstablecimiento,
						   String codPuntoEmision,
						   String tipoDocumento,
						   String tipoAmbiente,
						   String secuencial,
						   String formaEmision,
						   String Caja, String Establecimiento){
		this.ruc = ruc;
		this.codEstablecimiento = codEstablecimiento;
		this.codPuntoEmision = codPuntoEmision;
		this.tipoDocumento = tipoDocumento;
		this.tipoAmbiente = tipoAmbiente;
		this.secuencial = secuencial;
		this.formaEmision = formaEmision;
		this.Caja = Caja;
		this.Establecimiento = Establecimiento;
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
	public String getCodPuntoEmision() {
		return codPuntoEmision;
	}
	public void setCodPuntoEmision(String codPuntoEmision) {
		this.codPuntoEmision = codPuntoEmision;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getTipoAmbiente() {
		return tipoAmbiente;
	}
	public void setTipoAmbiente(String tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}
	public String getSecuencial() {
		return secuencial;
	}
	public void setSecuencial(String secuencial) {
		this.secuencial = secuencial;
	}
	public String getFormaEmision() {
		return formaEmision;
	}
	public void setFormaEmision(String formaEmision) {
		this.formaEmision = formaEmision;
	}
	public String getCaja() {
		return Caja;
	}
	public void setCaja(String Caja) {
		this.Caja = Caja;
	}
	public String getEstablecimiento() {
		return Establecimiento;
	}
	public void setEstablecimiento(String Establecimiento) {
		this.Establecimiento = Establecimiento;
	}
	
}
