package com.ri.se.dt.common;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExtendedKeyValue {

	private String rand;
	private String key;
	private String value;
	
	public String getRand() {
		return rand;
	}
	public void setRand(String rand) {
		this.rand = rand;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String serialize() throws Exception {
		return new ObjectMapper().writeValueAsString(this);
	}
	public void deserialize(String json) throws Exception {
		ExtendedKeyValue kv =new ObjectMapper().readValue(json, this.getClass());
		setKey(kv.getKey());
		setRand(kv.getRand());
		setValue(kv.getValue());
	}
}
