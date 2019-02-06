package org.openmrs.module.pharmacymanagement;

import org.openmrs.api.context.Context;

public class PharmacyConstants {
	public static final int ADVERSE_ACTIONS = 1645;
	public static final int NEW_PROPHYLAXIS = 3117;
	public static final int ADVERSE_EFFECTS = 1297;
	public static final int WEIGHT = 5089;
	public static final int NEXT_VISIT_DATE = 5096;
	public static final int REASON_ORDER_STOPPED = 1812;
	public static final int DRUG_ORDER_TYPE = 1;
	public static final String PROVIDER_ROLE = "Provider";
	public static final int CONSUMABLE = Integer.parseInt(Context.getAdministrationService().getGlobalProperty("pharmacymanagement.CONSUMABLE"));
	public static final String CMD_DRUG = "pharmacymanagement_cmd_drug";
	public static final String ARV_RETURN_STORE = "pharmacymanagement_arv_return_store";
	public static final String DRUGPRODUCT_INVENTORY = "pharmacymanagement_drugproduct_inventory";
	public static final String PHARMACY_INVENTORY = "pharmacymanagement_pharmacy_inventory";
	public static final String CONSUMABLE_DISPENSE = "pharmacymanagement_consumable_dispense";
	public static final String DRUG_ORDER_PRESCRIPTION = "pharmacymanagement_drug_order_prescription";
	public static final String DRUG_PRODUCT = "pharmacymanagement_drug_product";
	public static final String PHARMACY = "pharmacymanagement_pharmacy";
	public static final int RETURN_VISIT_DATE = 5096;
	public static final int RWANDA_INSURANCE_TYPE = 6740;
	public static final int RWANDA_INSURANCE_NUMBER = 6741;
	
}
