package org.openmrs.module.pharmacymanagement;

import org.openmrs.api.context.Context;

public class HMISStockOfTracerDrugs {
	private int quantityDispensed;
	private int edl;
	private int solde;
	private int stockout;
	private String drugName;
	
	public HMISStockOfTracerDrugs(int quantityDispensed, int edl, int solde, int stockout, String drugName) {
		this.quantityDispensed = quantityDispensed;
		this.edl = edl;
		this.solde = solde;
		this.stockout = stockout;
		this.drugName = drugName;
	}
	/**
	 * @return the quantityDispensed
	 */
	public int getQuantityDispensed() {
		return quantityDispensed;
	}
	/**
	 * @param quantityDispensed the quantityDispensed to set
	 */
	public void setQuantityDispensed(int quantityDispensed) {
		this.quantityDispensed = quantityDispensed;
	}
	/**
	 * edl >> Expired/Damaged/Lost
	 * @return the edl
	 */
	public int getEdl() {
		return edl;
	}
	/**
	 * edl >> Expired/Damaged/Lost
	 * @param edl the edl to set
	 */
	public void setEdl(int edl) {
		this.edl = edl;
	}
	/**
	 * @return the endMonthStock
	 */
	public int getSolde() {
		return solde;
	}
	/**
	 * @param endMonthStock the endMonthStock to set
	 */
	public void setSolde(int endMonthStock) {
		this.solde = endMonthStock;
	}
	/**
	 * @return the stockoutDays
	 */
	public int getStockout() {
		return stockout;
	}
	/**
	 * @param stockoutDays the stockoutDays to set
	 */
	public void setStockout(int stockout) {
		this.stockout = stockout;
	}
	/**
	 * @return the drugName
	 */
	public String getDrugName() {
		return drugName;
	}
	/**
	 * @param drugName the drugName to set
	 */	
}
