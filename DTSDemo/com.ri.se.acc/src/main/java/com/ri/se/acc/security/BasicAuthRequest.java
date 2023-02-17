package com.ri.se.acc.security;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class BasicAuthRequest {

	@NotNull //@Email @Length(min = 5, max = 50)
	private String email;
	
	@NotNull 
	private String password;

	public BasicAuthRequest() {
		
	}
	
	public BasicAuthRequest(String email,String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
	
	
