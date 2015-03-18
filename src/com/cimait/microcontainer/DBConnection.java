package com.cimait.microcontainer;

public abstract class DBConnection {

	protected String dataBase;
	protected String driver;
	protected String url;
	protected String user;
	protected String password;
	protected String id;
	protected String status;
	
	public void setDataBase(String dataBase){
		this.dataBase = dataBase;		
	}
	
	public String getDataBase(){
		return dataBase;
	}

	public void setDriver(String driver){
		this.driver = driver;		
	}
	
	public String getDriver(){
		return driver;
	}

	public void setUrl(String url){
		this.url = url;		
	}
	
	public String getUrl(){
		return url;
	}

	public void setUser(String user){
		this.user = user;		
	}
	
	public String getUser(){
		return user;
	}

	public void setPassword(String password){
		this.password = password;		
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public String getStatus(){
		return status;
	}
	
	public abstract Object getInstance(); 
	
}
