package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.view.extn.AjaxViewRenderer;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class JSonHandler extends ParameterizableViewController {

	protected AjaxViewRenderer viewRenderer;
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView mav = new ModelAndView();
		Map<String, List<?>> model = new HashMap<String, List<?>>();
		List<Drug> drugs = new ArrayList<Drug>();
		List<Concept> concepts = new ArrayList<Concept>();
		List<DrugProduct> drugproducts = new ArrayList<DrugProduct>();
		
		LocationService ls = Context.getLocationService();
		Pharmacy pharmacy = null;
		String retType = null;
		Location dftLoc = null;
		DrugOrderService dos = Context.getService(DrugOrderService.class);
		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		List<DrugProduct> drugProducts = (List<DrugProduct>) dos
				.getAllProducts();
		List<DrugProduct> drugProducts1 = new ArrayList<DrugProduct>();
		Map<Integer, Drug> storeDrugMap = new HashMap<Integer, Drug>();
		Map<Integer, DrugProduct> pharmaDrugMap = new HashMap<Integer, DrugProduct>();
		Map<Integer, DrugProduct> pharmaConceptMap = new HashMap<Integer, DrugProduct>();

		try {
			dftLoc = ls.getLocation(Integer.valueOf(locationStr));
		} catch (Exception e) {
			mav.addObject("msg", "pharmacymanagement.missingDftLoc");
		}

		if (request.getParameter("serviceId") != null
				&& !request.getParameter("serviceId").equals("")) {
			for (DrugProduct dps : drugProducts) {
				try {
					if (dps.getCmddrugId() != null)
						if (dps.getCmddrugId().getPharmacy() != null) {
							drugProducts1.add(dps);
						}
				} catch (NullPointerException npe) {
				}
			}
			for (DrugProduct drPr : drugProducts1) {
				if (drPr.getConceptId() != null) {
					if (drPr.getCmddrugId().getPharmacy() != null
							&& dos.getCurrSoldeDisp(null, drPr.getConceptId()
									.getConceptId()
									+ "", request.getParameter("serviceId"),
									drPr.getExpiryDate() + "", drPr.getLotNo(),
									null) > 0) {
//						concepts.add(drPr.getConceptId());
//						pharmaConceptMap.put(drPr.getConceptId().getConceptId(), drPr);
						drugproducts.add(drPr);
					}
				}
			}
			model.put("source", drugproducts);
			return new ModelAndView(viewRenderer, model);		
		}

		if (request.getParameter("fromId") != null
				&& !request.getParameter("fromId").equals("")
				&& request.getParameter("toId") != null
				&& !request.getParameter("toId").equals("")
				&& request.getParameter("retType") != null
				&& !request.getParameter("retType").equals("")) {

			for (DrugProduct dps : drugProducts) {
				try {
					if (dps.getCmddrugId() != null)
						if (dps.getCmddrugId().getPharmacy() != null) {
							drugProducts1.add(dps);
						}
				} catch (NullPointerException npe) {
				}
			}
			for (DrugProduct drPr : drugProducts) {
				if (drPr.getDrugId() != null) {
					if (dos.getCurrSolde(drPr.getDrugId().getDrugId() + "",
							null, dftLoc.getLocationId() + "", drPr
									.getExpiryDate()
									+ "", drPr.getLotNo(), null) > 0)
						storeDrugMap.put(drPr.getDrugId().getDrugId(), drPr
								.getDrugId());
					if (drPr.getCmddrugId() != null) {
						if (drPr.getCmddrugId().getPharmacy() != null
								&& dos.getCurrSoldeDisp(drPr.getDrugId()
										.getDrugId()
										+ "", null, drPr.getCmddrugId()
										.getPharmacy().getPharmacyId()
										+ "", drPr.getExpiryDate() + "", drPr
										.getLotNo(), null) > 0) {
							pharmaDrugMap.put(drPr.getDrugId().getDrugId(),
									drPr);
						}
					}
				}
			}
			retType = request.getParameter("retType");
			if (retType.equals("internal")) {
				pharmacy = dos.getPharmacyById(Integer.valueOf(request
						.getParameter("fromId")));
				for (Map.Entry entry : pharmaDrugMap.entrySet()) {
					if (pharmacy.equals(((DrugProduct) entry.getValue())
							.getCmddrugId().getPharmacy())) {
						drugs.add(((DrugProduct) entry.getValue()).getDrugId());
					}
				}
				model.put("source", drugs);
				return new ModelAndView(viewRenderer, model);

			} else {
				for (Map.Entry entry : storeDrugMap.entrySet()) {
					drugs.add((Drug) entry.getValue());
				}
				model.put("source", drugs);
				return new ModelAndView(viewRenderer, model);
			}
		}
		return new ModelAndView(getViewName(), model);
	}

	/**
	 * @return the viewRenderer
	 */
	public AjaxViewRenderer getViewRenderer() {
		return viewRenderer;
	}

	/**
	 * @param viewRenderer
	 *            the viewRenderer to set
	 */
	public void setViewRenderer(AjaxViewRenderer viewRenderer) {
		this.viewRenderer = viewRenderer;
	}

}
