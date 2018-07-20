package org.openmrs.module.pharmacymanagement;

import java.util.List;


public class DrugStore {
	
	private CmdDrug cmdDrug;
	private List<DrugProduct> drugProducts;
	
	/**
	 * @return the cmdDrug
	 */
	public CmdDrug getCmdDrug() {
		return cmdDrug;
	}
	/**
	 * @param cmdDrug the cmdDrug to set
	 */
	public void setCmdDrug(CmdDrug cmdDrug) {
		this.cmdDrug = cmdDrug;
	}
	/**
	 * @return the drugProducts
	 */
	public List<DrugProduct> getDrugProducts() {
		return drugProducts;
	}
	/**
	 * @param drugProducts the drugProducts to set
	 */
	public void setDrugProducts(List<DrugProduct> drugProducts) {
		this.drugProducts = drugProducts;
	}	
	
}
