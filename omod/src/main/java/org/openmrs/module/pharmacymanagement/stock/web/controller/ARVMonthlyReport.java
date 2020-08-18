package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.CmdDrug;
import org.openmrs.module.pharmacymanagement.Consommation;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class ARVMonthlyReport extends ParameterizableViewController {	
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CmdDrug cmdDrug;
		Collection<DrugProduct> drugProducts;
		String fromParam = "";
		String from = null;
		String toParam = "";
		String to = null;
		DrugOrderService service;
		ModelAndView mav = new ModelAndView();
		HashMap<String, Consommation> consommationMap = new HashMap<String, Consommation>();
		service = Context.getService(DrugOrderService.class);
		LocationService locationService = Context.getLocationService();
		
		Location dftLoc = null;

		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
		} catch (Exception e) {
			mav
					.addObject(
							"msg",
							"pharmacymanagement.missingDftLoc");
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
			
			

			drugProducts = service.getARVReportByFromTo(from, to, dftLoc.getLocationId()+"");

			for (DrugProduct dp : drugProducts) {

				Consommation ca = new Consommation();
				cmdDrug = service.getCmdDrugById(dp.getCmddrugId()
						.getCmddrugId());

				ca.setDrugName(dp.getDrugId().getName());
				ca.setDrugId(dp.getDrugId().getDrugId().toString());
				ca.setConditUnitaire(dp.getDrugId().getDoseLimitUnits().getDisplayString());
				ca.setExpirationDate(dp.getExpiryDate());
				if (cmdDrug.getLocationId() != null) {
					if(dp.getDrugId() != null)
						ca
							.setQntPremJour(service.getSoldeByFromDrugLocation(
									from,
									dp.getDrugId().getDrugId()+"", null,
									cmdDrug.getLocationId().getLocationId()
											.toString()));
					else
						ca
						.setQntPremJour(service.getSoldeByFromDrugLocation(
								from,
								null, dp.getConceptId().getConceptId()+"",
								cmdDrug.getLocationId().getLocationId()
										.toString()));
					if(dp.getDrugId() != null) {
						ca.setQntRestMens(service.getSoldeByToDrugLocation(to, dp
							.getDrugId().getDrugId()+"", null, cmdDrug
							.getLocationId().getLocationId().toString()));
					} else {
						ca.setQntRestMens(service.getSoldeByToDrugLocation(to, null, dp.getConceptId().getConceptId()+"", cmdDrug
								.getLocationId().getLocationId().toString()));						
					}
					if(dp.getDrugId() != null)
						ca.setQntRecuMens(service
							.getSumEntreeSortieByFromToDrugLocation(from, to,
									dp.getDrugId().getDrugId()+"", null,
									cmdDrug.getLocationId().getLocationId()
											.toString())[0]);
					else
						ca.setQntRecuMens(service
								.getSumEntreeSortieByFromToDrugLocation(from, to,
										dp.getDrugId().getDrugId()+"", null,
										cmdDrug.getLocationId().getLocationId()
												.toString())[0]);
					if(dp.getDrugId() != null)
						ca.setQntConsomMens(service
							.getSumEntreeSortieByFromToDrugLocation(from, to,
									dp.getDrugId().getDrugId()+"", null,
									cmdDrug.getLocationId().getLocationId()
											.toString())[1]);
					else
						ca.setQntConsomMens(service
								.getSumEntreeSortieByFromToDrugLocation(from, to,
										null, dp.getConceptId().getConceptId()+"",
										cmdDrug.getLocationId().getLocationId()
												.toString())[1]);
					
					ca.setLocationId(cmdDrug.getLocationId().getLocationId());
				} else {
					if(dp.getDrugId() != null)
						ca.setQntPremJour(service.getSoldeByFromDrugLocation(from,
							dp.getDrugId().getDrugId()+"", null, cmdDrug
									.getPharmacy().getLocationId()
									.getLocationId().toString()));
					else
						ca.setQntPremJour(service.getSoldeByFromDrugLocation(from,
								null, dp.getConceptId().getConceptId()+"", cmdDrug
										.getPharmacy().getLocationId()
										.getLocationId().toString()));
					if(dp.getDrugId() != null) {
						ca.setQntRestMens(service.getSoldeByToDrugLocation(to, dp
							.getDrugId().getDrugId()+"", null, cmdDrug
							.getPharmacy().getLocationId().getLocationId()
							.toString()));
					} else {
						ca.setQntRestMens(service.getSoldeByToDrugLocation(to, null, dp.getConceptId().getConceptId()+"", cmdDrug
								.getPharmacy().getLocationId().getLocationId()+""));
						
					}
					if(dp.getDrugId() != null)
						ca.setQntRecuMens(service
							.getSumEntreeSortieByFromToDrugLocation(from, to,
									dp.getDrugId().getDrugId()+"", null,
									cmdDrug.getPharmacy().getLocationId()
											.getLocationId().toString())[0]);
					else
						ca.setQntRecuMens(service
								.getSumEntreeSortieByFromToDrugLocation(from, to,
										null, dp.getConceptId().getConceptId()+"",
										cmdDrug.getPharmacy().getLocationId()
												.getLocationId().toString())[0]);
					
					if(dp.getDrugId() != null)
						ca.setQntConsomMens(service
							.getSumEntreeSortieByFromToDrugLocation(from, to,
									dp.getDrugId().getDrugId()+"", null,
									cmdDrug.getPharmacy().getLocationId()
											.getLocationId().toString())[1]);
					else
						ca.setQntConsomMens(service
								.getSumEntreeSortieByFromToDrugLocation(from, to,
										null, dp.getConceptId().getConceptId()+"",
										cmdDrug.getPharmacy().getLocationId()
												.getLocationId().toString())[1]);
					
					ca.setLocationId(cmdDrug.getPharmacy().getLocationId().getLocationId());
				}

				String key = "";
				if (cmdDrug.getLocationId() != null) {
					key = ca.getDrugId().toString() + "_"
							+ cmdDrug.getLocationId().getLocationId();
					consommationMap.put(key, ca);
				} else {
					key = ca.getDrugId().toString()
							+ "_"
							+ cmdDrug.getPharmacy().getLocationId()
									.getLocationId();
					consommationMap.put(key, ca);
				}
			}
		}

		mav.addObject("consommationMap", consommationMap);

		mav.setViewName(getViewName());

		return mav;
	}
}
