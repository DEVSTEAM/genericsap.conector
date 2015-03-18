package com.cimait.Util;

import org.apache.commons.configuration.Configuration;

import com.cimait.runtime.Environment;



public class AlarmManager implements Runnable {
	/* Variable Definidas para el envio de SMS*/
	private String sms = "";	
	private String messageSMS = "";	
	private String ip = "";
	private String port = "";
	private String source ="";
	private String list_sms = "";
	private String[] telefonos;
	
	/* Variable Definidas para el envio de Emails*/
	private String email = "";
	private String host = "";
	private String messageEmail = "";
	private String from = "";
	private String list_email = "";
	private String subject = "";
	private String pieMensaje="";
	
	private String processId = "";
	private Configuration c;
	
	public AlarmManager() throws Exception {		
		email = Environment.c.getString("OroVerde.alarm.email.enable");
		ip = Environment.c.getString("OroVerde.alarm.sms.ip");
	}
	
	public AlarmManager(Configuration lc_c, String ls_processId)
		    throws Exception
		  {
		    this.c = lc_c;
		    this.processId = ls_processId;
		    email = Environment.c.getString("OroVerde.alarm.email.enable");
			ip = Environment.c.getString("OroVerde.alarm.sms.ip");
    }
	public void setMessageSMS(String message) {
		this.messageSMS = message;
	}
	
	public void setMessageEmail(String message) {
		this.messageEmail = message;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		String trama = "";	
		String result="";
		try{		
				if (email.equals("Y")) {
						host = Environment.c.getString("OroVerde.alarm.email.host");
						from = Environment.c.getString("OroVerde.alarm.email.sender");
						list_email = Environment.c.getString("OroVerde.alarm.email.receivers-list");
						subject = Environment.c.getString("OroVerde.alarm.email.subject");
						pieMensaje = Environment.c.getString("OroVerde.alarm.email.final-message");
					
					EmailSender e = new EmailSender(host, from);
					result = e.send(list_email, subject, messageEmail+"\n\n"+pieMensaje);
					if(result == null){		result ="";		}
				}
		}catch(Exception e)
		{
			e.printStackTrace();
		}			
	}
}
