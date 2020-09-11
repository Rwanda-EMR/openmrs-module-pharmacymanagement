package org.openmrs.module.pharmacymanagement.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.EncounterRole;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohappointment.model.Appointment;
import org.openmrs.module.mohappointment.model.AppointmentState;
import org.openmrs.module.mohappointment.model.Services;
import org.openmrs.module.mohappointment.utils.AppointmentUtil;
import org.openmrs.module.pharmacymanagement.ConsumableDispense;
import org.openmrs.module.pharmacymanagement.DrugOrderPrescription;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.DrugProductInventory;
import org.openmrs.module.pharmacymanagement.PharmacyInventory;
import org.openmrs.module.pharmacymanagement.ProductReturnStore;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;

public class Utils
{
	private org.apache.commons.logging.Log log = LogFactory.getLog(getClass());



	public Utils() {}



	public static Integer biggestObsIdNubmer(List<Obs> numbers)
	{
		int tmp = 0;int big = 0;
		for (Obs obs : numbers) {
			if (tmp < obs.getObsId().intValue()) {
				big = obs.getObsId().intValue();
				tmp = big;
			} else {
				big = tmp;
				tmp = big;
			}
		}
		return Integer.valueOf(big);
	}

	public static Obs createObservation(Date obsDatetime, Location loc, Person p, Concept c, Object obsValue, int obsValueType)
	{
		Obs o = new Obs();
		try
		{
			o.setDateCreated(new Date());
			o.setObsDatetime(obsDatetime);
			o.setLocation(loc);
			o.setPerson(p);
			o.setConcept(c);

			if (obsValueType == 1) {
				o.setValueNumeric((Double)obsValue);
			} else if (obsValueType == 2) {
				o.setValueDatetime((Date)obsValue);
			} else if (obsValueType == 3) {
				o.setValueText(""+obsValue);
			} else if (obsValueType == 4) {
				o.setValueCoded((Concept)obsValue);
			}
		}
		catch (Exception e) {
			System.out.println("An Error occured when trying to create an observation :\n");
			e.printStackTrace();
			o = null;
		}
		return o;
	}

	public static Encounter createEncounter(Date encounterDate, Location location, Patient patient, EncounterType encounterType, List<Obs> obsList)
	{
		Encounter enc = new Encounter();
		try
		{
			enc.setDateCreated(new Date());
			enc.setEncounterDatetime(encounterDate);
			
			EncounterRole encounterRole = Context.getEncounterService()
					.getEncounterRoleByUuid(EncounterRole.UNKNOWN_ENCOUNTER_ROLE_UUID);

			enc.setProvider(encounterRole, getProvider());
			enc.setLocation(location);
			enc.setPatient(patient);
			enc.setEncounterType(encounterType);

			for (Obs o : obsList) {
				if (o != null) {
					enc.addObs(o);
				}
				else {
					System.out.println("An observation has not been saved because it was null.");
				}
			}
		} catch (Exception e) {
			System.out.println("An Error occured when trying to create an encounter :\n");
			e.printStackTrace();
			enc = null;
		}
		return enc;
	}

	private static Provider getProvider() {
		//Assuming that the logged in user is associated with a provider account.
		Person person = Context.getAuthenticatedUser().getPerson();
		return Context.getProviderService().getProvidersByPerson(person).iterator().next();
	}
	
	public static int getGP(String id)
	{
		return Integer.valueOf(Context.getAdministrationService()
				.getGlobalProperty(id)).intValue();
	}

	public static String[] getPossibleFrequencyFromGlobalProperty(String id)
	{
		return
				Context.getAdministrationService().getGlobalProperty(id).split(",");
	}

	public static boolean emptyDays(String date, String drugId, String conceptId, String locationId)
	{
		boolean isTrue = false;
		int soldeOnDate = 0;
		soldeOnDate =
				((DrugOrderService)Context.getService(DrugOrderService.class))
						.getSoldeByFromDrugLocation(date, drugId, conceptId, locationId).intValue();
		if (soldeOnDate == 0) {
			isTrue = true;
		}
		return isTrue;
	}



	public static int getLastDayOfMonth(int year, int month)
	{
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);

		cal.set(1, year);

		cal.set(2, month);

