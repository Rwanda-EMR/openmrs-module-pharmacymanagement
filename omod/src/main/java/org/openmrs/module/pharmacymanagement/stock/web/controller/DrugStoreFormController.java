package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.ConceptAnswer;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.CmdDrug;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.PharmacyConstants;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class DrugStoreFormController extends AbstractController {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		int storeqnty = 0;
		ModelAndView mav = new ModelAndView();

		List<Drug> drugs = new ArrayList<Drug>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<Integer, String> drugMap = new HashMap<Integer, String>();
		LocationService locationService = Context.getLocationService();
		Location dftLoc = null;
		DrugOrderService service = Context.getService(DrugOrderService.class);
		ConceptService cs = Context.getConceptService();
		Collection<ConceptAnswer> css = null;
		
		// Reference Data
		drugs = Context.getConceptService().getAllDrugs();
		List<Location> locations = locationService.getAllLocations();
		int i;
		for (i = 0; i < drugs.size(); i++) {
			if(drugs.get(i).getName()!=null)
				drugMap.put(drugs.get(i).getDrugId(), drugs.get(i).getName().toString());
			else
				drugMap.put(drugs.get(i).getDrugId(), drugs.get(i).getConcept().getName().toString());
		}

		Collection<DrugProduct> products = service.getAllProducts();
		
		try {
			css = cs.getConcept(PharmacyConstants.CONSUMABLE).getAnswers();
		} catch (NullPointerException npe) {
			mav.addObject("msg", "pharmacymanagement.noConsumable");
		}
		

		/*String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);*/

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

		DrugProduct drugProduct;
		CmdDrug cmdDrug = new CmdDrug();

		Map requestMap = request.getParameterMap();

		ArrayList<String> fieldNames = new ArrayList<String>();
		for (Object key : requestMap.keySet()) {
			String keyString = (String) key;
			fieldNames.add(keyString);
		}

		// setting and saving Product Order
		if (request.getParameterMap().containsKey("destination")
				|| request.getParameterMap().containsKey("supporter")
				|| request.getParameterMap().containsKey("month")) {
			if (!request.getParameter("fosaName").equals("")
					|| !request.getParameter("supporter").equals("")
					|| !request.getParameter("month").equals("")) {
				Location from = locationService.getLocation(Integer
						.valueOf(request.getParameter("fosaName")));
				Location to = locationService.getLocation(Integer
						.valueOf(request.getParameter("destination")));
				cmdDrug.setLocationId(from);
				cmdDrug.setDestination(to);
				cmdDrug.setSupportingProg(request.getParameter("supporter"));

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date date = sdf.parse(request.getParameter("month"));

				cmdDrug.setMonthPeriod(date);
			}
		}

		// setting and saving the product;
		if (fieldNames.size() != 0) {
			boolean hasSaved = false;
			int count = 1;
			for (String str : fieldNames) {
				String suffixId = str.substring(str.indexOf("_") + 1);
				String drugSuffix = "drugs_" + suffixId;
				String consSuffix = "consumable_" + suffixId;
				
				if (drugSuffix.equals(str)) {
					
					// saving the drug product
					String id = request.getParameter("drugs_" + suffixId);
					String drugneeded = request.getParameter("drugneeded_"+suffixId);
					if(count == 1)
						service.saveCmdDrug(cmdDrug);
					
					drugProduct = new DrugProduct();
					
					drugProduct.setDrugId(cs.getDrug(Integer.valueOf(id)));
					drugProduct.setQntyReq(Integer.parseInt(drugneeded));
					storeqnty = Utils.getSumOfSoldesInStock(id, null, cmdDrug
							.getLocationId().getLocationId()+"", null, cmdDrug.getCmddrugId()+"");
					drugProduct.setStoreQnty(storeqnty);
					drugProduct.setCmddrugId(cmdDrug);
					service.saveDrugProduct(drugProduct);
					hasSaved = true;
					count++;
				} else if (consSuffix.equals(str)) {
					// saving the consumable product
					String id = request.getParameter("consumable_" + suffixId);
					String consneeded = request.getParameter("consneeded_"+suffixId);
					
					if(count == 1)
						service.saveCmdDrug(cmdDrug);
					
					drugProduct = new DrugProduct();
					
					

					drugProduct.setConceptId(cs.getConcept(Integer.valueOf(id)));
					
					storeqnty = service.getCurrSolde(null, consneeded, cmdDrug.getLocationId().getLocationId()+"", null, null, cmdDrug.getCmddrugId()+"");
					drugProduct.setStoreQnty(storeqnty);
					drugProduct.setQntyReq(Integer.parseInt(consneeded));
					drugProduct.setCmddrugId(cmdDrug);
					service.saveDrugProduct(drugProduct);
					hasSaved = true;
					count++;
				} else {
					mav.addObject("msg", "You have to enter at least one drug or cunsumable");
				}
			}
			
			if(hasSaved) {
				mav.addObject("msg", "pharmacymanagement.drugorder.save");				
			} 
		}
		
		SortedSet<Map.Entry<Integer, String>> sortedDrugs = Utils.SortMapValues(drugMap);
		List<ConceptAnswer> consumables = new ArrayList<ConceptAnswer>(css);
		
		List<ConceptAnswer> sortedConsumable = Utils.sortConsumable(consumables);
		
		map.put("drugs", sortedDrugs);
		mav.addObject("products", products);
		mav.addObject("css", Utils.sortConsumable(consumables));
		mav.addAllObjects(map);
		mav.addObject("locations", locations);
		mav.addObject("dftLoc", dftLoc);
		mav.setViewName("module/pharmacymanagement/drugStoreForm");
		return mav;
	}
}
