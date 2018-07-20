/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Location;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *@author Dusabe E.
 */
public class PharmacyFormController extends ParameterizableViewController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		LocationService locationService = Context.getLocationService();
		ConceptService cs = Context.getConceptService();
		Location dftLoc = null;
		DrugOrderService service = Context.getService(DrugOrderService.class);
		Pharmacy pharmacy = null;
		Collection<ConceptAnswer> services = cs.getConcept(6702).getAnswers();
		Collection<Pharmacy> pharmacies = service.getAllPharmacies();

		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
		} catch (Exception e) {
			mav.addObject("msg", "pharmacymanagement.missingDftLoc");
		}
		
		if (request.getParameter("pharmacyId") != null
				&& !request.getParameter("pharmacyId").equals("")) {
			pharmacy = service.getPharmacyById(Integer.valueOf(request
						.getParameter("pharmacyId")));
			System.out.println("Pharmacy Name: "+pharmacy.getName());
		} else {
			pharmacy = new Pharmacy();
		}

		if (request.getParameter("phcyName") != null
				&& !request.getParameter("phcyName").equals("")
				&& request.getParameter("location") != null
				&& !request.getParameter("location").equals("")) {
			String phcyName = request.getParameter("phcyName");
			int locationId = Integer.valueOf(request.getParameter("location"));
			
			Location location = Context.getLocationService().getLocation(
					locationId);
			
			pharmacy.setName(phcyName);
			pharmacy.setLocationId(location);

			service.savePharmacy(pharmacy);
			mav.addObject("msg", "pharmacymanagement.pharmacy.saved");
			pharmacy = null;
		}
		

		mav.addObject("phcy", pharmacy);
		mav.addObject("services", services);
		mav.addObject("location", dftLoc);
		mav.setViewName(getViewName());

		return mav;
	}
}
