package com.cimait.microcontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cimait.DAO.DetalleFactura;
import com.cimait.DAO.FacEmpresa;
import com.cimait.DAO.FacPuntoEmision;
import com.cimait.DAO.ImpuestosFactura;
import com.cimait.DAO.InfoFactura;
import com.cimait.DAO.InfoTributaria;
import com.cimait.DAO.OperaFolio;

public class DBOperaTransaction {
	
	private static String userSchemaDb = "";	
	public ArrayList<OperaFolio> getFolio(Connection conDatabase, String resort, String folioType, int billNo, String status, int rowNum, String queueName){
	Connection Con = conDatabase;	
	ResultSet Rs= null;
	PreparedStatement pst = null;
	ArrayList<OperaFolio> ListFolios = new ArrayList<OperaFolio>();
	try{    	
    	String sql = " Select bill_no as bill_noOpera, lpad( bill_no, 9, '0') as bill_no, resort, business_date, bill_generation_date, insert_date, update_date, bill_generation_time, queue_name, folio_no " +
    				 " from " + userSchemaDb + "FOLIO$_TAX " +
    				 " where resort = ? " +
    				 " and status = ? " +
    				 " and folio_type in ? " +    				 
    				 " and bill_generation_date >= trunc(sysdate-4) and folio_type <> 'INTERNAL' " +
    				 " and rownum <= ? " +
    				 " and bill_no > ? " +
    				 //" and bill_no = 38834 " +
    				 " and QUEUE_NAME = ? " +
    				 " order by to_number(bill_no) asc ";
    	
    	pst = Con.prepareStatement(sql);
    	pst.setString(1, resort);
    	pst.setString(2, status);
    	pst.setString(3, folioType);
    	pst.setInt(4, rowNum);
    	pst.setInt(5, billNo);
    	pst.setString(6, queueName);
    	
    	Rs= pst.executeQuery();
    	while (Rs.next()){ 
    		OperaFolio folios = new OperaFolio(Rs.getString(1),Rs.getString(2),Rs.getString(3), Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7),Rs.getString(8),Rs.getString(9), Rs.getInt(10));
    		ListFolios.add(folios);
    	}
	}catch(Exception e){
    	e.printStackTrace();
	}finally {
    	try {
			Rs.close();
	    	pst.close();
	    	//Con.close();
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return ListFolios;
	}
	}

