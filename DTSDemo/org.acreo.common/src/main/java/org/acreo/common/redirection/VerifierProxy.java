/*
 * package org.acreo.common.redirection;
 * 
 * import java.io.BufferedReader; import java.io.IOException; import
 * java.io.InputStream; import java.io.InputStreamReader; import java.net.URI;
 * import java.net.URISyntaxException; import java.util.Base64;
 * 
 * import javax.ws.rs.WebApplicationException; import
 * javax.ws.rs.container.ContainerRequestContext; import
 * javax.ws.rs.core.HttpHeaders; import javax.ws.rs.core.Response.Status;
 * 
 * import org.acreo.common.entities.VeidErrorMessage; import
 * org.acreo.common.exceptions.SecurityContextFormatException; import
 * org.acreo.common.exceptions.VeidblockException; import
 * org.acreo.common.utils.VeidblockLogger; import org.apache.http.Header; import
 * org.apache.http.HttpResponse; import org.apache.http.client.HttpClient;
 * import org.apache.http.client.methods.HttpPost; import
 * org.apache.http.impl.client.HttpClientBuilder; import org.slf4j.Logger;
 * import org.slf4j.LoggerFactory;
 * 
 * import com.google.common.base.Optional;
 * 
 * public class VerifierProxy {
 * 
 * private URI verifier; final static Logger logger =
 * LoggerFactory.getLogger(VerifierProxy.class); public VerifierProxy(URI
 * verifier_) { try { if (verifier_.toString().endsWith("/verify")) {
 * this.verifier = new URI(verifier_.toString()); } else { this.verifier = new
 * URI(verifier_.toString() + "/verify"); } } catch (URISyntaxException e) { try
 * { this.verifier = new URI(verifier_.toString() + "verify"); } catch
 * (URISyntaxException e1) { logger.error(VeidblockLogger.logMarker
 * +"Invalid verification [auth service URL], "+verifier_.toString()+" !");
 * logger.error(e1.getMessage()); } }
 * 
 * }
 * 
 * public Object process(ContainerRequestContext requestContext, boolean
 * isLocal) throws WebApplicationException { Optional authenticatedUser; String
 * encodedAuth; encodedAuth = extractCredentials(requestContext); if
 * (encodedAuth == null) { return encodedAuth; } String securityContextEncoded =
 * encodedAuth.toLowerCase(); if(isLocal){
 * logger.debug(VeidblockLogger.logMarker
 * +"Credentials verification local switch is ON so it will verify username and password locally !"
 * ); if (securityContextEncoded.startsWith("basic")) {
 * logger.debug(VeidblockLogger.logMarker
 * +"Authorization [Authentication] scheme is BASIC !"); return
 * parse(encodedAuth); } } return processAuthorizationHeader(encodedAuth,
 * requestContext);
 * 
 * }
 * 
 * private String[] parse(String basicHeader) throws
 * SecurityContextFormatException { logger.debug(VeidblockLogger.logMarker
 * +"Parsing credentials [Authentication Scheme = BASIC ]!"); try { String
 * encodedSecurityContext = basicHeader.substring("basic ".length()); byte
 * token[] = Base64.getDecoder().decode(encodedSecurityContext); int index = new
 * String(token).indexOf(":"); if (index == -1) { index = new
 * String(token).indexOf("|"); if (index == -1) {
 * logger.error(VeidblockLogger.logMarker
 * +"Credentials format is not according to the [Authentication Scheme = BASIC ] format !"
 * ); throw new SecurityContextFormatException("Inavliad message format !"); } }
 * byte[] uniqueId = new byte[index]; byte[] recSecretToken = new
 * byte[token.length - index - 1]; System.arraycopy(token, 0, uniqueId, 0,
 * index); index = index + 1; System.arraycopy(token, index, recSecretToken, 0,
 * token.length - index); String[] temp = new String[2]; temp[0] = new
 * String(uniqueId); temp[1] = new String(recSecretToken); return temp;
 * 
 * } catch (Exception exp) { logger.error(VeidblockLogger.logMarker
 * +"Credentials procesing error [Authentication Scheme = BASIC ] !");
 * logger.error(exp.getMessage()); throw new
 * SecurityContextFormatException(exp.getMessage()); } }
 * 
 * private String extractCredentials(ContainerRequestContext requestContext) {
 * logger.debug(VeidblockLogger.logMarker
 * +"Extracting credentials received for authentication !"); try { String
 * rawToken = requestContext.getCookies().get("auth_token").getValue(); String
 * rawUserId = requestContext.getCookies().get("auth_user").getValue();
 * logger.debug(VeidblockLogger.logMarker
 * +"Credentials extracted : Raw Token !"); return rawToken; } catch (Exception
 * e) { String aaSecurityContextEncoded =
 * requestContext.getHeaderString(HttpHeaders.AUTHORIZATION); if
 * (aaSecurityContextEncoded == null) { logger.debug(VeidblockLogger.logMarker
 * +"Extracting credentials from header maked Authorization !"); try { if
 * (isSpecialUrl(requestContext.getUriInfo().getPath(),
 * requestContext.getRequest().getMethod())) {
 * logger.debug(VeidblockLogger.logMarker
 * +"This is a special URL so it retunrs null !"); return null; }
 * aaSecurityContextEncoded = readRequestBody(requestContext); } catch
 * (IOException exp) { logger.error(VeidblockLogger.logMarker
 * +"Credentials extraction : "+exp.getMessage()); return null; // throw new //
 * WebApplicationException(Response.status(Status.UNAUTHORIZED).build()); } if
 * (aaSecurityContextEncoded == null || aaSecurityContextEncoded.equals("")) {
 * logger.error(VeidblockLogger.logMarker +"Credentials are null !"); return
 * null; // throw new //
 * WebApplicationException(Response.status(Status.UNAUTHORIZED).build()); } }
 * logger.info(VeidblockLogger.logMarker
 * +"Successfully extracted credentials !"); return aaSecurityContextEncoded; }
 * }
 * 
 * private boolean isSpecialUrl(String path, String method) {
 * 
 * String relaxUrl = "resource/user"; String relaxmethod = "POST"; if
 * (method.equalsIgnoreCase(relaxmethod) && path.equalsIgnoreCase(relaxUrl)) {
 * return true; }
 * 
 * return false; }
 * 
 * private String readRequestBody(ContainerRequestContext request) throws
 * IOException { logger.debug(VeidblockLogger.logMarker
 * +"Processing body of the message to extract credentials !"); StringBuilder
 * stringBuilder = new StringBuilder(); BufferedReader bufferedReader = null;
 * try { InputStream inputStream = request.getEntityStream(); if (inputStream !=
 * null) { bufferedReader = new BufferedReader(new
 * InputStreamReader(inputStream)); char[] readChars = new char[1024]; int
 * bytesRead = -1; while ((bytesRead = bufferedReader.read(readChars)) > 0) {
 * stringBuilder.append(readChars, 0, bytesRead); } } else {
 * stringBuilder.append(""); } } catch (IOException ex) {
 * logger.debug(VeidblockLogger.logMarker
 * +"Check with folloiwng error in the context of the type of processing !");
 * logger.debug(ex.getMessage()); throw ex; } finally { if (bufferedReader !=
 * null) { try { bufferedReader.close(); } catch (IOException ex) { throw ex; }
 * } } String body = stringBuilder.toString();
 * logger.debug(VeidblockLogger.logMarker +"Extracted body of the message !");
 * return body; }
 * 
 * private Object processAuthorizationHeader(String encodedHeader,
 * ContainerRequestContext requestContext) throws WebApplicationException {
 * 
 * logger.debug(VeidblockLogger.logMarker +"Processing Authorization header !");
 * String securityContextEncoded = encodedHeader.toLowerCase(); if
 * (securityContextEncoded.startsWith("bearer") ||
 * securityContextEncoded.startsWith("oauth") ||
 * securityContextEncoded.startsWith("oauth2")) { try {
 * logger.debug(VeidblockLogger.logMarker
 * +"Token based authentication detected !"); return parser(encodedHeader); }
 * catch (VeidblockException e) { logger.debug(VeidblockLogger.logMarker
 * +"Problems when extractig token; "+e.getMessage()+" !"); return null; }
 * 
 * } else { try {
 * 
 * // Send to the Verification Service; logger.debug(VeidblockLogger.logMarker
 * +"Sending credentials to the 'auth' service for verification !");
 * logger.debug(VeidblockLogger.logMarker
 * +"Verification URL (POST) : "+this.verifier.toString()); HttpClient
 * httpClient = HttpClientBuilder.create().build(); HttpPost request = new
 * HttpPost(this.verifier.toString());
 * request.addHeader(HttpHeaders.AUTHORIZATION, encodedHeader); HttpResponse
 * response = httpClient.execute(request); if
 * (response.getStatusLine().getStatusCode() == 200) { Header header[] =
 * response.getHeaders(HttpHeaders.AUTHORIZATION); for (Header h : header) { if
 * (h.getName().equals(HttpHeaders.AUTHORIZATION)) { try {
 * logger.debug(VeidblockLogger.logMarker
 * +"Successfully verified credentials and now parsing response !"); return
 * parser(h.getValue()); } catch (VeidblockException e) {
 * logger.error(VeidblockLogger.logMarker
 * +"Status.UNAUTHORIZED : Problems when verifying credentials !" ); return new
 * VeidErrorMessage(Status.UNAUTHORIZED.getStatusCode(),
 * "Problems when verifying credentials !"); } } }
 * logger.error(VeidblockLogger.logMarker
 * +"Status.UNAUTHORIZED : Problems when verifying credentials !" ); return new
 * VeidErrorMessage(Status.UNAUTHORIZED.getStatusCode(),
 * "Problems when processing credentials !");
 * 
 * } else if (response.getStatusLine().getStatusCode() == 401) {
 * logger.error(VeidblockLogger.logMarker
 * +"Status.UNAUTHORIZED : Invalid credentials !" ); return new
 * VeidErrorMessage(Status.UNAUTHORIZED.getStatusCode(),
 * "Invalid credentials !");
 * 
 * } else { logger.error(VeidblockLogger.logMarker
 * +"Status.UNAUTHORIZED : Problems when verifying credentials !" ); return new
 * VeidErrorMessage(Status.UNAUTHORIZED.getStatusCode(),
 * "Problems when processing credentials !"); }
 * 
 * } catch (Exception e) { logger.error(VeidblockLogger.logMarker
 * +"Status.UNAUTHORIZED : "+e.getMessage() );
 * logger.error(Status.UNAUTHORIZED.getReasonPhrase()
 * +" : Problems when processing credentials !" ); return new
 * VeidErrorMessage(Status.UNAUTHORIZED.getStatusCode(),
 * "Problems when processing credentials !"); } } }
 * 
 * private String parser(String encodedSecurityContext) throws
 * VeidblockException {
 * 
 * logger.debug(VeidblockLogger.logMarker
 * +"Parsing credentials for SSO type Authentication scheme !"); String temp =
 * encodedSecurityContext; temp = temp.toLowerCase(); String jwnTokenencoded =
 * null;
 * 
 * int headerLength = 0; if (temp.startsWith("oauth2 bearer")) { headerLength =
 * "oauth2 bearer".length(); jwnTokenencoded =
 * encodedSecurityContext.substring(headerLength,
 * encodedSecurityContext.length());
 * 
 * } else if (temp.startsWith("oauth bearer")) { headerLength =
 * "oauth bearer".length(); jwnTokenencoded =
 * encodedSecurityContext.substring(headerLength,
 * encodedSecurityContext.length());
 * 
 * } else if (temp.startsWith("oauth2")) {
 * 
 * headerLength = "oauth2".length(); jwnTokenencoded =
 * encodedSecurityContext.substring(headerLength,
 * encodedSecurityContext.length());
 * 
 * } else if (temp.startsWith("oauth")) { headerLength = "oauth".length();
 * jwnTokenencoded = encodedSecurityContext.substring(headerLength,
 * encodedSecurityContext.length());
 * 
 * } else { headerLength = "bearer".length(); jwnTokenencoded =
 * encodedSecurityContext.substring(headerLength,
 * encodedSecurityContext.length()); } jwnTokenencoded = jwnTokenencoded.trim();
 * logger.debug(VeidblockLogger.logMarker
 * +"Token extracted and returned for verification !"); return jwnTokenencoded;
 * } }
 */