package com.ri.se.citizen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.ri.se.selectivedisclosure.Utils;
import org.ri.se.selectivedisclosure.VerifiablePresentation;
import org.web3j.crypto.Credentials;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CitizenPresentation {

	public static void main(String[] args) throws Exception {
		RestClient rc = RestClient.builder().baseUrl("http://localhost:9192").build();
		rc.setShowLog(false);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("content-type", "application/json");
		// Get server ether address
		Representation rep = rc.get("/v1/acc/list", headers);
		ArrayList<String> vcs = new ObjectMapper().readValue(rep.getBody().toString(), ArrayList.class);
		ArrayList<String> requestedAtt = new ArrayList<String>();
		requestedAtt.add("flowID");
		requestedAtt.add("quality");
		

		String walletDir = "/home/ag/Desktop/RISE/development/traceability/org.ri.se.trace.test/src/main/resources";
		String username = "citizen";
		String password = "1122334455";
		Credentials credentials = new Utils().getCredentials(walletDir, username, password);

		RequestedAttributes requestedAttributes = new RequestedAttributes();
		requestedAttributes.setRequestedAtt(requestedAtt);
		requestedAttributes.setRequester(credentials.getAddress());
		System.out.println(" Requested Values: ");
		long presenOpenStart = System.currentTimeMillis();
		for (String reqFile : vcs) {
			rep = rc.put("/v1/acc/record/" + reqFile, requestedAttributes, headers);
			
			Map<String, Object> claims = openResponse(rep.getBody().toString());
			/*Iterator<String> i = claims.keySet().iterator();
			while (i.hasNext()) {
				Map<String, Object> values = (Map<String, Object>) claims.get(i.next());
				System.out.print(values.get("name") + " = " + values.get("value") + "\t");

			}
			
			System.out.println("");*/
		}
		long presenOpenEnd = System.currentTimeMillis();
		System.out.println("Citizen Total Records : "+vcs.size());
		System.out.println("Start Time : "+presenOpenStart);
		System.out.println("End Time : "+presenOpenEnd);
		System.out.println("Time Difference: "+(presenOpenEnd-presenOpenStart));
	}

	public void execute() throws Exception {

		RestClient rc = RestClient.builder().baseUrl("http://localhost:9192").build();
		rc.setShowLog(false);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("content-type", "application/json");
		// Get server ether address
		Representation rep = rc.get("/v1/acc/list", headers);
		ArrayList<String> vcs = new ObjectMapper().readValue(rep.getBody().toString(), ArrayList.class);
		ArrayList<String> requestedAtt = new ArrayList<String>();
		requestedAtt.add("flowID");
		requestedAtt.add("quality");
		

		String walletDir = "/home/ag/Desktop/RISE/development/traceability/org.ri.se.trace.test/src/main/resources";
		String username = "citizen";
		String password = "1122334455";
		Credentials credentials = new Utils().getCredentials(walletDir, username, password);

		RequestedAttributes requestedAttributes = new RequestedAttributes();
		requestedAttributes.setRequestedAtt(requestedAtt);
		requestedAttributes.setRequester(credentials.getAddress());
		System.out.println(" Requested Values: ");
		long presenOpenStart = System.currentTimeMillis();
		for (String reqFile : vcs) {
			rep = rc.put("/v1/acc/record/" + reqFile, requestedAttributes, headers);
			System.out.println(rep);
			Map<String, Object> claims = openResponse(rep.getBody().toString());
			/*Iterator<String> i = claims.keySet().iterator();
			while (i.hasNext()) {
				Map<String, Object> values = (Map<String, Object>) claims.get(i.next());
				System.out.print(values.get("name") + " = " + values.get("value") + "\t");

			}
			
			System.out.println("");*/
		}
		long presenOpenEnd = System.currentTimeMillis();
		System.out.println("Citizen: "+vcs.size()+" "+presenOpenEnd+" - "+presenOpenStart+" = "+(presenOpenEnd-presenOpenStart));
		write("Citizen: "+vcs.size()+" "+presenOpenEnd+" - "+presenOpenStart+" = "+(presenOpenEnd-presenOpenStart));
	}
	
	public void write(String text) throws IOException {
		text=text+"\n";
		 String path = "/home/ag/Desktop/DTSDDemo/results/citizen.txt";
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
		 String path = "/home/ag/Desktop/DTSDDemo/results/waterPurificationVP.txt";
		 if(!new File(path).exists()) {
			 new File(path).createNewFile();
		 }

	        try {
	        	Files.write(Paths.get(path), title.getBytes(), StandardOpenOption.APPEND);
	            Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
	        } catch (IOException e) {
	        }
	}
	public static Map<String, Object> openResponse(String vcms) throws Exception {
		String vcStr = new String();
		if (vcms.startsWith("\"")) {
			vcStr = vcms.substring(1, vcms.length() - 1);
		} else {
			vcStr = vcms;
		}
		
		byte vcBytes[] = Base64.getDecoder().decode(vcStr.getBytes());
		vcStr = new String(vcBytes);
		System.out.println(vcStr);
		writeFile("V Presentation for Citizen" ,vcStr);
		VerifiablePresentation vp = new VerifiablePresentation(vcStr);
		if (!vp.verifyClaimsAttributes()) {
			System.err.println("Error in verfying presentation !");
			return null;
		}
		Map<String, Object> claims = vp.getClaims();
		Map<String, Object> presentationdate = new HashMap<String, Object>(); 
		presentationdate.put("name","Presentation date"); 
		presentationdate.put("value",vp.getPresentationDate());
		claims.put("Presentationdate",presentationdate);
		
		Map<String, Object> dataCollectiondate = new HashMap<String, Object>();
		dataCollectiondate.put("name","Data Collection date"); 
		dataCollectiondate.put("value",vp.getVerifiableCredentialDate());
		
		claims.put("DataCollectiondate",dataCollectiondate);
		return claims;
	}
}
