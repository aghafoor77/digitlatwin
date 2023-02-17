package org.ri.se.platform.datamodel;

public class DataType {

	private String dataType;
	private String metadataLink;
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getMetadataLink() {
		return metadataLink;
	}

	public void setMetadataLink(String metadataLink) {
		this.metadataLink = metadataLink;
	}	
	public boolean equals(DataType dataType) {

		if (!this.dataType.equals(dataType.getDataType()))
			return false;
		
		if (!this.metadataLink.equals(dataType.getMetadataLink()))
			return false;
		
		return true;
	}
	
	
}
