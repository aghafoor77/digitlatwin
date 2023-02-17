package org.ri.se.platform;

import java.security.KeyPair;

import org.acreo.security.bc.CertificateHandlingBC;
import org.acreo.security.certificate.CertificateSuite;
import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
import org.acreo.security.utils.DistinguishName;
import org.acreo.security.utils.StoreHandling;
import org.ri.se.platform.datamodel.AccessLevel;
import org.ri.se.platform.datamodel.DataStorageType;
import org.ri.se.platform.datamodel.DataType;
import org.ri.se.platform.datamodel.EncodedGenericDataSharingObject;
import org.ri.se.platform.datamodel.GDSOHeader;
import org.ri.se.platform.datamodel.GenericDataSharingObject;
import org.ri.se.platform.datamodel.PublicCredentials;
import org.ri.se.platform.datamodel.SecurityContext;
import org.ri.se.platform.engine.CryptoProcessor;
import org.ri.se.platform.engine.GDSOOperation;
import org.ri.se.platform.engine.IGDSOOperation;
import org.ri.se.platform.engine.Utils;

public class GDSOMain {

	/*
	 * public static void examples() throws Exception { GenericDataSharingObject
	 * genericDataSharingObject = new GenericDataSharingObject(); GDSOHeader
	 * gdsoHeader = new GDSOHeader(); gdsoHeader.setAccessLevel(AccessLevel.CHAIN);
	 * gdsoHeader.setDataStorageType(DataStorageType.ONLINE);
	 * gdsoHeader.setSecurityContext(SecurityContext.DES);
	 * gdsoHeader.setEncoding(ENCODING_DECODING_SCHEME.BASE64);
	 * genericDataSharingObject.setGdsoHeader(gdsoHeader); Packet packet = new
	 * Packet(); packet.add("MYPUL", "myenckey"); packet.setData("Here is ");
	 * genericDataSharingObject.setPacket(packet);
	 * System.out.println(genericDataSharingObject.serialize()); CryptoProcessor
	 * cryptoProcessor = new CryptoProcessor(); byte[] key = cryptoProcessor
	 * .generateSymmetrickey(genericDataSharingObject.getGdsoHeader().
	 * getSecurityContext()); byte[] out = cryptoProcessor.encrypt(key,
	 * genericDataSharingObject); Packet enc = new Packet(); enc.add("MYPUL",
	 * "myenckey"); enc.setData(new String(out));
	 * genericDataSharingObject.setPacket(enc);
	 * System.out.println(genericDataSharingObject.serialize()); byte[] dec =
	 * cryptoProcessor.decrypt(key, genericDataSharingObject);
	 * System.out.println(new String(dec));
	 * 
	 * Packet dep = new Packet(); dep.add("MYPUL", "myenckey"); dep.setData(new
	 * String(dec)); genericDataSharingObject.setPacket(dep);
	 * 
	 * // RSA Crypto handling
	 * System.out.println(genericDataSharingObject.serialize()); KeyPair keyPair =
	 * cryptoProcessor.generateRSAKeyPair(1024);
	 * System.out.println(keyPair.getPublic().getEncoded().length);
	 * 
	 * out = cryptoProcessor.encrypt(keyPair.getPublic(), genericDataSharingObject);
	 * enc.add("MYPUL", "myenckey"); enc.setData(new String(out));
	 * genericDataSharingObject.setPacket(enc);
	 * System.out.println(genericDataSharingObject.serialize());
	 * 
	 * out = cryptoProcessor.decrypt(keyPair.getPrivate(),
	 * genericDataSharingObject); enc.add("MYPUL", "myenckey"); enc.setData(new
	 * String(out)); genericDataSharingObject.setPacket(enc);
	 * System.out.println(genericDataSharingObject.serialize());
	 * 
	 * out = cryptoProcessor.encrypt(keyPair.getPrivate(),
	 * genericDataSharingObject); enc.add("MYPUL", "myenckey"); enc.setData(new
	 * String(out)); genericDataSharingObject.setPacket(enc);
	 * System.out.println(genericDataSharingObject.serialize());
	 * 
	 * out = cryptoProcessor.decrypt(keyPair.getPublic(), genericDataSharingObject);
	 * enc.add("MYPUL", "myenckey"); enc.setData(new String(out));
	 * genericDataSharingObject.setPacket(enc);
	 * System.out.println(genericDataSharingObject.serialize());
	 * 
	 * GenericDataSharingObjectDelegator genericDataSharingObjectDelegator = new
	 * GenericDataSharingObjectDelegator( null, null);
	 * 
	 * GenericDataSharingObject gdso =
	 * genericDataSharingObjectDelegator.createGenericDataSharingObject(keyPair,
	 * null, genericDataSharingObject);
	 * System.out.println(gdso.getPacket().getPartners().size()); // KeyPair
	 * keyPair2 = cryptoProcessor.generateRSAKeyPair(1024); byte[] dda =
	 * genericDataSharingObjectDelegator.openGenericDataSharingObject( gdso);
	 * System.out.println(new String(dda));
	 * 
	 * KeyPair keyPair2 = cryptoProcessor.generateRSAKeyPair(1024); gdso =
	 * genericDataSharingObjectDelegator.addPartnerGenericDataSharingObject(keyPair,
	 * keyPair2.getPublic(), gdso);
	 * System.out.println(gdso.getPacket().getPartners().size());
	 * 
	 * dda = genericDataSharingObjectDelegator.openGenericDataSharingObject( gdso);
	 * System.out.println(new String(dda));
	 * 
	 * System.err.println(
	 * "==================================================================");
	 * IGDSOOperation i = new GDSOOperation((keyPair), "address");
	 * GenericDataSharingObject n = i.create(AccessLevel.CHAIN, new DataType(),
	 * DataStorageType.INLINE, "HERE IS  data ".getBytes());
	 * System.out.println(n.serialize());
	 * 
	 * }
	 */

