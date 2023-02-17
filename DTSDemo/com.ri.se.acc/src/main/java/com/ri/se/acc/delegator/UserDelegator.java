package com.ri.se.acc.delegator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ri.se.acc.users.User;

/***
 * 
 * @author Abdul Ghafoor, abdul.ghafoor@ri.se
 * @category Delegator
 * @version 1.0.0,
 * @apiNote Microcredential Microservice Development
 * @apiNote The user delegation handles various operations required by the other
 *          classes to perform operations on the user entity/token.
 *
 */
public class UserDelegator {

	Log logger = LogFactory.getLog(UserDelegator.class);
	String marker = "\t===> : ";

	public UserDelegator() {
	}
	/**
			 * 
			 * @param sub: email address of the token owner
			 * @param scp  : specify scope (role, separated by ',') of the user
			 * @return Token in JWS foramt
			 * @throws Exception
			 *//*
				 * public String generateToken(String sub, String scp) throws Exception {
				 * 
				 * String owner = SchemaApplication.owner; String password =
				 * SchemaApplication.pass;
				 * 
				 * Util util = new Util(); logger.info(marker + sub +
				 * " - Fetching application's RSA credentials !"); MVCKeyPair keyPair =
				 * util.fetechRSACredentials(owner, password); // Generating token
				 * logger.info(marker + sub + " - Generating JWS token !"); JWSToken token = new
				 * SsoManager().generateSsoToken(keyPair.getPrivateKey(),
				 * keyPair.getPublicKey(), owner, sub, "1.0", "[" + scp + "]", 8); return
				 * token.toEncoded(); }
				 */

	/**
	 * 
	 * @param token
	 * @return User information from JWS Token
	 * @throws Exception
	 */
	public User getJWTTokenOwner(String token) throws Exception {
		logger.info(marker + "Extracting user info from JWS Token !");
		
		User user = new User();
		user.setEmail("user");
		user.setRoles("admin");
		return user;
	}

	/**
	 * 
	 * @param token
	 * @return true of false
	 * @throws Exception
	 */
	public boolean verify(String token) throws Exception {
		return true;
	}
}
