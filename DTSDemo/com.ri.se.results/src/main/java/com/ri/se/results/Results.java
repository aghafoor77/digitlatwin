package com.ri.se.results;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.ri.se.citizen.CitizenPresentation;
import com.ri.se.riverflowmanagement.RiverFlowManager;
import com.ri.se.waterpurificationcompany.WaterPurificationCompany;

public class Results {

	public static void main(String[] args) throws Exception {		
		new RiverFlowManager().delete();	
		for (int i = 1; i <= 20; i++) {
			new RiverFlowManager().execute(i);
			new CitizenPresentation().execute();
			new WaterPurificationCompany().execute();
			new RiverFlowManager().delete();			
		}
	}
	
	
}
