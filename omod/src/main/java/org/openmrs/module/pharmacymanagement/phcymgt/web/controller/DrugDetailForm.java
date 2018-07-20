/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugDetails;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class DrugDetailForm extends ParameterizableViewController {
		
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Drug drug;
		ConceptService conceptService;
		DrugDetails drugDetails = null;
		ModelAndView mav = new ModelAndView();
		conceptService = Context.getConceptService();
		DrugOrderService service = Context.getService(DrugOrderService.class);
		
		if(request.getParameter("drugDetailId") != null && !request.getParameter("drugDetailId").equals("")) {
			drugDetails = service.getDrugDetailsById(Integer.valueOf(request.getParameter("drugDetailId")));
						
			mav.addObject("drugDetails", drugDetails);
		}
		
		if (request.getParameter("drug") != null
				&& !request.getParameter("drug").equals("")
				&& request.getParameter("forme") != null
				&& !request.getParameter("forme").equals("")
				&& request.getParameter("m_unit") != null
				&& !request.getParameter("m_unit").equals("")
				&& request.getParameter("units") != null
				&& !request.getParameter("units").equals("")) {
			if(drugDetails == null)
				drugDetails = new DrugDetails();
			
			drug = conceptService.getDrug(Integer.valueOf(request.getParameter("drug")));
			
			drugDetails.setDrug(drug);
			drugDetails.setForme(request.getParameter("forme"));
			drugDetails.setUnits(Integer.valueOf(request.getParameter("units")));
			drugDetails.setMeasurementUnit(request.getParameter("m_unit"));
			
			service.saveDrugDetails(drugDetails);
			mav.addObject("msg", "The Drug details are saved successful!");
		}
		mav.setViewName(getViewName());
		return mav;
	}
}
