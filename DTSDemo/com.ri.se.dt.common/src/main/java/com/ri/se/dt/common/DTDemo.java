
package com.ri.se.dt.common;

import java.net.URI;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import org.acreo.security.bc.CertificateHandlingBC;
import org.acreo.security.certificate.CertificateSuite;
import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
import org.acreo.security.utils.DistinguishName;
import org.acreo.security.utils.PEMStream;
import org.acreo.security.utils.StoreHandling;
import org.apache.commons.lang3.RandomStringUtils;
import org.ri.se.platform.datamodel.SecurityContext;
import org.ri.se.platform.engine.CryptoProcessor;
import org.ri.se.platform.engine.CryptoProcessorDelegator;
import org.ri.se.selectivedisclosure.Utils;
import org.ri.se.verifiablecredentials.asymmetric.RSA2018VerifiableCredentials;
import org.ri.se.verifiablecredentials.entities.ProofAttributes;
import org.web3j.crypto.Credentials;

import com.ri.se.dap.VeidblockManager;
import com.ri.se.dt.common.models.HydropowerData;
import com.ri.se.dt.common.models.RiverFlow;
import com.ri.se.dt.common.models.WaterTreatment;

import foundation.identity.did.jsonld.DIDKeywords;
import foundation.identity.jsonld.JsonLDObject;

public class DTDemo {

	public static void main(String[] args) throws Exception {

		new DTDemoFlow().executeKeyExchangeFlow();

		RiverFlow riverFlow = new RiverFlow();
		riverFlow.setFlowID("1");
		riverFlow.setInflow("2.5");
		riverFlow.setLevel("10f");
		riverFlow.setOutflow("2.4");
		riverFlow.setQuality("high");
		riverFlow.setSessionID("WM238437612SE");
		new DTDemoFlow().secureExchange(riverFlow);

		WaterTreatment waterTreatment = new WaterTreatment();
		waterTreatment.setEffluent("effluentinformation");
		waterTreatment.setInflow("2.0");
		waterTreatment.setResidualRemovingPercentage("10");
		waterTreatment.setStationId("WM238437612SE");
		waterTreatment.setTotoalDisolvedSolids("TotoalDisolvedSolidsInfor");
		waterTreatment.setTreatmentProcess("Bajri wala");
		waterTreatment.setWaterTreated("1.9");
		new DTDemoFlow().secureExchange(waterTreatment);

		HydropowerData hydropowerData = new HydropowerData();
		hydropowerData.setEnergyProduced("10kW");
		hydropowerData.setPrecipitation("7mm");
		hydropowerData.setReservoirLevel("7f");
		hydropowerData.setStationID("WM238437612SE");
		hydropowerData.setTemperature("22C");
		hydropowerData.setWindDirection("South");
		hydropowerData.setWindSpeed("7km/h");
		new DTDemoFlow().secureExchange(hydropowerData);
		// new DTDemoFlow().extractSecureSubject();
	}

