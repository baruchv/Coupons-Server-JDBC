package com.baruch.coupons.beans;

import java.sql.Date;

import com.baruch.coupons.enums.UserType;

public class Purchase {
	
	//VARIABLES
	
	private int   amount;
	
	private long  id, userID, couponID;
	
	private Date  timeStamp;
	
	private float totalPrice;
	
	private String companyName ,couponTitle;
	
	//CTORS
	
	public Purchase() {
		super();
	}
	
	public Purchase(long userID, long couponID, int amount, Date timeStamp) {
		super();
		this.userID = userID;
		this.couponID = couponID;
		this.amount = amount;
		this.timeStamp = timeStamp;
	}
	
	public Purchase(long id, long userID, long couponID, int amount, Date timeStamp) {
		this(userID, couponID, amount, timeStamp);
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getUserID() {
		return userID;
	}
	
	public void setUserID(long userID) {
		this.userID = userID;
	}
	
	public long getCouponID() {
		return couponID;
	}
	
	public void setCouponID(long couponID) {
		this.couponID = couponID;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public Date getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCouponTitle() {
		return couponTitle;
	}

	public void setCouponTitle(String couponTitle) {
		this.couponTitle = couponTitle;
	}

}
