package org.openmrs.module.pharmacymanagement;

import java.util.Date;

public class Adherance {
	private DrugOrderPrescription dop;
	private Double weight;
	private Date nvDate;
	private Date encDate;
	
	
	public DrugOrderPrescription getDop() {
		return dop;
	}
	public void setDop(DrugOrderPrescription dop) {
		this.dop = dop;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Date getNvDate() {
		return nvDate;
	}
	public void setNvDate(Date nvDate) {
		this.nvDate = nvDate;
	}
	public Date getEncDate() {
		return encDate;
	}
	public void setEncDate(Date encDate) {
		this.encDate = encDate;
	}
}
