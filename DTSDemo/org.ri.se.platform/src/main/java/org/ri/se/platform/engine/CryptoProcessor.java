package org.ri.se.platform.engine;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
import org.ri.se.platform.datamodel.GDSOHeader;
import org.ri.se.platform.datamodel.GenericDataSharingObject;
import org.ri.se.platform.datamodel.SecurityContext;


/**
 * 
 * @author Abdul Ghafoor, abdul.ghafoor@ri.se
 * @implNote : This class implementa various functions required for protection
 *           (encryption and decryption) of data.
 *
 */
public class CryptoProcessor implements ICryptoProcessor {

	

	/**
	 * This method encrypts data using the symmetric key and then encodes in
	 * specified encoding format.
	 * 
	 * @param key:        symmetric key in bytes
	 * @param GDSOHeader: Used to identify the security context and encoding scheme.
	 * @param data:       Actual data which is being encrypted.
	 * @return byte[] return encrypted data.
	 * 
	 */
	public byte[] encrypt(byte[] key, GDSOHeader gdsoHeader, byte[] data) throws Exception {
		CryptoProcessorDelegator cryptoProcessorDelegator = new CryptoProcessorDelegator();
		try {
			byte output[] = cryptoProcessorDelegator.encrypt(key, gdsoHeader.getEncoding(),
					gdsoHeader.getSecurityContext(), data);
			return output;
		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	/**
	 * This method encrypts data using the symmetric key and then encodes in
	 * specified encoding format defined in the GenericDataSharingObject;s header
	 * field.
	 * 
	 * @param key:                      symmetric key in bytes
	 * @param GenericDataSharingObject: Used to get GDSOHeader to identify the
	 *                                  security context and encoding scheme.
	 * @param data:                     Actual data which is being encrypted.
	 * @return byte[] return encrypted data.
	 * 
	 */
	public byte[] encrypt(byte[] key, GenericDataSharingObject genericDataSharingObject) throws Exception {
		CryptoProcessorDelegator cryptoProcessorDelegator = new CryptoProcessorDelegator();
		try {
			byte output[] = cryptoProcessorDelegator.encrypt(key,
					genericDataSharingObject.getGdsoHeader().getEncoding(),
					genericDataSharingObject.getGdsoHeader().getSecurityContext(),
					genericDataSharingObject.getPacket().getData().getBytes());
			return output;
		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	/**
	 * This method decodes and then decrypts data using symmetric key. The method
	 * extracts encrypted data and encoding scheme from GenericDataSharingObject;s
	 * header field.
	 * 
	 * @param key:                      symmetric key in bytes
	 * @param GenericDataSharingObject: Used to get GDSOHeader to identify the
	 *                                  security context and encoding scheme and
	 *                                  also provides access to data.
	 * @return byte[] return decrypted data.
	 * 
	 */
	public byte[] decrypt(byte[] key, GenericDataSharingObject genericDataSharingObject) throws Exception {
		CryptoProcessorDelegator cryptoProcessorDelegator = new CryptoProcessorDelegator();
		System.out.println("Decrypting contents using symmetric key . . . ");
		try {
			byte output[] = cryptoProcessorDelegator.decrypt(key,
					genericDataSharingObject.getGdsoHeader().getEncoding(),
					genericDataSharingObject.getGdsoHeader().getSecurityContext(),
					genericDataSharingObject.getPacket().getData().getBytes());
			return output;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * This method generates symmetric key based on the information provided in the
	 * SecurityContext.
	 * 
	 * @param SecurityContext: Used to identify symmetric key algorithm
	 * @return byte[] return symmetric key in bytes.
	 * 
	 */
	public byte[] generateSymmetrickey(SecurityContext securityContext) throws Exception {
		return new CryptoProcessorDelegator().generateSymmetrickey(securityContext);
	}

	/**
	 * This method generates key pair .
	 * 
	 * @param size: size of the asymmetric key
	 * @return byte[] returns keypair.
	 * 
	 */
	public KeyPair generateRSAKeyPair(int keySize) throws Exception {
		return new CryptoProcessorDelegator().generateRSAKeyPair(keySize);
	}

	/**
	 * This method encrypts data stored in GenericDataSharingObject and then encodes
	 * according to the information specified in the GenericDataSharingObject header
	 * 
	 * @param PublicKey:                Used to encrypt data stored in
	 *                                  GenericDataSharingObject
	 * @param GenericDataSharingObject: provides attributes required to encrypt and
	 *                                  encoded data store in data attribute.
	 * @return byte[] returns encrypted data in bytes.
	 * 
	 */
	public byte[] encrypt(PublicKey publicKey, GenericDataSharingObject genericDataSharingObject) throws Exception {
		return new CryptoProcessorDelegator().encrypt(publicKey, genericDataSharingObject.getGdsoHeader().getEncoding(),
				genericDataSharingObject.getGdsoHeader().getSecurityContext(),
				genericDataSharingObject.getPacket().getData().getBytes());
	}

	/**
	 * This method decodes data and then decrypts data using private key stored in
	 * GenericDataSharingObject.
	 * 
	 * @param PrivateKey:               Used to decrypt data stored in
	 *                                  GenericDataSharingObject
	 * @param GenericDataSharingObject: provides attributes required to decrypt and
	 *                                  encoded data store in data attribute.
	 * @return byte[] returns decrypted data in bytes.
	 * 
	 */
	public byte[] decrypt(PrivateKey privateKey, GenericDataSharingObject genericDataSharingObject) throws Exception {
		return new CryptoProcessorDelegator().decrypt(privateKey,
				genericDataSharingObject.getGdsoHeader().getEncoding(),
				genericDataSharingObject.getGdsoHeader().getSecurityContext(),
				genericDataSharingObject.getPacket().getData().getBytes());
	}

	/**
	 * This method encrypts data stored in GenericDataSharingObject using private
	 * key and then encodes according to the information specified in the
	 * GenericDataSharingObject header
	 * 
	 * @param PrivateKey:               Used to encrypt data stored in
	 *                                  GenericDataSharingObject
	 * @param GenericDataSharingObject: provides attributes required to encrypt and
	 *                                  encoded data store in data attribute.
	 * @return byte[] returns encrypted data in bytes.
	 * 
	 */
	public byte[] encrypt(PrivateKey privateKey, GenericDataSharingObject genericDataSharingObject) throws Exception {
		return new CryptoProcessorDelegator().encrypt(privateKey,
				genericDataSharingObject.getGdsoHeader().getEncoding(),
				genericDataSharingObject.getGdsoHeader().getSecurityContext(),
				genericDataSharingObject.getPacket().getData().getBytes());
	}

	/**
	 * This method decodes data and then decrypts data using private key stored in
	 * GenericDataSharingObject.
	 * 
	 * @param PublicKey:                Used to decrypt data stored in
	 *                                  GenericDataSharingObject
	 * @param GenericDataSharingObject: provides attributes required to decrypt and
	 *                                  encoded data store in data attribute.
	 * @return byte[] returns decrypted data in bytes.
	 * 
	 */
	public byte[] decrypt(PublicKey publicKey, GenericDataSharingObject genericDataSharingObject) throws Exception {
		return new CryptoProcessorDelegator().decrypt(publicKey, genericDataSharingObject.getGdsoHeader().getEncoding(),
				genericDataSharingObject.getGdsoHeader().getSecurityContext(),
				genericDataSharingObject.getPacket().getData().getBytes());
	}

	/**
	 * 
	 * @param publicKey : public key used for encryption
	 * @param input     : input data for encryption
	 * @param encoding  : used to define encoding scheme
	 * @return : encrypted data in bytes array
	 * @throws Exception
	 */
	public byte[] encrypt(PublicKey publicKey, byte[] input, ENCODING_DECODING_SCHEME encoding) throws Exception {
		return new CryptoProcessorDelegator().encrypt(publicKey, encoding, null, input);
	}

	/**
	 * 
	 * @param PrivateKey : PrivateKey used for decryption
	 * @param input      : input data for decryption
	 * @param encoding   : used to define encoding scheme
	 * @return : encrypted data in bytes array
	 * @throws Exception
	 */
	public byte[] decrypt(PrivateKey privateKey, byte[] input, ENCODING_DECODING_SCHEME encoding) throws Exception {
		return new CryptoProcessorDelegator().decrypt(privateKey, encoding, null, input);
	}

	/**
	 * 
	 * @param PrivateKey : PrivateKey used for encryption
	 * @param input      : input data for encryption
	 * @param encoding   : used to define encoding scheme
	 * @return : encrypted data in bytes array
	 * @throws Exception
	 */
	public byte[] encrypt(PrivateKey privateKey, byte[] input, ENCODING_DECODING_SCHEME encoding) throws Exception {
		return new CryptoProcessorDelegator().encrypt(privateKey, encoding, null, input);
	}

	/**
	 * 
	 * @param PublicKey : PublicKey used for decryption
	 * @param input     : input data for decryption
	 * @param encoding  : used to define encoding scheme
	 * @return : encrypted data in bytes array
	 * @throws Exception
	 */
	public byte[] decrypt(PublicKey publicKey, byte[] input, ENCODING_DECODING_SCHEME encoding) throws Exception {
		return new CryptoProcessorDelegator().decrypt(publicKey, encoding, null, input);
	}

	public byte[] digest(byte data[]) throws Exception {
		return new CryptoProcessorDelegator().digest(data);
	}

	public boolean verify(byte[] data, byte[] digest) throws Exception {
		return new CryptoProcessorDelegator().verify(data, digest);
	}

	public byte[] encrypt(byte[] key, ENCODING_DECODING_SCHEME encoding, SecurityContext securityContext, byte data[])
			throws Exception {
		CryptoProcessorDelegator cryptoProcessorDelegator = new CryptoProcessorDelegator();
		try {
			byte output[] = cryptoProcessorDelegator.encrypt(key, encoding, securityContext, data);
			return output;
		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	public byte[] decrypt(byte[] key, ENCODING_DECODING_SCHEME encoding, SecurityContext securityContext, byte data[])
			throws Exception {
		CryptoProcessorDelegator cryptoProcessorDelegator = new CryptoProcessorDelegator();
		try {
			byte output[] = cryptoProcessorDelegator.decrypt(key, encoding, securityContext, data);
			return output;
		} catch (Exception e) {
			throw new Exception(e);
		}

	}
}