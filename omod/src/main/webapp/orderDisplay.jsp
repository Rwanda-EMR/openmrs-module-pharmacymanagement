<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="View Drug Store management" otherwise="/login.htm" redirect="/module/pharmacymanagement/order.list"/>


<div>

<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle">

<%@ include file="template/localHeader.jsp"%>
<script type="text/javascript">
var $ = jQuery.noConflict();
$(document).ready( function() {
	$('.cmts').hide();
	      
	$(".basic").click(
			function() {
				var target = this.id;
				$("#ordre").attr("value", target);
				<c:if test="${cmdDrug.locationId.locationId != dftLoc.locationId}">
					$("#lotNoId").empty().html('Loading...');
					$("#lotNoId").load("patOrders.list?drugproductId="+target+" #lots");
				</c:if>
	});

	$("#lotNoId").change(function() {
		var selectId = $("#selectLotId").val();
		<c:if test="${cmdDrug.locationId.locationId == null}">
			$("#expirationId").empty().html('Loading...');
			$("#expirationId").load("patOrders.list?dpFromGet="+selectId+" #expDate");
		</c:if>
	});

	//jquery to print the div with printArea as class
	$("#print_button").click(function(){
		$('.cmts').show();
		$(".printArea").printArea();
		$('.cmts').hide();
	});

	$("#printSelect").change(function() {
		var print = $("#printSelect").val();
		if(print == 1) {
			$('.cmts').show();
			$(".printArea").printArea();
			$('.cmts').hide();
		} 
		if (print == 2) {
			$('.cmts').show();
			
			//removing the colspan attributes
			$('.doubles').attr('colspan', '');
			$('.triples').attr('colspan', '');
			$('.quintuples').attr('colspan', '');

			//adding the colspan attributes
			//$('.simpleSpan').attr('colspan', '');
			
			$('.simple').hide();
			$(".printArea").printArea();
			$('.simple').show();

			//removing the colspan attributes
			$('.simpleSpan').attr('colspan', '');

			//restoring the colspan
			$('.doubles').attr('colspan', '2');
			$('.triples').attr('colspan', '3');
			$('.quintuples').attr('colspan', '5');
			//$('#qtyReceivedDuringMonthId').attr('colspan', '');
			//$('#qtyReceivedId').attr('colspan','');
			$('.cmts').hide();
		}		
	});
	
	$('.edit-basic').click(function () {
		var currId = this.id;
		var arrDrug = new Array();
		var drugString = "<c:out value="${drugs}" />";
		arrDrug = drugString.split(",");

		var arrCons = new Array();
		var consString = "<c:out value="${consumerList}" />";
		arrCons = consString.split(",");

		var arrLoc = new Array();
		var locString = "<c:out value="${locations}" />";
		arrLoc = locString.split(",");
		
		var drug = $(this).parents('tr').children("td:nth-child(2)").text();
		var qtyRec = $(this).parents('tr').children("td:nth-child(14)").text();
		var month = $('#month').text();
		var supProg = $('#supProg').text();
		var loc = $('#loc').text();
		var drugId = null;
		var drugName = null;
		var currIdSplit = new Array();
		currIdSplit = currId.split("_");
		$("#edit-modal-content").load("cmdUpdate.form?dpId=" + currIdSplit[1] + " #cmdUpdate", function() {
			$("#cmdUpdateBtn").click(function() {
				alert("Updated Successfully");
			});
		});
	});

});


</script>
<select id="printSelect">
	<option value="">--Print--</option>
	<option value="1">Full Print</option>
	<option value="2">Simple Print</option>
</select>
<!-- <img id="print_button" src="${pageContext.request.contextPath}/images/printer.gif" style="cursor: pointer;" /> -->

<div  class="printArea">
<div
	style="border: 2px solid black; margin-left: 15%; margin-right: 15%; text-align: center">
<h3><spring:message code="pharmacymanagement.reqForm" /></h3>
</div>
<br />
<br />

