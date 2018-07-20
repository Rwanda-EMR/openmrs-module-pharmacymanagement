/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class PharmacyViewController extends ParameterizableViewController {

	private Log log = LogFactory.getLog(this.getClass());

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Collection<Pharmacy> pharmacies;
		ModelAndView mav = new ModelAndView();
		DrugOrderService service = Context.getService(DrugOrderService.class);

		if (request.getParameter("pharmacyId") != null
				&& !request.getParameter("pharmacyId").equals("")) {
			Pharmacy pharmacy = service.getPharmacyById(Integer.valueOf(request
					.getParameter("pharmacyId")));
			try {
				service.cancelPharmacy(pharmacy);
			} catch (Exception e) {
				log.error(e.getMessage());
				mav
						.addObject(
								"msg",
								"pharmacymanagement.noPharmacyDel");
			}
		}

		pharmacies = service.getAllPharmacies();

		mav.addObject("pharmacies", pharmacies);

		mav.setViewName(getViewName());
		return mav;
	}
}
