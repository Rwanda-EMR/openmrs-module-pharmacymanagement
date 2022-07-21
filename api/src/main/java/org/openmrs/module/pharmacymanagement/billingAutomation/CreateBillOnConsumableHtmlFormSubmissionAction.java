package org.openmrs.module.pharmacymanagement.billingAutomation;

import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.CustomFormSubmissionAction;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.mohbilling.businesslogic.ConsommationUtil;
import org.openmrs.module.mohbilling.businesslogic.GlobalBillUtil;
import org.openmrs.module.mohbilling.businesslogic.InsuranceBillUtil;
import org.openmrs.module.mohbilling.businesslogic.PatientBillUtil;
import org.openmrs.module.mohbilling.model.*;
import org.openmrs.module.mohbilling.service.BillingService;
import org.openmrs.module.mohbilling.utils.Utils;
import org.openmrs.module.pharmacymanagement.ConsumableOrder;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateBillOnConsumableHtmlFormSubmissionAction implements CustomFormSubmissionAction {


    @Override
    public void applyAction(FormEntrySession session) {
Integer insuranceNumberConceptID=Integer.parseInt(Context.getAdministrationService().getGlobalProperty("registration.insuranceNumberConcept"));
        System.out.println("Bill startttttttttttttttttttttttttttttttttttttttt ");

String insuranceCardNumber=null;
//GlobalBill gb =null;



List<Obs> currentInsuranceId=Utils.getLastNObservations(1,session.getPatient(),Context.getConceptService().getConcept(insuranceNumberConceptID),false);

if(currentInsuranceId.size()>=1)
    insuranceCardNumber=currentInsuranceId.get(0).getValueText();


        InsurancePolicy ip =Context.getService(BillingService.class).getInsurancePolicyByCardNo(insuranceCardNumber);


        List<PatientServiceBill> psbList=new ArrayList<PatientServiceBill>();
        Department department=null;
        List<Obs> obs=session.getSubmissionActions().getObsToCreate();
        System.out.println("Number of Obssssssssss: "+obs.size());
        BigDecimal totalMaximaTopay=new BigDecimal(0);
        for (Obs o:obs) {
            PatientServiceBill psb=new PatientServiceBill();

          if (department==null && o.getValueCoded()!=null ) {
              for (Department dept : Context.getService(BillingService.class).getAllDepartements()) {
                  if (o.getValueCoded().getName().getName().toString().trim().equals(dept.getName().toString().trim())) {
                      department = dept;
                      System.out.println("Department Foundddddddddd: "+department.getName());
                      break;
                  }
              }
          }

            //value coded
            if (o.getValueCoded()!=null) {
                FacilityServicePrice fsp = Context.getService(BillingService.class).getFacilityServiceByConcept(o.getValueCoded());
                if (fsp!=null) {
                    System.out.println("FacilityServicePrice Found: " + fsp.getName());
                    BillableService bs = Context.getService(BillingService.class).getBillableServiceByConcept(fsp,ip.getInsurance());
                    totalMaximaTopay=totalMaximaTopay.add(bs.getMaximaToPay());
                    System.out.println("BillableService maxima_to_pay: " + bs.getMaximaToPay());
                    psb.setService(bs);
                    psb.setServiceDate(new Date());
                    psb.setUnitPrice(bs.getMaximaToPay());
                    psb.setQuantity(new BigDecimal(1));
                    //psb.setHopService(Context.getService(BillingService.class).getHopService(2));
                    psb.setHopService(Context.getService(BillingService.class).getHopService(fsp.getCategory()));
                    psb.setCreator(Context.getAuthenticatedUser());
                    psb.setCreatedDate(new Date());

                    psbList.add(psb);
                }

            }
            //value Numeric
            System.out.println("Obsssssss: "+o.getConcept().getName().getName()+ " With Qnty: "+o.getValueNumeric());
            if (o.getValueNumeric()!=null) {
                FacilityServicePrice fsp = Context.getService(BillingService.class).getFacilityServiceByConcept(o.getConcept());
                if (fsp!=null) {
                    System.out.println("FSP Foundddddddddd: "+fsp.getName()+ "With Qnty: "+o.getValueNumeric());
                    System.out.println("FacilityServicePrice Found: " + fsp.getName());
                    BillableService bs = Context.getService(BillingService.class).getBillableServiceByConcept(fsp,ip.getInsurance());
                    totalMaximaTopay=totalMaximaTopay.add(bs.getMaximaToPay().multiply(new BigDecimal(o.getValueNumeric())));
                    System.out.println("BillableService maxima_to_pay: " + bs.getMaximaToPay());
                    psb.setService(bs);
                    psb.setServiceDate(new Date());
                    psb.setUnitPrice(bs.getMaximaToPay());
                    psb.setQuantity(new BigDecimal(o.getValueNumeric()));
                    //psb.setHopService(Context.getService(BillingService.class).getHopService(2));
                    psb.setHopService(Context.getService(BillingService.class).getHopService(fsp.getCategory()));
                    psb.setCreator(Context.getAuthenticatedUser());
                    psb.setCreatedDate(new Date());
                    psbList.add(psb);

                    if (bs!=null){
                        ConsumableOrder consumableOrder=new ConsumableOrder();
                        consumableOrder.setDate(new Date());
                        consumableOrder.setQnty(o.getValueNumeric().intValue());
                        consumableOrder.setConsumable(o.getConcept());
                        consumableOrder.setProvider(Context.getAuthenticatedUser());
                        consumableOrder.setPatientId((Patient) o.getPerson());
                        DrugOrderService pharmacyService=Context.getService(DrugOrderService.class);
                        pharmacyService.saveOrUpdateConsumableOrder(consumableOrder);
                    }

                }


            }
            //value Numeric
            if (o.getValueBoolean()!=null) {
                FacilityServicePrice fsp = Context.getService(BillingService.class).getFacilityServiceByConcept(o.getConcept());
                if (fsp!=null) {
                    System.out.println("FacilityServicePrice Found: " + fsp.getName());
                    BillableService bs = Context.getService(BillingService.class).getBillableServiceByConcept(fsp,ip.getInsurance());
                    totalMaximaTopay=totalMaximaTopay.add(bs.getMaximaToPay());
                    System.out.println("BillableService maxima_to_pay: " + bs.getMaximaToPay());
                    psb.setService(bs);
                    psb.setServiceDate(new Date());
                    psb.setUnitPrice(bs.getMaximaToPay());
                    psb.setQuantity(new BigDecimal(1));
                    //psb.setHopService(Context.getService(BillingService.class).getHopService(2));
                    psb.setHopService(Context.getService(BillingService.class).getHopService(fsp.getCategory()));
                    psb.setCreator(Context.getAuthenticatedUser());
                    psb.setCreatedDate(new Date());

                    psbList.add(psb);
                }

            }

        }

        //drugs start

        List<Order> orders=session.getSubmissionActions().getCurrentEncounter().getOrdersWithoutOrderGroups();
        for (Order order:orders) {
            DrugOrder drugOrder=(DrugOrder)order;
           //Billng code here
        }

        //Drugs end





        if(psbList.size()>0) {

            GlobalBill gb = Context.getService(BillingService.class).getOpenGlobalBillByInsuranceCardNo(ip.getInsuranceCardNo());
            BigDecimal globalAmount = gb.getGlobalAmount().add(totalMaximaTopay);
            gb.setGlobalAmount(globalAmount);
            gb = GlobalBillUtil.saveGlobalBill(gb);


            System.out.println("Totalllllllllllllllllllllllllll: " + totalMaximaTopay);

            PatientBill pb = PatientBillUtil.createPatientBill(totalMaximaTopay, ip);
            InsuranceBill ib = InsuranceBillUtil.createInsuranceBill(ip.getInsurance(), totalMaximaTopay);

            Consommation cons = new Consommation();
            cons.setBeneficiary(Context.getService(BillingService.class).getBeneficiaryByPolicyNumber(insuranceCardNumber));
            cons.setPatientBill(pb);
            cons.setInsuranceBill(ib);
            cons.setGlobalBill(gb);
            cons.setCreatedDate(new Date());
            cons.setCreator(Context.getAuthenticatedUser());
            //cons.setDepartment(Context.getService(BillingService.class).getDepartement(2));
            cons.setDepartment(department);
            ConsommationUtil.saveConsommation(cons);
            //Context.getService(BillingService.class).saveConsommation(cons);

            for (PatientServiceBill psb : psbList) {
                psb.setConsommation(cons);
                ConsommationUtil.createPatientServiceBill(psb);
                //cons.addBillItem(psb);
            }
        }


        System.out.println("Bill Enddddddddddddddddddddddddddddddddddddddd ");

    }


}