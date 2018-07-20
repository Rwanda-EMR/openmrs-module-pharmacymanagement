package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.view.extn.AjaxSoldeRenderer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class DrugOrderSoldeHandler extends ParameterizableViewController {
	protected AjaxSoldeRenderer drugSoldeViewRenderer;
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Drug> model = new HashMap<String, Drug>();
		String drugIdParam = request.getParameter("drugId");
		if(drugIdParam != null && !drugIdParam.equals("")) {
			
		}
		Drug drug = Context.getConceptService().getDrug(Integer.valueOf(drugIdParam));
		model.put("source", drug);
		return new ModelAndView(drugSoldeViewRenderer, model);
	}
	/**
	 * @return the drugSoldeViewRenderer
	 */
	public AjaxSoldeRenderer getDrugSoldeViewRenderer() {
		return drugSoldeViewRenderer;
	}
	/**
	 * @param drugSoldeViewRenderer the drugSoldeViewRenderer to set
	 */
	public void setDrugSoldeViewRenderer(AjaxSoldeRenderer drugSoldeViewRenderer) {
		this.drugSoldeViewRenderer = drugSoldeViewRenderer;
	}

}
