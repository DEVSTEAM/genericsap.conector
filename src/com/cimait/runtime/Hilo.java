package com.cimait.runtime;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//Propias del Programador
import com.cimait.runtime.Environment;
import com.cimait.xml.CreditoXml;
import com.cimait.xml.FacturaXml;
import com.cimait.microcontainer.DBTransaction;
import com.cimait.microcontainer.DBFacElectronica;
import com.cimait.microcontainer.GenericTransaction;
import com.cimait.DAO.DetalleFactura;
import com.cimait.DAO.FacEmpresa;
import com.cimait.DAO.FacEstablecimiento;
import com.cimait.DAO.FacPuntoEmision;
import com.cimait.DAO.ImpuestosFactura;
import com.cimait.DAO.InfoAdicional;
import com.cimait.DAO.InfoCredito;
import com.cimait.DAO.InfoFactura;
import com.cimait.Util.CtrlFile;
import com.cimait.Util.Util;

public class Hilo extends GenericTransaction{

	private CtrlFile cf = Environment.cf;
	//private Connection opera=null;
	//private Connection empresa=null;
	private Connection facturacion=null;
	
	private PreparedStatement stmt = null;
	
	
	private ResultSet rs = null;
	private CallableStatement cs = null;
    private int flagbill = 0;
	
	private int threadId;
	private FacPuntoEmision puntoEmision;	
	private FacEstablecimiento establecimiento;
	
	//private static String configId = "HiloEmpresa";
	private static String ruc;
	private String transactionId;
	private String idMovimiento;
	private String secuencial;
	private String fechaEmision;
	private String tipoDocumento;
	private String local;
	private String caja;
	private String version;
	
	public Hilo(int threadId, String ruc, String idMovimiento,
			String secuencial, String fechaEmision, String tipoDocumento,
			String local, String caja, String version) {
		
		this.threadId = threadId;
		classReference = "EmpresaHilo";
		this.ruc = ruc;
		this.secuencial = secuencial;
		this.fechaEmision = fechaEmision;
		this.tipoDocumento = tipoDocumento;
		this.idMovimiento = idMovimiento;
		this.local = local;
		this.caja = caja;
		this.version = version;
		// setLogger();
	}

