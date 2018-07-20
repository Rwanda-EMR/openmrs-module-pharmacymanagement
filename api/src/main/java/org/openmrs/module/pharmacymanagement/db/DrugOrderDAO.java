package org.openmrs.module.pharmacymanagement.db;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.module.pharmacymanagement.ProductReturnStore;
import org.openmrs.module.pharmacymanagement.CmdDrug;
import org.openmrs.module.pharmacymanagement.ConsumableDispense;
import org.openmrs.module.pharmacymanagement.DrugDetails;
import org.openmrs.module.pharmacymanagement.DrugOrderPrescription;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.DrugProductInventory;
import org.openmrs.module.pharmacymanagement.DrugStore;
import org.openmrs.module.pharmacymanagement.Pharmacy;
import org.openmrs.module.pharmacymanagement.PharmacyInventory;

/**
 *
 */
public interface DrugOrderDAO {
	
	/**
	 * saving the drug order in stock 
	 * @param cmdDrug
	 */
	public void saveCmdDrug(CmdDrug cmdDrug);
	
	/**
	 * saving the Drug in stock 
	 * @param drugProduct
	 */
	public void saveDrugProduct(DrugProduct drugProduct);
	
	/**
	 * Get all Drugs in the store 
	 * 
	 */
	public Collection<CmdDrug> getOrders();
	
		
	/**
	 * finds an product in the store by passing FOSAId as parameter
	 * 
	 * @param locationId
	 */
	public Collection<DrugStore> findProductByLocation(Location location);
	
	/**
	 * finds products by passing the Store Keeper Id as parameter
	 * @param providerId
	 */
	public Collection<DrugStore> findProductByStoreKeeper(User user);
	
	/**
	 * finds products by passing the expiration date as parameter
	 * @param providerId
	 */
	public Collection<DrugStore> findProductsByDate(Date expiryDate);
	
	
	/**
	 * get a Command Drug by passing the cmddrugId
	 * @parameter productId
	 */
	public CmdDrug getCmdDrugById(int cmddrugId);
	
	
	/**
	 * updates DrugStore
	 * @param drugStore
	 */
	public void updateStore(DrugProduct drugProduct);
	
	/**
	 * cancel a product by passing the product id as parameter
	 * 
	 * @param ProductId
	 */
	public void cancelProduct(DrugStore drugStore);
	
	/**
	 * find drug by passing the Command Drug Id as parameter
	 * 
	 * @param cmddrugId
	 */
	public Collection<DrugProduct> getDrugProductByCmdDrugId(CmdDrug cmddrug);
	
	/**
	 * find product by passing the product id as parameter
	 * @param drugproductId
	 */
	public DrugProduct getDrugProductById(int drugproductId);
	
	/**
	 * get all drug products
	 */
	public Collection<DrugProduct> getAllProducts();
	
	/**
	 * find Drug Orders by Supporting program
	 * @param prog
	 * @return a Collection of Drug Orders 
	 */
	public Collection<CmdDrug> findDrugOrdersByProg(String prog);
	
	/**
	 * Find Drug Orders By Month
	 * @param mois
	 * @return Collection<CmdDrug>
	 */
	public Collection<CmdDrug> findDrugOrdersByMonth(int mois);
	
	/**
	 * Find Drug Orders By Location, Supporting program and Month
	 * 
	 * @param locationId
	 * @param supporting
	 * @param mois
	 * @return
	 */
	public Collection<CmdDrug> findOrdersByLocSupProgMonth(String locationId, String supporting, String mois, String pharmacyId, String type);
	
	/**
	 * saving inventory
	 * 
	 * @param drugproductInventory
	 */
	public void saveInventory(DrugProductInventory drugproductInventory);
	
	/**
	 * gets <code>DrugProductInventory</code>
	 * 
	 * @param dpiId
	 * @return <code>DrugProductInventory</code>
	 */
	public DrugProductInventory getDrugProductInventoryById(int dpiId);
	
	/**
	 * gets all <code>DrugProductInventory</code>
	 * 
	 * @return
	 */
	public List<DrugProductInventory> getAllDrugProductInventory();
		
	/**
	 * gets current solde
	 * 
	 * @param drugId
	 * @param locationId
	 * @return <code>Integer</code>
	 */
	public Integer getCurrSolde(String drugId, String conceptId, String locationId, String expiryDate, String lotNo, String cmddrug);
	
	/**
	 * get inventory
	 * 
	 * @param locationId
	 * @param supporter
	 * @param mois
	 * @param from
	 * @param to
	 * @return
	 */
	public Collection<DrugProductInventory> getDrugInventoryByDrugId(String drugId, String conceptId, String locationId, String pharmacyId);
	
