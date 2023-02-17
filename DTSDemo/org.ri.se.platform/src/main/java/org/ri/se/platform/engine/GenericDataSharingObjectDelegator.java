package org.ri.se.platform.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;


import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
import org.acreo.security.utils.PEMStream;
import org.apache.commons.codec.binary.Hex;
import org.ri.se.ipfsj.v2.DocumentManager;
import org.ri.se.ipfsj.v2.IPFSFileDescriptor;
import org.ri.se.platform.datamodel.AccessLevel;
import org.ri.se.platform.datamodel.DataStorageType;
import org.ri.se.platform.datamodel.DataType;
import org.ri.se.platform.datamodel.GDSOHeader;
import org.ri.se.platform.datamodel.GenericDataSharingObject;
import org.ri.se.platform.datamodel.Packet;
import org.ri.se.platform.datamodel.PublicCredentials;
import org.ri.se.platform.datamodel.PublicCredentialsList;
import org.ri.se.platform.datamodel.SecurityContext;

public class GenericDataSharingObjectDelegator {

	
	private String ipfsURL = null;

	public GenericDataSharingObjectDelegator(String ipfsURL) {
		this.ipfsURL = ipfsURL;
	}

	private byte[] extractSharedCredentials(PrivateKey privateKey, GenericDataSharingObject genericDataSharingObject,
			String address) throws Exception {
		CryptoProcessor cryptoProcessor = new CryptoProcessor();
		System.out.println("Extracting symmetric key ... ");
		
		String encKey = genericDataSharingObject.getPacket().get(address);
		System.out.println("Encrypted symmetric key : "+encKey);
		if (Objects.isNull(encKey)) {
			throw new Exception("You are not authorized to open the resource !");
		}
		
		System.out.println("Decrypting symmetric key using private key !");
		
		byte[] symKey = cryptoProcessor.decrypt(privateKey, encKey.getBytes(),
				genericDataSharingObject.getGdsoHeader().getEncoding());
		
		if (Objects.isNull(symKey)) {
			throw new Exception("You are not authorized to open the resource. Shared secret is not valid !");
		}
		System.out.println("Successfully decrypted symmetric key and its size is : "+symKey.length);
		return symKey;

	}