<table width="100%" style="">
	<tr>
		<td width="15%"><spring:message code="pharmacymanagement.fosaPharma" />:</td>
		<td width="25%" id="loc"> ${cmdDrug.locationId.locationId == null ? cmdDrug.pharmacy.name : cmdDrug.locationId.name}</td>
		<td width="20%">&nbsp;</td>
		<td width="15%"><spring:message code="pharmacymanagement.supportProg" />:</td>
		<td width="25%" id="supProg"> ${cmdDrug.supportingProg}</td>
		<td width="20%">&nbsp;</td>
	</tr>
	<tr>
		<td width="15%"><spring:message code="pharmacymanagement.district" />:</td>
		<td width="25%"> ${cmdDrug.locationId.locationId == null ? cmdDrug.pharmacy.locationId.countyDistrict : cmdDrug.locationId.countyDistrict}</td>
		<td width="20%">&nbsp;</td>
		<td width="15%"><spring:message code="pharmacymanagement.month" />:</td>
		<td width="25%" id="month"> ${cmdDrug.monthPeriod}</td>
		<td width="20%">&nbsp;</td>
	</tr>
	<tr>
		<td><spring:message code="pharmacymanagement.province" />:</td>
		<td> ${cmdDrug.locationId.stateProvince}</td>
	</tr>
</table>
<br />
<br />

 <div>
<table border="1" style="font-size: 9px; font-family: 'Times New Roman'; text-align: center;" cellpadding="0" cellspacing="0">
<tr>
    <td class="non-printable" rowspan="2"><spring:message code="Edit" /></td>
	<td rowspan="2"><spring:message code="pharmacymanagement.productName" /></td>
	<td rowspan="2" style="width:100px"><spring:message code="pharmacymanagement.unit" /></td>
	<td style="width:100px" class="simple"><spring:message code="pharmacymanagement.stockOnHandAtBeginning" /></td>
	<td id="qtyReceivedDuringMonthId" style="width:100px" class="simpleSpan"><spring:message code="pharmacymanagement.qtyReceivedDuringMonth" /></td>
	<td style="width:100px" class="simple"><spring:message code="pharmacymanagement.qtyDispDuringMonth" /></td>
	<td colspan="2" style="width:100px" class="simple"><spring:message code="pharmacymanagement.lossesAndAdjustment" /></td>
	<td style="width:100px" class="simple"><spring:message code="pharmacymanagement.storeQntyLastDay" /></td>
	<td style="width:100px" class="simple"><spring:message code="pharmacymanagement.stockOutDaysDuringMonth" /></td>
	<td style="width:100px" class="simple"><spring:message code="pharmacymanagement.adjMonthlyConsumption" /></td>
	<td style="width:100px" class="simple"><spring:message code="pharmacymanagement.maxQty" /></td>
	<td style="width:100px" class="simple"><spring:message code="pharmacymanagement.qty2Order" /></td>
	<td id="qtyReceivedId" style="width:100px" class="simpleSpan"><spring:message code="pharmacymanagement.qtyReceived" /></td>
	<td rowspan="2" style="width:100px" class="simpleRSpan"><spring:message code="Requested Quantity" /></td>
	<td rowspan="2" style="width:100px" class="simpleRSpan">Requested By </td>
	<td rowspan="2" style="width:100px" class="simpleRSpan">Approved By </td>


	<td rowspan="2" style="width:100px" class="simpleRSpan">Reason</td>
    <td class="non-printable" rowspan="2"><spring:message code="pharmacymanagement.status" /></td>
</tr>
<tr>
    <td class="simple">A</td>
    <td class="simple">B</td>
    <td class="simple">C</td>
    <td class="simple">D-</td>
    <td class="simple">D+</td>
    <td class="simple">E</td>
    <td class="simple">F</td>
    <td class="simple">G</td>
    <td class="simple">H</td>
    <td class="simple">I</td>
    <td class="simple">J</td>
  </tr>
