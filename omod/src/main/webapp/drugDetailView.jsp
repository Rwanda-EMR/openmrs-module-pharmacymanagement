<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacymanagement/drugDetail.list"/>

<div>

<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>

<div id="middle">
<%@ include file="template/adminLocalHeader.jsp"%>

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
			<th>N<sub>o</sub></th>
			<th>Drug</th>
			<th>Forme</th>
			<th>Units</th>
			<th>Measurement Unit</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${drugDetails}" var="detail" varStatus="num">

			<tr>

				<td><a
					href="${pageContext.request.contextPath}/module/pharmacymanagement/drugDetail.form?drugDetailId=${detail.drugDetailsId}">${num.count}</a></td>
				<td>${detail.drug.name}</td>
				<td>${detail.forme}</td>
				<td>${detail.units}</td>
				<td>${detail.measurementUnit}</td>
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