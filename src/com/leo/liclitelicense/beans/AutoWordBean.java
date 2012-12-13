package com.leo.liclitelicense.beans;

public class AutoWordBean {

	private String autoServerName = null;
	private String autoServerCmd = null;
	
	public AutoWordBean (String autoServerName, String autoServerCmd){
		
		this.autoServerName = autoServerName;
		this.autoServerCmd = autoServerCmd;
	}
	
	public String getAutoServerName() {
		return autoServerName;
	}

	public void setAutoServerName(String autoServerName) {
		this.autoServerName = autoServerName;
	}

	public String getAutoServerCmd() {
		return autoServerCmd;
	}

	public void setAutoServerCmd(String autoServerCmd) {
		this.autoServerCmd = autoServerCmd;
	}
	
	@Override
	public String toString(){
		return this.autoServerName + " - " + this.autoServerCmd;
	}
}