	public InfoFactura getFolioInfoTributariaXml(Connection conDatabase,  String resort, int billNo, String queueName, int folioNo){
		Connection Con = conDatabase;	
		ResultSet Rs= null;
		PreparedStatement pst = null;
		InfoFactura infoFacturaOpera = new InfoFactura();
		
		try{    	
	    	String sql = " SELECT ALL " +
	    				 " to_char(ft.BILL_GENERATION_DATE,'dd/mm/rrrr') as fechaEmision, " +
	    				 " ft.PAYEE_NAME_ID as codigoCliente, " +
	    				 " NVL(n.COMPANY, n.LAST||' '||n.FIRST) as nombreCliente, " +
	    				 " (Select decode(name_type,'D',Passport,nvl(n.tax1_no, n.passport)) from " + userSchemaDb + "name where name_id = ft.PAYEE_NAME_ID) identificacionComprador, " +
	    				 " (Select decode(name_type,'D','06','04') from " + userSchemaDb + "name where name_id = ft.PAYEE_NAME_ID) tipoIdentificacionComprador, " +
	    				 " " + userSchemaDb + "NAME_REF.get_phone_no(ft.PAYEE_NAME_ID,'EMAIL') as emailCliente, " +
	    				 " (FT.net1_amt) totalSinImpuesto, " +
	    				 //Revisar
	    				 " 0 totalDescuento, " +
	    				 //SERVICIO + TASA 
	    			     " round((FT.tax2_amt+FT.net3_amt),2) propina, " +
	    			     " round(nvl(ft.net1_amt,0) + nvl(ft.tax1_amt,0)+ nvl(ft.tax2_amt,0) +nvl(ft.net3_amt,0),2) importeTotal, " + 
	    			     //Parametrizar
	    			     " 'DOLAR' moneda, " +
	    			     " round((FT.tax1_amt),2) totalImpuestosIva, " +
	    			     " 0 baseImponible, " +
	    			     "  (Select LAST||' '||FIRST from name where NAME_ID = ft.NAME_ID) as huesped, " +
	    			     " ft.room as habitacion, " +
	    			     " to_char(ft.bill_start_date,'dd/mm/yyyy') as fecha_llegada, " +
	    			     " to_char(ft.bill_generation_date,'dd/mm/yyyy') as fecha_salida " +
	    			     /*
	    			     " ft.STATUS, " +
	    			     " u.APP_USER, " +	    			     
	    			     " pay_datos.address1,  " +
	    			     " pay_datos.city, " +
	    			     " pay_datos.state, " +
	    			     " pay_datos.country, " +
	    			     " nvl(n.tax1_no, n.passport)  tax1_no, " +
	    			     " ft.CASHPAY,  " +
	    			     " (ft.CLPAY+ft.CLARPAY) cmp, " +
	    			     " ft.CCPAY, " +
	    			     " (ft.TOTAL_GROSS-ft.CASHPAY-ft.CLPAY-ft.CLARPAY-ft.CCPAY) otros, " +
	    			     " (FT.net1_amt) VAL_NETO, " +
	    			     " (FT.tax1_amt) IVA, " +
	    			     " (FT.tax2_amt) SERVICIO, " +
	    			     " (FT.net3_amt) TASA, " +
	    			     " nvl(ft.net1_amt,0) + nvl(ft.tax1_amt,0)+ nvl(ft.tax2_amt,0) +nvl(ft.net3_amt,0) valfac " +          
	    			     */
	    			     " FROM " + userSchemaDb + "FOLIO$_TAX ft,  " +
	    			     " " + userSchemaDb + "NAME n,  " + 
	    			     " " + userSchemaDb + "APPLICATION$_USER u " +
	    			     " ,(select name_id ,address1, city, state , " +
	    			     " (Select country_name from " + userSchemaDb + "COUNTRY$ where country_code = COUNTRY) country " +
	    			     " from " + userSchemaDb + "name_address  " +
	    			     " where  primary_yn = 'Y') pay_datos " +
	    			     " WHERE ft.bill_no = ? " +
	    			     " AND (pay_datos.name_id = ft.PAYEE_NAME_ID ) " +
	    			     " AND (ft.PAYEE_NAME_ID = n.NAME_ID) " +
	    			     " AND (ft.RESORT=?) " +
	    			     " AND (ft.CASHIER_ID=u.DEF_CASHIER_ID) " +
	    			     " AND ft.QUEUE_NAME=? " +
	    			     " AND ft.FOLIO_NO=? " +
	    			     " AND NOT(ft.queue_name like 'MIC%' OR ft.queue_name like 'INT%'  OR ft.queue_name like 'FOL%') " +
	    			     " ORDER BY ft.BILL_GENERATION_DATE,  " +
	    			     " ft.QUEUE_NAME, " +
	    			     " ft.BILL_NO  ";
	    	
	    	pst = Con.prepareStatement(sql);
	    	
	    	pst.setInt(1, billNo);
	    	pst.setString(2, resort);
	    	pst.setString(3, queueName);
	    	pst.setInt(4, folioNo);
	    	
	    	Rs= pst.executeQuery();
	    	while (Rs.next()){
	    		infoFacturaOpera.setFechaEmision(Rs.getString("fechaEmision"));
	    		infoFacturaOpera.setCodigoCliente(Rs.getString("codigoCliente"));
	    		infoFacturaOpera.setRazonSocialComprador(Rs.getString("nombreCliente"));
	    		infoFacturaOpera.setIdentificacionComprador(Rs.getString("identificacionComprador"));
	    		infoFacturaOpera.setTipoIdentificacionComprador(Rs.getString("tipoIdentificacionComprador"));
	    		infoFacturaOpera.setEmailCliente(Rs.getString("emailCliente"));
	    		infoFacturaOpera.setTotalSinImpuestos(Rs.getDouble("totalSinImpuesto"));
	    		infoFacturaOpera.setTotalDescuento(Rs.getDouble("totalDescuento"));
	    		infoFacturaOpera.setPropina(Rs.getDouble("propina"));
	    		infoFacturaOpera.setImporteTotal(Rs.getDouble("importeTotal"));
	    		infoFacturaOpera.setMoneda(Rs.getString("moneda"));
	    		infoFacturaOpera.setTotalImpuestosIva(Rs.getDouble("totalImpuestosIva"));
	    		infoFacturaOpera.setBaseImponible(Rs.getDouble("baseImponible"));
	    		/*
	    		infoFacturaOpera.setHuesped(Rs.getString("huesped"));
	    		infoFacturaOpera.setHabitacion(Rs.getString("habitacion"));
	    		infoFacturaOpera.setFechaLlegada(Rs.getString("fecha_llegada"));
	    		infoFacturaOpera.setFechaSalida(Rs.getString("fecha_salida"));*/
	    	}
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
				Rs.close();
		    	pst.close();
		    	//Con.close();
	    	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return infoFacturaOpera;
		}
		}
	
