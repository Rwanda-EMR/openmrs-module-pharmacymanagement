<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="View Drug Store management" otherwise="/login.htm" redirect="/module/pharmacymanagement/stocksecurity.list"/>

<div>
<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle">

<%@ include file="template/localHeader.jsp"%>

<!--  
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/dataentrystyle.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/demo_page.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/demo_table.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.dataTables.js" />
-->

<script type="text/javascript">
var $ = jQuery.noConflict();
	$(document).ready( function() {
		$('#example').dataTable();
	});
</script>


<table width="100%" style="">
	<tr>
		<td width="15%"><spring:message code="pharmacymanagement.fosa" /></td>
		<td width="25%">: ${location.name}</td>
		<td width="20%">&nbsp;</td>
		<td width="15%"><spring:message code="pharmacymanagement.denoDosForm" /></td>
		<td width="25%">: ${drug.name == null ? concept.name.name : drug.name}</td>
		<td width="20%">&nbsp;</td>
	</tr>
	<tr>
		<td width="15%"><spring:message code="pharmacymanagement.district" /></td>
		<td width="25%">: ${location.countyDistrict}</td>
		<td width="20%">&nbsp;</td>
		<td width="15%"><spring:message code="pharmacymanagement.units" /></td>
		<td width="25%">: ${drug.units}</td>
		<td width="20%">&nbsp;</td>
	</tr>
	<tr>
		<td><spring:message code="pharmacymanagement.region" /></td>
		<td>: ${location.stateProvince} </td>
		<td width="20%">&nbsp;</td>
		<td width="15%"><spring:message code="pharmacymanagement.storageCondition" /></td>
		<td width="25%">: &nbsp;</td>
		<td width="20%">&nbsp;</td>
	</tr>
	<tr>
		<td><spring:message code="pharmacymanagement.supportProg" /></td>
		<td>: &nbsp;</td>
		<td width="20%">&nbsp;</td>
		<td width="15%"><spring:message code="pharmacymanagement.prodCode" /></td>
		<td width="25%">: &nbsp;</td>
		<td width="20%">&nbsp;</td>
	</tr>
</table>
<br />
<br />

<c:if test="${!(drugProInv eq null)}">
	<div id="dt_example">
	<div id="container">
	<table cellpadding="0" cellspacing="0" border="0" class="display"
		id="example">
		<thead>
			<tr>
				<th>N<sup>o</sup></th>
				<th><spring:message code="pharmacymanagement.date" /></th>
				<th><spring:message code="pharmacymanagement.location" /></th>
				<c:if test="${drug.name != null}">
				<th><spring:message code="pharmacymanagement.lotNr" /></th>
				<th><spring:message code="pharmacymanagement.expDate" /></th>
				</c:if>
				<th><spring:message code="pharmacymanagement.in" /></th>
				<th><spring:message code="pharmacymanagement.out" /></th>
				<th><spring:message code="pharmacymanagement.solde" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="inventory" items="${drugProInv}" varStatus="num">
				<tr>
					<td>${num.count}.</td>
					<td>${inventory.inventoryDate}</td>
					<td>${inventory.drugproductId.cmddrugId.locationId.name eq null ? inventory.drugproductId.cmddrugId.pharmacy.name : inventory.drugproductId.cmddrugId.locationId.name}</td>
					<c:if test="${drug.name != null}">
					<td>${inventory.drugproductId.lotNo}</td>
					<td>${inventory.drugproductId.expiryDate}</td>
					</c:if>
					<td>${inventory.entree}</td>
					<td>${inventory.sortie}</td>
					<td>${inventory.solde}</td>
				</tr>
			</c:forEach>
		</tbody>

	</table>
	</div>
	</div>
</c:if>

</div>
<div style="clear: both;"></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>