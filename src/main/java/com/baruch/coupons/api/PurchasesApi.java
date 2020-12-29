package com.baruch.coupons.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baruch.coupons.beans.Purchase;
import com.baruch.coupons.beans.UserLoginData;
import com.baruch.coupons.exceptions.ApplicationException;
import com.baruch.coupons.logic.PurchasesController;

@RestController
@RequestMapping("/purchases")
public class PurchasesApi {
	
	@Autowired
	private PurchasesController controller;
	
	public PurchasesApi() {
		
	}
	
	@PostMapping
	public void addPurchase(@RequestBody Purchase purchase, @RequestAttribute("UserLoginData") UserLoginData userData) throws ApplicationException {
		purchase.setUserID(userData.getId());
		controller.addPurchase(purchase);
	}
	
	@DeleteMapping("/{purchaseID}")
	public void deletePurchase(@PathVariable("purchaseID") long id) throws ApplicationException{
		controller.deletePurchse(id);
	}
	
	@GetMapping
	public List<Purchase> getAllPurchases(@RequestAttribute("UserLoginData") UserLoginData userData) throws ApplicationException{
		return controller.getAllPurchases(userData);
	}
	
	@GetMapping("/{purchaseID}")
	public Purchase getPurchase(@PathVariable("purchaseID") long id) throws ApplicationException{
		return controller.getPrchase(id);
	}
	
	@GetMapping("/byUser")
	public List<Purchase> getPurchasesByUser(@RequestParam("userID") long userID) throws ApplicationException {
		return controller.getPurchasesByUser(userID);
	}
	
	@GetMapping("/byCompany")
	public List<Purchase> getPurchaseByCompany(@RequestParam("companyID") long companyID) throws ApplicationException{
		return controller.getPurchasesByCompany(companyID);
	}
	
	
}