	public static String createsDiDcomKeyAgreement(String serverEtherAddress, String etherURL, String dapAddress,
			String walletDir, String username, String password) throws Exception {

		Credentials credentials = new Utils().getCredentials(walletDir, username, password);
		// Create didcom
		String controller = "did:veid:" + credentials.getAddress();

		// Fetch public key from smart contract against BC Address
		VeidblockManager veidblockManager = new VeidblockManager(etherURL);
		if (!veidblockManager.isSmartContractExists(dapAddress, credentials)) {
			throw new Exception("DAP smart contract does not exisit !");
		}

		System.err.println("Fetching RSA key of sender from DAP :" + credentials.getAddress());
		String myRSAKey = veidblockManager.getRSAPublicKey(dapAddress, credentials, serverEtherAddress);
		if (Objects.isNull(myRSAKey)) {
			System.err.println("Sender is not registered in DAP !");
			throw new Exception(" Ledger address '" + credentials.getAddress()
					+ "' does not have RSA publickey when trying to extract my own !");
		}

		System.err.println("Sender RSA key :" + myRSAKey);
		PublicKey pubKey = new PEMStream().fromHex(myRSAKey);

		// Add encrypted symmetric key with key agreement tag
		CryptoProcessor cryptoProcessor = new CryptoProcessor();
		System.err.println("Generating symmetric key  . . . ");
		byte[] key = cryptoProcessor.generateSymmetrickey(SecurityContext.AES128);
		System.out.println("Symmetric key  generated and size id " + key.length);
		System.out.println("Encrypting data . . .  ");
		CryptoProcessorDelegator cryptoProcessorDelegator = new CryptoProcessorDelegator();

		byte[] encryptedKey = null;
		try {
			encryptedKey = cryptoProcessor.encrypt(pubKey, key, ENCODING_DECODING_SCHEME.BASE64);
			System.out.println(new String(encryptedKey));
		} catch (Exception e) {
			throw new Exception(e);
		}
		if (Objects.isNull(encryptedKey)) {
			throw new Exception("Problems when creating key for sharing !");
		}

		// Creating credentials
		JsonLDObject baseJson = new JsonLDObject();
		baseJson.setJsonObjectKeyValue("did", controller);
		Vector<String> vec = new Vector();
		vec.add("KeyAgreement");
		baseJson.setJsonObjectKeyValue("type", vec);

		String hashOfSK = new String(cryptoProcessor.digest(encryptedKey));
		JsonLDObject subProps = new JsonLDObject();
		subProps.setJsonObjectKeyValue("did", "did:veid:" + hashOfSK + "");
		subProps.setJsonObjectKeyValue("type", "KeyAgreement");
		subProps.setJsonObjectKeyValue("controller", controller);
		subProps.setJsonObjectKeyValue("key", "" + new String(encryptedKey) + "");

		/*
		 * JsonLDObject credProps = new JsonLDObject();
		 * credProps.setJsonObjectKeyValue("did", "did:veid:" + hashOfSK + "");
		 * credProps.setJsonObjectKeyValue("issuer", controller);
		 * credProps.setJsonObjectKeyValue("name", "" + new String(encryptedKey) + "");
		 */
		Map<String, Object> properties = new HashMap<String, Object>();
		// properties.put("subject", credProps);
		properties.put("Subject", subProps);

		ProofAttributes proofAtt = new ProofAttributes();
		proofAtt.setDomain("http://schema.org");
		proofAtt.setVerificationMethod("http://www.veidblock.com/keyagreementmethod");
		proofAtt.setPurpose(DIDKeywords.JSONLD_TERM_KEYAGREEMENT);

		AsymmetricPair asymmetricPair = getPrivateKey(username, password);
		RSA2018VerifiableCredentials rsaVerifiableCredentials = new RSA2018VerifiableCredentials();
		URI uriController = new URI(controller);
		String json = rsaVerifiableCredentials.create(uriController, null, "did:veid:" + hashOfSK + "#key1", baseJson,
				properties, asymmetricPair.getPrivateKey().getEncoded(), asymmetricPair.getPublicKey().getEncoded(),
				proofAtt);
		return json;
	}

	public static byte[] getSharedKey(String json, String username, String password) throws Exception {
		RSA2018VerifiableCredentials rsaVerifiableCredentials = new RSA2018VerifiableCredentials();

		// fetch ip address of server against serverEtherAddress
		System.out.println(json);
		System.err.println("Verification Result :" + rsaVerifiableCredentials.verifyOffline(json));
		Map<String, Object> subject = rsaVerifiableCredentials.getClaims(json);
		System.out.println(subject.get("key").toString());
		String keyRet = subject.get("key").toString();

		AsymmetricPair asymmetricPairRec = getPrivateKey(username, password);
		CryptoProcessor cryptoProcessor = new CryptoProcessor();
		byte[] key = cryptoProcessor.decrypt(asymmetricPairRec.getPrivateKey(), keyRet.getBytes(),
				ENCODING_DECODING_SCHEME.BASE64);
		return key;
	}

	public static AsymmetricPair getPrivateKey(String username, String password) throws Exception {
		DistinguishName distinguishName = DistinguishName.builder().name(username).build();
		System.out.println("Distinguished name : " + distinguishName.toString());
		StoreHandling storeHandling = new StoreHandling();
		CertificateSuite certificateSuite = new CertificateSuite(username, CertificateHandlingBC.getClientKeyUsage());
		System.out.println("Username : " + username);
		System.out.println("Public key : ");

		System.out.println("0x"
				+ PEMStream.toHex(storeHandling.fetchCertificate(certificateSuite, distinguishName).getPublicKey()));
		return new AsymmetricPair(storeHandling.fetchCertificate(certificateSuite, distinguishName).getPublicKey(),
				storeHandling.fetchPrivateKey(certificateSuite, password, distinguishName));

	}

