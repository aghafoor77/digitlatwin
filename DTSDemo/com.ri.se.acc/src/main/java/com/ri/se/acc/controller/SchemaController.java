package com.ri.se.acc.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ri.se.selectivedisclosure.RSAAsymmetricKeyPair;
import org.ri.se.selectivedisclosure.Utils;
import org.ri.se.verifiablecredentials.asymmetric.RSA2018VerifiableCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;

import com.ri.se.acc.delegator.CVDidCommHandler;
import com.ri.se.acc.delegator.CommunicationSecurityContext;
import com.ri.se.acc.delegator.RequestedAttributes;
import com.ri.se.acc.delegator.VCDelegator;
import com.ri.se.acc.storage.SchemaStorageService;

/***
 * 
 * @author Abdul Ghafoor, abdul.ghafoor@ri.se
 * @category Controller
 * @version 1.0.0,
 * @apiNote Microcredential Microservice Development
 * @apiNote The user controller is responsable to manage user registration,
 *          updates, enable and disable users. Both user, owner and the admin,
 *          can enable and disable user.
 *
 */
@RestController
public class SchemaController {

	/***
	 * To access various operations performed on the User entity
	 */

	Log logger = LogFactory.getLog(SchemaController.class);
	String marker = "\t===> : ";
	@Autowired
	private SchemaStorageService schemaStorageService;
	private String dap = "0x0ed8964b12636f85b095d6c20771a4db31850234";
	String walletDir = "/home/ag/Desktop/RISE/development/traceability/org.ri.se.trace.test/src/main/resources";
	String username = "acc";
	String password = "1122334455";
	String web3url = "http://172.17.0.2:8545";
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/v1/acc/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadSchemaUpload(@RequestBody @Valid String vcms) {
		String vcStr = new String();
		if (vcms.startsWith("\"")) {
			vcStr = vcms.substring(1, vcms.length() - 1);
		} else {
			vcStr = vcms;
		}
		try {

			byte vcBytes[] = Base64.getDecoder().decode(vcStr.getBytes());
			vcStr = new String(vcBytes);
			RSA2018VerifiableCredentials rsaVerifiableCredentials = new RSA2018VerifiableCredentials();
			Map<String, Object> subject = rsaVerifiableCredentials.getClaims(vcStr);
			CommunicationSecurityContext senderContext = CVDidCommHandler.serverBucket
					.get(subject.get("didcom").toString());
			String vsKMAck = new VCDelegator().openDataPacket(vcStr, senderContext.getKey());
			if (!rsaVerifiableCredentials.verifyOffline(vsKMAck)) {
				throw new Exception("Credentials verification failed !");
			}
			String ref = schemaStorageService.save(vsKMAck);
			return ResponseEntity.ok().body(ref);
		} catch (Exception e) {
			System.err.println("Problems when uploading record !"+e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/v1/acc/session", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> secureSessionSchemaUpload(@RequestBody @Valid String vcms) {
			String vcStr = new String();
		if (vcms.startsWith("\"")) {
			vcStr = vcms.substring(1, vcms.length() - 1);
		} else {
			vcStr = vcms;
		}
		try {
			Credentials credentials = new Utils().getCredentials(walletDir, username, password);
			String vsKMAck = new VCDelegator().extractVC(vcStr, web3url, dap, walletDir,credentials, username, password);
			return ResponseEntity.ok().body(vsKMAck);
		} catch (Exception e) {
			System.err.println("Problems when creting session !"+e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/acc/record/{number}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> fetchVCList(@RequestBody @Valid RequestedAttributes requestedAttributes, @PathVariable String number) {
		try {
			byte[] vc = schemaStorageService.load(number);
			RSAAsymmetricKeyPair keyPair = new Utils().getPrivateKey(username, password);
			String verifier = requestedAttributes.getRequester();
			String vp = new VCDelegator().createVP(keyPair, verifier, new String(vc), requestedAttributes.getRequestedAtt());
			String vp64 = new String(Base64.getEncoder().encode(vp.getBytes()));
			return ResponseEntity.ok().body(vp64);
		} catch (Exception e) {
			System.err.println("File fetching problems !"+e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/v1/acc/list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> fetchVCPresentation() {
		
		try {
			ArrayList<String> list = schemaStorageService.list();
			return ResponseEntity.ok().body(list);
		} catch (Exception e) {
			System.err.println("File fetching problems !"+e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	

	@RequestMapping(method = RequestMethod.GET, value = "/v1/acc/etheraddress", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> downloadSchema() {
		Credentials ethCredentials;
		try {
			ethCredentials = new org.ri.se.selectivedisclosure.Utils().getCredentials(walletDir, username, password);
		} catch (Exception e) {
			System.err.println("Problems fetching ether address on server !"+e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		if (java.util.Objects.isNull(ethCredentials)) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok().body(ethCredentials.getAddress());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/v1/acc/del", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteVCs() {
		
		try {
			schemaStorageService.deleteAll();
			return ResponseEntity.ok().body("delete all");
		} catch (Exception e) {
			System.err.println("File fetching problems !"+e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}		
	}	
}