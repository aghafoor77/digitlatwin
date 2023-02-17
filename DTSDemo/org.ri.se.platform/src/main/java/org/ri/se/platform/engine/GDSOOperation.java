package org.ri.se.platform.engine;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Objects;

import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
import org.ri.se.platform.datamodel.AccessLevel;
import org.ri.se.platform.datamodel.DataStorageType;
import org.ri.se.platform.datamodel.DataType;
import org.ri.se.platform.datamodel.GDSOHeader;
import org.ri.se.platform.datamodel.GenericDataSharingObject;
import org.ri.se.platform.datamodel.PublicCredentials;
import org.ri.se.platform.datamodel.PublicCredentialsList;
import org.ri.se.platform.datamodel.SecurityContext;

public class GDSOOperation implements IGDSOOperation {

	
	private String ipfsURL = null;

	public GDSOOperation(String ipfsURL) throws Exception {
		this.ipfsURL = ipfsURL;
	}

	public GenericDataSharingObject create(PublicCredentials publicCredentials, AccessLevel accessLevel, DataType dt,
			DataStorageType dst, SecurityContext securityContext, ENCODING_DECODING_SCHEME encoding, byte[] data)
			throws Exception {
		System.out.println("Checking header of generic data sharing object !");
		if (Objects.isNull(securityContext)) {
			System.out.println("SecurityContext not defined, used default 'None'!");
			securityContext = SecurityContext.NONE;
		} else {
			System.out.println("SecurityContext :" + securityContext.value());
		}

		if (Objects.isNull(accessLevel)) {
			System.out.println("AccessLevel not defined, used default 'None'!");
			accessLevel = AccessLevel.NONE;
		} else {
			System.out.println("AccessLevel :" + accessLevel.value());
		}
		if (Objects.isNull(dt)) {
			System.out.println("DataType not defined, used default !");
			dt = new DataType();
		}
		if (Objects.isNull(dst)) {
			System.out.println("DataStorageType not defined, used default 'INLINE'!");
			dst = DataStorageType.INLINE;
		} else {
			System.out.println("DataStorageType :" + dst.value());
		}
		if (Objects.isNull(encoding)) {
			System.out.println("Encoding not defined, used default 'None'!");
			encoding = ENCODING_DECODING_SCHEME.NONE;
		} else {
			System.out.println("Encoding :" + encoding.value());
		}
		if (Objects.isNull(data)) {
			System.out.println("Data payload is null !");
			throw new Exception("Data (payload) is null !");
		}
		return new GenericDataSharingObjectDelegator(ipfsURL).create(publicCredentials, accessLevel, dt, dst,
				securityContext, encoding, data);
	}

	public GenericDataSharingObject create(PublicCredentials publicCredentials, AccessLevel accessLevel, DataType dt,
			DataStorageType dst, SecurityContext securityContext, ENCODING_DECODING_SCHEME encoding, File data)
			throws Exception {

		System.out.println("Checking header of generic data sharing object !");
		if (Objects.isNull(securityContext)) {
			System.out.println("SecurityContext not defined, used default 'None'!");
			securityContext = SecurityContext.NONE;
		} else {
			System.out.println("SecurityContext :" + securityContext.value());
		}

		if (Objects.isNull(accessLevel)) {
			System.out.println("AccessLevel not defined, used default 'None'!");
			accessLevel = AccessLevel.NONE;
		} else {
			System.out.println("AccessLevel :" + accessLevel.value());
		}
		if (Objects.isNull(dt)) {
			System.out.println("DataType not defined, used default !");
			dt = new DataType();
		}
		if (Objects.isNull(dst)) {
			System.out.println("DataStorageType not defined, used default 'INLINE'!");
			dst = DataStorageType.INLINE;
		} else {
			System.out.println("DataStorageType :" + dst.value());
		}
		if (Objects.isNull(encoding)) {
			System.out.println("Encoding not defined, used default 'None'!");
			encoding = ENCODING_DECODING_SCHEME.NONE;
		} else {
			System.out.println("Encoding :" + encoding.value());
		}
		if (Objects.isNull(data)) {
			System.out.println("Data payload is null !");
			throw new Exception("Data (payload) is null !");
		}
		// CipherOutputStream out = new CipherOutputStream(new FileOutputStream(data),
		// cipher);
		System.err.println("AG ToDo : Change this to org cipher stream !");
		Path path = Paths.get(data.getAbsolutePath());
		byte[] input = Files.readAllBytes(path);
		return new GenericDataSharingObjectDelegator(ipfsURL).create(publicCredentials, accessLevel, dt, dst,
				securityContext, encoding, input);
	}

