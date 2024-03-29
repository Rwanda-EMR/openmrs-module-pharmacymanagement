package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.CmdDrug;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class CmdSearch extends ParameterizableViewController {	

	@Override
	@RequestMapping(value = "module/pharmacymanagement/cmdsearch.form", method = RequestMethod.POST)
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		DrugOrderService service = Context.getService(DrugOrderService.class);
		LocationService locationService = Context.getLocationService();
		Collection<CmdDrug> orders = null;
		String month = "";

		Location dftLoc = null;
		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
		} catch (Exception e) {
			mav
					.addObject(
							"msg",
							"pharmacymanagement.missingDftLoc");
		}


		if (request.getParameter("on") != null) {
			if (request.getParameter("month") != null
					&& !request.getParameter("month").equals("")) {
				String monthParam = request.getParameter("month");
				String[] monthArray = monthParam.split("/");
				if(Context.getLocale().toString().equals("en_US") || Context.getLocale().toString().equals("en")) {
					month = monthArray[2] + "-" + monthArray[0] + "-"
							+ monthArray[1];
				}
				else {
					month = monthArray[2] + "-" + monthArray[1] + "-"
							+ monthArray[0];
				}
			}
				orders = service.findOrdersByLocSupProgMonth(
					request.getParameter("fosaName"), request
							.getParameter("supporter"), month, null, "STORE");

			Collection<CmdDrug> cmds=new ArrayList<CmdDrug>();
			//if (request.getParameter("status").equals("completed") || request.getParameter("status").equals("approved")){
			if (request.getParameter("status").equals("completed")){
			for (CmdDrug cmd:orders) {
					if(cmd.getIsAchieved()){
						cmds.add(cmd);
					}
				}
			}
			//else if (request.getParameter("status").equals("incomplete") || request.getParameter("status").equals("not approved")){
			else if (request.getParameter("status").equals("incomplete")){
			for (CmdDrug cmd:orders) {
					if(!cmd.getIsAchieved()){
						cmds.add(cmd);
					}
				}
			}
			else {
				cmds=orders;
			}


			mav.addObject("orders", cmds);
		}
		mav.addObject("dftLoc", dftLoc);
		mav.setViewName(getViewName());

		return mav;
	}
}
