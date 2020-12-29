package com.baruch.coupons.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baruch.coupons.beans.UserLoginData;
import com.baruch.coupons.logic.CacheController;

@Component
public class LoginFilter implements Filter {
	
	@Autowired
	private CacheController cacheController;
	
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest)(request);
		
		if( !isLoginRequired(httpRequest) ) {
			chain.doFilter(request, response);
			return;
		}
		
		String token = httpRequest.getHeader("Authorization");
		
		UserLoginData loginData = cacheController.get(token);
		
		if( loginData != null ) {
			httpRequest.setAttribute("UserLoginData", loginData);
			chain.doFilter(httpRequest, response);
			return;
		}
		
		HttpServletResponse httpResponse = (HttpServletResponse)(response);
		
		httpResponse.setStatus(401);
	}
	
	private boolean isLoginRequired(HttpServletRequest httpRequest) {
		
		//It's a Login request, therefore login isn't required.
		
		if(httpRequest.getRequestURL().toString().endsWith("/login")) {
			return false;
		}
		
		//It's a Sign In request, therefore login isn't required.
		
		if(httpRequest.getMethod().equals("POST")) {
		
			if(httpRequest.getRequestURL().toString().endsWith("/users")) {
				return false;
			}
		}
		
		return true;
	}

}
