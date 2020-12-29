package com.baruch.coupons.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baruch.coupons.beans.Coupon;
import com.baruch.coupons.beans.UserLoginData;
import com.baruch.coupons.enums.Categories;
import com.baruch.coupons.exceptions.ApplicationException;
import com.baruch.coupons.logic.CouponsController;

@RestController
@RequestMapping("/coupons")
public class CouponsApi {

	@Autowired
	private CouponsController controller;

	public CouponsApi() {

	}

	@PostMapping
	public void createCoupon(@RequestBody Coupon coupon, @RequestAttribute("UserLoginData") UserLoginData userData) throws ApplicationException{

		coupon.setCompanyID(userData.getCompanyID());

		controller.createCoupon(coupon);
	}

	@PutMapping
	public void updateCoupon(@RequestBody Coupon coupon, @RequestAttribute("UserLoginData") UserLoginData userData) throws ApplicationException{

		coupon.setCompanyID(userData.getCompanyID());

		controller.updateCoupon(coupon);
	}

	@DeleteMapping("/{couponID}")
	public void deleteCoupon(@PathVariable("couponID") long id) throws ApplicationException {
		controller.deleteCoupon(id);
	}

	@GetMapping
	public List<Coupon> getAllCoupons() throws ApplicationException{
		return controller.getAllCoupons();
	}

	@GetMapping("/{couponID}")
	public Coupon getCoupon(@PathVariable("couponID") long id) throws ApplicationException {
		return controller.getCoupon(id);
	}

	@GetMapping("/byCompany")
	public List<Coupon> getCouponsByCompany(@RequestParam("companyID") long companyID) throws ApplicationException {
		return controller.getCouponsBycompany(companyID);
	}

	@GetMapping("/byCategory")
	public List<Coupon> getCouponsbyCategory(@RequestParam("category") Categories category) throws ApplicationException {
		return controller.getCouponsByCategory(category);
	}

	@GetMapping("/byMaxPrice")
	public List<Coupon> getCouponsByMaxPrice(@RequestParam("userID") long userID, @RequestParam("maxPrice") float maxPrice) throws ApplicationException{
		return controller.getPruchasedCouponsByMaxPrice(userID, maxPrice);
	}

}
