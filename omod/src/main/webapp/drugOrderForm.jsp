<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacymanagement/drugDetail.form"/>


<div>

<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>

<div id="middle">
<%@ include file="template/dispensingLocalHeader.jsp"%>


<form class="box" method="post" style="background-color: #DCEEF9">
<table>

	<tr>
		<td><spring:message code="pharmacymanagement.pharmacy"/></td>
		<td>
		<select	name="pharmacyId" id="fosa">
			<optgroup label="---">
			<option>---</option>
			</optgroup>
			<c:forEach var="location" items="${locations}">

				<optgroup label="---${location.name}---">
					<c:forEach var="phcy" items="${pharmacies}">

						<c:if test="${location.locationId eq phcy.locationId.locationId}">
							<option value="${phcy.pharmacyId}">${phcy.name}</option>
						</c:if>

					</c:forEach>
				</optgroup>

			</c:forEach>
		</select>
		</td>
	</tr>
	

	<tr>
		<td><spring:message code="pharmacymanagement.patient"/></td>
		<td><input type="hidden" name="dopId" value="${dop.drugOrderPrescriptionId}" /> 
			<openmrs:fieldGen type="org.openmrs.Patient"
					formFieldName="patientId" val="${dop.patient}" />
		</td>
	</tr>

	<tr>
		<td><spring:message code="pharmacymanagement.pharmacist"/></td>
		<td><openmrs:fieldGen type="org.openmrs.User" formFieldName="userId" val="${dop.user}" /></td>
	</tr>

	<tr>
		<td><spring:message code="pharmacymanagement.drug"/></td>
		<td colspan="5">
		<openmrs:fieldGen type="org.openmrs.Drug" formFieldName="drugId" val="${drug}" /></td>
	</tr>
 
	<tr>
		<td>Quantity</td>
		<td><input type="text" name="quantity" size="3" value="${dop.quantity}" /></td>
	</tr>
 
	<tr>
		<td><spring:message code="pharmacymanagement.date"/></td>
		<td><openmrs_tag:dateField formFieldName="date"
			startValue="${dop.date}" /></td>
	</tr>

	<tr>
		<td><input type="submit" value="Save" /></td>
	</tr>

</table>

</form>
</div>

<div style="clear: both;"></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>