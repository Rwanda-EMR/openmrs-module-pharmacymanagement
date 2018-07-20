/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement;

import java.util.Date;

import org.openmrs.Encounter;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.User;

/**
 *
 */
public class DrugOrderPrescription {
	
	private int drugOrderPrescriptionId;
	private Patient patient;
	private User user;
	private DrugProduct drugproductId;
	private int quantity;
	private Date date;
	private Encounter encounterId;
	private Order orderId;
	
	/**
	 * @return the orders
	 */
	public Encounter getEncounterId() {
		return encounterId;
	}
	/**
	 * @param orders the orders to set
	 */
	public void setEncounterId(Encounter encounterId) {
		this.encounterId = encounterId;
	}
	/**
	 * @return the drugOrderId
	 */
	public int getDrugOrderPrescriptionId() {
		return drugOrderPrescriptionId;
	}
	/**
	 * @param drugOrderId the drugOrderId to set
	 */
	public void setDrugOrderPrescriptionId(int drugOrderPrescriptionId) {
		this.drugOrderPrescriptionId = drugOrderPrescriptionId;
	}
	/**
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}
	/**
	 * @param patient the patient to set
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
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
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	 * @return the orderId
	 */
	public Order getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Order orderId) {
		this.orderId = orderId;
	}
}
