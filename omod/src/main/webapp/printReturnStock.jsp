<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="View Drug Store management"
	otherwise="/login.htm" redirect="/module/pharmacymanagement/return.form" />

<div>
<div id="outer"><%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle"><%@ include file="template/localHeader.jsp"%>



<script type="text/javascript">
var $j = jQuery.noConflict();
$j(document).ready(function() {
	$j('#example').dataTable();
});
</script>
	
	
<c:if test="${!empty returnStoreList}">
<div id="return_stock">
<div id="dt_example">
	<div id="container">
	<table cellpadding="0" cellspacing="0" border="0" class="display"
		id="example">
		<thead>
			<tr>
				<th>N<sub>o</sub></th>
				<th>Return Date</th>
				<th>Drug</th>
				<th>Units</th>
				<th>From(Location)</th>
				<th>From(Pharmacy)</th>
				<th>To</th>
				<th>Quantity</th>
				<th>Lot N<sub>o</sub></th>
				<th>Expiration Date</th>
				<th>Observations</th>				
				<!-- Uncomment if you want to do the edit
				<th>Edit</th> -->
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${returnStoreList}" var="rs" varStatus="num">
				<tr class="ars" id="ars_${rs.returnStoreId}">
					<td>${num.count}</td>
					<td><openmrs:formatDate date="${rs.retDate}" type="textbox" /></td>
					<td>${rs.drugproductId.drugId.name}</td>
					<td>${rs.drugproductId.drugId.units}</td>
					<td>${rs.originLocation.name}</td>
					<td>${rs.originPharmacy.name}</td>
					<td>${rs.destination.name}</td>
					<td>${rs.retQnty}</td>
					<td>${rs.drugproductId.lotNo}</td>
					<td><openmrs:formatDate date="${rs.drugproductId.expiryDate}" type="textbox" /></td>
					<td>${rs.observation}</td>
					<!-- Uncomment if you want to do the edit
					<td><img src="${pageContext.request.contextPath}/images/edit.gif" style="cursor: pointer" /></td>  -->
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	</div>
</div>
</c:if>
	
	
	
	
	
	
</div>
<div style="clear: both;"></div>
</div>