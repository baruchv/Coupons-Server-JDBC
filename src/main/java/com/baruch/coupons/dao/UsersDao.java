	package com.baruch.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import com.baruch.coupons.beans.CensoredUser;
import com.baruch.coupons.beans.User;
import com.baruch.coupons.beans.UserLoginData;
import com.baruch.coupons.enums.ErrorTypes;
import com.baruch.coupons.enums.UserType;
import com.baruch.coupons.exceptions.ApplicationException;
import com.baruch.coupons.utils.JdbcUtils;



@Repository
public class UsersDao {
	
	//VARIABLES
	
	private Connection connection;
	
	private PreparedStatement stmnt;
	
	private String sqlStatement;
	
	
	//PUBLIC-METHODS
	
	/*
	 * This method creates a new user in the DB.
	 * If creation process succeeded, the method returns the generated id.
	 */
	public long createUser(User user) throws ApplicationException {
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "insert into users(user_name,password,user_type,company_id) values(?,?,?,?)";
			stmnt = connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
			stmnt.setString(1, user.getUserName());
			stmnt.setString(2, user.getPassword());
			stmnt.setString(3, user.getType().toString());
			if(user.getCompanyID() == null) {
				stmnt.setObject(4, null);
			}
			else {
				stmnt.setLong(4, user.getCompanyID());
			}
			stmnt.execute();
			rs = stmnt.getGeneratedKeys();
			if(rs.next()) {
				return rs.getLong(1);
			}
			throw new ApplicationException("UsersDao.createUser() faild for " + user.toString(), ErrorTypes.GENERAL_ERROR);
		}
		catch(Exception e) {
			throw new ApplicationException("UsersDao.createUser() faild for " + user.toString(), ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt);
		}
	}
	
	/*
	 * This method updates an existing user in the DB.
	 * The method returns the number of rows in DB that where updated as a result of the request.
	 */
	public int updateUser(User user) throws ApplicationException{
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "update users set password = ? where id = ?";
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setString(1, user.getPassword());
			stmnt.setLong(2, user.getId());
			return stmnt.executeUpdate();
		}
		catch(Exception e) {
			throw new ApplicationException("UsersDao.updateUser() faild for User: " + user.toString(), ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt);
		}
	}
	
	/*
	 * Deletes the user with the given id.
	 * purchases associated with the user will be deleted accordingly.
	 */
	public void deleteUser(long id) throws ApplicationException{
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "delete from users where id=?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, id);
			stmnt.execute();
		}
		catch(Exception e) {
			throw new ApplicationException("CompaniesDao.deleteUser() faild ", ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt);
		}
	}
	
	public CensoredUser getUser(long id) throws ApplicationException{
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from users where id=?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, id);
			rs = stmnt.executeQuery();
			if(rs.next()) {
				return getUserFromResult(rs);
			}
			return null;
		}
		catch(Exception e) {
			throw new ApplicationException("getUser() faild for ID: " +id, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt,rs);
		}
	}
	
	public List<CensoredUser> getAllUsers() throws ApplicationException{
		ResultSet rs = null;
		try {
			List<CensoredUser> users = new ArrayList<CensoredUser>();
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from users" ;
			stmnt = connection.prepareStatement(sqlStatement);
			rs = stmnt.executeQuery();
			while(rs.next()) {
				users.add(getUserFromResult(rs));
			}
			return users;
		}
		catch(Exception e) {
			throw new ApplicationException("UsersDao.getAllUsers() faild", ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public List<CensoredUser> getAllUsersByType(UserType type) throws ApplicationException{
		ResultSet rs = null;
		try {
			List<CensoredUser> users = new ArrayList<CensoredUser>();
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from users where user_type =?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setString(1, type.toString());
			rs = stmnt.executeQuery();
			while(rs.next()) {
				users.add(getUserFromResult(rs));
			}
			return users;
		}
		catch(Exception e) {
			throw new ApplicationException("UsersDao.getAllUsersByType() faild for UserType: " + type.toString(), ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public List<CensoredUser> getUsersByCompany(long companyID) throws ApplicationException{
		ResultSet rs = null;
		try {
			List<CensoredUser> users = new ArrayList<CensoredUser>();
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from users where company_id = ?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setLong(1, companyID);
			rs = stmnt.executeQuery();
			while(rs.next()) {
				users.add(getUserFromResult(rs));
			}
			return users;
		}
		catch(Exception e) {
			throw new ApplicationException("UsersDao.getUsersByCompany faild for ID: " + companyID, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public UserLoginData login(String userName,String password) throws ApplicationException {
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from users where user_name =? and password=?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setString(1, userName);
			stmnt.setString(2, password);
			rs = stmnt.executeQuery();
			if(rs.next()) {
				return new UserLoginData(rs.getLong("id"), (Long)rs.getObject("company_id"), UserType.valueOf(rs.getString("user_type")));
			}
			return null;
		}
		catch(Exception e) {
			throw new ApplicationException("UsersDao.login() faild for userName: " +userName +" Password: " + password, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection,stmnt,rs);
		}
	}
	
	public boolean doesUserNameExist(String userName) throws ApplicationException {
		ResultSet rs = null;
		try {
			boolean existence = false;
			connection = JdbcUtils.getConnection();
			sqlStatement = "select * from users where user_name =?" ;
			stmnt = connection.prepareStatement(sqlStatement);
			stmnt.setString(1, userName);
			rs = stmnt.executeQuery();
			if(rs.next()) {
				existence = true;
			}
			return existence;
		}
		catch(Exception e) {
			throw new ApplicationException("UsersDao.doesUserNameExist() faild for userName: " + userName, ErrorTypes.GENERAL_ERROR,e);
		}
		finally {
			JdbcUtils.closeResources(connection, stmnt,rs);
		}
	}
	
	//PRIVATE-METHODS
	
	private CensoredUser getUserFromResult(ResultSet rs) throws ApplicationException {
		try {
		return new CensoredUser(rs.getString("user_name"), rs.getLong("id"), UserType.valueOf(rs.getString("user_type").toUpperCase()), (Long)rs.getObject("company_id"));
		}
		catch(Exception e) {
			throw new ApplicationException("UsersDao.getUserFromResult() faild", ErrorTypes.GENERAL_ERROR,e);
		}
	}
	
	
}
