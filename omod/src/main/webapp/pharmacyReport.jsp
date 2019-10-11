<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="View Pharmacy report"
	otherwise="/login.htm"
	redirect="/module/pharmacymanagement/weeklyreport.form" />

<div>
<div id="outer"><%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle"><%@ include
	file="template/dispensingLocalHeader.jsp"%> 
<script type="text/javascript">

	var $ = jQuery.noConflict();
	
	function addSevenDaysToTo() {
		
		var fromVal = $("#fromId").val();
		
		var arr = new Array();
		arr = fromVal.split('/');
		var dat = new Date(arr[2], arr[1] - 1, arr[0]);
		dat.setDate(dat.getDate() + 7);

		var day = dat.getDate();		
		var d = day<10?"0"+day:day;
		
		var month = dat.getMonth() + 1;
		var m = month<10?"0"+month:month;
		
		var year = dat.getYear() + 1900;

		var upToSevenDays = d +"/"+ m + "/"+year;
		
		$("#toId").val(upToSevenDays);
		$("#toId").attr("disabled", "disabled");
	}
function subSevenDaysToFrom() {
		
		var toVal = $("#toId").val();
		
		var arr = new Array();
		arr = toVal.split('/');
		var dat = new Date(arr[2], arr[1] - 1, arr[0]);
		dat.setDate(dat.getDate() - 7);

		var day = dat.getDate();		
		var d = day<10?"0"+day:day;
		
		var month = dat.getMonth() + 1;
		var m = month<10?"0"+month:month;
		
		var year = dat.getYear() + 1900;

		var upToSevenDays = d +"/"+ m + "/"+year;

		$("#fromId").val(upToSevenDays);
		$("#fromId").attr("disabled", "disabled");
}

	$(document).ready( function() {
		$('#example').dataTable();
		$('.send').click(function() {
			if ($('#fromId').is(':disabled')){
				$('#fromId').removeAttr("disabled");
			}
			if ($('#toId').is(':disabled')){
				$('#toId').removeAttr("disabled");
			}			
		});
		$("#print_button").click(function(){		
			$("#example_length").hide();
			$("#example_filter").hide();
			$("#example_info").hide();
			$("#example_paginate").hide();
			$(".printArea").printArea();
			$("#example_length").show();
			$("#example_filter").show();
			$("#example_info").show();
			$("#example_paginate").show();
		});		
	});
</script>

<fieldset><legend><spring:message
	code="pharmacymanagement.pharmacyReport" /></legend>
<form method="post">
<table>
	<tr>
		<td><spring:message code="pharmacymanagement.pharmacy" /></td>
		<td>: 
			<select name="pharmacyId">
				<option value="">All Pharmacy</option>
				<c:forEach items="${pharmacyList}" var="pharmacy">
					<option value="${pharmacy.pharmacyId}" ${pharmacy.pharmacyId eq pharmacyId ? "selected='selected'" : "" }>${pharmacy.name}</option>
				</c:forEach>
			</select></td>
		<td><spring:message code="pharmacymanagement.from" /></td>
		<td>: <input name="from" id="fromId" type="text" onfocus="showCalendar(this);" size="10" value="${from}"/></td>
		<td><spring:message code="pharmacymanagement.to" /></td>
		<td>: <input name="to" id="toId" type="text" onfocus="showCalendar(this)" size="10" value="${to}" /></td>
	</tr>
	<tr>
		<td><input type="submit" value="Find" class="send" /></td>
	</tr>
</table>
</form>
</fieldset>

<hr />
<br />
<c:if test="${!empty consommationMap}">
<img id="print_button" src="${pageContext.request.contextPath}/images/printer.gif" style="cursor: pointer;" />
</c:if>
<br />
<br />
<div  class="printArea">
<h3><c:out value="${fromto}" /></h3>
	<div id="dt_example">
	<div id="container">
	<table cellpadding="0" cellspacing="0" border="0" class="display"
		id="example">
		<thead>
			<tr>
				<th>N<sup>o</sup></th>
				<th><spring:message code="pharmacymanagement.drug" /></th>
				<th><spring:message code="pharmacymanagement.units" /></th>
				<th><spring:message code="pharmacymanagement.storeQntyFist" /></th>
				<th><spring:message code="pharmacymanagement.qntyRecDurRep" /></th>
				<th><spring:message code="pharmacymanagement.qntyDispDurRep" /></th>
				<th><spring:message code="pharmacymanagement.storeQntyLastDay" /></th>
			</tr>
		</thead>
		<tbody id="weeklyReprtId">
			<c:forEach var="consommation" items="${consommationMap}" varStatus="num">
				<tr>
				<td>${num.count}.</td>
				<td>${consommation.value.drugName}</td>
				<td>${consommation.value.conditUnitaire}</td>
				<td>${consommation.value.qntPremJour}</td>
				<td>${consommation.value.qntRecuMens}</td>
				<td>${consommation.value.qntConsomMens}</td>
				<td>${consommation.value.qntRestMens}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	</div>
</div>
</div>
<div style="clear: both;"></div>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>