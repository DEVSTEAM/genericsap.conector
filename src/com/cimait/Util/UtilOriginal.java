
package com.cimait.Util;

import java.util.Date;

import com.cimait.runtime.*;

public class UtilOriginal {
	public static final String FORMATO_FECHA_DB = "yyyy-MM-dd";
	public static final int EDITAR = 1;
	
	//#######- Niveles de Error - SCP -#######
	//NIVEL_ERROR - 0: Informativo, 1: Warning 2: Error leve 3: Error Critico
	public static final int nivel_error_inf=0;//NIVEL_ERROR - 0: Informativo, 
	public static final int nivel_error_war=1;//NIVEL_ERROR - 1: Warning 
	public static final int nivel_error_lev=2;//NIVEL_ERROR - 2: Error leve
	public static final int nivel_error_cri=3;//NIVEL_ERROR - 3: Error Critico
	
	public static final int nivel_seguimiento=Environment.c.getInt("OroVerde.oracle.scp.nivel-seguimiento");
	public static final String bandScp=Environment.c.getString("OroVerde.oracle.scp.ctrl-scp-seguimiento");
	public static final String bandScpP=Environment.c.getString("OroVerde.oracle.scp.ctrl-scp-principal");
	
	//#######- Cabecera SCP -#######
	
	public static final String ls_unidad_registro_scp = "DESPACHADOR_COMANDOS";//Unidad de registro por ejemplo CDRS, VOUCHERS, SERVICIOS, CUENTAS. El programador debe especificar que es lo que se esta procesando. - scp_bitacora_procesos
	public static final double li_total_registros_scp = 0;					   //Registros totales a ser procesados. - scp_bitacora_procesos
	public static final String ls_referencia_scp = "OroVerde.jar";		   	   //Nombre de programa invocado, sirve como ayuda al operador para identificar el programa responsable del proceso. - scp_bitacora_procesos
	//public static final String ls_error_scp = null; 						   //error del procedimiento del scp - scp_bitacora_procesos_ins
	public static final String ls_id_proceso_scp =Environment.c.getString("OroVerde.oracle.scp.id_proceso_scp"); //"DESPACHADOR DESGSM";	   //Identificador del proceso. - scp_procesos - Configurado en el globito
	//#######- SCP -  Paquetes y procedimientos #######
	public static final String procesos_ins=Environment.c.getString("OroVerde.oracle.scp.procesos_ins");
	public static final String detalles_bitacora_ins=Environment.c.getString("OroVerde.oracle.scp.detalles_bitacora_ins");
	public static final String procesos_act=Environment.c.getString("OroVerde.oracle.scp.procesos_act");
	public static final String bitacora_procesos_fin=Environment.c.getString("OroVerde.oracle.scp.bitacora_procesos_fin");
	
	//***********************************************************************
	
	//Estados
	public static final String status_reproceso=Environment.c.getString("OroVerde.oracle.axis.request.ds_servicios_cab.estado_registro.reproceso");
	public static final String status_procesado=Environment.c.getString("OroVerde.oracle.axis.request.ds_servicios_cab.estado_registro.procesado");
	public static final String status_tomado=Environment.c.getString("OroVerde.oracle.axis.request.ds_servicios_cab.estado_registro.tomado");
	public static final int status_procesado_resp=Environment.c.getInt("OroVerde.oracle.axis.request.ds_sas_response.estado.procesado");
	public static final int status_respo_error=Environment.c.getInt("OroVerde.oracle.axis.request.ds_sas_response.estado.error");
	public static final int status_det_error=Environment.c.getInt("OroVerde.oracle.axis.request.ds_servicios_det.estado_respuesta.error");
	public static final int status_cab_inicial=Environment.c.getInt("OroVerde.oracle.axis.request.ds_servicios_cab.estado_registro.inicial");
	
	
	//#######- Hilos -#######
	public static final String query=Environment.c.getString("OroVerde.oracle.axis.request.ds_servicios_cab.query-principal");
	public static final int max_row=Environment.c.getInt("OroVerde.oracle.axis.request.max-row");
	public static final int hilos_ejecucion=Environment.c.getInt("OroVerde.oracle.axis.request.hilos.cantidad_ejecucion");
	
	
	public static final String QUEUE_ID="OroVerde.oracle.axis.request.hilos.thread.QUEUE_ID";
	public static final String TYPE_THREAD="OroVerde.oracle.axis.request.hilos.thread.type-thread";
	public static String QUEUE_IH="";
	public static String TYPE_THREADH="";
		
