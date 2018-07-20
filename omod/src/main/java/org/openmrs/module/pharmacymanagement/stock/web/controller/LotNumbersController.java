package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.view.extn.AjaxView;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class LotNumbersController extends ParameterizableViewController {
	protected final Log log = LogFactory.getLog(getClass());
	protected AjaxView av;
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		log.info("Returning ajax view");
		LocationService locationService = Context.getLocationService();
		Location dftLoc = null;

		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
		} catch (Exception e) {
			log.info("No Default Location Set");
		}

		Map<Integer, List<Object[]>> dpLot = new HashMap<Integer, List<Object[]>>();
		DrugOrderService service = Context.getService(DrugOrderService.class);

		if (request.getParameter("drugId") != null
				&& !request.getParameter("drugId").equals("")) {
			List<Object[]> obj = null;
			if(request.getParameter("drugId") != null)
				obj = service.getLotNumbersExpirationDates(request
					.getParameter("drugId"), null, dftLoc.getLocationId().toString(),
					null);
			
			//av = new AjaxView();
			
			dpLot.put(1, obj);
		}			
		return new ModelAndView(av);
	}

	/**
	 * @return the av
	 */
	public AjaxView getAv() {
		return av;
	}

	/**
	 * @param av the av to set
	 */
	public void setAv(AjaxView av) {
		this.av = av;
	}
	
	
}
