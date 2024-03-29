package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugLotDate;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.PharmacyConstants;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.openmrs.parameter.OrderSearchCriteria;
import org.openmrs.parameter.OrderSearchCriteriaBuilder;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class PatientDrugOrders extends ParameterizableViewController {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(this.getClass());

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		StopWatch sw = new StopWatch();
		sw.start();
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

			drugOrders = getDrugOrdersByPatient(patient);

			pharmacy = service.getPharmacyById(Integer.valueOf(request
					.getParameter("pharmacyId")));
			List<Integer> drugIdList = new ArrayList<Integer>();
			for (DrugOrder dor : drugOrders) {
				if (dor.isActive()) {
					drugOrderList.add(dor);
				}
			}
			Set<DrugProduct> lotNos = new HashSet<DrugProduct>();
			int solde = 0;
			Map<String, String> availNotAvailOrderedDrug = new HashMap<String, String>();


			List<Integer> drugIdss = new ArrayList<Integer>();
			List<DrugOrder> drugOrders1 = new ArrayList<DrugOrder>();

			int numLotsChecked = 0;
			long timeCheckingLots = 0;

			for (DrugOrder drOr : drugOrderList) {

			//	if (!drugIdss.contains(drOr.getDrug().getDrugId()) && drOr.getAutoExpireDate() == null) {
				//if ((!(drOr.getAutoExpireDate() != null || drOr.getEffectiveStopDate()!=null)) && drOr.getOrderType().getOrderTypeId()==PharmacyConstants.DRUG_ORDER_TYPE) {
				if (drOr.getDispenseAsWritten()==false && drOr.getOrderType().getOrderTypeId()==PharmacyConstants.DRUG_ORDER_TYPE) {

					dld = new DrugLotDate();
					drugOrders1.add(drOr);
					drugIdList.add(drOr.getDrug().getDrugId());
					dld.setDrugOrder(drOr);
					drug = conceptService.getDrug(drOr.getDrug().getDrugId());
					dld.setDrug(drug);
					Map<String, String> dpMap = new HashMap<String, String>();
					lotNos = service.getDrugProducts(pharmacy, drOr.getDrug());
					/**
					 * TO DO
					 * change this list of lotNos by the testLots in the Utils class
					 * remember to retrieve a list of list instead of a unique set!!!!!!!!!!!!!!!!
					 * 
					 */
				
					if (lotNos.size() > 0) {
						for (DrugProduct drugproduct : lotNos) {
							if (drug != null) {
								numLotsChecked++;
								long startTime = System.currentTimeMillis();
								solde = service.getCurrSoldeDisp(
										drug.getDrugId() + "",
										null,
										pharmacy.getPharmacyId() + "",
										(drugproduct.getExpiryDate() == null ? "" : drugproduct.getExpiryDate() + ""),
										drugproduct.getLotNo(),
										null
								);
								long endTime = System.currentTimeMillis();
								timeCheckingLots += (endTime-startTime);
							}
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
				//drugIdss.add(drOr.getDrug().getDrugId());

				if(drOr.getDrug()!=null && !availNotAvailOrderedDrug.containsKey(drOr.getDrug().getName().toString()) ) {
					if (drOr.getOrderType().getOrderTypeId()==PharmacyConstants.DRUG_ORDER_TYPE)
					availNotAvailOrderedDrug.put(drOr.getDrug().getName().toString(),"Not Available (OrderId: "+drOr.getOrderId()+", Date: "+drOr.getEffectiveStartDate()+", Orderer: "+drOr.getOrderer().getName()+")");
				}
			}

			log.debug("number of lots checked: " + numLotsChecked);
			log.debug("time spent checking lot numbers: " + (int)(timeCheckingLots/1000) + " seconds");

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
			if (drugproduct != null && !drugproduct.getTransferType().equalsIgnoreCase("adjustment")) {
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
			if (drugproduct != null && drugproduct.getTransferType().equalsIgnoreCase("adjustment")) {
				lots=service.getLotNumberByDrugProductId(drugproduct.getDrugproductId());

			}
				mav.addObject("lots", lots);
		}

		if (request.getParameter("dpFromGet") != null
				&& !request.getParameter("dpFromGet").equals("")) {
			DrugProduct dp = service.getDrugProductById(Integer.valueOf(request
					.getParameter("dpFromGet")));
			String dateStr = dp.getExpiryDate().toString();
			String[] dateArr = dateStr.split("-");
			String date;

			if(Context.getLocale().toString().equals("en_US") || Context.getLocale().toString().equals("en")) {
				date = dateArr[1] + "/" + dateArr[2] + "/" + dateArr[0];
			}
			else {
				date = dateArr[2] + "/" + dateArr[1] + "/" + dateArr[0];
			}
			mav.addObject("date", date);
		}

		sw.stop();
		log.debug("patient drug orders loaded in: " + sw);

		mav.setViewName(getViewName());
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	public List<DrugOrder> getDrugOrdersByPatient(Patient patient){

		//OrderType drugOrderType = Context.getOrderService().getOrderTypeByUuid(OrderType.DRUG_ORDER_TYPE_UUID);
		OrderService orderService = Context.getOrderService();

		OrderType drugOrderType = orderService
				.getOrderType(PharmacyConstants.DRUG_ORDER_TYPE);
		OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteriaBuilder().setPatient(patient)
				.setOrderTypes(Collections.singletonList(drugOrderType)).build();

		List<DrugOrder> allDrugOrder=new ArrayList<DrugOrder>();
		for (Order dro:Context.getOrderService().getOrders(orderSearchCriteria)) {
			if(dro instanceof DrugOrder && dro.getPreviousOrder()==null && dro.isActive()==true){
				allDrugOrder.add((DrugOrder) dro);
			}
		}
return allDrugOrder;
		//return (List)Context.getOrderService().getOrders(orderSearchCriteria);
	}
}