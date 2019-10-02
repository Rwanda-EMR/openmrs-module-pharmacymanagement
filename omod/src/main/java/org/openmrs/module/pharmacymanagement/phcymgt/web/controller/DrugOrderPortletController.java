/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.ObsService;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugOrderPrescription;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.DrugProductInventory;
import org.openmrs.module.pharmacymanagement.PharmacyConstants;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
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
			drugOrders = orderService.getDrugOrdersByPatient(patient);
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
					if (o1.getStartDate() != null && o.getStartDate() != null)
						if (o1.getStartDate().equals(o.getStartDate())) {
							ordList.add(o1);
							dat1 = o1.getStartDate();
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

		/** Commented by KAMONYO because he used it above...(LINE: 70) */
		//		model.put("patientId", patient.getPatientId());

		model.put("patient", patient);
		model.put("provider", Context.getAuthenticatedUser());
		model.put("drugsAtaTime", possibleFrequency[0]);
		model.put("timesAday", possibleFrequency[1]);
		model.put("days", possibleFrequency[2]);
		model.put("insuranceType", insuranceType);
		model.put("insuranceNumber", insuranceNumber);
		super.populateModel(request, model);

	}

}
