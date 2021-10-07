package org.openmrs.module.pharmacymanagement;

public class PrescriptionRequest {
	String qtyTakenAtOnceField;
	String timesPerDayField;
	String daysField;
	String dnameField;
	String routeField;
	String frequencyIdField;
	String dquantityField;
	String dstartDateField;
	String dinstructionsField;
	
	

	public PrescriptionRequest() {
		super();
	}

	public PrescriptionRequest(String qtyTakenAtOnceField, String timesPerDayField, String daysField, String dnameField,
			String routeField, String frequencyIdField, String dquantityField, String dstartDateField,
			String dinstructionsField) {
		super();
		this.qtyTakenAtOnceField = qtyTakenAtOnceField;
		this.timesPerDayField = timesPerDayField;
		this.daysField = daysField;
		this.dnameField = dnameField;
		this.routeField = routeField;
		this.frequencyIdField = frequencyIdField;
		this.dquantityField = dquantityField;
		this.dstartDateField = dstartDateField;
		this.dinstructionsField = dinstructionsField;
	}

	public String getQtyTakenAtOnceField() {
		return qtyTakenAtOnceField;
	}

	public void setQtyTakenAtOnceField(String qtyTakenAtOnceField) {
		this.qtyTakenAtOnceField = qtyTakenAtOnceField;
	}

	public String getTimesPerDayField() {
		return timesPerDayField;
	}

	public void setTimesPerDayField(String timesPerDayField) {
		this.timesPerDayField = timesPerDayField;
	}

	public String getDaysField() {
		return daysField;
	}

	public void setDaysField(String daysField) {
		this.daysField = daysField;
	}

	public String getDnameField() {
		return dnameField;
	}

	public void setDnameField(String dnameField) {
		this.dnameField = dnameField;
	}

	public String getRouteField() {
		return routeField;
	}

	public void setRouteField(String routeField) {
		this.routeField = routeField;
	}

	public String getFrequencyIdField() {
		return frequencyIdField;
	}

	public void setFrequencyIdField(String frequencyIdField) {
		this.frequencyIdField = frequencyIdField;
	}

	public String getDquantityField() {
		return dquantityField;
	}

	public void setDquantityField(String dquantityField) {
		this.dquantityField = dquantityField;
	}

	public String getDstartDateField() {
		return dstartDateField;
	}

	public void setDstartDateField(String dstartDateField) {
		this.dstartDateField = dstartDateField;
	}

	public String getDinstructionsField() {
		return dinstructionsField;
	}

	public void setDinstructionsField(String dinstructionsField) {
		this.dinstructionsField = dinstructionsField;
	}

}