	@Override
	//Heredado de la Clase GenericTransaction
	public void run() {
		Thread.currentThread().getName();
		Thread.currentThread().getId();
		int flag = 0;
		//VPI para caso especial de Avicola feranandez en error recurente en 
		//"Tipo de Identificacion is Null" --> Al reprocesar no daba el error 
		//Para evitar que quede marcado el registro y vuelva a ser tomado
		boolean flagInsertaError = true;
		//int sleepBloque = 2000;		
		System.out.print("\nServiceConectorFactura::->run::Iniciando Service... ");						
		try{
		//Inicializacion de la conexion con la Base de Datos
		
		
		//VPI - GBA
		//empresa = getConnectionBD(empresa,"Empresa", "SQLServer");
		//empresa = ConexionBase.DBManager.get();
		
		facturacion =	ConexionBase.getConnectionBD(facturacion,"Invoice", "SQLServer");
		/*
		if (empresa == null){				
			throw new Exception ("ERRORGENERAL,getConnectionBD,No se logro la conexion a la base panacea");
		}
		*/
		if (facturacion == null){				
			throw new Exception ("ERRORGENERAL,getConnectionBD,No se logro la conexion a la base facturacion");
		}		
			flag=1;
		}catch(Exception e){
			flag=0;
		}
		if (flag==1)		
		try {		
				System.out.println("Ruc::"+this.getRuc());
			    FacEmpresa empresa = DBFacElectronica.getRucEmpresa(facturacion, getRuc());
				if (empresa == null){				
					throw new Exception("ERRORBITACORA,getRucEmpresa La Empresa no existe::"+getRuc()+",FacturaElectronica");
				}				
				
				Integer.parseInt(getSecuencial());
				flagbill=1;					
				
				if (flagbill == 1)
				
						establecimiento=DBFacElectronica.getEstablecimiento( facturacion, 
																			getRuc(),
																			getLocal());
						
						if (establecimiento==null){
							throw new Exception("ERRORBITACORA,getEstablecimiento El Establecimiento no existe::"+getLocal()+",Invoice");
						}						
						puntoEmision=DBFacElectronica.getPuntoEmision(facturacion, 
																	  getRuc(), 
																	  establecimiento.getCodEstablecimiento(), 
																	  getCaja(),
																	  getLocal());
						if (puntoEmision==null){
							throw new Exception("ERRORBITACORA,getPuntoEmision El Punto de Emision no existe::"+getCaja()+",Invoice");
						}	
						System.out.println("::------------Informacion General de Bloque-------------::");
						System.out.println("Fecha de Emision::"+this.getFechaEmision());
						System.out.println("IdMovimiento::"+this.getIdMovimiento());
						System.out.println("Secuencial::"+this.getSecuencial());
						System.out.println("TipoDocumento::"+this.getTipoDocumento());
						System.out.println("Local::"+this.getLocal());						
						System.out.println("Caja::"+this.getCaja());						
						System.out.println("::-------------------------::");
						
						if (this.getTipoDocumento().equals("01")){
							InfoFactura infFac = new InfoFactura(); 
							infFac = DBTransaction.getTrxInfoTributariaXml(getLocal(), 
																				getCaja(), 
																				getIdMovimiento(),
																				getSecuencial(), 
																				getTipoDocumento());
							
							if (infFac==null)
								throw new Exception("ERRORBITACORA,getTrxInfoTributariaXml infFac is null >> movimiento : "+getIdMovimiento());
							
							if (infFac.getTipoIdentificacionComprador() == null){
								flagInsertaError = false;
								throw new Exception("ERRORBITACORA,getTrxInfoTributariaXml Error Tipo de Identificacion is Null >> movimiento : "
													+getIdMovimiento());

							}
							infFac.getListInfoAdicional().add(new InfoAdicional("OFICINA", establecimiento.getDirEstablecimiento()));
							infFac.getListInfoAdicional().add(new InfoAdicional("CAJA", puntoEmision.getCaja()));
							infFac.getListInfoAdicional().add(new InfoAdicional("MOVIMIENTO", getIdMovimiento()));

							infFac.setDirEstablecimiento(establecimiento.getDirEstablecimiento());
							infFac.setContribuyenteEspecial(new Integer(empresa.getContribEspecial()).toString());
							infFac.setObligadoContabilidad(empresa.getObligContabilidad());						
						
							
							System.out.println("::------------Informacion Factura General-------------::");
							System.out.println("fechaEmision::"+infFac.getFechaEmision());
							System.out.println("dirEstablecimiento::"+infFac.getDirEstablecimiento());
							System.out.println("contribuyenteEspecial::"+infFac.getContribuyenteEspecial());
							System.out.println("obligadoContabilidad::"+infFac.getObligadoContabilidad());
							System.out.println("tipoIdentificacionComprador::"+infFac.getTipoIdentificacionComprador());
							System.out.println("guiaRemision::"+infFac.getGuiaRemision());
							System.out.println("razonSocialComprador::"+infFac.getRazonSocialComprador());
							System.out.println("identificacionComprador::"+infFac.getIdentificacionComprador());						
							System.out.println("totalSinImpuestos::"+infFac.getTotalSinImpuestos());
							System.out.println("totalDescuento::"+infFac.getTotalDescuento());						
							System.out.println("codigoCliente::"+infFac.getCodigoCliente());
							System.out.println("emailCliente::"+infFac.getEmailCliente());						
							System.out.println("Propina::"+infFac.getPropina());
							System.out.println("ImporteTotal::"+infFac.getImporteTotal());
							System.out.println("Moneda::"+infFac.getMoneda());
							System.out.println("TotalImpuestosIva::"+infFac.getTotalImpuestosIva());
							System.out.println("BaseImponible::"+infFac.getBaseImponible());						
							System.out.println("::-------------------------::");						
							System.out.println("::------------Informacion Total de Impuestos-------------::");
							
							ArrayList<ImpuestosFactura> ListTotImp = new ArrayList<ImpuestosFactura>();
							ListTotImp = DBTransaction.getTotalImpuestoTrxInfoFactura(getIdMovimiento());
							if (ListTotImp==null)
								throw new Exception("ERRORBITACORA,getTotalImpuestoTrxInfoFactura Error el metodo getTotalImpuestoTrxInfoFactura devuelve Null>> movimiento : "+getIdMovimiento());							
							infFac.setListTotalImpuestos(ListTotImp);
							
							if (ListTotImp != null){
								for (int j=0; j<ListTotImp.size(); j++){
									System.out.println("Codigo::"+ListTotImp.get(j).getCodigo());						
									System.out.println("CodigoPorcentaje::"+ListTotImp.get(j).getCodigoPorcentaje());
									System.out.println("Tarifa::"+ListTotImp.get(j).getTarifa());
									System.out.println("BaseImponible::"+ListTotImp.get(j).getBaseImponible());
									System.out.println("Valor::"+ListTotImp.get(j).getValor());
									System.out.println("::-------------------------::");							
								}
							}											
							ArrayList<DetalleFactura> ListDet= new ArrayList<DetalleFactura>();
							ListDet = DBTransaction.getDetalleTrxInfoFactura(//this.empresa,
																				    getIdMovimiento());
							if (ListDet==null)
								throw new Exception("ERRORBITACORA,getDetalleTrxInfoFactura Error el metodo getDetalleTrxInfoFactura devuelve Null>> movimiento : "+getIdMovimiento());
							infFac.setListDetFactura(ListDet);						
							if (ListDet != null){
								for (int j=0; j<ListDet.size(); j++){
									System.out.println("::------------Informacion Detalle -------------::");
									System.out.println("codigoPrincipal::"+ListDet.get(j).getCodigoPrincipal());						
									System.out.println("codigoAuxiliar::"+ListDet.get(j).getCodigoAuxiliar());
									System.out.println("descripcion::"+ListDet.get(j).getDescripcion());
									System.out.println("cantidad::"+ListDet.get(j).getCantidad());
									System.out.println("precioUnitario::"+ListDet.get(j).getPrecioUnitario());
									System.out.println("descuento::"+ListDet.get(j).getDescuento());
									System.out.println("precioTotalSinImpuesto::"+ListDet.get(j).getPrecioTotalSinImpuesto());
									System.out.println("::-------------------------::");								
									
									System.out.println("::------------Informacion Detalle de Impuestos-------------::");
									for (int k=0; k<ListDet.get(j).getListImpuestos().size(); k++){
										System.out.println("codigo::"+ListDet.get(j).getListImpuestos().get(k).getCodigo());
										System.out.println("codigoPorcentaje::"+ListDet.get(j).getListImpuestos().get(k).getCodigoPorcentaje());
										System.out.println("tarifa::"+ListDet.get(j).getListImpuestos().get(k).getTarifa());
										System.out.println("baseImponible::"+ListDet.get(j).getListImpuestos().get(k).getBaseImponible());
										System.out.println("valor::"+ListDet.get(j).getListImpuestos().get(k).getValor());									
									}
									System.out.println("::-------------------------------------------::");
								}
							}
							
						FacturaXml facXml = new FacturaXml("1.0.0", 
														   empresa, 
														   establecimiento, 
														   puntoEmision, 
														   getSecuencial(), 
														   getSecuencial(), 
														   infFac);
						try{
							if (facXml.crearFactura(getVersion())){
								//VPI 
								
								DBTransaction.UpdateEstadoTrx(//this.empresa, 
																				 getIdMovimiento(),
																				 "G");
																				 
								/*
								DBTransaction.insertFactElectronicaTmp(//this.empresa,
										this.getIdMovimiento(), "G",this.getTipoDocumento(), this.getEstablecimiento().getCodEstablecimiento(),this.getCaja(),this.getSecuencial());
										*/
							}else{
								throw new Exception("ERRORGENERAXML, Error el metodo crearFactura no se pudo crear el XML >> movimiento : "+getIdMovimiento());
							}
						}catch(Exception e){
							throw new Exception(e.getMessage());
						}
						
					}
					if (this.getTipoDocumento().equals("04")){
						InfoCredito infCre = new InfoCredito(); 

						
						 
						infCre = DBTransaction.getTrxInfoNotaTributariaXml(//this.empresa,  
																				getLocal(), 
																				getCaja(), 
																				getIdMovimiento(),
																				getSecuencial(), 
																				getTipoDocumento());
						if (infCre==null)
							throw new Exception("ERRORBITACORA,getTrxInfoNotaTributariaXml,Empresa >> movimiento : "+getIdMovimiento());
						infCre.getListInfoAdicional().add(new InfoAdicional("OFICINA", establecimiento.getDirEstablecimiento()));
						infCre.getListInfoAdicional().add(new InfoAdicional("CAJA", puntoEmision.getCaja()));
						infCre.getListInfoAdicional().add(new InfoAdicional("MOVIMIENTO", getIdMovimiento()));

						infCre.setDirEstablecimiento(establecimiento.getDirEstablecimiento());
						infCre.setContribuyenteEspecial(new Integer(empresa.getContribEspecial()).toString());
						infCre.setObligadoContabilidad(empresa.getObligContabilidad());						
					
						System.out.println("::------------Informacion Credito General-------------::");
						System.out.println("fechaEmision::"+infCre.getFechaEmision());
						System.out.println("dirEstablecimiento::"+infCre.getDirEstablecimiento());
						System.out.println("contribuyenteEspecial::"+infCre.getContribuyenteEspecial());
						System.out.println("obligadoContabilidad::"+infCre.getObligadoContabilidad());
						System.out.println("tipoIdentificacionComprador::"+infCre.getTipoIdentificacionComprador());
						System.out.println("guiaRemision::"+infCre.getGuiaRemision());
						System.out.println("razonSocialComprador::"+infCre.getRazonSocialComprador());
						System.out.println("identificacionComprador::"+infCre.getIdentificacionComprador());						
						System.out.println("totalSinImpuestos::"+infCre.getTotalSinImpuestos());
						System.out.println("totalDescuento::"+infCre.getTotalDescuento());						
						System.out.println("codigoCliente::"+infCre.getCodigoCliente());
						System.out.println("emailCliente::"+infCre.getEmailCliente());						
						System.out.println("Propina::"+infCre.getPropina());
						System.out.println("ImporteTotal::"+infCre.getImporteTotal());
						System.out.println("Moneda::"+infCre.getMoneda());
						System.out.println("TotalImpuestosIva::"+infCre.getTotalImpuestosIva());
						System.out.println("BaseImponible::"+infCre.getBaseImponible());						
						System.out.println("Motivo::"+infCre.getMotivo());
						System.out.println("::-------------------------::");						
						System.out.println("::------------Informacion Total de Impuestos-------------::");
						
						ArrayList<ImpuestosFactura> ListTotImp = new ArrayList<ImpuestosFactura>();
						ListTotImp = DBTransaction.getTotalImpuestoTrxInfoFactura(//this.empresa, 
								   														 getIdMovimiento());
							
						if (ListTotImp==null)
							throw new Exception("ERRORBITACORA,getTotalImpuestoTrxInfoCredito,Empresa >> movimiento : "+getIdMovimiento());							
						infCre.setListTotalImpuestos(ListTotImp);
						if (ListTotImp != null){
							for (int j=0; j<ListTotImp.size(); j++){
								System.out.println("Codigo::"+ListTotImp.get(j).getCodigo());						
								System.out.println("CodigoPorcentaje::"+ListTotImp.get(j).getCodigoPorcentaje());
								System.out.println("Tarifa::"+ListTotImp.get(j).getTarifa());
								System.out.println("BaseImponible::"+ListTotImp.get(j).getBaseImponible());
								System.out.println("Valor::"+ListTotImp.get(j).getValor());
								System.out.println("::-------------------------::");							
							}
						}												
						ArrayList<DetalleFactura> ListDet= new ArrayList<DetalleFactura>();
						ListDet = DBTransaction.getNotaDetalleTrxInfoFactura(//this.empresa, 
									 											getIdMovimiento());
						if (ListDet==null)
							throw new Exception("ERRORBITACORA,getDetalleTrxInfoCredito,Empresa >> movimiento : "+getIdMovimiento());
						infCre.setListDetFactura(ListDet);						
						if (ListDet != null){
							for (int j=0; j<ListDet.size(); j++){
								System.out.println("::------------Informacion Detalle -------------::");
								System.out.println("codigoPrincipal::"+ListDet.get(j).getCodigoPrincipal());						
								System.out.println("codigoAuxiliar::"+ListDet.get(j).getCodigoAuxiliar());
								System.out.println("descripcion::"+ListDet.get(j).getDescripcion());
								System.out.println("cantidad::"+ListDet.get(j).getCantidad());
								System.out.println("precioUnitario::"+ListDet.get(j).getPrecioUnitario());
								System.out.println("descuento::"+ListDet.get(j).getDescuento());
								System.out.println("precioTotalSinImpuesto::"+ListDet.get(j).getPrecioTotalSinImpuesto());
								System.out.println("::-------------------------::");								
								
								System.out.println("::------------Informacion Detalle de Impuestos-------------::");
								for (int k=0; k<ListDet.get(j).getListImpuestos().size(); k++){
									System.out.println("codigo::"+ListDet.get(j).getListImpuestos().get(k).getCodigo());
									System.out.println("codigoPorcentaje::"+ListDet.get(j).getListImpuestos().get(k).getCodigoPorcentaje());
									System.out.println("tarifa::"+ListDet.get(j).getListImpuestos().get(k).getTarifa());
									System.out.println("baseImponible::"+ListDet.get(j).getListImpuestos().get(k).getBaseImponible());
									System.out.println("valor::"+ListDet.get(j).getListImpuestos().get(k).getValor());									
								}
								System.out.println("::-------------------------------------------::");
							}
						}
						CreditoXml creXml = new CreditoXml("1.0.0", 
														   empresa, 
														   establecimiento, 
														   puntoEmision, 
														   Integer.parseInt(this.getSecuencial()), 
														   getSecuencial(), 
														   infCre);
						try{
							if (creXml.crearNotaCredito(getVersion())){
								//VPI - 
								DBTransaction.UpdateEstadoTrx(//this.empresa, 
																				getIdMovimiento(),
																				"G");
								
								/*
								DBTransaction.insertFactElectronicaTmp(//this.empresa,
										this.getIdMovimiento(), "G",this.getTipoDocumento(), this.getEstablecimiento().getCodEstablecimiento(),this.getCaja(),this.getSecuencial());
								*/
							}else{
								throw new Exception("ERRORGENERAXML, Error en el metodo crearNotaCredito >> movimiento : "+getIdMovimiento());
							}
						}catch(Exception e){
							throw new Exception(e.getMessage());
						}
					
					}
						
				//throw new Exception("ServiceInvoice::Invoice->main :: El archivo de control no contiene el valor correcto.");					

		} catch (Exception e) {	
			
			e.printStackTrace();
			String ls_mail = e.getMessage();//, ls_error = "";//, ls_tipo_error="", ls_database="";			
			//ls_error= e.getMessage();
			//String ls_errores[] = ls_error.split(",");			
			/*ls_tipo_error = ls_errores[0];
			ls_error = ls_errores[1];
			ls_database = ls_errores[2];
			ls_mail = "Tipo de Error:"+ls_tipo_error+"::Error producido en:"+ls_error+"::En la Base de Datos de:"+ls_database;*/
			ls_mail = ls_mail + "\n Registro Afectado ";
			ls_mail = ls_mail + "\n Fecha de Emision::"+this.getFechaEmision();
			ls_mail = ls_mail + "\n IdMovimiento::"+this.getIdMovimiento();
			ls_mail = ls_mail + "\n Secuencial::"+this.getSecuencial();
			ls_mail = ls_mail + "\n TipoDocumento::"+this.getTipoDocumento();			
			ls_mail = ls_mail + "\n Local::"+this.getLocal();
			ls_mail = ls_mail + "\n Caja::"+this.getCaja();
			Util.enviaEmailSoporteService("message_service_error", ls_mail);
			
			System.out.println("Fecha de Emision::"+this.getFechaEmision());
			System.out.println("IdMovimiento::"+this.getIdMovimiento());
			System.out.println("Secuencial::"+this.getSecuencial());
			System.out.println("TipoDocumento::"+this.getTipoDocumento());
			System.out.println("Local::"+this.getLocal());						
			System.out.println("Caja::"+this.getCaja());						
			System.out.println("::-------------------------::");
				try {
					//VPI -
					if (flagInsertaError){
						DBTransaction.UpdateEstadoTrx(//empresa, 
								getIdMovimiento(),
								"E");
					}

						
					/*
					DBTransaction.insertFactElectronicaTmp(//this.empresa,
							this.getIdMovimiento(), "E",this.getTipoDocumento(), this.getEstablecimiento().getCodEstablecimiento(),this.getCaja(),this.getSecuencial());
					*/		
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println(e1.getLocalizedMessage());
				}

		}finally{			
			try {
				//empresa.close();
				//Se cierra la conexion al finalizar ejecucion del Hilo 
				 if (ConexionBase.DBManager.get()!= null){
					   ConexionBase.cerrarConexionBD();
					 }
				System.out.println("Cerrando conexion del Hilo");
				facturacion.close();			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	/**************************************************************************************
	** Function:	 getConnectionBD
	** Description:  Funcion que crea una conexion a la base de datos especificada
	** Return:		 Retorna > Objeto Connection -> exito.  En caso de error -> null.
	**************************************************************************************/
	/*
	public static Connection getConnectionBD(Connection datase, String ls_BD, String Database)
	{
		Connection respuesta = null;
		int intento=0;
		String g_msg = "";
		String ls_error = "";
		try {
		if (datase == null){
			//conecta por primera vez
			intento=0;
			do{
				try{
					System.out.print("\nServiceInvoice::Invoice->getConnectionBD :: Conectandose a Base de Datos " + ls_BD);
					//log.debug(new StringBuffer().append("OroVerde :: ").append(classReference).append("->getConnectionBD").append(" :: ").append("Conectandose a Base de Datos " + ls_BD));					
					if (Database.equals("Oracle")){
						respuesta = setOracleConnectionOracle(ls_BD,"Empresa.oracle");
					}
					if (Database.equals("PostgreSQL")){
						respuesta = setConnectionPostgreSQL(ls_BD,"Empresa.postgreSQL");
					}
					
					if (Database.equals("SQLServer")){
						respuesta = setConnectionSQLServer(ls_BD,"Empresa.DB");
					}
					if (respuesta == null){
						intento++;
					}
				}catch(Exception e){
					intento++;
					//log.debug(new StringBuffer().append("OroVerde :: ").append(classReference).append("->getConnectionBD").append(" :: ").append("BD: "+ls_BD+" | Error de Conexion "+e.toString()+" Reintentando..."));
					System.out.print("ServiceInvoice::Invoice->getConnectionBD :: BD: "+ ls_BD +" | Error de Conexion "+e.toString()+" Reintentando...");
				}
			}while((respuesta == null)&&(intento<3));
		}
		else{
			respuesta = datase;
		}
	    /* excedio intentos de conexion 
		if ((intento >= 3) && (respuesta == null))
	    {
			System.out.print("ServiceInvoice::Invoice->getConnectionBD::** Error conexion a Base de Datos " + ls_BD + ": Mas de 3 intentos de conexion a Base de Datos\n");
			//log.debug(new StringBuffer().append("ServiceOroVerde::").append(classReference).append("->getConnectionBD").append(" :: ").append("** Error socket: Mas de 3 intentos de conexion a Base de Datos\n"));
			/*
			try{
				respuesta.close();
			}catch(Exception e){			
			}
		    g_msg = "Service::ERROR DE CONEXION. Mas de 3 intentos de conexion a Base de Datos ("+ ls_BD +").";
			envioAlarma(g_msg, ls_error, "getConnectionBD");	
		}
		}catch(Exception e){			
			//log.debug(new StringBuffer().append("ServiceOroVerde :: ").append(classReference).append("->getConnectionBD").append(" :: ").append(e.toString()));
			System.out.print("ServiceInvoice::Invoice->getConnectionBD :: " + e.toString());
			g_msg = "ServiceOroVerde: ERROR GENERAL: Conexion a Base de Datos " + ls_BD;
			
			envioAlarma(g_msg, ls_error, "Exception::getConnectionBD");
		}
		
		return respuesta; 
    }
*/
	public CtrlFile getCf() {
		return cf;
	}

	public void setCf(CtrlFile cf) {
		this.cf = cf;
	}

/*
	public Connection getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Connection empresa) {
		this.empresa = empresa;
	}
*/
	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getCaja() {
		return caja;
	}

	public void setCaja(String caja) {
		this.caja = caja;
	}

	public Connection getFacturacion() {
		return facturacion;
	}

	public void setFacturacion(Connection facturacion) {
		this.facturacion = facturacion;
	}

	public PreparedStatement getStmt() {
		return stmt;
	}

	public void setStmt(PreparedStatement stmt) {
		this.stmt = stmt;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public CallableStatement getCs() {
		return cs;
	}

	public void setCs(CallableStatement cs) {
		this.cs = cs;
	}

	public int getFlagbill() {
		return flagbill;
	}

	public void setFlagbill(int flagbill) {
		this.flagbill = flagbill;
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public FacPuntoEmision getPuntoEmision() {
		return puntoEmision;
	}

	public void setPuntoEmision(FacPuntoEmision puntoEmision) {
		this.puntoEmision = puntoEmision;
	}

	public FacEstablecimiento getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(FacEstablecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}

	public static String getRuc() {
		return ruc;
	}

	public static void setRuc(String ruc) {
		Hilo.ruc = ruc;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getSecuencial() {
		return secuencial;
	}

	public void setSecuencial(String secuencial) {
		this.secuencial = secuencial;
	}

	public String getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(String idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}