package com.ri.se.vc.digitaltwin.bc;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.ri.se.vt.blockchain.exceptions.EtherGenericException;
import org.ri.se.vt.blockchain.exceptions.InsufficientBalanceException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

public class EtherTransferManagerV3 {
	
	public void transferEther(Web3j web3, String privateKey, String recipient, String ethToBeTransfered, Unit moneyUnit,
			String gasFee, Unit gassUnit) throws InsufficientBalanceException, EtherGenericException {
		// Decrypt and open the wallet into a Credential object
		Credentials credentials = new AccountsManagerV3().createCredentilsFromPrivateKey(privateKey);
		double bal;

		bal = getEthBalance(web3, credentials.getAddress());

		if (bal <= Double.parseDouble(ethToBeTransfered)) {
			throw new InsufficientBalanceException("Insuficent balance in " + credentials.getAddress() + " account !");
		}

		// Get nonce
		EthGetTransactionCount ethGetTransactionCount;
		try {
			ethGetTransactionCount = web3
					.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
		} catch (IOException e) {
			throw new EtherGenericException(e);
		}
		BigInteger nonce = ethGetTransactionCount.getTransactionCount();
		// ---------------------------------
		// Recipient account

		//
		// Value to Transfer
		BigInteger value = Convert.toWei(ethToBeTransfered, moneyUnit).toBigInteger();

		// A transfer cost 21,000 units of gas
		BigInteger gasLimit = BigInteger.valueOf(21000);

		//
		// I am willing to pay 1Gwei (1,000,000,000 wei or 0.000000001 ether) for each
		// unit of gas consumed by the transaction.
		BigInteger gasPrice = Convert.toWei(gasFee, gassUnit).toBigInteger();
		RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, recipient,
				value);
		 
		// Sign the transaction
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);

		// Convert it to Hexadecimal String to be sent to the node
		String hexValue = Numeric.toHexString(signedMessage);
		// Send transaction
		EthSendTransaction ethSendTransaction;
		try {
			ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
		} catch (IOException e) {
			throw new EtherGenericException(e);
		}

		// Get the transaction hash
		String transactionHash = ethSendTransaction.getTransactionHash();
		Optional<TransactionReceipt> transactionReceipt = null;
		int i = 0;
		do {
			EthGetTransactionReceipt ethGetTransactionReceiptResp;
			try {
				ethGetTransactionReceiptResp = web3.ethGetTransactionReceipt(transactionHash).send();
			} catch (IOException e) {
				throw new EtherGenericException(e);
			}
			transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				throw new EtherGenericException(e);
			} // Retry after 3 sec
			i++;
			if (i >= 10) {
				throw new EtherGenericException(
						"Tried 10 times to get the transaction receipt but it is not ready yet ! Please try later.");
			}
		} while (!transactionReceipt.isPresent());

	}
	public double getEthBalance(Web3j web3, String address) throws EtherGenericException {
		EthGetBalance ethGetBalance;
		try {
			ethGetBalance = web3.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new EtherGenericException(e);
		}
		BigDecimal balanceInEther = Convert.fromWei(ethGetBalance.getBalance().toString(), Unit.ETHER);
		return balanceInEther.doubleValue();
	}

}
