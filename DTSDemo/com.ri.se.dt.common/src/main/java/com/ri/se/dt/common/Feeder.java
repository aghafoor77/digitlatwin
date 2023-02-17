package com.ri.se.dt.common;

import java.net.URI;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
import org.acreo.security.utils.PEMStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.ri.se.platform.datamodel.SecurityContext;
import org.ri.se.platform.engine.CryptoProcessor;
import org.ri.se.platform.engine.CryptoProcessorDelegator;
import org.ri.se.selectivedisclosure.RSAAsymmetricKeyPair;
import org.ri.se.selectivedisclosure.Utils;
import org.ri.se.selectivedisclosure.VC;
import org.ri.se.selectivedisclosure.VerifiableCredential;
import org.ri.se.selectivedisclosure.VerifiableCredentialManager;
import org.ri.se.selectivedisclosure.VerifiablePresentation;
import org.ri.se.verifiablecredentials.asymmetric.RSA2018VerifiableCredentials;
import org.ri.se.verifiablecredentials.entities.ProofAttributes;
import org.web3j.crypto.Credentials;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ri.se.dap.VeidblockManager;

import foundation.identity.did.jsonld.DIDKeywords;
import foundation.identity.jsonld.JsonLDObject;

public class Feeder {
	public static SecurityContextBucket clientBucket = new SecurityContextBucket();

	private String random = null;
	private byte[] key = null;

	public String sndR1(String serverEtherAddress, String etherURL, String dapAddress, String walletDir,Credentials credentials,
			String username, String password) throws Exception {
		

		// Create controller
		String controller = "did:veid:" + credentials.getAddress();

		// Fetch public key from smart contract against BC Address
		VeidblockManager veidblockManager = new VeidblockManager(etherURL);
		if (!veidblockManager.isSmartContractExists(dapAddress, credentials)) {
			throw new Exception("DAP smart contract does not exisit !");
		}

		// credentials.getAddress());
		String myRSAKey = veidblockManager.getRSAPublicKey(dapAddress, credentials, serverEtherAddress);
		if (Objects.isNull(myRSAKey)) {
			System.err.println("Sender is not registered in DAP !");
			throw new Exception(" Ledger address '" + credentials.getAddress()
					+ "' does not have RSA publickey when trying to extract my own !");
		}
		PublicKey pubKey = new PEMStream().fromHex(myRSAKey);

		// Add encrypted symmetric key with key agreement tag
		CryptoProcessor cryptoProcessor = new CryptoProcessor();
		key = cryptoProcessor.generateSymmetrickey(SecurityContext.AES128);
		CryptoProcessorDelegator cryptoProcessorDelegator = new CryptoProcessorDelegator();
		byte[] hashKey;
		byte[] encryptedKey = null;
		try {
			encryptedKey = cryptoProcessor.encrypt(pubKey, key, ENCODING_DECODING_SCHEME.BASE64);
			hashKey = cryptoProcessor.digest(encryptedKey);
		} catch (Exception e) {
			throw new Exception(e);
		}
		if (Objects.isNull(encryptedKey)) {
			throw new Exception("Problems when creating key for sharing !");
		}

		// Creating credentials
		JsonLDObject baseJson = new JsonLDObject();
		baseJson.setJsonObjectKeyValue("controller", controller);
		Vector<String> vec = new Vector();
		vec.add("KeyAgreement");
		baseJson.setJsonObjectKeyValue("type", vec);
		random = RandomStringUtils.randomAlphanumeric(32);
		JsonLDObject subProps = new JsonLDObject();
		subProps.setJsonObjectKeyValue("didcom", "did:veid:" + new String(hashKey) + "");
		subProps.setJsonObjectKeyValue("type", "KeyAgreement");
		subProps.setJsonObjectKeyValue("controller", controller);
		subProps.setJsonObjectKeyValue("key", "" + new String(encryptedKey) + "");
		subProps.setJsonObjectKeyValue("random", "" + random + "");
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("claims", subProps);

		ProofAttributes proofAtt = new ProofAttributes();
		proofAtt.setDomain("http://schema.org");
		proofAtt.setVerificationMethod("http://www.veidblock.com/keyagreementmethod");
		proofAtt.setPurpose(DIDKeywords.JSONLD_TERM_KEYAGREEMENT);

		RSAAsymmetricKeyPair asymmetricPair = new Utils().getPrivateKey(username, password);
		RSA2018VerifiableCredentials rsaVerifiableCredentials = new RSA2018VerifiableCredentials();
		URI uriController = new URI(controller);
		String json = rsaVerifiableCredentials.create(uriController, null, controller + "#key1", baseJson, properties,
				asymmetricPair.getPrivateKey().getEncoded(), asymmetricPair.getPublicKey().getEncoded(), proofAtt);
		return json;
	}

	public boolean rcvR2(String json, String username, String password, byte key[], String random) throws Exception {
		RSA2018VerifiableCredentials rsaVerifiableCredentials = new RSA2018VerifiableCredentials();
		// fetch ip address of server against serverEtherAddress
		if (!rsaVerifiableCredentials.verifyOffline(json)) {
			throw new Exception("Credentials verification failed !");
		}

		Map<String, Object> subject = rsaVerifiableCredentials.getClaims(json);
		String randomRet = subject.get("random").toString();

		CryptoProcessor cryptoProcessor = new CryptoProcessor();

		byte[] recRandom = cryptoProcessor.decrypt(key, ENCODING_DECODING_SCHEME.BASE64, SecurityContext.AES128,
				randomRet.getBytes());
		if (random.equals(new String(recRandom))) {
			CommunicationSecurityContext senderContext = new CommunicationSecurityContext();
			senderContext.setDidcom(subject.get("didcom").toString());
			senderContext.setController(subject.get("controller").toString());
			senderContext.setKey(key);
			senderContext.setInitTime(new Date());
			clientBucket.put(senderContext.getDidcom(), senderContext);
			return true;
		}
		return false;
	}

	public String getRandomShared() {
		return random;
	}

	public byte[] getSharedKey() {
		return key;
	}

	public String sendDataPacket(Object model, RSAAsymmetricKeyPair keyPair, String holder, String controller,
			String didcom, byte[] key) throws Exception {
		Map<String, Object> subjectData = new HashMap<String, Object>();
		if (!Objects.isNull(model)) {
			JsonLDObject jsonObj = JsonLDObject.fromJson(new ObjectMapper().writeValueAsString(model));
			subjectData = jsonObj.getJsonObject();
		}
		controller = VC.PREID + controller;
		holder = VC.PREID + holder;
		VerifiableCredentialManager credentialManager = new VerifiableCredentialManager();
		VerifiableCredential jsonVC = credentialManager.create(controller, keyPair, subjectData, holder, didcom, key);
		String json = jsonVC.toJson();
		return json;
	}
}
