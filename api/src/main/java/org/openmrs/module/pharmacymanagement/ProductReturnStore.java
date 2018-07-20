package org.openmrs.module.pharmacymanagement;

import java.util.Date;

import org.openmrs.User;
import org.openmrs.Location;

public class ProductReturnStore {
	private int returnStoreId;
	private DrugProduct drugproductId;
	private int retQnty;
	private String observation;
	private Date retDate;
	private boolean isReturned;
	private User returnedBy;
	private DrugProductInventory dpInventory;
	private Location originLocation;
	private Location destination;
	private PharmacyInventory phInventory;
	private Pharmacy originPharmacy;
	
	/**
	 * @return the returnStoreId
	 */
	public int getReturnStoreId() {
		return returnStoreId;
	}
	/**
	 * @param returnStoreId the returnStoreId to set
	 */
	public void setReturnStoreId(int returnStoreId) {
		this.returnStoreId = returnStoreId;
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
	 * @return the retQnty
	 */
	public int getRetQnty() {
		return retQnty;
	}
	/**
	 * @param retQnty the retQnty to set
	 */
	public void setRetQnty(int retQnty) {
		this.retQnty = retQnty;
	}
	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}
	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}
	/**
	 * @return the retDate
	 */
	public Date getRetDate() {
		return retDate;
	}
	/**
	 * @param retDate the retDate to set
	 */
	public void setRetDate(Date retDate) {
		this.retDate = retDate;
	}
	/**
	 * @return the isReturned
	 */
	public boolean getIsReturned() {
		return isReturned;
	}
	/**
	 * @param isReturned the isReturned to set
	 */
	public void setIsReturned(boolean isReturned) {
		this.isReturned = isReturned;
	}
	/**
	 * @return the returnedBy
	 */
	public User getReturnedBy() {
		return returnedBy;
	}
	/**
	 * @param returnedBy the returnedBy to set
	 */
	public void setReturnedBy(User returnedBy) {
		this.returnedBy = returnedBy;
	}
	/**
	 * @return the dpInventory
	 */
	public DrugProductInventory getDpInventory() {
		return dpInventory;
	}
	/**
	 * @param dpInventory the dpInventory to set
	 */
	public void setDpInventory(DrugProductInventory dpInventory) {
		this.dpInventory = dpInventory;
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
	 * @return the phInventory
	 */
	public PharmacyInventory getPhInventory() {
		return phInventory;
	}
	/**
	 * @param phInventory the phInventory to set
	 */
	public void setPhInventory(PharmacyInventory phInventory) {
		this.phInventory = phInventory;
	}
	/**
	 * @return the originLocation
	 */
	public Location getOriginLocation() {
		return originLocation;
	}
	/**
	 * @param originLocation the originLocation to set
	 */
	public void setOriginLocation(Location originLocation) {
		this.originLocation = originLocation;
	}
	/**
	 * @return the originPharmacy
	 */
	public Pharmacy getOriginPharmacy() {
		return originPharmacy;
	}
	/**
	 * @param originPharmacy the originPharmacy to set
	 */
	public void setOriginPharmacy(Pharmacy originPharmacy) {
		this.originPharmacy = originPharmacy;
	}	
}