		cal.set(5, 1);
		int lastDateOfMonth = cal.getActualMaximum(5);
		cal.set(5, lastDateOfMonth);
		return cal.getTime().getDate();
	}

	public static Integer stockOut(DrugProduct dp, int year, int month, String locationId)
	{
		int month1 = month - 1;
		int daysOfMonth = getLastDayOfMonth(year, month1);
		int count = 0;
		int i = 1;
		String date = "";
		for (i = 1; i <= daysOfMonth; i++) {
			date = year +""+ month1+""+ i;
			if (dp.getDrugId() != null) {
				if (emptyDays(date, dp.getDrugId().getDrugId()+"", null,
						locationId)) {
					count++;
				}
			} else if (emptyDays(date, null,
					dp.getConceptId().getConceptId()+"", locationId)) {
				count++;
			}
		}
		return Integer.valueOf(count);
	}

	public static Integer getSumOfSoldesInStock(String drugId, String conceptId, String locationId, String pharmacyId, String cmddrug)
	{
		int sum = 0;
		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);

		List<Object[]> list = null;
		if (drugId != null) {
			list = service.getLotNumbersExpirationDates(drugId, null,
					locationId, pharmacyId);
		} else {
			list = service.getLotNumbersExpirationDates(null, conceptId,
					locationId, pharmacyId);
		}
		for (Object[] obj : list) {
			if (list.size() > 0)
			{
				sum = sum + service.getCurrSolde(drugId, conceptId, locationId, obj[1].toString(), obj[0].toString(), cmddrug).intValue();
			}
		}
		return Integer.valueOf(sum);
	}









	public static Integer getSumOfSoldesInPharmacy(String drugId, String conceptId, String locationId, String pharmacyId)
	{
		int sum = 0;
		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);

		List<Object[]> list = null;
		if (drugId != null) {
			list = service.getLotNumbersExpirationDates(drugId, null,
					locationId, pharmacyId);
		} else {
			list = service.getLotNumbersExpirationDates(null, conceptId,
					locationId, pharmacyId);
		}
		for (Object[] obj : list) {
			if (list.size() > 0)
			{
				sum = sum + service.getCurrSoldeDisp(drugId, conceptId, pharmacyId, obj[1].toString(), obj[0].toString(), null).intValue();
			}
		}
		return Integer.valueOf(sum);
	}









	public static Integer getLentDrugsDuringTheMonth(Date date, DrugProduct product)
			throws ParseException
	{
		String startDate = null;String endDate = null;
		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);

		int month = date.getMonth() + 1;
		int gregMonth = date.getMonth();
		int year = date.getYear() + 1900;
		int lastDayOfMonth = getLastDayOfMonth(year, gregMonth);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		startDate = year + "-" + month + "-" + "01";
		endDate = year + "-" + month + "-" + lastDayOfMonth;

		return service.getReturnedItemsByDates(startDate, endDate, product, "Lend");
	}









	public static Integer getBorrowedDrugsDuringTheMonth(Date date, DrugProduct product)
			throws ParseException
	{
		String startDate = null;
		String endDate = null;
		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);
		int month = date.getMonth() + 1;
		int gregMonth = date.getMonth();
		int year = date.getYear() + 1900;
		int lastDayOfMonth = getLastDayOfMonth(year, gregMonth);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		startDate = year + "-" + month + "-" + "01";
		endDate = year + "-" + month + "-" + lastDayOfMonth;

		return service.getReturnedItemsByDates(startDate, endDate, product, "Borrow");
	}








	public static HashMap<Integer, String> createCodedOptions(int codedConceptQuestionId)
	{
		HashMap<Integer, String> answersMap = new HashMap();
		Concept questionConcept = Context.getConceptService().getConcept(
				Integer.valueOf(codedConceptQuestionId));
		if (questionConcept != null) {
			for (ConceptAnswer ca : questionConcept.getAnswers()) {
				answersMap.put(
						ca.getAnswerConcept().getConceptId(),
						ca.getAnswerConcept().getDisplayString()
								.toLowerCase(Context.getLocale()));
			}
		}
		return answersMap;
	}


	public static Integer getTheConsommationMaximumMoyenne(String hc, DrugProduct dp)
	{
		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);
		Collection<DrugOrderPrescription> dopList = service
				.getAllDrugOrderPrescription();
		List<ConsumableDispense> consDisp = service.getAllConsumableDipsense();

		int cmm = 0;
		Calendar cal = Calendar.getInstance();
		Calendar then = Calendar.getInstance();
		cal.add(2, -3);
		int sum = 0;
		if ((dp.getDrugId() != null) && (dp.getLotNo() != null)) {
			for (DrugOrderPrescription dop : dopList) {
				if (dp.getDrugId().getDrugId() == dop.getDrugproductId()
						.getDrugId().getDrugId())
					if (dp.getLotNo().equals(
							dop.getDrugproductId().getLotNo())) {
						then.setTime(dop.getDate());
						if (then.after(cal)) {
							sum += dop.getQuantity();
						} else if (!then.before(cal))
						{

							sum += dop.getQuantity(); }
					}
			}
		} else {
			for (ConsumableDispense cd : consDisp) {
				if (dp.getConceptId().getConceptId() == cd.getDrugproductId()
						.getConceptId().getConceptId()) {
					then.setTime(cd.getDate());
					if (then.after(cal)) {
						sum += cd.getQnty();
					} else if (!then.before(cal))
					{

						sum += cd.getQnty(); }
				}
			}
		}
		cmm = sum / 3;
		if (hc.equals("HC")) {
			cmm = sum / 3 / 4;
		}
		return Integer.valueOf(cmm);
	}

	public static PharmacyInventory getPharmacyInventoryById(int piId)
	{
		PharmacyInventory pharmacyInv = null;
		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);
		Collection<PharmacyInventory> piList = service
				.getAllPharmacyInventory();
		for (PharmacyInventory pi : piList) {
			if (pi.getPharmacyInventoryId() == piId) {
				pharmacyInv = pi;
				break;
			}
		}
		return pharmacyInv;
	}






	public static void updatePharmacyInventory(PharmacyInventory pi) {}






	public static PharmacyInventory getPharmaLotExpDP(String drugId, String conceptId, String pharmacy)
	{
		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);
		Collection<PharmacyInventory> pharmaList = service
				.getAllPharmacyInventory();
		PharmacyInventory pharmaInv = null;
		List<DrugProduct> tmpList = new ArrayList();
		for (PharmacyInventory pi : pharmaList) {
			if ((conceptId != null) &&
					(pi.getDrugproductId().getCmddrugId().getPharmacy()
							.getPharmacyId() == Integer.valueOf(pharmacy).intValue())) {
				if (pi.getDrugproductId().getConceptId().getConceptId() ==
						Integer.valueOf(conceptId)) {
					if (!tmpList.contains(pi.getDrugproductId())) {
						pharmaInv = pi;
					} else {
						tmpList.add(pi.getDrugproductId());
					}
				}
			}

			if ((drugId != null) &&
					(pi.getDrugproductId().getCmddrugId().getPharmacy()
							.getPharmacyId() == Integer.valueOf(pharmacy).intValue())) {
				if (pi.getDrugproductId().getDrugId().getDrugId() ==
						Integer.valueOf(drugId)) {
					if (!tmpList.contains(pi.getDrugproductId())) {
						pharmaInv = pi;
					} else {
						tmpList.add(pi.getDrugproductId());
					}
				}
			}
		}
		return pharmaInv;
	}

	public static List<Object[]> getStoreLotExpDP(String drugId, String conceptId, String location, String pharmacy)
	{
		List<Object[]> prodIdentification = new ArrayList();

		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);
		List<ProductReturnStore> ars = null;
		Collection<DrugProduct> dpList = service.getAllProducts();


		List<DrugProduct> tmpList = new ArrayList();
		for (DrugProduct dp : dpList) {
			if (dp.getCmddrugId() != null) {
				if ((dp.getCmddrugId().getLocationId() != null) &&
						(dp.getCmddrugId().getLocationId().getLocationId() ==
								Integer.valueOf(location))) {
					if (conceptId != null) {
						if (!tmpList.contains(dp)) {
							Object[] obj = new Object[3];
							obj[0] = dp.getLotNo();
							obj[1] = dp.getExpiryDate();
							obj[2] = dp;
							prodIdentification.add(obj);
						} else {
							tmpList.add(dp);
						}
					}
				}
			}
			else {
				ars = service.getReturnStockByDP(dp);
				if (((ProductReturnStore)ars.get(0)).getDestination().getLocationId() ==
						Integer.valueOf(location)) {
					if (conceptId != null) {
						if (!tmpList.contains(dp)) {
							Object[] obj = new Object[3];
							obj[0] = dp.getLotNo();
							obj[1] = dp.getExpiryDate();
							obj[2] = dp;
							prodIdentification.add(obj);
						} else {
							tmpList.add(dp);
						}
					}
				}
			}

			if ((drugId != null) && (dp.getCmddrugId() != null)) {
				if (dp.getCmddrugId() != null) {
					if ((dp.getCmddrugId().getLocationId() != null) &&
							(dp.getCmddrugId().getLocationId().getLocationId() ==
									Integer.valueOf(location))) {
						if (!tmpList.contains(dp)) {
							Object[] obj = new Object[3];
							obj[0] = dp.getLotNo();
							obj[1] = dp.getExpiryDate();
							obj[2] = dp;
							prodIdentification.add(obj);
						} else {
							tmpList.add(dp);
						}
					}
				}
				else {
					ars = service.getReturnStockByDP(dp);
					if (((ProductReturnStore)ars.get(0)).getDestination().getLocationId() ==
							Integer.valueOf(location)) {
						if (!tmpList.contains(dp)) {
							Object[] obj = new Object[3];
							obj[0] = dp.getLotNo();
							obj[1] = dp.getExpiryDate();
							obj[2] = dp;
							prodIdentification.add(obj);
						} else {
							tmpList.add(dp);
						}
					}
				}
			}
		}

		return prodIdentification;
	}

	public static Set<DrugProduct> getLotsExpDp(String conceptId, String drugId, String locationId, String pharmacyId)
	{
		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);
		Set<DrugProduct> dpSet = new java.util.HashSet();

		if (pharmacyId != null) {
			Collection<PharmacyInventory> piList = service
					.getAllPharmacyInventory();
			for (PharmacyInventory pi : piList) {
				if ((pi.getDrugproductId().getCmddrugId() != null) &&
						(pi.getDrugproductId().getCmddrugId().getPharmacy().getPharmacyId() == Integer.valueOf(pharmacyId).intValue())) {
					if ((drugId != null) && (pi.getDrugproductId().getDrugId() != null))
					{
						if (pi.getDrugproductId().getDrugId().getDrugId().toString().equals(drugId)) {
							dpSet.add(pi.getDrugproductId());
						}
					} else if ((conceptId != null) &&
							(conceptId.equals(pi.getDrugproductId().getConceptId().getConceptId()))) {
						dpSet.add(pi.getDrugproductId());
					}
				}
			}
		}


		return dpSet;
	}

	public static String DispensingConfig(int goBackXMonth, String to)
			throws ParseException
	{
		String from = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date toDate = sdf.parse(to);
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate);
		cal.set(5, 1);
		cal.add(2, -goBackXMonth);
		from = sdf.format(cal.getTime());
		return from;
	}

	public static PharmacyInventory getPharmacyInventoryByDrugProduct(int dpId) {
		DrugOrderService dos = (DrugOrderService)Context.getService(DrugOrderService.class);
		Collection<PharmacyInventory> piList = dos.getAllPharmacyInventory();
		List<PharmacyInventory> piList1 = new ArrayList();
		for (PharmacyInventory pharmaInv : piList) {
			if (pharmaInv.getDrugproductId().getDrugproductId() == dpId) {
				piList1.add(pharmaInv);
			}
		}
		PharmacyInventory pi = null;
		int tmpId = 0;
		for (PharmacyInventory phInv : piList1) {
			if (tmpId < phInv.getPharmacyInventoryId()) {
				pi = phInv;
			}
		}
		return pi;
	}

	public static DrugProductInventory getDrugProductInventoryByDrugProduct(int dpId)
	{
		DrugOrderService dos = (DrugOrderService)Context.getService(DrugOrderService.class);
		List<DrugProductInventory> dpList = dos.getAllDrugProductInventory();
		List<DrugProductInventory> dpList1 = new ArrayList();
		for (DrugProductInventory dpInv : dpList) {
			if (dpInv.getDrugproductId().getDrugproductId() == dpId) {
				dpList1.add(dpInv);
			}
		}
		DrugProductInventory dp = null;
		int tmpId = 0;
		for (DrugProductInventory dpInv : dpList1) {
			if (tmpId < dpInv.getInventoryId()) {
				dp = dpInv;
			}
		}
		return dp;
	}

	public static ConsumableDispense getConsumableDispense(int cdId) {
		Collection<ConsumableDispense> cdList = ((DrugOrderService)Context.getService(
				DrugOrderService.class)).getAllConsumableDipsense();
		ConsumableDispense consumableDispense = null;
		for (ConsumableDispense cd : cdList) {
			if (cd.getConsumabledispenseId() == cdId) {
				consumableDispense = cd;
				break;
			}
		}
		return consumableDispense;
	}

	private static Comparator<Object> OPENMRS_OBJECT_COMPARATOR = new Comparator<Object>(){

		@Override
		public int compare(Object obj1, Object obj2) {
			int compareInt = 0;
			if (obj1 instanceof Concept && obj2 instanceof Concept) {
				compareInt = ((Concept)obj1).getName().getName().toLowerCase().compareTo(((Concept)obj2).getName().getName().toLowerCase());
			} else if (obj1 instanceof Drug && obj2 instanceof Drug) {
				compareInt = ((Drug)obj1).getName().toLowerCase().compareTo(((Drug)obj2).getName().toLowerCase());
			} else if (obj1 instanceof ConceptAnswer && obj2 instanceof ConceptAnswer) {
				compareInt = ((ConceptAnswer)obj1).getAnswerConcept().getName().getName().toLowerCase().compareTo(((ConceptAnswer)obj2).getAnswerConcept().getName().getName().toLowerCase());
			} else if (obj1 instanceof DrugProduct && obj2 instanceof DrugProduct) {
				String str1 = ((DrugProduct)obj1).getConceptId() != null ? ((DrugProduct)obj1).getConceptId().getName().getName().toLowerCase() : ((DrugProduct)obj1).getDrugId().getName().toLowerCase();
				String str2 = ((DrugProduct)obj2).getConceptId() != null ? ((DrugProduct)obj2).getConceptId().getName().getName().toLowerCase() : ((DrugProduct)obj2).getDrugId().getName().toLowerCase();
				compareInt = str1.toLowerCase().compareTo(str2);
			}
			return compareInt;
		}
	};

	private static Comparator<Map.Entry<Integer, String>> comparator = new Comparator<Map.Entry<Integer, String>>() {
		@Override
		public int compare(Map.Entry<Integer, String> obj1, Map.Entry<Integer, String> obj2) {
			return obj1.getValue().toLowerCase().compareTo(obj2.getValue().toLowerCase());
				}
			};

	private static Comparator<Map.Entry<Integer, DrugProduct>> consumableComparator = new Comparator<Map.Entry<Integer, DrugProduct>>(){
		public int compare(Map.Entry < Integer, DrugProduct > obj1, Map.Entry < Integer, DrugProduct > obj2){
		return obj1.getValue().getConceptId().getName().getName().toLowerCase().compareTo(obj2.getValue().getConceptId().getName().getName().toLowerCase());
	}
	};

	private static Comparator<Map.Entry<Integer, DrugProduct>> drugComparator = new Comparator<Map.Entry<Integer, DrugProduct>>(){
		public int compare(Map.Entry < Integer, DrugProduct > obj1, Map.Entry < Integer, DrugProduct > obj2){
		return obj1.getValue().getDrugId().getName().toLowerCase().compareTo(obj2.getValue().getDrugId().getName().toLowerCase());
	}
	};

	public static SortedSet<Map.Entry<Integer, String>> SortMapValues(Map<Integer, String> map)
	{
		SortedSet<Map.Entry<Integer, String>> entries = new TreeSet(
				comparator);
		entries.addAll(map.entrySet());
		return entries;
	}

	public static SortedSet<Map.Entry<Integer, DrugProduct>> SortDrugMapValues(Map<Integer, DrugProduct> map) {
		SortedSet<Map.Entry<Integer, DrugProduct>> entries = new TreeSet(drugComparator);
		entries.addAll(map.entrySet());
		return entries;
	}

	public static SortedSet<Map.Entry<Integer, DrugProduct>> SortConsumableMapValues(Map<Integer, DrugProduct> map) {
		SortedSet<Map.Entry<Integer, DrugProduct>> entries = new TreeSet(consumableComparator);
		entries.addAll(map.entrySet());
		return entries;
	}

	public static List<Concept> getMedsets(List<Concept> medset) {
		List<Concept> sortedMedset = new ArrayList();

		for (Concept concept : medset) {
			sortedMedset.add(concept);
		}

		Collections.sort(sortedMedset, OPENMRS_OBJECT_COMPARATOR);

		return sortedMedset;
	}

	public static List<ConceptAnswer> sortConsumable(List<ConceptAnswer> consumable)
	{
		List<ConceptAnswer> sortedConsumable = new ArrayList();

		for (ConceptAnswer conceptAnswer : consumable) {
			sortedConsumable.add(conceptAnswer);
		}
		Collections.sort(sortedConsumable, OPENMRS_OBJECT_COMPARATOR);

		return sortedConsumable;
	}

	public static List<Drug> sortDrugs(List<Drug> drugs) {
		List<Drug> sortedDrugs = new ArrayList();

		for (Drug drug : drugs) {
			sortedDrugs.add(drug);
		}

		Collections.sort(sortedDrugs, OPENMRS_OBJECT_COMPARATOR);

		return sortedDrugs;
	}

	public static Collection<DrugProduct> sortDrugProducts(Collection<DrugProduct> drugproducts) {
		List<DrugProduct> sortedDrugProducts = new ArrayList();

		for (DrugProduct drugproduct : drugproducts) {
			sortedDrugProducts.add(drugproduct);
		}

		Collections.sort(sortedDrugProducts, OPENMRS_OBJECT_COMPARATOR);

		return sortedDrugProducts;
	}

	public static void setConsultationAppointmentAsAttended(Appointment appointment)
	{
		AppointmentUtil.saveAttendedAppointment(appointment);
	}

	public static void createWaitingPharmacyAppointment(Patient patient, Encounter encounter)
			throws ParseException
	{
		Appointment waitingAppointment = new Appointment();
		Services service = AppointmentUtil.getServiceByConcept(
				Context.getConceptService().getConcept(Integer.valueOf(6711)));

		waitingAppointment.setAppointmentDate(new Date());
		waitingAppointment.setAttended(Boolean.valueOf(false));
		waitingAppointment.setVoided(false);
		waitingAppointment.setCreatedDate(new Date());
		waitingAppointment.setCreator(Context.getAuthenticatedUser());
		waitingAppointment.setProvider(Context.getAuthenticatedUser()
				.getPerson());
		waitingAppointment.setNote("This is a waiting patient to the Pharmacy");
		waitingAppointment.setPatient(patient);
		waitingAppointment.setLocation(Context.getLocationService()
				.getDefaultLocation());

		waitingAppointment.setService(service);

		if (encounter != null) {
			waitingAppointment.setEncounter(encounter);
		}
		if (!AppointmentUtil.isPatientAlreadyWaitingThere(patient,
				new AppointmentState(Integer.valueOf(4), "WAITING"), service, new Date()).booleanValue())
		{
			AppointmentUtil.saveWaitingAppointment(waitingAppointment);
		}
	}
	public static void setPharmacyAppointmentAsAttended(Appointment appointment) {
		AppointmentUtil.saveAttendedAppointment(appointment);
	}
	public static DrugProduct getDrugStoreProductByDrugId(int drugId) {
		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);
		Collection<DrugProduct> drugProducts = service.getAllProducts();
		DrugProduct drugProduct = null;
		for (DrugProduct dp : drugProducts) {
			if ((dp.getDrugId() != null) && (dp.getIsDelivered()) && (dp.getCmddrugId().getLocationId() != null) &&
					(dp.getDrugId().getDrugId().intValue() == drugId)) {
				drugProduct = dp;
				break;
			}
		}

		return drugProduct;
	}

	public static Integer getExpiredDrugByDatesAndLocation(Date startDate, Date endDate, Location location, int drugId)
	{
		DrugOrderService service = (DrugOrderService)Context.getService(DrugOrderService.class);
		Collection<DrugProduct> drugProducts = service.getAllProducts();
		int expiredDrugs = 0;
		for (DrugProduct dp : drugProducts) {
			if ((dp.getCmddrugId().getLocationId() != null) && (dp.getCmddrugId().getLocationId().getLocationId() == location.getLocationId()) &&
					(dp.getDrugId() != null) &&
					(drugId == dp.getDrugId().getDrugId().intValue()) &&
					(compareDates(startDate, dp.getExpiryDate(), endDate))) {
				expiredDrugs = service.getCurrSolde(dp.getDrugId().getDrugId()+"",null,location.getLocationId()+"",dp.getExpiryDate()+"", dp.getLotNo(), null).intValue();
			}
		}
		return Integer.valueOf(expiredDrugs);
	}

	public static boolean compareDates(Date startDate, Date toCompare, Date endDate)
	{
		boolean before = false;

		if ((toCompare.after(startDate)) && (toCompare.before(endDate))) {
			before = true;
		}
		return before;
	}
}