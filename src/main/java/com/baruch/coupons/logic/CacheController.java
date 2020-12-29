package com.baruch.coupons.logic;

import java.util.HashMap;
import java.util.Map;


import org.springframework.stereotype.Component;

import com.baruch.coupons.beans.UserLoginData;

@Component
public class CacheController {
	
	private Map<String, UserLoginData> cache = new HashMap<String, UserLoginData>();
	
	public void put(String token, UserLoginData userDetails) {
		cache.put(token, userDetails);
	}
	
	public UserLoginData get(String token) {
		return cache.get(token);
	}
	
	public void remove(String token) {
		cache.remove(token);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return cache.toString();
	}
	
}
