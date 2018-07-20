package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.openmrs.module.pharmacymanagement.view.extn.AjaxViewRenderer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class ConceptDrugHandler
		extends ParameterizableViewController
{
	protected AjaxViewRenderer viewRenderer = null;

	public ConceptDrugHandler() {}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, List<?>> model = new HashMap();

		ConceptService conceptService = Context.getConceptService();

		Concept concept = null;
		List<Concept> drugConcepts = null;
		List<Drug> drugs = null;


		String patientId = null;

		if ((request.getParameter("patientId") != null) &&
				(!request.getParameter("patientId").equals(""))) {
			patientId = request.getParameter("patientId");
		}

		if ((request.getParameter("medSet") != null) &&
				(!request.getParameter("medSet").equals(""))) {
			concept = conceptService.getConcept(Integer.valueOf(request
					.getParameter("medSet")));
			drugConcepts = conceptService.getConceptsByConceptSet(concept);
			model.put("source", drugConcepts);
			return new ModelAndView(viewRenderer, model);
		}
		if ((request.getParameter("drugConcept") != null) &&
				(!request.getParameter("drugConcept").equals(""))) {
			concept = conceptService.getConcept(Integer.valueOf(request
					.getParameter("drugConcept")));
			drugs = conceptService.getDrugsByConcept(concept);
			List<Drug> drugList = Utils.sortDrugs(drugs);
			model.put("source", drugList);
			return new ModelAndView(viewRenderer, model);
		}
		return new ModelAndView(getViewName(), model);
	}



	public AjaxViewRenderer getViewRenderer()
	{
		return viewRenderer;
	}




	public void setViewRenderer(AjaxViewRenderer viewRenderer)
	{
		this.viewRenderer = viewRenderer;
	}
}