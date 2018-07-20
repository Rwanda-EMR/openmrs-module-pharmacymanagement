/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugDetails;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class DrugDetailView extends ParameterizableViewController {
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		DrugOrderService service = Context.getService(DrugOrderService.class);
		
		Collection<DrugDetails> drugDetails = service.getAllDrugDetails();
		
		mav.addObject("drugDetails", drugDetails);
		mav.setViewName(getViewName());
		return mav;
	}
}
