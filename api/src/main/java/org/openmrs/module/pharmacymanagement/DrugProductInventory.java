package org.openmrs.module.pharmacymanagement;

import java.util.Date;

public class DrugProductInventory {
	
	private int inventoryId;
	private Date inventoryDate;
	private DrugProduct drugproductId;
	private int entree;
	private int sortie;
	private int solde;
	private boolean isStore;
	private int adjustedOldSolde;

	public int getAdjustedOldSolde() {
		return this.adjustedOldSolde;
	}

	public void setAdjustedOldSolde(final int adjustedOldSolde) {
		this.adjustedOldSolde = adjustedOldSolde;
	}

	/**
	 * @return the inventoryId
	 */
	public int getInventoryId() {
		return inventoryId;
	}
	/**
	 * @param inventoryId the inventoryId to set
	 */
	public void setInventoryId(int inventoryId) {
		this.inventoryId = inventoryId;
	}
	/**
	 * @return the inventoryDate
	 */
	public Date getInventoryDate() {
		return inventoryDate;
	}
	/**
	 * @param inventoryDate the inventoryDate to set
	 */
	public void setInventoryDate(Date inventoryDate) {
		this.inventoryDate = inventoryDate;
	}
	/**
	 * @return the drugproductId
	 */
	public DrugProduct getDrugproductId() {
		return drugproductId;
	}
	/**
	 * @param drugproductId the drugproductId to set
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
	 * @return the isStore
	 */
	public boolean getIsStore() {
		return isStore;
	}
	/**
	 * @param isStore the isStore to set
	 */
	public void setIsStore(boolean isStore) {
		this.isStore = isStore;
	}
	

}
