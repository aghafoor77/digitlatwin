package com.ri.se.vc.digitaltwin.bc;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.LogManager;

import org.acreo.security.bc.CertificateHandlingBC;
import org.acreo.security.certificate.CertificateSuite;
import org.acreo.security.utils.DistinguishName;
import org.acreo.security.utils.PEMStream;
import org.acreo.security.utils.StoreHandling;
import org.ri.se.vt.blockchain.Web3JConnector;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Numeric;

import com.ri.se.dap.VeidblockManager;

public class VDRManager {

	public static String prvt[] = new String[5];

	public static String prvtSh[] = new String[5];
	public static String usernames[] = new String[4];

	public static String walletDir = "/home/ag/Desktop/RISE/development/traceability/org.ri.se.trace.test/src/main/resources";
	public static String mprivateKey = prvt[prvt.length - 1];
	// public static String username = "abdul";
	public static String password = "1122334455";
	public static String url = "http://172.17.0.2:8545";

	static Web3JConnector web3JConnector = new Web3JConnector(url);

	public static void main(String[] args) throws Exception {
		// disableLog();
		ArrayList<String> privateKeys = new ManagePrivateKeys().getPrivateKeys();

		String privateKey1 = privateKeys.get(0);
		String privateKey2 = privateKeys.get(1);
		prvt[0] = privateKeys.get(2);
		prvt[1] = privateKeys.get(3);
		prvt[2] = privateKeys.get(4);
		prvt[3] = privateKeys.get(5);
		prvt[4] = privateKeys.get(6);
		//prvt[5] = privateKeys.get(7);
		prvtSh[0] = privateKeys.get(8);
		prvtSh[1] = privateKeys.get(9);
		prvtSh[2] = privateKeys.get(10);
		prvtSh[3] = privateKeys.get(11);
		prvtSh[4] = privateKeys.get(12);
		//prvtSh[5] = privateKeys.get(13);
		usernames[0] = "acc";
		usernames[1] = "citizen";
		usernames[2] = "riverflowmng";
		usernames[3] = "wtrpurificatcpy";
		

		String dapSCAddress = new DeployVDR().deployDAPSmartContract(privateKey2);

		createAndRegisterRSA(dapSCAddress, prvt);
	}

	public static void createAndRegisterRSA(String dapSCAddress, String priv[]) throws Exception {

		int i = 0;
		for (String privateKey : priv) {
			if(i == usernames.length) {
				break;
			}
			String uname = usernames[i];
			System.out.println("=========================== > " + uname);
			i++;
			String addr = new AccountsManagerV3().createAccount(walletDir, uname, password);
			Credentials cred = new AccountsManagerV3().getCredentials(walletDir, uname, password);

			String rsaKeyStored = new VeidblockManager(url).getRSAPublicKey(dapSCAddress,
					Numeric.toHexStringWithPrefix(cred.getEcKeyPair().getPrivateKey()), cred.getAddress());
			System.out.println("============================> " + rsaKeyStored);
			DeployVDR manageEther = new DeployVDR();
			manageEther.manageAccounts(0, "1900000000000", walletDir, uname, password, url, privateKey);
			// Manage more
			manageEther.manageAccounts(0, "1900000000000", walletDir, uname, password, url, prvtSh[i]);

			if (Objects.isNull(rsaKeyStored) || rsaKeyStored.length() == 0) {

				DistinguishName distinguishName = DistinguishName.builder().name(uname).build();

				CertificateSuite certificateSuite = new CertificateSuite(uname,
						CertificateHandlingBC.getClientKeyUsage());
				StoreHandling storeHandling = new StoreHandling();

				CertificateHandlingBC certificateHandlingBC = new CertificateHandlingBC(certificateSuite, password);
				certificateHandlingBC.createSelfSignedClientCert(distinguishName,
						CertificateHandlingBC.getClientKeyUsage());
				PublicKey publicKey = storeHandling.fetchCertificate(certificateSuite, distinguishName).getPublicKey();

				System.out.println("Registering RSA key: " + PEMStream.toHex(publicKey));
				long recordStart = System.currentTimeMillis();

				new VeidblockManager(url).addRSAPublicKey(dapSCAddress,
						Numeric.toHexStringWithPrefix(cred.getEcKeyPair().getPrivateKey()),
						PEMStream.bytesToHex(publicKey.getEncoded()));

				long recordEnd = System.currentTimeMillis();
				System.out.println("======> Register RSA Key in VDR time: " + "\t" + recordEnd+ "-" + recordStart 
						+ " = " + (recordEnd - recordStart));
				long fetchStart = System.currentTimeMillis();
				String rsaFetchedKey = new VeidblockManager(url).getRSAPublicKey(dapSCAddress,
						Numeric.toHexStringWithPrefix(cred.getEcKeyPair().getPrivateKey()), cred.getAddress());
				long fetchEnd = System.currentTimeMillis();
				System.out.println("======> Fetch RSA Key from VDR time: " + "\t" + fetchEnd+ "-" + fetchStart 
						+ " = " + (fetchEnd - fetchStart));
				
						System.out.println(
						"Fetched Registered RSA Key: " + rsaFetchedKey);
			}

		}
	}

}
