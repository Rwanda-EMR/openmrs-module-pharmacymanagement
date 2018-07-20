/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement;

import java.util.Map;

import org.openmrs.Drug;
import org.openmrs.DrugOrder;

/**
 *
 */
public class DrugLotDate {
	private Drug drug;
	private Map<String, String> dpMap;
	private DrugOrder drugOrder;
	
	/**
	 * @return the dpMap
	 */
	public Map<String, String> getDpMap() {
		return dpMap;
	}
	/**
	 * @param dpMap the dpMap to set
	 */
	public void setDpMap(Map<String, String> dpMap) {
		this.dpMap = dpMap;
	}
	/**
	 * @return the drug
	 */
	public Drug getDrug() {
		return drug;
	}
	/**
	 * @param drug the drug to set
	 */
	public void setDrug(Drug drug) {
		this.drug = drug;
	}
	/**
	 * @return the drugOrder
	 */
	public DrugOrder getDrugOrder() {
		return drugOrder;
	}
	/**
	 * @param drugOrder the drugOrder to set
	 */
	public void setDrugOrder(DrugOrder drugOrder) {
		this.drugOrder = drugOrder;
	}	
}
