package org.ri.se.platform.datamodel;

public enum AccessLevel {
	CHAIN("CHAIN"),SELF("SELF"), PREVIOUSOWNER("PREVIOUSOWNER"), PREVIOUSOWNERS("PREVIOUSOWNERS"), RECIPIENT("RECIPIENT"), RECIPIENTS("RECIPIENTS"), NONE("NONE"), PRIVACY("PRIVACY");

	AccessLevel(String value) {
		this.value = value;
	}

	private final String value;

	public String value() {
		return value;
	}
}