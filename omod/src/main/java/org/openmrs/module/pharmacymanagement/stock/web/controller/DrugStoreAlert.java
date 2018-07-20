package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.StoreWarning;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class DrugStoreAlert extends ParameterizableViewController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		StoreWarning sw;
		List<StoreWarning> swList;
		List<DrugProduct> dpList;
		ModelAndView mav = new ModelAndView();

		DrugOrderService dos = Context.getService(DrugOrderService.class);
		swList = new ArrayList<StoreWarning>();
		
		dpList = dos.getProductListAboutToExpiry();
				
		Collection<DrugProduct> drugproductList = dos.getAllProducts();
		Collection<DrugProduct> drugproducts = new ArrayList<DrugProduct>();
		Collection<String> dpTemp = new ArrayList<String>();
		
		for(DrugProduct dp : drugproductList) {
			if(dp.getDrugId() != null) {
				if(!dpTemp.contains(dp.getDrugId().getName()+""+dp.getLotNo()) && dp.getIsDelivered()) {
					dpTemp.add(dp.getDrugId().getName()+""+dp.getLotNo());
					drugproducts.add(dp);
				}
			} else {
				if(!dpTemp.contains(dp.getConceptId().getName().getName()) && dp.getIsDelivered()) {
					dpTemp.add(dp.getConceptId().getName().getName());
					drugproducts.add(dp);
				}
			}
		}
		
		for (DrugProduct dpr : drugproducts) {

			sw = new StoreWarning();
			List<Object[]> calculations = null;
			if(dpr.getDrugId() != null)
				calculations = dos.getdrugStatistics(dpr.getDrugId().getDrugId()+"", null);
			else
				calculations = dos.getdrugStatistics(null, dpr.getConceptId().getConceptId()+"");
			
			String locString = null;
			if(dpr.getCmddrugId() != null) {
				locString = dpr.getCmddrugId().getPharmacy() != null ? dpr.getCmddrugId().getPharmacy().getLocationId().getLocationId()+"" : dpr.getCmddrugId().getLocationId().getLocationId()+"";
			} else {
				locString = dos.getReturnStockByDP(dpr).get(0).getDestination().getLocationId()+"";
			}
			
			 
			int solde = 0;
			if(dpr.getDrugId() != null)
				solde = dos.getCurrSolde(dpr.getDrugId().getDrugId()+"", null, locString, dpr.getExpiryDate()+"", dpr.getLotNo(), null);
			else
				solde = dos.getCurrSolde(null, dpr.getConceptId().getConceptId()+"", locString, null, null, null);
			
			if (calculations != null) {
				for (Object[] obj : calculations) {
					try {
						if (obj[2].toString() != null && !obj[2].equals(""))
							sw.setConsumed(Integer.valueOf(obj[2].toString()));
					} catch (NullPointerException e) {
					}					
				}
				
				if( solde <= Utils.getTheConsommationMaximumMoyenne("HC", dpr)) {
					if(dpr.getDrugId() != null) {
						sw.setDrugName(dpr.getDrugId().getName());
						sw.setLotNo(dpr.getLotNo());
					} else {
						sw.setDrugName(dpr.getConceptId().getName().getName());
					}
					sw.setStore(solde);
					swList.add(sw);
				}
			}		
		}		

		mav.addObject("swList", swList);
		mav.addObject("dpList", dpList);

		mav.setViewName(getViewName());
		return mav;
	}
}
