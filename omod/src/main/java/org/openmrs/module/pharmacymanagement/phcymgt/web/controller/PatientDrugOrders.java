package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugLotDate;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.PharmacyConstants;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class PatientDrugOrders extends ParameterizableViewController {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(this.getClass());

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();

		Drug drug = null;
		Location dftLoc = null;
		LocationService locationService = Context.getLocationService();

		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
		} catch (Exception e) {
			mav.addObject("msg", "pharmacymanagement.missingDftLoc");
		}
		@SuppressWarnings("unused")
		String pharmacyId = null;
		DrugLotDate dld = null;
		List<DrugLotDate> dlds = new ArrayList<DrugLotDate>();
		String patientIdStr = null;
		Patient patient = null;
		Person person = null;
		DrugOrderService service = Context.getService(DrugOrderService.class);
		List<Pharmacy> pharmacyList = service.getPharmacyByLocation(dftLoc);
		List<DrugOrder> drugOrders = new ArrayList<DrugOrder>();
		ConceptService conceptService = Context.getConceptService();
				
		Pharmacy pharmacy = null;

		List<DrugOrder> drugOrderList = new ArrayList<DrugOrder>();
		pharmacyId = pharmacyList.size() > 0 ? "not empty" : "";

		if (request.getParameter("patientId") != null
				&& !request.getParameter("patientId").equals("")) {
			patientIdStr = request.getParameter("patientId");

			patient = Context.getPatientService().getPatient(
					Integer.valueOf(patientIdStr));
		}

		if (patient != null && request.getParameter("pharmacyId") != null
				&& !request.getParameter("pharmacyId").equals("")) {

			drugOrders = Context.getOrderService().getDrugOrdersByPatient(
					patient);

			pharmacy = service.getPharmacyById(Integer.valueOf(request
					.getParameter("pharmacyId")));
			List<Integer> drugIdList = new ArrayList<Integer>();
			for (DrugOrder dor : drugOrders) {
				if (dor.getDiscontinued() == false) {
					drugOrderList.add(dor);
				}
			}

			Set<DrugProduct> lotNos = new HashSet<DrugProduct>();
			int solde = 0;
			Map<String, String> availNotAvailOrderedDrug = new HashMap<String, String>();


			List<Integer> drugIdss = new ArrayList<Integer>();
			List<DrugOrder> drugOrders1 = new ArrayList<DrugOrder>();
			for (DrugOrder drOr : drugOrderList) {
			//	if (!drugIdss.contains(drOr.getDrug().getDrugId()) && drOr.getAutoExpireDate() == null) {
				if (!(drOr.getAutoExpireDate() != null || drOr.getDiscontinuedDate()!=null)) {

					dld = new DrugLotDate();
					drugOrders1.add(drOr);
					drugIdList.add(drOr.getDrug().getDrugId());
					dld.setDrugOrder(drOr);
					drug = conceptService.getDrug(drOr.getDrug().getDrugId());
					dld.setDrug(drug);
					Map<String, String> dpMap = new HashMap<String, String>();
					lotNos = Utils.getLotsExpDp(null, drOr.getDrug().getDrugId()+"", null, pharmacy.getPharmacyId()+"");
					
					/**
					 * TO DO
					 * change this list of lotNos by the testLots in the Utils class
					 * remember to retrieve a list of list instead of a unique set!!!!!!!!!!!!!!!!
					 * 
					 */
				
					if (lotNos.size() > 0) {
						for (DrugProduct drugproduct : lotNos) {
							if (drug != null)
								solde = service.getCurrSoldeDisp(drug
										.getDrugId()
										+ "", null, pharmacy.getPharmacyId()
										+ "", drugproduct.getExpiryDate() + "", drugproduct
										.getLotNo(), null);
							
							if(solde != 0) {
								dpMap.put(drugproduct.getLotNo() + " / " + solde + " (" + drugproduct.getExpiryDate() + ") ", drugproduct.getDrugproductId() + "");								
								dld.setDpMap(dpMap);
								availNotAvailOrderedDrug.put(drugproduct.getDrugId().getName().toString(),"Available");
							}
						}

						if(dpMap.size() > 0)
							dlds.add(dld);
					}

				}
				drugIdss.add(drOr.getDrug().getDrugId());

				if(!availNotAvailOrderedDrug.containsKey(drOr.getDrug().getName().toString())) {
					availNotAvailOrderedDrug.put(drOr.getDrug().getName().toString(),"Not Available");
				}
			}

			mav.addObject("patient", patient);
			mav.addObject("availNotAvailOrderedDrug", availNotAvailOrderedDrug);

		}

		if (dlds.size() != 0) {
			mav.addObject("dlds", dlds);
		}

		List<Object[]> lots = null;
		DrugProduct drugproduct = null;
		if (request.getParameter("drugproductId") != null
				&& !request.getParameter("drugproductId").equals("")) {
			drugproduct = service.getDrugProductById(Integer.valueOf(request
					.getParameter("drugproductId")));
			if (drugproduct != null) {
				if(drugproduct.getCmddrugId() != null) {
					if (drugproduct.getCmddrugId().getDestination().getLocationId() == dftLoc.getLocationId()) {
						if (drugproduct.getDrugId() != null)
							lots = service.getLotNumbersExpirationDates(drugproduct
									.getDrugId().getDrugId()
									+ "", null, dftLoc.getLocationId() + "", null);
						else
							lots = service.getLotNumbersExpirationDates(null,
									drugproduct.getConceptId().getConceptId() + "",
									dftLoc.getLocationId() + "", null);
					}
				} else {
					if (service.getReturnStockByDP(drugproduct).get(0).getDestination().getLocationId() == dftLoc.getLocationId()) {
						if (drugproduct.getDrugId() != null)
							lots = service.getLotNumbersExpirationDates(drugproduct
									.getDrugId().getDrugId()
									+ "", null, dftLoc.getLocationId() + "", null);
						else
							lots = service.getLotNumbersExpirationDates(null,
									drugproduct.getConceptId().getConceptId() + "",
									dftLoc.getLocationId() + "", null);
					}
				}
			}
			mav.addObject("lots", lots);
		}

		if (request.getParameter("dpFromGet") != null
				&& !request.getParameter("dpFromGet").equals("")) {
			DrugProduct dp = service.getDrugProductById(Integer.valueOf(request
					.getParameter("dpFromGet")));
			String dateStr = dp.getExpiryDate().toString();
			String[] dateArr = dateStr.split("-");
			String date = dateArr[2] + "/" + dateArr[1] + "/" + dateArr[0];
			mav.addObject("date", date);
		}

		mav.setViewName(getViewName());
		return mav;
	}
}