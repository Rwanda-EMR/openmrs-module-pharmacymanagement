/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.pharmacymanagement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.BaseModuleActivator;


/**
 * This class contains the logic that is run every time this module
 * is either started or shutdown
 */
public class PharmacyManagementModuleActivator extends BaseModuleActivator {

	protected Log log = LogFactory.getLog(this.getClass());

	/**
	 * @see BaseModuleActivator#started()
	 * */
	public void started() {
		log.info("Drug Store Management and Dispensing Rwanda Module started");
	}
	
	/**
	 *  @see BaseModuleActivator#stopped()
	 * */
	public void stopped() {
		log.info("Drug Store Management and Dispensing Rwanda Module stopped");
	}
	
}