	/**
	 *return different calculation on a particular drug
	 * 
	 * @param DrugId
	 * @return
	 */
	public List<Object[]> getCalculations(int drugId, int isStore);
	
	/**
	 * save the pharmacy location
	 * 
	 * @param pharmacy
	 */
	public void savePharmacy(Pharmacy pharmacy);
	
	/**
	 * returns all the pharmacies
	 * 
	 * @return Pharmacy
	 */
	public Collection<Pharmacy> getAllPharmacies();
	
	/**
	 * get pharmacy by passing pharmacyId as parameter
	 * 
	 * @param pharmacyId
	 * @return Pharmacy
	 */
	public Pharmacy getPharmacyById(int pharmacyId);
	
	/**
	 * returns pharmacy by passing <code>Location</code> 
	 * 
	 * @return <code>Pharmacy</code>
	 */
	public List<Pharmacy> getPharmacyByLocation(Location location);
	
	/**
	 * Auto generated method comment
	 * 
	 * @param pharmacy
	 * @return <code>Location</code>
	 */
	public Location getLocationByPharmacy(Pharmacy pharmacy);
	
	/**
	 * Delete the Pharmacy object by passing pharmacy as parameter
	 * 
	 * @param pharmacy
	 */
	public void cancelPharmacy(Pharmacy pharmacy);
		
	/**
	 * Save or update the DrugDetails object 
	 * @param drugDetails
	 */	
	public void saveDrugDetails(DrugDetails drugDetail);
	
	/**
	 * returns all the DrugDetails
	 * 
	 * @return DrugDetails
	 */
	public Collection<DrugDetails> getAllDrugDetails();
	
	/**
	 * get DrugDetail by passing drugDetailsId as parameter
	 * 
	 * @param drugDetailsId
	 * @return DrugDetail
	 */
	public DrugDetails getDrugDetailsById(int drugDetailId);
	
	/**
	 * Delete the DrugDetail object by passing drugDetailId as parameter
	 * 
	 * @param drugDetailId
	 */
	public void cancelDrugDetail(DrugDetails drugDetail);
	
	/**
	 * Save or update the DrugOrderPrescription object 
	 * @param drugOrderPrescription
	 */	
	public void saveDrugOrderPrescription(DrugOrderPrescription drugOrderPrescription);
	
	/**
	 * returns all the DrugOrderPrescription
	 * 
	 * @return DrugOrderPrescription
	 */
	public Collection<DrugOrderPrescription> getAllDrugOrderPrescription();
	
	/**
	 * get DrugOrderPrescription by passing drugOrderPrescriptionId as parameter
	 * 
	 * @param drugOrderPrescriptionId
	 * @return DrugOrderPrescription
	 */
	public DrugOrderPrescription getDrugOrderPrescriptionById(int drugOrderPrescriptionId);
	
	
	/**
	 * returns different statistics on a particular drug
	 * 
	 * @param DrugId
	 * @return a List of objects
	 */
	public List<Object[]> getdrugStatistics(String drugId, String conceptId);
	
	/**
	 * get the current solde of a particular dispensed drug
	 * 
	 * @return current solde
	 */
	public Integer getCurrSoldeDisp(String drugId, String conceptId, String pharmacy, String expiryDate, String lotNo, String nd);
	
	/**
	 * saves the pharmacy inventory
	 */
	public void savePharmacyInventory(PharmacyInventory pharmacyInventory);
	
	/**
	 * get all PharmacyInventory
	 * 
	 * @return a collection of PharmacyInventory
	 */
	public Collection<PharmacyInventory> getAllPharmacyInventory(); 
	
	/**
	 * returns Collection<<code>PharmacyInventory</code>> by <code>Date</code> and <code>Location</code>
	 * 
	 * @return pharmacyInventories
	 */
	public Collection<PharmacyInventory> getPharmacyInventoryByFromToLocation(String from, String to, String pharmacyId);
	
	/**
	 * returns a collection of <code>DrugProduct</code> by passing the date from up to and the location
	 * 
	 * @param from
	 * @param to
	 * @param locationId
	 * @return <code>Collection</code><DrugProduct>
	 */
	public Collection<DrugProduct> getARVReportByFromTo(String from, String to, String locationId);
	
	
	/**
	 * Returns the drug being consumed by a patient appointed on the next visit <code>Obs</code>
	 * 
	 * @returns drugId <code>Drug</code>
	 */
	public List<Integer> getDrugByNextVisitConcept();
	
	
	/**
	 * returns a <code>List</code><<code>DrugProduct</code>> that  remainder is 2 month to expiry
	 * 
	 * @return <code>List</code> of <code>DrugProduct</code>
	 */
	public List<DrugProduct> getProductListAboutToExpiry();
	
