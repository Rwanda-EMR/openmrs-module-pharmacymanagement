/**
 * Auto generated file comment
 */
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
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.ConceptAnswer;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.CmdDrug;
import org.openmrs.module.pharmacymanagement.Consommation;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.DrugProductInventory;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.PharmacyInventory;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class DisplayDrugOrders extends ParameterizableViewController {
	private Log log = LogFactory.getLog(this.getClass());

	@SuppressWarnings("deprecation")
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String dispConf = Context.getAdministrationService().getGlobalProperty(
				"pharmacymanagement.periodDispense");
		SimpleDateFormat sdf;

		if(request.getLocale().toString().equals("en_US"))
			sdf = new SimpleDateFormat("MM/dd/yyyy");
		else
			sdf = new SimpleDateFormat("dd/MM/yyyy");
		LocationService locationService = Context.getLocationService();
		ConceptService conceptService = Context.getConceptService();
		List<Drug> drugs = conceptService.getAllDrugs();
		List<Location> locations;
		int total = 0;
		int total1 = 0;
		int lentProd=0;
		int borrowedProd;
		Date invDate = null;
		DrugOrderService service;
		int currSolde, currentSolde = 0;
		Map<String, Consommation> drugMap = new HashMap<String, Consommation>();
		Map<String, Consommation> consommationMap = new HashMap<String, Consommation>();
		HttpSession httpSession = request.getSession();
		ModelAndView mav = new ModelAndView();
		DrugProduct prodFromLot = null;
		Object obQntyRec = null;
		Object obQntyConsomMens = null;
		int retProd = 0;
		DrugProduct dpStockout = null;

		Collection<ConceptAnswer> consumers = null;
		List<ConceptAnswer> consumerList = null;
		try {
			consumers = conceptService.getConcept(Integer.parseInt(Context.getAdministrationService().getGlobalProperty("pharmacymanagement.CONSUMABLE"))).getAnswers();
			consumerList = new ArrayList<ConceptAnswer>(consumers);

		} catch (NullPointerException npe) {
			mav.addObject("msg", "No consumable in the system");
		}

		Location dftLoc = null;
		String locationStr = Context.getAuthenticatedUser().getUserProperties()
				.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

		try {
			dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
			mav.addObject("dftLoc", dftLoc);
		} catch (Exception e) {
			mav.addObject("msg", "pharmacymanagement.missingDftLoc");
		}

		locations = locationService.getAllLocations();

		service = Context.getService(DrugOrderService.class);

		if (request.getParameter("qntAcc") != null) {

			DrugProductInventory dpi = new DrugProductInventory();
			DrugProductInventory dpiCurrSortie = new DrugProductInventory();

			int prodId = Integer.valueOf(request.getParameter("ordre")
					.toString());
			int orderId = Integer.valueOf(request.getParameter("orderId"));
			int qntAcc = Integer.parseInt(request.getParameter("qntAcc"));
			String noLot = null;

			DrugProduct dp = service.getDrugProductById(prodId);

			if (request.getParameter("noLotStock") != null
					&& !request.getParameter("noLotStock").equals(""))
				noLot = request.getParameter("noLotStock");

			String strDate = null;
			String dateStr = null;
			if (request.getParameter("expDate") != null
					&& !request.getParameter("expDate").equals("")) {
				strDate = request.getParameter("expDate");
				String[] strDateArr = strDate.split("/");
				dateStr = strDateArr[2] + "-" + strDateArr[1] + "-"
						+ strDateArr[0];
			}

			if (request.getParameter("prodFromLot") != null
					&& !request.getParameter("prodFromLot").equals("")) {
				prodFromLot = service.getDrugProductById(Integer
						.valueOf(request.getParameter("prodFromLot")));
				noLot = prodFromLot.getLotNo();
			}

			CmdDrug cmddrug = service.getCmdDrugById(orderId);

			if (cmddrug.getLocationId() != null) {
				if (dp.getDrugId() != null)
					currSolde = service.getCurrSolde(dp.getDrugId().getDrugId()
							+ "", null, cmddrug.getLocationId().getLocationId()
							+ "", dateStr, noLot, null);
				else
					currSolde = service.getCurrSolde(null, dp.getConceptId()
							.getConceptId()
							+ "", cmddrug.getLocationId().getLocationId() + "",
							dateStr, noLot, null);

				if (dp.getDrugId() != null)
					currentSolde = service.getCurrSolde(dp.getDrugId()
							.getDrugId()
							+ "", null, cmddrug.getDestination()
							.getLocationId()
							+ "", dateStr, noLot, null);
				else
					currentSolde = service.getCurrSolde(null, dp.getConceptId()
							.getConceptId()
							+ "", cmddrug.getDestination().getLocationId()
							.toString(), dateStr, noLot, null);
			} else {
				if (dp.getDrugId() != null) {
					currSolde = service.getCurrSolde(dp.getDrugId().getDrugId()
							+ "", null, cmddrug.getPharmacy().getLocationId()
							.getLocationId()
							+ "", dateStr, noLot, null);
				} else {
					currSolde = service.getCurrSolde(null, dp.getConceptId()
							.getConceptId()
							+ "", cmddrug.getPharmacy().getLocationId()
							.getLocationId().toString(), dateStr, noLot, null);
				}
			}

			if (qntAcc <= dp.getQntyReq()) {
				if (request.getParameter("invDate") != null
						&& !request.getParameter("invDate").equals("")) {
					String inventoryDateStr = request.getParameter("invDate");
					invDate = sdf.parse(inventoryDateStr);
				}

				dpi.setInventoryDate(invDate);

				Location lcation = null;

				if (dp.getCmddrugId() != null) {
					lcation = dp.getCmddrugId().getLocationId();
				} else {
					lcation = service.getReturnStockByDP(dp).get(0)
							.getDestination();
				}

				// when operating on the level of the main store
				if (lcation != null) {
					if (locations.contains(lcation)) {
						dpi.setEntree(qntAcc);
						dpi.setIsStore(true);
						total = currSolde + qntAcc;
					}
					if (cmddrug.getDestination().getLocationId() == dftLoc
							.getLocationId()
							&& cmddrug.getLocationId().getLocationId() != null) {
						dpiCurrSortie.setInventoryDate(invDate);
						dpiCurrSortie.setSortie(qntAcc);
						dpiCurrSortie.setIsStore(true);
						total1 = currentSolde - qntAcc;

					}
				} else {
					// operating on the level of the pharmacy(dispensing)
					int currStat = 0;
					if (dp.getDrugId() != null)
						currStat = service.getCurrSoldeDisp(dp.getDrugId()
								.getDrugId()
								+ "", null, cmddrug.getPharmacy()
								.getPharmacyId()
								+ "", dateStr, noLot, null);
					else
						currStat = service.getCurrSoldeDisp(null, dp
								.getConceptId().getConceptId()
								+ "", cmddrug.getPharmacy().getPharmacyId()
								+ "", dateStr, noLot, null);

					dpi.setSortie(qntAcc);
					dpi.setIsStore(true);
					total = currSolde - qntAcc;
					int solde = qntAcc + currStat;

					// saving in the pharmacy inventory table
					if (solde >= 0 && dp.getCmddrugId().getPharmacy() != null) {
						PharmacyInventory pi = new PharmacyInventory();
						pi.setDate(invDate);
						pi.setEntree(qntAcc);
						pi.setSortie(0);
						pi.setDrugproductId(dp);
						pi.setSolde(solde);
						if (total >= 0) {
							service.savePharmacyInventory(pi);
						}
					}
				}

				if (total >= 0) {
					dp.setDeliveredQnty(qntAcc);
					dp.setLotNo(noLot);
					if (strDate != null) {
						Date date = sdf.parse(strDate);
						dp.setExpiryDate(date);
					}

					dp.setIsDelivered(true);
					dp.setCmddrugId(cmddrug);

					dpi.setDrugproductId(dp);
					dpi.setSolde(total);
					service.saveDrugProduct(dp);
					service.saveInventory(dpi);
					mav.addObject("msg",
							"The order has been updated successfully");

				} else if (total1 >= 0
						&& cmddrug.getDestination().getLocationId() == dftLoc
								.getLocationId()
						&& cmddrug.getLocationId() != null) {
					dpiCurrSortie.setDrugproductId(dp);
					dpiCurrSortie.setSolde(total1);
					service.saveInventory(dpiCurrSortie);
				} else {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
							"The quantity requested is greater than what is in store: "+currentSolde);
				}
			} else {
				httpSession
						.setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
								"The Given Quantity has to be less or equal to the number requested");
			}

		}

		// displaying orders
		if (request.getParameter("orderId") != null) {
			int id = Integer.parseInt(request.getParameter("orderId"));
			CmdDrug cmddrug = service.getCmdDrugById(id);
			String from = cmddrug.getMonthPeriod() + "";

			int gregMonth = cmddrug.getMonthPeriod().getMonth();
			int month = cmddrug.getMonthPeriod().getMonth() + 1;
			int year = cmddrug.getMonthPeriod().getYear() + 1900;
			int lastDay = Utils.getLastDayOfMonth(year, gregMonth);
			String toString = year + "-" + month + "-" + lastDay;

			mav.addObject("cmdDrug", cmddrug);
			Collection<DrugProduct> drugProducts = service
					.getDrugProductByCmdDrugId(cmddrug);

			if (isFalseIn(drugProducts)) {
				cmddrug.setIsAchieved(true);
				service.saveCmdDrug(cmddrug);
			}

			/*for (DrugProduct dp : drugProducts) {
				Consommation drugReq = new Consommation();
				Consommation consReq = new Consommation();
				retProd = Utils.getReturnedProductDuringTheMonth(cmddrug
						.getMonthPeriod(), dp);*/
			for (DrugProduct dp : drugProducts)
			{
				log.info("Start loop *********************************************: " + new Date());

				Consommation drugReq = new Consommation();
				Consommation consReq = new Consommation();
				log.info("Start getLentDrugsDuringTheMonth(): " + new Date());
				lentProd = Utils.getLentDrugsDuringTheMonth(cmddrug.getMonthPeriod(), dp).intValue();
				log.info("End getLentDrugsDuringTheMonth(): " + new Date());


				log.info("Start getBorrowedDrugsDuringTheMonth(): " + new Date());
				borrowedProd = Utils.getBorrowedDrugsDuringTheMonth(cmddrug.getMonthPeriod(), dp).intValue();
				log.info("End getBorrowedDrugsDuringTheMonth(): " + new Date());


				if (dp.getDrugId() != null) {
					drugReq.setReturnedProduct(retProd);
					drugReq.setDrugName(dp.getDrugId().getName());
					drugReq.setDrugId(dp.getDrugId().getDrugId() + "");
					drugReq.setConditUnitaire(dp.getDrugId().getUnits() + "");
					if (dp.getExpiryDate() != null)
						drugReq.setExpirationDate(dp.getExpiryDate());
				} else {
					consReq.setReturnedProduct(0);
					consReq.setDrugName(dp.getConceptId().getName().getName());
					consReq.setConceptId(dp.getConceptId().getConceptId() + "");
					consReq.setConditUnitaire("");
				}

				if (cmddrug.getLocationId() != null) {
					if (dp.getDrugId() != null) {
						dpStockout = dp;
					} else {
						dpStockout = dp;
					}

					// String pharmacyId = null;
					// try {
					// pharmacyId = cmddrug.getPharmacy().getPharmacyId() + "";
					// } catch (NullPointerException npe) {
					// }

					/**
					 * TO DO check on line 338 and see why it is returnin an
					 * error of index out of bound
					 */
					List<Pharmacy> pharmaList = service
							.getPharmacyByLocation(dftLoc);
					String pharmaStr = "";
					for (int i = 0; i < pharmaList.size(); i++) {
						pharmaStr += pharmaList.get(i).getPharmacyId();
						if (i != (pharmaList.size() - 1)) {
							pharmaStr += ",";
						}
					}

					if (dp.getDrugId() != null) {
						String fromStr = Utils.DispensingConfig(Integer
								.valueOf(dispConf), toString);
						log.info("Start Quantity Consumed Mensually: "
								+ new Date());
						obQntyConsomMens = service.getReceivedDispensedDrug(
								fromStr, toString, dp.getDrugId().getDrugId()
										+ "", pharmaStr)[1];
						log.info("End Quantity Consumed Mensually: "
								+ new Date());

						if (obQntyConsomMens != null)
							drugReq.setQntConsomMens(obQntyConsomMens);
						else
							drugReq.setQntConsomMens(0);
					} else {
						consReq.setQntConsomMens(0);
					}

					int e = 0;
					if (dp.getDrugId() != null) {
						log.info("Start Physical Count: " + new Date());
						e = service.getSoldeByToDrugLocation(toString, dp
								.getDrugId().getDrugId()
								+ "", null, cmddrug.getLocationId()
								.getLocationId()
								+ "");
						log.info("End Physical Count: " + new Date());
					} else {
						log.info("Start Physical Count: " + new Date());
						e = service.getSoldeByToDrugLocation(toString, null, dp
								.getConceptId().getConceptId()
								+ "", cmddrug.getLocationId().getLocationId()
								+ "");
						log.info("End Physical Count: " + new Date());
					}

					int c = 0;
					if (dp.getDrugId() != null)
						c = Integer.parseInt(drugReq.getQntConsomMens() + "");
					else
						c = Integer.parseInt(consReq.getQntConsomMens() + "");

					int f = Utils.stockOut(dpStockout, year, month, dftLoc
							.getLocationId()
							+ "");
					int g = 0;
					try {
						g = (c * 30) / (30 - f);
					} catch (ArithmeticException ae) {
						log.error(ae.getStackTrace());
					}
					int h = 2 * g;
					int i = h - e;
					if (dp.getDrugId() != null) {
						log.info("Start Solde: " + new Date());
						drugReq.setQntPremJour(service
								.getSoldeByFromDrugLocation(from, dp
										.getDrugId().getDrugId()
										+ "", null, cmddrug.getLocationId()
										.getLocationId()
										+ ""));
						log.info("End Solde: " + new Date());
						drugReq.setQntRestMens(e);
					} else {
						log.info("Start Solde: " + new Date());
						consReq.setQntPremJour(service
								.getSoldeByFromDrugLocation(from, null, dp
										.getConceptId().getConceptId()
										+ "", cmddrug.getLocationId()
										.getLocationId()
										+ ""));
						log.info("End Solde: " + new Date());
						consReq.setQntRestMens(e);
					}

					if (dp.getDrugId() != null) {
						log.info("Start Quantity Received Mensually: "
								+ new Date());
						obQntyRec = service
								.getSumEntreeSortieByFromToDrugLocation(from,
										toString, dp.getDrugId().getDrugId()
												+ "", null, cmddrug
												.getLocationId()
												.getLocationId()
												+ "")[0];
						log.info("End Quantity Received Mensually: "
								+ new Date());
						if (obQntyRec != null)
							drugReq.setQntRecuMens(obQntyRec);
						else
							drugReq.setQntRecuMens(0);

						drugReq.setLocationId(cmddrug.getLocationId()
								.getLocationId());
						drugReq.setDrugProduct(dp);

						drugReq.setStockOut(Utils.stockOut(dpStockout, year,
								month, dftLoc.getLocationId() + ""));
						drugReq.setAdjustMonthlyConsumption(g);
						drugReq.setMaxQnty(h);
						drugReq.setQntyToOrder(i);
					} else {
						log.info("Start Quantity Received Mensually: "
								+ new Date());
						obQntyRec = service
								.getSumEntreeSortieByFromToDrugLocation(from,
										toString, null, dp.getConceptId()
												.getConceptId()
												+ "", cmddrug.getLocationId()
												.getLocationId()
												+ "")[0];
						log.info("End Quantity Received Mensually: "
								+ new Date());
						if (obQntyRec != null)
							consReq.setQntRecuMens(obQntyRec);
						else
							consReq.setQntRecuMens(0);

						consReq.setLocationId(cmddrug.getLocationId()
								.getLocationId());
						consReq.setDrugProduct(dp);

						consReq.setStockOut(Utils.stockOut(dpStockout, year,
								month, dftLoc.getLocationId() + ""));
						consReq.setAdjustMonthlyConsumption(g);
						consReq.setMaxQnty(h);
						consReq.setQntyToOrder(i);
					}
				} else {
					if (dp.getDrugId() != null) {
						dpStockout = dp;
					} else {
						dpStockout = dp;
					}
					if (dp.getDrugId() != null) {
						String fromStr = Utils.DispensingConfig(Integer
								.valueOf(dispConf), toString);
						log.info("Start Quantity Received Mensually: "
								+ new Date());
						obQntyConsomMens = service.getReceivedDispensedDrug(
								fromStr, toString, dp.getDrugId().getDrugId()
										+ "", cmddrug.getPharmacy()
										.getPharmacyId()
										+ "")[1];
						log.info("End Quantity Received Mensually: "+new Date());
						
						if (obQntyConsomMens != null)
							drugReq.setQntConsomMens(obQntyConsomMens);
						else
							drugReq.setQntConsomMens(0);
					} else {
						consReq.setQntConsomMens(0);
					}

					int e = 0;
					int c = 0;
					if (dp.getDrugId() != null) {
						log.info("Start Physical Count: "+new Date());
						e = service.getPharmacySoldeLastDayOfWeek(toString, dp
								.getDrugId().getDrugId()
								+ "", null, dp.getCmddrugId().getPharmacy()
								.getPharmacyId()
								+ "");
						log.info("Start Physical Count: "+new Date());
						c = Integer.parseInt(drugReq.getQntConsomMens()
								.toString());
					} else {
						log.info("Start Physical Count: "+new Date());
						e = service.getPharmacySoldeLastDayOfWeek(toString,
								null, dp.getConceptId().getConceptId() + "", dp
										.getCmddrugId().getPharmacy()
										.getPharmacyId()
										+ "");
						log.info("End Physical Count: "+new Date());
						c = Integer.parseInt(consReq.getQntConsomMens()
								.toString());
					}

					int f = Utils.stockOut(dpStockout, year, month, dftLoc
							.getLocationId()
							+ "");
					int g = 0;
					try {
						g = (c * 30) / (30 - f);
					} catch (ArithmeticException ae) {
						log.error(ae.getStackTrace());
					}
					int h = 2 * g;
					int i = h - e;
					if (dp.getDrugId() != null) {
						log.info("Start Quantite Premiere Jour: "+new Date());
						drugReq.setQntPremJour(service
								.getPharmacySoldeFirstDayOfWeek(from, dp
										.getDrugId().getDrugId()
										+ "", null, dp.getCmddrugId()
										.getPharmacy().getPharmacyId()
										+ ""));
						log.info("End Quantite Premiere Jour: "+new Date());
						
						drugReq.setQntRestMens(e);
					} else {
						log.info("Start Quantite Premiere Jour: "+new Date());
						consReq.setQntPremJour(service
								.getPharmacySoldeFirstDayOfWeek(from, null, dp
										.getConceptId().getConceptId()
										+ "", dp.getCmddrugId().getPharmacy()
										.getPharmacyId()
										+ ""));
						log.info("End Quantite Premiere Jour: "+new Date());
						consReq.setQntRestMens(e);
					}

					if (dp.getDrugId() != null) {
						log.info("Start Quantite Recu Mensuelle: "+new Date());
						obQntyRec = service.getReceivedDispensedDrug(from,
								toString,
								dp.getDrugId().getDrugId().toString(), dp
										.getCmddrugId().getPharmacy()
										.getPharmacyId()
										+ "")[0];
						log.info("End Quantite Recu Mensuelle: "+new Date());

						if (obQntyRec != null)
							drugReq.setQntRecuMens(obQntyRec);
						else
							drugReq.setQntRecuMens(0);

						drugReq.setLocationId(cmddrug.getPharmacy()
								.getLocationId().getLocationId());
						drugReq.setDrugProduct(dp);
						drugReq.setStockOut(Utils.stockOut(dpStockout, year,
								month, dftLoc.getLocationId() + ""));
						drugReq.setAdjustMonthlyConsumption(g);
						drugReq.setMaxQnty(h);
						drugReq.setQntyToOrder(i);
					} else {
						log.info("Start Quantite Recu Mensuelle: "+new Date());
						obQntyRec = service.getReceivedDispensedDrug(from,
								toString,
								dp.getConceptId().getConceptId() + "", dp
										.getCmddrugId().getPharmacy()
										.getPharmacyId()
										+ "")[0];
						log.info("End Quantite Recu Mensuelle: "+new Date());

						if (obQntyRec != null)
							consReq.setQntRecuMens(obQntyRec);
						else
							consReq.setQntRecuMens(0);

						consReq.setLocationId(cmddrug.getPharmacy()
								.getLocationId().getLocationId());
						consReq.setDrugProduct(dp);
						consReq.setStockOut(Utils.stockOut(dpStockout, year,
								month, dftLoc.getLocationId() + ""));
						consReq.setAdjustMonthlyConsumption(g);
						consReq.setMaxQnty(h);
						consReq.setQntyToOrder(i);
					}
				}

				String key = "";
				if (cmddrug.getLocationId() != null) {
					if (drugReq.getDrugId() != null) {
						key = drugReq.getDrugId().toString() + "_"
								+ cmddrug.getLocationId().getLocationId();
						drugMap.put(key, drugReq);
					} else {
						key = consReq.getConceptId().toString() + "_"
								+ cmddrug.getLocationId().getLocationId();
						consommationMap.put(key, consReq);
					}
				} else {
					if (drugReq.getDrugId() != null) {
						key = drugReq.getDrugId().toString()
								+ "_"
								+ cmddrug.getPharmacy().getLocationId()
										.getLocationId();
						drugMap.put(key, drugReq);
					} else {
						key = consReq.getConceptId().toString()
								+ "_"
								+ cmddrug.getPharmacy().getLocationId()
										.getLocationId();
						consommationMap.put(key, consReq);
					}
				}
			}
		}

		mav.addObject("drugMap", drugMap);
		mav.addObject("consommationMap", consommationMap);
		mav.setViewName(getViewName());
		mav.addObject("now", new Date());
		String drugIdName = "";
		String locIdName = "";
		String consIdName = "";
		if (drugs.size() > 0)
			for (int i = 0; i < drugs.size(); i++) {
				drugIdName += drugs.get(i).getDrugId() + "_"
						+ drugs.get(i).getName();
				if (drugs.size() != (i + 1))
					drugIdName += ",";
			}
		for (int j = 0; j < locations.size(); j++) {
			locIdName += locations.get(j).getLocationId() + "_"
					+ locations.get(j).getName();
			if (locations.size() != (j + 1))
				locIdName += ",";
		}
		try {
			for (int k = 0; k < consumerList.size(); k++) {
				consIdName += consumerList.get(k).getAnswerConcept()
						.getConceptId()
						+ "_"
						+ consumerList.get(k).getAnswerConcept().getName()
								.getName();
				if (consumerList.size() != (k + 1))
					consIdName += ",";

				mav.addObject("consumerList", consIdName);
			}
		} catch (NullPointerException npe) {
			mav.addObject("msg", "No Consumables in the concept dictionary");
		}

		mav.addObject("drugs", drugIdName);
		mav.addObject("locations", locIdName);
		return mav;

	}

	public boolean isFalseIn(Collection<DrugProduct> pros) {
		boolean isContained = true;
		for (DrugProduct dp : pros) {
			if (!dp.getIsDelivered()) {
				isContained = false;
			}
		}
		return isContained;
	}
}
