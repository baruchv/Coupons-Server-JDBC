	package com.baruch.coupons.logic;


import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.baruch.coupons.beans.CensoredUser;
import com.baruch.coupons.beans.SuccessfulLoginData;
import com.baruch.coupons.beans.User;
import com.baruch.coupons.beans.UserLoginData;
import com.baruch.coupons.dao.UsersDao;
import com.baruch.coupons.enums.ErrorTypes;
import com.baruch.coupons.enums.UserType;
import com.baruch.coupons.exceptions.ApplicationException;

@Controller
public class UsersController {
	
	//VARIABLES
	
	@Autowired
	private UsersDao dao;
	
	@Autowired
	private CompaniesController companiesController;
	
	@Autowired
	private CacheController cacheController;
	
	private final String HASHINGEXTENTION = "DASF;lkpoi493i@@#$%*21"; 

	//CTORS
	
	public UsersController() {
		
	}
	
	//PUBLIC-METHODS
	
	public long createUser(User user) throws ApplicationException {
		validateCreateUser(user);
		String hashedPassword = getHashedPassword(user.getPassword());
		user.setPassword(hashedPassword);
		return dao.createUser(user);
	}
	
	public int updateUser(User user) throws ApplicationException{
		validateUserID(user.getId());
		validatePassword(user.getPassword());
		String hashedPassword = getHashedPassword(user.getPassword());
		user.setPassword(hashedPassword);
		return dao.updateUser(user);
	}
	
	public List<CensoredUser> getAllUsers() throws ApplicationException{
		return dao.getAllUsers();
	}
	
	public CensoredUser getUser(long id) throws ApplicationException{
		validateUserID(id);
		return dao.getUser(id);
	}
	
	public List<CensoredUser> getUsersByCompany(long companyID) throws ApplicationException{
		companiesController.validateCompanyID(companyID);
		return dao.getUsersByCompany(companyID);
	}
	
	public List<CensoredUser> getUsersByType(UserType type) throws ApplicationException{
		return dao.getAllUsersByType(type);
	}
	
	/*
	 * Deletes a user from the DB.
	 * purchases associated with the user will be deleted accordingly.
	 */
	public void deleteUser(long id) throws ApplicationException{
		dao.deleteUser(id);
	}
	
	public SuccessfulLoginData login(String userName, String password) throws ApplicationException {
		
		UserLoginData userDetails = dao.login(userName, getHashedPassword(password));
		
		if(userDetails == null) {
			throw new ApplicationException(ErrorTypes.LOGIN_ERROR);
		}
		
		String token = generateToken(userName, password);
		
		cacheController.put(token, userDetails);
		
		return new SuccessfulLoginData(token, userDetails.getType());
	}
	
	public void logout(String token) {
		cacheController.remove(token);
	}
	
	protected void validateUserID(long id) throws ApplicationException{
		if(dao.getUser(id)==null) {
			throw new ApplicationException("UsersController.validateUserID() failed for ID: " +id, ErrorTypes.NO_USER_ID);
		}
	}
	
	//PRIVATE-METHODS
	
	private void validateCreateUser(User user) throws ApplicationException{
		if(dao.doesUserNameExist(user.getUserName())) {
			throw new ApplicationException(ErrorTypes.EXISTING_USERNAME_ERROR);
		}
		validateUserName(user.getUserName());
		validatePassword(user.getPassword());
		if(user.getType() == UserType.COMPANY) {
			validateCompanyID(user.getCompanyID());
		}
	}
	
	private String getHashedPassword(String password ) {
		return String.valueOf((password + HASHINGEXTENTION ).hashCode());
	}
		
	private void validateUserName(String userName) throws ApplicationException{
		if(userName == null) {
			throw new ApplicationException(ErrorTypes.EMPTY_USERNAME_ERROR);
		}
		if(userName.length()<2) {
			throw new ApplicationException(ErrorTypes.INVALID_USERNAME_ERROR);
		}
	}
	
	private void validatePassword(String password) throws ApplicationException{
		if(password == null) {
			throw new ApplicationException(ErrorTypes.EMPTY_PASSWORD_ERROR);
		}
		if(password.length()<8) {
			throw new ApplicationException(ErrorTypes.INVALID_PASSWORD_ERROR);
		}
	}
	
	private void validateCompanyID(Long id) throws ApplicationException{
		if(id == null) {
			throw new ApplicationException(ErrorTypes.EMPTY_COMPANYID_ERROR);
		}
		companiesController.validateCompanyID(id);
	}
	
	private String generateToken(String userName, String password) {
		Calendar now = Calendar.getInstance();
		int token = (userName + password + now.getTime().toString() + HASHINGEXTENTION).hashCode();
		return Integer.toString(token);
	}
}
	
	
	
	

