package org.ri.se.platform.datamodel;

import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GDSOHeader {
	private SecurityContext securityContext;
	private AccessLevel accessLevel;
	private DataStorageType dataStorageType;
	private DataType dataType;
	private ENCODING_DECODING_SCHEME encoding;

	public SecurityContext getSecurityContext() {
		return securityContext;
	}

	public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	public AccessLevel getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

	public DataStorageType getDataStorageType() {
		return dataStorageType;
	}

	public void setDataStorageType(DataStorageType dataStorageType) {
		this.dataStorageType = dataStorageType;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public ENCODING_DECODING_SCHEME getEncoding() {
		return encoding;
	}

	public void setEncoding(ENCODING_DECODING_SCHEME encoding) {
		this.encoding = encoding;
	}

	public boolean equals(GDSOHeader gsdoHeader) {

		if (!this.accessLevel.value().equals(gsdoHeader.getAccessLevel().value()))
			return false;

		if (!this.dataStorageType.value().equals(gsdoHeader.getDataStorageType().value()))
			return false;

		if (!this.encoding.value().equals(gsdoHeader.getEncoding().value()))
			return false;

		if (!this.securityContext.value().equals(gsdoHeader.getSecurityContext().value()))
			return false;

		if (!this.dataType.equals(gsdoHeader.getDataType()))
			return false;

		return true;
	}
	
	public String serialize() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}

}
