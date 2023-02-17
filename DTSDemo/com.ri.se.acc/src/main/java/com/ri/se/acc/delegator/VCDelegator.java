package com.ri.se.acc.delegator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

import org.ri.se.selectivedisclosure.RSAAsymmetricKeyPair;
import org.ri.se.selectivedisclosure.VC;
import org.ri.se.selectivedisclosure.VerifiableCredential;
import org.ri.se.selectivedisclosure.VerifiableCredentialManager;
import org.ri.se.selectivedisclosure.VerifiablePresentation;
import org.web3j.crypto.Credentials;

public class VCDelegator {

	public String extractVC(String vc, String web3url, String dap, String walletDir, Credentials credentials, String username, String password)
			throws Exception {
		byte vcBytes[] = Base64.getDecoder().decode(vc.getBytes());
		String vcStr = new String(vcBytes);
		writeFile("ACC",vcStr); 
		VerifiableCredential verifiableCredential = new VerifiableCredential(vcStr);
		Map<String, Object> vcMap = verifiableCredential.getJsonObject();
		Object type = vcMap.get("type");
		if (Objects.isNull(type)) {
			throw new Exception("Type of VC does not exist !");
		}
		Object controllerObj = vcMap.get("controller");
		if (Objects.isNull(controllerObj)) {
			throw new Exception("Controller of VC does not exist !");
		}
		if (type.toString().equals("[KeyAgreement]")) {

			CVDidCommHandler cvDidCommHandler = new CVDidCommHandler();
			ExtractedValues extractedValues = cvDidCommHandler.recR1(vcStr, username, password);
			String controller = controllerObj.toString();
			String jsonVCAck = cvDidCommHandler.sndR2(extractedValues.getSubDid(), controller, web3url, dap, walletDir,credentials,
					username, password, extractedValues.getSharedKey(), extractedValues.getRandom());
				return jsonVCAck;

		}
		return null;
	}
	
	public String openDataPacket(String encDataPacket,byte [] key) {
		VerifiableCredential verifiableCredential= new VerifiableCredential(encDataPacket);
		VerifiableCredential clearVC = null;
		try {
			clearVC = verifiableCredential.open(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clearVC.toJson();
	}
	
	public String createVP(RSAAsymmetricKeyPair keyPair, String verifier, String vc, ArrayList<String> requestedItems) throws Exception {
		VerifiableCredentialManager manager = new VerifiableCredentialManager();
		verifier = VC.PREID + verifier;
		VerifiablePresentation vp = manager.createVerifiablePresentation(vc, verifier, requestedItems, keyPair);
		return vp.toJson();
	}
	
	/*public String createSecureVP(RSAAsymmetricKeyPair keyPair, String verifier, String vc, ArrayList<String> requestedItems, String didcom, byte [] key) throws Exception {
		VerifiableCredentialManager manager = new VerifiableCredentialManager();
		verifier = VC.PREID + verifier;
		VerifiablePresentation vptest = manager.createVerifiablePresentation(vc, verifier, requestedItems, keyPair, didcom, key);
		return vptest .toJson();
	}*/
	public static void writeFile(String title ,String text) throws IOException {
		text=text+"\n";
		 String path = "/home/ag/Desktop/DTSDDemo/results/sessionACC.txt";
		 if(!new File(path).exists()) {
			 new File(path).createNewFile();
		 }

	        try {
	        	Files.write(Paths.get(path), title.getBytes(), StandardOpenOption.APPEND);
	            Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
	        } catch (IOException e) {
	        }
	}
}
