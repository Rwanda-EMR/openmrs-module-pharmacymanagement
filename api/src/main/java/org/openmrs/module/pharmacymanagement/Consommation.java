/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement;

import java.util.Date;


/**
 *@author Dusabe Eric
 *to help in the display of the ARV Monthly Consumption
 */
public class Consommation {
	
	private String drugName;
	private String drugId;
	private String conceptId;
	private String forme;
	private String conditUnitaire;
	private int qntPremJour;
	private Object qntRecuMens;
	private Object qntConsomMens;
	private int qntRestMens;
	private int locationId;
	private Date expirationDate;
	private DrugProduct drugProduct;
	private int stockOut;
	private int adjustMonthlyConsumption;
	private int maxQnty;
	private int qntyToOrder;
	private int returnedProduct;
	private int adjustedProduct;
	
	public DrugProduct getDrugProduct() {
		return drugProduct;
	}
	public void setDrugProduct(DrugProduct drugProduct) {
		this.drugProduct = drugProduct;
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
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}	
	/**
	 * @return the drugId
	 */
	public String getDrugId() {
		return drugId;
	}
	/**
	 * @param drugId the drugId to set
	 */
	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}	
	/**
	 * @return the conceptId
	 */
	public String getConceptId() {
		return conceptId;
	}
	/**
	 * @param conceptId the conceptId to set
	 */
	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}
	/**
	 * @return the forme
	 */
	public String getForme() {
		return forme;
	}
	/**
	 * @param forme the forme to set
	 */
	public void setForme(String forme) {
		this.forme = forme;
	}
	/**
	 * @return the conditUnitaire
	 */
	public String getConditUnitaire() {
		return conditUnitaire;
	}
	/**
	 * @param conditUnitaire the conditUnitaire to set
	 */
	public void setConditUnitaire(String conditUnitaire) {
		this.conditUnitaire = conditUnitaire;
	}
	/**
	 * @return the qntPremJour
	 */
	public int getQntPremJour() {
		return qntPremJour;
	}
	/**
	 * @param qntPremJour the qntPremJour to set
	 */
	public void setQntPremJour(int qntPremJour) {
		this.qntPremJour = qntPremJour;
	}
	/**
	 * @return the qntRecuMens
	 */
	public Object getQntRecuMens() {
		return qntRecuMens;
	}
	/**
	 * @param obQntyRestMens the qntRecuMens to set
	 */
	public void setQntRecuMens(Object obQntyRestMens) {
		this.qntRecuMens = obQntyRestMens;
	}
	/**
	 * @return the qntConsomMens
	 */
	public Object getQntConsomMens() {
		return qntConsomMens;
	}
	/**
	 * @param obQntyConsomMens the qntConsomMens to set
	 */
	public void setQntConsomMens(Object obQntyConsomMens) {
		this.qntConsomMens = obQntyConsomMens;
	}
	/**
	 * @return the qntRestMens
	 */
	public int getQntRestMens() {
		return qntRestMens;
	}
	/**
	 * @param qntRestMens the qntRestMens to set
	 */
	public void setQntRestMens(int qntRestMens) {
		this.qntRestMens = qntRestMens;
	}
	/**
	 * @return the locationId
	 */
	public int getLocationId() {
		return locationId;
	}
	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}
	/**
	 * @return the stockOut
	 */
	public int getStockOut() {
		return stockOut;
	}
	/**
	 * @param stockOut the stockOut to set
	 */
	public void setStockOut(int stockOut) {
		this.stockOut = stockOut;
	}
	/**
	 * @return the adjustMonthlyConsumption
	 */
	public int getAdjustMonthlyConsumption() {
		return adjustMonthlyConsumption;
	}
	/**
	 * @param adjustMonthlyConsumption the adjustMonthlyConsumption to set
	 */
	public void setAdjustMonthlyConsumption(int adjustMonthlyConsumption) {
		this.adjustMonthlyConsumption = adjustMonthlyConsumption;
	}
	/**
	 * @return the maxQnty
	 */
	public int getMaxQnty() {
		return maxQnty;
	}
	/**
	 * @param maxQnty the maxQnty to set
	 */
	public void setMaxQnty(int maxQnty) {
		this.maxQnty = maxQnty;
	}
	/**
	 * @return the qntyToOrder
	 */
	public int getQntyToOrder() {
		return qntyToOrder;
	}
	/**
	 * @param qntyToOrder the qntyToOrder to set
	 */
	public void setQntyToOrder(int qntyToOrder) {
		this.qntyToOrder = qntyToOrder;
	}
	/**
	 * @return the returnedProduct
	 */
	public int getReturnedProduct() {
		return returnedProduct;
	}
	/**
	 * @param returnedProduct the returnedProduct to set
	 */
	public void setReturnedProduct(int returnedProduct) {
		this.returnedProduct = returnedProduct;
	}
	/**
	 * @return the adjustedProduct
	 */
	public int getAdjustedProduct() {
		return adjustedProduct;
	}
	/**
	 * @param adjustedProduct the adjustedProduct to set
	 */
	public void setAdjustedProduct(int adjustedProduct) {
		this.adjustedProduct = adjustedProduct;
	}
}
