package org.openmrs.module.pharmacymanagement;

public class StoreWarning {
	private String drugName;
	private int consumed;
	private int in;
	private int store;
	private String lotNo;
	private String expirationDate;
	
	public StoreWarning() {
	}
	
	public StoreWarning(String drugName, int consumed, int in, int store, String lotNo, String expirationDate) {
		this.drugName = drugName;
		this.consumed = consumed;
		this.in = in;
		this.store = store;
		this.lotNo = lotNo;
		this.expirationDate = expirationDate;
	}
	
	/**
	 * @return the drugName
	 */
	public String getDrugName() {
		return drugName;
	}
	/**
	 * @param drugName the drugName to set
	 */
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	/**
	 * @return the consumed
	 */
	public int getConsumed() {
		return consumed;
	}
	/**
	 * @param consumed the consumed to set
	 */
	public void setConsumed(int consumed) {
		this.consumed = consumed;
	}
	/**
	 * @return the in
	 */
	public int getIn() {
		return in;
	}
	/**
	 * @param in the in to set
	 */
	public void setIn(int in) {
		this.in = in;
	}
	/**
	 * @return the store
	 */
	public int getStore() {
		return store;
	}
	/**
	 * @param store the store to set
	 */
	public void setStore(int store) {
		this.store = store;
	}
	/**
	 * @return the lotNo
	 */
	public String getLotNo() {
		return lotNo;
	}
	/**
	 * @param lotNo the lotNo to set
	 */
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	/**
	 * @return the expirationDate
	 */
	public String getExpirationDate() {
		return expirationDate;
	}
	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
}
