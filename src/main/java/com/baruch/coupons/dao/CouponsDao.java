	package com.baruch.coupons.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import org.springframework.stereotype.Repository;

import com.baruch.coupons.beans.Coupon;
import com.baruch.coupons.enums.Categories;
import com.baruch.coupons.enums.ErrorTypes;
import com.baruch.coupons.exceptions.ApplicationException;
import com.baruch.coupons.utils.JdbcUtils;


@Repository
public class CouponsDao {
	
	//VARIABLES
	
	private Connection connection;
	
	private PreparedStatement stmnt;
	
	private String sqlStatement;
	
	//PUBLIC-METHODS
	
	/*
	 * This method creates a new coupon in the DB.
	 * If creation process succeeded, the method returns the generated id.
	 */
	public long createCoupon(Coupon coupon) throws ApplicationException {
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "insert into Coupons(title,description,category,image,amount,price,start_date,end_date,company_id) values(?,?,?,?,?,?,?,?,?)";
			stmnt = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
			stmnt.setString(1, coupon.getTitle());
			stmnt.setString(2, coupon.getDescription());
			stmnt.setString(3, coupon.getCategory().toString());
			stmnt.setString(4, coupon.getImage());
			stmnt.setInt(5, coupon.getAmount());
			stmnt.setFloat(6, coupon.getPrice());
			stmnt.setDate(7, new Date(coupon.getStartDate().getTimeInMillis()));
			stmnt.setDate(8, new Date(coupon.getEndDate().getTimeInMillis()));
			stmnt.setLong(9, coupon.getCompanyID());
			stmnt.execute();
			rs = stmnt.getGeneratedKeys();
			if(rs.next()) {
				return rs.getLong(1);
			}
			throw new ApplicationException("CouponsDao.createCoupon() failed for " + coupon.toString(),ErrorTypes.GENERAL_ERROR);
		}
		catch(Exception e) {
			throw new ApplicationException("CouponsDao.createCoupon() failed for " + coupon.toString(), ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt);
			
		}
	}
	
	/*
	 * This method updates an existing coupon in the DB.
	 * The method returns the number of rows in DB that where updated as a result of the request.
	 */	
	public int updateCoupon(Coupon coupon) throws ApplicationException{
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "update coupons set amount = ?, price = ?,image=? where id = ?";
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setInt(1, coupon.getAmount());
			stmnt.setFloat(2, coupon.getPrice());
			stmnt.setString(3, coupon.getImage());
			stmnt.setLong(4,coupon.getId());
			return stmnt.executeUpdate();
		}
		catch(Exception e) {
			throw new ApplicationException("CouponsDao.updateCoupon() failed for " + coupon.toString(), ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt);
			
		}
	}
	
	/*
	 * Deletes the coupon with the given id.
	 * purchases associated with the coupon will be deleted accordingly.
	 */
	public void deleteCoupon(long id) throws ApplicationException{
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "delete from coupons where id=?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, id);
			stmnt.execute();
		}
		catch(Exception e) {
			throw new ApplicationException("CouponsDao.deleteCoupon() failed for ID " + id, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt);
			
		}
	}
	
	public void deleteExpiredCoupons(Date now) throws ApplicationException{
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "delete from coupons where end_date <= ?";
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setDate(1, now);
			stmnt.execute();
		}
		catch(Exception e) {
			throw new ApplicationException("CouponsDao.deleteExpiredCoupon() failed for date: " + now, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt);
		}
	}
	
	public Coupon getCoupon(long id) throws ApplicationException{
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from coupons where id=?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, id);
			rs = stmnt.executeQuery();
			if(rs.next()) {
				return getCouponFromResult(rs);
			}
			return null;
		}
		catch(Exception e) {
			throw new ApplicationException("CouponsDao.getCoupon() failed for ID: " + id, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt,rs);
		}
	}
	
	public List<Coupon> getAllCoupons() throws ApplicationException{
		ResultSet rs = null;
		try {
			List<Coupon> Coupons = new ArrayList<Coupon>();
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from coupons" ;
			stmnt = connection.prepareStatement(sqlStatement);
			rs = stmnt.executeQuery();
			while(rs.next()) {
				Coupons.add(getCouponFromResult(rs));
			}
			return Coupons;
		}
		catch(Exception e) {
			throw new ApplicationException("CouponsDao.getAllCoupons() failed", ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public List<Coupon> getCouponsByCategory(Categories category) throws ApplicationException{
		ResultSet rs = null;
		try {
			List<Coupon> Coupons = new ArrayList<Coupon>();
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from coupons where category = ?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setString(1, category.toString());
			rs = stmnt.executeQuery();
			while(rs.next()) {
				Coupons.add(getCouponFromResult(rs));
			}
			return Coupons;
		}
		catch(Exception e) {
			throw new ApplicationException("CouponsDao.getCouponsByCategory() failed for Category: " + category.toString(), ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public List<Coupon> getCouponsByCompany(long companyID) throws ApplicationException{
		ResultSet rs = null;
		try {
			List<Coupon> Coupons = new ArrayList<Coupon>();
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from coupons where company_id = ?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, companyID);;
			rs = stmnt.executeQuery();
			while(rs.next()) {
				Coupons.add(getCouponFromResult(rs));
			}
			return Coupons;
		}
		catch(Exception e) {
			throw new ApplicationException("CouponsDao.getCouponsByCompany() failed for ID: " + companyID, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public List<Coupon> getPurchasedCouponsByMaxPrice(long userID, float maxPrice) throws ApplicationException{
		ResultSet rs = null;
		try {
			List<Coupon> Coupons = new ArrayList<Coupon>();
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from coupons where price <= ? and id in(select coupon_id from purchases where user_id = ?)" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, userID);
			stmnt.setFloat(2, maxPrice);
			rs = stmnt.executeQuery();
			while(rs.next()) {
				Coupons.add(getCouponFromResult(rs));
			}
			return Coupons;
		}
		catch(Exception e) {
			throw new ApplicationException("CouponsDao.getPurchasedCouponsByMaxPrice() failed for userID: " + userID, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public boolean doesCouponExistForCompany(String title, long companyID) throws ApplicationException {
		ResultSet rs = null;
		try {
			boolean existence = false;
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from coupons where title =? and company_id=?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setString(1, title);
			stmnt.setLong(2, companyID);
			rs = stmnt.executeQuery();
			if(rs.next()) {
				existence = true;
			}
			return existence;
		}
		catch(Exception e) {
			throw new ApplicationException("CouponsDao.doesCouponExist() failed for title: " + title, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt,rs);
		}
	}
	
	//PRIVATE-METHODS
	
	private Coupon getCouponFromResult(ResultSet rs) throws ApplicationException {
		try {
			Calendar startDate = Calendar.getInstance();
			Calendar endDate = Calendar.getInstance();
			startDate.setTimeInMillis(rs.getDate("start_date").getTime());
			endDate.setTimeInMillis(rs.getDate("end_date").getTime());
			Categories category = Categories.valueOf(rs.getString("category"));
			return new Coupon(rs.getLong("id"),rs.getLong("company_id"), rs.getInt("amount"), rs.getFloat("price"), rs.getString("title"), rs.getString("description"), rs.getString("image"), category,startDate,endDate );
		}
		catch(Exception e){
			throw new ApplicationException("CouponsDao.getCouponFromResultSet() failed", ErrorTypes.GENERAL_ERROR,e);
		}
	}
	

}
