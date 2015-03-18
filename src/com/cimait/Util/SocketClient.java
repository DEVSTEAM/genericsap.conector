package com.cimait.Util;
import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class SocketClient {
	private Socket s = null;
	private BufferedReader sReader = null;
	private PrintWriter sWriter = null;
    private String ip = "";
	private int port = 0;
	
	private long startMsDate = 0;
	private long currentMsDate = 0;
	private Date startDate = null;
	private Date currentDate = null;


	public SocketClient(String vip, String vport) {
		ip = vip;
		port = Integer.parseInt(vport);
	}

	public void open() throws Exception {
		s = new Socket( ip,port );
		sReader  = new BufferedReader (new InputStreamReader (s.getInputStream()));
		sWriter = new PrintWriter(s.getOutputStream());
	}
	public String executeCommand(String command) {
		String resultString = null;
		long miliSeconds = 0;
		try {
			sWriter.println(command); 
			sWriter.flush();
			Thread.sleep(5); 
			startDate = new Date();
			startMsDate = startDate.getTime();
			currentDate = new Date();
			currentMsDate = currentDate.getTime();
			miliSeconds = currentMsDate - startMsDate;
			while( ((resultString = sReader.readLine()) == null) && (miliSeconds < 10000) )
			{
				currentDate = new Date();
				currentMsDate = currentDate.getTime();
				miliSeconds = currentMsDate - startMsDate;
				Thread.sleep(5);
			}
		} catch( Exception e ) { }
		return resultString;
	}

	public void close() {
		try {
			sWriter.close();
			sReader.close();
		} catch (IOException e){ }
		try {
			s.close();
		} catch (IOException e){ }
	}
}
