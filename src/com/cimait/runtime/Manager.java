package com.cimait.runtime;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cimait.DAO.Transaction;
import com.cimait.DAO.FacEmpresa;
import com.cimait.Util.Util;
import com.cimait.microcontainer.DBTransaction;
import com.cimait.microcontainer.DBFacElectronica;
import com.cimait.microcontainer.GenericTransaction;
import com.cimait.runtime.Environment;
import com.cimait.runtime.Hilo;

public class Manager extends GenericTransaction{	
	//private static HashMap listEstablecimientoMap = new HashMap<String,String>();	
	private static Connection facturacion=null;
	//private static Connection empresa=null;		
		
	static ArrayList<Transaction> listTrxAtender = new ArrayList<Transaction>();
		
	public Manager(){
		classReference = "Manager";
		
	}	
	
	public static void main(String[] args) {
		int hilos = 0, contHilos=0;		
		String ls_FileConfig = null, ls_ruc = null, ls_sucursal = null;//, ls_resort = null;		
		System.out.println("args.length::" + args.length);
	  do{
		  System.err.println("Iniciando...");
		try { 
			if (args == null)
				throw new Exception("No se envio correctamente los parametros... no se puede iniciar el procesamiento");			
			if (!(args.length==3))
				throw new Exception("No se envio correctamente los parametros... no se puede iniciar el procesamiento. Solo hay "+args.length+ "parametros");
			ls_FileConfig=args[0];
			ls_ruc=args[1];
			ls_sucursal= args[2];
			
			if(ls_FileConfig.equals("") || (ls_FileConfig==null)){
				throw new Exception("No envio el archivo de configuracion como parametro... no se puede iniciar el procesamiento");
			}			
			if(ls_ruc.equals("") || (ls_ruc==null)){
				throw new Exception("No envio el ruc como parametro... no se puede iniciar el procesamiento");
			}	
			if(ls_sucursal.equals("") || (ls_sucursal==null)){
				throw new Exception("No envio el cciSucursal como parametro... no se puede iniciar el procesamiento");
			}	
			
			Environment.setConfiguration(ls_FileConfig);
			Environment.setCtrlFile();
			System.out.println("LogControl::"+Environment.c.getString("Empresa.log.control"));
			Environment.setLogger(Environment.c.getString("Empresa.log.control"));			
			hilos = 10;
			Thread t = null;
			facturacion = ConexionBase.getConnectionBD(facturacion,"Invoice", "SQLServer");
			if (facturacion == null){				
				throw new Exception ("No se logro la conexion a la base Invoice");
			}
			
			//empresa = getConnectionBD(empresa,"Empresa", "SQLServer");
			ConexionBase.init();
			//empresa =  ConexionBase
			/*
			if (empresa == null){				
				throw new Exception ("No se logro la conexion a ERP");
			}*/
			String version = Environment.c.getString("Empresa.service.facElectronica.version");
			
			//if (Environment.cf.readCtrl().equals("S")) {					
				FacEmpresa empresa = DBFacElectronica.getRucEmpresa(facturacion, ls_ruc);
				if (empresa == null){				
					throw new Exception ("Empresa no configurada en FacElectronica");
				}				
				String tiposDocumentos = Environment.c.getString("Empresa.service.facturacion.getTrx.tiposDocumentos");
				if ((tiposDocumentos == null)||(tiposDocumentos.equals(""))){				
					throw new Exception ("No se encontro el parametro de tipo de documentos del Trx xml Empresa.service.facturacion.getTrx.queue_names");
				}
				int rownumBloque = Environment.c.getInt("Empresa.service.facturacion.getTrx.rownumBloque");
				if ((rownumBloque<=0)){				
					rownumBloque=100;
				}
				//  Envio de mail de subida del servicio
				Util.enviaEmailSoporteService("message_service_up",(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));
				
				while(Environment.cf.readCtrl().equals("S")) {
					listTrxAtender.clear();
					listTrxAtender = DBTransaction.getTrx(rownumBloque,tiposDocumentos,ls_sucursal);				
					if (listTrxAtender.size()>0){
						for (int i=0; i<listTrxAtender.size();i++)
						{	
							
							if(contHilos == hilos){
								t.join();
								new Thread().sleep(3000);
								System.gc();
								contHilos=0;
								
							}
							++contHilos;
							
							Hilo threadEmpresa = new Hilo(i, 
														  ls_ruc, 
														  listTrxAtender.get(i).getIdMovimiento(), 
														  listTrxAtender.get(i).getSecuencial(), 
														  listTrxAtender.get(i).getFechaEmision(),
														  listTrxAtender.get(i).getTipoDocumento(),
														  listTrxAtender.get(i).getLocal(),
														  listTrxAtender.get(i).getCaja(),
														  version);
							t = new Thread(threadEmpresa);
							t.start();
							new Thread().sleep(200);
							
							/*
							if (contHilos>hilos){
								contHilos=0;
								t.join();
								new Thread().sleep(3000);
							}*/
						}
					}else{
						new Thread().sleep(3000);
					}
				}
				/*
			} else {
				throw new Exception("ServiceEmpresa::EmpresaManager->main :: Se detiene servicio");	
			}*/
		} catch (Exception e) {
			String lvError = "EXCEPTION GENERAL : " + e.getMessage();
			Util.enviaEmailSoporteService("message_service_error", lvError);
			
			e.printStackTrace();
			System.err.println("Conector::->main :: "+e.getMessage());
			
			Environment.log.info(new StringBuffer().append("ServiceEmpresa :: ").append("ServiceEmpresa").append("->main").append(" :: ").append(e.getMessage()));
			StackTraceElement[] ste = e.getStackTrace();
			//System.err.println("ServiceEmpresa:: EmpresaManager->main :: ");
			for (int x=0; x<ste.length; x++){
				System.err.println(ste[x].toString());
				Environment.log.info(new StringBuffer().append(x+"::Exception::").append("ServiceEmpresa:: ").append("ServiceEmpresa").append("->main").append(" :: ").append(ste[x].toString()));
			}	
			try {
				System.err.println("Reintentando generacion de archivos xml, esperando...");
				Thread.sleep(200000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
	   }while(Environment.cf.readCtrl().equals("S")); //Fin while 
	  Util.enviaEmailSoporteService("message_service_down", (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));
	  System.err.println("Se detiene conector");
	}


}
