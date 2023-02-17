package com.ri.se.acc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/***
 * 
 * @author Abdul Ghafoor, abdul.ghafoor@ri.se
 * @category Application
 * @version 1.0.0,
 * @apiNote Microcredential Microservice Development
 * @apiNote The entry point of the microservice and main call to execute user
 *          service.
 *
 */
@SpringBootApplication
public class ACCApplication {

	static Log logger = LogFactory.getLog(ACCApplication.class);
	static String marker = "\t===> : ";
	
	public static String owner= null;
	public static String pass= null;
	public static void main(String[] args) {
		/*
		 * try { new MicrocredentialDemo().presentationDemos(); } catch (Exception e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		// ===================================
		SpringApplication application = new SpringApplication(ACCApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);		
	}	
}
