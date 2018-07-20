<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacymanagement/pharmacyorder.list"/>

<div>

<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle">

<%@ include file="template/dispensingLocalHeader.jsp"%>


<!--  
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/demo_page.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/demo_table.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.dataTables.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/dataentrystyle.css" />
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
-->

<script type="text/javascript">
var $ = jQuery.noConflict();
	$(document).ready( function() {
		$('#example').dataTable();
	});
</script>


<fieldset><legend><spring:message code="pharmacymanagement.findPharmaCmd" /></legend>
<form id="searchform" method="post" action="pharmacyorder.list?on=true">
<table>
	<tr>
		<td><spring:message code="pharmacymanagement.pharmacy" /></td>
		<td>
		<select name="pharmacyId">
			<option value="">-- select --</option>
			<c:forEach var="pharmacy" items="${pharmacies}">
				<option value="${pharmacy.pharmacyId}">${pharmacy.name}</option>
			</c:forEach>			
		</select>
		</td>
	</tr>
	<tr>
		<td><spring:message code="pharmacymanagement.supportProg" /></td>
		<td><input type="text" name="supporter" /></td>
	</tr>
	<tr>
		<td><spring:message code="pharmacymanagement.month" /></td>
		<td><input type="text" name="month" onfocus="showCalendar(this)" size="10" /></td>
	</tr>
	<tr>
		<td colspan="2"><input type="submit" value="Find" class="send" /></td>
	</tr>
</table>
</form>
</fieldset>

<div style="border: 1px solid black"></div>
<c:if test="${!(orders eq null)}">
	<div id="dt_example">
	<div id="container">
	<table cellpadding="0" cellspacing="0" border="0" class="display"
		id="example">
		<thead>
			<tr>
				<th>No</th>
				<th><spring:message code="pharmacymanagement.fosaPharma" /></th>
				<th><spring:message code="pharmacymanagement.supportProg" /></th>
				<th><spring:message code="pharmacymanagement.month" /></th>
				<th><spring:message code="pharmacymanagement.status" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${orders}" var="order" varStatus="num">
				<tr>
					<td>${num.count}.</td>
					<td>${order.locationId.locationId == null ? order.pharmacy.name : order.locationId.name}</td>
					<td>${order.supportingProg}</td>
					<td>${order.monthPeriod}</td>
					<td>${order.isAchieved == true ? 'Completed' : 'Incomplete'}</td>
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