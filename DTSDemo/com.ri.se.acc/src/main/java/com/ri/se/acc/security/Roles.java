package com.ri.se.acc.security;

public enum Roles {
	ADMIN("ADMIN"), ISSUER("ISSUER"), HOLDER("HOLDER"), VERIFIER("VERIFIER");

	// declaring private variable for getting values
	private String role;

	Roles(String r) {
		role = r;
	}

	// getter method
	public String getRole() {
		return this.role;
	}

}