	/**
	 * returns the solde by passing the date, drug and the location
	 */
	public Integer getSoldeByFromDrugLocation(String from, String drugId, String conceptId, String locationId);
	
	/**
	 * returns the solde by passing the date, drug and the location
	 */
	public Integer getSoldeByToDrugLocation(String to, String drugId, String conceptId, String locationId);
	
	/**
	 * returns the sum of sortie and entree by passing the date, drug and the location 
	 */
	public Object[] getSumEntreeSortieByFromToDrugLocation(String from, String to, String drugId, String conceptId, String locationId);
	
	/**
	 * returns the quantity of drug in store the first day of the week reported
	 * 
	 * @param from
	 * @param drugId
	 * @param pharmacyId
	 * @return
	 */
	public Integer getPharmacySoldeFirstDayOfWeek(String from, String drugId, String conceptId, String pharmacyId);
	
	/**
	 * returns the quantity of drugs in store the last day of the reported week
	 * 
	 * @param to
	 * @param drugId
	 * @param pharmacyId
	 * @return
	 */
	public Integer getPharmacySoldeLastDayOfWeek(String to, String drugId, String conceptId, String pharmacyId);
	
	/**
	 * returns the quantity of the denspensed and received drug during the week reported
	 * 
	 * @param from
	 * @param to
	 * @param drugId
	 * @param pharmacyId
	 * @return
	 */
	public Object[] getReceivedDispensedDrug(String from, String to, String drugId, String pharmacyId);
	
	/**
	 * returns the <code>DrugOrderPrescription</code> by passing the orderId
	 * 
	 * @param orderId
	 * @return <code>DrugOrderPrescription</code>
	 */
	public DrugOrderPrescription getDOPByOrderId(String orderId);
	
	/**
	 * returns <code>PharmacyInventory</code> by passing the dopId
	 * 
	 * @param <code>String</code> dopId
	 * @return <code>PharmacyInventory</code>
	 */
	public PharmacyInventory getPIbyDOPId(String dopId);
	
	/**
	 * gets the <code>DrugOrderPrescription</code> by passing the patientId
	 * 
	 * @param patientId
	 * @return <code>DrugOrderPrescription</code>
	 */
	public List<DrugOrderPrescription> getDOPByPatientId(Patient patient);
	
	/**
	 * gets the lot numbers and expirations dates on a <code>Drug</code>
	 * 
	 * @param drugId
	 * @param locationId
	 * @param pharmacyId
	 * @return a list of <code>Object</code>
	 */
	public List<Object[]> getLotNumbersExpirationDates(String drugId, String conceptId, String locationId, String pharmacyId);
	
	/**
	 * Save or update the <code>ProductReturnStore</code>
	 * 
	 * @param returnedStore
	 */
	public void saveReturnStock(ProductReturnStore returnedStore);
	
	/**
	 * returns <code>ProductReturnStore</code>
	 * 
	 * @param arsId
	 * @return <code>ProductReturnStore</code>
	 */
	public ProductReturnStore getReturnStockById(int arsId);
	
	/**
	 * returns <code>ProductReturnStore</code> by passing the <code>String</code> date
	 * 
	 * @param date
	 * @return
	 */
	//public List<ProductReturnStore> getReturnStockByDate(Date date);
	public abstract List<ProductReturnStore> getReturnStockByDate(Date paramDate, String paramString);
	
	/**
	 * returns <code>ProductReturnStore</code> by passing the <code>DrugProduct</code> dp
	 * 
	 * @param dp
	 * @return a list of <code>ProductReturnStore</code>
	 */
	public List<ProductReturnStore> getReturnStockByDP(DrugProduct dp);
	
	/**
	 * Saves or Updates <code>ConsumableDispense</code>
	 * 
	 * @return
	 */
	public void saveOrUpdateConsumableDispense(ConsumableDispense consumableDispense);
	
	/**
	 * gets all the <code>ConsumableDispense</code>
	 * 
	 * @return a <code>java.util.List</code> of <code>ConsumableDispense</code>
	 */
	public List<ConsumableDispense> getAllConsumableDipsense();

    public abstract Integer getReturnedItemsByDates(String paramString1, String paramString2, DrugProduct paramDrugProduct, String paramString3);
		
}
