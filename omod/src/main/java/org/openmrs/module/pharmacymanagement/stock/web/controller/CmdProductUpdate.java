package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.CmdDrug;
import org.openmrs.module.pharmacymanagement.ConsumableDispense;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.PharmacyConstants;
import org.openmrs.module.pharmacymanagement.PharmacyInventory;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

public class CmdProductUpdate extends ParameterizableViewController {
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		LocationService ls = Context.getLocationService();
		DrugOrderService dos = Context.getService(DrugOrderService.class);
		List<Location> locations = ls.getAllLocations();
		ConceptService cs = Context.getConceptService();
		List<Drug> drugs = new ArrayList<Drug>();
		Collection<ConceptAnswer> consumable = new ArrayList<ConceptAnswer>();
		SimpleDateFormat sdf;
        if(Context.getLocale().toString().equals("en_US") || Context.getLocale().toString().equals("en")) {
			sdf = new SimpleDateFormat("MM/dd/yyyy");
		}
		else {
			sdf = new SimpleDateFormat("dd/MM/yyyy");
		}
		
		Collection<PharmacyInventory> piList = dos.getAllPharmacyInventory();
		
		if(request.getParameter("cdId") != null && !request.getParameter("cdId").equals("")) {
			Collection<ConceptAnswer> services = cs.getConcept(6702).getAnswers();
			
			Map<Integer, DrugProduct> map = new HashMap<Integer, DrugProduct>();
			for(PharmacyInventory pi : piList) {
				if(pi.getDrugproductId().getConceptId() != null) {				
					int currSolde = dos.getCurrSoldeDisp(null, pi.getDrugproductId().getConceptId().getConceptId()+"", pi.getDrugproductId().getCmddrugId().getPharmacy().getPharmacyId()+"", null, null, null);
					if(currSolde > 0) {
						if(pi.getDrugproductId().getConceptId() != null)
							map.put(pi.getDrugproductId().getConceptId().getConceptId(), pi.getDrugproductId());
					}
				}
			}
			
			ConsumableDispense cd = Utils.getConsumableDispense(Integer.valueOf(request.getParameter("cdId")));
			
			mav.addObject("services", services);
			mav.addObject("map", map);
			mav.addObject("cd", cd);
//			mav.setView(new RedirectView("consumabledispensation.htm"));
//			return mav;			
		}
		
		
		String dpId = null;
		DrugProduct drugproduct = new DrugProduct();
		Location location = null;
		CmdDrug cmddrug = null;
		if(request.getParameter("dpId") != null && !request.getParameter("dpId").equals("")) {
			dpId = request.getParameter("dpId");
			drugproduct= dos.getDrugProductById(Integer.valueOf(dpId));
			cmddrug = drugproduct.getCmddrugId();
		}
		
		if(request.getParameter("on") != null && !request.getParameter("on").equals("")) {
//			String qtyRec = request.getParameter("qtyRec");
			//setting the CmdDrug
			Date date = sdf.parse(request.getParameter("reqDate"));
			location = ls.getLocation(Integer.valueOf(request.getParameter("location")));
			cmddrug.setDestination(location);
			cmddrug.setSupportingProg(request.getParameter("supportingProgram"));
			cmddrug.setMonthPeriod(date);
			dos.saveCmdDrug(cmddrug);
			
			//setting the DrugProduct
			if(drugproduct.getDrugId() != null) {
				Drug drug = cs.getDrug(Integer.valueOf(request.getParameter("drugCons")));
				drugproduct.setDrugId(drug);
			} else {
				Concept concept = cs.getConcept(Integer.valueOf(request.getParameter("drugCons")));
				drugproduct.setConceptId(concept);
			}
//			drugproduct.setDeliveredQnty(Integer.valueOf(qtyRec));
			drugproduct.setQntyReq(Integer.valueOf(request.getParameter("qntyReq")));
			dos.saveDrugProduct(drugproduct);
			
//			int currSoldeDisp = 0;
//			int currSolde = 0;
//			if(drugproduct.getDrugId() != null) {
//				currSoldeDisp = dos.getCurrSoldeDisp(drugproduct.getDrugId().getDrugId()+"", null, cmddrug.getPharmacy().getPharmacyId()+"", drugproduct.getExpiryDate()+"", drugproduct.getLotNo(), 1+"");
//				currSolde = dos.getCurrSolde(drugproduct.getDrugId().getDrugId()+"", null, cmddrug.getDestination().getLocationId()+"", drugproduct.getExpiryDate()+"", drugproduct.getLotNo(), 1+"");
//			} else {
//				currSoldeDisp = dos.getCurrSoldeDisp(drugproduct.getConceptId().getConceptId()+"", null, cmddrug.getPharmacy().getPharmacyId()+"", drugproduct.getExpiryDate()+"", drugproduct.getLotNo(), 1+"");
//				currSolde = dos.getCurrSolde(drugproduct.getConceptId().getConceptId()+"", null, cmddrug.getDestination().getLocationId()+"", drugproduct.getExpiryDate()+"", drugproduct.getLotNo(), 1+"");
//			}
//			int solde = 0;
//			int soldeDisp = 0;
//			if(cmddrug.getPharmacy() != null) {
//				PharmacyInventory pi = Utils.getPharmacyInventoryByDrugProduct(drugproduct.getDrugproductId());
//				DrugProductInventory dpi = Utils.getDrugProductInventoryByDrugProduct(drugproduct.getDrugproductId());
//				pi.setEntree(Integer.valueOf(qtyRec));
//				pi.setSortie(0);
//				soldeDisp = currSoldeDisp + Integer.valueOf(qtyRec);
//				pi.setSolde(soldeDisp);
//				
//				dpi.setEntree(0);
//				dpi.setSortie(Integer.valueOf(qtyRec));
//				solde = currSolde - Integer.valueOf(qtyRec);
//				dpi.setSolde(solde);
//				
//				dos.savePharmacyInventory(pi);
//				dos.saveInventory(dpi);
//			} else {
//				DrugProductInventory dpi = Utils.getDrugProductInventoryByDrugProduct(drugproduct.getDrugproductId());
//
//				dpi.setEntree(Integer.valueOf(qtyRec));
//				dpi.setSortie(0);
//				solde = currSolde + Integer.valueOf(qtyRec);
//				dpi.setSolde(solde);
//				dos.saveInventory(dpi);
//			}
			
			
			mav.setView(new RedirectView("order.list?orderId=" + cmddrug.getCmddrugId() + "&status=updated"));
			return mav;
		}
		if(drugproduct.getDrugId() != null) {
			drugs = cs.getAllDrugs();
			mav.addObject("drugs", drugs);
		} else {
			try {
				consumable = cs.getConcept(Integer.parseInt(Context.getAdministrationService().getGlobalProperty("pharmacymanagement.CONSUMABLE"))).getAnswers();
			} catch (NullPointerException npe) {
				mav.addObject("msg", "pharmacymanagement.noConsumable");
			}
			mav.addObject("consumable", consumable);
		}
		
		mav.addObject("locations", locations);
		mav.addObject("drugproduct", drugproduct);
		mav.setViewName(getViewName());
		return mav;
	}
}
