package com.baruch.coupons.beans;


import java.util.Calendar;

import com.baruch.coupons.enums.Categories;

public class Coupon {
	
	//VARIABLES
	
	private int amount;
	
	private long  id, companyID;
	
	private float  price;
	
	private String  title, description, image;
	
	private Categories  category;
	
	private Calendar startDate, endDate;
	
	private String comapnyName;
	
	
	//CTORS
	
	
	public Coupon() {
		super();
	}
	
	public Coupon(long companyID, int amount, float price, String title, String description, String image,
			Categories category, Calendar startDate, Calendar endDate) {
		super();
		this.companyID = companyID;
		this.amount = amount;
		this.price = price;
		this.title = title;
		this.description = description;
		this.image = image;
		this.category = category;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Coupon(long id, long companyID, int amount, float price, String title, String description, String image,
			Categories category, Calendar startDate, Calendar endDate) {
		this(companyID, amount, price, title, description, image, category, startDate, endDate);
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getCompanyID() {
		return companyID;
	}
	
	public void setCompanyID(long companyID) {
		this.companyID = companyID;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public Categories getCategory() {
		return category;
	}
	
	public void setCategory(Categories category) {
		this.category = category;
	}
	
	public Calendar getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	
	public Calendar getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public String getComapnyName() {
		return comapnyName;
	}

	public void setComapnyName(String comapnyName) {
		this.comapnyName = comapnyName;
	}
}
