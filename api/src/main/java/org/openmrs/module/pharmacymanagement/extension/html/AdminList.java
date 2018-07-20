package org.openmrs.module.pharmacymanagement.extension.html;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.AdministrationSectionExt;

public class AdminList extends AdministrationSectionExt {

	/**
	 * @see AdministrationSectionExt#getMediaType()
	 */
	public MEDIA_TYPE getMediaType() {
		return MEDIA_TYPE.html;
	}

	/**
	 * @see AdministrationSectionExt#getTitle()
	 */
	public String getTitle() {
		return "pharmacymanagement.storeMgt";
	}

	/**
	 * @see AdministrationSectionExt#getLinks()
	 */
	public Map<String, String> getLinks() {
		Map<String, String> map = new LinkedHashMap<String, String>();

		if (Context.getAuthenticatedUser().hasPrivilege("View Drug Store management"))
			map.put("module/pharmacymanagement/storequest.form", "pharmacymanagement.storeMgt");
		if (Context.getAuthenticatedUser().hasPrivilege("View Pharmacy management"))
			map.put("module/pharmacymanagement/pharmacyrequest.form", "pharmacymanagement.phcyMgt");
		if (Context.getAuthenticatedUser().hasPrivilege("View Administration Functions"))
			map.put("module/pharmacymanagement/pharmacy.form", "pharmacymanagement.cfg");
		if (Context.getAuthenticatedUser().hasPrivilege("View Drug Store alert"))
			map.put("module/pharmacymanagement/storeAlert.htm", "pharmacymanagement.storeAlert");
		
		return map;
	}
	
	@Override
	public String getRequiredPrivilege() {
		return "View Pharmacy management and Dispensing";
	}

}
