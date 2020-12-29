package com.baruch.coupons.timertasks;

import java.sql.Date;
import java.util.TimerTask;

import com.baruch.coupons.dao.CouponsDao;


public class CouponsValidator extends TimerTask{
	
	private CouponsDao dao ;
	
	
	public CouponsValidator() {
		dao = new CouponsDao();
	}

	public void run() {
		try{
			Date now = new Date(System.currentTimeMillis());
			dao.deleteExpiredCoupons(now);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

