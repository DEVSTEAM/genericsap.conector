package com.cimait.microcontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cimait.DAO.DetalleFactura;
import com.cimait.DAO.Transaction;
import com.cimait.DAO.FacEmpresa;
import com.cimait.DAO.FacPuntoEmision;
import com.cimait.DAO.ImpuestosFactura;
import com.cimait.DAO.InfoAdicional;
import com.cimait.DAO.InfoCredito;
import com.cimait.DAO.InfoFactura;
import com.cimait.DAO.InfoTributaria;
import com.cimait.runtime.ConexionBase;
import com.cimait.runtime.Environment;

public class DBTransaction {
	
	//private static String userSchemaDb = "cicf10.dbo.";
	private static String userSchemaDb = Environment.c.getString("Empresa.DB.Empresa.userSchemaDb");

	public static ArrayList<Transaction> getTrx(int rownum, String tipoDocumento, String sucursal) {
		
		//Connection Con = conDatabase;
		//VPI - GBA
		Connection Con = ConexionBase.DBManager.get();
		
		ResultSet Rs = null;
		PreparedStatement pst = null;
		ArrayList<Transaction> ListTrx = new ArrayList<Transaction>();
		try {
			String sql = Environment.c
					.getString("Empresa.DB.Empresa.sql.getTrx");
			sql = sql.replace("[rownum]", new Integer(rownum).toString());
			sql = sql.replace("[ESQUEMA]", userSchemaDb);
			sql = sql.replace("[TIPO_DOCUMENTO]", tipoDocumento);
			/*
			 * Select Top 100 cnusecuencia as secuencial, NIdPvMovimiento as
			 * idmovimiento, DFxEmision as fechaEmision, CCiTipoDocumento as
			 * tipoDocumento,CCiSucursal, CciPtoEmision from
			 * [ESQUEMA]tblpvcabmovimiento x where CCiTipoDocumento
			 * in([TIPO_DOCUMENTO]) and FElEstado is null and cnusecuencia is
			 * not null and cnusecuencia &lt;&gt; '' and cciSucursal = ? order
			 * by NIdPvMovimiento asc
			 */

			System.out.println("SQL:: " + sql);
			pst = Con.prepareStatement(sql);
			pst.setString(1, sucursal);
			Rs = pst.executeQuery();
			while (Rs.next()) {
				// System.out.println("Fecha:"+Rs.getString(3));
				Transaction trx = new Transaction(Rs.getString(2),
						Rs.getString(1), Rs.getString(4), Rs.getString(3),
						Rs.getString(5), Rs.getString(6));
				ListTrx.add(trx);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Rs.close();
				pst.close();
				//Con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return ListTrx;
	}

	public static int UpdateEstadoTrx(//Connection conDatabase,
									  String idMovimiento, 
									  String estado) throws Exception{
		
		Connection Con = ConexionBase.DBManager.get();	
		int li_result=0;
		PreparedStatement pst = null;
		try{    	
			String sql = Environment.c.getString("Empresa.DB.Empresa.sql.UpdateEstadoTrx");
			sql = sql.replace("[ESQUEMA]", userSchemaDb);
			
	    	pst = Con.prepareStatement(sql);
	    	
	    	pst.setString(1, estado);	    
	    	pst.setString(2, idMovimiento);
	    	
	    	li_result = pst.executeUpdate();	    	
	    	
		}catch(Exception e){
	    	e.printStackTrace();
	    	throw new Exception("ERRORDATABASE,UpdateEstadoTrx Error al Actualizar el movimiento::"+idMovimiento+"->"+e.toString());
		}finally {
	    	try {
		    	pst.close();
		    	//Con.close();
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return li_result;
	}
	
	// VPI - Insertara en la tabla temporal las transacciones tomadas
	public static int insertFactElectronicaTmp(//Connection conDatabase,
			String idMovimiento, String estado, String tipoMovimiento,
			String establecimiento, String ptoEmision, String secuencial)
			throws Exception {

		//Connection Con = conDatabase;
		Connection Con = ConexionBase.DBManager.get();
		int li_result = 0;
		PreparedStatement pst = null;
		try {
			String sql = Environment.c
					.getString("Empresa.DB.Empresa.sql.insertFactElectronicaTmp");
			sql = sql.replace("[ESQUEMA]", userSchemaDb);

			pst = Con.prepareStatement(sql);

			pst.setString(1, idMovimiento);
			pst.setString(2, tipoMovimiento);
			pst.setString(3, estado);
			pst.setString(4, establecimiento);
			pst.setString(5, ptoEmision);
			pst.setString(6, secuencial);

			li_result = pst.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"ERRORDATABASE,UpdateEstadoTrx Error al Insertar el movimiento en Tabla Temporal::"
							+ idMovimiento + "->" + e.toString());
		} finally {
			try {
				pst.close();
				//Con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return li_result;
	}
	
	
	public static InfoFactura getTrxInfoTributariaXml(String codlocal, String codcaja, String idMovimiento,
			String secuencial, String tipoDocumento) {
		
		//Connection Con = conDatabase;
		Connection Con = ConexionBase.DBManager.get();
		ResultSet Rs = null;
		PreparedStatement pst = null;
		InfoFactura infoFacturaTrx = new InfoFactura();

		ArrayList<InfoAdicional> listInfoAdic = new ArrayList<InfoAdicional>();
		try {
			String sql = Environment.c
					.getString("Empresa.DB.Empresa.sql.InfoTributariaXmlTrx");
			sql = sql.replace("[ESQUEMA]", userSchemaDb);

			pst = Con.prepareStatement(sql);

			pst.setString(1, codlocal);
			pst.setString(2, codcaja);
			pst.setString(3, idMovimiento);
			pst.setString(4, secuencial);
			pst.setString(5, tipoDocumento);

			Rs = pst.executeQuery();
			while (Rs.next()) {
				infoFacturaTrx.setFechaEmision(Rs.getString("fechaEmision"));
				infoFacturaTrx.setCodigoCliente(Rs.getString("codigoCliente"));
				infoFacturaTrx.setRazonSocialComprador(Rs
						.getString("nombreCliente"));
				infoFacturaTrx.setIdentificacionComprador(Rs
						.getString("identificacionComprador"));
				infoFacturaTrx.setTipoIdentificacionComprador(Rs
						.getString("tipoIdentificacionComprador"));
				infoFacturaTrx.setEmailCliente(Rs.getString("emailCliente"));
				infoFacturaTrx.setTotalSinImpuestos(Rs
						.getDouble("totalSinImpuesto"));
				infoFacturaTrx
						.setTotalDescuento(Rs.getDouble("totalDescuento"));
				infoFacturaTrx.setPropina(Rs.getDouble("propina"));
				infoFacturaTrx.setImporteTotal(Rs.getDouble("importeTotal"));
				infoFacturaTrx.setMoneda(Rs.getString("moneda"));
				infoFacturaTrx.setTotalImpuestosIva(Rs
						.getDouble("totalImpuestosIva"));

				if ((Rs.getString("direccion").length() > 0)
						|| (Rs.getString("direccion") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("DIRECCION",
							Rs.getString("direccion"));
					listInfoAdic.add(infoAdic);
				}
				if ((Rs.getString("telefono").length() > 0)
						|| (Rs.getString("telefono") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("TELEFONO",
							Rs.getString("telefono"));
					listInfoAdic.add(infoAdic);
				}
				if ((Rs.getString("emailCliente").length() > 0)
						|| (Rs.getString("emailCliente") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("EMAIL",
							Rs.getString("emailCliente"));
					listInfoAdic.add(infoAdic);
				}
				if ((Rs.getString("codigoCliente").length() > 0)
						|| (Rs.getString("codigoCliente") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("CLIENTE",
							Rs.getString("codigoCliente"));
					listInfoAdic.add(infoAdic);
				}

				// VPI se agregan campos de informacion adicional
				if ((Rs.getString("vendedor").length() > 0)
						|| (Rs.getString("vendedor") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("VENDEDOR",
							Rs.getString("vendedor"));
					listInfoAdic.add(infoAdic);
				}

				if ((Rs.getString("origenDocumento").length() > 0)
						|| (Rs.getString("origenDocumento") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("ORIGEN",
							Rs.getString("origenDocumento"));
					listInfoAdic.add(infoAdic);
				}

			}
			infoFacturaTrx.setListInfoAdicional(listInfoAdic);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Rs.close();
				pst.close();
				//Con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return infoFacturaTrx;
	}

	public static InfoCredito getTrxInfoNotaTributariaXml(
			//Connection conDatabase, 
			String codlocal, String codcaja,
			String idMovimiento, String secuencial, String tipoDocumento) {

		//Connection Con = conDatabase;
		Connection Con = ConexionBase.DBManager.get();
		ResultSet Rs = null;
		PreparedStatement pst = null;
		InfoCredito infoCreditoTrx = new InfoCredito();

		ArrayList<InfoAdicional> listInfoAdic = new ArrayList<InfoAdicional>();
		try {
			String sql = Environment.c
					.getString("Empresa.DB.Empresa.sql.InfoNotaTributariaXmlTrx");
			sql = sql.replace("[ESQUEMA]", userSchemaDb);
			pst = Con.prepareStatement(sql);

			pst.setString(1, codlocal);
			pst.setString(2, codcaja);
			pst.setString(3, idMovimiento);
			pst.setString(4, secuencial);
			pst.setString(5, tipoDocumento);
			Rs = pst.executeQuery();
			while (Rs.next()) {
				infoCreditoTrx.setFechaEmision(Rs.getString("fechaEmision"));
				infoCreditoTrx.setCodigoCliente(Rs.getString("codigoCliente"));
				infoCreditoTrx.setRazonSocialComprador(Rs
						.getString("nombreCliente"));
				infoCreditoTrx.setIdentificacionComprador(Rs
						.getString("identificacionComprador"));
				infoCreditoTrx.setTipoIdentificacionComprador(Rs
						.getString("tipoIdentificacionComprador"));
				infoCreditoTrx.setEmailCliente(Rs.getString("emailCliente"));
				infoCreditoTrx.setTotalSinImpuestos(Rs
						.getDouble("totalSinImpuesto"));
				//VPI 
				infoCreditoTrx.setValorModificacion(Rs
						.getDouble("valorModificacion"));
				///
				
				infoCreditoTrx
						.setTotalDescuento(Rs.getDouble("totalDescuento"));
				infoCreditoTrx.setPropina(Rs.getDouble("propina"));
				infoCreditoTrx.setImporteTotal(Rs.getDouble("importeTotal"));
				infoCreditoTrx.setMoneda(Rs.getString("moneda"));
				infoCreditoTrx.setTotalImpuestosIva(Rs
						.getDouble("totalImpuestosIva"));
				infoCreditoTrx.setMotivo(Rs.getString("motivo"));
				infoCreditoTrx.setCodDocModificado(Rs
						.getString("codDocModificado"));
				infoCreditoTrx.setNumDocModificado(Rs
						.getString("numDocModificado"));
				infoCreditoTrx.setFechaEmisionDocSustento(Rs
						.getString("fechaEmisionDocSustento"));

				if ((Rs.getString("direccion").length() > 0)
						|| (Rs.getString("direccion") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("DIRECCION",
							Rs.getString("direccion"));
					listInfoAdic.add(infoAdic);
				}
				if ((Rs.getString("telefono").length() > 0)
						|| (Rs.getString("telefono") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("TELEFONO",
							Rs.getString("telefono"));
					listInfoAdic.add(infoAdic);
				}
				if ((Rs.getString("emailCliente").length() > 0)
						|| (Rs.getString("emailCliente") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("EMAIL",
							Rs.getString("emailCliente"));
					listInfoAdic.add(infoAdic);
				}
				
				if ((Rs.getString("codigoCliente").length() > 0)
						|| (Rs.getString("codigoCliente") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("CLIENTE",
							Rs.getString("codigoCliente"));
					listInfoAdic.add(infoAdic);
				}
				//VPI se agregan campos de informacion adicional 
				if ((Rs.getString("vendedor").length() > 0)
						|| (Rs.getString("vendedor") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("VENDEDOR",
							Rs.getString("vendedor"));
					listInfoAdic.add(infoAdic);
				}
				if ((Rs.getString("origenDocumento").length() > 0)
						|| (Rs.getString("origenDocumento") != null)) {
					InfoAdicional infoAdic = new InfoAdicional("ORIGEN",
							Rs.getString("origenDocumento"));
					listInfoAdic.add(infoAdic);
				}
				
				
			}
			infoCreditoTrx.setListInfoAdicional(listInfoAdic);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Rs.close();
				pst.close();
				//Con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return infoCreditoTrx;
	}

	public static ArrayList<ImpuestosFactura> getTotalImpuestoTrxInfoFactura(//Connection conDatabase, 
																			 String idMovimiento){
		
		//Connection Con = conDatabase;	
		Connection Con = ConexionBase.DBManager.get();
		ResultSet Rs= null;
		PreparedStatement pst = null;
		InfoFactura infoFacturaOpera = new InfoFactura();
		ArrayList<ImpuestosFactura> listTotImpuestosFactura = new ArrayList<ImpuestosFactura>();
		
		try{  
			String sql = Environment.c.getString("Empresa.DB.Empresa.sql.getTotalImpuestoTrxInfoFactura");
			sql = sql.replace("[ESQUEMA]", userSchemaDb);			
	    	pst = Con.prepareStatement(sql);
	    	//CCiSucursal = ? and CciPtoEmision = ? and NIdPvMovimiento  = ? and CNuSecuencia = ? and CCiTipoDocumento = ?
	    	
	    	pst.setString(1, idMovimiento);
	    	
	    	
	    	Rs= pst.executeQuery();
	    	listTotImpuestosFactura.clear();
	    	while (Rs.next()){
	    		ImpuestosFactura TotImp = new ImpuestosFactura();
	    		TotImp.setCodigo(Integer.parseInt(Rs.getString(1)));
	    		TotImp.setCodigoPorcentaje(Integer.parseInt(Rs.getString(2)));
	    		TotImp.setBaseImponible(Rs.getDouble(3));
	    		TotImp.setValor(Rs.getDouble(4));
	    		listTotImpuestosFactura.add(TotImp);	    		
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

		return listTotImpuestosFactura;
	}
	public static ArrayList<DetalleFactura> getDetalleTrxInfoFactura(//Connection conDatabase,
																	 String idMovimiento){
		//Connection Con = conDatabase;
		Connection Con = ConexionBase.DBManager.get();
		ResultSet Rs= null, RsImp= null;
		PreparedStatement pst = null;
		
		ArrayList<DetalleFactura> listDetFactura = new ArrayList<DetalleFactura>();
		
		
		try{    
			String sql = Environment.c.getString("Empresa.DB.Empresa.sql.getDetalleTrxInfoFactura");
			sql = sql.replace("[ESQUEMA]", userSchemaDb);
			String sqlImp = Environment.c.getString("Empresa.DB.Empresa.sql.getDetalleTrxInfoFacturaImp");
			sqlImp = sqlImp.replace("[ESQUEMA]", userSchemaDb);
	    	pst = Con.prepareStatement(sql);	    	
	    	//CCiSucursal = ? and CciPtoEmision = ? and NIdPvMovimiento  = ? and CNuSecuencia = ? and CCiTipoDocumento = ?
	    	
	    	pst.setString(1, idMovimiento);
	    	
	    	
	    	Rs= pst.executeQuery();
	    	listDetFactura.clear();
	    	
	    	while (Rs.next()){
	    		ArrayList<ImpuestosFactura> listImpuestosFactura = new ArrayList<ImpuestosFactura>();   			    			    		
	    		
	    		DetalleFactura detFactura = new DetalleFactura();
	    		detFactura.setCodigoPrincipal(Rs.getString("codigoPrincipal"));
	    		//detFactura.setCodigoAuxiliar(Rs.getString("codigoAuxiliar"));
	    		detFactura.setDescripcion(Rs.getString("descripcion"));
	    		System.out.println("Cantidad::"+Rs.getString("cantidad"));
	    		detFactura.setCantidad(Double.parseDouble(Rs.getString("cantidad")));
	    		detFactura.setPrecioUnitario(Rs.getDouble("precioUnitario"));
	    		detFactura.setDescuento(Rs.getDouble("descuento"));
	    		detFactura.setPrecioTotalSinImpuesto(Rs.getDouble("precioTotalSinImpuesto"));
	    		//VPI -  Se comenta para soportar items negativos y los productos se agrupen por codigo
	    		//detFactura.setSecuencia(Rs.getInt("secuencia"));
	    		double baseImponible = detFactura.getPrecioTotalSinImpuesto()-detFactura.getDescuento();	    		

	    			pst = Con.prepareStatement(sqlImp);		    		
	    			//CCiSucursal = ? and CciPtoEmision = ? and NIdPvMovimiento  = ? and CNuSecuencia = ? and CCiTipoDocumento = ?
	    	    	
	    	    	pst.setString(1, idMovimiento);
	    	    	pst.setString(2,detFactura.getCodigoPrincipal());
	    	    	//VPI -  Se comenta para soportar items negativos y los productos se agrupen por codigo
	    	    	//pst.setInt(3,detFactura.getSecuencia());
				   	
				   	RsImp= pst.executeQuery();	
				   	
				   	listImpuestosFactura.clear();
				   	while (RsImp.next()){
				   		ImpuestosFactura detImpuesto = new ImpuestosFactura();
				   		detImpuesto.setCodigo(Integer.parseInt(RsImp.getString(1)));
				   		detImpuesto.setCodigoPorcentaje(Integer.parseInt(RsImp.getString(2)));
				   		detImpuesto.setTarifa(Integer.parseInt(RsImp.getString(3)));
				   		detImpuesto.setBaseImponible(RsImp.getDouble(4));
				   		detImpuesto.setValor(RsImp.getDouble(5));
				   		
				   		listImpuestosFactura.add(detImpuesto);
				   	}
				   	detFactura.setListImpuestos(listImpuestosFactura);	    		
	    		
	    		listDetFactura.add(detFactura);
	    		
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
		return listDetFactura;
		}	

	public static ArrayList<DetalleFactura> getNotaDetalleTrxInfoFactura(
			//Connection conDatabase, 
			String idMovimiento) {
		
		//Connection Con = conDatabase;
		Connection Con = ConexionBase.DBManager.get();
		ResultSet Rs = null, RsImp = null;
		PreparedStatement pst = null;

		ArrayList<DetalleFactura> listDetFactura = new ArrayList<DetalleFactura>();

		try {
			String sql = Environment.c
					.getString("Empresa.DB.Empresa.sql.getNotaDetalleTrxInfoFactura");
			sql = sql.replace("[ESQUEMA]", userSchemaDb);
			String sqlImp = Environment.c
					.getString("Empresa.DB.Empresa.sql.getDetalleTrxInfoFacturaImp");
			sqlImp = sqlImp.replace("[ESQUEMA]", userSchemaDb);
			pst = Con.prepareStatement(sql);
			// CCiSucursal = ? and CciPtoEmision = ? and NIdPvMovimiento = ? and
			// CNuSecuencia = ? and CCiTipoDocumento = ?

			pst.setString(1, idMovimiento);

			Rs = pst.executeQuery();
			listDetFactura.clear();

			while (Rs.next()) {
				ArrayList<ImpuestosFactura> listImpuestosFactura = new ArrayList<ImpuestosFactura>();

				DetalleFactura detFactura = new DetalleFactura();
				detFactura.setCodigoPrincipal(Rs.getString("codigoPrincipal"));
				// detFactura.setCodigoAuxiliar(Rs.getString("codigoAuxiliar"));
				detFactura.setDescripcion(Rs.getString("descripcion"));
				System.out.println("Cantidad::" + Rs.getString("cantidad"));
				detFactura.setCantidad(Double.parseDouble(Rs
						.getString("cantidad")));
				detFactura.setPrecioUnitario(Rs.getDouble("precioUnitario"));
				detFactura.setDescuento(Rs.getDouble("descuento"));
				detFactura.setPrecioTotalSinImpuesto(Rs
						.getDouble("precioTotalSinImpuesto"));
				//VPI -  Se comenta para soportar items negativos y los productos se agrupen por codigo
				//detFactura.setSecuencia(Rs.getInt("secuencia"));
				double baseImponible = detFactura.getPrecioTotalSinImpuesto()
						- detFactura.getDescuento();

				pst = Con.prepareStatement(sqlImp);
				// CCiSucursal = ? and CciPtoEmision = ? and NIdPvMovimiento = ?
				// and CNuSecuencia = ? and CCiTipoDocumento = ?

				pst.setString(1, idMovimiento);
				pst.setString(2, detFactura.getCodigoPrincipal());
				//VPI -  Se comenta para soportar items negativos y los productos se agrupen por codigo
				//pst.setInt(3, detFactura.getSecuencia());

				RsImp = pst.executeQuery();

				listImpuestosFactura.clear();
				while (RsImp.next()) {
					ImpuestosFactura detImpuesto = new ImpuestosFactura();
					detImpuesto.setCodigo(Integer.parseInt(RsImp.getString(1)));
					detImpuesto.setCodigoPorcentaje(Integer.parseInt(RsImp
							.getString(2)));
					detImpuesto.setTarifa(Integer.parseInt(RsImp.getString(3)));
					detImpuesto.setBaseImponible(RsImp.getDouble(4));
					detImpuesto.setValor(RsImp.getDouble(5));

					listImpuestosFactura.add(detImpuesto);
				}
				detFactura.setListImpuestos(listImpuestosFactura);

				listDetFactura.add(detFactura);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Rs.close();
				pst.close();
				// Con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listDetFactura;
	}
}
