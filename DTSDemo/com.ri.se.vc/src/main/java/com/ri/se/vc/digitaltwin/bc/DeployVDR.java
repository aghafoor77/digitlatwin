package com.ri.se.vc.digitaltwin.bc;

import java.util.Objects;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert.Unit;

import com.ri.se.dap.VeidblockSmartContract;

public class DeployVDR {

	private static Logger logger_ = LoggerFactory.getLogger(DeployVDR.class);

	public DeployVDR() {

	}

	private static Web3j getWeb3j(String url) throws Exception {
		if (Objects.isNull(url)) {
			System.err.print("URL is not defined, please call constructor once before geting web3j instance !");
			throw new Exception("URL is not defined, please call constructor once before geting web3j instance !");
		}
		return Web3j.build(new HttpService(url));

	}

	/*
	 * public static void disableLog() { LogManager.getLogManager().reset();
	 * ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)
	 * org.slf4j.LoggerFactory .getLogger("org.apache.http");
	 * root.setLevel(ch.qos.logback.classic.Level.INFO);
	 * ch.qos.logback.classic.Logger root1 = (ch.qos.logback.classic.Logger)
	 * org.slf4j.LoggerFactory .getLogger("org.web3j.protocol");
	 * root1.setLevel(ch.qos.logback.classic.Level.INFO); }
	 */
	public static String deployDAPSmartContract(String privateKey) throws Exception {
		DeployVDR manageEther = new DeployVDR();
		
		String credentialsPath = "/home/ag/Desktop/RISE/development/traceability/org.ri.se.trace.test/src/main/resources";
		String password = "99887766";
		String username = "dapsuper";
		String etherURL = "http://172.17.0.2:8545";
		String[] optionsStr = { "Manage Balanace and Deploy Contract", "Manage Balance", "Deploy Contract" };

		JComboBox jcd = new JComboBox(optionsStr);
		jcd.setEditable(false);
		String walletDir = credentialsPath;
		Credentials cred = new AccountsManagerV3().createCredentilsFromPrivateKey(privateKey);
		// create a JOptionPane
		Object[] options = new Object[] {};
		String option = JOptionPane.showInputDialog(null, "Please select options ?", "Smart Contract Manager",
				JOptionPane.QUESTION_MESSAGE, null, optionsStr, optionsStr[0]).toString();
		if (Objects.isNull(option)) {
			return "";
		}
		if (option.equalsIgnoreCase(optionsStr[0])) {
			manageEther.manageAccounts(0, "1900000000000", walletDir, username, password, etherURL, privateKey);
			VeidblockSmartContract veidblockSmartContract = new VeidblockSmartContract(etherURL);
			long recordStart = System.currentTimeMillis();
			String smartContractAddress = veidblockSmartContract.deployContract(walletDir, username, password);
			long recordEnd = System.currentTimeMillis();
			System.out.println("======> Deploy VDR time: " + "\t" + recordStart + "-" + recordEnd + " = "
					+ (recordEnd - recordStart));
			
			System.out.println("This is DAP smart contract address : " + smartContractAddress);
			manageEther.displayMyBalanace(walletDir, username, password, etherURL);
			return smartContractAddress;
			
		} else if (option.equalsIgnoreCase(optionsStr[1])) {
			manageEther.manageAccounts(0, "1900000000000", walletDir, username, password, etherURL, privateKey);
			return "";
		} else {
			if (option.equalsIgnoreCase(optionsStr[2])) {
				VeidblockSmartContract veidblockSmartContract = new VeidblockSmartContract(etherURL);
				String smartContractAddress = veidblockSmartContract.deployContract(walletDir, username, password);
				System.out.println("This is DAP smart contract address : " + smartContractAddress);
				manageEther.displayMyBalanace(walletDir, username, password, etherURL);
				return smartContractAddress;
			}
		}
		return null;
	}

