<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm"	redirect="/module/pharmacymanagement/drugOrder.list" />

<!--  
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/demo_page.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/demo_table.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.dataTables.js" />
-->

<div>

<div id="outer"><%@ include file="template/leftMenu.jsp"%>
</div>
<script type="text/javascript">
	var $dsm = jQuery.noConflict();
	$dsm(document).ready( function() {
		$dsm('#example').dataTable();
	});
</script>



<div id="middle">
<%@ include file="template/dispensingLocalHeader.jsp"%> 
<c:if test="${!empty adheranceList}">
	<div id="dt_example">
	<div id="container">
	<table cellpadding="0" cellspacing="0" border="0" class="display"
		id="example">
		<thead>
			<tr>
				<th>N<sub>o</sub></th>
				<th>Date</th>
				<th>Weight</th>
				<th>Patient Name</th>
				<th>Drug</th>
				<th>Quantity</th>
				<th>Pharmacist</th>
				<th>Pharmacy</th>
				<th>Next Visit Date</th>
			</tr>
		</thead>
		<tbody id="drugOrderList">
			<c:forEach items="${adheranceList}" var="dl" varStatus="num">
				<tr>
					<td>${num.count}</td>
					<td><openmrs:formatDate date="${dl.encDate}" type="textbox" /></td>
					<td>${dl.weight}</td>
					<td>${dl.dop.patient.personName.familyName} &nbsp;
					${dl.dop.patient.personName.givenName}</td>
					<td>${dl.dop.drugproductId.drugId.name}</td>
					<td>${dl.dop.quantity}</td>
					<td>${dl.dop.user.personName.familyName} &nbsp;
					${dl.dop.user.personName.givenName}</td>
					<td>${dl.dop.drugproductId.cmddrugId.pharmacy.name}</td>
					<td><openmrs:formatDate date="${dl.nvDate}" type="textbox" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	</div>
</c:if></div>
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
				<th>Quantity</th>
				<th>Req. Ref. File N<sub>o</sub></th>
				<th>Lot N<sub>o</sub></th>
				<th>Expiration Date</th>
				<th>Observations</th>
				<th>Edit</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${returnStoreList}" var="rs" varStatus="num">
				<tr>
					<td>${num.count}</td>
					<td id="returnDate_id_${rs.returnStoreId}"><openmrs:formatDate date="${rs.retDate}" type="textbox" /></td>
					<td id="drug_name_${rs.returnStoreId}">${rs.drugproductId.drugId.name}</td>
					<td>${rs.drugproductId.drugId.units}</td>
					<td id="qty_id_${rs.returnStoreId}">${rs.retQnty}</td>
					<td>&nbsp;</td>
					<td id="lot_id_${rs.returnStoreId}">${rs.drugproductId.lotNo}</td>
					<td id="expDate_id_${rs.returnStoreId}"><openmrs:formatDate date="${rs.drugproductId.expiryDate}" type="textbox" /></td>
					<td id="obs_id_${rs.returnStoreId}">${rs.observation}</td>
					<td><button type="button" class="ars" id="ars_${rs.returnStoreId}"><img src="${pageContext.request.contextPath}/images/edit.gif" /></button></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	</div>
</div>
</c:if>
<div style="clear: both;"></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>