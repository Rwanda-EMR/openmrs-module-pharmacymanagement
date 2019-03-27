<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<div id="ordersId">
<table class="return">
<c:if test="${!empty dlds}">
	<thead>
		<tr>
			<th width="30%"><spring:message code="pharmacymanagement.designation" /></th>
			<th width="10%">Orderer</th>
			<th width="10%">Start date</th>
			<th width="10%"><spring:message code="pharmacymanagement.qntyReq" /></th>
			<th width="10%">Frequency</th>
			<th width="30%">Lot No / Solde (Expiration Date)</th>
		</tr>
	</thead>
</c:if>
	<tbody>
		<c:if test="${!empty dlds}">
			<c:forEach var="dld" items="${dlds}"  varStatus="num">
				<tr>
					<td width="30%">${num.count}. ${dld.drugOrder.drug.name}</td>
					<td width="10%">${dld.drugOrder.orderer.person.familyName} ${dld.drugOrder.orderer.person.givenName}</td>
					<td width="10%"><openmrs:formatDate date="${dld.drugOrder.startDate}" type="textbox" /></td>

					<td width="10%">
						<input type="hidden" name="do_${num.count}" value="${dld.drugOrder.orderId}" />
						<input type="text" name="drug_${num.count}_${dld.drugOrder.drug.drugId}" value="${dld.drugOrder.quantity}" size="5" />
					</td>
					<td width="10%"><input type="text" value="${dld.drugOrder.frequency}" size="6" /></td>
					<td width="30%">
						<select name="dp_${num.count}" id="dpId_${num.count}" class="dpClass">							
							<option value=""><center>---</center></option>
							<c:forEach items="${dld.dpMap}" var="dp">
								<option value="${dp.value}">${dp.key}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="6"><hr /></td>
				</tr>
			</c:forEach>
		</c:if>

		<c:if test="${empty availNotAvailOrderedDrug}">
        		<tr>
        			<td colspan='6' width='100%'><input type="hidden" name="hideBtn" /><p style="font-family: 'Comic sans serif'; color: red;"><spring:message code="pharmacymanagement.noRegimenOrderedMsg" /></p></td>
        		</tr>
        </c:if>

		<c:if test="${!empty availNotAvailOrderedDrug}">
        		<tr>
        			<td colspan='6' width='100%'><input type="hidden" name="hideBtn" /><p style="font-family: 'Comic sans serif'; color: red;"><spring:message code="pharmacymanagement.drugOrderedMsg" /></p></td>
        		</tr>

        		<tr>
                        			<td colspan='6' width='100%'><p style="font-family: 'Comic sans serif';">
                        			<c:forEach var="type" items="${availNotAvailOrderedDrug}">

                        			        <c:choose>
                                                <c:when test="${type.value eq 'Available'}">
                                                    ${type.key} : ${type.value} <br />
                                                </c:when>
                                                <c:otherwise>
                                                   <font color='red'> ${type.key} : ${type.value}</font> <br />
                                                </c:otherwise>
                                            </c:choose>

                                    </c:forEach>

                        			</p></td>
                </tr>

        </c:if>


	</tbody>
</table>
</div>
<div id="lots">
<c:if test="${!empty lots}">
	<select name="prodFromLot" id="selectLotId">
		<option value="">---</option>
		<c:forEach items="${lots}" var="lot">
			<option value="${lot[2]}">${lot[0]}</option>
		</c:forEach>
	</select>
</c:if>
<c:if test="${empty lots}">
	<div class="error">The Drug is not in store yet</div>
</c:if>
</div>

<c:if test="${!empty date}">
<div id="expDate">
	<input name="expDate" value="${date}" size="11" readonly="readonly" />
</div>
</c:if>
<c:if test="${empty date}">
<div id="expDate">
	<div class="error">No expiration date</div>
</div>
</c:if>