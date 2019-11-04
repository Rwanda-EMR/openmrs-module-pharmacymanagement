<ul id="menu">
	<openmrs:hasPrivilege privilege="View Drug Store management">
		<li
			class="first<c:if test='<%= request.getRequestURI().contains("drugStoreForm") %>'> active</c:if>">
		<a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/storequest.form">
		<spring:message code="pharmacymanagement.drugReq" /> (<spring:message code="@MODULE_ID@.stock" />)</a></li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Drug Store management">
		<li
			<c:if test='<%= request.getRequestURI().contains("findDrugCmd") %>'>class="active"</c:if>>
		<a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/cmdsearch.form">
		<spring:message code="pharmacymanagement.findCmd" /> </a></li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Drug Store management">
		<li
			<c:if test='<%= request.getRequestURI().contains("storeReturnForm") %>'>class="active"</c:if>>
		<a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/return.form">
		<spring:message code="pharmacymanagement.returnForm" /></a></li>
		<li
			<c:if test='<%= request.getRequestURI().contains("storeSearch") %>'>class="active"</c:if>>
		<a
			href="${pageContext.request.contextPath}/module/pharmacymanagement/storeSearch.form">
		<spring:message code="Store Search" /></a></li>
	</openmrs:hasPrivilege>

	<openmrs:hasPrivilege privilege="View Pharmacy Drug Store Adjustment">
    		<li
    			class="first<c:if test='<%= request.getRequestURI().contains("drugAdjustmentForm") %>'> active</c:if>">
    		<a
    			href="${pageContext.request.contextPath}/module/pharmacymanagement/adjustmentrequest.form">
    		<spring:message code="pharmacymanagement.drugAdjustment" /> (<spring:message code="@MODULE_ID@.stock" />)</a></li>
    	</openmrs:hasPrivilege>
</ul>