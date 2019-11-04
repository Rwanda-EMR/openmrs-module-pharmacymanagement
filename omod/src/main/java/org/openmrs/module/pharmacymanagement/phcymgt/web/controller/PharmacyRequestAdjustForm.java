package org.openmrs.module.pharmacymanagement.phcymgt.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.CmdDrug;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.ProductReturnStore;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.module.pharmacymanagement.utils.Utils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class PharmacyRequestAdjustForm
        extends ParameterizableViewController
{
    private Log log = LogFactory.getLog(getClass());

    public PharmacyRequestAdjustForm() {}

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        ModelAndView mav = new ModelAndView();
        int amountreq = 0;
        DrugProduct drugProduct = null;
        DrugProduct currDP = null;
        Drug drug = null;



        DrugOrderService serviceDrug = (DrugOrderService)Context.getService(DrugOrderService.class);
        LocationService locationService = Context.getLocationService();
        ConceptService cs = Context.getConceptService();

        Collection<Pharmacy> pharmacies = serviceDrug.getAllPharmacies();

        List<Location> locations = locationService.getAllLocations();

       // Map<Integer, DrugProduct> drugMap = new HashMap();
        Map<String, DrugProduct> drugMap = new HashMap();

        Map<Integer, DrugProduct> consumableMap = new HashMap();

        Collection<DrugProduct> dpList = serviceDrug.getAllProducts();
        Collection<DrugProduct> dpList1 = new ArrayList();
        Collection<DrugProduct> fromReturned = new ArrayList();

        for (DrugProduct dp : dpList) {
            try {
                if (dp.getCmddrugId() != null) {
                    dpList1.add(dp);
                } else {
                    fromReturned.add(dp);
                }
            }
            catch (NullPointerException localNullPointerException1) {}
        }
        HashSet<DrugProduct> dps = new HashSet(dpList1);
        HashSet<DrugProduct> dps1 = new HashSet(fromReturned);

        List<DrugProduct> dpSet = new ArrayList();
        List<DrugProduct> consumableSet = new ArrayList();

        Location dftLoc = null;
        List<Pharmacy> pharmacyList = null;

        String locationStr =
                (String)Context.getAuthenticatedUser().getUserProperties().get("defaultLocation");
        try
        {
            dftLoc = locationService.getLocation(Integer.valueOf(locationStr));
            pharmacyList = serviceDrug.getPharmacyByLocation(dftLoc);
        } catch (Exception e) {
            mav.addObject("msg", "pharmacymanagement.missingDftLoc");
        }
        try
        {
            if (pharmacyList.size() == 0) {
                mav.addObject("msg", "You need to create Pharmacy first");
            }
        } catch (NullPointerException npe) {
            mav.addObject("msg", "You need to create Pharmacy first");
        }

        CmdDrug cmdDrug = new CmdDrug();

        Map requestMap = request.getParameterMap();

        ArrayList<String> fieldNames = new ArrayList();
        for (Object key : requestMap.keySet()) {
            String keyString = (String)key;
            fieldNames.add(keyString); }
        Date date;
        int currConsumableSolde;
        if ((request.getParameter("on") != null) &&
                (request.getParameter("on").equals("true")))
        {

            if (((request.getParameterMap().containsKey("destination")) ||
                    (request.getParameterMap().containsKey("supporter")) ||
                    (request.getParameterMap().containsKey("month"))) && (
                    (!request.getParameter("destination").equals("")) ||
                            (!request.getParameter("supporter").equals("")) ||
                            (!request.getParameter("month").equals(""))))
            {
                Pharmacy from = serviceDrug.getPharmacyById(
                        Integer.valueOf(request.getParameter("pharmacy")).intValue());
                Location to = Context.getLocationService().getLocation(
                        from.getLocationId().getLocationId());

                cmdDrug.setPharmacy(from);
                cmdDrug.setDestination(to);
                cmdDrug
                        .setSupportingProg(request
                                .getParameter("supporter"));

                SimpleDateFormat sdf;

                if(Context.getLocale().toString().equals("en_US") || Context.getLocale().toString().equals("en")) {
                    sdf = new SimpleDateFormat("MM/dd/yyyy");
                }
                else {
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                }
                date = null;
                try {
                    date = sdf.parse(request.getParameter("month"));
                    cmdDrug.setMonthPeriod(date);
                } catch (ParseException e) {
                    mav.addObject("msg", "pharmacymanagement.date.missing");
                }
            }




            if (fieldNames.size() != 0) {
                boolean hasSaved = false;
                int count = 1;

                for (String str : fieldNames)
                {
                    String suffixId = str.substring(str.indexOf("_") + 1);
                    String drugSuffix = "drugs_" + suffixId;
                    String consSuffix = "consumable_" + suffixId;

                    if (drugSuffix.equals(str))
                    {
                        String id = (request.getParameter("drugs_" + suffixId)).split("@")[0];
                        String lotNo = (request.getParameter("drugs_" + suffixId)).split("@")[1];
                        String expiryDateStr = (request.getParameter("drugs_" + suffixId)).split("@")[2];
                        SimpleDateFormat sdfexp = new SimpleDateFormat("yyyy-MM-dd");

                        Date expiryDate= sdfexp.parse(expiryDateStr);


                        String drugneeded = request.getParameter("drugneeded_" +
                                suffixId);
                        String reqReasonOfDrug=request.getParameter("reqReson_" +suffixId);

                        User requestedBy=Context.getAuthenticatedUser();
                        Date dateRequested=new Date();
                        String transferType=request.getParameter("transferType_"+suffixId);

                        if ((count == 1) && (!id.equals("")) &&
                                (!drugneeded.equals(""))) {
                            serviceDrug.saveCmdDrug(cmdDrug);
                        }
                        drugProduct = new DrugProduct();
                        currDP = serviceDrug.getDrugProductById(
                                Integer.valueOf(id).intValue());
                        drug = cs.getDrug(currDP.getDrugId().getDrugId());

                        drugProduct.setDrugId(drug);

                        drugProduct.setExpiryDate(expiryDate);

                        amountreq = Integer.parseInt(drugneeded);

                        int storeqnty = serviceDrug.getCurrSolde(id, null,cmdDrug.getPharmacy().getLocationId()+"",currDP.getExpiryDate()+"", currDP.getLotNo(), cmdDrug.getCmddrugId()+"");

                        drugProduct.setStoreQnty(storeqnty);
                        drugProduct.setQntyReq(amountreq);
                        drugProduct.setComments(reqReasonOfDrug);
                        drugProduct.setCmddrugId(cmdDrug);


                       /* User requestedBy=Context.getAuthenticatedUser();
                        Date dateRequested=new Date();
                        String transferType=request.getParameter("transferType_"+suffixId);*/
                        drugProduct.setRequestedBy(requestedBy);
                        drugProduct.setReqDate(dateRequested);
                        drugProduct.setTransferType(transferType);

                        if(lotNo!=null && !lotNo.equals("")){
                            drugProduct.setLotNo(lotNo);
                        }

                        serviceDrug.saveDrugProduct(drugProduct);
                        hasSaved = true;
                        count++;
                    } else if (consSuffix.equals(str))
                    {
                        String id = (request.getParameter("consumable_" +suffixId)).split("@")[0];
                        String lotNo = (request.getParameter("consumable_" + suffixId)).split("@")[1];
                        String expiryDateStr = (request.getParameter("consumable_" + suffixId)).split("@")[2];

                        SimpleDateFormat sdfexp = new SimpleDateFormat("yyyy-MM-dd");
                        Date expiryDate= sdfexp.parse(expiryDateStr);


                        String consneeded = request.getParameter("consneeded_" +suffixId);
                        String reqReasonOfConsum=request.getParameter("ConsreqReson_" +suffixId);
                        User requestedBy=Context.getAuthenticatedUser();
                        Date dateRequested=new Date();
                        String transferType=request.getParameter("ConstransferType_"+suffixId);

                        if ((count == 1) && (!id.equals("")) &&
                                (!consneeded.equals(""))) {
                            serviceDrug.saveCmdDrug(cmdDrug);
                        }
                        drugProduct = new DrugProduct();
                        currDP = serviceDrug.getDrugProductById(
                                Integer.valueOf(id).intValue());


                        drugProduct.setExpiryDate(expiryDate);

                        drugProduct.setConceptId(currDP.getConceptId());
                        int storeqnty = serviceDrug.getCurrSolde(null,
                                consneeded, cmdDrug.getPharmacy()
                                        .getLocationId()+"",
                                null, null, cmdDrug.getCmddrugId()+"").intValue();

                        drugProduct.setStoreQnty(storeqnty);
                        if ((consneeded != null) && (!consneeded.equals(""))) {
                            amountreq = Integer.parseInt(consneeded);
                        }
                        drugProduct.setQntyReq(amountreq);
                        drugProduct.setComments(reqReasonOfConsum);
                        drugProduct.setCmddrugId(cmdDrug);


                        drugProduct.setRequestedBy(requestedBy);
                        drugProduct.setReqDate(dateRequested);
                        drugProduct.setTransferType(transferType);

                        if(lotNo!=null && !lotNo.equals("")){
                            drugProduct.setLotNo(lotNo);
                        }
                        serviceDrug.saveDrugProduct(drugProduct);
                        hasSaved = true;
                        count++;
                    }
                }
                if (hasSaved) {
                    serviceDrug.saveCmdDrug(cmdDrug);
                    mav.addObject("msg", "pharmacymanagement.drugorder.save");
                }
                else {
                    mav.addObject("msg",
                            "pharmacymanagement.drugorder.missingdrug");
                }
            }
        }
        else {
            int currSolde = 0;
            currConsumableSolde = 0;
            for (DrugProduct dp : dps) {
                if ((dp.getDrugId() != null) &&
                        (dp.getCmddrugId().getLocationId() != null)) {
                    currSolde = serviceDrug.getCurrSolde(dp.getDrugId()
                                    .getDrugId()+"",
                            null, dftLoc.getLocationId()+"",
                            dp.getExpiryDate()+"", dp.getLotNo(), null);



                    if (currSolde > 0) {
                        dpSet.add(dp);
                    }
                }

                if ((dp.getConceptId() != null) &&
                        (dp.getCmddrugId().getLocationId() != null))
                {
                    currConsumableSolde = serviceDrug.getCurrSolde(null, dp.getConceptId().getConceptId()+"", dftLoc.getLocationId()+"", null, null, null);




                    if (currConsumableSolde > 0) {
                        consumableSet.add(dp);
                    }
                }
            }

            for (DrugProduct dp : dps1) {
                if ((dp.getDrugId() != null) && (serviceDrug.getReturnStockByDP(dp).size() > 0) &&
                        (((ProductReturnStore)serviceDrug.getReturnStockByDP(dp).get(0))
                                .getDestination().getLocationId() == dftLoc
                                .getLocationId())) {
                    currSolde = serviceDrug.getCurrSolde(dp.getDrugId().getDrugId()+"",null, ((ProductReturnStore)serviceDrug.getReturnStockByDP(dp).get(0)).getDestination().getLocationId()+"", dp.getExpiryDate()+"", dp.getLotNo(), null);

                    if (currSolde > 0) {
                        dpSet.add(dp);
                    }
                }
            }
        }




        for (DrugProduct drugproduct : dpSet) {
            drugMap.put(drugproduct.getLotNo(), drugproduct);

        }



        for (DrugProduct drugproduct : consumableSet) {
            consumableMap.put(drugproduct.getConceptId().getConceptId(), drugproduct);
        }



        Collection<DrugProduct> sortedDrug = Utils.sortDrugProducts(drugMap.values());
        Object sortedConsumable = Utils.sortDrugProducts(consumableMap.values());

        mav.addObject("pharmacyList", pharmacyList);
        mav.addObject("pharmacies", pharmacies);
        mav.addObject("locations", locations);
        mav.addObject("dftLoc", dftLoc);
        mav.addObject("dpSet", sortedDrug);
        mav.addObject("consumableSet", sortedConsumable);
        mav.setViewName(getViewName());
        return mav;
    }
}