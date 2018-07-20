/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement;

import org.openmrs.Location;

/**
 * @author Dusabe E.
 *
 */
public class Pharmacy {
	private int pharmacyId;
	private String name;
	private Location locationId;
	/**
	 * @return the pharmacyId
	 */
	public int getPharmacyId() {
		return pharmacyId;
	}
	/**
	 * @param pharmacyId the pharmacyId to set
	 */
	public void setPharmacyId(int pharmacyId) {
		this.pharmacyId = pharmacyId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
}