<tr>
<td colspan="21" style="background-color: #c0c0c0"><center><spring:message code="pharmacymanagement.drug" />s ${empty drugMap ? '(no record)' : '' }</center></td>
</tr>
<c:forEach var="consommation" items="${drugMap}" varStatus="num">
<c:if test="${consomation.value.drugProduct.conceptId.conceptId == null}">
<c:set var="counter" value="${consommation.value.drugProduct.drugproductId}" />
<tr class="tr_${counter}">
   	<td class="non-printable">
   		<input type="hidden" value="${counter}" id="prodId_${counter}" class="id" name="${counter}" />
		<c:if test="${consommation.value.drugProduct.isDelivered}">
		<div style="display : none">
		<center><img id="drug_${counter}" class="edit-basic" src="${pageContext.request.contextPath}/images/edit.gif" style="cursor: pointer;" /></center>
	    </div>
	    </c:if>

	    <c:if test="${!consommation.value.drugProduct.isDelivered}">
        		<center><img id="drug_${counter}" class="edit-basic" src="${pageContext.request.contextPath}/images/edit.gif" style="cursor: pointer;" /></center>
        </c:if>

	</td>
   <td><a href="${pageContext.request.contextPath}/module/pharmacymanagement/stocksecurity.list?drugId=${consommation.value.drugId}&locationId=${consommation.value.locationId}">${consommation.value.drugName}</a></td>
    <td>${consommation.value.conditUnitaire}&nbsp;</td>
    <td class="simple">${consommation.value.qntPremJour}&nbsp;</td>
   <td class="simpleSpan">${consommation.value.qntRecuMens}&nbsp;</td>
   <td class="simple">${consommation.value.qntConsomMens}&nbsp;</td>
    <td class="simple">${consommation.value.returnedProduct}</td>
    <td class="simple">-</td>
    <td class="simple">${consommation.value.qntRestMens}&nbsp;</td>
    <td class="simple">${consommation.value.stockOut}</td>
    <td class="simple">${consommation.value.adjustMonthlyConsumption}</td>
    <td class="simple">${consommation.value.maxQnty}</td>
    <td class="simple">${consommation.value.qntyToOrder}&nbsp;</td>
    <td class="simpleSpan">${consommation.value.drugProduct.isDelivered == false ? '' : consommation.value.drugProduct.deliveredQnty}&nbsp;</td>
    <td class="simpleSpan">${consommation.value.drugProduct.qntyReq}  ${consommation.value.drugProduct.lotNo}</td>
    <td class="simpleSpan">${consommation.value.drugProduct.requestedBy.names}&nbsp;</td>
    <td class="simpleSpan">${consommation.value.drugProduct.transfereBy.names}&nbsp;</td>
    <td class="simpleSpan">${consommation.value.drugProduct.comments}&nbsp;</td>

    <td class="non-printable">
    	<c:if test="${!consommation.value.drugProduct.isDelivered}">
			<center><img id="${counter}" class="basic" src="${pageContext.request.contextPath}/images/problem.gif" style="cursor: pointer;"></center>
		</c:if>
		<c:if test="${consommation.value.drugProduct.isDelivered}">
			<center><img src="${pageContext.request.contextPath}/images/checkmark.png" /></center>
		</c:if>
	</td>
  </tr>
  </c:if>
  </c:forEach>

<tr>
<td colspan="21" style="background-color: #c0c0c0"><center><spring:message code="pharmacymanagement.consumable" /> ${empty consommationMap ? '(no record)' : '' }</center></td>
</tr>
<c:forEach var="consommation" items="${consommationMap}" varStatus="num">
<c:if test="${consomation.value.drugProduct.conceptId.conceptId == null}">
<c:set var="counter" value="${consommation.value.drugProduct.drugproductId}" />
<tr>
   	<td class="non-printable">
   		<input type="hidden" value="${counter}" id="prodId_${counter}" class="id" name="${counter}" />
		<center><img id="cons_${counter}" class="edit-basic" src="${pageContext.request.contextPath}/images/edit.gif" style="cursor: pointer;" /></center>
	</td>
   <td><a href="${pageContext.request.contextPath}/module/pharmacymanagement/stocksecurity.list?conceptId=${consommation.value.conceptId}&locationId=${consommation.value.locationId}">${consommation.value.drugName}</a></td>
    <td>${consommation.value.conditUnitaire}&nbsp;</td>
    <td class="simple">${consommation.value.qntPremJour}&nbsp;</td>
   <td class="simpleSpan">${consommation.value.qntRecuMens}&nbsp;</td>
   <td class="simple">${consommation.value.qntConsomMens}&nbsp;</td>
    <td class="simple">${consommation.value.returnedProduct}</td>
    <td class="simple">-</td>
    <td class="simple">${consommation.value.qntRestMens}&nbsp;</td>
    <td class="simple">${consommation.value.stockOut}</td>
    <td class="simple">${consommation.value.adjustMonthlyConsumption}</td>
    <td class="simple">${consommation.value.maxQnty}</td>
    <td class="simple">${consommation.value.qntyToOrder}&nbsp;</td>
    <td class="simpleSpan">${consommation.value.drugProduct.isDelivered == false ? '' : consommation.value.drugProduct.deliveredQnty}&nbsp;</td>
    <td class="simpleSpan">${consommation.value.drugProduct.qntyReq}&nbsp; ${consommation.value.drugProduct.lotNo}</td>
    <td class="simpleSpan">${consommation.value.drugProduct.requestedBy.names}&nbsp;</td>
    <td class="simpleSpan">${consommation.value.drugProduct.transfereBy.names}&nbsp;</td>
     <td class="simpleSpan">${consommation.value.drugProduct.comments}&nbsp;</td>

    <td class="non-printable">

    	<c:if test="${!consommation.value.drugProduct.isDelivered}">
			<center><img id="${counter}" class="basic" src="${pageContext.request.contextPath}/images/problem.gif" style="cursor: pointer;" /></center>
		</c:if>
		<c:if test="${consommation.value.drugProduct.isDelivered}">
			<center><img src="${pageContext.request.contextPath}/images/checkmark.png" /></center>
		</c:if>
	</td>
  </tr>
  </c:if>
  </c:forEach>
  
