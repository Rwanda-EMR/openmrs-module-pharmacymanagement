/**
 * @author Eric
 */
package org.openmrs.module.pharmacymanagement;

import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.User;

import java.util.Date;

/**
 *
 */
public class ConsumableOrder {
	private int consumableOrderId;
	private Date date;
	private int qnty;
	private Concept consumable;
	private User provider;
	private Patient patientId;

	private Boolean isDispensed =false;
	private User dispensedBy;
	private Date dispensedDate;
	private int consumabledispenseId;



	public int getConsumableOrderId() {
		return consumableOrderId;
	}

	public void setConsumableOrderId(int consumableOrderId) {
		this.consumableOrderId = consumableOrderId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getQnty() {
		return qnty;
	}

	public void setQnty(int qnty) {
		this.qnty = qnty;
	}

	public Concept getConsumable() {
		return consumable;
	}

	public void setConsumable(Concept consumable) {
		this.consumable = consumable;
	}

	public User getProvider() {
		return provider;
	}

	public void setProvider(User provider) {
		this.provider = provider;
	}

	public Patient getPatientId() {
		return patientId;
	}

	public void setPatientId(Patient patientId) {
		this.patientId = patientId;
	}


	public Boolean getIsDispensed() {
		return isDispensed;
	}

	public void setIsDispensed(Boolean dispensed) {
		isDispensed = dispensed;
	}

	public User getDispensedBy() {
		return dispensedBy;
	}

	public void setDispensedBy(User dispensedBy) {
		this.dispensedBy = dispensedBy;
	}

	public Date getDispensedDate() {
		return dispensedDate;
	}

	public void setDispensedDate(Date dispensedDate) {
		this.dispensedDate = dispensedDate;
	}

	public int getConsumabledispenseId() {
		return consumabledispenseId;
	}

	public void setConsumabledispenseId(int consumabledispenseId) {
		this.consumabledispenseId = consumabledispenseId;
	}
}
