	package com.baruch.coupons.logic;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.baruch.coupons.beans.Company;
import com.baruch.coupons.dao.CompaniesDao;
import com.baruch.coupons.enums.ErrorTypes;
import com.baruch.coupons.exceptions.ApplicationException;

@Controller
public class CompaniesController {
	
	//VERIABLES
	
	@Autowired
	private CompaniesDao dao;

	//CTORS
	
	public CompaniesController() {
		
	}
	
	//PUBLIC-METHODS
	
	public long createCompany(Company company) throws ApplicationException{
		validateCreateCompany(company);	
		return dao.createCompany(company);
	}
	
	public int updateCompany(Company company) throws ApplicationException{
		validateUpdateCompany(company);
		return dao.updateCompany(company);
	}
	
	/*
	 * Deletes a company from the DB.
	 * Users, coupons and purchases associated with the company will be deleted accordingly.
	 */
	public void deleteCompany(long id) throws ApplicationException {
		dao.deleteCompany(id);
	}
	
	public Company getCompany(long id) throws ApplicationException {
		validateCompanyID(id);
		return dao.getCompany(id);
	}
	
	public List<Company> getAllcompanies() throws ApplicationException{
		return dao.getAllCompanies();
	}
	
	protected void validateCompanyID(long id) throws ApplicationException{
		if(dao.getCompany(id)==null) {
			throw new ApplicationException("CompaniesController.validateCompanyID() failed for ID: " + id, ErrorTypes.NO_COMPANY_ID);
		}
	}	
	
	//PRIVATE-METHDOS
	
	private void validateCreateCompany(Company company) throws ApplicationException{
		if(dao.doesCompanyNameExist(company.getName())) {
			throw new ApplicationException(ErrorTypes.EXISTING_COMPANY_ERROR);
		}
		validatePhoneNumber(company.getPhoneNumber());
		validateName(company.getName());
		validateAddress(company.getAddress());
	}
	
	private void validateUpdateCompany(Company company) throws ApplicationException{
		validateCompanyID(company.getId());
		validateAddress(company.getAddress());
		validatePhoneNumber(company.getPhoneNumber());
	}
	
	private void validatePhoneNumber(String phoneNumber) throws ApplicationException{
		if(phoneNumber == null) {
			throw new ApplicationException(ErrorTypes.EMPTY_PHONENUMBER_ERROR);
		}
		if(phoneNumber.length()<10) {
			throw new ApplicationException(ErrorTypes.INVALID_PHONENUMBER_ERROR);
		}
	}
	
	private void validateAddress(String address) throws ApplicationException{
		if(address == null) {
			throw new ApplicationException(ErrorTypes.EMPTY_ADDRESS_ERROR);
		}
		if(address.length()<2) {
			throw new ApplicationException(ErrorTypes.INAVLID_ADDRESS_ERROR);
		}
	}
	
	private void validateName(String name) throws ApplicationException{
		if(name == null) {
			throw new ApplicationException(ErrorTypes.EMPTY_ADDRESS_ERROR);
		}
		if(name.length()<2) {
			throw new ApplicationException(ErrorTypes.INAVLID_ADDRESS_ERROR);
		}
	}
}