	public byte[] openGenericDataSharingObject(PrivateKey privateKey, String address,
			GenericDataSharingObject genericDataSharingObject) throws Exception {
		byte out[];
		System.out.println("Checking data storage type . . . ");
		switch (genericDataSharingObject.getGdsoHeader().getDataStorageType()) {
		case INLINE:
			if (!SecurityContext.NONE.value()
					.equals(genericDataSharingObject.getGdsoHeader().getSecurityContext().value())) {
				CryptoProcessor cryptoProcessor = new CryptoProcessor();
				byte[] symKey = extractSharedCredentials(privateKey, genericDataSharingObject, address);
				out = cryptoProcessor.decrypt(symKey, genericDataSharingObject);
			} else {
				out = performDecoding(genericDataSharingObject.getPacket().getData().getBytes(),
						genericDataSharingObject.getGdsoHeader().getEncoding());
			}
			break;
		case FTP:
			System.err.println("AG ToDo : Download from FTP and then store in local befor edecrypting ");
			File file = new File(genericDataSharingObject.getPacket().getData());
			Path path = Paths.get(file.getAbsolutePath());
			genericDataSharingObject.getPacket().setData(new String(Files.readAllBytes(path)));
			if (!SecurityContext.NONE.value()
					.equals(genericDataSharingObject.getGdsoHeader().getSecurityContext().value())) {
				CryptoProcessor cryptoProcessor = new CryptoProcessor();
				byte[] symKey = extractSharedCredentials(privateKey, genericDataSharingObject, address);
				out = cryptoProcessor.decrypt(symKey, genericDataSharingObject);
			} else {
				out = performDecoding(genericDataSharingObject.getPacket().getData().getBytes(),
						genericDataSharingObject.getGdsoHeader().getEncoding());
			}
			break;
		case IPFS:
			System.out.println("IPFS data storage type was used !");
			String packet = genericDataSharingObject.getPacket().getData();
			System.out.println("Data packet extracted : "+packet);
			if (!packet.contains("|")) {
				throw new Exception("Invalid data in packet [| operator is missing]!");
			}
			String packets[] = packet.split("\\|");
			System.out.println("IPFS URL : "+ipfsURL);
			File ipfsDir = new File("ipfsdownloads");
			if(!ipfsDir.exists())
				ipfsDir.mkdir(); 
			
			DocumentManager documentManager = new DocumentManager(ipfsURL);
			File ipfsFile = new File(ipfsDir.getAbsolutePath()+File.separator+packets[0]);
			
			FileOutputStream fos = new FileOutputStream(ipfsFile);
			boolean isDownloaded = documentManager.download(packets[1],
					new FileOutputStream(ipfsFile.getAbsolutePath()));
			if (!isDownloaded) {
				throw new Exception("Problems when downlading file from IPFS [" + packets[1] + "]!");
			}
			
			System.out.println("Downloaded and saved at : " + ipfsFile.getAbsolutePath());
			
			Path pathIPFSFile = Paths.get(ipfsFile.getAbsolutePath());
			GenericDataSharingObject genericDataSharingObjectCpy = new GenericDataSharingObject();
			genericDataSharingObjectCpy.setGdsoHeader(genericDataSharingObject.getGdsoHeader());
			Packet packetCpy = new Packet();
			packetCpy.setData(new String(Files.readAllBytes(pathIPFSFile)));
			System.out.println("Loaded contents of stored file in generic data sharing object !");
			packetCpy.setPartners(genericDataSharingObject.getPacket().getPartners());
			genericDataSharingObjectCpy.setPacket(packetCpy);
			System.out.println("Deleting downloaded file  '" + ipfsFile.getAbsolutePath()+"'");
			ipfsFile.delete();
			
			if (!SecurityContext.NONE.value()
					.equals(genericDataSharingObjectCpy.getGdsoHeader().getSecurityContext().value())) {
				System.out.println("Security on contetns applied !");
				System.out.println("Initializing crypto processor to extract encrypted contents !");
				CryptoProcessor cryptoProcessor = new CryptoProcessor();
				
				byte[] symKey = extractSharedCredentials(privateKey, genericDataSharingObjectCpy, address);
				out = cryptoProcessor.decrypt(symKey, genericDataSharingObjectCpy);
			} else {
				out = performDecoding(genericDataSharingObjectCpy.getPacket().getData().getBytes(),
						genericDataSharingObjectCpy.getGdsoHeader().getEncoding());

			}
			break;
		default:
			throw new Exception("Data Storage not defined !");
		}
		return out;

	}

	public GenericDataSharingObject addPartnerGenericDataSharingObject(KeyPair myKeyPair, String myLedgerAdderss,
			PublicCredentials partner, GenericDataSharingObject genericDataSharingObject) throws Exception {

		CryptoProcessor cryptoProcessor = new CryptoProcessor();

		PEMStream pemStream = new PEMStream();
		String encKey = genericDataSharingObject.getPacket().get(myLedgerAdderss);
		if (Objects.isNull(encKey)) {
			throw new Exception("You are not authorized to open the resource !");
		}
		byte[] symKey = cryptoProcessor.decrypt(myKeyPair.getPrivate(), encKey.getBytes(),
				genericDataSharingObject.getGdsoHeader().getEncoding());
		if (Objects.isNull(symKey)) {
			throw new Exception("You are not authorized to open the resource. Shared secret is not valid !");
		}

		if (!Objects.isNull(partner)) {
			byte[] encSK = cryptoProcessor.encrypt(partner.getPublicKey(), symKey,
					genericDataSharingObject.getGdsoHeader().getEncoding());
			genericDataSharingObject.getPacket().add(partner.getLedgerAddress(), new String(encSK));
		}
		return genericDataSharingObject;
	}

	public GenericDataSharingObject addPartnerGenericDataSharingObject(KeyPair myKeyPair, String myLedgerAdderss,
			PublicCredentialsList partners, GenericDataSharingObject genericDataSharingObject) throws Exception {

		CryptoProcessor cryptoProcessor = new CryptoProcessor();

		PEMStream pemStream = new PEMStream();
		String encKey = genericDataSharingObject.getPacket().get(myLedgerAdderss);
		if (Objects.isNull(encKey)) {
			throw new Exception("You are not authorized to open the resource !");
		}
		byte[] symKey = cryptoProcessor.decrypt(myKeyPair.getPrivate(), encKey.getBytes(),
				genericDataSharingObject.getGdsoHeader().getEncoding());
		if (Objects.isNull(symKey)) {
			throw new Exception("You are not authorized to open the resource. Shared secret is not valid !");
		}
		for (PublicCredentials partner : partners) {
			if (!Objects.isNull(partner)) {
				byte[] encSK = cryptoProcessor.encrypt(partner.getPublicKey(), symKey,
						genericDataSharingObject.getGdsoHeader().getEncoding());
				genericDataSharingObject.getPacket().add(partner.getLedgerAddress(), new String(encSK));
			}
		}
		return genericDataSharingObject;
	}
	
