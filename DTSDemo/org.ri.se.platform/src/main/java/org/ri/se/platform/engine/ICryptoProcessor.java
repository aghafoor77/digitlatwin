package org.ri.se.platform.engine;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.ri.se.platform.datamodel.GDSOHeader;
import org.ri.se.platform.datamodel.GenericDataSharingObject;
import org.ri.se.platform.datamodel.SecurityContext;

public interface ICryptoProcessor {

	public byte[] encrypt(byte[] key,GDSOHeader gdsoHeader, byte [] data) throws Exception;
	public byte[] encrypt(byte key[], GenericDataSharingObject genericDataSharingObject) throws Exception;
	
	public byte[] decrypt(byte [] key, GenericDataSharingObject genericDataSharingObject) throws Exception;
	
	public byte[] generateSymmetrickey(SecurityContext securityContext) throws Exception;
	
	public KeyPair generateRSAKeyPair(int keySize) throws Exception;
	
	public byte[] encrypt(PublicKey publicKey, GenericDataSharingObject genericDataSharingObject) throws Exception ;

	public byte[] decrypt(PrivateKey privateKey, GenericDataSharingObject genericDataSharingObject) throws Exception ;

	public byte[] encrypt(PrivateKey privateKey, GenericDataSharingObject genericDataSharingObject) throws Exception ;
	
	public byte[] decrypt(PublicKey publicKey, GenericDataSharingObject genericDataSharingObject) throws Exception ;
	
}
