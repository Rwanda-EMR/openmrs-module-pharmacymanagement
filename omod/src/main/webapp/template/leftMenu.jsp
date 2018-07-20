<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />


<!--  <openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/jquery.simplemodal.js" /> -->
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/basic.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/style/basic.css" />

<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/validator.js" />

<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/jquery.PrintArea.js" />


<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/jquery.dataTables.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/style/demo_page.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/style/demo_table.css" />

<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/style/dataentrystyle.css" />

<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/jquery.tabs.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/style/jquery.tabs.css" />

<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/create_dynamic_field.js" />

<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/jquery.treeview.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/style/jquery.treeview.css" />

<script type="text/javascript">
var $e = jQuery.noConflict();
	$e(document).ready( function() {
		$e("#browser").treeview();
	});
</script>
<ul id="browser" class="filetree treeview-famfamfam">
	<openmrs:hasPrivilege privilege="View Drug Store management">
		<li class="closed"><span class="folder"><spring:message code="pharmacymanagement.storeMgt"/></span>
		<ul>
			<li><span class="file"><a
				href="${pageContext.request.contextPath}/module/pharmacymanagement/storequest.form"><spring:message code="pharmacymanagement.drugReq" /> (<spring:message code="pharmacymanagement.stock" />)</a></span></li>
			<li><span class="file"><a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/cmdsearch.form"><spring:message code="pharmacymanagement.findCmd"/></a></span></li>
			<li><span class="file"><a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/return.form"><spring:message code="pharmacymanagement.returnForm"/></a></span></li>
			<li><span class="file"><a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/storeSearch.form"><spring:message code="Store Search"/></a></span></li>
		</ul>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Pharmacy management">
		<li class="closed"><span class="folder"><spring:message code="pharmacymanagement.phcyMgt"/></span>
		<ul>
			<li><span class="file"><a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacyrequest.form"><spring:message code="pharmacymanagement.drugReq" /> (<spring:message code="pharmacymanagement.pharmacy" />)</a></span></li>
			<li><span class="file"><a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacyorder.list"><spring:message code="pharmacymanagement.findPhcyOrder"/></a></span></li>
			<li><span class="file"><a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacyDrugDisp.htm"><spring:message code="Drug Dispensing"/></a></span></li>
			<li><span class="file"><a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/drugOrder.list"><spring:message code="pharmacymanagement.orderList"/></a></span></li>
			<li><span class="file"><a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/consumabledispensation.htm"><spring:message code="pharmacymanagement.consumableDispensing"/></a></span></li>
		</ul> 
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Drug store alert">
		<li class="closed"><span class="folder"><spring:message code="pharmacymanagement.storeAlert"/></span>
		<ul>
			<li><span class="file"><a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/storeAlert.htm"><spring:message code="pharmacymanagement.storeAlert"/> & <br /><spring:message code="pharmacymanagement.expDateAlert"/></a></span></li>
		</ul>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Administration Functions">
		<li class="closed"><span class="folder"><spring:message code="pharmacymanagement.phcy"/></span>
		<ul>
			<li><span class="file"><a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacy.list"><spring:message code="pharmacymanagement.phcyView"/></a></span></li>
			<li><span class="file"><a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacy.form"><spring:message code="pharmacymanagement.phcyAdd"/></a></span></li>
		</ul>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Pharmacy report">
		<li class="closed"><span class="folder"><spring:message code="pharmacymanagement.report"/></span>
			<ul>
				<li><span class="file"><a
					href="${pageContext.request.contextPath}/module/pharmacymanagement/report.form"><spring:message code="pharmacymanagement.pharmacyReport"/></a></span></li>
			</ul>
		</li>
	</openmrs:hasPrivilege>
</ul>