	public GenericDataSharingObject create(PublicCredentialsList publicCredentialsList, AccessLevel accessLevel,
			DataType dt, DataStorageType dst, SecurityContext securityContext, ENCODING_DECODING_SCHEME encoding,
			byte[] data) throws Exception {
		System.out.println("Checking header of generic data sharing object !");
		if (Objects.isNull(securityContext)) {
			System.out.println("SecurityContext not defined, used default 'None'!");
			securityContext = SecurityContext.NONE;
		} else {
			System.out.println("SecurityContext :" + securityContext.value());
		}

		if (Objects.isNull(accessLevel)) {
			System.out.println("AccessLevel not defined, used default 'None'!");
			accessLevel = AccessLevel.NONE;
		} else {
			System.out.println("AccessLevel :" + accessLevel.value());
		}
		if (Objects.isNull(dt)) {
			System.out.println("DataType not defined, used default !");
			dt = new DataType();
		}
		if (Objects.isNull(dst)) {
			System.out.println("DataStorageType not defined, used default 'INLINE'!");
			dst = DataStorageType.INLINE;
		} else {
			System.out.println("DataStorageType :" + dst.value());
		}
		if (Objects.isNull(encoding)) {
			System.out.println("Encoding not defined, used default 'None'!");
			encoding = ENCODING_DECODING_SCHEME.NONE;
		} else {
			System.out.println("Encoding :" + encoding.value());
		}
		if (Objects.isNull(data)) {
			System.out.println("Data payload is null !");
			throw new Exception("Data (payload) is null !");
		}
		return new GenericDataSharingObjectDelegator(ipfsURL).create(publicCredentialsList, accessLevel, dt, dst,
				securityContext, encoding, data);
	}

	public GenericDataSharingObject create(PublicCredentialsList publicCredentialsList, AccessLevel accessLevel,
			DataType dt, DataStorageType dst, SecurityContext securityContext, ENCODING_DECODING_SCHEME encoding,
			File data) throws Exception {
		System.out.println("Checking header of generic data sharing object !");
		if (Objects.isNull(securityContext)) {
			System.out.println("SecurityContext not defined, used default 'None'!");
			securityContext = SecurityContext.NONE;
		} else {
			System.out.println("SecurityContext :" + securityContext.value());
		}

		if (Objects.isNull(accessLevel)) {
			System.out.println("AccessLevel not defined, used default 'None'!");
			accessLevel = AccessLevel.NONE;
		} else {
			System.out.println("AccessLevel :" + accessLevel.value());
		}
		if (Objects.isNull(dt)) {
			System.out.println("DataType not defined, used default !");
			dt = new DataType();
		}
		if (Objects.isNull(dst)) {
			System.out.println("DataStorageType not defined, used default 'INLINE'!");
			dst = DataStorageType.INLINE;
		} else {
			System.out.println("DataStorageType :" + dst.value());
		}
		if (Objects.isNull(encoding)) {
			System.out.println("Encoding not defined, used default 'None'!");
			encoding = ENCODING_DECODING_SCHEME.NONE;
		} else {
			System.out.println("Encoding :" + encoding.value());
		}
		if (Objects.isNull(data)) {
			System.out.println("Data payload is null !");
			throw new Exception("Data (payload) is null !");
		}
		Path path = Paths.get(data.getAbsolutePath());
		byte[] input = Files.readAllBytes(path);
		return new GenericDataSharingObjectDelegator(ipfsURL).create(publicCredentialsList, accessLevel, dt, dst,
				securityContext, encoding, input);
	}

	public byte[] open(PrivateKey privateKey, String address, GenericDataSharingObject genericDataSharingObject)
			throws Exception {

		return new GenericDataSharingObjectDelegator(ipfsURL).openGenericDataSharingObject(privateKey, address,
				genericDataSharingObject);
	}

