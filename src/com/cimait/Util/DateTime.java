package com.cimait.Util;
/*
   Clase : Fecha y Hora
   Facilita el manejo de la clase calendario.
*/

import java.util.Calendar;

public class DateTime 
{
	private Calendar fecha = null;

	public DateTime() 
	{
		fecha = Calendar.getInstance();
	}

	public String getDate()
	{
		int ano = fecha.get(Calendar.YEAR);
		int mes = fecha.get(Calendar.MONTH)+1;
		int dia = fecha.get(Calendar.DAY_OF_MONTH);
		return (ano + (mes<10 ? "0" : "") + mes + (dia<10 ? "0" : "") + dia);
	}

	public String getTime()
	{
		int hor = fecha.get(Calendar.HOUR_OF_DAY);
		int min = fecha.get(Calendar.MINUTE);
		int sec = fecha.get(Calendar.SECOND);
		return ((hor<10 ? "0" : "") + hor + ":" + (min<10 ? "0" : "") + min + ":" + (sec<10 ? "0" : "") + sec);
	}

	public String getTimeM()
	{
		int hor = fecha.get(Calendar.HOUR_OF_DAY);
		int min = fecha.get(Calendar.MINUTE);
		int sec = fecha.get(Calendar.SECOND);
		int mil = fecha.get(Calendar.MILLISECOND);
		return ((hor<10 ? "0" : "") + hor + ":" + (min<10 ? "0" : "") + min + ":" + (sec<10 ? "0" : "") + sec + ":" + mil );
	}
}