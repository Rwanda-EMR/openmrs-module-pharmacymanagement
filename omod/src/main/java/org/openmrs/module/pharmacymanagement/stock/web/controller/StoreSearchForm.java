/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.ConceptAnswer;
import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugProductInventory;
import org.openmrs.module.pharmacymanagement.PharmacyConstants;
import org.openmrs.module.pharmacymanagement.StoreWarning;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class StoreSearchForm extends ParameterizableViewController {
	private Log log = LogFactory.getLog(this.getClass());

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		DrugOrderService service = Context.getService(DrugOrderService.class);
		ConceptService conceptService = Context.getConceptService();
		Map itemMap = new HashMap<String, StoreWarning>();
		List<Drug> drugsInSystem = conceptService.getAllDrugs();
		List<DrugProductInventory> itemsInStore = service
				.getAllDrugProductInventory();
		Collection<ConceptAnswer> consumablesInSystem = conceptService
				.getConcept(PharmacyConstants.CONSUMABLE).getAnswers();
		String drugId = null, consumableId = null, name = "";
		int in = 0, out = 0, solde = 0;

		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);
			

		StoreWarning storeWarning = null;
		for (DrugProductInventory dpi : itemsInStore) {
			if (dpi.getDrugproductId().getConceptId() != null) {
				consumableId = dpi.getDrugproductId().getConceptId()
						.getConceptId()
						+ "";
				name = dpi.getDrugproductId().getConceptId().getName()
						.getName();
				Object outStr1 = service
						.getSumEntreeSortieByFromToDrugLocation(null,
								new Date() + "", drugId, consumableId,
								locationStr)[1];
				Object inStr1 = service
						.getSumEntreeSortieByFromToDrugLocation(null,
								new Date() + "", drugId, consumableId,
								locationStr)[0];

				if (inStr1 != null)
					in = Integer.valueOf(inStr1 + "");

				if (outStr1 != null)
					out = Integer.valueOf(outStr1 + "");

				solde = in - out;

				storeWarning = new StoreWarning(name, out, in, solde,
						dpi.getDrugproductId().getLotNo(), dpi
								.getDrugproductId().getExpiryDate()
								+ "");

				itemMap.put(name, storeWarning);
				drugId = null;
				consumableId = null;
			}
			
			if (dpi.getDrugproductId().getDrugId() != null) {
				drugId = dpi.getDrugproductId().getDrugId().getDrugId()
						+ "";
				name = dpi.getDrugproductId().getDrugId().getName();

				Object inStr3 = service
						.getSumEntreeSortieByFromToDrugLocation(null,
								new Date() + "", drugId, consumableId,
								locationStr)[0];
				Object outStr3 = service
						.getSumEntreeSortieByFromToDrugLocation(null,
								new Date() + "", drugId, consumableId,
								locationStr)[1];

				if (inStr3 != null)
					in = Integer.valueOf(inStr3 + "");

				if (outStr3 != null)
					out = Integer.valueOf(outStr3 + "");

				solde = in - out;

				storeWarning = new StoreWarning(dpi.getDrugproductId()
						.getDrugId().getName(), out, in, solde, dpi
						.getDrugproductId().getLotNo(), dpi
						.getDrugproductId().getExpiryDate() + "");

				itemMap.put(name, storeWarning);
				drugId = null;
				consumableId = null;
				in = 0;
				out = 0;
			}
		}
		
		
		
