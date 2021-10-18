/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.ConsumableDispense;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.PharmacyInventory;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class ConsumableDispensationController extends
		ParameterizableViewController {
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		PatientService ps = Context.getPatientService();
		ConceptService cs = Context.getConceptService();

		DrugOrderService dos = Context.getService(DrugOrderService.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Concept service = null;
		DrugProduct dp = null;
		User provider = null;
		ConsumableDispense cd = null;
		PharmacyInventory pinv = null;
		boolean isEdit = false;
		Collection<PharmacyInventory> piList = new ArrayList<PharmacyInventory>();
		int solde = 0, currSolde = 0, currConsSolde = 0;
//		Collection<ConceptAnswer> services = cs.getConcept(6702).getAnswers();
		Collection<Pharmacy> pharmacies = dos.getAllPharmacies();
		
		piList = dos.getAllPharmacyInventory();
		Map<Integer, DrugProduct> map = new HashMap<Integer, DrugProduct>();
		String nd = null;

		for (PharmacyInventory pi : piList) {
			if (pi.getDrugproductId().getConceptId() != null) {
				currSolde = dos.getCurrSoldeDisp(null, pi.getDrugproductId().getConceptId().getConceptId()+ "", pi.getDrugproductId().getCmddrugId().getPharmacy().getPharmacyId()+ "", null, null, nd);
				if (currSolde > 0) {
					if (pi.getDrugproductId().getConceptId() != null)
						map.put(pi.getDrugproductId().getConceptId()
								.getConceptId(), pi.getDrugproductId());
				}
			}
		}
		
		List<ConsumableDispense> consumableList = dos.getAllConsumableDipsense();

		if ((request.getParameter("editId") != null && !request.getParameter("editId").equals(""))
				|| (request.getParameter("cdId") != null && !request.getParameter("cdId").equals(""))) {
			if(request.getParameter("editId") != null && !request.getParameter("editId").equals("")) {
				cd = Utils.getConsumableDispense(Integer.valueOf(request.getParameter("editId")));
				mav.addObject("cd", cd);	
				isEdit = true;
			}
			if(request.getParameter("cdId") != null && !request.getParameter("cdId").equals("")) {
				cd = Utils.getConsumableDispense(Integer.valueOf(request.getParameter("cdId")));
			}				
		} else {
			cd = new ConsumableDispense();
			pinv = new PharmacyInventory();
		}

		if (request.getParameter("date") != null
				&& !request.getParameter("date").equals("")
				&& request.getParameter("consumable") != null
				&& !request.getParameter("consumable").equals("")
				&& request.getParameter("qnty") != null
				&& !request.getParameter("qnty").equals("")
				&& request.getParameter("service") != null
				&& !request.getParameter("service").equals("")
				&& request.getParameter("patientId") != null
				&& !request.getParameter("patientId").equals("")) {
			String[] dateArr = request.getParameter("date").split("/");

			String dateStr;
			if(Context.getLocale().toString().equals("en_US") || Context.getLocale().toString().equals("en")) {
				dateStr = dateArr[2] + "-" + dateArr[0] + "-" + dateArr[1];
			}
			else {
				dateStr = dateArr[2] + "-" + dateArr[1] + "-" + dateArr[0];
			}
			Date date = sdf.parse(dateStr);


			cd.setDate(date);
			cd.setQnty(Integer.valueOf(request.getParameter("qnty")));
			System.out.println("DrugProduct Id: " + request.getParameter("consumable"));
			dp = dos.getDrugProductById(Integer.valueOf(request
					.getParameter("consumable")));
			cd.setDrugproductId(dp);
			System.out.println("concept id: " + dp.getConceptId().getConceptId());
			System.out.println("order Id: " + dp.getCmddrugId().getCmddrugId());
//			if (isEdit) {
			currConsSolde = dos.getCurrSoldeDisp(null, dp.getConceptId()
						.getConceptId()
						+ "", dp.getCmddrugId().getPharmacy().getPharmacyId()
						+ "", dp.getExpiryDate()+"", dp.getLotNo()+"", null);
			solde = currConsSolde
						- Integer.valueOf(request.getParameter("qnty"));
//			}

			service = cs.getConcept(Integer.valueOf(request
					.getParameter("service")));
			cd.setService(service);
			provider = Context.getAuthenticatedUser();
			cd.setProvider(provider);
			Patient patient = ps.getPatient(Integer.valueOf(request
					.getParameter("patientId")));
			cd.setPatientId(patient);


			if (solde >= 0) {
				dos.saveOrUpdateConsumableDispense(cd);
	//			if (isEdit) {
					pinv.setDate(date);
					pinv.setDrugproductId(dp);
					pinv.setCpId(cd);
					pinv.setEntree(0);
					pinv.setSortie(Integer.valueOf(request.getParameter("qnty")));

					pinv.setSolde(solde);
					dos.savePharmacyInventory(pinv);
					piList = dos.getAllPharmacyInventory();
	//			}
				mav.addObject("msg", "Saved");
			}
		}
		try {
			for (int i = 0; i < 100; i++) {
				if(request.getParameter("date"+i) != null && !request.getParameter("date"+i).equals("")) {
					handleCloneRows(request, sdf, cd, dp, dos, solde, currSolde, currConsSolde, cs, provider, ps, pinv, piList, mav, i);
				}else {
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		mav.addObject("map", map);
		mav.addObject("consumableList", consumableList);
		mav.addObject("pharmacies", pharmacies);
		mav.setViewName(getViewName());
		return mav;
	}
	
	protected void handleCloneRows(HttpServletRequest request, SimpleDateFormat sdf, ConsumableDispense cd, 
			DrugProduct dp, DrugOrderService dos, int solde, int currSolde, int currConsSolde, ConceptService cs,
			User provider, PatientService ps, PharmacyInventory pinv, Collection<PharmacyInventory> piList, ModelAndView mav, int i) throws ParseException {
		if (request.getParameter("date") != null
				&& !request.getParameter("date").equals("")
				&& request.getParameter("consumable"+i) != null
				&& !request.getParameter("consumable"+i).equals("")
				&& request.getParameter("qnty"+i) != null
				&& !request.getParameter("qnty"+i).equals("")
				&& request.getParameter("service") != null
				&& !request.getParameter("service").equals("")
				&& request.getParameter("patientId") != null
				&& !request.getParameter("patientId").equals("")) {
			String[] dateArr = request.getParameter("date").split("/");

			String dateStr;
			if(Context.getLocale().toString().equals("en_US") || Context.getLocale().toString().equals("en")) {
				dateStr = dateArr[2] + "-" + dateArr[0] + "-" + dateArr[1];
			}
			else {
				dateStr = dateArr[2] + "-" + dateArr[1] + "-" + dateArr[0];
			}
			Date date = sdf.parse(dateStr);


			cd.setDate(date);
			cd.setQnty(Integer.valueOf(request.getParameter("qnty"+i)));
			System.out.println("DrugProduct Id: " + request.getParameter("consumable"+i));
			dp = dos.getDrugProductById(Integer.valueOf(request
					.getParameter("consumable"+i)));
			cd.setDrugproductId(dp);
			System.out.println("concept id: " + dp.getConceptId().getConceptId());
			System.out.println("order Id: " + dp.getCmddrugId().getCmddrugId());
//			if (isEdit) {
			currConsSolde = dos.getCurrSoldeDisp(null, dp.getConceptId()
						.getConceptId()
						+ "", dp.getCmddrugId().getPharmacy().getPharmacyId()
						+ "", dp.getExpiryDate()+"", dp.getLotNo()+"", null);
			solde = currConsSolde
						- Integer.valueOf(request.getParameter("qnty"+i));
//			}

			Concept service = cs.getConcept(Integer.valueOf(request
					.getParameter("service")));
			cd.setService(service);
			provider = Context.getAuthenticatedUser();
			cd.setProvider(provider);
			Patient patient = ps.getPatient(Integer.valueOf(request
					.getParameter("patientId")));
			cd.setPatientId(patient);


			if (solde >= 0) {
				dos.saveOrUpdateConsumableDispense(cd);
	//			if (isEdit) {
					pinv.setDate(date);
					pinv.setDrugproductId(dp);
					pinv.setCpId(cd);
					pinv.setEntree(0);
					pinv.setSortie(Integer.valueOf(request.getParameter("qnty"+i)));

					pinv.setSolde(solde);
					dos.savePharmacyInventory(pinv);
					piList = dos.getAllPharmacyInventory();
	//			}
				mav.addObject("msg", "Saved");
			}
		}
	}
}