	public GenericDataSharingObject create(PublicCredentialsList publicCredentialsList, AccessLevel accessLevel,
			DataType dt, DataStorageType dst, SecurityContext securityContext, ENCODING_DECODING_SCHEME encoding,
			byte[] data) throws Exception {
		GenericDataSharingObject genericDataSharingObject = new GenericDataSharingObject();
		GDSOHeader gdsoHeader = new GDSOHeader();
		gdsoHeader.setAccessLevel(accessLevel);
		gdsoHeader.setDataStorageType(dst);
		gdsoHeader.setDataType(dt);
		gdsoHeader.setSecurityContext(securityContext);
		gdsoHeader.setEncoding(encoding);
		genericDataSharingObject.setGdsoHeader(gdsoHeader);
		
		//System.out.println("Generic header at sender side : \n"+gdsoHeader.serialize());

		byte[] out = null;
		Packet packet = new Packet();
		System.out.println("Checking security context . . . ");
		if (!SecurityContext.NONE.value().equals(securityContext.value())) {
			System.out.println("Applying security context !");
			System.out.println("Intializing crypto processor !");
			CryptoProcessor cryptoProcessor = new CryptoProcessor();
			System.out.println("Generating symmetric key  . . . ");
			byte[] key = cryptoProcessor
					.generateSymmetrickey(genericDataSharingObject.getGdsoHeader().getSecurityContext());
			System.out.println("Symmetric key  generated and size id "+key.length);
			System.out.println("Encrypting data . . .  ");
			out = cryptoProcessor.encrypt(key, gdsoHeader, data);
			System.out.println("Encrypting data lenght : "+out.length);
			System.out.println("Encrypting symmetric key with the public key of the share accounts . . . ");
			for (PublicCredentials publicCredentials : publicCredentialsList) {
				System.out.println("Encrypting symmetric key for : "+publicCredentials.getLedgerAddress());
				byte[] encryptedKey = cryptoProcessor.encrypt(publicCredentials.getPublicKey(), key,
						gdsoHeader.getEncoding());
				packet.add(publicCredentials.getLedgerAddress(), new String(encryptedKey));
			}
		} else {
			System.out.println("Not security context found so only applying encoding !");
			out = performEncoding(data, encoding);
			System.out.println("Successfully encoded data !");
		}

		System.out.println("Applying data storage type !");
		switch (dst) {
		case INLINE:
			System.out.println("Data storage type is ININE  so setting in the data packet attribute !");
			packet.setData(new String(out));
			break;
		case FTP:
			System.out.println("Data storage type is FTP so uploading on the FTP !");
			// Upload on IPFS and then store link here
			System.out.println("Storing file on FTP Server !");
			String parent = "/home/ag/Desktop/RISE/development/traceability/org.ri.se.platform/res/";
			ExternalStorage externalStorage = new ExternalStorage(parent);
			String link = externalStorage.storeLocalFile(out);
			packet.setData(link);
			break;

		case IPFS:
			
			System.out.println("Data storage type is IPFS so uploading on IPFS ("+ipfsURL+")");
			DocumentManager documentManager = new DocumentManager(ipfsURL);
			/*
			 * IPFSFileDescriptor descriptor1 = documentManager.upload(dt.getDataType() +
			 * new Random().nextLong(), generateMetadata().getBytes());
			 * System.out.println(descriptor1.getHashBase58());
			 */
			
			IPFSFileDescriptor descriptor = documentManager.upload(dt.getDataType() + new Random().nextLong(), out);
			System.out.println("Successfully uploaded on IPFS ("+descriptor.getHashBase58()+")");
			packet.setData(descriptor.getName() + "|" + descriptor.getHashBase58());
			System.out.println("Data in packet attribute is "+packet.getData());
			break;
		default:
			throw new Exception("Data Storage not defined !");
		}

		genericDataSharingObject.setPacket(packet);
		return genericDataSharingObject;
	}

