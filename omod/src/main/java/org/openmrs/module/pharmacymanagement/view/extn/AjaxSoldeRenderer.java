package org.openmrs.module.pharmacymanagement.view.extn;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.Person;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacymanagement.service.DrugOrderService;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.web.servlet.view.AbstractView;

public class AjaxSoldeRenderer extends AbstractView {
	private static final Log log = LogFactory.getLog(AjaxViewRenderer.class);
	protected String sourceKey = "source";

	@Override
	protected void renderMergedOutputModel(Map map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Map from the returned form: " + map);
		PrintWriter writer = response.getWriter();
		Object source = map.get(sourceKey);

		// Disable caching
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/json");

		writer.write("[");

		if (source != null) {
			if (source instanceof Drug) {
				Drug drug = (Drug) source;
				int currSolde = 0;
				// Object[] items = list.toArray();
				DrugOrderService dos = Context
						.getService(DrugOrderService.class);
				LocationService locationService = Context.getLocationService();
				Location dftLoc = null;
				String locationStr = Context.getAuthenticatedUser()
						.getUserProperties()
						.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);

				try {
					dftLoc = locationService.getLocation(Integer
							.valueOf(locationStr));
				} catch (Exception e) {
				}
				
				currSolde = dos.getSoldeByToDrugLocation(new Date()
						+ "", drug.getDrugId() + "", null,
						dftLoc.getLocationId() + "");				
				
				writer.write("{\"solde\":\"" + currSolde + "\"}");
			} else
				writer.write("\"ERROR: Source object is null\"");

			writer.write("]");
		}
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
