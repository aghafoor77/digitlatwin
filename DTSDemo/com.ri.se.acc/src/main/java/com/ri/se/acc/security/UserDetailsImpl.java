
package com.ri.se.acc.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ri.se.acc.users.User;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String userName;
	private String password;
	private String roles;
	private boolean isActive;
	private boolean isVerified;
	private List<GrantedAuthority> authorities;

	public UserDetailsImpl(User user) {
		// Careful using email as a name 
		this.userName = user.getEmail();
		this.password = user.getPassword();
		this.isActive = (user.getStatus() == 0) ? true : false;
		this.isVerified = (user.getVerified() == 0) ? true : false;
		
		
		String upRoles[] = user.getRoles().split(",");
		for(int i=0 ;i<upRoles.length; i++) {
			upRoles[i] = "ROLE_"+upRoles[i];
		}
		this.authorities = Arrays.stream(upRoles).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		this.roles = user.getRoles();
	}

	public UserDetailsImpl() {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {

		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isActive;
	}

	public String getRoles() {
		return roles;
	}

	public boolean isVerified() {
		return isVerified;
	}	
}
