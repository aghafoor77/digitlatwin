package com.ri.se.vc.digitaltwin.bc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ManagePrivateKeys {

	private ArrayList<String> privateKeys = new ArrayList<String>();
	
	private void process() {
		String rawKeys = null;
		try {
			rawKeys = new String(Files.readAllBytes(Paths.get("keys.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}		
		String keysArray[]= rawKeys .split("\n");
		for(int i=0; i< keysArray.length; i++) {
			String cleanKey = keysArray[i].replace("("+i+") ", "").trim();
			privateKeys.add(cleanKey);
		}
	}
	public ArrayList<String> getPrivateKeys() {
		privateKeys = new ArrayList<String>();
		process();
		return privateKeys;
	}
	
	public static void main(String arg[]) {
		File fi = new File("ab.txt");
		System.out.println(fi.getAbsolutePath() );
		new ManagePrivateKeys().process();
		
	}
	
}
