package org.ri.se.platform.datamodel;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Packet {

	private Hashtable<String, String> partners;
	private String data;

	public Packet() {
		partners = new Hashtable<String, String>();
		data = new String();
	}

	public String add(String pub, String encryptedKey) {
		return partners.put(pub, encryptedKey);
	}

	public String get(String pub) {
		return partners.get(pub);
	}

	public String remove(String pub) {
		return partners.remove(pub);
	}

	public Enumeration<String> keys() {
		return partners.keys();
	}

	public Set<String> keySet() {
		return partners.keySet();
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Hashtable<String, String> getPartners() {
		return partners;
	}

	public void setPartners(Hashtable<String, String> partners) {
		this.partners = partners;
	}
	
	public String serialize() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}


}
