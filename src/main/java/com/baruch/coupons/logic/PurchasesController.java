package com.baruch.coupons.logic;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.baruch.coupons.beans.Company;
import com.baruch.coupons.beans.Coupon;
import com.baruch.coupons.beans.Purchase;
import com.baruch.coupons.beans.UserLoginData;
import com.baruch.coupons.dao.PurchasesDao;
import com.baruch.coupons.enums.ErrorTypes;
import com.baruch.coupons.enums.UserType;
import com.baruch.coupons.exceptions.ApplicationException;

@Controller
public class PurchasesController {
	
	//VERIABLES
	
	@Autowired
	private PurchasesDao dao;
	
	@Autowired
	private CouponsController couponsController;
	
	@Autowired
	private CompaniesController companiesController;
	
	@Autowired
	private UsersController usersController;
	
	//CTORS
	
	public PurchasesController() {
		
	}
	
	//PUBLIC-METHODS
	
	/*
	 * Adding a new purchase.
	 * The amount of the purchased coupon will be changed accordingly.
	 * The method assumes all coupons in the DB are valid.
	 */
	public long addPurchase(Purchase purchase) throws ApplicationException{
		validateAddPurchase(purchase.getAmount(), purchase.getCouponID());
		long id = dao.addPurchase(purchase);
		
		//Updating coupon's amount.
		Coupon updatedCoupon = couponsController.getCoupon(purchase.getCouponID());
		int amount = updatedCoupon.getAmount();
		updatedCoupon.setAmount(amount - purchase.getAmount());
		couponsController.updateCoupon(updatedCoupon);
		
		return id;
	}
	
	public Purchase getPrchase(long id) throws ApplicationException{
		Purchase purchase = dao.getPurchase(id);
		if( purchase == null) {
			throw new ApplicationException("PurchasesController.getPurchase() failed for ID: " + id, ErrorTypes.GENERAL_ERROR);
		}
		prepareForPresentation(purchase, UserType.ADMIN);
		return purchase;
	}
	
	public List<Purchase> getAllPurchases(UserLoginData userData) throws ApplicationException{
		List<Purchase> purchases = null;
		switch (userData.getType()) {
		case COMPANY:
			long companyID = userData.getCompanyID();
			purchases = dao.getPurchasesByCompany(companyID);
			break;
		case CUSTOMER:
			long userID = userData.getId();
			dao.getPurchasesByUser(userID);
			break;
		default:
			purchases = dao.getAllPurchases();
			break;
		}
		prepareForPresentation(purchases, userData.getType());
		return purchases;
	}
	
	public List<Purchase> getPurchasesByUser(long userID) throws ApplicationException{
		usersController.validateUserID(userID);
		List<Purchase> purchases = dao.getPurchasesByUser(userID);
		prepareForPresentation(purchases, UserType.ADMIN);
		return purchases;
	}
	
	//This methods serves Admin users only.
	public List<Purchase> getPurchasesByCompany(long companyID) throws ApplicationException{
		companiesController.validateCompanyID(companyID);
		List<Purchase> purchases = dao.getPurchasesByCompany(companyID);
		prepareForPresentation(purchases, UserType.ADMIN);
		return purchases;
	}
	
	/*
	 * Deletes a purchase from the DB.
	 * Purchased coupon's amount will be restored accordingly.
	 * In case restoration process was failed the purchase will not be deleted.
	 */
	public void deletePurchse(long id) throws ApplicationException{
		
		try {
			//Restoring coupon's amount.
			//In case restoration was failed an exception will be thrown.
			Purchase purchase = dao.getPurchase(id);
			Coupon purchasedCoupon = couponsController.getCoupon(purchase.getCouponID());
			purchasedCoupon.setAmount(purchasedCoupon.getAmount() + purchase.getAmount());
			couponsController.updateCoupon(purchasedCoupon);
		}
		catch(ApplicationException e) {
			throw new ApplicationException("PurhcasesController.deletePurchase() could'nt restore coupon's amount. Given ID: " +id, ErrorTypes.AMOUNT_RESTORING_ERROR, e);
		}
		
		dao.deletePurchase(id);
	}
	
	//PRIVATE-METHODS
	
	/*
	 * An assumption - all coupons in the DB are valid, thanks to the CouponsValidator class.
	 * In case there is no coupon associated with couponID, couponsController.getCoupon(couponID) will throw an ApplicationException.
	 */
	private void validateAddPurchase(int amount, long couponID) throws ApplicationException{
		if(amount < 1) {
			throw new ApplicationException(ErrorTypes.INVALID_AMOUNT_ERROR);
		}
		Coupon coupon = couponsController.getCoupon(couponID);
		if(coupon.getAmount() < amount) {
			throw new ApplicationException(ErrorTypes.OUT_OF_STOCK_ERROR);
		}
	}
	
	private void prepareForPresentation(Purchase purchase, UserType type) throws ApplicationException{
		long couponID = purchase.getCouponID();
		Coupon coupon = couponsController.getCoupon(couponID);
		purchase.setCouponTitle(coupon.getTitle());
		purchase.setTotalPrice( purchase.getAmount() * coupon.getPrice() );
		if( ! type.equals(UserType.COMPANY)) {
			long companyID = coupon.getCompanyID();
			Company company = companiesController.getCompany(companyID);
			purchase.setCompanyName(company.getName());
		}
		if( ! type.equals(UserType.ADMIN)) {
			purchase.setUserID(0);
			purchase.setId(0);
		}
		
	}
	
	private void prepareForPresentation(List<Purchase> purchases, UserType type) throws ApplicationException{
		for( Purchase purchase : purchases) {
			prepareForPresentation(purchase, type);
		}
	}
	
}
