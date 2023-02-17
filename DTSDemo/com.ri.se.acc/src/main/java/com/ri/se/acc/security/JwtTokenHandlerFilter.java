package com.ri.se.acc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ri.se.acc.delegator.UserDelegator;
import com.ri.se.acc.users.User;

@Component
public class JwtTokenHandlerFilter extends OncePerRequestFilter {

	Log logger = LogFactory.getLog(JwtTokenHandlerFilter.class);
	String marker = "\t===> : ";

	private UserDelegator userDelegator = new UserDelegator();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (!hasAuthorizationBearer(request)) {
			logger.info(marker + "JWS Token based authorization not required !");
			filterChain.doFilter(request, response);
			return;
		}
		logger.info(marker + "Verifying token !");
		
		String token = getAccessToken(request);
		logger.info(marker + "JWS Token extracted !");
		try {
			if (!userDelegator.verify(token)) {
				response.addHeader("exception", "Invalid JWS Token !");
				filterChain.doFilter(request, response);
				return;
			}
		} catch (Exception e) {
			logger.error(marker + "Error in verifying JWS Token !");
			logger.error(marker + "" + e.getMessage());
			response.addHeader("exception", "JWS Token expired !");
			filterChain.doFilter(request, response);
			return;
		}
		response.setHeader("Authorization", header);
		setAuthenticationContext(token, request);
		filterChain.doFilter(request, response);
	}

	private boolean hasAuthorizationBearer(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
			return false;
		}

		return true;
	}

	private String getAccessToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		String token = header.split(" ")[1].trim();
		return token;
	}

	private void setAuthenticationContext(String token, HttpServletRequest request) {
		logger.info(marker + "Setting authentication context !");
		UserDetails userDetails = null;
		try {
			userDetails = getUserDetails(token);
		} catch (Exception e) {
			e.printStackTrace();
		}

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private UserDetails getUserDetails(String token) throws Exception {
		User user = userDelegator.getJWTTokenOwner(token);
		String roles = (String) user.getRoles();
		roles = roles.replace("[", "").replace("]", "");
		user.setRoles(roles);
		logger.info(marker + "User information extracted !");
		return new UserDetailsImpl(user);
	}
}
