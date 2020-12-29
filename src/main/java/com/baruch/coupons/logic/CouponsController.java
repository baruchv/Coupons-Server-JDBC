package com.baruch.coupons.logic;



import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.baruch.coupons.beans.Company;
import com.baruch.coupons.beans.Coupon;
import com.baruch.coupons.dao.CouponsDao;
import com.baruch.coupons.enums.Categories;
import com.baruch.coupons.enums.ErrorTypes;
import com.baruch.coupons.enums.UserType;
import com.baruch.coupons.exceptions.ApplicationException;

@Controller
public class CouponsController {
	
	//VARIABLES
	
	@Autowired
	private CouponsDao dao;
	
	@Autowired
	private CompaniesController companiesController;
	
	@Autowired
	private UsersController usersController;
	
	//CTORS
	
	public CouponsController() {
		
	}
	
	//PUBLIC-METHODS
	
	/*
	 * Creates a new coupon.
	 * In case startDate is null, copoun's startDate will be reassigned as the current moment.
	 */
	public long createCoupon(Coupon coupon) throws ApplicationException{
		Calendar now = Calendar.getInstance();
		if(coupon.getStartDate() == null) {
			coupon.setStartDate(now);
		}
		validateCreateCoupon(coupon,now);
		return dao.createCoupon(coupon);
	}
	
	public int updateCoupon(Coupon coupon) throws ApplicationException{
		validateUpdateCoupon(coupon);
		return dao.updateCoupon(coupon);
	}
	
	/*
	 * Deletes the coupon with the given id.
	 * purchases associated with the coupon will be deleted accordingly.
	 */
	public void deleteCoupon(long id) throws ApplicationException{
		dao.deleteCoupon(id);
	}
	
	public Coupon getCoupon(long id) throws ApplicationException{
		validateCouponID(id);
		return dao.getCoupon(id);
	}
	
	public List<Coupon> getPruchasedCouponsByMaxPrice(long userID, float maxPrice) throws ApplicationException{
		usersController.validateUserID(userID);
		List<Coupon> coupons = dao.getPurchasedCouponsByMaxPrice(userID, maxPrice);
		prepareForPresentation(coupons);
		return coupons;
	}
	
	public List<Coupon> getAllCoupons() throws ApplicationException{
		return dao.getAllCoupons();
	}
	
	public List<Coupon> getCouponsByCategory(Categories category) throws ApplicationException{
		List<Coupon> coupons = dao.getCouponsByCategory(category);
		prepareForPresentation(coupons);
		return coupons;
	}
	
	public List<Coupon> getCouponsBycompany(long companyID) throws ApplicationException{
		companiesController.validateCompanyID(companyID);
		List<Coupon> coupons = dao.getCouponsByCompany(companyID);
		prepareForPresentation(coupons);
		return coupons;
	}
	
	protected void validateCouponID(long id) throws ApplicationException{
		if(dao.getCoupon(id)==null) {
			throw new ApplicationException("CouponsController.validateCouponID failed for ID: " + id, ErrorTypes.GENERAL_ERROR);
		}
	}
	
	//PRIVATE-METHODS
	
	private void validateCreateCoupon(Coupon coupon,Calendar now) throws ApplicationException{
		validateTitle(coupon.getTitle(),coupon.getCompanyID());
		validateDescription(coupon.getDescription());
		validateDates(coupon,now);
		if(coupon.getAmount() < 1) {
			throw new ApplicationException(ErrorTypes.INVALID_AMOUNT_ERROR);
		}
		if(coupon.getPrice() < 0) {
			throw new ApplicationException(ErrorTypes.INVALID_PRICE_ERROR);
		}
	}
	
	private void validateUpdateCoupon(Coupon coupon) throws ApplicationException{
		validateCouponID(coupon.getId());
		if(coupon.getAmount() < 1) {
			throw new ApplicationException(ErrorTypes.INVALID_AMOUNT_ERROR);
		}
		if(coupon.getPrice() < 0) {
			throw new ApplicationException(ErrorTypes.INVALID_PRICE_ERROR);
		}
	}
	
	private void validateTitle(String title,long companyID) throws ApplicationException{
		if(title == null) {
			throw new ApplicationException(ErrorTypes.EMPTY_TITLE_ERROR);
		}
		if(title.length()<2) {
			throw new ApplicationException(ErrorTypes.INVALID_TITLE_ERROR);
		}
		if(dao.doesCouponExistForCompany(title, companyID)) {
			throw new ApplicationException(ErrorTypes.DUPLICATE_TITLE_ERROR);
		}
	}
	
	private void validateDescription(String description) throws ApplicationException{
		if(description == null) {
			throw new ApplicationException(ErrorTypes.EMPTY_DESCRIPTION_ERROR);
		}
		if(description.length()<2) {
			throw new ApplicationException(ErrorTypes.INVALID_DESCRIPTION_ERROR);
		}
	}
	
	private void validateDates(Coupon coupon,Calendar now) throws ApplicationException{
		Calendar startDate = coupon.getStartDate();
		Calendar endDate = coupon.getEndDate();
		if(isBefore(startDate,now)) {
			throw new ApplicationException(ErrorTypes.INVALID_STARTDATE_ERROR);
		}
		if(endDate == null) {
			throw new ApplicationException(ErrorTypes.EMPTY_ENDDATE_ERROR);
		}

		else if(isBefore(endDate,startDate)) {
			throw new ApplicationException(ErrorTypes.INVALID_DATES_ERROR);
		}
	}
	
	private boolean isBefore(Calendar time1, Calendar time2) {
		if(time1.get(Calendar.YEAR) < time2.get(Calendar.YEAR)) {
			return true;
		}
		else if(time1.get(Calendar.YEAR) == time2.get(Calendar.YEAR)) {
			if(time1.get(Calendar.MONTH) < time2.get(Calendar.MONTH)) {
				return true;
			}
			else if(time1.get(Calendar.MONTH) == time2.get(Calendar.MONTH)) {
				if(time1.get(Calendar.DAY_OF_MONTH) < time2.get(Calendar.DAY_OF_MONTH)) {
					return true;
				}
			}	
		}
		
		return false;
	}
	
	private void prepareForPresentation(Coupon coupon) throws ApplicationException{
		coupon.setCompanyID(0);
		long companyID = coupon.getCompanyID();
		Company company = companiesController.getCompany(companyID);
		coupon.setComapnyName(company.getName());
	}
	
	private void prepareForPresentation(List<Coupon> coupons) throws ApplicationException{
		for( Coupon coupon : coupons) {
			prepareForPresentation(coupon);
		}
	}
	
	

}
	