package org.openmrs.module.pharmacymanagement.db.hibernate;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.module.pharmacymanagement.*;
import org.openmrs.module.pharmacymanagement.db.DrugOrderDAO;

public class DrugOrderDAOImpl implements DrugOrderDAO {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void cancelProduct(DrugStore product) {
		Session session = getSessionFactory().getCurrentSession();
		session.delete(product);
	}

	public Collection<DrugStore> findProductByLocation(Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<DrugStore> findProductByStoreKeeper(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<DrugStore> findProductsByDate(Date expiryDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public CmdDrug getCmdDrugById(int cmddrugId) {
		Session session = getSessionFactory().getCurrentSession();
		CmdDrug cmdDrug = (CmdDrug) session.get(CmdDrug.class, cmddrugId);
		return cmdDrug;
	}

	@SuppressWarnings("unchecked")
	public Collection<CmdDrug> getOrders() {
		Session session = getSessionFactory().getCurrentSession();
		Collection<CmdDrug> orders = session.createCriteria(CmdDrug.class)
				.list();
		return orders;
	}

	public void updateStore(DrugProduct product) {
		Session session = getSessionFactory().getCurrentSession();
		session.update(product);
	}

	public void saveDrugProduct(DrugProduct drugProduct) {
		if (drugProduct.getStoreQnty() >= 0) {
			log.error("======="+drugProduct.getExpiryDate()+"======"+drugProduct.getReqDate());
			sessionFactory.getCurrentSession().save(drugProduct);
		}
	}

	public void saveCmdDrug(CmdDrug cmdDrug) {
		sessionFactory.getCurrentSession().saveOrUpdate(cmdDrug);
	}

	@Override
	public Set<DrugProduct> getDrugProducts(Pharmacy pharmacy, Drug drug) {
		Criteria c = getSessionFactory().getCurrentSession().createCriteria(PharmacyInventory.class);
		c.createAlias("drugproductId", "drugProduct");
		if (pharmacy != null) {
			c.createAlias("drugProduct.cmddrugId", "cmdDrug");
			c.add(Restrictions.eq("cmdDrug.pharmacy", pharmacy));
		}
		if (drug != null) {
			c.add(Restrictions.eq("drugProduct.drugId", drug));
		}
		c.setProjection(Projections.property("drugproductId"));
		return new HashSet<DrugProduct>(c.list());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DrugProduct> getDrugProductByCmdDrugId(CmdDrug cmddrug) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(DrugProduct.class)
				.createCriteria("cmddrugId").add(
						Restrictions.idEq(cmddrug.getCmddrugId()));
		Collection<DrugProduct> drugproducts = criteria.list();
		return drugproducts;
	}

	@Override
	public DrugProduct getDrugProductById(int drugproductId) {
		Session session = getSessionFactory().getCurrentSession();
		DrugProduct drugProduct = (DrugProduct) session.get(DrugProduct.class,
				drugproductId);
		return drugProduct;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DrugProduct> getAllProducts() {
		Session session = getSessionFactory().getCurrentSession();
		Collection<DrugProduct> products = session.createCriteria(
				DrugProduct.class).list();
		return products;
	}

	@Override
	public Collection<CmdDrug> findDrugOrdersByProg(String prog) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<CmdDrug> findDrugOrdersByMonth(int mois) {
		Session session = getSessionFactory().getCurrentSession();
		Collection<CmdDrug> orders = session.createCriteria(CmdDrug.class).add(
				Restrictions.eq("monthPeriod", mois)).list();
		return orders;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<CmdDrug> findOrdersByLocSupProgMonth(String locationId,
			String supporting, String mois, String pharmacyId, String type) {
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT cd.* FROM " + PharmacyConstants.CMD_DRUG
				+ " cd WHERE 1 = 1 ");

		if (type.equals("DISPENSE")) {
			if (pharmacyId != null && !pharmacyId.equals(""))
				sb.append("AND cd.pharmacy = '" + pharmacyId + "' ");

			sb.append(" AND cd.location_id IS null AND destination = '"
					+ locationId + "' ");
		}

		if (type.equals("STORE")) {
			if (locationId != null && !locationId.equals(""))
				sb.append(" AND (cd.location_id = '" + locationId
						+ "' OR cd.destination = '" + locationId + "' ) ");
			// if (locationId != null && !locationId.equals(""))
			// sb.append(" OR cd.destination = '" + locationId + "' ");
		}

		if (supporting != null && !supporting.equals(""))
			sb.append(" AND cd.supporting_prog = '" + supporting + "' ");

		if (mois != null && !mois.equals(""))
			sb.append(" AND cd.month_period = '" + mois + "' ");

		sb.append(";");

		Session session = sessionFactory.getCurrentSession();
		Collection<CmdDrug> orders = session.createSQLQuery(sb.toString())
				.addEntity("cd", CmdDrug.class).list();

		return orders;
	}

	@Override
	public void saveInventory(DrugProductInventory drugproductInventory) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(drugproductInventory);
	}

	@Override
	public DrugProductInventory getDrugProductInventoryById(int dpiId) {
		Session session = sessionFactory.getCurrentSession();
		DrugProductInventory dpi = (DrugProductInventory) session.get(
				DrugProductInventory.class, dpiId);
		return dpi;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DrugProductInventory> getAllDrugProductInventory() {
		Session session = getSessionFactory().getCurrentSession();
		List<DrugProductInventory> dpi = session.createCriteria(
				DrugProductInventory.class).list();
		return dpi;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getCurrSolde(String drugId, String conceptId,
			String locationId, String expiryDate, String lotNo, String nd) {
		StringBuffer sb = new StringBuffer();
		int solde = 0;

		sb.append("SELECT dpi.solde FROM "
				+ PharmacyConstants.DRUGPRODUCT_INVENTORY + " dpi INNER JOIN "
				+ PharmacyConstants.DRUG_PRODUCT
				+ " dp ON dpi.drugproduct_id = dp.drugproduct_id LEFT JOIN ");

		if (locationId != null && !locationId.equals("")) {
			sb
					.append(PharmacyConstants.CMD_DRUG
							+ " cd ON dp.cmddrug_id = cd.cmddrug_id LEFT JOIN "
							+ PharmacyConstants.PHARMACY
							+ " p ON cd.pharmacy = p.pharmacy_id LEFT JOIN "
							+ PharmacyConstants.ARV_RETURN_STORE
							+ " pars ON dp.drugproduct_id = pars.drugproduct_id WHERE 1 = 1 ");
			sb.append(" AND (cd.location_id = '" + locationId
					+ "' OR p.location_id = '" + locationId
					+ "' OR pars.destination = '" + locationId + "') ");
		}

		if (drugId != null && !drugId.equals(""))
			sb.append(" AND dp.drug_id = '" + drugId + "' ");

		if (conceptId != null && !conceptId.equals(""))
			sb.append(" AND dp.concept_id = '" + conceptId + "' ");

		/*if (expiryDate != null && !expiryDate.equals(""))
			sb.append(" AND dp.expiry_date = '" + expiryDate + "' ");*/

		if (lotNo != null && !lotNo.equals(""))
			sb.append(" AND dp.lot_no = '" + lotNo + "' ");

		sb.append(" ORDER BY dpi.inventory_id DESC LIMIT 1");

		if (nd != null && !nd.equals(""))
			sb.append(", " + nd);

		sb.append(" ;");

		Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		List<Object> list = query.list();
		if (list.size() > 0) {
			solde = Integer.valueOf(list.get(0).toString());
		}

		return solde;
	}

	@Override
	public List<Integer> getDrugsCurrSolde() {
		List<Integer> productIds = new ArrayList<Integer>();
		String queryStr = "select grouped.drugproduct_id from " +
				"(select * from " +
				"(SELECT dpi.drugproduct_id,dp.drug_id,dpi.solde,dp.lot_no FROM pharmacymanagement_drugproduct_inventory dpi " +
				"INNER JOIN pharmacymanagement_drug_product dp ON dpi.drugproduct_id = dp.drugproduct_id " +
				"WHERE 1 = 1 " +
				"ORDER BY dpi.inventory_id DESC) ordered " +
				"where ordered.drug_id is not null " +
				"group by ordered.drug_id,ordered.lot_no) grouped " +
				"where grouped.solde > 0";
		Session session = sessionFactory.getCurrentSession();
		List<Integer> record = session.createSQLQuery(queryStr).list();
		for (Integer obj : record) {
			productIds.add(obj);
		}
		return productIds;
	}

	@Override
	public Map<Integer,Integer> getCurrSoldeOfDrugProducts(List<DrugProduct> drugProducts) {
		Map<Integer,Integer> pdWithSolde = new HashMap<Integer, Integer>();

		StringBuffer drugProductsIds=new StringBuffer();
		int i=0;
		if(drugProducts.size()>0){
			for (DrugProduct dp:drugProducts) {
				if (i==0){
					drugProductsIds.append(dp.getDrugproductId());
					i++;
				}else {
					drugProductsIds.append(","+dp.getDrugproductId());
				}
			}
		}

		String queryStr = "WITH quantities as (SELECT drug_product.drugproduct_id, pharmacy_inventory.solde, RANK() OVER (PARTITION BY drug_product.drug_id, lot_no order by pharmacyinventory_id DESC) ranking from  pharmacymanagement_drug_product drug_product LEFT JOIN pharmacymanagement_pharmacy_inventory  pharmacy_inventory  on pharmacy_inventory.drugproduct_id=drug_product.drugproduct_id where pharmacy_inventory.solde > 0 and lot_no is not null AND drug_product.drugproduct_id in  ("+drugProductsIds+") )SELECT concat(drugproduct_id,',',solde) from quantities where ranking=1";




		/*String queryStr = "select concat(grouped.drugproduct_id,',',grouped.solde) from " +
				"(select * from " +
				"(SELECT dpi.drugproduct_id,dp.drug_id,dpi.solde,dp.lot_no FROM pharmacymanagement_drugproduct_inventory dpi " +
				"INNER JOIN pharmacymanagement_drug_product dp ON dpi.drugproduct_id in ("+drugProductsIds+") " +
				"WHERE 1 = 1 " +
				"ORDER BY dpi.inventory_id DESC) ordered " +
				"where ordered.drug_id is not null " +
				"group by ordered.drug_id,ordered.lot_no) grouped " +
				"where grouped.solde > 0";*/
		Session session = sessionFactory.getCurrentSession();
		List<String> records = session.createSQLQuery(queryStr).list();
		for (String record : records) {
			String[] dpAndSold=record.split(",");
			pdWithSolde.put(Integer.parseInt(dpAndSold[0]),Integer.parseInt(dpAndSold[1]));
		}
		return pdWithSolde;
	}
	@Override
	public List<Integer> getConsummablesCurrSolde() {
		List<Integer> productIds = new ArrayList<Integer>();
		String queryStr = "select grouped.drugproduct_id from " +
				"(select * from " +
				"(SELECT dpi.drugproduct_id,dp.concept_id,dpi.solde,dp.lot_no FROM pharmacymanagement_drugproduct_inventory dpi " +
				"INNER JOIN pharmacymanagement_drug_product dp ON dpi.drugproduct_id = dp.drugproduct_id " +
				"WHERE 1 = 1 " +
				"ORDER BY dpi.inventory_id DESC) ordered " +
				"where ordered.concept_id is not null " +
				"group by ordered.concept_id,ordered.lot_no) grouped " +
				"where grouped.solde > 0";
		Session session = sessionFactory.getCurrentSession();
		List<Integer> record = session.createSQLQuery(queryStr).list();
		for (Integer obj : record) {
			productIds.add(obj);
		}
		return productIds;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DrugProductInventory> getDrugInventoryByDrugId(
			String drugId, String conceptId, String locationId,
			String pharmacyId) {

		StringBuffer sb = new StringBuffer();

		sb.append(" SELECT dpi.* FROM "
				+ PharmacyConstants.DRUGPRODUCT_INVENTORY + " dpi INNER JOIN "
				+ PharmacyConstants.DRUG_PRODUCT
				+ " dp ON dpi.drugproduct_id = dp.drugproduct_id LEFT JOIN "
				+ PharmacyConstants.CMD_DRUG
				+ " cd ON dp.cmddrug_id = cd.cmddrug_id LEFT JOIN "
				+ PharmacyConstants.ARV_RETURN_STORE
				+ " ars ON dp.drugproduct_id = ars.drugproduct_id LEFT JOIN "
				+ PharmacyConstants.PHARMACY
				+ " p ON cd.pharmacy = p.pharmacy_id WHERE 1 = 1 ");

		if (drugId != null && !drugId.equals(""))
			sb.append(" AND dp.drug_id = '" + drugId + "' ");

		if (conceptId != null && !conceptId.equals(""))
			sb.append(" AND dp.concept_id = '" + conceptId + "' ");

		if (locationId != null && !locationId.equals(""))
			sb.append(" AND (cd.location_id = '" + locationId
					+ "' OR p.location_id ='" + locationId
					+ "' OR ars.destination = '" + locationId + "') ");

		sb.append(" ORDER BY dpi.inventory_id;");

		Session session = sessionFactory.getCurrentSession();
		Collection<DrugProductInventory> drupi = session.createSQLQuery(
				sb.toString()).addEntity("dpi", DrugProductInventory.class)
				.list();

		return drupi;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCalculations(int drugId, int isStore) {
		StringBuffer sb = new StringBuffer();

		sb
				.append("SELECT MAX(solde), SUM(entree), SUM(sortie), MIN(solde) FROM "
						+ PharmacyConstants.DRUGPRODUCT_INVENTORY + " dpi ");
		sb.append(" INNER JOIN " + PharmacyConstants.DRUG_PRODUCT
				+ " dp ON dpi.drugproduct_id = dp.drugproduct_id ");
		sb.append(" INNER JOIN drug d on dp.drug_id = d.drug_id ");
		sb.append(" AND dpi.is_store = '" + isStore + "' ");
		sb.append(" and d.drug_id = '" + drugId + "' ");
		sb.append(";");

		Session session = sessionFactory.getCurrentSession();
		List<Object[]> calculations = session.createSQLQuery(sb.toString())
				.list();

		return calculations;
	}

	@Override
	public void savePharmacy(Pharmacy pharmacy) {
		sessionFactory.getCurrentSession().saveOrUpdate(pharmacy);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Pharmacy> getAllPharmacies() {
		Session session = getSessionFactory().getCurrentSession();
		Collection<Pharmacy> pharmacies = session
				.createCriteria(Pharmacy.class).list();
		return pharmacies;
	}

	@Override
	public Pharmacy getPharmacyById(int pharmacyId) {
		Session session = getSessionFactory().getCurrentSession();
		Pharmacy pharmacy = (Pharmacy) session.get(Pharmacy.class, pharmacyId);
		return pharmacy;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pharmacy> getPharmacyByLocation(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Pharmacy.class);
		criteria.add(Restrictions.eq("locationId", location));
		return criteria.list();
	}

	@Override
	public void saveDrugDetails(DrugDetails drugDetail) {
		sessionFactory.getCurrentSession().saveOrUpdate(drugDetail);
	}

	@Override
	public void cancelDrugDetail(DrugDetails drugDetail) {
		sessionFactory.getCurrentSession().delete(drugDetail);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DrugDetails> getAllDrugDetails() {
		Session session = getSessionFactory().getCurrentSession();
		Collection<DrugDetails> drugDetails = session.createCriteria(
				DrugDetails.class).list();
		return drugDetails;
	}

	@Override
	public DrugDetails getDrugDetailsById(int drugDetailId) {
		Session session = getSessionFactory().getCurrentSession();
		DrugDetails drugDetail = (DrugDetails) session.get(DrugDetails.class,
				drugDetailId);
		return drugDetail;
	}

	@Override
	public void cancelPharmacy(Pharmacy pharmacy) {
		sessionFactory.getCurrentSession().delete(pharmacy);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DrugOrderPrescription> getAllDrugOrderPrescription() {
		Session session = getSessionFactory().getCurrentSession();
		Collection<DrugOrderPrescription> drugOrderPrescription = session
				.createCriteria(DrugOrderPrescription.class).list();
		return drugOrderPrescription;
	}

	@Override
	public DrugOrderPrescription getDrugOrderPrescriptionById(
			int drugOrderPrescriptionId) {
		Session session = getSessionFactory().getCurrentSession();
		DrugOrderPrescription drugOrderPrescription = (DrugOrderPrescription) session
				.get(DrugOrderPrescription.class, drugOrderPrescriptionId);
		return drugOrderPrescription;
	}

	@Override
	public void saveDrugOrderPrescription(
			DrugOrderPrescription drugOrderPrescription) {
		sessionFactory.getCurrentSession().saveOrUpdate(drugOrderPrescription);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getdrugStatistics(String drugId, String conceptId) {
		StringBuffer sb = new StringBuffer();

		sb
				.append("SELECT MAX(solde), SUM(entree), SUM(sortie), MIN(solde) FROM "
						+ PharmacyConstants.DRUGPRODUCT_INVENTORY
						+ " pi INNER JOIN "
						+ PharmacyConstants.DRUG_PRODUCT
						+ " dp ON pi.drugproduct_id = dp.drugproduct_id ");

		if (drugId != null && !drugId.equals(""))
			sb.append(" AND dp.drug_id = '" + drugId + "' ");

		if (conceptId != null && !conceptId.equals(""))
			sb.append(" AND dp.concept_id = '" + conceptId + "' ");

		sb.append(";");

		Session session = sessionFactory.getCurrentSession();
		List<Object[]> statistics = session.createSQLQuery(sb.toString())
				.list();

		return statistics;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getCurrSoldeDisp(String drugId, String conceptId,
			String pharmacy, String expiryDate, String lotNo, String nd) {

		StringBuffer sb = new StringBuffer();
		int solde = 0;

		sb
				.append("SELECT pi.solde FROM pharmacymanagement_pharmacy_inventory pi INNER JOIN pharmacymanagement_drug_product dp ON pi.drugproduct_id = dp.drugproduct_id LEFT JOIN pharmacymanagement_cmd_drug cd ON dp.cmddrug_id = cd.cmddrug_id LEFT JOIN pharmacymanagement_arv_return_store pars ON dp.drugproduct_id = pars.drugproduct_id LEFT JOIN pharmacymanagement_pharmacy p ON (cd.pharmacy = p.pharmacy_id  OR pars.origin_pharmacy = p.pharmacy_id) WHERE 1 = 1");

		if (drugId != null && !drugId.equals(""))
			sb.append(" AND	dp.drug_id = '" + drugId + "' ");

		if (conceptId != null && !conceptId.equals(""))
			sb.append(" AND	dp.concept_id = '" + conceptId + "' ");

		if (pharmacy != null && !pharmacy.equals(""))
			sb.append(" AND p.pharmacy_id = '" + pharmacy + "' ");

		if (expiryDate != null && !expiryDate.equals(""))
			sb.append(" AND dp.expiry_date = '" + expiryDate + "' ");

		if (lotNo != null && !lotNo.equals(""))
			sb.append(" AND dp.lot_no = '" + lotNo + "' ");

		sb.append(" ORDER BY pi.pharmacyinventory_id DESC LIMIT 1 ");

		if (nd != null && !nd.equals(""))
			sb.append(", " + nd);

		sb.append(" ;");

		Session session = sessionFactory.getCurrentSession();

		List<Object> dpi = session.createSQLQuery(sb.toString()).list();

		if (dpi.size() > 0) {
			solde = Integer.valueOf(dpi.get(0).toString());
		}

		return solde;
	}

	@Override
	public void savePharmacyInventory(PharmacyInventory pharmacyInventory) {
		sessionFactory.getCurrentSession().saveOrUpdate(pharmacyInventory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<PharmacyInventory> getAllPharmacyInventory() {
		return getSessionFactory().getCurrentSession().createCriteria(
				PharmacyInventory.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<PharmacyInventory> getAllPharmacyInventoryWithSolde() {
		return getSessionFactory().getCurrentSession().createCriteria(
				PharmacyInventory.class).add(Restrictions.gt("solde",0))
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DrugProduct> getARVReportByFromTo(String from, String to,
			String locationId) {
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT dp.* FROM " + PharmacyConstants.DRUGPRODUCT_INVENTORY
				+ " di INNER JOIN " + PharmacyConstants.DRUG_PRODUCT
				+ " dp ON di.drugproduct_id = dp.drugproduct_id INNER JOIN "
				+ PharmacyConstants.CMD_DRUG
				+ " cd ON dp.cmddrug_id = cd.cmddrug_id");

		sb.append(" WHERE 1 = 1 ");

		if (from != null && !from.equals(""))
			sb.append(" AND di.inventory_date >= '" + from + "' ");

		if (to != null && !to.equals(""))
			sb.append(" AND di.inventory_date <= '" + to + "' ");

		if (locationId != null && !locationId.equals(""))
			sb.append(" AND cd.location_id = '" + locationId + "' ");

		Session session = sessionFactory.getCurrentSession();

		Collection<DrugProduct> dpList = session.createSQLQuery(sb.toString())
				.addEntity("dp", DrugProduct.class).list();

		return dpList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getDrugByNextVisitConcept() {
		List<Integer> drugIds = new ArrayList<Integer>();
		String queryStr = "SELECT DISTINCT dp.drug_id FROM obs o INNER JOIN "
				+ PharmacyConstants.DRUG_ORDER_PRESCRIPTION
				+ " dop ON o.person_id = dop.patient INNER JOIN "
				+ PharmacyConstants.DRUG_PRODUCT
				+ " dp on dop.drugproduct_id = dp.drugproduct_id WHERE o.concept_id = '"
				+ PharmacyConstants.RETURN_VISIT_DATE + "';";
		Session session = sessionFactory.getCurrentSession();
		List<Integer> record = session.createSQLQuery(queryStr).list();
		for (Integer obj : record) {
			drugIds.add(obj);
		}
		return drugIds;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DrugProduct> getProductListAboutToExpiry() {
		String queryStr = "SELECT dp.* FROM "
				+ PharmacyConstants.DRUG_PRODUCT
				+ " dp WHERE dp.expiry_date <= date_add(curdate(), interval 2 month) AND dp.expiry_date >= date_add(curdate(), interval 0 day) GROUP BY dp.lot_no;";
		Session session = sessionFactory.getCurrentSession();
		List<DrugProduct> dpList = session.createSQLQuery(queryStr).addEntity(
				"dp", DrugProduct.class).list();

		return dpList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getSoldeByFromDrugLocation(String from, String drugId,
			String conceptId, String locationId) {
		StringBuffer sb = new StringBuffer();
		int solde = 0;

		sb.append(" SELECT dpi.solde FROM "
				+ PharmacyConstants.DRUGPRODUCT_INVENTORY + " dpi INNER JOIN "
				+ PharmacyConstants.DRUG_PRODUCT
				+ " dp ON dpi.drugproduct_id = dp.drugproduct_id INNER JOIN "
				+ PharmacyConstants.CMD_DRUG
				+ " cd ON dp.cmddrug_id = cd.cmddrug_id LEFT JOIN "
				+ PharmacyConstants.PHARMACY
				+ " p ON cd.pharmacy = p.pharmacy_id WHERE 1 = 1 ");

		if (from != null && !from.equals(""))
			sb.append(" AND dpi.inventory_date <= '" + from + "' ");

		if (drugId != null && !drugId.equals(""))
			sb.append(" AND dp.drug_id = '" + drugId + "' ");

		if (conceptId != null && !conceptId.equals(""))
			sb.append(" AND dp.concept_id = '" + conceptId + "' ");

		if (locationId != null && !locationId.equals(""))
			sb.append(" AND (cd.location_id = '" + locationId
					+ "' OR p.location_id = '" + locationId + "') ");

		sb.append(" ORDER BY dpi.inventory_id DESC LIMIT 1 ;");

		Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		List<Object> list = query.list();
		if (list.size() > 0) {
			solde = Integer.valueOf(list.get(0).toString());
		}

		return solde;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getSoldeByToDrugLocation(String to, String drugId,
			String conceptId, String locationId) {

		StringBuffer sb = new StringBuffer();
		StringBuffer sbPharmInv = new StringBuffer();

		int solde = 0;

		sb.append("SELECT dpi.solde FROM "
				+ PharmacyConstants.DRUGPRODUCT_INVENTORY + " dpi INNER JOIN "
				+ PharmacyConstants.DRUG_PRODUCT
				+ " dp ON dpi.drugproduct_id = dp.drugproduct_id INNER JOIN "
				+ PharmacyConstants.CMD_DRUG
				+ " cd ON dp.cmddrug_id = cd.cmddrug_id LEFT JOIN "
				+ PharmacyConstants.PHARMACY
				+ " p ON cd.pharmacy = p.pharmacy_id WHERE 1 = 1 ");

		if (to != null && !to.equals(""))
			sb.append(" AND dpi.inventory_date <= '" + to + "' ");

		if (drugId != null && !drugId.equals(""))
			sb.append(" AND dp.drug_id = '" + drugId + "' ");

		if (conceptId != null && !conceptId.equals(""))
			sb.append(" AND dp.concept_id = '" + conceptId + "' ");

		if (locationId != null && !locationId.equals(""))
			sb.append(" AND (cd.location_id = '" + locationId
					+ "' OR p.location_id = '" + locationId
					+ "' ) ORDER BY dpi.inventory_id DESC LIMIT 1 ; ");




		sbPharmInv.append("SELECT sum(dpi.solde) FROM (select * from (select * from "+PharmacyConstants.PHARMACY_INVENTORY+" order by pharmacyinventory_id desc) pi group by drugproduct_id) dpi INNER JOIN "
				+ PharmacyConstants.DRUG_PRODUCT
				+ " dp ON dpi.drugproduct_id = dp.drugproduct_id INNER JOIN "
				+ PharmacyConstants.CMD_DRUG
				+ " cd ON dp.cmddrug_id = cd.cmddrug_id LEFT JOIN "
				+ PharmacyConstants.PHARMACY
				+ " p ON cd.pharmacy = p.pharmacy_id WHERE 1 = 1 ");

		if (drugId != null && !drugId.equals(""))
			sbPharmInv.append(" AND dp.drug_id = '" + drugId + "' ");

		if (conceptId != null && !conceptId.equals(""))
			sbPharmInv.append(" AND dp.concept_id = '" + conceptId + "' ");

		if (locationId != null && !locationId.equals(""))
			sbPharmInv.append(" AND (cd.location_id = '" + locationId
					+ "' OR p.location_id = '" + locationId
					+ "' ) ; ");



		Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		Query queryPharmInv = session.createSQLQuery(sbPharmInv.toString());


		List<Object> list = query.list();
		List<Object> listPharmInv = queryPharmInv.list();

		if (list.size() > 0) {
			solde = Integer.valueOf(list.get(0).toString());
		}
		if (listPharmInv.size() > 0 && listPharmInv.get(0)!=null) {
			solde += Integer.valueOf(listPharmInv.get(0).toString());
		}
		return solde;
	}



	@Override
	public Integer getSoldeByDrugOrConcept(String drugId, String conceptId) {

		StringBuffer sb = new StringBuffer();
		StringBuffer sbAdjustment = new StringBuffer();

		StringBuffer sbPharmInv = new StringBuffer();


		int solde = 0;

		if (drugId != null && !drugId.equals("")) {


			sb.append("SELECT (SUM(dpi.entree) - SUM(dpi.sortie)) FROM "
					+ PharmacyConstants.DRUGPRODUCT_INVENTORY + " dpi INNER JOIN "
					+ PharmacyConstants.DRUG_PRODUCT
					+ " dp ON dpi.drugproduct_id=dp.drugproduct_id INNER JOIN "
					+ PharmacyConstants.CMD_DRUG
					+ " cd ON dp.cmddrug_id = cd.cmddrug_id LEFT JOIN "
					+ PharmacyConstants.PHARMACY
					+ " p ON cd.pharmacy = p.pharmacy_id ");

			sb.append(" WHERE 1 = 1 ");

			if (drugId != null && !drugId.equals(""))
				sb.append(" AND dp.drug_id = '" + drugId + "' ");

			if (conceptId != null && !conceptId.equals(""))
				sb.append(" AND dp.concept_id = '" + conceptId + "' ");



			//sb.append("SELECT sum(dp.deliv_qnty) FROM pharmacymanagement_drug_product dp,pharmacymanagement_cmd_drug cmd where 1 = 1 and cmd.cmddrug_id=dp.cmddrug_id and dp.is_deliv=1 and dp.drug_id=" + drugId + " and cmd.pharmacy is null;");

		/*	//sbAdjustment.append("SELECT sum(dp.deliv_qnty) FROM pharmacymanagement_drug_product dp,pharmacymanagement_cmd_drug cmd where 1 = 1 and cmd.cmddrug_id=dp.cmddrug_id and dp.is_deliv=1 and dp.drug_id=" + drugId + " and dp.transferType like '%adjustment%' and cmd.pharmacy is null;");
*/


			sbPharmInv.append("SELECT (sum(pi.entree)-sum(pi.sortie)) FROM pharmacymanagement_pharmacy_inventory pi INNER JOIN pharmacymanagement_drug_product dp ON pi.drugproduct_id = dp.drugproduct_id INNER JOIN pharmacymanagement_cmd_drug cd ON dp.cmddrug_id = cd.cmddrug_id ");

			if (drugId != null && !drugId.equals(""))
				sbPharmInv.append(" AND dp.drug_id = '" + drugId + "' ");

			if (conceptId != null && !conceptId.equals(""))
				sbPharmInv.append(" AND dp.concept_id = '" + conceptId + "' ");
			sbPharmInv.append(" ORDER BY pi.pharmacyinventory_id");

			//sbPharmInv.append("SELECT sum(dpr.quantity) FROM pharmacymanagement_drug_product dp,pharmacymanagement_drug_order_prescription dpr where 1 = 1 and dp.drugproduct_id=dpr.drugproduct_id and dp.drug_id=" + drugId + ";");
		}

		if ((drugId == null || drugId.equals("")) && (conceptId != null && !conceptId.equals("")) ) {



			sb.append("SELECT (SUM(dpi.entree) - SUM(dpi.sortie)) FROM "
					+ PharmacyConstants.DRUGPRODUCT_INVENTORY + " dpi INNER JOIN "
					+ PharmacyConstants.DRUG_PRODUCT
					+ " dp ON dpi.drugproduct_id=dp.drugproduct_id INNER JOIN "
					+ PharmacyConstants.CMD_DRUG
					+ " cd ON dp.cmddrug_id = cd.cmddrug_id LEFT JOIN "
					+ PharmacyConstants.PHARMACY
					+ " p ON cd.pharmacy = p.pharmacy_id ");

			sb.append(" WHERE 1 = 1 ");

			if (drugId != null && !drugId.equals(""))
				sb.append(" AND dp.drug_id = '" + drugId + "' ");

			if (conceptId != null && !conceptId.equals(""))
				sb.append(" AND dp.concept_id = '" + conceptId + "' ");




			//sb.append("SELECT sum(dp.deliv_qnty) FROM pharmacymanagement_drug_product dp,pharmacymanagement_cmd_drug cmd where 1 = 1 and cmd.cmddrug_id=dp.cmddrug_id and dp.is_deliv=1 and dp.concept_id=" + conceptId + " and cmd.pharmacy is null;");
/*

			//sbAdjustment.append("SELECT sum(dp.deliv_qnty) FROM pharmacymanagement_drug_product dp,pharmacymanagement_cmd_drug cmd where 1 = 1 and cmd.cmddrug_id=dp.cmddrug_id and dp.is_deliv=1 and dp.concept_id=" + conceptId + " and dp.transferType like '%adjustment%' and cmd.pharmacy is null;");

*/


			sbPharmInv.append("SELECT (sum(pi.entree)-sum(pi.sortie)) FROM pharmacymanagement_pharmacy_inventory pi INNER JOIN pharmacymanagement_drug_product dp ON pi.drugproduct_id = dp.drugproduct_id INNER JOIN pharmacymanagement_cmd_drug cd ON dp.cmddrug_id = cd.cmddrug_id ");


			if (drugId != null && !drugId.equals(""))
				sbPharmInv.append(" AND dp.drug_id = '" + drugId + "' ");

			if (conceptId != null && !conceptId.equals(""))
				sbPharmInv.append(" AND dp.concept_id = '" + conceptId + "' ");
			sbPharmInv.append(" ORDER BY pi.pharmacyinventory_id");


			//sbPharmInv.append("SELECT sum(dpr.quantity) FROM pharmacymanagement_drug_product dp,pharmacymanagement_drug_order_prescription dpr where 1 = 1 and dp.drugproduct_id=dpr.drugproduct_id and dp.concept_id=" + conceptId + ";");
		}

		Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		Query queryPharmInv = session.createSQLQuery(sbPharmInv.toString());


		List<Object> list = query.list();
		List<Object> listPharmInv = queryPharmInv.list();
		//List<Object> listAdjustment = (session.createSQLQuery(sbAdjustment.toString())).list();
        System.out.println("Stockkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk: "+list.get(0).toString());
		System.out.println("Pharmacyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy: "+listPharmInv.get(0).toString());

		if (list.size() > 0) {
			solde = Integer.valueOf(list.get(0).toString());
		}
		if (listPharmInv.size() > 0 && listPharmInv.get(0)!=null) {
			//solde -= Integer.valueOf(listPharmInv.get(0).toString());
			solde += Integer.valueOf(listPharmInv.get(0).toString());

		}
		/*if (listAdjustment.size() > 0 && listAdjustment.get(0)!=null) {
			solde = Integer.valueOf(listAdjustment.get(0).toString());
		}*/
		return solde;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getSumEntreeSortieByFromToDrugLocation(String from,
			String to, String drugId, String conceptId, String locationId) {
		StringBuffer sb = new StringBuffer();
		Object[] obj = new Object[2];
		sb.append("SELECT SUM(dpi.entree) , SUM(dpi.sortie) FROM "
				+ PharmacyConstants.DRUGPRODUCT_INVENTORY + " dpi INNER JOIN "
				+ PharmacyConstants.DRUG_PRODUCT
				+ " dp ON dpi.drugproduct_id=dp.drugproduct_id INNER JOIN "
				+ PharmacyConstants.CMD_DRUG
				+ " cd ON dp.cmddrug_id = cd.cmddrug_id LEFT JOIN "
				+ PharmacyConstants.PHARMACY
				+ " p ON cd.pharmacy = p.pharmacy_id ");

		sb.append(" WHERE 1 = 1 ");

		/*
		 * if (from != null && !from.equals("")) sb.append(" AND dpi.inventory_date > '"
		 * + from + "' ");
		 * 
		 * if (to != null && !to.equals("")) sb.append(" AND dpi.inventory_date < '" +
		 * to + "' ");
		 */

		if (drugId != null && !drugId.equals(""))
			sb.append(" AND dp.drug_id = '" + drugId + "' ");

		if (conceptId != null && !conceptId.equals(""))
			sb.append(" AND dp.concept_id = '" + conceptId + "' ");

		if (locationId != null && !locationId.equals(""))
			sb.append(" AND (cd.location_id = '" + locationId
					+ "' OR p.location_id = '" + locationId + "') ; ");

		Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		List<Object[]> list = query.list();

		for (Object[] object : list) {
			if (query.list().size() > 0) {
				obj[0] = object[0];
				obj[1] = object[1];
			}
		}

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getPharmacySoldeFirstDayOfWeek(String from, String drugId,
			String conceptId, String pharmacyId) {

		StringBuffer sb = new StringBuffer();
		int solde = 0;

		sb.append(" SELECT pi.solde FROM "
				+ PharmacyConstants.PHARMACY_INVENTORY + " pi INNER JOIN "
				+ PharmacyConstants.DRUG_PRODUCT
				+ " dp ON pi.drugproduct_id = dp.drugproduct_id INNER JOIN "
				+ PharmacyConstants.CMD_DRUG
				+ " cd ON dp.cmddrug_id = cd.cmddrug_id WHERE 1 = 1 ");

		if (from != null && !from.equals(""))
			sb.append(" AND pi.date <= '" + from + "' ");

		if (drugId != null && !drugId.equals(""))
			sb.append(" AND dp.drug_id = '" + drugId + "' ");

		if (conceptId != null && !conceptId.equals(""))
			sb.append(" AND dp.concept_id = '" + conceptId + "' ");

		if (pharmacyId != null && !pharmacyId.equals(""))
			sb.append(" AND cd.pharmacy = '" + pharmacyId + "' ");

		sb.append(" ORDER BY pi.pharmacyinventory_id DESC LIMIT 1 ;");

		Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		List<Object> list = query.list();

		if (list.size() > 0) {
			solde = Integer.valueOf(list.get(0).toString());
		}

		return solde;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getPharmacySoldeLastDayOfWeek(String to, String drugId,
			String conceptId, String pharmacyId) {
		StringBuffer sb = new StringBuffer();
		int solde = 0;

		sb.append(" SELECT sum(pi.entree)-sum(pi.sortie) FROM "
				+ PharmacyConstants.PHARMACY_INVENTORY + " pi INNER JOIN "
				+ PharmacyConstants.DRUG_PRODUCT
				+ " dp ON pi.drugproduct_id = dp.drugproduct_id INNER JOIN "
				+ PharmacyConstants.CMD_DRUG
				+ " cd ON dp.cmddrug_id = cd.cmddrug_id WHERE 1 = 1 ");

		if (to != null && !to.equals(""))
			sb.append(" AND pi.date <= '" + to + "' ");

		if (drugId != null && !drugId.equals(""))
			sb.append(" AND dp.drug_id = '" + drugId + "' ");

		if (conceptId != null && !conceptId.equals(""))
			sb.append(" AND dp.concept_id = '" + conceptId + "' ");

		if (pharmacyId != null && !pharmacyId.equals(""))
			sb.append(" AND cd.pharmacy = '" + pharmacyId + "' ");

		//sb.append(" ORDER BY pi.pharmacyinventory_id DESC LIMIT 1 ;");
		sb.append(" ORDER BY pi.pharmacyinventory_id;");
		/*if (drugId != null && !drugId.equals(""))
			sb.append(" group by dp.drug_id");

		if (conceptId != null && !conceptId.equals(""))
			sb.append(" group by dp.concept_id");
*/
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		List<Object> list = query.list();
		if (list.size() > 0) {
			solde = Integer.valueOf(list.get(0).toString());
		}

		return solde;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getReceivedDispensedDrugOrConsumable(String from, String to,
			String drugId, String pharmacyId,String conceptId) {
		StringBuffer sb = new StringBuffer();
		Object[] obj = new Object[2];
		sb.append("SELECT SUM(pi.entree), SUM(pi.sortie) FROM "
				+ PharmacyConstants.PHARMACY_INVENTORY + " pi INNER JOIN "
				+ PharmacyConstants.DRUG_PRODUCT
				+ " dp ON pi.drugproduct_id = dp.drugproduct_id INNER JOIN "
				+ PharmacyConstants.CMD_DRUG
				+ " cd ON dp.cmddrug_id = cd.cmddrug_id WHERE 1 = 1 ");

		if (from != null && !from.equals(""))
			sb.append(" AND pi.date >= '" + from + "' ");

		if (to != null && !to.equals(""))
			sb.append(" AND pi.date <= '" + to + "' ");

		if (drugId != null && !drugId.equals(""))
			sb.append(" AND dp.drug_id = '" + drugId + "' ");

		if (conceptId != null && !conceptId.equals(""))
			sb.append(" AND dp.concept_id = '" + conceptId + "' ");

		if (pharmacyId != null && !pharmacyId.equals(""))
			sb.append(" AND cd.pharmacy IN ('" + pharmacyId + "') ; ");

		Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		List<Object[]> list = query.list();

		for (Object[] object : list) {
			if (query.list().size() > 0) {
				obj[0] = object[0];
				obj[1] = object[1];
			}
		}

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<PharmacyInventory> getPharmacyInventoryByFromToLocation(
			String from, String to, String pharmacyId) {
		StringBuffer sb = new StringBuffer();

		sb.append(" select * from (SELECT pi.* FROM " + PharmacyConstants.PHARMACY_INVENTORY
				+ " pi INNER JOIN " + PharmacyConstants.DRUG_PRODUCT
				+ " dp ON pi.drugproduct_id = dp.drugproduct_id INNER JOIN "
				+ PharmacyConstants.CMD_DRUG
				+ " cd ON dp.cmddrug_id = cd.cmddrug_id WHERE 1 = 1 ");

		if (from != null && !from.equals(""))
			sb.append(" AND pi.date >= '" + from + "' ");

		if (to != null && !to.equals(""))
			sb.append(" AND pi.date <= '" + to + "' ");

		/*if (pharmacyId != null && !pharmacyId.equals(""))
			sb
					.append(" AND cd.pharmacy = '"
							+ pharmacyId
							+ "' AND (pi.dop_id IS NOT NULL or pi.cp_id is not null) ORDER BY pi.pharmacyinventory_id desc) pio group by pio.drugproduct_id ");
*/

		if (pharmacyId != null && !pharmacyId.equals("")) {
			sb
					.append(" AND cd.pharmacy = '"
							+ pharmacyId
							+ "' ORDER BY pi.pharmacyinventory_id desc) pio group by pio.drugproduct_id ");

		}else{
			sb
					.append(" ORDER BY pi.pharmacyinventory_id desc) pio group by pio.drugproduct_id ");
		}

		System.out.println(sb.toString());

		Session session = sessionFactory.getCurrentSession();

		List<PharmacyInventory> pi = session.createSQLQuery(sb.toString())
				.addEntity("pi", PharmacyInventory.class).list();

		return pi;
	}

	public DrugOrderPrescription getDOPByOrderId(String orderId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(DrugOrderPrescription.class)
				.createCriteria("orderId").add(
						Restrictions.idEq(Integer.valueOf(orderId)));

		DrugOrderPrescription dopur = (DrugOrderPrescription) criteria
				.uniqueResult();
		return dopur;
	}

	public PharmacyInventory getPIbyDOPId(String dopId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(PharmacyInventory.class)
				.createCriteria("dopId").add(
						Restrictions.idEq(Integer.valueOf(dopId)));

		PharmacyInventory phi = (PharmacyInventory) criteria.uniqueResult();
		return phi;
	}

	@Override
	public Location getLocationByPharmacy(Pharmacy pharmacy) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Location.class)
				.createCriteria("pharmacyId").add(
						Restrictions.idEq(Integer.valueOf(pharmacy
								.getPharmacyId())));

		Location location = (Location) criteria.uniqueResult();
		return location;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DrugOrderPrescription> getDOPByPatientId(Patient patient) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				DrugOrderPrescription.class);
		criteria.add(Restrictions.eq("patient", patient));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getLotNumbersExpirationDates(String drugId,
			String conceptId, String locationId, String pharmacyLocationId) {
		StringBuffer sb = new StringBuffer();

		sb
				.append(" SELECT DISTINCT dp.lot_no, dp.expiry_date, dp.drugproduct_id FROM pharmacymanagement_drug_product dp LEFT JOIN "
						+ "(SELECT cd.cmddrug_id from pharmacymanagement_cmd_drug cd  WHERE ");

		if (locationId != null && !locationId.equals(""))
			sb.append(" cd.location_id = '" + locationId + "' ");
		else
			sb.append(" cd.pharmacy = '" + pharmacyLocationId
					+ "' AND cd.pharmacy IS NOT NULL ");

		sb
				.append(" GROUP BY cd.cmddrug_id) ab ON dp.cmddrug_id = ab.cmddrug_id LEFT JOIN (SELECT ars.drugproduct_id FROM pharmacymanagement_arv_return_store ars ");

		if (locationId != null && !locationId.equals(""))
			sb.append(" WHERE ars.destination = '" + locationId + "' ");

		sb.append(") ac ON dp.drugproduct_id = ac.drugproduct_id WHERE ");

		if (drugId != null && !drugId.equals(""))
			sb.append(" dp.drug_id = '" + drugId + "' ");
		else
			sb.append(" dp.concept_id = '" + conceptId + "' ");

		sb
				.append(" AND dp.lot_no IS NOT NULL AND dp.cmddrug_id IS NOT NULL GROUP BY dp.lot_no; ");

		Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		List<Object[]> list = query.list();

		return list;
	}

	@Override
	public void saveReturnStock(ProductReturnStore returnedStore) {
		sessionFactory.getCurrentSession().saveOrUpdate(returnedStore);
	}

	@Override
	public ProductReturnStore getReturnStockById(int arsId) {
		Session session = sessionFactory.getCurrentSession();
		ProductReturnStore ars = (ProductReturnStore) session.get(
				ProductReturnStore.class, arsId);
		return ars;
	}

	@SuppressWarnings("unchecked")
	@Override
	/*public List<ProductReturnStore> getReturnStockByDate(Date date) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProductReturnStore.class);
		criteria.add(Restrictions.eq("retDate", date));
		return criteria.list();
	}*/
	public List<ProductReturnStore> getReturnStockByDate(Date date, String observation)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProductReturnStore.class);
		criteria.add(Restrictions.eq("retDate", date));
		criteria.add(Restrictions.eq("observation", observation));
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReturnStore> getReturnStockByDP(DrugProduct dp) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProductReturnStore.class);
		criteria.add(Restrictions.eq("drugproductId", dp));
		return criteria.list();
	}

	@Override
	public void saveOrUpdateConsumableDispense(
			ConsumableDispense consumableDispense) {
		sessionFactory.getCurrentSession().saveOrUpdate(consumableDispense);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumableDispense> getAllConsumableDipsense() {
		Session session = getSessionFactory().getCurrentSession();
		List<ConsumableDispense> consumableDispenseList = session
				.createCriteria(ConsumableDispense.class).list();
		return consumableDispenseList;
	}
	public Integer getReturnedItemsByDates(String startDate, String endDate, DrugProduct product, String observation)
	{
		StringBuffer sb = new StringBuffer();
		int sum = 0;

		sb.append("SELECT SUM(ars.ret_qnty) FROM pharmacymanagement_arv_return_store ars INNER JOIN pharmacymanagement_drug_product dp ON ars.drugproduct_id = dp.drugproduct_id  WHERE 1 = 1 ");


		if ((startDate != null) && (!startDate.equals(""))) {
			sb.append(" AND ars.ret_date >= '" + startDate + "' ");
		}
		if ((endDate != null) && (!endDate.equals(""))) {
			sb.append(" AND ars.ret_date <= '" + endDate + "' ");
		}
		if (product.getDrugId() != null) {
			sb.append(" AND dp.drug_id = '" + product.getDrugId().getDrugId() + "' ");
		}
		if (product.getConceptId() != null) {
			sb.append(" AND dp.concept_id = '" + product.getConceptId().getConceptId() + "' ");
		}
		if ((observation != null) && (!observation.equals(""))) {
			sb.append(" AND ars.observation = '" + observation + "' ");
		}
		sb.append(";");

		org.hibernate.Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		List<Object> list = query.list();
		if ((list.size() > 0) && (list.get(0) != null)) {
			sum = Integer.valueOf(list.get(0).toString()).intValue();
		}

		return Integer.valueOf(sum);
	}

	@Override
	public List<Object[]> getStoreStatus() {

		StringBuffer sb = new StringBuffer();

		//Before ordering
		/*sb.append("select dpdpi.lot_no,sum(dpdpi.entree) as entree,sum(dpdpi.sortie) as sortie,(sum(dpdpi.entree)-sum(dpdpi.sortie)) as solde,dpdpi.expiry_date,dpdpi.drug_id,dpdpi.concept_id from (select dp.drugproduct_id,dpi.entree,dpi.sortie,dp.lot_no,dp.expiry_date,dp.drug_id,dp.concept_id from pharmacymanagement_drug_product dp " +
				"inner join pharmacymanagement_drugproduct_inventory dpi on dp.drugproduct_id=dpi.drugproduct_id) dpdpi group by dpdpi.lot_no,dpdpi.drug_id,dpdpi.concept_id");
*/
		//After ordering
		sb.append("select dpdpi.lot_no,sum(dpdpi.entree) as entree,sum(dpdpi.sortie) as sortie,(sum(dpdpi.entree)-sum(dpdpi.sortie)) as solde,dpdpi.expiry_date,dpdpi.drug_id,dpdpi.concept_id,cn.name as cName,d.name from (select dp.drugproduct_id,dpi.entree,dpi.sortie,dp.lot_no,dp.expiry_date,dp.drug_id,dp.concept_id from pharmacymanagement_drug_product dp " +
				"inner join pharmacymanagement_drugproduct_inventory dpi on dp.drugproduct_id=dpi.drugproduct_id  where dp.lot_no is not null and dp.expiry_date is not null) dpdpi " +
				"left join concept_name cn on cn.concept_id=dpdpi.concept_id  and cn.locale='en' and concept_name_type='FULLY_SPECIFIED'" +
				"left join drug d on d.drug_id=d.dpdpi.drug_id " +
				"group by dpdpi.lot_no,dpdpi.drug_id,dpdpi.concept_id order by cn.name,d.name");


		Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		List<Object[]> list = query.list();

		return list;
	}

    @Override
    public List<Object[]> getLotNumberByDrugProductId(int drugProductId) {
        StringBuffer sb = new StringBuffer();

        sb.append(" SELECT DISTINCT dp.lot_no, dp.expiry_date, dp.drugproduct_id FROM pharmacymanagement_drug_product dp WHERE ");

            sb.append(" dp.drugproduct_id = '" + drugProductId + "' ");

        Session session = sessionFactory.getCurrentSession();

        Query query = session.createSQLQuery(sb.toString());

        List<Object[]> list = query.list();

        return list;    }

	@Override
	public Boolean checkIfOneDrugOrConsummableUseOneLotNo(String drugId, String conceptId, String lotNo) {
		StringBuffer sb = new StringBuffer();

		sb.append("select * from pharmacymanagement_drug_product where lot_no='"+lotNo+"' ");

		if(drugId!=null && !drugId.equals("")){
			sb.append(" and drug_id!= '" + drugId + "' ");

		}
		if(conceptId!=null && !conceptId.equals("")){
			sb.append(" and drug_id!= '" + conceptId + "' ");

		}
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createSQLQuery(sb.toString());

		List<Object[]> list = query.list();

		if(list.size()>0){
			return false;
		}
		return true;
	}

	@Override
	public Collection<DrugProduct> getPharmacyDrugProducts() {
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT dp.* FROM pharmacymanagement_drug_product dp " +
				"INNER JOIN pharmacymanagement_pharmacy_inventory pi ON dp.drugproduct_id = pi.drugproduct_id " +
				"where dp.drug_id is not null and lot_no is not null");

		Session session = sessionFactory.getCurrentSession();

		Collection<DrugProduct> dpList = session.createSQLQuery(sb.toString())
				.addEntity("dp", DrugProduct.class).list();

		return dpList;
	}
	@Override
	public Collection<DrugProduct> getPharmacyConsummableProducts() {
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT dp.* FROM pharmacymanagement_drug_product dp " +
				"INNER JOIN pharmacymanagement_pharmacy_inventory pi ON dp.drugproduct_id = pi.drugproduct_id " +
				"where dp.concept_id is not null and lot_no is not null");

		Session session = sessionFactory.getCurrentSession();

		Collection<DrugProduct> dpList = session.createSQLQuery(sb.toString())
				.addEntity("dp", DrugProduct.class).list();

		return dpList;
	}

	@Override
	public void saveOrUpdateConsumableOrder(ConsumableOrder conso) {
		sessionFactory.getCurrentSession().saveOrUpdate(conso);
	}

	@Override
	public List<ConsumableOrder> getConsumableOrderByDate(String date,Patient patient) {

		StringBuffer sb = new StringBuffer();

		sb.append("SELECT * FROM pharmacymanagement_consumable_order where date='"+date+"' and patientId="+patient.getPatientId());

		Session session = sessionFactory.getCurrentSession();

		List<ConsumableOrder> consumableOrders = session.createSQLQuery(sb.toString())
				.addEntity("dp", ConsumableOrder.class).list();

		return consumableOrders;

/*
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ConsumableOrder.class);
		criteria.add(Restrictions.eq("date", date));
		//criteria.add(Restrictions.eq("patientId", patient));
		return criteria.list();*/
	}


	@Override
	public ConsumableOrder getConsumableOrderId(int consumableOrderId) {
		Session session = getSessionFactory().getCurrentSession();
		ConsumableOrder consumableOrder = (ConsumableOrder) session.get(ConsumableOrder.class,
				consumableOrderId);
		return consumableOrder;
	}

}
