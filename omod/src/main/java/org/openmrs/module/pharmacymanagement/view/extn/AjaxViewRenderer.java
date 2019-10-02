/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.view.extn;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.DrugProduct;
import org.openmrs.module.pharmacymanagement.PharmacyInventory;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 */
public class AjaxViewRenderer extends AbstractView {
	private static final Log log = LogFactory.getLog(AjaxViewRenderer.class);
	protected String sourceKey = "source";
	
	@Override
	protected void renderMergedOutputModel(Map map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Map from the returned form: " + map);
		PrintWriter writer = response.getWriter();
		Object source = map.get(sourceKey);

		DrugOrderService dos = Context.getService(DrugOrderService.class);

		Collection<PharmacyInventory> piList = new ArrayList<PharmacyInventory>();
		int solde = 0, currSolde = 0, currConsSolde = 0;
		piList = dos.getAllPharmacyInventory();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");



		// Disable caching
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/json");

		writer.write("[");
		
		if (source != null) {
			if (source instanceof Collection) {
				Collection<?> collection = (Collection<?>) source;
				Object[] items = collection.toArray();

				for (int i = 0; i < items.length; i++) {
					String id = null;
					String name = null;
					String lotno=null;
					Date expiryDate=null;
					String nd = null;
					Object item = items[i];
					if(item instanceof Drug) {
						id = ((Drug) item).getDrugId() + "";
						name = ((Drug) item).getName();
					} 
					if(item instanceof DrugProduct) {


					/*	for (PharmacyInventory pi : piList) {
							if (pi.getDrugproductId().getConceptId() == ((DrugProduct) item).getConceptId() && pi.getDrugproductId().getLotNo()==((DrugProduct) item).getLotNo()) {
								currSolde = dos.getCurrSoldeDisp(null, pi.getDrugproductId().getConceptId().getConceptId()+ "", pi.getDrugproductId().getCmddrugId().getPharmacy().getPharmacyId()+ "", null, null, nd);
							}
						}*/

						currSolde = dos.getCurrSoldeDisp(null, ((DrugProduct) item).getConceptId().getConceptId()+ "", ((DrugProduct) item).getCmddrugId().getPharmacy().getPharmacyId()+ "", ((DrugProduct) item).getExpiryDate()+"", ((DrugProduct) item).getLotNo()+"", nd);


						id = ((DrugProduct) item).getDrugproductId() + "";
						name = ((DrugProduct) item).getConceptId().getName().getName();
						lotno=((DrugProduct) item).getLotNo();
						expiryDate=((DrugProduct) item).getExpiryDate();
						//expiryDate=sdf.parse(expiryDate.toString());
					}

					if (i > 0)
						writer.write(',');

					writer.write("{\"id\":\"" + id + "\", \"name\":\""+ name + "\",\"lotno\":\""+lotno+"\",\"expiry\":\""+expiryDate+"\",\"solde\":\""+currSolde+"\"}");
				}
			}
		} else
			writer.write("\"ERROR: Source object is null\"");

		writer.write("]");
	}

	/**
	 * @return the sourceKey
	 */
	public String getSourceKey() {
		return sourceKey;
	}

	/**
	 * @param sourceKey
	 *            the sourceKey to set
	 */
	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}
}
