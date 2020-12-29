package com.baruch.coupons.beans;

import com.baruch.coupons.enums.UserType;

public class UserLoginData {
	
	private long id;
	private Long companyID;
	private UserType type;
	
	
	public UserLoginData(long id, Long companyID, UserType type) {
		super();
		this.id = id;
		this.companyID = companyID;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getCompanyID() {
		return companyID;
	}

	public void setCompanyID(Long companyID) {
		this.companyID = companyID;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "UserLoginData [id=" + id + ", companyID=" + companyID + ", type=" + type + "]";
	}
	
	
}
