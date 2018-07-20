<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacymanagement/pharmacy.form"/>

<div>

<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle">

<%@ include file="template/adminLocalHeader.jsp"%>

<c:set var="locStrLength" value="${location.name}"/>
<br />
<br />
<form method="post" action="pharmacy.form">
<table>
<tr>
<td><spring:message code="pharmacymanagement.pharmacy" /></td>
<td>:
<input type="hidden" name="pharmacyId" value="${phcy.pharmacyId > 0 ? phcy.pharmacyId : ''}" />
<input name="phcyName" value="${phcy.name}" />

<!-- 
<select name="phcyName">
	<option value="">-- Service --</option>
	<c:forEach var="service" items="${services}">
		<option value="${service.answerConcept.name.name}" ${service.answerConcept.name.name eq phcy.name ? 'selected="selected"' : '' }>${service.answerConcept.name.name}</option>
	</c:forEach>
</select>
 -->
 
</td>
</tr>
<tr>
<td><spring:message code="pharmacymanagement.location" /></td>
<td>: 
<input type="hidden" name="location" value="${location.locationId}" />
<input type="text" name="loc" disabled="disabled" value="${location.name}" size="${fn:length(locStrLength) + 2}" />
</td>
</tr>
<tr>
<td><input type="submit" value="Submit" class="send" /></td>
</tr>
</table>
</form>
</div>

<div style="clear: both;"></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>