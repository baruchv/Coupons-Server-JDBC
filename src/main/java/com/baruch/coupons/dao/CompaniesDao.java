package com.baruch.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Repository;

import com.baruch.coupons.beans.Company;
import com.baruch.coupons.enums.ErrorTypes;
import com.baruch.coupons.exceptions.ApplicationException;
import com.baruch.coupons.utils.JdbcUtils;


@Repository
public class CompaniesDao {
	
	//VARIABLES
	
	private Connection connection;
	
	private PreparedStatement stmnt;
	
	private String sqlStatement;
	
	//PUBLIC-METHODS
	
	/*
	 * This method creates a new company in the DB.
	 * If creation process succeeded, the method returns the generated id.
	 */
	public long createCompany(Company company) throws ApplicationException {
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "insert into companies(name,address,phone_number) values(?,?,?)";
			stmnt = connection.prepareStatement(sqlStatement,PreparedStatement.RETURN_GENERATED_KEYS);
			stmnt.setString(1, company.getName());
			stmnt.setString(2, company.getAddress());
			stmnt.setString(3, company.getPhoneNumber());
			stmnt.execute();
			rs = stmnt.getGeneratedKeys();
			if(rs.next()) {
				return rs.getLong(1);
			}
			throw new ApplicationException("CompaniesDao.createCompany() failed for " + company.toString(), ErrorTypes.GENERAL_ERROR);
		}
		catch(Exception e) {
			throw new ApplicationException("CompaniesDao.createCompany() failed for " + company.toString(), ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt,rs);	
		}
	}
	
	/*
	 * This method updates an existing company in the DB.
	 * The method returns the number of rows in DB that where updated as a result of the request.
	 */
	public int updateCompany(Company company) throws ApplicationException{
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "update companies set address = ?,phone_number=? where id = ?";
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setString(1, company.getAddress());
			stmnt.setString(2, company.getPhoneNumber());
			stmnt.setLong(3, company.getId());
			return stmnt.executeUpdate();
		}
		catch(Exception e) {
			throw new ApplicationException("CompaniesDao.updateCompany() failed for " + company.toString(), ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt);	
		}
	}
	
	/*
	 * This method will automatically delete users and coupons that are associated with the given company.
	 * purchases that are associated with coupons belong to the given company will be deleted accordingly.
	 */
	public void deleteCompany(long id) throws ApplicationException{
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "delete from companies where id= ?";
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, id);;
			stmnt.execute();
		}
		catch(Exception e) {
			throw new ApplicationException("CompaniesDao.deleteCompany() failed for ID: " + id, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt);
		}
	}
	
	public Company getCompany(long id) throws ApplicationException{
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from companies where id=?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, id);;
			rs = stmnt.executeQuery();
			if(rs.next()) {
				return getCompanyFromResult(rs);
			}
			
			return null;
		}
		catch(Exception e) {
			throw new ApplicationException("CompaniesDao.getCompany() failed for ID: " + id, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt,rs);
		}
	}
	
	public List<Company> getAllCompanies() throws ApplicationException{
		ResultSet rs = null;
		try {
			List<Company> companies = new ArrayList<Company>();
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from companies" ;
			stmnt = connection.prepareStatement(sqlStatement);
			rs = stmnt.executeQuery();
			while(rs.next()) {
				companies.add(getCompanyFromResult(rs));
			}
			return companies;
		}
		catch(Exception e) {
			throw new ApplicationException("CompaniesDao.getAllCompanies() failed", ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public boolean doesCompanyNameExist(String name) throws ApplicationException {
		ResultSet rs = null;
		try {
			boolean existence = false;
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from companies where name =? " ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setString(1, name);
			rs = stmnt.executeQuery();
			if(rs.next()) {
				existence = true;
			}
			return existence;
		}
		catch(Exception e) {
			throw new ApplicationException("CompaniesDao.doesCompanyExist() failed for name: " + name, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt,rs);
		}
	}
	
	//PRIVATE-METHODS
	
	private Company getCompanyFromResult(ResultSet rs) throws ApplicationException{
		try {
		return new Company(rs.getString("name"), rs.getString("address"), rs.getString("phone_number"), rs.getLong("id"));
		}
		catch(Exception e) {
			throw new ApplicationException("CompaiesDao.getCompanyFromResultSet() failed", ErrorTypes.GENERAL_ERROR,e);
		}
	}
}
