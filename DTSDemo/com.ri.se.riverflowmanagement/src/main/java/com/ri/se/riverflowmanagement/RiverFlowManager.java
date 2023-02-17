package com.ri.se.riverflowmanagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

import org.ri.se.selectivedisclosure.RSAAsymmetricKeyPair;
import org.ri.se.selectivedisclosure.Utils;
import org.ri.se.selectivedisclosure.VerifiableCredential;
import org.web3j.crypto.Credentials;

import com.ri.se.dt.common.CommunicationSecurityContext;
import com.ri.se.dt.common.Feeder;

public class RiverFlowManager {

	public static void main(String[] args) throws Exception {

		RestClient rc = RestClient.builder().baseUrl("http://localhost:9192").build();
		rc.setShowLog(false);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("content-type", "application/json");
		// Get server ether address
		Representation rep = rc.get("/v1/acc/etheraddress", headers);

		String walletDir = "/home/ag/Desktop/RISE/development/traceability/org.ri.se.trace.test/src/main/resources";
		String username = "riverflowmng";
		String password = "1122334455";

		String url = "http://172.17.0.2:8545";

		String dap = "0x0ed8964b12636f85b095d6c20771a4db31850234";
		String serverEtherAddress = rep.getBody().toString();
		Feeder feeder = new Feeder();
		Credentials credentials = new Utils().getCredentials(walletDir, username, password);
		long sessionStart = System.currentTimeMillis();

		String jsonVC = feeder.sndR1(serverEtherAddress, url, dap, walletDir, credentials, username, password);
		writeFile("Exchnage key VC",jsonVC ); 
		String encodedMsg = new String(Base64.getEncoder().encode(jsonVC.getBytes()));
		rep = rc.post("/v1/acc/session", encodedMsg, headers);
		writeFile("Reply Exchnage key VC",rep.getBody().toString() ); 
		
		boolean status = feeder.rcvR2(rep.getBody().toString(), username, password, feeder.getSharedKey(),
				feeder.getRandomShared());
		long sessionEnd = System.currentTimeMillis();
		System.out.println("Session time: " + sessionEnd + "-" + sessionStart + " = " + (sessionEnd - sessionStart));
		if (status) {
			System.out.println("Session established !");

			VerifiableCredential verifiableCredential = new VerifiableCredential(rep.getBody().toString());
			String didcom = verifiableCredential.getClaims().get("didcom").toString();
			CommunicationSecurityContext communicationSecurityContext = Feeder.clientBucket.get(didcom);
			System.out.println(communicationSecurityContext.getInitTime());
			Random rand = new Random();
			int records = 10;
			long recordStart = System.currentTimeMillis();
			for (int i = 0; i < records; i++) {
				int in = rand.nextInt(100);
				RiverFlow riverFlow = new RiverFlow();
				riverFlow.setInflow("" + in);
				riverFlow.setOutflow("" + (in - rand.nextInt(10)));
				riverFlow.setLevel("" + rand.nextInt(50));
				riverFlow.setFlowID("" + i);
				riverFlow.setQuality("" + rand.nextInt(60));
				riverFlow.setSessionID(System.currentTimeMillis() + "");
				RSAAsymmetricKeyPair keyPair = new Utils().getPrivateKey(username, password);
				String holder = serverEtherAddress;
				String encDataPacket = feeder.sendDataPacket(riverFlow, keyPair, holder, credentials.getAddress(),
						communicationSecurityContext.getDidcom(), communicationSecurityContext.getKey());
				
				writeFile("Encrypred : VC",encDataPacket ); 
				
				String encodedDP = new String(Base64.getEncoder().encode(encDataPacket.getBytes()));
				rep = rc.post("/v1/acc/upload", encodedDP, headers);
			}
			long recordEnd = System.currentTimeMillis();
			System.out.println("RFM Upload Record time: " + records + "\t" + recordStart + "-" + recordEnd + " = "
					+ (recordEnd - recordStart));
		} else {
			System.out.println("fail to establish session !");
		}
	}
	
