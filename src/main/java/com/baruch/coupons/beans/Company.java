package com.baruch.coupons.beans;

public class Company {
	
	//VARIABLES
	
	private String  name, address, phoneNumber;
	
	private long  id;
	
	
	//CTORS
	
	public Company() {
		super();
	}
	
	public Company(String name, String address, String phoneNumber) {
		super();
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
	
	public Company(String name, String address, String phoneNumber, long id) {
		this(name,address,phoneNumber);
		this.id = id;
	}
	
	
	//METHODS
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	
}
