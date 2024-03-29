package com.ri.se.dt.common;

import java.security.PrivateKey;
import java.security.PublicKey;

public class AsymmetricPair {
	
	private PublicKey publicKey;
	private PrivateKey privateKey;
	
	public AsymmetricPair(PublicKey publicKey, PrivateKey privateKey) {
		super();
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	

}
