package com.baruch.coupons.beans;

import com.baruch.coupons.enums.UserType;

public class SuccessfulLoginData {
	
	private String token;
	private UserType type;
	
	
	public SuccessfulLoginData() {
		super();
	}

	public SuccessfulLoginData(String token, UserType type) {
		super();
		this.token = token;
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SuccessfulLoginData [token=" + token + ", type=" + type + "]";
	}
	
	
	
}
