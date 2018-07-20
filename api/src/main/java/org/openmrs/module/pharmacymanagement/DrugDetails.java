/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement;

import org.openmrs.Drug;

/**
 *
 */
public class DrugDetails {
	
	private int drugDetailsId;
	private Drug drug;
	private String forme;
	private int units;
	private String measurementUnit;
	
	/**
	 * @return the drugDetailsId
	 */
	public int getDrugDetailsId() {
		return drugDetailsId;
	}
	/**
	 * @param drugDetailsId the drugDetailsId to set
	 */
	public void setDrugDetailsId(int drugDetailsId) {
		this.drugDetailsId = drugDetailsId;
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
	 * @return the units
	 */
	public int getUnits() {
		return units;
	}
	/**
	 * @param units the units to set
	 */
	public void setUnits(int units) {
		this.units = units;
	}
	/**
	 * @return the measurementUnit
	 */
	public String getMeasurementUnit() {
		return measurementUnit;
	}
	/**
	 * @param measurementUnit the measurementUnit to set
	 */
	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}
}
