/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.util.ArrayListWrapper;
import org.openmrs.*;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.ObsService;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugOrderPrescription;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.PharmacyConstants;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.openmrs.parameter.OrderSearchCriteria;
import org.openmrs.parameter.OrderSearchCriteriaBuilder;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.controller.PortletController;

/**
 *
 */
public class DrugOrderPortletController extends PortletController {

	@Override
	protected void populateModel(HttpServletRequest request,
			Map<String, Object> model){
		ConceptService conceptService = Context.getConceptService();
		ObsService obsService = Context.getObsService();
		DrugOrderService service = Context.getService(DrugOrderService.class);
		OrderService orderService = Context.getOrderService();

		//List<Drug> drugs = Context.getConceptService().getAllDrugs(false);
		List<DrugProduct> drugProductList= (List<DrugProduct>) service.getAllProducts();

		List<Drug> drugs =new ArrayList<Drug>();
		for (DrugProduct dp:drugProductList) {

			if(dp.getDrugId()!=null && !drugs.contains(dp.getDrugId()) && !dp.getDrugId().getRetired()) {
				drugs.add(dp.getDrugId());
			}
		}





		Map<Integer, String> drugMap = new HashMap<Integer, String>();
		LocationService locationService = Context.getLocationService();
		ConceptClass cc = conceptService.getConceptClass(9);
		List<Concept> medSet = conceptService.getConceptsByClass(cc);
		List<Concept> concMedset = Utils.getMedsets(medSet);
		String[] possibleFrequency = Utils.getPossibleFrequencyFromGlobalProperty("pharmacymanagement.possibleFrequency");
		Concept insuranceTypeConcept = conceptService.getConcept(PharmacyConstants.RWANDA_INSURANCE_TYPE);
		Concept insuranceNumberConcept = conceptService.getConcept(PharmacyConstants.RWANDA_INSURANCE_NUMBER);

		Location dftLoc = null;
		Person person = null;
		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
			model.put("dftLoc", dftLoc);
		} catch (Exception e) {
			model.put("msg", "Missing Profile Default Location");
		}

		Patient patient = null;
		if (request.getParameter("patientId") != null
				&& !request.getParameter("patientId").equals("")) {

			/** Sending the patient ID after checking it is not null... (KAMONYO) */
			model.put("patientId", Integer.valueOf(request.getParameter("patientId")));
			/** ... */

			patient = Context.getPatientService().getPatient(
					Integer.valueOf(request.getParameter("patientId")));
			person = Context.getPersonService().getPerson(Integer.valueOf(request.getParameter("patientId")));
		}

		/**
		 * Checking whether the Appointment ID is not null at this level... (KAMONYO)
		 */

		if(request.getParameter("appointmentId")!=null)
			if(!request.getParameter("appointmentId").equals("")){
			model.put("appointmentId", request.getParameter("appointmentId"));
		}

		/**	... */

		List<Obs> insuranceTypeObsList = obsService.getObservationsByPersonAndConcept(person, insuranceTypeConcept);
		String insuranceType = insuranceTypeObsList.size() == 0 ? null : insuranceTypeObsList.get(insuranceTypeObsList.size() -1).getValueCoded().getName().getName();
		List<Obs> insuranceNumberObsList = obsService.getObservationsByPersonAndConcept(person, insuranceNumberConcept);
		String insuranceNumber = insuranceNumberObsList.size() == 0 ? null : insuranceNumberObsList.get(insuranceNumberObsList.size() -1 ).getValueText();


		List<DrugOrder> drugOrders = new ArrayList<DrugOrder>();
//		Collection<DrugProduct> dpList = dos.getAllProducts();
		if (patient != null) {
			drugOrders = getDrugOrdersByPatient(patient);
			model.put("patient", patient);
		}
		for(Drug drg : drugs) {
			if(drg.getName()!=null)
				drugMap.put(drg.getDrugId(), drg.getName().toString());
			else
				drugMap.put(drg.getDrugId(), drg.getConcept().getName().toString());

		}

//		System.out.println("The loop A Start: ********************* " + new Date());
//		for(Drug drug : drugs) {
//				currSolde = dos.getSoldeByToDrugLocation(new Date() + "", drug.getDrugId() + "", null,dftLoc.getLocationId() + "");				
//				if(currSolde > 0) {
//					drugMap.put(drug.getDrugId(), drug.getName()+" ["+currSolde+"]");
//				}
//		}		
//		System.out.println("The loop A End: ********************* " + new Date());


		Map<Date, List<DrugOrder>> map = new HashMap<Date, List<DrugOrder>>();

		Date dat1 = null;


		for(DrugOrder o : drugOrders) {
			List<DrugOrder> ordList = new ArrayList<DrugOrder>();
			for(DrugOrder o1 : drugOrders) {
				if(o1.getOrderType().getOrderTypeId()==PharmacyConstants.DRUG_ORDER_TYPE) {
					if (o1.getEffectiveStartDate() != null && o.getEffectiveStartDate() != null)
						if (o1.getEffectiveStartDate().equals(o.getEffectiveStartDate())) {
							ordList.add(o1);
							dat1 = o1.getEffectiveStartDate();
						}
				}
			}
			map.put(dat1, ordList);
		}