	public InfoFactura getDetalleFolioInfoFactura(Connection conDatabase, String resort, int billNo, String queueName, int folioNo){
		Connection Con = conDatabase;	
		ResultSet Rs= null;
		PreparedStatement pst = null;
		InfoFactura infoFacturaOpera = new InfoFactura();
		ArrayList<DetalleFactura> listDetFactura = new ArrayList<DetalleFactura>();
		
		
		try{    	
	    	String sql = " Select code codigoPrincipal , code_secundario codigoAuxiliar, descripcion, " +
	    				 " sum(cantidad) cantidad, round(sum(precio_unitario)/sum(cantidad),2) precioUnitario, sum(descuento) descuento, " +
	    				 " round(sum(precio_total_sin_descuento),2) precioTotalSinImpuesto, " +
	    				 " sum(iva) valorIva " +
	    				 " from ( " +  
	    				 " Select trx_code as code, tc_group||'-'||tc_subgroup code_secundario, " + 
	    				 " (select description from " + userSchemaDb + "TC$_SUBGROUPS g where g.tc_group = f.tc_group and g.tc_subgroup = f.tc_subgroup and f.resort = g.resort) as descripcion, " +
	    				 " nvl(quantity,0) cantidad, " +
	    				 " round(nvl(trx_amount,0)/nvl(quantity,1),2) precio_unitario, " +
	    				 " nvl(0,0) descuento, " +
	    				 " round(nvl(trx_amount,0),2) precio_total_sin_descuento, " +
	    	                //--f.trx_no, 
	    	                //--f.trx_no_added_by,
	    	             " ( Select " + 
	    	             " round(sum(nvl(trx_amount,0)),2) precio_total_sin_descuento " +
	    	             " from " + userSchemaDb + "financial_transactions fi " +
	    	             " where bill_no = ? " +
	    	             " and resort = ? " +
	    	             " and trx_no_added_by =f.trx_no " +
	    	             " and trx_no_added_by is not null " +
	    	             " and trx_code = '9555' " +
	    	             " and tc_group = 'TAX') iva " +
	    	             " from " + userSchemaDb + "financial_transactions f " +       
	    	             " where bill_no = ? " +
	    	             " and resort = ? " +
	    	             " and tc_group <> 'PAGO' " +
	    	             " and tc_group <> 'TAX' " +
	    	             " and queue_name = ? " +
	    	             " and folio_no = ? " +
	    	             " ) detalle  " +
	    	             " group by code, code_secundario, descripcion ";
	    	
	    	pst = Con.prepareStatement(sql);
	    	
	    	pst.setInt(1, billNo);
	    	pst.setString(2, resort);
	    	pst.setInt(3, billNo);
	    	pst.setString(4, resort);	    	
	    	pst.setString(5, queueName);
	    	pst.setInt(6, folioNo);
	    	
	    	Rs= pst.executeQuery();
	    	listDetFactura.clear();
	    	//infoFacturaOpera.getListDetFactura().clear();
	    	
	    	while (Rs.next()){
	    		ArrayList<ImpuestosFactura> listImpuestosFactura = new ArrayList<ImpuestosFactura>();   			    			    		
	    		ImpuestosFactura detImpuesto = new ImpuestosFactura();
	    		DetalleFactura detFactura = new DetalleFactura();
	    		
	    		detFactura.setCodigoPrincipal(Rs.getString("codigoPrincipal"));
	    		detFactura.setCodigoAuxiliar(Rs.getString("codigoAuxiliar"));
	    		detFactura.setDescripcion(Rs.getString("descripcion"));
	    		detFactura.setCantidad(Rs.getInt("cantidad"));
	    		detFactura.setPrecioUnitario(Rs.getDouble("precioUnitario"));
	    		detFactura.setDescuento(Rs.getDouble("descuento"));
	    		detFactura.setPrecioTotalSinImpuesto(Rs.getDouble("precioTotalSinImpuesto"));
	    		/*
	    		detImpuesto.setCodigo(2);
	    		detImpuesto.setCodigoPorcentaje(2);
	    		detImpuesto.setTarifa(12);
	    		detImpuesto.setBaseImponible(Rs.getDouble("precioTotalSinImpuesto"));
	    		detImpuesto.setValor(Rs.getDouble("valorIva"));*/
	    		
	    		listImpuestosFactura.clear();	    		
	    		listImpuestosFactura.add(detImpuesto);
	    		listImpuestosFactura.get(0).setCodigo(2);
	    		listImpuestosFactura.get(0).setCodigoPorcentaje(2);
	    		listImpuestosFactura.get(0).setTarifa(12);
	    		listImpuestosFactura.get(0).setBaseImponible(Rs.getDouble("precioTotalSinImpuesto"));
	    		listImpuestosFactura.get(0).setValor(Rs.getDouble("valorIva"));
	    		detFactura.setListImpuestos(listImpuestosFactura);
	    		
	    		listDetFactura.add(detFactura);
	    		
	    	}
	    	infoFacturaOpera.setListDetFactura(listDetFactura);
	    	/*Rs= pst.executeQuery();
	    	int indice = -1;
	    	while (Rs.next()){
	    		indice++;
	    		listImpuestosFactura.clear();
	    		ImpuestosFactura detImpuesto = new ImpuestosFactura();
	    		detImpuesto.setCodigo(2);
	    		detImpuesto.setCodigoPorcentaje(2);
	    		detImpuesto.setTarifa(12);
	    		detImpuesto.setBaseImponible(Rs.getDouble("precioTotalSinImpuesto"));
	    		detImpuesto.setValor(Rs.getDouble("valorIva"));
	    		listImpuestosFactura.add(detImpuesto);
	    		infoFacturaOpera.getListDetFactura().get(indice).setListImpuestos(listImpuestosFactura);
	    	}*/
		}catch(Exception e){
	    	e.printStackTrace();
		}finally {
	    	try {
				Rs.close();
		    	pst.close();
		    	//Con.close();
	    	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return infoFacturaOpera;
		}
		}	
}
