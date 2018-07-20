/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement;

import org.openmrs.Drug;

/**
 *
 */
public class DrugAndSolde {
	private Drug drug;
	private int solde;
	
	public DrugAndSolde(Drug drug, int solde) {
		this.drug = drug;
		this.solde = solde;
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
}