	public void execute(int records) throws Exception {

		RestClient rc = RestClient.builder().baseUrl("http://localhost:9192").build();
		rc.setShowLog(false);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("content-type", "application/json");
		// Get server ether address
		Representation rep = rc.get("/v1/acc/etheraddress", headers);

		String walletDir = "/home/ag/Desktop/RISE/development/traceability/org.ri.se.trace.test/src/main/resources";
		String username = "riverflowmng";
		String password = "1122334455";

		String url = "http://172.17.0.2:8545";

		String dap = "0x0ed8964b12636f85b095d6c20771a4db31850234";
		String serverEtherAddress = rep.getBody().toString();
		Feeder feeder = new Feeder();
		Credentials credentials = new Utils().getCredentials(walletDir, username, password);
		long sessionStart = System.currentTimeMillis();

		String jsonVC = feeder.sndR1(serverEtherAddress, url, dap, walletDir, credentials, username, password);
		
		String encodedMsg = new String(Base64.getEncoder().encode(jsonVC.getBytes()));
		rep = rc.post("/v1/acc/session", encodedMsg, headers);
		boolean status = feeder.rcvR2(rep.getBody().toString(), username, password, feeder.getSharedKey(),
				feeder.getRandomShared());
		long sessionEnd = System.currentTimeMillis();
		System.out.println("Session time: " + sessionEnd + "-" + sessionStart + " = " + (sessionEnd - sessionStart));
		writeSession("Session time: " + sessionEnd + "-" + sessionStart + " = " + (sessionEnd - sessionStart));
		if (status) {
			System.out.println("Session established !");

			VerifiableCredential verifiableCredential = new VerifiableCredential(rep.getBody().toString());
			String didcom = verifiableCredential.getClaims().get("didcom").toString();
			CommunicationSecurityContext communicationSecurityContext = Feeder.clientBucket.get(didcom);
			System.out.println(communicationSecurityContext.getInitTime());
			Random rand = new Random();
			long recordStart = System.currentTimeMillis();
			for (int i = 0; i < records; i++) {
				int in = rand.nextInt(100);
				RiverFlow riverFlow = new RiverFlow();
				riverFlow.setInflow("" + in);
				riverFlow.setOutflow("" + (in - rand.nextInt(10)));
				riverFlow.setLevel("" + rand.nextInt(50));
				riverFlow.setFlowID("" + i);
				riverFlow.setQuality("" + rand.nextInt(60));
				riverFlow.setSessionID(System.currentTimeMillis() + "");
				RSAAsymmetricKeyPair keyPair = new Utils().getPrivateKey(username, password);
				String holder = serverEtherAddress;
				String encDataPacket = feeder.sendDataPacket(riverFlow, keyPair, holder, credentials.getAddress(),
						communicationSecurityContext.getDidcom(), communicationSecurityContext.getKey());
				String encodedDP = new String(Base64.getEncoder().encode(encDataPacket.getBytes()));
				System.out.println();
				rep = rc.post("/v1/acc/upload", encodedDP, headers);
			}
			long recordEnd = System.currentTimeMillis();
			System.out.println("RFM Upload Record time: " + records + " " + recordStart + "-" + recordEnd + " = "
					+ (recordEnd - recordStart));
			write("RFM Upload Record time: " + records + " " + recordStart + "-" + recordEnd + " = "
					+ (recordEnd - recordStart));
			
		} else {
			System.out.println("fail to establish session !");
		}
	}
	public void write(String text) throws IOException {
		text=text+"\n";
		 String path = "/home/ag/Desktop/DTSDDemo/results/rfm.txt";
		 if(!new File(path).exists()) {
			 new File(path).createNewFile();
		 }

	        try {
	            Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
	        } catch (IOException e) {
	        }
	}
	public void writeSession(String text) throws IOException {
		text=text+"\n";
		 String path = "/home/ag/Desktop/DTSDDemo/results/session.txt";
		 if(!new File(path).exists()) {
			 new File(path).createNewFile();
		 }

	        try {
	            Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
	        } catch (IOException e) {
	        }
	}
	
	public static void writeFile(String title ,String text) throws IOException {
		text=text+"\n";
		 String path = "/home/ag/Desktop/DTSDDemo/results/sessionVC.txt";
		 if(!new File(path).exists()) {
			 new File(path).createNewFile();
		 }

	        try {
	        	Files.write(Paths.get(path), title.getBytes(), StandardOpenOption.APPEND);
	            Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
	        } catch (IOException e) {
	        }
	}
	
	public void delete() throws Exception{
		RestClient rc = RestClient.builder().baseUrl("http://localhost:9192").build();
		rc.setShowLog(false);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("content-type", "application/json");
		// Get server ether address
		Representation rep = rc.get("/v1/acc/del", headers);
	}
	
}