//		if (request.getParameter("consumable") != null
//				&& !request.getParameter("consumable").equals("")) {
//			if (request.getParameter("consumable").equals("0")) {
//				for (DrugProductInventory dpi : itemsInStore) {
//					if (dpi.getDrugproductId().getConceptId() != null) {
//						consumableId = dpi.getDrugproductId().getConceptId()
//								.getConceptId()
//								+ "";
//						name = dpi.getDrugproductId().getConceptId().getName()
//								.getName();
//						Object outStr1 = service
//								.getSumEntreeSortieByFromToDrugLocation(null,
//										new Date() + "", drugId, consumableId,
//										locationStr)[1];
//						Object inStr1 = service
//								.getSumEntreeSortieByFromToDrugLocation(null,
//										new Date() + "", drugId, consumableId,
//										locationStr)[0];
//
//						if (inStr1 != null)
//							in = Integer.valueOf(inStr1 + "");
//
//						if (outStr1 != null)
//							out = Integer.valueOf(outStr1 + "");
//
//						solde = in - out;
//
//						storeWarning = new StoreWarning(name, out, in, solde,
//								dpi.getDrugproductId().getLotNo(), dpi
//										.getDrugproductId().getExpiryDate()
//										+ "");
//
//						itemMap.put(name, storeWarning);
//						drugId = null;
//						consumableId = null;
//					}
//				}
//			} else {
//				for (DrugProductInventory dpi : itemsInStore) {
//					consumableId = request.getParameter("consumable");
//					if (dpi.getDrugproductId().getConceptId() != null) {
//						if (dpi.getDrugproductId().getConceptId()
//								.getConceptId() == Integer
//								.valueOf(consumableId)) {
//							Object outStr2 = service
//									.getSumEntreeSortieByFromToDrugLocation(
//											null, null, null, consumableId,
//											locationStr)[1];
//							Object inStr2 = service
//									.getSumEntreeSortieByFromToDrugLocation(
//											null, null, null, consumableId,
//											locationStr)[0];
//
//
//							if (inStr2 != null)
//								in = Integer.valueOf(inStr2 + "");
//
//							if (outStr2 != null)
//								out = Integer.valueOf(outStr2 + "");
//
//							solde = in - out;
//
//							storeWarning = new StoreWarning(dpi
//									.getDrugproductId().getConceptId()
//									.getName().getName(), out, in, solde, dpi
//									.getDrugproductId().getLotNo(), dpi
//									.getDrugproductId().getExpiryDate() + "");
//							
//							itemMap.put(dpi.getDrugproductId().getConceptId()
//									.getName().getName(), storeWarning);
//							consumableId = null;
//							drugId = null;
//							break;
//						}
//					}
//				}
//			}
//		}

//		if ((request.getParameter("drug") != null && !request.getParameter(
//				"drug").equals(""))) {
//			if (request.getParameter("drug").equals("0")) {
//				for (DrugProductInventory dpi : itemsInStore) {
//					if (dpi.getDrugproductId().getDrugId() != null) {
//						drugId = dpi.getDrugproductId().getDrugId().getDrugId()
//								+ "";
//						name = dpi.getDrugproductId().getDrugId().getName();
//
//						Object inStr3 = service
//								.getSumEntreeSortieByFromToDrugLocation(null,
//										new Date() + "", drugId, consumableId,
//										locationStr)[0];
//						Object outStr3 = service
//								.getSumEntreeSortieByFromToDrugLocation(null,
//										new Date() + "", drugId, consumableId,
//										locationStr)[1];
//
//						if (inStr3 != null)
//							in = Integer.valueOf(inStr3 + "");
//
//						if (outStr3 != null)
//							out = Integer.valueOf(outStr3 + "");
//
//						solde = in - out;
//
//						storeWarning = new StoreWarning(dpi.getDrugproductId()
//								.getDrugId().getName(), out, in, solde, dpi
//								.getDrugproductId().getLotNo(), dpi
//								.getDrugproductId().getExpiryDate() + "");
//
//						itemMap.put(name, storeWarning);
//						drugId = null;
//						consumableId = null;
//						in = 0;
//						out = 0;
//					}
//				}
//			} else {
//				for (DrugProductInventory dpi : itemsInStore) {
//					drugId = request.getParameter("drug");
//					if (dpi.getDrugproductId().getDrugId() != null) {
//						if (dpi.getDrugproductId().getDrugId().getDrugId() == Integer
//								.valueOf(drugId)) {
//
//							Object inStr4 = service
//									.getSumEntreeSortieByFromToDrugLocation(
//											null, new Date() + "", drugId,
//											null, locationStr)[0];
//							Object outStr4 = service
//									.getSumEntreeSortieByFromToDrugLocation(
//											null, new Date() + "", drugId,
//											null, locationStr)[1];
//
//
//							if (inStr4 != null)
//								in = Integer.valueOf(inStr4 + "");
//
//							if (outStr4 != null)
//								out = Integer.valueOf(outStr4 + "");
//
//							solde = in - out;
//
//							storeWarning = new StoreWarning(dpi
//									.getDrugproductId().getDrugId().getName(),
//									out, in, solde, dpi.getDrugproductId()
//											.getLotNo(), dpi.getDrugproductId()
//											.getExpiryDate() + "");
//
//							itemMap.put(dpi.getDrugproductId().getDrugId()
//									.getName(), storeWarning);
//							break;
//						}
//					}
//				}
//			}
//		}

		mav.addObject("itemMap", itemMap);
		mav.addObject("drugsInSystem", drugsInSystem);
		mav.addObject("consumablesInSystem", consumablesInSystem);

		mav.setViewName(getViewName());
		return mav;

	}
}