		model.put("map", map);
		model.put("drugOrders", drugOrders);
		model.put("reasonStoppedOptions", Utils
				.createCodedOptions(PharmacyConstants.REASON_ORDER_STOPPED));
		model.put("drugMap", drugMap);
		model.put("medSet", concMedset);



		Collections.sort(drugs, new Comparator<Drug>() {
			public int compare(Drug d1, Drug d2) {
				if (d1.getName() == null || d2.getName() == null) {
					return 0;
				}
				if (d1.getName().compareToIgnoreCase(d2.getName()) < 0) {
					return -1;
				} else {
					return 1;
				}

			}
		});


		//DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);

		Collection<DrugOrderPrescription> dopList = service.getDOPByPatientId(patient);
		Map<Integer,Integer> orderIdAndDisQuantity=new HashMap<Integer,Integer>();

		for (DrugOrderPrescription dop:dopList) {
			orderIdAndDisQuantity.put(dop.getOrderId().getOrderId(),dop.getQuantity());
		}
		model.put("dispensedQuantity", orderIdAndDisQuantity);

		model.put("drugs", drugs);

	/*	List<Person> people=new ArrayList<Person>();
		people.add(patient);
		List<Encounter> encounters=new ArrayList<Encounter>();

		for (String encounterTypeName: Context.getAdministrationService().getGlobalProperty("pharmacymanagement.nextHIVVisitDateNeededDuringDrudOrder").split(",") ) {
			encounters.add(Context.get)
		}


		List<Concept> concepts=new ArrayList<Concept>();
		concepts.add(Context.getConceptService().getConceptByUuid("3ce94df0-26fe-102b-80cb-0017a47871b2"));

		Context.getObsService().getObservations(people,encounters,concepts,null,null,null,null,null,null,null,null,true);
*/
		List<Obs> retunVisitDateobservations=Context.getObsService().getObservationsByPersonAndConcept(patient,Context.getConceptService().getConceptByUuid("3ce94df0-26fe-102b-80cb-0017a47871b2"));

		Date retunVisitDate=null;
		Obs retunVisitDateObs=null;
		for (Obs obs:retunVisitDateobservations) {
			retunVisitDate=	obs.getValueDate();
				if(obs.getEncounter().getEncounterType().getName().equals(Context.getAdministrationService().getGlobalProperty("pharmacymanagement.nextHIVVisitDateNeededDuringDrudOrder")) && obs.getValueDate().after(retunVisitDate)){
					retunVisitDate=	obs.getValueDate();
					retunVisitDateObs=obs;
				}
		}

		long todayInMs = new Date().getTime();
		long nextVisitDateInMs = 0;
		long timeDiff = 0;

		if (retunVisitDate!=null){
			nextVisitDateInMs=retunVisitDate.getTime();
			if(nextVisitDateInMs > todayInMs) {
				timeDiff = nextVisitDateInMs - todayInMs;
			}
		}
		int daysDiff = (int) (timeDiff / (1000 * 60 * 60* 24));
		/*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {
			retunVisitDate=sdf.parse(retunVisitDate.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}*/


		model.put("returnVisitDate", retunVisitDate);
		model.put("returnVisitDateInDays", daysDiff);

		if (retunVisitDateObs!=null)
			model.put("retunVisitDateObs", retunVisitDateObs);

		System.out.println("Dateeeeeeeeeeeeeeeeeee: "+retunVisitDate);
		System.out.println("Dayyyyyyyyyyyyyyyyyyyy: "+daysDiff);



		/** Commented by KAMONYO because he used it above...(LINE: 70) */
		//		model.put("patientId", patient.getPatientId());

		model.put("patient", patient);
		model.put("provider", Context.getAuthenticatedUser());
		model.put("drugsAtaTime", possibleFrequency[0]);
		model.put("timesAday", possibleFrequency[1]);
		model.put("days", possibleFrequency[2]);
		model.put("insuranceType", insuranceType);
		model.put("insuranceNumber", insuranceNumber);
		model.put("drugDosingUnits", Context.getOrderService().getDrugDosingUnits().stream().distinct().collect(Collectors.toList()));
		model.put("drugRoutes", Context.getOrderService().getDrugRoutes());
		super.populateModel(request, model);

	}
	
	@SuppressWarnings("unchecked")
	public List<DrugOrder> getDrugOrdersByPatient(Patient patient){

		OrderService orderService = Context.getOrderService();

		OrderType drugOrderType = orderService
				.getOrderType(PharmacyConstants.DRUG_ORDER_TYPE);
		OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteriaBuilder().setPatient(patient)
				.setOrderTypes(Collections.singletonList(drugOrderType)).build();

		List<DrugOrder> drugOrdersWithNoPreviousOrder=new ArrayList<DrugOrder>();
		for (Order ord:(List<Order>)Context.getOrderService().getOrders(orderSearchCriteria)) {
			if (ord instanceof DrugOrder && ord.getPreviousOrder() == null){
				drugOrdersWithNoPreviousOrder.add((DrugOrder)ord);
			}
		}
		return drugOrdersWithNoPreviousOrder;

	}

}
