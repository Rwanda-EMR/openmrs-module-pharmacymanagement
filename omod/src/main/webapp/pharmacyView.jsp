<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="View Pharmacies" otherwise="/login.htm" redirect="/module/pharmacymanagement/pharmacy.list" />


<div>

<div id="outer"><%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle"><%@ include file="template/adminLocalHeader.jsp"%>

<script type="text/javascript">
	var $ = jQuery.noConflict();
	$(document).ready( function() {
		$('#example').dataTable();
	});
</script>


<div id="dt_example">
<div id="container">
<table cellpadding="0" cellspacing="0" border="0" class="display"
	id="example">
	<thead>
		<tr>
			<th>N<sup>o</sup></th>
			<th><spring:message code="pharmacymanagement.phcyName" /></th>
			<th><spring:message code="pharmacymanagement.location" /></th>
			<th><spring:message code="pharmacymanagement.delete" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${pharmacies}" var="pharmacy" varStatus="num">
			<tr>
				<td>${num.count}</td>
				<td><a
					href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacy.form?pharmacyId=${pharmacy.pharmacyId}">${pharmacy.name}</a></td>
				<td>${pharmacy.locationId.name}</td>
				<td><a
					href="${pageContext.request.contextPath}/module/pharmacymanagement/pharmacy.list?pharmacyId=${pharmacy.pharmacyId}"><img
					src="${pageContext.request.contextPath}/images/delete.gif"
					style="cursor: pointer;" /></a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</div>
</div>

<div style="clear: both;"></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>