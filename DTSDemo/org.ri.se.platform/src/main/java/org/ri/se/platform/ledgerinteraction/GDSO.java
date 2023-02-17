/*
 * package org.ri.se.platform.ledgerinteraction;
 * 
 * import java.util.Objects;
 * 
 * import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
 * import org.acreo.security.utils.PEMStream; import
 * org.ri.se.platform.datamodel.AccessLevel; import
 * org.ri.se.platform.datamodel.DataStorageType; import
 * org.ri.se.platform.datamodel.DataType; import
 * org.ri.se.platform.datamodel.EncodedGenericDataSharingObject; import
 * org.ri.se.platform.datamodel.GDSOHeader; import
 * org.ri.se.platform.datamodel.GenericDataSharingObject; import
 * org.ri.se.platform.datamodel.PublicCredentials; import
 * org.ri.se.platform.datamodel.PublicCredentialsList; import
 * org.ri.se.platform.datamodel.SecurityContext; import
 * org.ri.se.platform.engine.GDSOOperation; import
 * org.ri.se.platform.engine.IGDSOOperation; import org.ri.se.vt.ITrace; import
 * org.ri.se.vt.Record; import org.ri.se.vt.RecordList; import
 * org.ri.se.vt.Trace; import org.ri.se.vt.blockchain.Web3JConnector; import
 * org.web3j.crypto.Credentials;
 * 
 * import com.ri.se.dap.VeidblockManager;
 * 
 * public class GDSO {
 * 
 * public EncodedGenericDataSharingObject createGDSO(String traceAddress, String
 * dapAddress, Credentials credentials, String etherURL, String identity,
 * AccessLevel accessLevel, DataType dt, DataStorageType dst, SecurityContext
 * securityContext, ENCODING_DECODING_SCHEME encoding, String recipent, byte[]
 * data) throws Exception {
 * 
 * // Get public keys from smart contact PublicCredentialsList
 * publicCredentialsList = getPublicCredentialsList(accessLevel, credentials,
 * etherURL, traceAddress, dapAddress, identity, recipent);
 * 
 * IGDSOOperation igdsoOperation = new GDSOOperation(); GenericDataSharingObject
 * n = igdsoOperation.create(publicCredentialsList, accessLevel, dt, dst,
 * securityContext, encoding, data); EncodedGenericDataSharingObject
 * dataSharingObject = new EncodedGenericDataSharingObject(n);
 * System.out.println(dataSharingObject.serialize()); return dataSharingObject;
 * }
 * 
 * public EncodedGenericDataSharingObject createGDSO(String traceAddress, String
 * dapAddress, Credentials credentials, String etherURL, String identity,
 * GDSOHeader gdsoHeader, String recipent, byte[] data) throws Exception {
 * return createGDSO(traceAddress, dapAddress, credentials, etherURL, identity,
 * gdsoHeader.getAccessLevel(), gdsoHeader.getDataType(),
 * gdsoHeader.getDataStorageType(), gdsoHeader.getSecurityContext(),
 * gdsoHeader.getEncoding(), recipent, data);
 * 
 * }
 * 
 * private PublicCredentialsList getPublicCredentialsList(AccessLevel
 * accessLevel, Credentials credentials, String etherURL, String traceAddress,
 * String dapAddress, String identity, String recipent) throws Exception {
 * 
 * switch (accessLevel) { case CHAIN: return getChainData(credentials, etherURL,
 * traceAddress, dapAddress, identity, recipent);
 * 
 * case PREVIOUSOWNER: return getPrevious(credentials, etherURL, traceAddress,
 * dapAddress, identity, recipent);
 * 
 * case PREVIOUSOWNERS: return getChainData(credentials, etherURL, traceAddress,
 * dapAddress, identity, recipent);
 * 
 * case RECIPIENT:
 * 
 * break; case RECIPIENTS: break; case NONE: break; default: break; } return
 * null; }
 * 
 * private PublicCredentialsList getChainData(Credentials credentials, String
 * etherURL, String traceAddress, String dapAddress, String identity, String
 * recipent) throws Exception { ITrace trace = new Trace(new
 * Web3JConnector(etherURL), traceAddress, credentials); if
 * (!trace.isSmartContractExists()) { throw new
 * Exception("Traceability smart contract does not exisit !"); }
 * VeidblockManager veidblockManager = new VeidblockManager(etherURL); if
 * (!veidblockManager.isSmartContractExists(dapAddress, credentials)) { throw
 * new Exception("DAP smart contract does not exisit !"); }
 * 
 * RecordList recordList = trace.get(identity); if (Objects.isNull(recordList)
 * || recordList.size() == 0) { return null; } PublicCredentialsList
 * publicCredentialsList = new PublicCredentialsList(); for (Record r :
 * recordList) { String add = r.getSender(); String rsaKey =
 * veidblockManager.getRSAPublicKey(dapAddress, credentials, add); if
 * (Objects.isNull(rsaKey)) { throw new Exception(" Ledger address '" +
 * credentials.getAddress() + "' does not have RSA publickey !"); }
 * PublicCredentials publicCredentials = new PublicCredentials();
 * publicCredentials.setLedgerAddress(add); publicCredentials.setPublicKey(new
 * PEMStream().fromPem(rsaKey)); publicCredentialsList.add(publicCredentials); }
 * // get my own key { String myRSAKey =
 * veidblockManager.getRSAPublicKey(dapAddress, credentials,
 * credentials.getAddress()); if (Objects.isNull(myRSAKey)) { throw new
 * Exception(" Ledger address '" + credentials.getAddress() +
 * "' does not have RSA publickey when trying to extract my own !"); }
 * PublicCredentials myPublicCredentials = new PublicCredentials();
 * myPublicCredentials.setLedgerAddress(credentials.getAddress());
 * myPublicCredentials.setPublicKey(new PEMStream().fromPem(myRSAKey));
 * publicCredentialsList.add(myPublicCredentials); } // Get recipent { String
 * recRSAKey = veidblockManager.getRSAPublicKey(dapAddress, credentials,
 * recipent); if (Objects.isNull(recRSAKey)) { throw new
 * Exception(" Ledger address '" + credentials.getAddress() +
 * "' does not have RSA publickey when trying to extract recipent's key !"); }
 * PublicCredentials recPublicCredentials = new PublicCredentials();
 * recPublicCredentials.setLedgerAddress(credentials.getAddress());
 * recPublicCredentials.setPublicKey(new PEMStream().fromPem(recRSAKey));
 * publicCredentialsList.add(recPublicCredentials); } return
 * publicCredentialsList; }
 * 
 * private PublicCredentialsList getPrevious(Credentials credentials, String
 * etherURL, String traceAddress, String dapAddress, String identity, String
 * recipent) throws Exception { ITrace trace = new Trace(new
 * Web3JConnector(etherURL), traceAddress, credentials); if
 * (!trace.isSmartContractExists()) { throw new
 * Exception("Traceability smart contract does not exisit !"); }
 * VeidblockManager veidblockManager = new VeidblockManager(etherURL); if
 * (!veidblockManager.isSmartContractExists(dapAddress, credentials)) { throw
 * new Exception("DAP smart contract does not exisit !"); }
 * 
 * RecordList recordList = trace.get(identity); if (Objects.isNull(recordList)
 * || recordList.size() == 0) { return null; } Record r =
 * recordList.get(recordList.size() - 1); PublicCredentialsList
 * publicCredentialsList = new PublicCredentialsList(); String add =
 * r.getSender(); String rsaKey = veidblockManager.getRSAPublicKey(dapAddress,
 * credentials, add); if (Objects.isNull(rsaKey)) { throw new
 * Exception(" Ledger address '" + credentials.getAddress() +
 * "' does not have RSA publickey !"); } PublicCredentials publicCredentials =
 * new PublicCredentials(); publicCredentials.setLedgerAddress(add);
 * publicCredentials.setPublicKey(new PEMStream().fromPem(rsaKey));
 * publicCredentialsList.add(publicCredentials); // get my own key { String
 * myRSAKey = veidblockManager.getRSAPublicKey(dapAddress, credentials,
 * credentials.getAddress()); if (Objects.isNull(myRSAKey)) { throw new
 * Exception(" Ledger address '" + credentials.getAddress() +
 * "' does not have RSA publickey when trying to extract my own !"); }
 * PublicCredentials myPublicCredentials = new PublicCredentials();
 * myPublicCredentials.setLedgerAddress(credentials.getAddress());
 * myPublicCredentials.setPublicKey(new PEMStream().fromPem(myRSAKey));
 * publicCredentialsList.add(myPublicCredentials); } // Get recipent { String
 * recRSAKey = veidblockManager.getRSAPublicKey(dapAddress, credentials,
 * recipent); if (Objects.isNull(recRSAKey)) { throw new
 * Exception(" Ledger address '" + credentials.getAddress() +
 * "' does not have RSA publickey when trying to extract recipent's key !"); }
 * PublicCredentials recPublicCredentials = new PublicCredentials();
 * recPublicCredentials.setLedgerAddress(credentials.getAddress());
 * recPublicCredentials.setPublicKey(new PEMStream().fromPem(recRSAKey));
 * publicCredentialsList.add(recPublicCredentials); } return
 * publicCredentialsList; } }
 */