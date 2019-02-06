package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.LocationService;
import org.openmrs.api.ObsService;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohappointment.utils.AppointmentUtil;
import org.openmrs.module.pharmacymanagement.PharmacyConstants;
import org.openmrs.module.pharmacymanagement.DrugOrderPrescription;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.PharmacyInventory;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class PharmacyDrugDispController extends ParameterizableViewController {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(this.getClass());

	@SuppressWarnings({ "unused" })
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();

		int count = 1;
		Patient patient = null;
		Encounter encounter = null;
		User user = null;
		PatientService patientService = null;
		ConceptService conceptService = null;
		DrugOrderPrescription dop = null;
		PharmacyInventory pi = null;
		List<Drug> drugs = null;
		HttpSession httpSession = request.getSession();
		List<Pharmacy> pharmacyList = null;
		PatientProgram pp = null;
		PatientService ps = Context.getPatientService();
		OrderService orderService = Context.getOrderService();
		user = Context.getAuthenticatedUser();
		String pharmacyIdStr = null;
		ObsService obsService = Context.getObsService();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		DrugOrderService service = Context.getService(DrugOrderService.class);

		Location dftLoc = null;
		LocationService locationService = Context.getLocationService();

		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		String patientIdStr = request.getParameter("patientId");

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
			pharmacyList = service.getPharmacyByLocation(dftLoc);
		} catch (Exception e) {
			mav
					.addObject(
							"msg",
							"pharmacymanagement.missingDftLoc");
		}

		patientService = Context.getPatientService();
		conceptService = Context.getConceptService();
		
		drugs = conceptService.getAllDrugs();

		EncounterService encounterService = Context.getEncounterService();

		Map requestMap = request.getParameterMap();

		ArrayList<String> fieldNames = new ArrayList<String>();
		for (Object key : requestMap.keySet()) {
			String keyString = (String) key;
			fieldNames.add(keyString);
		}

		if (request.getParameter("dopId") != null
				&& !request.getParameter("dopId").equals("")) {
			dop = service.getDrugOrderPrescriptionById(Integer.valueOf(request
					.getParameter("dopId")));
			mav.addObject("dop", dop);
		}
		
		if(request.getParameter("patientId") != null && !request.getParameter("patientId").equals("")) {
			Person person = Context.getPersonService().getPerson(
					Integer.valueOf(patientIdStr));
			Obs obsWeight = obsService
					.getObs(Utils.biggestObsIdNubmer(obsService
							.getObservationsByPersonAndConcept(
									person,
									conceptService
											.getConcept(PharmacyConstants.WEIGHT))));
			mav.addObject("obsWeight", obsWeight);
		}
		
		
		

		if (request.getParameter("patientId") != null
				&& !request.getParameter("patientId").equals("")
				&& request.getParameter("encDate") != null
				&& !request.getParameter("encDate").equals("")) {

			patient = patientService.getPatient(Integer.valueOf(request
					.getParameter("patientId")));

			String dateStr = request.getParameter("encDate");
			String nvDateStr = null;
			Date nvDate = null;
			
			Date encDate = sdf.parse(dateStr);

			List<Obs> obsList = new ArrayList<Obs>();

			if(request.getParameter("weight") != null
					&& !request.getParameter("weight").equals("")) {

				Object weight = Double.valueOf(request.getParameter("weight"));

				Concept weightConcept = conceptService
						.getConcept(PharmacyConstants.WEIGHT);

				Obs weightObs = Utils.createObservation(encDate, dftLoc, patient,
						weightConcept, weight, 1);
				obsList.add(weightObs);
			}
			Concept nvDateConcept = conceptService
					.getConcept(PharmacyConstants.NEXT_VISIT_DATE);

			EncounterType encounterType = encounterService
					.getEncounterType(Utils
							.getGP("pharmacymanagement.pharmacyEncounter"));


			
			if(request.getParameter("nvDate") != null && !request.getParameter("nvDate").equals("")) {
				nvDateStr = request.getParameter("nvDate");
				nvDate = sdf.parse(nvDateStr);Obs nvDateObs = Utils.createObservation(encDate, dftLoc, patient,
						nvDateConcept, nvDate, 2);
				obsList.add(nvDateObs);
			}
			
			if(request.getParameter("pharmacy") != null && !request.getParameter("pharmacy").equals(""))
				pharmacyIdStr = request.getParameter("pharmacy");

			encounter = Utils.createEncounter(encDate, user, dftLoc, patient,
					encounterType, obsList);
			if (fieldNames.size() != 0) {
				for (String str : fieldNames) {
					if (str.contains("drug_")) {
						String countFields = str.substring(
								str.indexOf("_") + 1, str.lastIndexOf("_"));
						String id = str.substring(str.lastIndexOf("_") + 1);

						String drugSuffix = "drug_" + countFields + "_" + id;
						String dpSuffix = "dp_" + countFields;
						String dOSuffix = "do_" + countFields;

						if (drugSuffix.equals(str)) {
							if (request.getParameter(drugSuffix) != null
									&& !request.getParameter(drugSuffix)
											.equals("")
									&& request.getParameter(dpSuffix) != null
									&& !request.getParameter(dpSuffix).equals(
											"")) {
								int dpId = Integer.valueOf(request.getParameter(dpSuffix));
								DrugProduct drugProduct = service.getDrugProductById(dpId);
								
								DrugOrder drugOrder = orderService.getOrder(Integer.valueOf(request.getParameter(dOSuffix)), DrugOrder.class);
								Concept discontinueReason = conceptService.getConcept(1714);
								
								patient = patientService.getPatient(Integer
										.parseInt(request
												.getParameter("patientId")));
								dop = new DrugOrderPrescription();

								int quantity = Integer.valueOf(request
										.getParameter(drugSuffix));

								dop.setDate(encDate);
								dop.setQuantity(quantity);
								dop.setPatient(patient);
								dop.setUser(user);
								dop.setDrugproductId(drugProduct);

								// saving the the pharmacy inventory
								int currStat = service.getCurrSoldeDisp(id, null,
										pharmacyIdStr, drugProduct.getExpiryDate().toString(), drugProduct.getLotNo(), null);

								int solde = currStat - quantity;
								if (solde >= 0) {
									//auto expire the regimen to remove from the list which appears when dispensing what have been prescribed
									drugOrder.setAutoExpireDate(encDate);
									
									if (count == 1) {
										encounterService
												.saveEncounter(encounter);
									}

									dop.setEncounterId(encounter);
//									try {
										orderService.saveOrder(drugOrder);
										dop.setOrderId(drugOrder);
										service.saveDrugOrderPrescription(dop);
//									} catch (org.openmrs.api.APIException e) {
//										mav.addObject("msg", "You cannot dispense before prescription");
//									}				
									
									pi = new PharmacyInventory();
									pi.setDate(encDate);
									pi.setDrugproductId(drugProduct);
									pi.setEntree(0);
									pi.setSortie(quantity);
									pi.setSolde(solde);	

									pi.setDopId(dop);

									service.savePharmacyInventory(pi);

									mav
											.addObject("msg",
													"pharmacymanagement.drugorder.save");
									
									/**
									 * ________Here is the Appointment Stuff
									 */
									if(request.getParameter("appointmentId")!=null) {
										Utils.setPharmacyAppointmentAsAttended(
												AppointmentUtil.getWaitingAppointmentById(
														Integer.parseInt(request.getParameter("appointmentId"))));
										mav.addObject("appointmentId", request.getParameter("appointmentId"));
									}
								

								} else {
									httpSession
											.setAttribute(
													WebConstants.OPENMRS_ERROR_ATTR,
													"pharmacymanagement.pharmacy.noenough");
								}
								count++;
							}
						}
					}
				}
			}
		}
		
		if (patientIdStr != null && !patientIdStr.equals("")) {
			int patientId = Integer.parseInt(patientIdStr);
			mav.addObject("patient", Context.getPatientService().getPatient(patientId));
		}
		
		if(request.getParameter("serviceId") != null && !request.getParameter("serviceId").equals("")) {
			int serviceId = Integer.parseInt(request.getParameter("serviceId"));
			Concept pharmacy = conceptService.getConcept(serviceId);
			mav.addObject("pharmacy", pharmacy);
		}
		mav.addObject("drugs", drugs);
		mav.addObject("pharmacyList", pharmacyList);

		mav.setViewName(getViewName());

		return mav;
	}
}
