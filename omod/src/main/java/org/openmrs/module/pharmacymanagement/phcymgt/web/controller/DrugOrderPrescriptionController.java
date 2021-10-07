package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Order;
import org.openmrs.OrderFrequency;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohappointment.model.Appointment;
import org.openmrs.module.mohappointment.model.AppointmentState;
import org.openmrs.module.mohappointment.utils.AppointmentUtil;
import org.openmrs.module.pharmacymanagement.PharmacyConstants;
import org.openmrs.module.pharmacymanagement.PrescriptionRequest;
import org.openmrs.module.pharmacymanagement.utils.GlobalPropertiesMgt;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.WebConstants;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DrugOrderPrescriptionController extends AbstractController {
	protected final Log log = LogFactory.getLog(getClass());

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();

     	double dose = 0;


		String patientId = request.getParameter("patientId");
		Patient patient = Context.getPatientService().getPatient(
				Integer.valueOf(patientId));

		Encounter enc=new Encounter();
		enc.setEncounterType(Context.getEncounterService().getEncounterType("Drug Order Encounter"));
		enc.setPatient(patient);
		enc.setEncounterDatetime(removeTime(new Date()));

		//OrderFrequency of= Context.getOrderService().getOrderFrequencyByConcept(Context.getConceptService().getConceptByName("Frequency Not applicable"));

				//getOrderFrequencies(request.getParameter("frequency"), Context.getLocale(), false, false).get(0);

		HttpSession httpSession = request.getSession();
		String qtyStr = null;
		
		/** This is created here, so that it can't be declared anywhere else... (KAMONYO) */
		Integer appointmentId = null;
		/** ... */
		


		SimpleDateFormat sdf;

		if(Context.getLocale().toString().equals("en_US") || Context.getLocale().toString().equals("en")) {
			sdf = new SimpleDateFormat("MM/dd/yyyy");
		}
		else {
			sdf = new SimpleDateFormat("dd/MM/yyyy");
		}


		ConceptService conceptService = Context.getConceptService();
		OrderService orderService = Context.getOrderService();
		LocationService locationService = Context.getLocationService();

		Location dftLoc = null;
		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
		} catch (Exception e) {
		}

		if (request.getParameter("editcreate") != null
				&& !request.getParameter("editcreate").equals("")
				&& patient != null) {
			if (request.getParameter("editcreate").equals("create")) {

				ObjectMapper mapper = new ObjectMapper();
				String json = request.getParameter("selecteDrugs");

				try {
					// Convert JSON array to List of objects
					List<PrescriptionRequest> ppl2 = Arrays.asList(mapper.readValue(json, PrescriptionRequest[].class));

					for (PrescriptionRequest prescriptionRequest : ppl2) {
						handleCreate(request, patientId, mav, patient, orderService, httpSession, sdf, qtyStr, conceptService, dose, enc, prescriptionRequest);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		// Editing an order
		if (request.getParameter("editcreate") != null
				&& !request.getParameter("editcreate").equals("")
				&& patient != null) {
			if (request.getParameter("editcreate").equals("edit")) {
				DrugOrder drugOrder = cloneAndVoidPrevious((DrugOrder)orderService.getOrder(Integer.valueOf(request.getParameter("orderId"))),"Edit");
				Drug drug = conceptService.getDrug(Integer.valueOf(request
						.getParameter("drugs")));

				// editing the date from the form to get this format dd/MM/yyyy
				if (request.getParameter("startdate") != null
						&& !request.getParameter("startdate").equals("")) {
					String strDate1 = request.getParameter("startdate");

					Date startDate = null;
					try {
						startDate = sdf.parse(strDate1);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					drugOrder.setDateActivated(startDate);
				}
				if (request.getParameter("quantity") != null
						&& !request.getParameter("quantity").equals("")) {
					qtyStr = request.getParameter("quantity");
					drugOrder.setQuantity(Double.valueOf(qtyStr));
					drugOrder.setQuantityUnits(Context.getConceptService().getConceptByName("Quantity Units Not applicable"));

				}

				drugOrder.setConcept(drug.getConcept());
				drugOrder.setInstructions(request.getParameter("instructions"));
				drugOrder.setDateCreated(new Date());
				drugOrder.setPatient(patient);
				drugOrder.setDrug(drug);
				drugOrder.setDuration(Integer.parseInt(request.getParameter("days")));
				drugOrder.setDurationUnits(Context.getConceptService().getConceptByName("DAYS"));
				if (request.getParameter("dose") != null
						&& !request.getParameter("dose").equals(""))
					drugOrder.setDose(dose);

				//drugOrder.setFrequency(of);
				drugOrder.setDoseUnits(Context.getConceptService().getConceptByName("Dosing Unspecified"));

				if (request.getParameter("quantity") != null
						&& !request.getParameter("quantity").equals(""))
					drugOrder.setQuantity(Double.valueOf(request.getParameter("quantity")));
				Context.getEncounterService().saveEncounter(enc);
				drugOrder.setEncounter(enc);
				drugOrder.setOrderer(Utils.getProvider());
				orderService.saveOrder(drugOrder, Utils.getOrderContext());
				mav.addObject("msg", "An order has been updated successfully!");
			}
		}

		if (request.getParameter("delete") != null
				&& !request.getParameter("delete").equals("")
				&& patient != null) {
			Order order = orderService.getOrder(Integer.valueOf(request
					.getParameter("orderToDel")));
			order.setVoided(true);
			order.setVoidedBy(Context.getAuthenticatedUser());
			order.setVoidReason(request.getParameter("deleteReason"));
			orderService.voidOrder(order,request.getParameter("deleteReason"));
			Encounter encounter=order.getEncounter();
			encounter.setVoided(true);
			encounter.setVoidedBy(Context.getAuthenticatedUser());
			encounter.setVoidReason(request.getParameter("deleteReason"));
			Context.getEncounterService().saveEncounter(encounter);
			//orderService.saveOrder(order, Utils.getOrderContext());
			mav.addObject("msg", "An order has been deleted successfully!");

		}

		// Stopping an order
		if (request.getParameter("stopping") != null
				&& !request.getParameter("stopping").equals("")) {
			if (request.getParameter("stopping").equals("stop")) {
				Order order = orderService.getOrder(Integer.valueOf(request
						.getParameter("orderId")));
				Concept concept = conceptService.getConcept(Integer
						.valueOf(request.getParameter("reasons")));
				Date date = null;
				try {
					date = sdf.parse(request.getParameter("stopDate"));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Order discontinuationOrder = order.cloneForDiscontinuing();
				discontinuationOrder.setOrderReason(concept);
				discontinuationOrder.setOrderer(order.getOrderer());
				discontinuationOrder.setEncounter(order.getEncounter());
				orderService.saveOrder(discontinuationOrder, Utils.getOrderContext());
				mav.addObject("msg", "An order has been stopped successfully!");
			}
		}

		return new ModelAndView(new RedirectView(
				"../../patientDashboard.form?patientId=" + patientId));
	}

	/**
	 * Creates Pharmacy Waiting Appointment, and sets existing Consultation
	 * waiting appointments as they are now no longer needed...
	 * 
	 * @param request
	 *            HttpServletRequest parameter to be used
	 * @param patient
	 *            the patient that is going to pharmacy services
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	private void createPharmacyAppointment(Integer appointmentId,
			HttpServletRequest request, Patient patient, Encounter encounter)
			throws NumberFormatException, ParseException {

		if (appointmentId != null) {

			Appointment appointment = AppointmentUtil
					.getWaitingAppointmentById(Integer.valueOf(request
							.getParameter("appointmentId")));
			Utils.setConsultationAppointmentAsAttended(appointment);

			// Create Pharmacy waiting appointment here:
//			Utils.createWaitingPharmacyAppointment(patient, encounter);
		}

		for (Appointment appointment : AppointmentUtil
				.getAllWaitingAppointmentsByPatientAtService(patient,
						new AppointmentState(4, "WAITING"), new Date(),
						AppointmentUtil.getServiceByConcept(GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.Consultationservice)))) {

			Utils.setConsultationAppointmentAsAttended(appointment);
		}
	}

	private void updateSessionMessage(WebRequest request, Exception e) {
		String msg = Context.getMessageSourceService().getMessage("error.general", null, Context.getLocale());
		msg += ": " + e.getMessage();
		request.setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg, WebRequest.SCOPE_SESSION);
	}
	public static Date removeTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, -1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	@Transactional
	public DrugOrder cloneAndVoidPrevious(DrugOrder orderToVoid, String reason) {
		DrugOrder newOrder = orderToVoid.cloneForRevision();
		if (newOrder.getOrderGroup() != null && !newOrder.getOrderGroup().getOrders().contains(newOrder)) {
			newOrder.getOrderGroup().addOrder(newOrder);
		}
		newOrder.setAction(Order.Action.NEW);
		newOrder.setPreviousOrder(null);
		Context.getOrderService().voidOrder(orderToVoid, reason);
		return newOrder;
	}
	
	protected ModelAndView handleCreate(HttpServletRequest request, String patientId, ModelAndView mav, Patient patient, 
			OrderService orderService, HttpSession httpSession, SimpleDateFormat sdf, String qtyStr, 
			ConceptService conceptService, double dose, Encounter enc, PrescriptionRequest prescriptionRequest) throws Exception {
		if(request.getParameter("selecteDrugs") != null && request.getParameter("selecteDrugs").length() > 0) {
			
			int frequency=0;
			if (prescriptionRequest.getTimesPerDayField()!=null && !prescriptionRequest.getTimesPerDayField().equals("")) {
				String timesPerDayField = prescriptionRequest.getTimesPerDayField();
				frequency=Integer.parseInt(timesPerDayField.substring(0, timesPerDayField.indexOf("/")));
			}
			String frequencyConceptName="";
			String[] frequenciesGP=Context.getAdministrationService().getGlobalProperty("pharmacymanagement.pharmacyFrequencies").split(",");

			for (String frequencieGP:frequenciesGP){
				if(Integer.parseInt(frequencieGP.split(":")[0])==frequency){
					frequencyConceptName=frequencieGP.split(":")[1];
					break;
				}
			}
			OrderFrequency of= Context.getOrderService().getOrderFrequencyByConcept(Context.getConceptService().getConceptByName(frequencyConceptName));
		

			/** Creating an Pharmacy appointment if not exists (KAMONYO) */

			//		if (request.getParameter("appointmentId") != null
			//				&& !request.getParameter("appointmentId").equals("")) {
			//			appointmentId = Integer.parseInt(request
			//					.getParameter("appointmentId"));
			//
			//			/**
			//			 * Setting Appointment as Attended here and
			//			 * creating a pharmacy waiting one:
			//			 */
			//			createPharmacyAppointment(appointmentId, request, patient,
			//					null);
			//		}
			/** ... END of Appointment ... */

			Utils.createWaitingPharmacyAppointment(patient, null);

			DrugOrder drugOrder = new DrugOrder();
			/*
			 * Drug drug = conceptService.getDrug(Integer.valueOf(request
			 * .getParameter("drugs")));
			 */
			Drug drug = conceptService.getDrug(prescriptionRequest.getDnameField());
			/*
			 * String startDateStr = ServletRequestUtils.getStringParameter( request,
			 * "startdate", null);
			 */
			String startDateStr = prescriptionRequest.getDstartDateField();
			OrderType orderType = orderService
					.getOrderType(PharmacyConstants.DRUG_ORDER_TYPE);
			if (prescriptionRequest.getDquantityField() != null
					&& !prescriptionRequest.getDquantityField().equals("")) {
				qtyStr = prescriptionRequest.getDquantityField();
				drugOrder.setQuantity(Double.valueOf(qtyStr));
			}

			drugOrder.setCreator(Context.getAuthenticatedUser());
			drugOrder.setConcept(drug.getConcept());
			drugOrder.setOrderType(orderType);
			drugOrder.setInstructions(prescriptionRequest.getDinstructionsField());
			//drugOrder.setInstructions(request.getParameter("frequency"));
			drugOrder.setDateCreated(new Date());
			drugOrder.setPatient(patient);
			drugOrder.setDrug(drug);
			//drugOrder.setDiscontinued(false);

			try {
				//dose = Double.parseDouble(request.getParameter("drugDose"));
				dose = Double.parseDouble(prescriptionRequest.getDquantityField());
				drugOrder.setDose(dose);
			} catch (Exception e) {
				log.error("Error parsing dose value to double");
				e.printStackTrace();
			}

			int doseUnits = 0;
			try {
				//doseUnits = Integer.parseInt(request.getParameter("doseUnits"));
				//drugOrder.setDoseUnits(Context.getConceptService().getConcept(doseUnits));
				drugOrder.setDoseUnits(Context.getConceptService().getConceptByName("Dosing Unspecified"));
			} catch (Exception e) {
				log.error("Error parsing dose units to int");
				e.printStackTrace();
			}
			try {				
				drugOrder.setRoute(Context.getConceptService().getConcept(prescriptionRequest.getRouteField()));
			} catch (Exception e) {
				log.error("Error parsing route to int");
				e.printStackTrace();
			}
			drugOrder.setQuantityUnits(Context.getConceptService().getConceptByName("Quantity Units Not applicable"));


			Context.getEncounterService().saveEncounter(enc);

			drugOrder.setEncounter(enc);
			drugOrder.setCareSetting(Context.getOrderService().getCareSetting(2));
			drugOrder.setDuration(Integer.parseInt(prescriptionRequest.getDaysField().substring(0, prescriptionRequest.getDaysField().indexOf(" ")).trim()));
			drugOrder.setDurationUnits(Context.getConceptService().getConceptByName("DAYS"));

			drugOrder.setOrderer(Utils.getProvider());

			if (prescriptionRequest.getQtyTakenAtOnceField() != null
					&& !prescriptionRequest.getQtyTakenAtOnceField().equals(""))
				drugOrder.setDose(Double.valueOf(prescriptionRequest.getQtyTakenAtOnceField()));
			drugOrder.setFrequency(of);

			if (prescriptionRequest.getDquantityField() != null
					&& !prescriptionRequest.getDquantityField().equals(""))
				drugOrder.setQuantity(Double.valueOf(prescriptionRequest.getDquantityField()));
			if (!startDateStr.equals("") && startDateStr != null) {
				Date startDate = null;
				try {
					startDate = sdf.parse(startDateStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				drugOrder.setDateActivated(startDate);
				orderService.saveOrder(drugOrder, Utils.getOrderContext());



				/*
				 * To be uncommented whenever the commented lines are
				 * validated
				 * 
				 * Concept c = conceptService.getConcept(6189); Concept
				 * obsValue = conceptService.getConcept(6191); Obs o =
				 * Utils.createObservation(startDate, dftLoc, patient, c,
				 * obsValue, 4); os.saveObs(o, null);
				 */

				mav.addObject("msg",
						"An order has been created successfully!");
			} else {
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
						"You need to enter the start date!");
			}
		}
		return new ModelAndView(new RedirectView(
				"../../patientDashboard.form?patientId=" + patientId));
	}
}