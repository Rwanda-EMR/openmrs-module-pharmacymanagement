package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Order;
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
import org.openmrs.module.pharmacymanagement.utils.GlobalPropertiesMgt;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.WebConstants;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

public class DrugOrderPrescriptionController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();

		HttpSession httpSession = request.getSession();
		String qtyStr = null;
		
		/** This is created here, so that it can't be declared anywhere else... (KAMONYO) */
		Integer appointmentId = null;
		/** ... */
		
		String patientId = request.getParameter("patientId");
		Patient patient = Context.getPatientService().getPatient(
					Integer.valueOf(patientId));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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

				/** Creating an Pharmacy appointment if not exists (KAMONYO) */

//				if (request.getParameter("appointmentId") != null
//						&& !request.getParameter("appointmentId").equals("")) {
//					appointmentId = Integer.parseInt(request
//							.getParameter("appointmentId"));
//
//					/**
//					 * Setting Appointment as Attended here and
//					 * creating a pharmacy waiting one:
//					 */
//					createPharmacyAppointment(appointmentId, request, patient,
//							null);
//				}
				/** ... END of Appointment ... */
				
				Utils.createWaitingPharmacyAppointment(patient, null);

				DrugOrder drugOrder = new DrugOrder();
				Drug drug = conceptService.getDrug(Integer.valueOf(request
						.getParameter("drugs")));
				String startDateStr = ServletRequestUtils.getStringParameter(
						request, "startdate", null);

				OrderType orderType = orderService
						.getOrderType(PharmacyConstants.DRUG_ORDER_TYPE);
				if (request.getParameter("quantity") != null
						&& !request.getParameter("quantity").equals("")) {
					qtyStr = request.getParameter("quantity");
					drugOrder.setQuantity(Integer.valueOf(qtyStr));
				}

				drugOrder.setCreator(Context.getAuthenticatedUser());
				drugOrder.setConcept(drug.getConcept());
				drugOrder.setOrderType(orderType);
				drugOrder.setInstructions(request.getParameter("instructions"));
				drugOrder.setDateCreated(new Date());
				drugOrder.setPatient(patient);
				drugOrder.setDrug(drug);
				drugOrder.setDiscontinued(false);
				drugOrder.setOrderer(Context.getAuthenticatedUser());

				if (request.getParameter("dose") != null
						&& !request.getParameter("dose").equals(""))
					drugOrder.setDose(Double.valueOf(request
							.getParameter("dose")));
				drugOrder.setFrequency(request.getParameter("frequency"));
				drugOrder.setUnits(request.getParameter("units"));
				if (request.getParameter("quantity") != null
						&& !request.getParameter("quantity").equals(""))
					drugOrder.setQuantity(Integer.valueOf(request
							.getParameter("quantity")));
				if (!startDateStr.equals("") && startDateStr != null) {
					Date startDate = null;
					try {
						startDate = sdf.parse(startDateStr);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					drugOrder.setStartDate(startDate);

					orderService.saveOrder(drugOrder);

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

		}

		// Editing an order
		if (request.getParameter("editcreate") != null
				&& !request.getParameter("editcreate").equals("")
				&& patient != null) {
			if (request.getParameter("editcreate").equals("edit")) {
				DrugOrder drugOrder = orderService.getOrder(
						Integer.valueOf(request.getParameter("orderId")),
						DrugOrder.class);
				// Order order = orderService.getOrder(Integer.valueOf(request
				// .getParameter("orderId")));

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
					drugOrder.setStartDate(startDate);
				}
				if (request.getParameter("quantity") != null
						&& !request.getParameter("quantity").equals("")) {
					qtyStr = request.getParameter("quantity");
					drugOrder.setQuantity(Integer.valueOf(qtyStr));
				}

				drugOrder.setConcept(drug.getConcept());
				drugOrder.setInstructions(request.getParameter("instructions"));
				drugOrder.setDateCreated(new Date());
				drugOrder.setPatient(patient);
				drugOrder.setDrug(drug);

				if (request.getParameter("dose") != null
						&& !request.getParameter("dose").equals(""))
					drugOrder.setDose(Double.valueOf(request
							.getParameter("dose")));

				drugOrder.setFrequency(request.getParameter("frequency"));
				drugOrder.setUnits(request.getParameter("units"));

				if (request.getParameter("quantity") != null
						&& !request.getParameter("quantity").equals(""))
					drugOrder.setQuantity(Integer.valueOf(request
							.getParameter("quantity")));

				orderService.saveOrder(drugOrder);
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
			orderService.saveOrder(order);
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

				order.setDiscontinuedReason(concept);
				order.setDiscontinuedDate(date);
				order.setDiscontinued(true);
				order.setDiscontinuedBy(Context.getAuthenticatedUser());

				orderService.saveOrder(order);
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
}
