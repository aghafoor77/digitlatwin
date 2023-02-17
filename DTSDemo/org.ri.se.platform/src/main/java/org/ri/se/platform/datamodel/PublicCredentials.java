package org.ri.se.platform.datamodel;

import java.security.PublicKey;

public class PublicCredentials {
	
	private String ledgerAddress;
	private PublicKey publicKey;
	public String getLedgerAddress() {
		return ledgerAddress;
	}
	public void setLedgerAddress(String ledgerAddress) {
		this.ledgerAddress = ledgerAddress;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	
	

}