	private byte[] performEncoding(byte[] data, ENCODING_DECODING_SCHEME encoding) {
		switch (encoding) {
		case BASE64:
			return Base64.getEncoder().encode(data);
		case HEX:
			return new String(Hex.encodeHex(data)).getBytes();
		case NONE:
			return data;

		}
		return data;
	}

	private byte[] performDecoding(byte[] data, ENCODING_DECODING_SCHEME encoding) throws Exception {
		switch (encoding) {
		case BASE64:
			return Base64.getDecoder().decode(data);
		case HEX:
			return Hex.decodeHex(new String(data).toCharArray());
		case NONE:
			return data;
		}
		return data;
	}

	public GenericDataSharingObject create(PublicCredentials publicCredentials, AccessLevel accessLevel, DataType dt,
			DataStorageType dst, SecurityContext securityContext, ENCODING_DECODING_SCHEME encoding, byte[] data)
			throws Exception {
		GenericDataSharingObject genericDataSharingObject = new GenericDataSharingObject();
		GDSOHeader gdsoHeader = new GDSOHeader();
		gdsoHeader.setAccessLevel(accessLevel);
		gdsoHeader.setDataStorageType(dst);
		gdsoHeader.setDataType(dt);
		gdsoHeader.setSecurityContext(securityContext);
		gdsoHeader.setEncoding(encoding);
		genericDataSharingObject.setGdsoHeader(gdsoHeader);
		
		System.out.println("Generic header at sender side : \n"+gdsoHeader.serialize());
		
		Packet packet = new Packet();
		byte[] out = null;
		System.out.println("Checking security context . . . ");
		if (!SecurityContext.NONE.value().equals(securityContext.value())) {
			System.out.println("Applying security context !");
			System.out.println("Intializing crypto processor !");
			CryptoProcessor cryptoProcessor = new CryptoProcessor();
			System.out.println("Generating symmetric key  . . . ");
			byte[] key = cryptoProcessor
					.generateSymmetrickey(genericDataSharingObject.getGdsoHeader().getSecurityContext());
			System.out.println("Encrypting data . . .  ");
			out = cryptoProcessor.encrypt(key, gdsoHeader, data);
			System.out.println("Encrypting data lenght : "+out.length);
			System.out.println("Encrypting symmetric key for : "+publicCredentials.getLedgerAddress());
			byte[] encryptedKey = cryptoProcessor.encrypt(publicCredentials.getPublicKey(), key,
					gdsoHeader.getEncoding());
			System.out.println("Successfully encrypting symmetric key with the public key of address : "+publicCredentials.getLedgerAddress());
			packet.add(publicCredentials.getLedgerAddress(), new String(encryptedKey));
		} else {

			System.out.println("Not security context found so only applying encoding !");
			out = performEncoding(data, encoding);
			System.out.println("Successfully encoded data !");
		}
		
		System.out.println("Applying data storage type !");
		switch (dst) {
		case INLINE:
			System.out.println("Data storage type is ININE  so setting in the data packet attribute !");
			packet.setData(new String(out));
			break;
		case FTP:
			System.out.println("Data storage type is FTP so uploading on the FTP !");
			System.out.println("Storing file on FTP Server !");
			String parent = "/home/ag/Desktop/RISE/development/traceability/org.ri.se.platform/res/";
			ExternalStorage externalStorage = new ExternalStorage(parent);
			String link = externalStorage.storeLocalFile(out);
			packet.setData(link);
			break;

		case IPFS:
			
			System.out.println("Data storage type is IPFS so uploading on IPFS ("+ipfsURL+")");
			DocumentManager documentManager = new DocumentManager(ipfsURL);
			IPFSFileDescriptor descriptor = documentManager.upload(dt.getDataType() + new Random().nextLong(), out);
			System.out.println("Successfully uploaded on IPFS ("+descriptor.getHashBase58()+")");
			packet.setData(descriptor.getName() + "|" + descriptor.getHashBase58());
			System.out.println("Data in packet attribute is "+packet.getData());
			break;
		default:
			throw new Exception("Data Storage not defined !");
		}


		genericDataSharingObject.setPacket(packet);
		return genericDataSharingObject;

	}

}
