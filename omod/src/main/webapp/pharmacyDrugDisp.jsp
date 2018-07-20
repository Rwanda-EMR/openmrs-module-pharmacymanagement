<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacymanagement/pharmacyDrugDisp.htm"/>

<script type="text/javascript">

var $dsm = jQuery.noConflict();

var pId=0;
var pattId = 0;
var cyId = 0;
var patientParam = null;
var serviceParam = null;

function printId() {	
	var hidId = document.getElementsByName("hideBtn")[0].value;
	var patiId = document.getElementsByName("patientId")[0].value;
	if(pattId != patiId) {
		pattId = patiId;
		if(pattId != 0){
		}
	}
			
	setTimeout(printId, 500);
}

function getHideVal() {
	var hideVal = document.getElementsByName("hideBtn");
	var patiId = document.getElementsByName("patientId");
	if(hideVal.length > 0) {
		if(patiId.length > 0) {
			hideVal[0].setAttribute("value", patiId[0].value);
		}
	} else {
		setTimeout(getHideVal,500);
	}
}


function ddd(){
	var patientId=document.getElementsByName("patientId")[0].value;
	if(pId!=patientId){
			pId=patientId;
			if(pId != 0) {
				//$dsm('#pharmacyId').val("").attr("selected","selected");
				$dsm('#pharmaRpt').empty();
				$dsm("#drugs").empty();
			}
	}
	setTimeout(ddd,500);
}

function mdd(){
	var object = document.getElementsByName("patientId");
	if(object.length > 0) {
		ddd();
	}
	else 
		{
		setTimeout(mdd,500);
	}
}

mdd();
getHideVal();

var fieldGroupCount = 0;
var drugArray = new Array();
var drugIdArray = new Array();
var appointmentId = "<c:out value="${appointmentId}"/>"
var patientId = "<c:out value="${patient.patientId}"/>";
<c:forEach var="drug" items="${drugs}">
	drugArray.push("<c:out value="${drug.name}"/>");
	drugIdArray.push("<c:out value="${drug.drugId}"/>");
</c:forEach>

var titleArray = new Array();
titleArray.push("Drugs");
titleArray.push("Quantity");

serviceParam = "<c:out value="${param.serviceId}"/>";
patientParam = "<c:out value="${param.patientId}"/>";

$dsm(document).ready(function() {	
	var serviceId = $dsm('#pharmacyId').val();
		$dsm('#pharmacyId').change(function() {
			var patId = $dsm("input[name='patientId']").val();
			var phcyId = $dsm("#pharmacyId").val();
			if(patId.length > 0) {
				$dsm("#drugs").empty().html("<center><img src='${pageContext.request.contextPath}/images/loading.gif' /></center>");
				$dsm("#drugs").load("patOrders.list?patientId="+patId+"&pharmacyId="+phcyId+" #ordersId");
				$dsm("#pharmaRpt").load("drugOrder.list?patientId="+patId+" #dt_example", function() {
					$dsm('#example').dataTable();
				});
			}
		});
		if(appointmentId != null && patientId != null) {
			var phcyId = $dsm("#pharmacyId").val();
			if(patientId.length > 0) {
				$dsm("#drugs").empty().html("<center><img src='${pageContext.request.contextPath}/images/loading.gif' /></center>");
				$dsm("#drugs").load("patOrders.list?patientId="+patientId+"&pharmacyId="+phcyId+" #ordersId");
				$dsm("#pharmaRpt").load("drugOrder.list?patientId="+patientId+" #dt_example", function() {
					$dsm('#example').dataTable();
				});
			}	
		}
});

</script>

<div>

<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle">

<%@ include file="template/dispensingLocalHeader.jsp"%>

<br />
<br />
<div class="boxHeader">
<center><b><spring:message code="Drug Dispensing" /></b></center>
</div>
<div class="daebox">
<form method="post" name="frm">
<table class="shadow">
	<thead>
		<tr class="adhere">
			<th><spring:message code="pharmacymanagement.patient" /></th>
			<th><spring:message code="pharmacymanagement.pharmacy" /></th>
			<th><spring:message code="pharmacymanagement.visitDate" /></th>
			<th><spring:message code="pharmacymanagement.weight" /></th>
			<th><spring:message code="pharmacymanagement.nextVisitDate" /></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>
				<openmrs:fieldGen type="org.openmrs.Patient" formFieldName="patientId" val="${patient}" />
			</td>
			<td>
				<select name="pharmacy" id="pharmacyId">
					<option value="">-- select --</option>
					<c:forEach var="phcy" items="${pharmacyList}">
						<option value="${phcy.pharmacyId}" ${phcy.pharmacyId eq 13 ? 'selected="selected"' : ''}>${phcy.name}</option>
					</c:forEach>
				</select></td>
			<td><input type="text" name="encDate" id="encDateId"
			onchange="CompareDates('<openmrs:datePattern />');"
			onfocus="showCalendar(this)" class="date" size="11" /><br /><span id="msgErrorId"></span></td>			
			<td><input type="text" name="weight" size="5" value="${obsWeight.valueNumeric}" /></td>
			<!-- <td><input type="text" name="remainingDrug" size="4" /></td> -->
			<td><openmrs_tag:dateField formFieldName="nvDate"
				startValue="${date}" /></td>
		</tr>
		<tr>
			<td colspan="8">
			<fieldset><legend><spring:message code="Drugs" /></legend>
			<div id="drugs"></div>
			</fieldset>
			</td>
		</tr>
		<tr>
			<td colspan="8"><input type="submit" value="Save" id="saveButton" /></td>
		</tr>
	</tbody>
</table>
</form>
</div>
<br />
<hr />
<br />

<div id="pharmaRpt">
</div>
</div>

<div style="clear: both;"></div>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>