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
		  var idIndex = 0;
		  $p('#add').on('click',function() {
				var dateFieldValue = $p('#dateFieldId').val();
				if(dateFieldValue){}else{alert('Please, select a date value first');return;};
				var serviceFieldValue = $p('#serviceId').val();
				if(serviceFieldValue){}else{alert('Please, select a service first');return;};
				var patientFieldValue = $p('#patientId_id').val();
				if(patientFieldValue){}else{alert('Please, select a patient first');return;};
			     idIndex++;
			     var templateClone = $p("#emptyRow").clone();
			      templateClone.attr("id", "emptyRow"+idIndex);
			      templateClone.attr("hidden", false);
			      templateClone.addClass("clonedRow");
			      
			      templateClone.find('td:nth-child(1)').children().first().val(dateFieldValue).change();
			      templateClone.find('td:nth-child(1)').children().first().attr("name", "date"+idIndex);
			      templateClone.find('td:nth-child(2)').children().first().val(serviceFieldValue).change();
			      templateClone.find('td:nth-child(2)').children().first().attr("name", "service"+idIndex);
			      
					var concOption = '<option value="">-- Consumable --</option>';
					$p.getJSON('json.htm?serviceId=' + serviceFieldValue, function(data) {
						for(var i in data) {
							concOption += '<option value="'+data[i].id+'">'+data[i].name+'(Solde:'+data[i].solde+'/LotNo:'+data[i].lotno+'/Exp:'+data[i].expiry+')</option>';
						}
						templateClone.find('td:nth-child(3)').children().first().html(concOption);
					    templateClone.find('td:nth-child(3)').children().first().attr("name", "consumable"+idIndex);
					});
					
				 templateClone.find('td:nth-child(4)').children().first().attr("name", "qnty"+idIndex);	
				    
					var patientTextFieldValue = $p('#patientId_id_selection').val();
					$p('tr').each(function() {
					       if($p(this).is('[class*="clonedRow"]')){
					    	   var patientTd = $p(this).find('td:nth-child(5)');
					    	   patientTd.children().first().val(patientTextFieldValue);
					       }
					});
			      
			      templateClone.appendTo("#dynamic_field tbody");
			  });
		
		$p('#example').dataTable();
		
		$p('#dateFieldId').change(function() {
			var dateFieldValue = $p('#dateFieldId').val();
			$p('tr').each(function() {
		       if($p(this).hasClass('clonedRow')){
		    	   var dateTd = $p(this).find('td:first');
		    	   dateTd.children().first().val(dateFieldValue);
		       }
			});
		});

		$p('#serviceId').change(function() {
			var concOption = '<option value="">-- Consumable --</option>';
			var serviceFieldValue = $p('#serviceId').val();
			$p.getJSON('json.htm?serviceId=' + serviceFieldValue, function(data) {
				for(var i in data) {
					concOption += '<option value="'+data[i].id+'">'+data[i].name+'(Solde:'+data[i].solde+'/LotNo:'+data[i].lotno+'/Exp:'+data[i].expiry+')</option>';
				}
				$p('#dpId').html(concOption);
				$p('tr').each(function() {
				       if($p(this).is('[class*="clonedRow"]')){
				    	   $p(this).find('td:nth-child(3)').children().first().html(concOption);		
				       }
				});
			});
			
			$p('tr').each(function() {
			       if($p(this).is('[class*="clonedRow"]')){
			    	   var serviceTd = $p(this).find('td:nth-child(2)');
			    	   serviceTd.children().first().val(serviceFieldValue).change();
			       }
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

	<b class="boxHeader">Patient</b>
	<div class="box">
    		<form method="post" action="consumabledispensation.htm" >
    			<div  id="patientForm">
    			Service:
    			<select name="dispservice">
                    <option value="">-- Service --</option>
                    <c:forEach var="pharmacy" items="${pharmacies}">
                        <option value="${pharmacy.pharmacyId}" ${pharmacy.pharmacyId eq cd.service.conceptId ? 'selected="selected"' : '' }>${pharmacy.name}</option>
                    </c:forEach>
                </select>
    			Date: <input type="text" name="orderdate" id="dateFieldId" value="<openmrs:formatDate date="${cd.date}" type="textbox" />" onfocus="showCalendar(this)" size="10" style="min-width: 100px; max-width: 100px; width : 100px;" />
    			Patient: <openmrs:fieldGen type="org.openmrs.Patient" formFieldName="orderspatientId"  val="${cd.patientId}"/>
    			<input type="submit" name="findorders" value="Find Orders" />
                		</form>
    			</div>
    </div>


	<b class="boxHeader">Bon de Sortie</b>
	<div class="box">
		<form method="post" action="consumabledispensation.htm" >
			<div  id="consDispForm">
			<table id="dynamic_field" width="98%" class="return">
				<thead>
					<tr>
						<!-- <th><spring:message code="Date"/></th>
						<th><spring:message code="Service"/></th> -->
						<th><spring:message code="Consommable" /></th>
						<th><spring:message code="Quantity"/></th>
						<!-- <th><spring:message code="Patient"/></th>
						<th><spring:message code="Delete"/></th> -->
					</tr>
				</thead>
<!--				<tbody>
						<tr>
							<td>
								<input type="text" name="date" id="dateFieldId" value="<openmrs:formatDate date="${cd.date}" type="textbox" />" onfocus="showCalendar(this)" size="10" style="min-width: 100px; max-width: 100px; width : 100px;" />
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

									<c:forEach var="cons" items="${map}">
										<option value="${cons.value.drugproductId}">${cons.value.conceptId.name.name}(${cons.value.lotNo})</option>
									</c:forEach>

								</select>
							</td>
							<td><input type="text" name="qnty" size="5" value="${cd.qnty > 0 ? cd.qnty : ''}" /></td>							
							<td id = "patientFieldId">

							</td>
							<td></td>
						</tr>
					    <tr id = "emptyRow" hidden="hidden">
							<td>
								<input type="text" name="date" value="<openmrs:formatDate date="${cd.date}" type="textbox" />" onfocus="showCalendar(this)" size="10" style="min-width: 100px; max-width: 100px; width : 100px;" />
							</td>
							<td>
								<select name="service">
									<option value="">-- Service --</option>
									<c:forEach var="pharmacy" items="${pharmacies}">
										<option value="${pharmacy.pharmacyId}" ${pharmacy.pharmacyId eq cd.service.conceptId ? 'selected="selected"' : '' }>${pharmacy.name}</option>
									</c:forEach>
								</select>
							</td>
							<td><input type="text" name="qnty" size="5" value="${cd.qnty > 0 ? cd.qnty : ''}" /></td>
							<td>
								<input type="text">
							</td>
							<td></td>
						</tr>
				</tbody>
-->
<tbody>
<c:forEach var="cons" items="${map}">
<tr>
						<td style="width : 60%;">
						<input type="hidden" name="consumable" size="5" value="${cons.value.drugproductId}" />
						<input type="hidden" name="consumableorderId" size="5" value="${cons.key}" />
						<input type="hidden" name="orderdate" size="5" value="${orderdate}" />
						<input type="hidden" name="dispservice" size="5" value="${dispservice}" />
						<input type="hidden" name="orderspatientId" size="5" value="${orderspatientId}" />
                           ${cons.value.conceptId.name.name}(${cons.value.lotNo})
                        </td>
						<td><input type="text" name="qnty" size="5" value="${consDispQnties.get(cons.value.drugproductId)}" /></td>
</tr>
	</c:forEach>

</tbody>




			</table>
			</div>
			<!-- <button type="button" name="add" id="add" class="btn btn-success">Add More</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; --> <input type="submit" name="saveorder" value="Save" />
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