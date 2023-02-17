/**
 * 
 */
package org.ri.se.platform.datamodel;

/**
 * @author blockchain
 *
 */
public enum DataStorageType {
	INLINE("INLINE"), FTP("FTP"), IPFS("IPFS");

	DataStorageType(String value) {
		this.value = value;
	}

	private final String value;

	public String value() {
		return value;
	}
}
