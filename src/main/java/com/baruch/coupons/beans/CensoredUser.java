package com.baruch.coupons.beans;

import com.baruch.coupons.enums.UserType;

public class CensoredUser {
		
		//VARIABLES
	
		private long id;
		
		private Long  companyID;
		
		private String  userName;
		
		private UserType  type;

		//CTORS
		
		public CensoredUser() {
			super();
		}
		
		public CensoredUser(String userName, long id, UserType type, Long companyID) {
			super();
			this.id = id;
			this.companyID = companyID;
			this.userName = userName;
			this.type = type;
		}
		
		//GETTERS-SETTERS

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

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public UserType getType() {
			return type;
		}

		public void setType(UserType type) {
			this.type = type;
		}
		
		//METHODS

		@Override
		public String toString() {
			return "UserForPresentation [id=" + id + ", companyID=" + companyID + ", userName=" + userName + ", type="
					+ type + "]";
		}
		
		

		
		
		
		
		
		
		
		
	
}
