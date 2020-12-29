package com.baruch.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.baruch.coupons.beans.Purchase;
import com.baruch.coupons.enums.ErrorTypes;
import com.baruch.coupons.exceptions.ApplicationException;
import com.baruch.coupons.utils.JdbcUtils;

@Repository
public class PurchasesDao {
	
	//VARIABLES
	
	private Connection connection;
	
	private PreparedStatement stmnt;
	
	private String sqlStatement;
	
	//PUBLIC-METHODS
	
	/*
	 * This method creates a new purchase in the DB.
	 * If creation process succeeded, the method returns the generated id.
	 */
	public long addPurchase(Purchase purchase) throws ApplicationException {
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "insert into purchases(user_id,coupon_id,amount,time_stamp) values(?,?,?,?)";
			stmnt = connection.prepareStatement(sqlStatement,PreparedStatement.RETURN_GENERATED_KEYS);
			stmnt.setLong(1, purchase.getUserID());
			stmnt.setLong(2, purchase.getCouponID());
			stmnt.setInt(3, purchase.getAmount());
			stmnt.setDate(4, purchase.getTimeStamp());
			stmnt.execute();
			rs = stmnt.getGeneratedKeys();
			if(rs.next()) {
				return rs.getLong(1);
			}
			throw new ApplicationException("addPurchase() failed for " + purchase.toString(), ErrorTypes.GENERAL_ERROR);
		}
		catch(Exception e) {
			throw new ApplicationException("PurchasesDao.addPurchase() failed for " , ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt,rs);
		}
	}
	
	public Purchase getPurchase(long id) throws ApplicationException{
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from purchases where id=?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, id);
			rs = stmnt.executeQuery();
			if(rs.next()) {
				return getPurchaseFromResult(rs);
			}
			return null;
		}
		catch(Exception e) {
			throw new ApplicationException("PurchasesDao.getPurchae() failed", ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt,rs);
		}
	}
	
	public List<Purchase> getAllPurchases() throws ApplicationException{
		ResultSet rs = null;
		try {
			List<Purchase> Purchases = new ArrayList<Purchase>();
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from purchases" ;
			stmnt = connection.prepareStatement(sqlStatement);
			rs = stmnt.executeQuery();
			while(rs.next()) {
				Purchases.add(getPurchaseFromResult(rs));
			}
			return Purchases;
		}
		catch(Exception e) {
			throw new ApplicationException("PurchasesDao.getAllPurchases() failed", ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public List<Purchase> getPurchasesByUser(long userID) throws ApplicationException{
		ResultSet rs = null;
		try {
			List<Purchase> Purchases = new ArrayList<Purchase>();
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from purchases where user_id=?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, userID);
			rs = stmnt.executeQuery();
			while(rs.next()) {
				Purchases.add(getPurchaseFromResult(rs));
			}
			return Purchases;
		}
		catch(Exception e) {
			throw new ApplicationException("PurchasesDao.getPurchasesByUser() failed", ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public List<Purchase> getPurchasesByCompany(long companyID) throws ApplicationException{
		ResultSet rs = null;
		try {
			List<Purchase> Purchases = new ArrayList<Purchase>();
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from purchases where coupon_id in(select coupons.id from coupons where company_id=?)" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, companyID);
			rs = stmnt.executeQuery();
			while(rs.next()) {
				Purchases.add(getPurchaseFromResult(rs));
			}
			return Purchases;
		}
		catch(Exception e) {
			throw new ApplicationException("PurchasesDao.getPurchasesByCompany() failed", ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public void deletePurchase(long id) throws ApplicationException{
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "delete from purchases where id=?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, id);
			stmnt.execute();
		}
		catch(Exception e) {
			throw new ApplicationException("PurchasesDao.deletePurchase() failed", ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt);
		}
	}
	
	//PRIVATE-METHODS
	
	private Purchase getPurchaseFromResult(ResultSet rs) throws ApplicationException {
		try {
		return new Purchase(rs.getLong("id"), rs.getLong("user_id"), rs.getLong("coupon_id"), rs.getInt("amount"), rs.getDate("time_stamp"));
		}
		catch(Exception e) {
			throw new ApplicationException("getPurchaseFromResult() failed", ErrorTypes.GENERAL_ERROR,e);
		}
	}
}
