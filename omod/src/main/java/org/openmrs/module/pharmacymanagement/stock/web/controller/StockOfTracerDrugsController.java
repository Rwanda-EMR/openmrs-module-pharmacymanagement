package org.openmrs.module.pharmacymanagement.stock.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Location;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.HMISStockOfTracerDrugs;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class StockOfTracerDrugsController extends ParameterizableViewController {
		
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		DrugOrderService service = Context.getService(DrugOrderService.class);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String startDateStr = null;
		String endDateStr = null;
		Date startDate = null;
		Date endDate = null;
		Date sDate = null;
		Date eDate = null;
		String sDateStr = null;
		String eDateStr = null;

		AdministrationService adminService = Context.getAdministrationService();
		String drugListStr = adminService.getGlobalProperty("pharmacymanagement.tracerDrugs");
		String[] drugArray = drugListStr.split(",");
		Location dftLoc = null;
		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = Context.getLocationService().getLocation(Integer.valueOf(locationStr));
		} catch (Exception e) {
			mav
					.addObject(
							"msg",
							"pharmacymanagement.missingDftLoc");
		}
		
		List<HMISStockOfTracerDrugs> reportList = null;
		
		if(request.getParameter("startDate") != null && !request.getParameter("startDate").equals("")
				&& request.getParameter("endDate") != null && !request.getParameter("endDate").equals("")) {
			
			startDateStr = request.getParameter("startDate");
			endDateStr = request.getParameter("endDate");
			
			startDate = sdf.parse(startDateStr);
			endDate = sdf.parse(endDateStr);
			
			int startYear = startDate.getYear() + 1900;
			int startMonth = startDate.getMonth() + 1;
			int startDay = startDate.getDate();
			
			int endYear = endDate.getYear() + 1900;
			int endMonth = endDate.getMonth() + 1;
			int endDay = endDate.getDate();
			
			sDateStr = startYear + "-" + startMonth + "-" + startDay;
			eDateStr = endYear + "-" + endMonth + "-" + endDay;
			
			sDate = sdf1.parse(sDateStr);
			eDate = sdf1.parse(eDateStr);
			
			int drugId = 0;
			int edl = 0;
			int dispensed = 0;
			int solde = 0;
			int stockout = 0;
			reportList = new ArrayList<HMISStockOfTracerDrugs>();
				
			for(int i = 0; i < drugArray.length; i++) {
				/** -- Drug Dispensed --- **/
				Object dispensedObj = service.getReceivedDispensedDrug(sDateStr, eDateStr, drugArray[i] + "", null)[1];
				if(dispensedObj != null)
					dispensed = Integer.valueOf(dispensedObj + "");
					
				/** --- Solde at End of month --- **/
				Object soldeObj = service.getSoldeByToDrugLocation(eDateStr, drugArray[i] + "", null, dftLoc.getLocationId() + "");
				if(soldeObj != null)
					solde = Integer.valueOf(soldeObj + "");
				
				/** --- Stock out --- **/
				if(Utils.getDrugStoreProductByDrugId(Integer.valueOf(drugArray[i] + "")) != null)
					stockout = Utils.stockOut(Utils.getDrugStoreProductByDrugId(Integer.valueOf(drugArray[i] + "")), endYear, endMonth, dftLoc.getLocationId() + "");
		
				
				drugId = Integer.valueOf(drugArray[i] + "");
				/** --- Expired, Damaged & Lost --- **/
				String drugName = Context.getConceptService().getDrug(drugId).getName();
				
				edl = Utils.getExpiredDrugByDatesAndLocation(sDate, eDate, dftLoc, drugId);
				
				reportList.add(new HMISStockOfTracerDrugs(dispensed, edl, solde, stockout, drugName));
//				endDayStart = Utils.getLastDayOfMonth(year, month)
//				for(int j = startDay; j <= endDay; j++) {
//					Utils.getDamagedLostDrugs(drugArray[i], new Date(startYear + "-" + startMonth + "-" + j))
//				}
//				Damaged: select * from pharmacymanagement_arv_return_store where observation like ("%damaged%") and ret_date >= "start_date" and ret_date =< "end_date";
//				Lost: select * from pharmacymanagement_arv_return_store where observation like ("%lost%") and ret_date >= "start_date" and ret_date =< "end_date";
				drugId = 0;
				edl = 0;
				dispensed = 0;
				solde = 0;
				stockout = 0;
			}
		}
		
		
		mav.addObject("reportList", reportList);
		mav.setViewName(getViewName());
		
		return mav;
	}

}
