<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>


<div id="consDispRem">
<table width="98%" class="return">
				<thead>
					<tr>
						<th><spring:message code="Date"/></th>	
						<th><spring:message code="Consommable" /></th>
						<th><spring:message code="Quantity"/></th>
						<th><spring:message code="Service"/></th>
						<th><spring:message code="Patient"/></th>
					</tr>
				</thead>
				<tbody>
						<tr>
							<td>
								<input type="text" name="date" value="<openmrs:formatDate date="${cd.date}" type="textbox" />" size="12" /> (dd/mm/yyyy)
								<input type="hidden" name="piId" />
							</td>
							<td  style="min-width: 300px; max-width: 300px; width : 300px;">
								<select style="min-width: 300px; max-width: 300px; width : 300px;" name="consumable" id="dpId">
									<option value="">-- consumable --</option>
									<c:forEach var="cons" items="${map}">
										<option value="${cons.value.drugproductId}" ${cons.value.conceptId.conceptId eq cd.drugproductId.conceptId.conceptId ? 'selected="selected"' : '' }>${cons.value.conceptId.name.name}</option>
									</c:forEach>
								</select>
							</td>
							<td><input type="text" name="qnty" size="5" disabled="disabled" value="${cd.qnty}" /></td>
							<td>
								<select name="service" id="serviceId">
									<option value="">-- Service --</option>
									<c:forEach var="service" items="${services}">
										<option value="${service.answerConcept.conceptId}" ${service.answerConcept.conceptId eq cd.service.conceptId ? 'selected="selected"' : '' }>${service.answerConcept.name.name}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<openmrs:fieldGen type="org.openmrs.Patient" formFieldName="patientId" val="${cd.patientId}" />
							</td>
						</tr>
				</tbody>
			</table>
</div>

<!-- the div that will be taken to the update of the requisiton form -->
<div id="cmdUpdate">
<form method="post" action="cmdUpdate.form?on=true">
<table>
	<tr>
		<td>To</td>
		<td><input type="hidden" value="${drugproduct.drugproductId}" name="dpId" />
			<select name="location">
				<option>-- Location --</option>
				<c:forEach var="location" items="${locations}" varStatus="num">
					<option value="${location.locationId}" ${location.locationId eq drugproduct.cmddrugId.destination.locationId ? 'selected=selected' : ''}>${location.name}</option>
				</c:forEach>
			</select></td>
	</tr>
	<tr>
		<td>Supporting Program</td>
		<td><input type="text" name="supportingProgram" value="${drugproduct.cmddrugId.supportingProg}" /></td>
	</tr>
	<tr>
		<td>Requisition date</td>
		<td><input type="text" name="reqDate" value="<openmrs:formatDate date="${drugproduct.cmddrugId.monthPeriod}" type="textbox" />" /></td>
	</tr>
	<tr>
		<td>${not empty drugs ? 'Drugs' : 'Consumable'}</td>
		<td><select name="drugCons">
			<option>-- Drug/Consumable --</option>
			<c:if test="${not empty drugs}">
				<c:forEach var="drug" items="${drugs}" varStatus="num">
					<option value="${drug.drugId}" ${drug.drugId eq drugproduct.drugId.drugId ? 'selected=selected' : '' }>${drug.name}</option>
				</c:forEach>
			</c:if>
			<c:if test="${not empty consumable}">
				<c:forEach var="cons" items="${consumable}" varStatus="num">
					<option value="${cons.answerConcept.conceptId}" ${cons.answerConcept.conceptId eq drugproduct.conceptId.conceptId ? 'selected=selected' : '' }>${cons.answerConcept.name.name}</option>
				</c:forEach>
			</c:if>
		</select></td>
	</tr>
	<tr>
		<td>Quantity Requested</td>
		<td><input type="text" name="qntyReq" value="${drugproduct.qntyReq}" size="5" /></td>
	</tr>
	<!-- 
	<tr>
		<td>Quantity Received</td>
		<td><input type="text" name="qtyRec" value="${drugproduct.deliveredQnty}" size="5" /></td>
	</tr> -->
	<tr> 
		<td><input type="submit" value="update" id="cmdUpdateBtn" /></td>
	</tr>
</table>
</form>
</div>

<!-- the div that will be taken to the update of the returned drug -->
<div id="returnUpdate"></div>

<%@ include file="/WEB-INF/template/footer.jsp"%>