<tr class="cmts">
<td colspan="15" style="background-color: #c0c0c0"><center>OBSERVATIONS</center></td>
</tr>
<tr class="cmts">
	<td>&nbsp;</td><td colspan="2" class="doubles">NAMES</td><td colspan="2" class="doubles">TITLE</td><td colspan="2" class="doubles">TELEPHONE NUMBER</td><td colspan="3" class="triples">DATE</td><td colspan="5" class="quintuples">SIGNATURE</td>
</tr>
<tr class="cmts">
	<td>PREPARED BY (DP)</td><td colspan="2" class="doubles">&nbsp;</td><td colspan="2" class="doubles">&nbsp;</td><td colspan="2" class="doubles">&nbsp;</td><td colspan="3" class="triples">&nbsp;</td><td colspan="5" class="quintuples">&nbsp;</td>
</tr>
<tr class="cmts">
	<td>APPROVED BY (DP)</td><td colspan="2" class="doubles">&nbsp;</td><td colspan="2" class="doubles">&nbsp;</td><td colspan="2" class="doubles">&nbsp;</td><td colspan="3" class="triples">&nbsp;</td><td colspan="5" class="quintuples">&nbsp;</td>
</tr>
<tr class="cmts">
	<td>RECEIVED BY (CL)</td><td colspan="2" class="doubles">&nbsp;</td><td colspan="2" class="doubles">&nbsp;</td><td colspan="2" class="doubles">&nbsp;</td><td colspan="3" class="triples">&nbsp;</td><td colspan="5" class="quintuples">&nbsp;</td>
</tr>
</table>
</div>
</div>


<!-- update order modal -->
<div id="basic-modal-content">
<form method="post" action="order.list?orderId=${cmdDrug.cmddrugId}"  >
<input type="hidden" id="ordre"  name="ordre" size="5" />
<input type="hidden" name="pharmacyId" value="${cmdDrug.pharmacy.pharmacyId}" id="pharmacyId" />
<table>
	<tr>
		<td><spring:message code="pharmacymanagement.inventoryDate" /></td>
		<td><input type="hidden" name="now" id="nowId" value="<openmrs:formatDate date="${now}" type='textbox' />" />
			<input type="text" name="invDate" id="invDateId" onchange="CompareDates('<openmrs:datePattern />');"
				onfocus="showCalendar(this)" class="date" size="11" required /><span id="msgId"></span></td>
	</tr>
	
	<tr>
		<td><spring:message code="pharmacymanagement.givenQnty" /></td>
		<td><input type="text" name="qntAcc" size="5"  required /></td>
	</tr>
	
	<tr>
		<td><spring:message code="pharmacymanagement.lotNr" /></td>
		<td><div id="lotNoId"><input type="text" name="noLotStock" required /></div>
			
		</td>
	</tr>
	
	<tr>
		<td><spring:message code="pharmacymanagement.expDate" /></td>
		<td><div id="expirationId"><input type="text" name="expDate" onfocus="showCalendar(this)" class="date"  required size="11"  r/></div></td>
	</tr>

	<tr>
		<td><input type="submit" value="Update" class="send" /></td>
	</tr>
</table>

</form>
</div>

		<div id="edit-modal-content">
			<!-- update command -->
			Loading...
		</div>
	</div>

<div style="clear: both;"></div>
</div>
				
<%@ include file="/WEB-INF/template/footer.jsp"%>