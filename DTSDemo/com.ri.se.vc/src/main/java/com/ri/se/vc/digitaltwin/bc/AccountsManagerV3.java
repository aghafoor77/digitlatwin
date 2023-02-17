package com.ri.se.vc.digitaltwin.bc;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.ri.se.vt.blockchain.exceptions.CredentialException;
import org.ri.se.vt.blockchain.exceptions.EtherGenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;

public class AccountsManagerV3 {

	static Logger log = LoggerFactory.getLogger(AccountsManagerV3.class.getName());

	public String createAccount(String walletDir, String username, String password) throws CredentialException {
		try {

			if (!walletDir.endsWith(File.separator)) {
				walletDir = walletDir + File.separator;
			}
			String path = "";
			String userDir = walletDir + username;
			if (!new File(userDir).exists()) {
				new File(userDir).mkdirs();
			}
			if (Objects.isNull(new File(userDir).list()) || new File(userDir).list().length == 0) {
				log.info("Creating new credentials !");
				path = WalletUtils.generateNewWalletFile(password, new File(userDir), true);
			} else {
				log.info("Credentials already exisit !");
				File fl[] = new File(userDir).listFiles();
				for (File f : fl) {
					if (f.getName().endsWith(".json")) {
						path = f.getName();
					}
				}
			}
			Credentials credentials = WalletUtils.loadCredentials(password, userDir + "/" + path);
			return credentials.getAddress();
		} catch (Exception e) {
			log.error("===> Problems when creating credentials !");
			log.error(e.getMessage());
			throw new CredentialException(e);
		}
	}

	public String createAccount(String username, String password) throws CredentialException {
		try {

			String homeDir = System.getProperty("user.home");
			File dir = new File(homeDir + File.separator + "veidblock_RT" + File.separator + "credentials"
					+ File.separator + username);
			if (!dir.exists())
				dir.mkdirs();

			String userDir = dir.getAbsolutePath();
			String path = "";

			if (Objects.isNull(new File(userDir).list()) || new File(userDir).list().length == 0) {
				log.info("Creating new credentials !");
				path = WalletUtils.generateNewWalletFile(password, new File(userDir), true);
			} else {
				log.info("Credentials already exisit !");
				File fl[] = new File(userDir).listFiles();
				for (File f : fl) {
					if (f.getName().endsWith(".json")) {
						path = f.getName();
					}
				}
			}
			Credentials credentials = WalletUtils.loadCredentials(password, userDir + "/" + path);
			return credentials.getAddress();
		} catch (Exception e) {
			log.error("===> Problems when creating credentials !");
			log.error(e.getMessage());
			throw new CredentialException(e);
		}
	}

	public Credentials getCredentials(String walletDir, String username, String password) throws CredentialException {
		try {
			if (!walletDir.endsWith(File.separator)) {
				walletDir = walletDir + File.separator;
			}
			String path = "";
			String userDir = walletDir + username;
			if (Objects.isNull(new File(userDir)) && new File(userDir).list().length == 0) {
				log.info("Credentials do not exist !");
				throw new CredentialException("Credentials do not exist !");
			} else {
				log.info("Credentials already exisit in the wallet !");
				File fl[] = new File(userDir).listFiles();
				for (File f : fl) {
					if (f.getName().endsWith(".json")) {
						path = f.getName();
					}
				}
			}
			return WalletUtils.loadCredentials(password, userDir + "/" + path);
		} catch (Exception e) {
			log.error("===> Problems when extracting (creating) credentials !");
			log.error(e.getMessage());
			throw new CredentialException(e);
		}
	}

	public Credentials createCredentilsFromPrivateKey(String privateKey) {
		Credentials credentials = Credentials.create(privateKey);// );
		return credentials;
	}

	public EthAccounts getEthAccounts(Web3j web3) throws EtherGenericException {
		EthAccounts result = new EthAccounts();
		try {
			result = web3.ethAccounts().sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("===> Problems when fetching accounts from etherum !");
			log.error(e.getMessage());
			throw new EtherGenericException(e);
		}
		return result;
	}

	public boolean exists(String walletDir, String username) throws CredentialException {
		try {
			if (!walletDir.endsWith(File.separator)) {
				walletDir = walletDir + File.separator;
			}
			String userDir = walletDir + username;
			if (Objects.isNull(new File(userDir).list()) || new File(userDir).list().length == 0) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			log.error("===> Problems when creating credentials !");
			log.error(e.getMessage());
			throw new CredentialException(e);
		}
	}

	public boolean exists(String username) throws CredentialException {
		try {
			String homeDir = System.getProperty("user.home");
			File dir = new File(homeDir + File.separator + "veidblock_RT" + File.separator + "credentials"
					+ File.separator + username);
	
			String userDir = dir.getAbsolutePath();

			if (Objects.isNull(new File(userDir).list()) || new File(userDir).list().length == 0) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			log.error("===> Problems when creating credentials !");
			log.error(e.getMessage());
			throw new CredentialException(e);
		}
	}
}
