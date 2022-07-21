/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.module.pharmacymanagement.*;
import org.openmrs.module.pharmacymanagement.db.DrugOrderDAO;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;

/**
 *
 */
public class DrugOrderServiceImpl implements DrugOrderService {
	private DrugOrderDAO drugOrderDAO;
	public DrugOrderServiceImpl() {};
	/**
	 * @return the drugStoreDao
	 */
	public DrugOrderDAO getDrugOrderDAO() {
		return drugOrderDAO;
	}

	/**
	 * @param drugStoreDao the drugStoreDao to set
	 */
	public void setDrugOrderDAO(DrugOrderDAO drugOrderDAO) {
		this.drugOrderDAO = drugOrderDAO;
	}

	@Override
	public void cancelProduct(DrugStore product) {
		drugOrderDAO.cancelProduct(product);
	}

	@Override
	public Collection<DrugStore> findProductByLocation(Location location) {
		return drugOrderDAO.findProductByLocation(location);
	}

	@Override
	public Collection<DrugStore> findProductByStoreKeeper(User user) {
		return drugOrderDAO.findProductByStoreKeeper(user);
	}

	@Override
	public Collection<DrugStore> findProductsByDate(Date expiryDate) {
		return drugOrderDAO.findProductsByDate(expiryDate);
	}

	@Override
	public Collection<CmdDrug> getOrders() {	
		return drugOrderDAO.getOrders();
	}

	@Override
	public void saveCmdDrug(CmdDrug cmdDrug) {
		drugOrderDAO.saveCmdDrug(cmdDrug);		
	}
	
	@Override
	public void saveDrugProduct(DrugProduct drugProduct) {
		drugOrderDAO.saveDrugProduct(drugProduct);		
	}
	
	@Override
	public void updateStore(DrugProduct product) {
		drugOrderDAO.updateStore(product);
	}

	@Override
	public CmdDrug getCmdDrugById(int cmddrugId) {		
		return drugOrderDAO.getCmdDrugById(cmddrugId);
	}

	@Override
	public Collection<DrugProduct> getDrugProductByCmdDrugId(CmdDrug cmddrug) {
		return drugOrderDAO.getDrugProductByCmdDrugId(cmddrug);
		
	}

	@Override
	public DrugProduct getDrugProductById(int drugproductId) {
		return drugOrderDAO.getDrugProductById(drugproductId);
	}

	@Override
	public Collection<DrugProduct> getAllProducts() {
		return drugOrderDAO.getAllProducts();
	}

	@Override
	public Collection<CmdDrug> findDrugOrdersByProg(String prog) {
		return drugOrderDAO.findDrugOrdersByProg(prog);
	}

	@Override
	public Collection<CmdDrug> findDrugOrdersByMonth(int mois) {
		return drugOrderDAO.findDrugOrdersByMonth(mois);
	}

	@Override
	public Collection<CmdDrug> findOrdersByLocSupProgMonth(String locationId,
			String supporting, String mois, String pharmacyId, String type) {
		return drugOrderDAO.findOrdersByLocSupProgMonth(locationId, supporting, mois, pharmacyId, type);
	}

	@Override
	public void saveInventory(DrugProductInventory drugproductInventory) {
		drugOrderDAO.saveInventory(drugproductInventory);		
	}
	
	@Override
	public DrugProductInventory getDrugProductInventoryById(int dpiId) {
		return drugOrderDAO.getDrugProductInventoryById(dpiId);
	}
	
	@Override
	public List<DrugProductInventory> getAllDrugProductInventory() {
		return drugOrderDAO.getAllDrugProductInventory();
	}

	@Override
	public Integer getCurrSolde(String drugId, String conceptId, String locationId, String expiryDate, String lotNo, String cmddrug) {
		return drugOrderDAO.getCurrSolde(drugId, conceptId, locationId, expiryDate, lotNo, cmddrug);
	}

	public List<Integer> getDrugsCurrSolde() {
		return drugOrderDAO.getDrugsCurrSolde();
	}

	@Override
	public Map<Integer, Integer> getCurrSoldeOfDrugProducts(List<DrugProduct> drugProducts) {
		return drugOrderDAO.getCurrSoldeOfDrugProducts(drugProducts);
	}

	public List<Integer> getConsummablesCurrSolde() {
		return drugOrderDAO.getConsummablesCurrSolde();
	}
	@Override
	public Collection<DrugProductInventory> getDrugInventoryByDrugId(String drugId, String conceptId, String locationId, String pharmacyId) {
		return drugOrderDAO.getDrugInventoryByDrugId(drugId, conceptId, locationId, pharmacyId);
	}

