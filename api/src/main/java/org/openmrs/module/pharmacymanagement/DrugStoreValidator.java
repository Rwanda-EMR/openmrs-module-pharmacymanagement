package org.openmrs.module.pharmacymanagement;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DrugStoreValidator implements Validator {

	
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return DrugStore.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors err) {
		DrugStore drugstore = (DrugStore)obj;
//    	Date now = new Date();
    	    	
    	if(drugstore.getCmdDrug().getLocationId() == null) {
    		err.rejectValue("cmdDrug.fosaName", "drugstoremgt.emptyLocation");
    	}
    	
//    	if(drugstore.getCmdDrug().getMonthPeriod() == null) {
//    		err.rejectValue("cmdDrug.month", "drugstoremgt.emptyMonth");
//    	}
    	
    	if(drugstore.getCmdDrug().getSupportingProg() == null) {
    		err.rejectValue("cmdDrug.supportingProg", "drugstoremgt.emptyFund");
    	}
    		
    	
//    	if(drugstore.getDrugProduct().getExpiryDate() != null && drugstore.getDrugProduct().getExpiryDate().before(now)) {
//    		err.rejectValue("expiryDate", "drugstoremgt.late");
//    	}
//    	ValidationUtils.rejectIfEmpty(err, "qntyReq", "drugstoremgt.emptyQntyNeeded");
//    	ValidationUtils.rejectIfEmpty(err, "deliveredQnty", "drugstoremgt.emptyGivenQnty");
//    	ValidationUtils.rejectIfEmpty(err, "lotNo", "drugstoremgt.emptyLotNum");
//    	ValidationUtils.rejectIfEmpty(err, "storeQnty", "drugstoremgt.emptyActualQnty");
		
	}

}

