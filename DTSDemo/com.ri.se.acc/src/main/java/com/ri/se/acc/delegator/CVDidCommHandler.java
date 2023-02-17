package com.ri.se.acc.delegator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
import org.ri.se.selectivedisclosure.RSAAsymmetricKeyPair;
import org.ri.se.selectivedisclosure.Utils;
import org.ri.se.selectivedisclosure.security.CryptoProcessor;
import org.ri.se.selectivedisclosure.security.SecurityContext;
import org.ri.se.verifiablecredentials.asymmetric.RSA2018VerifiableCredentials;
import org.ri.se.verifiablecredentials.entities.ProofAttributes;
import org.web3j.crypto.Credentials;

import foundation.identity.did.jsonld.DIDKeywords;
import foundation.identity.jsonld.JsonLDObject;

public class CVDidCommHandler {
	public static SecurityContextBucket serverBucket = new SecurityContextBucket();

	public ExtractedValues recR1(String json, String username, String password) {
		try {
			RSA2018VerifiableCredentials rsaVerifiableCredentials = new RSA2018VerifiableCredentials();
			if (!rsaVerifiableCredentials.verifyOffline(json)) {
				throw new Exception("Credentials verification failed !");
			}
			Map<String, Object> subject = rsaVerifiableCredentials.getClaims(json);
			String keyRet = subject.get("key").toString();
			RSAAsymmetricKeyPair asymmetricPairRec = new Utils().getPrivateKey(username, password);
			CryptoProcessor cryptoProcessor = new CryptoProcessor();
			byte[] key = cryptoProcessor.decrypt(asymmetricPairRec.getPrivateKey(), keyRet.getBytes(),
					ENCODING_DECODING_SCHEME.BASE64);

			ExtractedValues extractedValues = new ExtractedValues();
			extractedValues.setSharedKey(key);
			extractedValues.setRandom(subject.get("random").toString());
			extractedValues.setSubDid(subject.get("didcom").toString());

			CommunicationSecurityContext context = new CommunicationSecurityContext();
			context.setDidcom(extractedValues.getSubDid());
			context.setController(subject.get("controller").toString());
			context.setKey(key);
			context.setInitTime(new Date());
			serverBucket.put(context.getDidcom(), context);
			return extractedValues;
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return null;
	}

	public String sndR2(String subDid, String clientEtherAddress, String etherURL, String dapAddress, String walletDir,Credentials credentials, 
			String username, String password, byte key[], String random) throws Exception {
		//Credentials credentials = new Utils().getCredentials(walletDir, username, password);
		// Create didcom
		String controller = "did:veid:" + credentials.getAddress();

		// Add encrypted symmetric key with key agreement tag
		CryptoProcessor cryptoProcessor = new CryptoProcessor();

		byte[] ecnRandom = cryptoProcessor.encrypt(key, ENCODING_DECODING_SCHEME.BASE64, SecurityContext.AES128,
				random.getBytes());

		// Creating credentials
		JsonLDObject baseJson = new JsonLDObject();
		baseJson.setJsonObjectKeyValue("controller", controller);
		Vector<String> vec = new Vector();
		vec.add("KeyAgreementAck");
		baseJson.setJsonObjectKeyValue("type", vec);

		JsonLDObject subProps = new JsonLDObject();
		subProps.setJsonObjectKeyValue("didcom", subDid);
		subProps.setJsonObjectKeyValue("type", "KeyAgreement");
		subProps.setJsonObjectKeyValue("controller", controller);
		subProps.setJsonObjectKeyValue("random", "" + new String(ecnRandom) + "");

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("claims", subProps);

		ProofAttributes proofAtt = new ProofAttributes();
		proofAtt.setDomain("http://schema.org");
		proofAtt.setVerificationMethod("http://www.veidblock.com/keyagreementmethod");
		proofAtt.setPurpose(DIDKeywords.JSONLD_TERM_KEYAGREEMENT);

		RSAAsymmetricKeyPair asymmetricPair = new Utils().getPrivateKey(username, password);
		System.out.println(asymmetricPair.getPublicKey());
		RSA2018VerifiableCredentials rsaVerifiableCredentials = new RSA2018VerifiableCredentials();
		URI uriController = new URI(controller);
		String json = rsaVerifiableCredentials.create(uriController, null, controller + "#key1", baseJson, properties,
				asymmetricPair.getPrivateKey().getEncoded(), asymmetricPair.getPublicKey().getEncoded(), proofAtt);
		return json;

	}
	
	
	
}