	public static final int waiting_time=Environment.c.getInt("OroVerde.oracle.axis.request.hilos.sleep");
	

	
	//#######- Sockets -#######
	public static final String send_hlr=Environment.c.getString("OroVerde.hlr.send-hlr");
	public static final String delete_cola=Environment.c.getString("OroVerde.hlr.delete-cola");
	public static final String ls_safirPtoRes = Environment.c.getString("OroVerde.hlr.SAFIR_PTO_REQ");
	public static final String ip=Environment.c.getString("OroVerde.hlr.SAFIR_IP");
	public static final int port=Environment.c.getInt("OroVerde.hlr.puerto_socket");
	public static final String ls_usrSafir = Environment.c.getString("OroVerde.hlr.USR_SAFIR");
	public static final int reintentos_socket = Environment.c.getInt("OroVerde.hlr.reintentos_socket");
	
	
	//#######- Nombres de Paquetes -#######
	public static final String arma_comando=Environment.c.getString("OroVerde.oracle.axis.pck.arma_comando");
	
	
	
	//#######- Reintentos para reproceso -#######
	public static final int max_reint=Environment.c.getInt("OroVerde.oracle.axis.request.ds_servicios_cab.reintentos.numero");
	public static final int reintentos_base=Environment.c.getInt("OroVerde.oracle.axis.reintentos_base");
	public static final String elimina_reg=Environment.c.getString("OroVerde.oracle.axis.request.ds_servicios_cab.reintentos.elimina_registro");
	
	
	//#######- Comandos -#######
	public static final String logout=Environment.c.getString("OroVerde.hlr.trama-logout");
	public static final String comando_login=Environment.c.getString("OroVerde.hlr.trama-login");
	
	
	
	
	//#######- Nombres de Arhivos Logs, Crtl -#######
	public static final String log_control=Environment.c.getString("OroVerde.log.control");
	public static final String file_control=Environment.c.getString("OroVerde.ctrl-on-off.file");
	public static final String name_proyect="DESGSM";
	
	//#######- CRUD DESGSM-#######
	public static final String update_response=Environment.c.getString("OroVerde.oracle.axis.request.ds_sas_response.update-status");
	public static final String update_dsServiciosCab=Environment.c.getString("OroVerde.oracle.axis.request.ds_servicios_cab.update-status");
	public static final String update_ReintentosdsServiciosCab=Environment.c.getString("OroVerde.oracle.axis.request.ds_servicios_cab.reintentos.update");
	public static final String consulta_reintentos=Environment.c.getString("OroVerde.oracle.axis.request.ds_servicios_cab.reintentos.consulta_reintentos");
	public static final String update_reproc_response=Environment.c.getString("OroVerde.oracle.axis.request.ds_sas_response.update-reproc-response");
	public static final String update_reproc_response_t=Environment.c.getString("OroVerde.oracle.axis.request.ds_sas_response.update-reproc-response-t");
	public static final String estado_reproc_response=Environment.c.getString("OroVerde.oracle.axis.request.ds_sas_response.estado.error");
	
	//#######- Parametros de Mail-#######
	public static final String host=Environment.c.getString("OroVerde.alarm.email.host");
	public static final String from=Environment.c.getString("OroVerde.alarm.email.sender");
	public static final String list_email=Environment.c.getString("OroVerde.alarm.email.receivers-list");
	public static final String subject=Environment.c.getString("OroVerde.alarm.email.subject");
	public static final String pieMensaje=Environment.c.getString("OroVerde.alarm.email.final-message");
	public static final String enablemail= Environment.c.getString("OroVerde.alarm.email.enable");
	public static final int time_mail= Environment.c.getInt("OroVerde.alarm.email.time-mail");
	
//	#######- Validar respuesta del HLR -#######
	public static final String mensaje_hlr_a=Environment.c.getString("OroVerde.oracle.axis.request.validaciones.errores-request.errores-apagar");
	public static final String mensaje_hlr_r=Environment.c.getString("OroVerde.oracle.axis.request.validaciones.errores-request.errores-reinicio");
	
	
	//#######- Calculo de tiempo entre dos fechas -#######
	public static double calcTimeMin(Date ld_fechaInicial, Date ld_fechaFinal){
		 try{
				  long fechaIni = ld_fechaInicial.getTime();
				  long fechaFin = ld_fechaFinal.getTime();  
				  double minutes = fechaFin - fechaIni;	 
				  return (minutes/(1000*60)); 
		 }catch(Exception e){
			 e.printStackTrace();
			 return -1;
		 }
	 }

	//#######- Funcion que permite relacionar el queid y el type  -#######
	public static void Asigna_queid_cola(int hilo){
		String que="";
		String type="";
		que=QUEUE_ID+hilo;
		type=TYPE_THREAD+hilo;
		QUEUE_IH=Environment.c.getString(que);
		TYPE_THREADH=Environment.c.getString(type); 
	 }
	
	//#######- DSK_SAFIR -  Paquetes y procedimientos #######
	public static final String fail_comand=Environment.c.getString("OroVerde.oracle.dsk_safir.funciones.send_failcomandossafir");	
	public static final String consulta_DserviciosBloqueo=Environment.c.getString("OroVerde.oracle.dsk_safir.funciones.consulta_dsServiciosBloqueo");
	
}

