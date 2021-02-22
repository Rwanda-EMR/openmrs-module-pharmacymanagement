/*
 * Decompiled with CFR 0_123.
 *
 * Could not load the following classes:
 *  org.openmrs.Location
 *  org.openmrs.Patient
 *  org.openmrs.User
 *  org.openmrs.module.pharmacymanagement.CmdDrug
 *  org.openmrs.module.pharmacymanagement.ConsumableDispense
 *  org.openmrs.module.pharmacymanagement.DrugDetails
 *  org.openmrs.module.pharmacymanagement.DrugOrderPrescription
 *  org.openmrs.module.pharmacymanagement.DrugProduct
 *  org.openmrs.module.pharmacymanagement.DrugProductInventory
 *  org.openmrs.module.pharmacymanagement.DrugStore
 *  org.openmrs.module.pharmacymanagement.Pharmacy
 *  org.openmrs.module.pharmacymanagement.PharmacyInventory
 *  org.openmrs.module.pharmacymanagement.ProductReturnStore
 *  org.springframework.transaction.annotation.Transactional
 */
package org.openmrs.module.pharmacymanagement.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.module.pharmacymanagement.CmdDrug;
import org.openmrs.module.pharmacymanagement.ConsumableDispense;
import org.openmrs.module.pharmacymanagement.DrugDetails;
import org.openmrs.module.pharmacymanagement.DrugOrderPrescription;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.DrugProductInventory;
import org.openmrs.module.pharmacymanagement.DrugStore;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.PharmacyInventory;
import org.openmrs.module.pharmacymanagement.ProductReturnStore;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DrugOrderService {
	public void saveCmdDrug(CmdDrug var1);

	public void saveDrugProduct(DrugProduct var1);

	public Collection<CmdDrug> getOrders();

	public Collection<DrugStore> findProductByLocation(Location var1);

	public Collection<DrugStore> findProductByStoreKeeper(User var1);

	public Collection<DrugStore> findProductsByDate(Date var1);

	public CmdDrug getCmdDrugById(int var1);

	public void updateStore(DrugProduct var1);

	public void cancelProduct(DrugStore var1);

	public Collection<DrugProduct> getDrugProductByCmdDrugId(CmdDrug var1);

	public DrugProduct getDrugProductById(int var1);

	public Collection<DrugProduct> getAllProducts();

	public Collection<CmdDrug> findDrugOrdersByProg(String var1);

	public Collection<CmdDrug> findDrugOrdersByMonth(int var1);

	public Collection<CmdDrug> findOrdersByLocSupProgMonth(String var1, String var2, String var3, String var4, String var5);

	public void saveInventory(DrugProductInventory var1);

	public DrugProductInventory getDrugProductInventoryById(int var1);

	public List<DrugProductInventory> getAllDrugProductInventory();

	public Integer getCurrSolde(String var1, String var2, String var3, String var4, String var5, String var6);

	public Collection<DrugProductInventory> getDrugInventoryByDrugId(String var1, String var2, String var3, String var4);

	public List<Object[]> getCalculations(int var1, int var2);

	public void savePharmacy(Pharmacy var1);

	public Collection<Pharmacy> getAllPharmacies();

	public Pharmacy getPharmacyById(int var1);

	public List<Pharmacy> getPharmacyByLocation(Location var1);

	public Location getLocationByPharmacy(Pharmacy var1);

	public void cancelPharmacy(Pharmacy var1);

	public void saveDrugDetails(DrugDetails var1);

	public Collection<DrugDetails> getAllDrugDetails();

	public DrugDetails getDrugDetailsById(int var1);

	public void cancelDrugDetail(DrugDetails var1);

	public void saveDrugOrderPrescription(DrugOrderPrescription var1);

	public Collection<DrugOrderPrescription> getAllDrugOrderPrescription();

	public DrugOrderPrescription getDrugOrderPrescriptionById(int var1);

	public List<Object[]> getdrugStatistics(String var1, String var2);

	public Integer getCurrSoldeDisp(String var1, String var2, String var3, String var4, String var5, String var6);

	public void savePharmacyInventory(PharmacyInventory var1);

	public Collection<PharmacyInventory> getAllPharmacyInventory();

	public Collection<PharmacyInventory> getPharmacyInventoryByFromToLocation(String var1, String var2, String var3);

	public Collection<DrugProduct> getARVReportByFromTo(String var1, String var2, String var3);

	public List<Integer> getDrugByNextVisitConcept();

	public List<DrugProduct> getProductListAboutToExpiry();

	public Integer getSoldeByFromDrugLocation(String var1, String var2, String var3, String var4);

	public Integer getSoldeByToDrugLocation(String var1, String var2, String var3, String var4);

	public Integer getSoldeByDrugOrConcept(String drugId, String conceptId);

	public Object[] getSumEntreeSortieByFromToDrugLocation(String var1, String var2, String var3, String var4, String var5);

	public Integer getPharmacySoldeFirstDayOfWeek(String var1, String var2, String var3, String var4);

	public Integer getPharmacySoldeLastDayOfWeek(String var1, String var2, String var3, String var4);

	public Object[] getReceivedDispensedDrugOrConsumable(String var1, String var2, String var3, String var4,String var5);

	public DrugOrderPrescription getDOPByOrderId(String var1);

	public PharmacyInventory getPIbyDOPId(String var1);

	public List<DrugOrderPrescription> getDOPByPatientId(Patient var1);

	public List<Object[]> getLotNumbersExpirationDates(String var1, String var2, String var3, String var4);

	public void saveReturnStock(ProductReturnStore var1);

	public ProductReturnStore getReturnStockById(int var1);

	public Integer getReturnedItemsByDates(String var1, String var2, DrugProduct var3, String var4);

	public List<ProductReturnStore> getReturnStockByDate(Date var1, String var2);

	public List<ProductReturnStore> getReturnStockByDP(DrugProduct var1);

	public void saveOrUpdateConsumableDispense(ConsumableDispense var1);

	public List<ConsumableDispense> getAllConsumableDipsense();

	public List<Object[]> getStoreStatus();

	public List<Object[]> getLotNumberByDrugProductId(int drugProductId);
	public Boolean checkIfOneDrugOrConsummableUseOneLotNo(String drugId,String conceptId,String lotNo);
	public Collection<DrugProduct> getPharmacyDrugProducts();
	public Collection<DrugProduct> getPharmacyConsummableProducts();

}