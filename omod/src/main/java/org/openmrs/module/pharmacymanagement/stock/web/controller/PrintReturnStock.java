package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.ProductReturnStore;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;





public class PrintReturnStock
		extends ParameterizableViewController
{
	public PrintReturnStock() {}

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		ModelAndView mav = new ModelAndView();

		List<ProductReturnStore> returnStoreList = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);

		if ((request.getParameter("isChecked") != null) && (!request.getParameter("isChecked").equals("")) &&
				(request.getParameter("isChecked").equals("1")) &&
				(request.getParameter("retDate") != null) && (!request.getParameter("retDate").equals(""))) {
			Date date = sdf.parse(request.getParameter("retDate"));

			List<ProductReturnStore> lentListProduct = service.getReturnStockByDate(date, "Lend");
			List<ProductReturnStore> borrowedListProduct = service.getReturnStockByDate(date, "Borrow");

			returnStoreList.addAll(lentListProduct);
			returnStoreList.addAll(borrowedListProduct);

			System.out.println("Lend: " + lentListProduct.size() + " Borrow: " + borrowedListProduct.size() + " return: " + returnStoreList.size());

			for (ProductReturnStore ars : returnStoreList) {
				System.out.println("Pharmacy: " + ars.getOriginPharmacy());
				System.out.println("Location: " + ars.getOriginLocation());
			}
			mav.addObject("returnStoreList", returnStoreList);
		}


		return mav;
	}
}