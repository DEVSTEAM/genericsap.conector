package com.cimait.DAO;

import java.util.ArrayList;

public class InfoFactura {

			
	private String fechaEmision;
	private String dirEstablecimiento;
	private String contribuyenteEspecial;
	private String obligadoContabilidad;
	private String tipoIdentificacionComprador;
	private String guiaRemision;
	private String razonSocialComprador;
	private String identificacionComprador;
	private Double totalSinImpuestos;
	private Double totalDescuento;
	
	private String codigoCliente;	
	private String emailCliente;

	
	
	private double propina;
	private double importeTotal;
	private String moneda;
	private double totalImpuestosIva;
	private double baseImponible;	
	private ArrayList<DetalleFactura> listDetFactura;
	private ArrayList<ImpuestosFactura> listTotalImpuestos;
	private ArrayList<InfoAdicional> listInfoAdicional;
	
	
			
	public String getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getDirEstablecimiento() {
		return dirEstablecimiento;
	}
	public void setDirEstablecimiento(String dirEstablecimiento) {
		this.dirEstablecimiento = dirEstablecimiento;
	}
	public String getContribuyenteEspecial() {
		return contribuyenteEspecial;
	}
	public void setContribuyenteEspecial(String contribuyenteEspecial) {
		this.contribuyenteEspecial = contribuyenteEspecial;
	}
	public String getObligadoContabilidad() {
		return obligadoContabilidad;
	}
	public void setObligadoContabilidad(String obligadoContabilidad) {
		this.obligadoContabilidad = obligadoContabilidad;
	}
	public String getTipoIdentificacionComprador() {
		return tipoIdentificacionComprador;
	}
	public void setTipoIdentificacionComprador(String tipoIdentificacionComprador) {
		this.tipoIdentificacionComprador = tipoIdentificacionComprador;
	}
	public String getGuiaRemision() {
		return guiaRemision;
	}
	public void setGuiaRemision(String guiaRemision) {
		this.guiaRemision = guiaRemision;
	}
	public String getRazonSocialComprador() {
		return razonSocialComprador;
	}
	public void setRazonSocialComprador(String razonSocialComprador) {
		this.razonSocialComprador = razonSocialComprador;
	}
	public String getIdentificacionComprador() {
		return identificacionComprador;
	}
	public void setIdentificacionComprador(String identificacionComprador) {
		this.identificacionComprador = identificacionComprador;
	}
	public Double getTotalSinImpuestos() {
		return totalSinImpuestos;
	}
	public void setTotalSinImpuestos(Double totalSinImpuestos) {
		this.totalSinImpuestos = totalSinImpuestos;
	}
	public Double getTotalDescuento() {
		return totalDescuento;
	}
	public void setTotalDescuento(Double totalDescuento) {
		this.totalDescuento = totalDescuento;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getEmailCliente() {
		return emailCliente;
	}
	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}
	public double getPropina() {
		return propina;
	}
	public void setPropina(double propina) {
		this.propina = propina;
	}
	public double getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public double getTotalImpuestosIva() {
		return totalImpuestosIva;
	}
	public void setTotalImpuestosIva(double totalImpuestosIva) {
		this.totalImpuestosIva = totalImpuestosIva;
	}
	public double getBaseImponible() {
		return baseImponible;
	}
	public void setBaseImponible(double baseImponible) {
		this.baseImponible = baseImponible;
	}
	public ArrayList<DetalleFactura> getListDetFactura() {
		return listDetFactura;
	}
	public void setListDetFactura(ArrayList<DetalleFactura> listDetFactura) {
		this.listDetFactura = listDetFactura;
	}	
	public ArrayList<ImpuestosFactura> getListTotalImpuestos() {
		return listTotalImpuestos;
	}
	public void setListTotalImpuestos(ArrayList<ImpuestosFactura> listTotalImpuestos) {
		this.listTotalImpuestos = listTotalImpuestos;
	}
	public ArrayList<InfoAdicional> getListInfoAdicional() {
		return listInfoAdicional;
	}
	public void setListInfoAdicional(ArrayList<InfoAdicional> listInfoAdicional) {
		this.listInfoAdicional = listInfoAdicional;
	}
	
}