	// This will consider all elements including subject, hashes, proof and other
	// elements
	public static String createVerifiableCredential(String serverEtherAddress, String etherURL, String dapAddress,
			String walletDir, String username, String password, String didKey, byte[] key,
			HashMap<String, String> subjectData) throws Exception {

		Credentials credentials = new Utils().getCredentials(walletDir, username, password);
		// Create didcom
		String controller = "did:veid:" + credentials.getAddress();

		// Fetch public key from smart contract against BC Address
		VeidblockManager veidblockManager = new VeidblockManager(etherURL);
		if (!veidblockManager.isSmartContractExists(dapAddress, credentials)) {
			throw new Exception("DAP smart contract does not exisit !");
		}

		System.err.println("Fetching RSA key of sender from DAP :" + credentials.getAddress());
		String myRSAKey = veidblockManager.getRSAPublicKey(dapAddress, credentials, serverEtherAddress);
		if (Objects.isNull(myRSAKey)) {
			System.err.println("Sender is not registered in DAP !");
			throw new Exception(" Ledger address '" + credentials.getAddress()
					+ "' does not have RSA publickey when trying to extract my own !");
		}

		System.err.println("Sender RSA key :" + myRSAKey);
		PublicKey pubKey = new PEMStream().fromHex(myRSAKey);

		// subjectData

		String hashOfAllHashes = "";

		// Add encrypted symmetric key with key agreement tag
		// Creating credentials
		JsonLDObject baseJson = new JsonLDObject();
		baseJson.setJsonObjectKeyValue("did", controller);
		Vector<String> vec = new Vector();
		vec.add("Verification");
		baseJson.setJsonObjectKeyValue("type", vec);

		// String hashOfSK = new String(cryptoProcessor.digest(encryptedKey));
		JsonLDObject subProps = new JsonLDObject();
		subProps.setJsonObjectKeyValue("did", "did:veid:" + hashOfAllHashes + "");
		subProps.setJsonObjectKeyValue("model", "NameofModelClass");
		ExtendedKeyValueList list = new ExtendedKeyValueList();
		HashMap<String, String> hashesList = new HashMap<String, String>();
		int i = 0;
		CryptoProcessor cryptoProcessor = new CryptoProcessor();
		for (String aDataKey : subjectData.keySet()) {
			ExtendedKeyValue enhancedKeyValue = new ExtendedKeyValue();
			String id = "veid" + i;
			enhancedKeyValue.setKey(aDataKey);
			enhancedKeyValue.setValue(subjectData.get(aDataKey));
			enhancedKeyValue.setRand(RandomStringUtils.randomAlphanumeric(64));
			hashesList.put(id, new String(cryptoProcessor.digest(enhancedKeyValue.serialize().getBytes())));
			list.put(id, enhancedKeyValue);
			i++;
		}
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("subject", list);
		properties.put("hashes", hashesList);

		ProofAttributes proofAtt = new ProofAttributes();
		proofAtt.setDomain("http://schema.org");
		proofAtt.setVerificationMethod("http://www.veidblock.com/keyagreementmethod");
		proofAtt.setPurpose(DIDKeywords.JSONLD_TERM_VERIFICATIONMETHOD);

		AsymmetricPair asymmetricPair = getPrivateKey(username, password);
		RSA2018VerifiableCredentials rsaVerifiableCredentials = new RSA2018VerifiableCredentials();
		URI uriController = new URI(controller);
		String json = rsaVerifiableCredentials.create(uriController, null, "did:veid:#key1", baseJson, properties,
				asymmetricPair.getPrivateKey().getEncoded(), asymmetricPair.getPublicKey().getEncoded(), proofAtt);
		return json;

	}

	// This will consider all elements except subject becasue the proof will be
	// based on the the hashes
	public void verifyVerifiableCredential() {

	}

}