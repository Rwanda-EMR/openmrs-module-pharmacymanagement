/**
 * @author Eric
 */
package org.openmrs.module.pharmacymanagement;

import java.util.Date;

import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.User;

/**
 *
 */
public class ConsumableDispense {
	private int consumabledispenseId;
	private Date date;
	private int qnty;
	private Concept service;
	private User provider;
	private Patient patientId;
	private DrugProduct drugproductId;
	private String patientNames;
	/**
	 * @return the consumabledispenseId
	 */
	public int getConsumabledispenseId() {
		return consumabledispenseId;
	}
	/**
	 * @param consumabledispenseId the consumabledispenseId to set
	 */
	public void setConsumabledispenseId(int consumabledispenseId) {
		this.consumabledispenseId = consumabledispenseId;
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
	 * @return the qnty
	 */
	public int getQnty() {
		return qnty;
	}
	/**
	 * @param qnty the qnty to set
	 */
	public void setQnty(int qnty) {
		this.qnty = qnty;
	}
	/**
	 * @return the service
	 */
	public Concept getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(Concept service) {
		this.service = service;
	}
	/**
	 * @return the provider
	 */
	public User getProvider() {
		return provider;
	}
	/**
	 * @param provider the provider to set
	 */
	public void setProvider(User provider) {
		this.provider = provider;
	}	
	/**
	 * @return the patientId
	 */
	public Patient getPatientId() {
		return patientId;
	}
	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(Patient patientId) {
		this.patientId = patientId;
	}
	
	/**
	 * get patient's name
	 * 
	 * @param patientId
	 * @return
	 */
	public String getPatientNames() {
		return patientId.getFamilyName() + " " + patientId.getMiddleName() + " " + patientId.getGivenName();
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
}
