package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class OrderUpdate extends ParameterizableViewController {
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		if(request.getParameter("ordre") != null) {
			DrugOrderService service = Context.getService(DrugOrderService.class);
			DrugProduct dp = new DrugProduct();
			dp.setDrugproductId(Integer.valueOf(request.getParameter("ordre")));
			dp.setDeliveredQnty(Integer.valueOf(request.getParameter("qntAcc")));
			dp.setLotNo(request.getParameter("noLot"));
			
			String strDate = request.getParameter("expDate");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(strDate);
			dp.setExpiryDate(date);
			
			service.saveDrugProduct(dp);
			
			mav.addObject("msg", "The order has been updated successfully");
			
		}
		mav.setViewName(getViewName());
		return mav;
	}

}
