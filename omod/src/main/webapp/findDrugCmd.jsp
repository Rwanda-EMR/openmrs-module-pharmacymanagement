<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="View Drug Store management" otherwise="/login.htm" redirect="/module/pharmacymanagement/cmdsearch.form"/>

<div>

<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle">

<%@ include file="template/localHeader.jsp"%>

<!--  
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/dataentrystyle.css" />
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/validator.js" />
-->

<script type="text/javascript">
var $ = jQuery.noConflict();
	$(document).ready( function() {
		$('#example').dataTable();
	});
</script>

<fieldset><legend><spring:message code="pharmacymanagement.findCmd" /></legend>
<form id="searchform" method="post" action="cmdsearch.form?on=true">
<table>
	<tr>
		<td><spring:message code="pharmacymanagement.fosa" /></td>
		<td>
		<input type="hidden" name="fosaName" value="${dftLoc.locationId}" />
		<input type="text" name="fosaNameTxt" value="${dftLoc.name}" readonly="readonly" />
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
					<!-- <td>${order.monthPeriod}</td> -->
					<td><openmrs:formatDate date="${order.monthPeriod}" type="textbox" /></td>
					<td><a
						href="${pageContext.request.contextPath}/module/pharmacymanagement/order.list?orderId=${order.cmddrugId}">
					${order.isAchieved == true ? 'Completed' : 'Incomplete'} </a></td>
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
