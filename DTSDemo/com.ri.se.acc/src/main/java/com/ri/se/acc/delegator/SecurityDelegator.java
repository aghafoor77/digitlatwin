package com.ri.se.acc.delegator;
/*
 * package schema.mvc.ri.se.delegator;
 * 
 * import java.security.PrivateKey; import java.security.PublicKey; import
 * java.util.Base64; import java.util.Objects;
 * 
 * import org.acreo.security.crypto.CryptoPolicy; import
 * org.acreo.security.crypto.Encryption; import
 * org.acreo.security.crypto.SecurityProperties; import
 * org.acreo.security.exceptions.VeidblockException; import
 * org.acreo.security.utils.PEMStream; import org.acreo.security.utils.SGen;
 * import org.apache.commons.logging.Log; import
 * org.apache.commons.logging.LogFactory;
 * 
 * import schema.mvc.ri.se.credentials.UserRSACredentials; import
 * schema.mvc.ri.se.utils.MVCKeyPair; import schema.mvc.ri.se.utils.Util;
 * 
 * 
 * public class SecurityDelegator {
 * 
 * Log logger = LogFactory.getLog(SecurityDelegator.class); String marker =
 * "\t===> : ";
 * 
 * public UserRSACredentials registerRSACredentials(String owner, String
 * ownerPassowrd, String userEmail, String userPassword) throws Exception { Util
 * util = new Util(); logger.info(marker + userEmail +
 * " : Create user's RSA credentials !"); try { if
 * (!util.createRSACredentials(userEmail, userPassword)) { throw new
 * Exception(userEmail + " : Problems when creating RSA Credentails ! "); } //
 * Fetching users stored RSA credentials logger.info(marker + userEmail +
 * " : Fetching user's RSA credentials !"); MVCKeyPair cred =
 * util.fetechRSACredentials(userEmail, userPassword); if (Objects.isNull(cred))
 * { throw new Exception(userEmail +
 * " : Problems when fetching RSA Credentails ! "); } CryptoPolicy cryptoPolicy
 * = new CryptoPolicy(); Encryption enc = new Encryption(cryptoPolicy);
 * SecurityProperties securityProperties = new SecurityProperties(cryptoPolicy);
 * 
 * logger.info(marker + userEmail +
 * " : Generating random symmetric key to protect RSA credentials !"); //
 * Generating symmetric key byte protectionKey[] =
 * securityProperties.generateSymmetricKey(new SGen().nextHexString(16));
 * 
 * logger.info(marker + userEmail + " : Protecting user's RSA credentials !");
 * 
 * // Generating private key with symmetric key byte[] privateKey64 =
 * Base64.getEncoder().encode(cred.getPrivateKey().getEncoded());
 * 
 * String encryptedKey = new
 * String(Base64.getEncoder().encode(enc.encryptRaw(protectionKey,
 * privateKey64))); // Encode random symmetric key into BASE64 byte[]
 * protectionKey64 = Base64.getEncoder().encode(protectionKey); // Generating
 * symmetric key with owner password String encKeyOwner = new
 * String(Base64.getEncoder()
 * .encode(enc.encryptRaw(securityProperties.getNewProtectedKey(ownerPassowrd),
 * protectionKey64)));
 * 
 * // Generating symmetric key with userpassword String encKeyUser = new
 * String(Base64.getEncoder()
 * .encode(enc.encryptRaw(securityProperties.getNewProtectedKey(userPassword),
 * protectionKey64)));
 * 
 * UserRSACredentials userRSACredentials = new UserRSACredentials();
 * userRSACredentials.setEmail(userEmail);
 * userRSACredentials.setOwnerEmail(owner);
 * userRSACredentials.setOwnerEncKey(encKeyOwner);
 * userRSACredentials.setPri(encryptedKey);
 * userRSACredentials.setPub(Base64.getEncoder().encodeToString(cred.
 * getPublicKey().getEncoded())); userRSACredentials.setUserEncKey(encKeyUser);
 * 
 * logger.info(marker + userEmail +
 * " : Successfully protected user's RSA credentials !");
 * 
 * return userRSACredentials;
 * 
 * } catch (VeidblockException e) {
 * 
 * logger.error(marker + userEmail +
 * " : Problems when protecting user's RSA credentials !");
 * 
 * logger.error(marker + userEmail + " : " + e.getMessage());
 * logger.error(marker + userEmail + " : " + e);
 * 
 * logger.info(marker + userEmail +
 * " : Due to failure, user's RSA credentials will be removed !");
 * 
 * util.deleteLocalRSACredentials(userEmail, userPassword); throw new
 * Exception(e); } }
 * 
 * public PrivateKey extractUserPrivateKey(String password, UserRSACredentials
 * userRSACredentials) throws Exception {
 * 
 * try { logger.info(marker + userRSACredentials.getEmail() +
 * " : Extracting user's RSA private credentials !");
 * 
 * String userKey = userRSACredentials.getUserEncKey();
 * 
 * byte[] decodeduserKey = Base64.getDecoder().decode(userKey); CryptoPolicy
 * cryptoPolicy = new CryptoPolicy(); Encryption enc = new
 * Encryption(cryptoPolicy); SecurityProperties securityProperties = new
 * SecurityProperties(cryptoPolicy); logger.info(marker +
 * userRSACredentials.getEmail() +
 * " : Extracting user's RSA private credentials master key !");
 * 
 * byte encKeyUser[] = Base64.getDecoder()
 * .decode(enc.decryptRaw(securityProperties.getNewProtectedKey(password),
 * decodeduserKey));
 * 
 * byte[] pri = Base64.getDecoder().decode(userRSACredentials.getPri());
 * 
 * logger.info(marker + userRSACredentials.getEmail() +
 * " : Using master key to extract user's RSA private credentials!");
 * 
 * byte decryptedPri64[] = enc.decryptRaw(encKeyUser, pri);
 * 
 * byte[] decryptedPri = Base64.getDecoder().decode(decryptedPri64);
 * 
 * return new PEMStream().getPrivateKey(decryptedPri); } catch (Exception exp) {
 * logger.error(marker + userRSACredentials.getEmail() +
 * " : Problems when extracting user's RSA private credentials!");
 * logger.error(marker + userRSACredentials.getEmail() + " : " +
 * exp.getMessage()); logger.error(marker + userRSACredentials.getEmail() +
 * " : " + exp); throw new Exception(exp); } }
 * 
 * public PublicKey extractPublicKey(UserRSACredentials userRSACredentials)
 * throws Exception { String pubB64 = userRSACredentials.getPub(); return new
 * PEMStream().fromBase64StringToPublicKey(pubB64); } }
 */