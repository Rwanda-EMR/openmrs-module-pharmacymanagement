<!--  
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/demo_page.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/demo_table.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.dataTables.js" />
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
-->
<ul id="menu">
	
	<li
		class="first<c:if test='<%= request.getRequestURI().contains("pharmacyView") %>'> active</c:if>">
	<a
		href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacy.list">
	<spring:message code="pharmacymanagement.phcyView"/></a></li>
	<li
		<c:if test='<%= request.getRequestURI().contains("pharmacyForm") %>'>class="active"</c:if>>
	<a
		href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacy.form">
	<spring:message code="pharmacymanagement.phcyAdd"/></a></li>
	<!-- 
	<li
		<c:if test='<%= request.getRequestURI().contains("drugDetailForm") %>'>class="active"</c:if>>
	<a
		href="${pageContext.request.contextPath}/module/pharmacymanagement/drugDetail.form">
	Add Drug details</a></li>
	<li
		<c:if test='<%= request.getRequestURI().contains("drugDetailView") %>'>class="active"</c:if>>
	<a
		href="${pageContext.request.contextPath}/module/pharmacymanagement/drugDetail.list">
	Display Drug details</a></li>
	 -->
</ul>