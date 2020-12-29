package com.baruch.coupons.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baruch.coupons.beans.Company;
import com.baruch.coupons.exceptions.ApplicationException;
import com.baruch.coupons.logic.CompaniesController;

@RestController
@RequestMapping("/companies")
public class CompaniesApi {
	
	@Autowired
	private CompaniesController controller;
	
	public CompaniesApi() {
		
	}
	
	@PostMapping
	public void createCompany(@RequestBody Company company) throws ApplicationException {
		controller.createCompany(company);
	}
	
	@PutMapping
	public void updateCompany(@RequestBody Company company) throws ApplicationException {
		controller.updateCompany(company);
	}
	
	@DeleteMapping("/{companyID}")
	public void deleteCompany(@PathVariable("companyID") long id) throws ApplicationException {
		controller.deleteCompany(id);
	}
	
	@GetMapping("/{companyID}")
	public Company getCompany(@PathVariable("companyID") long id) throws ApplicationException {
		return controller.getCompany(id);
	}
	
	@GetMapping
	public List<Company> getAllCompanies() throws ApplicationException{
		return controller.getAllcompanies();
	}
	
	
}