	@Override
	public GenericDataSharingObject create(PublicCredentials publicCredentials, GDSOHeader gdsoHeader, byte[] data)
			throws Exception {
		if (Objects.isNull(gdsoHeader)) {
			gdsoHeader = new GDSOHeader();
		} 
		System.out.println("Checking header of generic data sharing object !");
		if (Objects.isNull(gdsoHeader.getSecurityContext())) {
			System.out.println("SecurityContext not defined, used default 'None'!");
			gdsoHeader.setSecurityContext(SecurityContext.NONE);
		} else {
			System.out.println("SecurityContext :" + gdsoHeader.getSecurityContext().value());
		}

		if (Objects.isNull(gdsoHeader.getAccessLevel())) {
			System.out.println("AccessLevel not defined, used default 'None'!");
			gdsoHeader.setAccessLevel(AccessLevel.NONE);
		} else {
			System.out.println("AccessLevel :" + gdsoHeader.getAccessLevel().value());
		}
		if (Objects.isNull(gdsoHeader.getDataType())) {
			System.out.println("DataType not defined, used default !");
			gdsoHeader.setDataType(new DataType());
		}
		if (Objects.isNull(gdsoHeader.getDataStorageType())) {
			System.out.println("DataStorageType not defined, used default 'INLINE'!");
			gdsoHeader.setDataStorageType(DataStorageType.INLINE);
		} else {
			System.out.println("DataStorageType :" + gdsoHeader.getDataStorageType().value());
		}
		if (Objects.isNull(gdsoHeader.getEncoding())) {
			System.out.println("Encoding not defined, used default 'None'!");
			gdsoHeader.setEncoding(ENCODING_DECODING_SCHEME.NONE);
		} else {
			System.out.println("Encoding :" + gdsoHeader.getEncoding().value());
		}
		if (Objects.isNull(data)) {
			System.out.println("Data payload is null !");
			throw new Exception("Data (payload) is null !");
		}

		return new GenericDataSharingObjectDelegator(ipfsURL).create(publicCredentials, gdsoHeader.getAccessLevel(),
				gdsoHeader.getDataType(), gdsoHeader.getDataStorageType(), gdsoHeader.getSecurityContext(),
				gdsoHeader.getEncoding(), data);
	}

	@Override
	public GenericDataSharingObject create(PublicCredentials publicCredentials, GDSOHeader gdsoHeader, File data)
			throws Exception {
		if (Objects.isNull(gdsoHeader)) {
			gdsoHeader = new GDSOHeader();
		}
		System.out.println("Checking header of generic data sharing object !");
		if (Objects.isNull(gdsoHeader.getSecurityContext())) {
			System.out.println("SecurityContext not defined, used default 'None'!");
			gdsoHeader.setSecurityContext(SecurityContext.NONE);
		} else {
			System.out.println("SecurityContext :" + gdsoHeader.getSecurityContext().value());
		}

		if (Objects.isNull(gdsoHeader.getAccessLevel())) {
			System.out.println("AccessLevel not defined, used default 'None'!");
			gdsoHeader.setAccessLevel(AccessLevel.NONE);
		} else {
			System.out.println("AccessLevel :" + gdsoHeader.getAccessLevel().value());
		}
		if (Objects.isNull(gdsoHeader.getDataType())) {
			System.out.println("DataType not defined, used default !");
			gdsoHeader.setDataType(new DataType());
		}
		if (Objects.isNull(gdsoHeader.getDataStorageType())) {
			System.out.println("DataStorageType not defined, used default 'INLINE'!");
			gdsoHeader.setDataStorageType(DataStorageType.INLINE);
		} else {
			System.out.println("DataStorageType :" + gdsoHeader.getDataStorageType().value());
		}
		if (Objects.isNull(gdsoHeader.getEncoding())) {
			System.out.println("Encoding not defined, used default 'None'!");
			gdsoHeader.setEncoding(ENCODING_DECODING_SCHEME.NONE);
		} else {
			System.out.println("Encoding :" + gdsoHeader.getEncoding().value());
		}
		if (Objects.isNull(data)) {
			System.out.println("Data payload is null !");
			throw new Exception("Data (payload) is null !");
		}
		System.out.println("AG ToDo : Change this to org cipher stream !");
		Path path = Paths.get(data.getAbsolutePath());
		byte[] input = Files.readAllBytes(path);
		return new GenericDataSharingObjectDelegator(ipfsURL).create(publicCredentials, gdsoHeader.getAccessLevel(),
				gdsoHeader.getDataType(), gdsoHeader.getDataStorageType(), gdsoHeader.getSecurityContext(),
				gdsoHeader.getEncoding(), input);
	}

