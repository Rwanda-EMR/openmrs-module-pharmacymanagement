package org.openmrs.module.pharmacymanagement;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openmrs.Location;
import org.openmrs.User;

/**
 *@author Eric D.
 */
public class CmdDrug {
	
	private int cmddrugId;
	private Location locationId;
	private Location destination;
	private String supportingProg;
	private Date monthPeriod;
	private Set<DrugProduct> drugProducts = new HashSet<DrugProduct>();	
	private boolean isAchieved;
	private Pharmacy pharmacy;
	private String transferType;
	private User creator;

	public boolean isAchieved() {
		return this.isAchieved;
	}

	public void setAchieved(final boolean achieved) {
		this.isAchieved = achieved;
	}

	public String getTransferType() {
		return this.transferType;
	}

	public void setTransferType(final String transferType) {
		this.transferType = transferType;
	}

	public User getCreator() {
		return this.creator;
	}

	public void setCreator(final User creator) {
		this.creator = creator;
	}

	/**
	 * @return the isAchieved
	 */
	public boolean getIsAchieved() {
		return isAchieved;
	}
	/**
	 * @param isAchieved the isAchieved to set
	 */
	public void setIsAchieved(boolean isAchieved) {
		this.isAchieved = isAchieved;
	}
	/**
	 * @return the drugProducts
	 */
	public Set<DrugProduct> getDrugProducts() {
		return drugProducts;
	}
	/**
	 * @param drugProducts the drugProducts to set
	 */
	public void setDrugProducts(Set<DrugProduct> drugProducts) {
		this.drugProducts = drugProducts;
	}
	/**
	 * @return the cmddrugId
	 */
	public int getCmddrugId() {
		return cmddrugId;
	}
	/**
	 * @param cmddrugId the cmddrugId to set
	 */
	public void setCmddrugId(int cmddrugId) {
		this.cmddrugId = cmddrugId;
	}
	/**
	 * @return the locationId
	 */
	public Location getLocationId() {
		return locationId;
	}
	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Location locationId) {
		this.locationId = locationId;
	}
	
	
	/**
	 * @return the destination
	 */
	public Location getDestination() {
		return destination;
	}
	/**
	 * @param destination the destination to set
	 */
	public void setDestination(Location destination) {
		this.destination = destination;
	}
	/**
	 * @return the supportingProg
	 */
	public String getSupportingProg() {
		return supportingProg;
	}
	/**
	 * @param supportingProg the supportingProg to set
	 */
	public void setSupportingProg(String supportingProg) {
		this.supportingProg = supportingProg;
	}
	/**
	 * @return the monthPeriod
	 */
	public Date getMonthPeriod() {
		return monthPeriod;
	}
	/**
	 * @param monthPeriod the monthPeriod to set
	 */
	public void setMonthPeriod(Date monthPeriod) {
		this.monthPeriod = monthPeriod;
	}
	/**
	 * @return the pharmacy
	 */
	public Pharmacy getPharmacy() {
		return pharmacy;
	}
	/**
	 * @param pharmacy the pharmacy to set
	 */
	public void setPharmacy(Pharmacy pharmacy) {
		this.pharmacy = pharmacy;
	}
		
}
