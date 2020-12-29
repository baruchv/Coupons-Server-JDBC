package com.baruch.coupons.beans;

import com.baruch.coupons.enums.UserType;

public class User {
	
	//VARIABLES
	
	private long id;
	
	private Long  companyID;
	
	private String  userName, password;
	
	private UserType  type;

	
	//CTORS
	
	
	public User() {
		super();
	}
		
	public User(String userName, String password, UserType type, Long companyID) {
		super();
		this.userName = userName;
		this.password = password;
		this.type = type;
		this.companyID = companyID;
	}
	
	public User(String userName, String password, long id, UserType type, Long companyID) {
		this(userName, password, type, companyID);
		this.id = id;
	}
	
	
	//METHODS
	
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public UserType getType() {
		return type;
	}
	
	public void setType(UserType type) {
		this.type = type;
	}
	
	public Long getCompanyID() {
		return companyID;
	}
	
	public void setCompanyID(Long companyID) {
		this.companyID = companyID;
	}
	
	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password + ", id=" + id + ", type=" + type
				+ ", companyID=" + companyID + "]";
	}
	
	
	
}
