/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.StoreWarning;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class DrugStoreMgt extends ParameterizableViewController {
		
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StoreWarning sw;
		ConceptService cs;
		List<Integer> drugIds;
		List<StoreWarning> swList;
		List<DrugProduct> dpList;
		ModelAndView mav = new ModelAndView();
		
		DrugOrderService dos = Context.getService(DrugOrderService.class);
		swList = new ArrayList<StoreWarning>();
		cs = Context.getConceptService();
		dpList = dos.getProductListAboutToExpiry();
		
		drugIds = dos.getDrugByNextVisitConcept();

		for (Integer drugId : drugIds) {
			sw = new StoreWarning();
			sw.setDrugName(cs.getDrug(drugId).getName());
			
			
			List<Object[]> calculations = dos.getdrugStatistics(drugId+"", null);

			if (calculations != null) {
				for (Object[] obj : calculations) {
					try {
					if (obj[2].toString() != null && !obj[2].equals(""))
						sw.setConsumed(Integer.valueOf(obj[2].toString()));

					if (obj[3].toString() != null && !obj[3].equals(""))
						sw.setStore(Integer.valueOf(obj[3].toString()));
					} catch (NullPointerException e) {
					}
					
				}

				swList.add(sw);
			}

		}
		
		
		mav.setViewName(getViewName());
		if(swList.size() > 0 || dpList.size() > 0) {
			mav.addObject("msg", "<div style='background-color: #B22222; color: white; font-weight: bold'>Check on alerts page, You might have a store run out or drug expiration !!!</div>");
		}
		
		mav.addObject("swList", swList);
		mav.addObject("dpList", dpList);
		
		return mav;
	}
}