	public static void main(String args[]) throws Exception {
		CryptoProcessor cryptoProcessor = new CryptoProcessor();
		System.err.println("==================================================================");

		KeyPair keyPair = cryptoProcessor.generateRSAKeyPair(1024);
		CertificateSuite certificateSuite = new CertificateSuite("aghafoor77",
				CertificateHandlingBC.getClientKeyUsage());
		String keyStoreCredentials = "3344556677";
		CertificateHandlingBC certificateHandlingBC = new CertificateHandlingBC(certificateSuite, keyStoreCredentials);
		DistinguishName distinguishName = DistinguishName.builder().name("aghafoor77@gmail.com").build();
		try {
			certificateHandlingBC.createSelfSignedClientCert(distinguishName,
					CertificateHandlingBC.getClientKeyUsage());
		} catch (Exception exp) {

		}
		StoreHandling storeHandling = new StoreHandling();

		/*
		 * System.out.println(storeHandling.fetchCertificate(certificateSuite,
		 * distinguishName).getSubjectDN().getName()); System.out.println(
		 * storeHandling.fetchCertificate(certificateSuite,
		 * distinguishName).getPublicKey().getEncoded().length);
		 * System.out.println(storeHandling.fetchPrivateKey(certificateSuite,
		 * keyStoreCredentials, distinguishName) .getEncoded().length);
		 */
		PublicCredentials publicCredentials = new PublicCredentials();
		publicCredentials.setLedgerAddress("address");
		publicCredentials
				.setPublicKey(storeHandling.fetchCertificate(certificateSuite, distinguishName).getPublicKey());

		Utils utils = new Utils();
		;
		if (!utils.isBlockchainAddressExisits(publicCredentials.getLedgerAddress())) {
			throw new Exception("Blockchain address '" + publicCredentials.getLedgerAddress() + "' not found !");
		}

		IGDSOOperation igdsoOperation = new GDSOOperation("/ip4/172.17.0.3/tcp/5001");
		GDSOHeader gdsoHeader = new GDSOHeader();
		gdsoHeader.setAccessLevel(null);
		gdsoHeader.setDataType(new DataType());
		gdsoHeader.setDataStorageType(DataStorageType.INLINE);
		gdsoHeader.setSecurityContext(SecurityContext.AES128);
		gdsoHeader.setEncoding(ENCODING_DECODING_SCHEME.HEX);
		GenericDataSharingObject n = igdsoOperation.create(publicCredentials, gdsoHeader, "Here is data".getBytes());
		System.out.println(n.serialize());

		EncodedGenericDataSharingObject dataSharingObject = new EncodedGenericDataSharingObject(n);
		String ser = dataSharingObject.serialize();

		System.out.println(dataSharingObject.serialize());

		EncodedGenericDataSharingObject dSO = new EncodedGenericDataSharingObject(ser);

		System.out.println(dSO.serialize());

		byte[] data = igdsoOperation.open(
				storeHandling.fetchPrivateKey(certificateSuite, keyStoreCredentials, distinguishName),
				publicCredentials.getLedgerAddress(), n);
		System.out.println(new String(data));

	}
}
