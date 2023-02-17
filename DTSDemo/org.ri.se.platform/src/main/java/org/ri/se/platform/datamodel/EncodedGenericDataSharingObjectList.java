package org.ri.se.platform.datamodel;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EncodedGenericDataSharingObjectList extends ArrayList<EncodedGenericDataSharingObject> {
	
	public String toJSON() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);

	}
	
	public void fromJSON(String json) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		EncodedGenericDataSharingObjectList list = objectMapper.readValue(json, EncodedGenericDataSharingObjectList.class);
		for(EncodedGenericDataSharingObject egdso : list ) {
			this.add(egdso);	
		}		
	}
}