	@Override
	public List<Object[]> getCalculations(int drugId, int isStore) {
		return drugOrderDAO.getCalculations(drugId, isStore);
	}

	@Override
	public void savePharmacy(Pharmacy pharmacy) {
		drugOrderDAO.savePharmacy(pharmacy);		
	}

	@Override
	public Collection<Pharmacy> getAllPharmacies() {
		return drugOrderDAO.getAllPharmacies();
	}

	@Override
	public Pharmacy getPharmacyById(int pharmacyId) {
		return drugOrderDAO.getPharmacyById(pharmacyId);
	}
	
	@Override
	public List<Pharmacy> getPharmacyByLocation(Location location) {
		return drugOrderDAO.getPharmacyByLocation(location);
	}

	@Override
	public void saveDrugDetails(DrugDetails drugDetails) {
		drugOrderDAO.saveDrugDetails(drugDetails);
	}

	@Override
	public void cancelDrugDetail(DrugDetails drugDetail) {
		drugOrderDAO.cancelDrugDetail(drugDetail);
	}

	@Override
	public Collection<DrugDetails> getAllDrugDetails() {
		return drugOrderDAO.getAllDrugDetails();
	}

	@Override
	public DrugDetails getDrugDetailsById(int drugDetailId) {
		return drugOrderDAO.getDrugDetailsById(drugDetailId);
	}

	@Override
	public void cancelPharmacy(Pharmacy pharmacy) {
		drugOrderDAO.cancelPharmacy(pharmacy);
	}

	@Override
	public Collection<DrugOrderPrescription> getAllDrugOrderPrescription() {
		return drugOrderDAO.getAllDrugOrderPrescription();
	}

	@Override
	public DrugOrderPrescription getDrugOrderPrescriptionById(int drugOrderPrescriptionId) {
		return drugOrderDAO.getDrugOrderPrescriptionById(drugOrderPrescriptionId);
	}

	@Override
	public void saveDrugOrderPrescription(
			DrugOrderPrescription drugOrderPrescription) {
		drugOrderDAO.saveDrugOrderPrescription(drugOrderPrescription);
	}

	@Override
	public List<Object[]> getdrugStatistics(String drugId, String conceptId) {
		return drugOrderDAO.getdrugStatistics(drugId, conceptId);
	}

	@Override
	public Integer getCurrSoldeDisp(String drugId, String conceptId, String pharmacy, String expiryDate, String lotNo, String nd) {
		return drugOrderDAO.getCurrSoldeDisp(drugId, conceptId, pharmacy, expiryDate, lotNo, nd);
	}

	@Override
	public void savePharmacyInventory(PharmacyInventory pharmacyInventory) {
		drugOrderDAO.savePharmacyInventory(pharmacyInventory);
		
	}

	@Override
	public Collection<PharmacyInventory> getAllPharmacyInventory() {
		return drugOrderDAO.getAllPharmacyInventory();
	}
	public Collection<PharmacyInventory> getAllPharmacyInventoryWithSolde() {
		return drugOrderDAO.getAllPharmacyInventoryWithSolde();
	}
	@Override
	public Collection<DrugProduct> getARVReportByFromTo(String from, String to, String locationId) {
		return drugOrderDAO.getARVReportByFromTo(from, to, locationId);
	}

	@Override
	public List<Integer> getDrugByNextVisitConcept() {
		return drugOrderDAO.getDrugByNextVisitConcept();
	}

	@Override
	public List<DrugProduct> getProductListAboutToExpiry() {
		return drugOrderDAO.getProductListAboutToExpiry();
	}

	@Override
	public Integer getSoldeByFromDrugLocation(String from, String drugId, String conceptId,
			String locationId) {
		return drugOrderDAO.getSoldeByFromDrugLocation(from, drugId, conceptId, locationId);
	}

	@Override
	public Integer getSoldeByToDrugLocation(String to, String drugId, String conceptId,
			String locationId) {
		return drugOrderDAO.getSoldeByToDrugLocation(to, drugId, conceptId, locationId);
	}

@Override
	public Integer getSoldeByDrugOrConcept(String drugId, String conceptId){
		return drugOrderDAO.getSoldeByDrugOrConcept(drugId, conceptId);
	}


		@Override
	public Object[] getSumEntreeSortieByFromToDrugLocation(String from,
														   String to, String drugId, String conceptId, String locationId) {
		return drugOrderDAO.getSumEntreeSortieByFromToDrugLocation(from, to, drugId, conceptId, locationId);
	}

