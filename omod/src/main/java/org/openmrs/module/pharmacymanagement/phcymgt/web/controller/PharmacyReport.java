/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.Consommation;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.PharmacyInventory;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class PharmacyReport extends ParameterizableViewController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DrugOrderService service = Context.getService(DrugOrderService.class);
		String fromParam = null;
		String from = null;
		String toParam = "";
		String to = null;
		LocationService locationService = Context.getLocationService();
		List<Location> locations = locationService.getAllLocations();
		String pharmacyId = null;
		Collection<PharmacyInventory> piList = null;
		Pharmacy pharmacy = null;
		String fromto = null;

		ModelAndView mav = new ModelAndView();

		HashMap<String, Consommation> consommationMap = new HashMap<String, Consommation>();

		Location dftLoc = null;
		List<Pharmacy> pharmacyList = null;

		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));

			pharmacyList = service.getPharmacyByLocation(dftLoc);
		} catch (Exception e) {
			mav.addObject("msg", "pharmacymanagement.missingDftLoc");
		}

		if (request.getParameter("from") != null
				|| request.getParameter("to") != null) {
			if (request.getParameter("from") != null
					&& !request.getParameter("from").equals("")) {

				fromParam = request.getParameter("from");
				String fromArr[] = fromParam.split("/");
				from = fromArr[2] + "-" + fromArr[1] + "-" + fromArr[0];

			}

			if (request.getParameter("to") != null
					&& !request.getParameter("to").equals("")) {
				toParam = request.getParameter("to");
				String toArr[] = toParam.split("/");
				to = toArr[2] + "-" + toArr[1] + "-" + toArr[0];
			}
			/*if (request.getParameter("pharmacyId") != null
					&& !request.getParameter("pharmacyId").equals("")) {*/
				pharmacyId = request.getParameter("pharmacyId");

				if(!pharmacyId.equals("") && Integer.valueOf(pharmacyId)!=0) {
					pharmacy = service.getPharmacyById(Integer.valueOf(pharmacyId));
				}
				piList = service.getPharmacyInventoryByFromToLocation(from, to,
						pharmacyId);
				for (PharmacyInventory pi : piList) {

					Consommation ca = new Consommation();
					if (pi.getDrugproductId().getDrugId() != null) {
						ca.setDrugName(pi.getDrugproductId().getDrugId().getName());
						ca.setDrugId(pi.getDrugproductId().getDrugId().getDrugId()
								+ "");
						ca.setConditUnitaire(pi.getDrugproductId().getDrugId()
								.getUnits());
					}else {
						ca.setDrugName(pi.getDrugproductId().getConceptId().getName().getName());
						ca.setDrugId(pi.getDrugproductId().getConceptId().getConceptId()
								+ "");
					}


					// Quantity received on the first day of the reported month
					if (pi.getDrugproductId().getDrugId() != null) {
						ca.setQntPremJour(service
								.getPharmacySoldeFirstDayOfWeek(from, pi
										.getDrugproductId().getDrugId()
										.getDrugId()
										+ "", null, pharmacyId));
					}
					else {
						ca.setQntPremJour(service
								.getPharmacySoldeFirstDayOfWeek(from, null, pi
										.getDrugproductId().getConceptId()
										.getConceptId()
										+ "", pharmacyId));
					}

					// Quantity remaining on the reported month
					if (pi.getDrugproductId().getDrugId() != null)
						ca.setQntRestMens(service
								.getPharmacySoldeLastDayOfWeek(to, pi
										.getDrugproductId().getDrugId()
										.getDrugId()
										+ "", null, pharmacyId));
					else
						ca.setQntRestMens(service
								.getPharmacySoldeLastDayOfWeek(to, null, pi
										.getDrugproductId().getConceptId()
										.getConceptId()
										+ "", pharmacyId));

					Object obRec = null;
					Object obCons = null;

					if (pi.getDrugproductId().getConceptId() != null) {
						System.out.println("Innnnnnnnnnnnnnnnnnnnnnnnnn:"+pi.getDrugproductId().getConceptId().getName());
						obRec = service.getReceivedDispensedDrugOrConsumable(from, to, null, pharmacyId, pi.getDrugproductId().getConceptId().getConceptId()+"")[0];
						obCons = service.getReceivedDispensedDrugOrConsumable(from, to, null, pharmacyId, pi.getDrugproductId().getConceptId().getConceptId()+"")[1];
						// Quantity received during the reported period
						if (obRec != null)
							ca.setQntRecuMens(obRec);
						else
							ca.setQntRecuMens(0);

						// Quantity dispensed during the reported period
						if (obCons != null)
							ca.setQntConsomMens(obCons);
						else
							ca.setQntConsomMens(0);
					}

					if (pi.getDrugproductId().getDrugId() != null) {
						obRec = service.getReceivedDispensedDrugOrConsumable(from, to, pi
								.getDrugproductId().getDrugId().getDrugId()
								.toString(), pharmacyId, null)[0];
						obCons = service.getReceivedDispensedDrugOrConsumable(from, to, pi
								.getDrugproductId().getDrugId().getDrugId()
								.toString(), pharmacyId,null)[1];
						// Quantity received during the reported period
						if (obRec != null)
							ca.setQntRecuMens(obRec);
						else
							ca.setQntRecuMens(0);

						// Quantity dispensed during the reported period
						if (obCons != null)
							ca.setQntConsomMens(obCons);
						else
							ca.setQntConsomMens(0);

					}




					String key = "";
					key = ca.getDrugId().toString() + "_" + pharmacyId;
					consommationMap.put(key, ca);
				}
				if (fromParam != null && !fromParam.equals("") && toParam != null && !toParam.equals("") && pharmacy!=null) {
					fromto = "(From: " + fromParam + " To: " + toParam + ") "
							+ pharmacy.getName();
				}else{
					fromto = "(From: " + fromParam + " To: " + toParam + ") ";
				}
			/*}else {
				mav.addObject("msg", "Complete all the fields");
		}*/
		}

		mav.addObject("pharmacyId", pharmacyId);
		mav.addObject("from", fromParam);
		mav.addObject("to", toParam);
		mav.addObject("fromto", fromto);
		mav.addObject("locations", locations);
		mav.addObject("pharmacyList", pharmacyList);
		mav.addObject("consommationMap", consommationMap);
		mav.setViewName(getViewName());
		return mav;

	}
}
