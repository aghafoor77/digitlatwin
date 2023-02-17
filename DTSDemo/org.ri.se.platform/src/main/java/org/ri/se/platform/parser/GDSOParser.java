package org.ri.se.platform.parser;

import java.util.Objects;

import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
import org.ri.se.platform.datamodel.AccessLevel;
import org.ri.se.platform.datamodel.DataStorageType;
import org.ri.se.platform.datamodel.DataType;
import org.ri.se.platform.datamodel.GDSOHeader;
import org.ri.se.platform.datamodel.GenericDataSharingObject;
import org.ri.se.platform.datamodel.SecurityContext;

public class GDSOParser {

	public static void main(String[] args) throws Exception {

		GDSOParser gdsOParser = new GDSOParser();
		GenericDataSharingObject genericDataSharingObject = new GenericDataSharingObject();
		GDSOHeader gdsoHeader = new GDSOHeader();
		gdsoHeader.setAccessLevel(AccessLevel.CHAIN);
		gdsoHeader.setDataStorageType(DataStorageType.IPFS);
		DataType dataType = new DataType();
		dataType.setDataType("Movement");
		dataType.setMetadataLink("MovementMetaData");
		gdsoHeader.setDataType(dataType);
		gdsoHeader.setEncoding(ENCODING_DECODING_SCHEME.BASE64);
		gdsoHeader.setSecurityContext(SecurityContext.AES256);

		genericDataSharingObject.setGdsoHeader(gdsoHeader);
		System.out.println(gdsOParser.parse(genericDataSharingObject.getGdsoHeader()));

		GDSOHeader header = new GDSOHeader();
		header.setAccessLevel(AccessLevel.CHAIN);
		header.setDataStorageType(DataStorageType.IPFS);
		DataType dataType1 = new DataType();
		dataType1.setDataType("Movement");
		dataType1.setMetadataLink("MovementMetaData");
		header.setDataType(dataType1);
		header.setEncoding(ENCODING_DECODING_SCHEME.BASE64);
		header.setSecurityContext(SecurityContext.AES256);

		genericDataSharingObject.setGdsoHeader(header);
		System.out.println(gdsOParser.parse(genericDataSharingObject.getGdsoHeader()));

		System.out.println(header.equals(gdsoHeader));
		GDSOHeader gg = gdsOParser.extract(""+gdsOParser.parse(genericDataSharingObject.getGdsoHeader()));
		System.out.println(gg.equals(header));

	}

	private String version = "1";

	public GDSOParser() {
	}

	public String parse(GDSOHeader header) {
		if(Objects.isNull(header)) {
			return "0.0.0.0.0.0.0";
		}
		StringBuffer buff = new StringBuffer();
		buff.append(version);
		buff.append(".");
		buff.append(securityContext(header.getSecurityContext()));
		buff.append(".");
		buff.append(accessLevel(header.getAccessLevel()));
		buff.append(".");
		buff.append(dataStorageType(header.getDataStorageType()));
		buff.append(".");
		buff.append(encoding(header.getEncoding()));
		buff.append(".");
		buff.append(dataType(header.getDataType()));
		return buff.toString();
	}

	private SecurityContext securityContext(String code) {
		if (Objects.isNull(code))
			return null;
		switch (code) {
		case "1":
			return SecurityContext.DES;
		case "2":
			return SecurityContext.AES128;
		case "3":
			return SecurityContext.AES192;
		case "4":
			return SecurityContext.AES256;
		case "5":
			return SecurityContext.PRIVACY;
		case "6":
			return SecurityContext.NONE;
		default:
			return null;
		}
	}

	private AccessLevel accessLevel(String code) {
		if (Objects.isNull(code))
			return null;
		switch (code) {
		case "1":
			return AccessLevel.NONE;
		case "2":
			return AccessLevel.CHAIN;
		case "3":
			return AccessLevel.PREVIOUSOWNER;
		case "4":
			return AccessLevel.PREVIOUSOWNERS;
		case "5":
			return AccessLevel.RECIPIENT;
		case "6":
			return AccessLevel.RECIPIENTS;
		case "7":
			return AccessLevel.PRIVACY;
		default:
			return null;
		}
	}

