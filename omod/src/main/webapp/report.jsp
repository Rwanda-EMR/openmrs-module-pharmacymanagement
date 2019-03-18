<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Drug Store management" otherwise="/login.htm"
	redirect="/module/pharmacymanagement/arvreport.list" />


<div>
<div id="outer"><%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle"><%@ include file="template/localHeader.jsp"%>


 
<script	type="text/javascript">
	var $ = jQuery.noConflict();
	$(document).ready( function() {
		$('#example').dataTable();
	});
</script>

<fieldset><legend><spring:message code="pharmacymanagement.arvMonthlyReport" /></legend>
<form method="post" action="arvreport.list?stat=on">
<table>
	<tr>
		<td><spring:message code="pharmacymanagement.from" /></td>
		<td>: <input name="from" type="text" onfocus="showCalendar(this)"
			size="10" id="from" /></td>
		<td>&nbsp;</td>
		<td><spring:message code="pharmacymanagement.to" /></td>
		<td id="repo">: <input name="to" type="text" onfocus="showCalendar(this)"
			size="10" id="to" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="submit" value="Find" class="send" id="sendBtn" /></td>
	</tr>

</table>
</form>
</fieldset>

<hr />
<br />
<br />
<c:if test="${!(consommationMap eq null)}">
	<div id="dt_example">
	<div id="container">
	<table cellpadding="0" cellspacing="0" border="0" class="display"
		id="example">
		<thead>
			<tr>
				<th>N<sup>o</sup></th>
				<th><spring:message code="pharmacymanagement.drug" /></th>
				<th><spring:message code="pharmacymanagement.units" /></th>
				<th><spring:message code="pharmacymanagement.storeQntyFistDay" /></th>
				<th><spring:message code="pharmacymanagement.qntyRecDurRepMonth" /></th>
				<th><spring:message code="pharmacymanagement.qntyDispDurRepMonth" /></th>
				<th><spring:message code="pharmacymanagement.storeQntyLastDay" /></th>
				<th><spring:message code="pharmacymanagement.expDate" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="consommation" items="${consommationMap}"
				varStatus="num">
				<tr>
					<td>${num.count}.</td>
					<td><a
						href="${pageContext.request.contextPath}/module/pharmacymanagement/stocksecurity.list?drugId=${consommation.value.drugId}&locationId=${consommation.value.locationId}">
					${consommation.value.drugName}</a></td>
					<td>${consommation.value.conditUnitaire}</td>
					<td>${consommation.value.qntPremJour}</td>
					<td>${consommation.value.qntRecuMens}</td>
					<td>${consommation.value.qntConsomMens}</td>
					<td>${consommation.value.qntRestMens}</td>
					<td>${consommation.value.expirationDate}</td>
				</tr>
			</c:forEach>
		</tbody>

	</table>
	</div>
	</div>
</c:if></div>

<div style="clear: both;"></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>