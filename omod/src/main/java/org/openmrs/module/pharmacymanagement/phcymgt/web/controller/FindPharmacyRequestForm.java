/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.CmdDrug;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class FindPharmacyRequestForm extends ParameterizableViewController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DrugOrderService service = null;
		ModelAndView mav = new ModelAndView();
		service = Context.getService(DrugOrderService.class);
		
		LocationService locationService = Context.getLocationService();
		
		Location dftLoc = null;
		List<Pharmacy> pharmacyList = null;

		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
			
			pharmacyList = service.getPharmacyByLocation(dftLoc);
		} catch (Exception e) {
			mav
					.addObject(
							"msg",
							"pharmacymanagement.missingDftLoc");
		}
		
		String month = "";
		if (request.getParameter("on") != null) {
			if (request.getParameter("month") != null
					&& !request.getParameter("month").equals("")) {
				String monthParam = request.getParameter("month");
				String[] monthArray = monthParam.split("/");
				month = monthArray[2] + "-" + monthArray[1] + "-"
						+ monthArray[0];
			}
			Collection<CmdDrug> orders = service.findOrdersByLocSupProgMonth(
					dftLoc.getLocationId()+"", request
							.getParameter("supporter"), month, request.getParameter("pharmacyId"), "DISPENSE");
			mav.addObject("orders", orders);
		}

		
		mav.addObject("pharmacies", pharmacyList);
		mav.setViewName(getViewName());

		return mav;
	}
}
