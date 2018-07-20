/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class DrugStoreConfig extends ParameterizableViewController {	
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ConceptService cs;
		ModelAndView mav = new ModelAndView();
		
		cs = Context.getConceptService();
		List<Drug> drugs = cs.getAllDrugs();
		
		
		mav.addObject("drugs", drugs);
		return mav;
	}
}
