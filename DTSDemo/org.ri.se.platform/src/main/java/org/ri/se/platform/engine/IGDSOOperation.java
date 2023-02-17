package org.ri.se.platform.engine;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
import org.ri.se.platform.datamodel.AccessLevel;
import org.ri.se.platform.datamodel.DataStorageType;
import org.ri.se.platform.datamodel.DataType;
import org.ri.se.platform.datamodel.GDSOHeader;
import org.ri.se.platform.datamodel.GenericDataSharingObject;
import org.ri.se.platform.datamodel.PublicCredentials;
import org.ri.se.platform.datamodel.PublicCredentialsList;
import org.ri.se.platform.datamodel.SecurityContext;

public interface IGDSOOperation {
	
	public GenericDataSharingObject create(PublicCredentials PublicCredentials, AccessLevel accessLevel, DataType dt, DataStorageType dst,SecurityContext securityContext, ENCODING_DECODING_SCHEME encoding, byte [] data) throws Exception;
	public GenericDataSharingObject create(PublicCredentials publicCredentials, AccessLevel accessLevel, DataType dt, DataStorageType dst, SecurityContext securityContext, ENCODING_DECODING_SCHEME encoding,File data) throws Exception; 
	
	
	public GenericDataSharingObject create(PublicCredentialsList publicCredentialsList, AccessLevel accessLevel, DataType dt, DataStorageType dst, SecurityContext securityContext, ENCODING_DECODING_SCHEME encoding,byte [] data) throws Exception;
	public GenericDataSharingObject create(PublicCredentialsList publicCredentialsList, AccessLevel accessLevel, DataType dt, DataStorageType dst, SecurityContext securityContext, ENCODING_DECODING_SCHEME encoding,File data) throws Exception; 
	
	public GenericDataSharingObject create(PublicCredentials PublicCredentials, GDSOHeader gdsoHeader, byte [] data) throws Exception;
	public GenericDataSharingObject create(PublicCredentials publicCredentials, GDSOHeader gdsoHeader,File data) throws Exception; 
	
	public GenericDataSharingObject create(PublicCredentialsList publicCredentialsList, GDSOHeader gdsoHeader, byte [] data) throws Exception;
	public GenericDataSharingObject create(PublicCredentialsList publicCredentialsList, GDSOHeader gdsoHeader,File data) throws Exception; 
	
	public byte[] open(PrivateKey privateKey, String address, GenericDataSharingObject genericDataSharingObject) throws Exception;	
}


