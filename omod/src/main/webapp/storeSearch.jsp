<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Drug Store" otherwise="/login.htm" redirect="/module/pharmacymanagement/storeSearch.form"/>

<div>
<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle">

<%@ include file="template/localHeader.jsp"%>

<script type="text/javascript">
var $ = jQuery.noConflict();
	$(document).ready( function() {
		$('#example').dataTable();
		$('.box').hide();
	});
</script>
<div class="box">
<form method="post" action="storeSearch.form">
<table width="100%" style="">
	<tr>
		<td>Drug</td>
		<td>
			<select name="drug">
				<option value="0">-- All --</option>
				<c:forEach var="drug" items="${drugsInSystem}">
					<option value="${drug.drugId}">${drug.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td>Consumable</td>
		<td>
			<select name="consumable">
				<option value="0">-- All --</option>
				<c:forEach var="consumable" items="${consumablesInSystem}">
					<option value="${consumable.answerConcept.conceptId}">${consumable.answerConcept.name.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td><input type="submit" value="Search" class="send" /></td>
	</tr>
</table>
</form>


</div>
<br />
<br />
<div id="dt_example">
<div id="container">
<table cellpadding="0" cellspacing="0" border="0" class="display" id="example">
	<thead>
		<tr>
			<th>N<sup>o</sup></th>
			<th>Drug Name</th>
			<th><spring:message code="pharmacymanagement.lotNr" /></th>
			<th><spring:message code="pharmacymanagement.expDate" /></th>
			<th><spring:message code="pharmacymanagement.in" /></th>
			<th>Distributed/Lost</th>
			<th><spring:message code="pharmacymanagement.solde" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${itemMap}" varStatus="num">
			<tr>
				<td>${num.count}.</td>
				<td>${item.value.drugName}</td>
				<td>${item.value.lotNo}</td>
				<td>${item.value.expirationDate}</td>
				<td>${item.value.in}</td>
				<td>${item.value.consumed}</td>
				<td>${item.value.store}</td>
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