	private DataStorageType dataStorageType(String code) {
		if (Objects.isNull(code))
			return null;

		switch (code) {
		case "1":
			return DataStorageType.INLINE;
		case "2":
			return DataStorageType.FTP;
		case "3":
			return DataStorageType.IPFS;
		default:
			return null;
		}
	}

	private DataType dataType(String code1, String code2) {

		DataType dataType = new DataType();
		
		if (Objects.isNull(code1))
			dataType.setDataType(null);
		
		if (code1.equals("0"))
			dataType.setDataType(null);
		else if (code1.equals("1"))
			dataType.setDataType("");
		else
			dataType.setDataType(code1);
		
		if (Objects.isNull(code2))
			dataType.setMetadataLink(null);
		
		if (code2.equals("0"))
			dataType.setMetadataLink(null);
		else if (code2.equals("1"))
			dataType.setMetadataLink("");
		else
			dataType.setMetadataLink(code2);
		return dataType;
	}

	private ENCODING_DECODING_SCHEME encoding(String code) {
		if (Objects.isNull(code))
			return null;
		switch (code) {
		case "1":
			return  ENCODING_DECODING_SCHEME .BASE64;
		case "2":
			return  ENCODING_DECODING_SCHEME.HEX;
		case "3":
			return  ENCODING_DECODING_SCHEME .NONE;
		default:
			return null;
		}
	}

//==================================================
	private String securityContext(SecurityContext securityContext) {
		if (Objects.isNull(securityContext))
			return "0";
		switch (securityContext) {
		case DES:
			return "1";
		case AES128:
			return "2";
		case AES192:
			return "3";
		case AES256:
			return "4";
		case PRIVACY:
			return "5";
		case NONE:
			return "6";
		default:
			return "0";
		}
	}

	private String accessLevel(AccessLevel accessLevel) {
		if (Objects.isNull(accessLevel))
			return "0";
		switch (accessLevel) {
		case NONE:
			return "1";
		case CHAIN:
			return "2";
		case PREVIOUSOWNER:
			return "3";
		case PREVIOUSOWNERS:
			return "4";
		case RECIPIENT:
			return "5";
		case RECIPIENTS:
			return "6";
		case PRIVACY:
			return "7";
		default:
			return "0";
		}
	}

	private String dataStorageType(DataStorageType dataStorageType) {
		if (Objects.isNull(dataStorageType))
			return "0";

		switch (dataStorageType) {
		case INLINE:
			return "1";
		case FTP:
			return "2";
		case IPFS:
			return "3";
		default:
			return "0";
		}
	}

	private String dataType(DataType dataType) {

		StringBuffer retCode = new StringBuffer();
		if (Objects.isNull(dataType))
			return "0.0";

		if (Objects.isNull(dataType.getDataType()))
			retCode.append("0");
		else if (dataType.getDataType().equals("")) {
			retCode.append("1");
		}
		else
			retCode.append(dataType.getDataType());
		retCode.append(".");
		if (Objects.isNull(dataType.getMetadataLink()))
			retCode.append("0");
		else if (dataType.getMetadataLink().equals(""))
			retCode.append("1");
		else
			retCode.append(dataType.getMetadataLink());

		return retCode.toString();
	}

	private String encoding(ENCODING_DECODING_SCHEME encoding) {
		if (Objects.isNull(encoding))
			return "0";
		switch (encoding) {
		case BASE64:
			return "1";
		case HEX:
			return "2";
		case NONE:
			return "3";
		default:
			return "0";
		}
	}

	public GDSOHeader extract(String encodedHeader) throws Exception {
		
		if(!encodedHeader.contains(".")) {
			throw new Exception("Invalid encoded value !");
		}
		String codes[] = encodedHeader.split("\\.");
		if(!codes[0].equals(version)) {
			throw new Exception("Invalid version. Supported '"+version+"'");
		}
		if(codes.length != 7) {
			throw new Exception("Invalid encoded value becasue number of values do not match !");
		}
		
		GDSOHeader gdsoHeader = new GDSOHeader();
		gdsoHeader.setSecurityContext(securityContext(codes[1]));
		gdsoHeader.setAccessLevel(accessLevel(codes[2]));
		gdsoHeader.setDataStorageType(dataStorageType(codes[3]));
		gdsoHeader.setEncoding(encoding(codes[4]));
		gdsoHeader.setDataType(dataType(codes[5], codes[6]));
		
		return gdsoHeader;
	}

}
