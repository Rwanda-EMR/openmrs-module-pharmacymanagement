/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement;

import java.util.Date;

import org.openmrs.Concept;
import org.openmrs.Drug;

/**
 *@author Eric D.
 */
public class DrugProduct {
	
	private int drugproductId;
	private Drug drugId;
	private int storeQnty;
	private int qntyReq;
	private String lotNo;
	private Date expiryDate;
	private int deliveredQnty;
	private boolean isDelivered;
	private String comments;
	private CmdDrug cmddrugId;
	private Concept conceptId;

	
	
	/**
	 * @return the drugproductId
	 */
	public int getDrugproductId() {
		return drugproductId;
	}
	/**
	 * @param drugproductId the drugproductId to set
	 */
	public void setDrugproductId(Integer drugproductId) {
		this.drugproductId = drugproductId;
	}
	/**
	 * @return the cmdDrug
	 */
	public CmdDrug getCmddrugId() {
		return cmddrugId;
	}
	/**
	 * @param cmdDrug the cmdDrug to set
	 */
	public void setCmddrugId(CmdDrug cmddrugId) {
		this.cmddrugId = cmddrugId;
	}
	/**
	 * @return the drug
	 */
	public Drug getDrugId() {
		return drugId;
	}
	/**
	 * @param drug the drug to set
	 */
	public void setDrugId(Drug drugId) {
		this.drugId = drugId;
	}
	/**
	 * @return the storeQnty
	 */
	public int getStoreQnty() {
		return storeQnty;
	}
	/**
	 * @param storeQnty the storeQnty to set
	 */
	public void setStoreQnty(int storeQnty) {
		this.storeQnty = storeQnty;
	}
	/**
	 * @return the qntyReq
	 */
	public int getQntyReq() {
		return qntyReq;
	}
	/**
	 * @param qntyReq the qntyReq to set
	 */
	public void setQntyReq(int qntyReq) {
		this.qntyReq = qntyReq;
	}
	/**
	 * @return the noLot
	 */
	public String getLotNo() {
		return lotNo;
	}
	/**
	 * @param noLot the noLot to set
	 */
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}
	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	/**
	 * @return the deliveredQnty
	 */
	public int getDeliveredQnty() {
		return deliveredQnty;
	}
	/**
	 * @param deliveredQnty the deliveredQnty to set
	 */
	public void setDeliveredQnty(int deliveredQnty) {
		this.deliveredQnty = deliveredQnty;
	}
	
	/**
	 * @return the isDelivered
	 */
	public boolean getIsDelivered() {
		return isDelivered;
	}
	/**
	 * @param b the isDelivered to set
	 */
	public void setIsDelivered() {
		
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	/**
	 * @param the isDelivered to set
	 */
	public void setIsDelivered(boolean isDelivered) {
		this.isDelivered = isDelivered;
		
	}
	/**
	 * @return the conceptId
	 */
	public Concept getConceptId() {
		return conceptId;
	}
	/**
	 * @param conceptId the conceptId to set
	 */
	public void setConceptId(Concept conceptId) {
		this.conceptId = conceptId;
	}
	
	
	 	
}