	@Override
	public Integer getPharmacySoldeFirstDayOfWeek(String from, String drugId, String conceptId,
			String pharmacyId) {
		return drugOrderDAO.getPharmacySoldeFirstDayOfWeek(from, drugId, conceptId, pharmacyId);
	}

	@Override
	public Integer getPharmacySoldeLastDayOfWeek(String to, String drugId, String conceptId,
			String pharmacyId) {
		return drugOrderDAO.getPharmacySoldeLastDayOfWeek(to, drugId, conceptId, pharmacyId);
	}

	@Override
	public Object[] getReceivedDispensedDrugOrConsumable(String from, String to,
														 String drugId, String pharmacyId,String conceptId) {
		return drugOrderDAO.getReceivedDispensedDrugOrConsumable(from, to, drugId, pharmacyId,conceptId);
	}

	@Override
	public Collection<PharmacyInventory> getPharmacyInventoryByFromToLocation(String from, String to, String pharmacyId) {
		return drugOrderDAO.getPharmacyInventoryByFromToLocation(from, to, pharmacyId);
	}

	@Override
	public DrugOrderPrescription getDOPByOrderId(String orderId) {
		return drugOrderDAO.getDOPByOrderId(orderId);
	}

	@Override
	public PharmacyInventory getPIbyDOPId(String dopId) {
		return drugOrderDAO.getPIbyDOPId(dopId);
	}

	@Override
	public Location getLocationByPharmacy(Pharmacy pharmacy) {
		return drugOrderDAO.getLocationByPharmacy(pharmacy);
	}

	@Override
	public List<DrugOrderPrescription> getDOPByPatientId(Patient patient) {
		return drugOrderDAO.getDOPByPatientId(patient);
	}

	@Override
	public List<Object[]> getLotNumbersExpirationDates(String drugId, String conceptId, String locationId, String pharmacyId) {
		return drugOrderDAO.getLotNumbersExpirationDates(drugId, conceptId, locationId, pharmacyId);
	}
	
	@Override
	public void saveReturnStock(ProductReturnStore returnedStore) {
		drugOrderDAO.saveReturnStock(returnedStore);		
	}
	
	@Override
	public ProductReturnStore getReturnStockById(int arsId) {
		return drugOrderDAO.getReturnStockById(arsId);
	}

	/*@Override
	public List<ProductReturnStore> getReturnStockByDate(Date date) {
		return drugOrderDAO.getReturnStockByDate(date);
	}*/
	@Override
	public List<ProductReturnStore> getReturnStockByDate(Date date, String observation)
	{
		return drugOrderDAO.getReturnStockByDate(date, observation);
	}
	@Override
	public Integer getReturnedItemsByDates(String startDate, String endDate, DrugProduct product, String observation)
	{
		return drugOrderDAO.getReturnedItemsByDates(startDate, endDate, product, observation);
	}

	@Override
	public List<ProductReturnStore> getReturnStockByDP(DrugProduct dp) {
		return drugOrderDAO.getReturnStockByDP(dp);
	}

	@Override
	public void saveOrUpdateConsumableDispense(ConsumableDispense consumableDispense) {
		drugOrderDAO.saveOrUpdateConsumableDispense(consumableDispense);		
	}

	@Override
	public List<ConsumableDispense> getAllConsumableDipsense() {
		return drugOrderDAO.getAllConsumableDipsense();
	}

	@Override
	public List<Object[]> getStoreStatus() {
		return drugOrderDAO.getStoreStatus();
	}

	@Override
	public List<Object[]> getLotNumberByDrugProductId(int drugProductId){
		return drugOrderDAO.getLotNumberByDrugProductId(drugProductId);
	}

	@Override
	public Boolean checkIfOneDrugOrConsummableUseOneLotNo(String drugId, String conceptId, String lotNo) {
		return drugOrderDAO.checkIfOneDrugOrConsummableUseOneLotNo(drugId, conceptId, lotNo);
	}

	@Override
	public Collection<DrugProduct> getPharmacyDrugProducts() {
		return drugOrderDAO.getPharmacyDrugProducts();
	}

	@Override
	public Collection<DrugProduct> getPharmacyConsummableProducts() {
		return drugOrderDAO.getPharmacyConsummableProducts();
	}

	@Override
	public void saveOrUpdateConsumableOrder(ConsumableOrder conso) {
			drugOrderDAO.saveOrUpdateConsumableOrder(conso);
	}

	@Override
	public List<ConsumableOrder> getConsumableOrderByDate(String date, Patient patient) {
		return drugOrderDAO.getConsumableOrderByDate(date,patient);
	}
}