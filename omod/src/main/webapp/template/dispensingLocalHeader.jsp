<ul id="menu">
	<openmrs:hasPrivilege privilege="Manage Pharmacy">
		<li
			class="first<c:if test='<%= request.getRequestURI().contains("pharmacyRequestForm") %>'> active</c:if>">
		<a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacyrequest.form">
		<spring:message code="pharmacymanagement.drugReq" /> (<spring:message code="pharmacymanagement.pharmacy" />)</a></li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Pharmacy">
		<li
			<c:if test='<%= request.getRequestURI().contains("findPharmacyRequestForm") %>'>class="active"</c:if>>
		<a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacyorder.list">
		<spring:message code="pharmacymanagement.findPhcyOrder"/></a></li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Pharmacy report">
		<li
			<c:if test='<%= request.getRequestURI().contains("pharmacyReport") %>'>class="active"</c:if>>
		<a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/report.form">
		<spring:message code="pharmacymanagement.pharmacyReport"/></a></li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Pharmacy">
		<li
			<c:if test='<%= request.getRequestURI().contains("pharmacyDrugDisp") %>'>class="active"</c:if>>
		<a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacyDrugDisp.htm">
		<spring:message code="Drug Dispensing"/></a></li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Pharmacy">
		<li
			<c:if test='<%= request.getRequestURI().contains("drugOrderList") %>'>class="active"</c:if>>
		<a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/drugOrder.list">
		<spring:message code="pharmacymanagement.orderList"/></a></li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Pharmacy">
		<li
			<c:if test='<%= request.getRequestURI().contains("consumableDispensation") %>'>class="active"</c:if>>
		<a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/consumabledispensation.htm">
		<spring:message code="pharmacymanagement.consumableDispensing"/></a></li>
	</openmrs:hasPrivilege>
</ul>