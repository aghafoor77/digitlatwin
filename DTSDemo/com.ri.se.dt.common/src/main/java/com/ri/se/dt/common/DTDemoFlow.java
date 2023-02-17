package com.ri.se.dt.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.ri.se.platform.datamodel.SecurityContext;
import org.ri.se.platform.engine.CryptoProcessor;
import org.ri.se.selectivedisclosure.RSAAsymmetricKeyPair;
import org.ri.se.selectivedisclosure.Utils;
import org.ri.se.selectivedisclosure.VC;
import org.ri.se.selectivedisclosure.VerifiableCredential;
import org.ri.se.selectivedisclosure.VerifiableCredentialManager;
import org.web3j.crypto.Credentials;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ri.se.dap.Web3JConnector;

import foundation.identity.jsonld.JsonLDObject;

public class DTDemoFlow {

	public void secureExchange(Object model) throws Exception {

		//String walletDir = "/home/ag/Desktop/RISE/development/traceability/org.ri.se.trace.test/src/main/resources";
		String username0 = "abdul0";
		String password0 = "1122334455";

		Map<String, Object> subjectData = new HashMap<String, Object>();
		if (!Objects.isNull(model)) {
			JsonLDObject jsonObj = JsonLDObject.fromJson(new ObjectMapper().writeValueAsString(model));
			subjectData = jsonObj.getJsonObject();
			
		}
		String holder = "0x5cBc38572ec2f85e21Aa04Cf66f6A8882253C4d6";
		String controller = "0x7A3eA6fccD73c39da21923cdfDd9E5603DBF0A4b";

		controller = VC.PREID + controller;
		holder = VC.PREID + holder;

		String didcom = "did:veid:Ox767389a9889b87cb8a";
		CryptoProcessor cryptoProcessor = new CryptoProcessor();
		byte[] key = cryptoProcessor.generateSymmetrickey(SecurityContext.AES128);
		VerifiableCredentialManager credentialManager = new VerifiableCredentialManager();
		RSAAsymmetricKeyPair keyPair = new Utils().getPrivateKey(username0, password0);

		VerifiableCredential jsonVC = credentialManager.create(controller, keyPair, subjectData, holder, didcom, key);
		String json = jsonVC.toJson();
		System.out.println(json);

		System.in.read();
		System.err
				.println("-------------------------------------------------------------------------------------------");
		System.err.println("1.1. Openning protected microcredentials !");

		VerifiableCredential clearVC = jsonVC.open(key);
		System.out.println(clearVC.toJson());
		String toStore = clearVC.toJson();
		// return clearVC.toJson();

	}

	public void executeKeyExchangeFlow() throws Exception {
		
		String walletDir = "/home/ag/Desktop/RISE/development/traceability/org.ri.se.trace.test/src/main/resources";
		String username0 = "abdul0";
		String password0 = "1122334455";

		String username1 = "abdul1";
		String password1 = "1122334455";

		String url = "http://172.17.0.2:8545";

		String dap = "0x0ed8964b12636f85b095d6c20771a4db31850234";
		Web3JConnector bb = new Web3JConnector(url);
		Credentials abdul1 = new Utils().getCredentials(walletDir, "abdul1", password1);
		Credentials abdul0 = new Utils().getCredentials(walletDir, "abdul0", password0);

		// System.out.println(abdul1.getAddress());
		System.out.println("1. Sender (send shared secret) ---- > : Press enter [Key Exchange]");
		System.in.read();
		// abdul0 ---> abdul1

		Feeder feeder = new Feeder();

		String jsonVC = feeder.sndR1(abdul1.getAddress(), url, dap, walletDir,abdul0, username0, password0);
		System.out.println(jsonVC);

		System.out.println("2. Receiver  (extract values )---- > : Press enter");
		System.in.read();
		Aggregator aggregator = new Aggregator();
		ExtractedValues extractedValues = aggregator.recR1(jsonVC, "abdul1", password1);

		System.out.println("3. Receiver send Ack ---- > : Press enter");
		System.in.read();
		// abdul1 ---> abdul0
		String jsonVCAck = aggregator.sndR2(extractedValues.getSubDid(), abdul0.getAddress(), url, dap, walletDir,
				username1, password1, extractedValues.getSharedKey(), extractedValues.getRandom());
		System.out.println(jsonVCAck);

		System.out.println("4. Verify Key Ack---- > : Press enter");
		System.in.read();
		if (feeder.rcvR2(jsonVCAck, username1, password1, extractedValues.getSharedKey(),
				extractedValues.getRandom())) {
			System.out.println("Successfully key exchanged !");
			return;
		}
		System.out.println("Failed to exchange key !");
	}
}
