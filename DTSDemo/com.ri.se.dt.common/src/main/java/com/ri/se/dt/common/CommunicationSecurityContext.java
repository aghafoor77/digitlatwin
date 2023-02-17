package com.ri.se.dt.common;

import java.util.Date;

public class CommunicationSecurityContext {
	private String controller;
	private String didcom;
	private byte[] key;
	private Date initTime;

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getDidcom() {
		return didcom;
	}

	public void setDidcom(String didcom) {
		this.didcom = didcom;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public Date getInitTime() {
		return initTime;
	}

	public void setInitTime(Date initTime) {
		this.initTime = initTime;
	}
}
