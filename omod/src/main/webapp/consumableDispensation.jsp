<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacymanagement/consumabledispensation.htm"/>

<div>

<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>

<script	type="text/javascript">
	var $p = jQuery.noConflict();
	$p(document).ready( function() {
		
		$p('#example').dataTable();

		$p('#serviceId').change(function() {
			var concOption = '<option value="">-- Consumable --</option>';
			var serviceId = $p('#serviceId').val();
			$p.getJSON('json.htm?serviceId=' + serviceId, function(data) {
				for(var i in data) {
					concOption += '<option value="'+data[i].id+'">'+data[i].name+'(Solde:'+data[i].solde+'/LotNo:'+data[i].lotno+'/Exp:'+data[i].expiry+')</option>';
				}
				$p('#dpId').html(concOption);				
			});
		});
		/**
		$p(".trClass").click(function() {
			  //getting values from the table
			  var trId = $p(this).attr('id');
			  var providerId = trId.split('_')[1];
			  var date = $p(this).children("td:nth-child(2)").text();
			  var consumable = $p(this).children("td:nth-child(3)").text();
			  var quantity = $p(this).children("td:nth-child(4)").text();
			  var service = $p(this).children("td:nth-child(5)").text();
			  var provider = $p(this).children("td:nth-child(6)").text();
			  var proName = $p('input[name=provider]');

			  //populating the fields from table values
			  $p('input[name=piId]').val(trId);
			  $p('input[name=provider]').val(providerId);
			  proName.append('<span id="proName">'+provider+'</span>');
			  $p('input[name=date]').val(date);
			  $p("#dpId option[text=" + consumable +"]").attr("selected","selected");
			  $p('input[name=qnty]').val(quantity);
			  $p("#serviceId option[text=" + service +"]").attr("selected","selected");
			  $p("#providerId option[text=" + provider +"]").attr("selected","selected");
		}); 
		$p(".editConsDisp").click(function() {
			var consDispId = $(this).id;
			var consDisp = consDispId.split("_")[1];
			$p("#consDispForm").load("cmdUpdate.form?cdId="+consDisp+" #consDispRem");
		}); **/
	});
</script>


<div id="middle">
	<%@ include file="template/dispensingLocalHeader.jsp"%>
	
	<b class="boxHeader">Bon de Sortie</b>
	<div class="box">
		<form method="post" action="consumabledispensation.htm" >
			<div  id="consDispForm">
			<table width="98%" class="return">
				<thead>
					<tr>
						<th><spring:message code="Date"/></th>
						<th><spring:message code="Service"/></th>	
						<th><spring:message code="Consommable" /></th>
						<th><spring:message code="Quantity"/></th>
						<th><spring:message code="Patient"/></th>
					</tr>
				</thead>
				<tbody>
						<tr>
							<td>
								<input type="text" name="date" value="<openmrs:formatDate date="${cd.date}" type="textbox" />" onfocus="showCalendar(this)" size="10" style="min-width: 100px; max-width: 100px; width : 100px;" />
								<input type="hidden" name="cdId" value="${cd.consumabledispenseId}" />
							</td>
							<td>
								<select name="service" id="serviceId">
									<option value="">-- Service --</option>
									<c:forEach var="pharmacy" items="${pharmacies}">
										<option value="${pharmacy.pharmacyId}" ${pharmacy.pharmacyId eq cd.service.conceptId ? 'selected="selected"' : '' }>${pharmacy.name}</option>
									</c:forEach>
								</select>
							</td>
							<td style="min-width: 300px; max-width: 300px; width : 300px;">
								<select style="min-width: 300px; max-width: 300px; width : 300px;" name="consumable" id="dpId">
									<option value="">-- consumable --</option>
<!--
									<c:forEach var="cons" items="${map}">
										<option value="${cons.value.drugproductId}">${cons.value.conceptId.name.name}(${cons.value.lotNo})</option>
									</c:forEach>
-->
								</select>
							</td>
							<td><input type="text" name="qnty" size="5" value="${cd.qnty > 0 ? cd.qnty : ''}" /></td>							
							<td>
								<openmrs:fieldGen type="org.openmrs.Patient" formFieldName="patientId" val="${cd.patientId}" />
							</td>
						</tr>
				</tbody>
			</table>
			</div>
		<input type="submit" value="Save" />
		</form>
		
	</div>
		<br />
		<br />
		<br />
	<div id="dt_example">
	<div id="container">
	<table cellpadding="0" cellspacing="0" border="0" class="display"
		id="example">
		<thead>
			<tr>
				<th><spring:message code="No"/></th>
				<th><spring:message code="Date"/></th>	
				<th><spring:message code="Consumable" /></th>
				<th><spring:message code="Quantity"/></th>
				<th><spring:message code="Service"/></th>
				<th><spring:message code="Patient"/></th>
				<th><spring:message code="Edit"/></th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${consumableList}" var="cl" varStatus="num">
				<tr class="trClass" id="${cl.consumabledispenseId}_${cl.provider.userId}">
					<td>${num.count}.</td>
					<td><openmrs:formatDate date="${cl.date}" type="textbox" /></td>
					<td>${cl.drugproductId.conceptId.name.name}</td>
					<td>${cl.qnty}</td>
					<td>${cl.drugproductId.cmddrugId.pharmacy.name}</td>
					<td>${cl.patientId.familyName} ${cl.patientId.givenName}</td>
					<td>
						<a href="consumabledispensation.htm?editId=${cl.consumabledispenseId}" style="text-decoration: none;"><img class="editConsDisp" id="editConsDisp_${cl.consumabledispenseId}" src="${pageContext.request.contextPath}/images/edit.gif" style="cursor: pointer" /></a>
					</td>
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