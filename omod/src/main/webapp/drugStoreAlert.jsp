<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Drug store alert" otherwise="/login.htm" redirect="/module/pharmacymanagement/storeAlert.htm"/>

<!--  
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/dataentrystyle.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.tabs.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.tabs.css" />
-->

<div>

<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle">

<script type="text/javascript">
var $ = jQuery.noConflict();
	$(function() {
		$('#container').tabs();
	});
</script>


<div id="container">

<ul>
	<li><a href="#fragment-1"><span><spring:message code="pharmacymanagement.storeRunOut"/></span></a></li>
	<li><a href="#fragment-2"><span><spring:message code="pharmacymanagement.expDateAlert"/></span></a></li>
</ul>

 <div id="fragment-1">
<table class="return">
	<thead>
		<tr>
			<th>No</th>
			<th><spring:message code="pharmacymanagement.drug"/></th>			
			<th><spring:message code="lotNr" /></th>
			<th><spring:message code="pharmacymanagement.consumed"/></th>
			<th><spring:message code="pharmacymanagement.inStore"/></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${swList}" var="sw" varStatus="num">
			<tr>
				<td>${num.count}</td>
				<td>${sw.drugName}</td>
				<td>${sw.lotNo}</td>
				<td>${sw.consumed}</td>
				<td>${sw.store}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div id="fragment-2">
<table class="return">
	<thead>
		<tr>
			<th>No</th>
			<th><spring:message code="pharmacymanagement.drug"/></th>
			<th><spring:message code="pharmacymanagement.expDate"/></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${dpList}" var="dp" varStatus="num">
			<tr>
				<td>${num.count}</td>
				<td>${dp.drugId.name}</td>
				<td>${dp.expiryDate }</td>
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