	@Override
	public GenericDataSharingObject create(PublicCredentialsList publicCredentialsList, GDSOHeader gdsoHeader,
			byte[] data) throws Exception {
		if (Objects.isNull(gdsoHeader)) {
			gdsoHeader = new GDSOHeader();
		}
		System.out.println("Checking header of generic data sharing object !");
		if (Objects.isNull(gdsoHeader.getSecurityContext())) {
			System.out.println("SecurityContext not defined, used default 'None'!");
			gdsoHeader.setSecurityContext(SecurityContext.NONE);
		} else {
			System.out.println("SecurityContext :" + gdsoHeader.getSecurityContext().value());
		}

		if (Objects.isNull(gdsoHeader.getAccessLevel())) {
			System.out.println("AccessLevel not defined, used default 'None'!");
			gdsoHeader.setAccessLevel(AccessLevel.NONE);
		} else {
			System.out.println("AccessLevel :" + gdsoHeader.getAccessLevel().value());
		}
		if (Objects.isNull(gdsoHeader.getDataType())) {
			System.out.println("DataType not defined, used default !");
			gdsoHeader.setDataType(new DataType());
		}
		if (Objects.isNull(gdsoHeader.getDataStorageType())) {
			System.out.println("DataStorageType not defined, used default 'INLINE'!");
			gdsoHeader.setDataStorageType(DataStorageType.INLINE);
		} else {
			System.out.println("DataStorageType :" + gdsoHeader.getDataStorageType().value());
		}
		if (Objects.isNull(gdsoHeader.getEncoding())) {
			System.out.println("Encoding not defined, used default 'None'!");
			gdsoHeader.setEncoding(ENCODING_DECODING_SCHEME.NONE);
		} else {
			System.out.println("Encoding :" + gdsoHeader.getEncoding().value());
		}
		if (Objects.isNull(data)) {
			System.out.println("Data payload is null !");
			throw new Exception("Data (payload) is null !");
		}
		return new GenericDataSharingObjectDelegator(ipfsURL).create(publicCredentialsList, gdsoHeader.getAccessLevel(),
				gdsoHeader.getDataType(), gdsoHeader.getDataStorageType(), gdsoHeader.getSecurityContext(),
				gdsoHeader.getEncoding(), data);
	}

	@Override
	public GenericDataSharingObject create(PublicCredentialsList publicCredentialsList, GDSOHeader gdsoHeader,
			File data) throws Exception {
		if (Objects.isNull(gdsoHeader)) {
			gdsoHeader = new GDSOHeader();
		}
		System.out.println("Checking header of generic data sharing object !");
		if (Objects.isNull(gdsoHeader.getSecurityContext())) {
			System.out.println("SecurityContext not defined, used default 'None'!");
			gdsoHeader.setSecurityContext(SecurityContext.NONE);
		} else {
			System.out.println("SecurityContext :" + gdsoHeader.getSecurityContext().value());
		}

		if (Objects.isNull(gdsoHeader.getAccessLevel())) {
			System.out.println("AccessLevel not defined, used default 'None'!");
			gdsoHeader.setAccessLevel(AccessLevel.NONE);
		} else {
			System.out.println("AccessLevel :" + gdsoHeader.getAccessLevel().value());
		}
		if (Objects.isNull(gdsoHeader.getDataType())) {
			System.out.println("DataType not defined, used default !");
			gdsoHeader.setDataType(new DataType());
		}
		if (Objects.isNull(gdsoHeader.getDataStorageType())) {
			System.out.println("DataStorageType not defined, used default 'INLINE'!");
			gdsoHeader.setDataStorageType(DataStorageType.INLINE);
		} else {
			System.out.println("DataStorageType :" + gdsoHeader.getDataStorageType().value());
		}
		if (Objects.isNull(gdsoHeader.getEncoding())) {
			System.out.println("Encoding not defined, used default 'None'!");
			gdsoHeader.setEncoding(ENCODING_DECODING_SCHEME.NONE);
		} else {
			System.out.println("Encoding :" + gdsoHeader.getEncoding().value());
		}
		if (Objects.isNull(data)) {
			System.out.println("Data payload is null !");
			throw new Exception("Data (payload) is null !");
		}

		Path path = Paths.get(data.getAbsolutePath());
		byte[] input = Files.readAllBytes(path);
		return new GenericDataSharingObjectDelegator(ipfsURL).create(publicCredentialsList, gdsoHeader.getAccessLevel(),
				gdsoHeader.getDataType(), gdsoHeader.getDataStorageType(), gdsoHeader.getSecurityContext(),
				gdsoHeader.getEncoding(), input);
	}
}
