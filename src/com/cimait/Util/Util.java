
package com.cimait.Util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.Normalizer;
import org.apache.commons.lang.StringEscapeUtils;

import com.cimait.runtime.*;

public class Util {
	public static final String FORMATO_FECHA_DB = "yyyy-MM-dd";
	public static final int EDITAR = 1;
	

	
	//#######- Niveles de Error - SCP -#######
	//NIVEL_ERROR - 0: Informativo, 1: Warning 2: Error leve 3: Error Critico
	public static final int nivel_error_inf=0;//NIVEL_ERROR - 0: Informativo, 
	public static final int nivel_error_war=1;//NIVEL_ERROR - 1: Warning 
	public static final int nivel_error_lev=2;//NIVEL_ERROR - 2: Error leve
	public static final int nivel_error_cri=3;//NIVEL_ERROR - 3: Error Critico
	
	public static String normalizeValue(String value, int maxURLLength, String name) {
		String inputValue = value;
		String ret = "";
		if (inputValue != null) {			
			ret = Normalizer.normalize(inputValue.subSequence(0, inputValue.length()), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]+","");
			String cadena = Environment.c.getString("Empresa.util.normalizacion."+name);
			cadena = cadena!=null?cadena:Environment.c.getString("Empresa.util.normalizacion.GENERAL");
			
			ret = ret.trim();
			ret = ret.replaceAll(cadena, "|");
			ret = ret.replaceAll("[|]", " ");
			ret = ret.trim();
			ret = ret.replace("  ", " ");
			ret = ret.trim();
			
			if (ret.length() > maxURLLength) {
				ret = ret.substring(0, maxURLLength);
			}
			ret = ret.toLowerCase();
		}
		return ret;
	}
	
	public static int enviaEmailSoporteService(String ls_id_mensaje, String valor){
		
	       //Obtener nombre y direccion IP del equipo local juntos
	       InetAddress direccion = null;
			try {
				direccion = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
	       String nombreEquipo = "["+direccion.getHostName()+"-"+direccion.getHostAddress()+"] - ";
		
		//Dmiro.getLogger("ALL");
		String resultEnvioMail = "";
		String emailMensaje = "";
		String emailHost = "";
		String emailFrom = "";
		//String emailHelpDesk;
		emailHost = Environment.c.getString("Empresa.soporte.host");
		emailFrom = Environment.c.getString("Empresa.soporte.sender");
		EmailSender emSend = new com.cimait.Util.EmailSender(emailHost,emailFrom);	
		//emailHelpDesk ="vpincay@cimait.com.ec";
		String clave = Environment.c.getString("Empresa.soporte.password");
		
		String user = Environment.c.getString("Empresa.soporte.user");
		String tipo_autentificacion = Environment.c.getString("Empresa.soporte.tipo_autentificacion");
		String tipoMail = Environment.c.getString("Empresa.soporte.tipoMail");
		String port = Environment.c.getString("Empresa.soporte.port");
		
		
		
		String receivers = Environment.c.getString("Empresa.soporte.receivers-list");
		String subject = Environment.c.getString("Empresa.soporte.subject");
		emailMensaje = Environment.c.getString("Empresa.soporte."+ls_id_mensaje);
		if (receivers!=null){
			emSend.setPassword(clave);
			emSend.setSubject(subject);
			emSend.setUser(user);		
			emSend.setAutentificacion(tipo_autentificacion);
			emSend.setTipoMail(tipoMail);
			emSend.setPort(port);
			
			String emailSoporte = receivers;
			

			if(ls_id_mensaje.endsWith("message_service_error")){
				subject = "ERROR "+subject;
				emailMensaje = emailMensaje.replace("|MENSAJE ERROR|", valor);
			}
			if(ls_id_mensaje.endsWith("message_service_down")){
				subject = "DOWN "+subject;
				emailMensaje = emailMensaje.replace("|FECHA|", valor);
			}
			if(ls_id_mensaje.endsWith("message_service_up")){
				subject = "UP "+subject;
				emailMensaje = emailMensaje.replace("|FECHA|", valor);
			}
				
			
			emailMensaje = StringEscapeUtils.unescapeHtml(emailMensaje);
		if(tipoMail.equals("HTML")){
			emailMensaje = emailMensaje.replaceAll("\n", "<br>");
		}
			
		//Añadir nombre del equipo al msj 
		subject = nombreEquipo + subject;
		if ((emailSoporte!=null) && (emailSoporte.length()>0)){
			String[] partsMail = emailSoporte.split(";");
			//logThreadRet.debug("Mail de envio::"+emailSoporte);
			resultEnvioMail = emSend.send(receivers,
										  //partsMail[i], 
										  subject, 
										  emailMensaje,null,null);
			}
		
		}
		if (resultEnvioMail.equals("Enviado"))
				return 0;
		else
				//logThreadRet.debug("Error de Envio de Mail::"+resultEnvioMail);
		return -1;
}
	
	
	
}

