/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement;

import java.util.List;

/**
 *@author Eric D.
 */
public class ReceiptDrug {
private int receiptDrugId;
private CmdDrug cmdDrug;
private List<DrugProduct> drugproducts;
/**
 * @return the receiptDrugId
 */
public int getReceiptDrugId() {
	return receiptDrugId;
}
/**
 * @param receiptDrugId the receiptDrugId to set
 */
public void setReceiptDrugId(int receiptDrugId) {
	this.receiptDrugId = receiptDrugId;
}
/**
 * @return the cmdDrug
 */
public CmdDrug getCmdDrug() {
	return cmdDrug;
}
/**
 * @param cmdDrug the cmdDrug to set
 */
public void setCmdDrug(CmdDrug cmdDrug) {
	this.cmdDrug = cmdDrug;
}
/**
 * @return the drugproducts
 */
public List<DrugProduct> getDrugproducts() {
	return drugproducts;
}
/**
 * @param drugproducts the drugproducts to set
 */
public void setDrugproducts(List<DrugProduct> drugproducts) {
	this.drugproducts = drugproducts;
}

}
