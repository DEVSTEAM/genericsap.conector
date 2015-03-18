package com.cimait.DAO;

public class ImpuestosFactura {
	private int codigo;
	private int codigoPorcentaje;
	private int tarifa;
	private double baseImponible;
	private double valor;
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public int getCodigoPorcentaje() {
		return codigoPorcentaje;
	}
	public void setCodigoPorcentaje(int codigoPorcentaje) {
		this.codigoPorcentaje = codigoPorcentaje;
	}
	public int getTarifa() {
		return tarifa;
	}
	public void setTarifa(int tarifa) {
		this.tarifa = tarifa;
	}
	public double getBaseImponible() {
		return baseImponible;
	}
	public void setBaseImponible(double baseImponible) {
		this.baseImponible = baseImponible;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	
	
}
