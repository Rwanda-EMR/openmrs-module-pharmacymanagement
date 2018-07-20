package org.openmrs.module.pharmacymanagement;

import java.util.Date;

public class PharmacyInventory {
	
	private int pharmacyInventoryId;
	private Date date;
	private DrugProduct drugproductId;
	private int entree;
	private int sortie;
	private int solde;
	private DrugOrderPrescription dopId;
	private ConsumableDispense cpId;
	
	/**
	 * @return the dopId
	 */
	public DrugOrderPrescription getDopId() {
		return dopId;
	}
	/**
	 * @param dopId the dopId to set
	 */
	public void setDopId(DrugOrderPrescription dopId) {
		this.dopId = dopId;
	}
	
	/**
	 * @return the pharmacyInventoryId
	 */
	public int getPharmacyInventoryId() {
		return pharmacyInventoryId;
	}
	/**
	 * @param pharmacyInventoryId the pharmacyInventoryId to set
	 */
	public void setPharmacyInventoryId(int pharmacyInventoryId) {
		this.pharmacyInventoryId = pharmacyInventoryId;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the drug
	 */
	public DrugProduct getDrugproductId() {
		return drugproductId;
	}
	/**
	 * @param drug the drug to set
	 */
	public void setDrugproductId(DrugProduct drugproductId) {
		this.drugproductId = drugproductId;
	}
	/**
	 * @return the entree
	 */
	public int getEntree() {
		return entree;
	}
	/**
	 * @param entree the entree to set
	 */
	public void setEntree(int entree) {
		this.entree = entree;
	}
	/**
	 * @return the sortie
	 */
	public int getSortie() {
		return sortie;
	}
	/**
	 * @param sortie the sortie to set
	 */
	public void setSortie(int sortie) {
		this.sortie = sortie;
	}
	/**
	 * @return the solde
	 */
	public int getSolde() {
		return solde;
	}
	/**
	 * @param solde the solde to set
	 */
	public void setSolde(int solde) {
		this.solde = solde;
	}
	/**
	 * @return the cpId
	 */
	public ConsumableDispense getCpId() {
		return cpId;
	}
	/**
	 * @param cpId the cpId to set
	 */
	public void setCpId(ConsumableDispense cpId) {
		this.cpId = cpId;
	}
}
