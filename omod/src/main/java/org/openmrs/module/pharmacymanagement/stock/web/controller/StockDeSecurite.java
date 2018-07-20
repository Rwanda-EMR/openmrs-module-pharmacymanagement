/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugProductInventory;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;


/**
 *
 */
public class StockDeSecurite extends ParameterizableViewController {
	
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Location location;
		ModelAndView mav = new ModelAndView();
		
		Location dftLoc = null;
		LocationService locationService = Context.getLocationService();
		Collection<DrugProductInventory> drugProInv = null;
		Drug drug = null;
		Concept concept = null;
		ConceptService conceptService = Context.getConceptService();
		
		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
			mav.addObject("dftLoc", dftLoc);
		} catch (Exception e) {
			mav.addObject("msg", "pharmacymanagement.missingDftLoc");
		}
		
		DrugOrderService service = Context.getService(DrugOrderService.class);
		String drugId = "";
		String conceptId = "";
		String locationId = "";
		if(request.getParameter("locationId") != null && !(request.getParameter("locationId").equals(""))) {
			locationId = request.getParameter("locationId");
			location = Context.getLocationService().getLocation(Integer.valueOf(locationId));
			if(request.getParameter("drugId") != null && !(request.getParameter("drugId").equals(""))) {
				drugId = request.getParameter("drugId");	
				drug = conceptService.getDrug(Integer.valueOf(drugId));
				drugProInv = service.getDrugInventoryByDrugId(drugId, null, locationId, null);
			}
			if(request.getParameter("conceptId") != null && !(request.getParameter("conceptId").equals(""))) {
				conceptId = request.getParameter("conceptId");	
				concept = conceptService.getConcept(Integer.valueOf(conceptId));
				drugProInv = service.getDrugInventoryByDrugId(null, conceptId, locationId, null);
			}
			mav.addObject("drugProInv", drugProInv);
			mav.addObject("location", location);
			mav.addObject("drug", drug);
			mav.addObject("concept", concept);
		}
			
		
		mav.setViewName(getViewName());
		return mav;
	}
}
