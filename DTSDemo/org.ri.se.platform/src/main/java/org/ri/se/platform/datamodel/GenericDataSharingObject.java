package org.ri.se.platform.datamodel;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericDataSharingObject {
	private GDSOHeader gdsoHeader;
	private Packet packet;
	

	public GDSOHeader getGdsoHeader() {
		return gdsoHeader;
	}

	public void setGdsoHeader(GDSOHeader gdsoHeader) {
		this.gdsoHeader = gdsoHeader;
	}
	
	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public String serialize() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		return objectMapper.writeValueAsString(this);

	}

}
