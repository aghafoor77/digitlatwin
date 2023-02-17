package org.ri.se.platform.datamodel;

import java.io.IOException;

import org.ri.se.platform.parser.GDSOParser;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EncodedGenericDataSharingObject {
	private String header = null;;
	private Packet payload = null;;
	
	public EncodedGenericDataSharingObject() throws IOException {
		super();
	}
	public EncodedGenericDataSharingObject(String serialized) throws IOException {
		super();
		fromSerialize(serialized);
	}
	public EncodedGenericDataSharingObject(GenericDataSharingObject genericDataSharingObject) {
		this.header = new GDSOParser().parse(genericDataSharingObject.getGdsoHeader());
		this.payload = genericDataSharingObject.getPacket();		
	}
	
	public EncodedGenericDataSharingObject(String header, Packet payload) {
		super();
		this.header = header;
		this.payload = payload;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String encodedGDSOHeader) {
		this.header = encodedGDSOHeader;
	}
	public Packet getPayload() {
		return payload;
	}
	public void setPayload(Packet payload) {
		this.payload = payload;
	}
	
	public String serialize() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);

	}
	public void fromSerialize(String serialized) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		EncodedGenericDataSharingObject dataSharingObject = objectMapper.readValue(serialized.getBytes(),EncodedGenericDataSharingObject.class);
		this.setHeader(dataSharingObject.getHeader() );
		this.setPayload(dataSharingObject.getPayload());
	}
	
	
	public GenericDataSharingObject extractGenericDataSharingObject() throws Exception {
		
		GenericDataSharingObject genericDataSharingObject = new GenericDataSharingObject  ();
		System.out.println("Extracting generic data sharing object . . .  " );
		System.out.println("Encoded header of generic data sharing object : "+this.header );
		genericDataSharingObject.setGdsoHeader(new GDSOParser().extract(this.header));
		System.out.println("Decoded generic header !");
		//log.info("\n"+genericDataSharingObject.getGdsoHeader().serialize());
		genericDataSharingObject.setPacket(this.payload);
		//log.info("Generic data packet:");
		//log.info("\n"+genericDataSharingObject.getPacket().serialize());
		return genericDataSharingObject;
		
	}	
}