	public void displayMyBalanace(String walletDir, String username, String password, String etherURL)
			throws Exception {
		Credentials cred = new AccountsManagerV3().getCredentials(walletDir, username, password);
		double bal = new EtherTransferManagerV3().getEthBalance(getWeb3j(etherURL), cred.getAddress());
		System.out.printf("Account (%s) balanace = %.0f %s\n", cred.getAddress(), bal, Unit.ETHER.toString());
	}

	public boolean manageAccounts(int accountIndex, String money, String walletDir, String username, String password,
			String etherURL, String privateKey) throws Exception {

		String contractAddress = null;


		// Create my own local accound
		System.out.println("Creating a new account !");
		String address = new AccountsManagerV3().createAccount(walletDir, username, password);
		System.out.println("Address of newly created account is = " + address);
		Credentials cred = new AccountsManagerV3().getCredentials(walletDir, username, password);

		double bal = new EtherTransferManagerV3().getEthBalance(getWeb3j(etherURL), cred.getAddress());
		System.out.printf("Account (%s) balanace = %.0f %s\n", address, bal, Unit.ETHER.toString());

		Credentials credSender = new AccountsManagerV3().createCredentilsFromPrivateKey(privateKey);
		double balSender = new EtherTransferManagerV3().getEthBalance(getWeb3j(etherURL), credSender.getAddress());
		System.out.printf("Account (%s) balanace = %.0f %s\n", credSender.getAddress(), balSender,
				Unit.ETHER.toString());

		try {
			System.out.println("Transferring " + money + " " + Unit.ETHER.toString());
			System.out.println("\tfrom " + credSender.getAddress());
			System.out.println("\tto  " + cred.getAddress() + " account ... ");
			new EtherTransferManagerV3().transferEther(getWeb3j(etherURL), privateKey, cred.getAddress(),
					money/* "4500000000" */, Unit.ETHER, "1", Unit.GWEI);
			bal = new EtherTransferManagerV3().getEthBalance(getWeb3j(etherURL), cred.getAddress());
			System.out.printf("Updated account (%s) balanace = %.0f %s\n", address, bal, Unit.ETHER.toString());
			return true;
		} catch (Exception ibe) {

			ibe.printStackTrace();
			System.out.println("May be change account or private key to provide fresh balance input !");

			return false;
		}
	}

	public boolean manageAccounts(String privateKey, String money, String walletDir, String username, String password,
			String etherURL) throws Exception {
		String contractAddress = null;

		// Create my own local accound
		Credentials cred = new AccountsManagerV3().getCredentials(walletDir, username, password);
		System.out.println("Address = " + cred.getAddress());
		System.out.println(etherURL);
		double bal = new EtherTransferManagerV3().getEthBalance(getWeb3j(etherURL), cred.getAddress());
		System.out.printf("Account (%s) balanace = %.0f %s\n", cred.getAddress(), bal, Unit.ETHER.toString());

		Credentials credSender = new AccountsManagerV3().createCredentilsFromPrivateKey(privateKey);
		double balSender = new EtherTransferManagerV3().getEthBalance(getWeb3j(etherURL), credSender.getAddress());
		System.out.printf("Account (%s) balanace = %.0f %s\n", credSender.getAddress(), balSender,
				Unit.ETHER.toString());

		try {
			System.out.println("Transferring " + money + " " + Unit.ETHER.toString());
			System.out.println("\tfrom " + credSender.getAddress());
			System.out.println("\tto  " + cred.getAddress() + " account ... ");
			new EtherTransferManagerV3().transferEther(getWeb3j(etherURL), privateKey, cred.getAddress(),
					money/* "4500000000" */, Unit.ETHER, "1", Unit.GWEI);
			bal = new EtherTransferManagerV3().getEthBalance(getWeb3j(etherURL), cred.getAddress());
			System.out.printf("Updated account (%s) balanace = %.0f %s\n", cred.getAddress(), bal,
					Unit.ETHER.toString());
			return true;
		} catch (Exception ibe) {
			ibe.printStackTrace();
			return false;
		}
	}
}
