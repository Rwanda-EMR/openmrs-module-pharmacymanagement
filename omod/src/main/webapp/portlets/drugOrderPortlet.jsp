<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Patient Dashboard - View Drug Order Section" otherwise="/login.htm" redirect="/module/pharmacymanagement/storequest.form"/>

<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/demo_page.css"/>
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/demo_table.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/jquery.simplemodal.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/basic.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/style/basic.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/jquery.PrintArea.js" />

<script type="text/javascript">

	// drugs options
    var drugsName = new Array();
    var drugsId = new Array();
    <c:forEach var="drug" items="${model.drugMap}">
    	drugsName.push("<c:out value="${drug.value}"/>");
    	drugsId.push(<c:out value="${drug.key}"/>);    
    </c:forEach>

    var drName = ""; 
	<c:forEach var="ord" items="${model.drugOrders}" varStatus="status">
		var count = <c:out value="${status.count}"/>; 
		if(count > 1) {
			drName += ", ";
		}
		drName += "<c:out value="${ord.drug.name}"/>";
	</c:forEach>
	
	jQuery('#patientRegimen').hide();
	<!-- jQuery('#patientRegimenTab').hide(); -->
	/**
	jQuery('#patientHeaderRegimen').html(drName); **/
	
	jQuery(document).ready( function() {
		jQuery('.searchBox').hide();
		oTable = jQuery('#example_do').dataTable({
			"fnDrawCallback": function ( oSettings ) {
				if ( oSettings.aiDisplay.length == 0 )
				{
					jQuery('table#example_do').css({'width':'100%'});
					return;
				}

				var nTrs = jQuery('tbody tr', oSettings.nTable);
				jQuery('table#example_do').css({'width':'100%'});
				var iColspan = nTrs[0].getElementsByTagName('td').length;
				var sLastGroup = "";
				for ( var i=0 ; i<nTrs.length ; i++ )
				{
					var iDisplayIndex = oSettings._iDisplayStart + i;
					var sGroup = oSettings.aoData[ oSettings.aiDisplay[iDisplayIndex] ]._aData[0];
					if ( sGroup != sLastGroup )
					{
						var nGroup = document.createElement( 'tr' );	
						var nCell = document.createElement( 'td' );
						nCell.colSpan = iColspan;
						nCell.className = "group";
						nCell.innerHTML = sGroup;
						nGroup.appendChild( nCell );
						nTrs[i].parentNode.insertBefore( nGroup, nTrs[i] );
						sLastGroup = sGroup;
					}
				}
			},
			"aoColumnDefs": [
				{ "bVisible": false, "aTargets": [ 0 ] }
			],
			"aaSortingFixed": [[ 0, 'asc' ]],
			"aaSorting": [[ 1, 'asc' ]],
			"sDom": 'lfr<"giveHeight"t>ip'
		});

		jQuery('.edit').click( function() {
			jQuery('.toBRepl').hide();
			var drugsOption = '<option value="">-- Drugs --</option>';			
			for(var i = 0; i < drugsName.length; i++) {
				drugsOption += '<option value="'+drugsId[i]+'">'+drugsName[i]+'</option>';
			}
			jQuery('#dname').html(drugsOption);
			var index = this.id;
			var prefix = index.substring(0, index.indexOf("_"));
			var suffix = index.substring(index.indexOf("_") + 1);

			var varDose = jQuery("#dose_" + suffix).text();
			var drugId = jQuery("#drugId_" + suffix).text().trim();
			var varUnits = jQuery("#units_" + suffix).text();
			var varQuantity = jQuery("#quantity_" + suffix).text();
			var varStartDate = jQuery("#startDate_" + suffix).text();
			var varDiscDate = jQuery("#discontinuedDate_" + suffix).text();
			var varInstructions = jQuery("#instructions_" + suffix).val();
			
			var varFrequency = jQuery("#frequency_" + suffix).text();
			var varFrequencyArray = varFrequency.split('X');
			var freqDrugQty = varFrequencyArray[0];
			var freqTimesperday = varFrequencyArray[1];
			var freqDays = varFrequencyArray[2];
			jQuery('#qtyTakenAtOnceId').val(freqDrugQty).attr('selected', true);
			jQuery('#timesPerDayId').val(freqTimesperday).attr('selected',true);
			jQuery('#daysId').val(freqDays).attr('selected',true);

			jQuery("#editing").attr("value", suffix);
			
			jQuery('#dname').val(drugId).attr('selected', true);							
			jQuery("#dquantity").attr("value", varQuantity);
			jQuery("#dstartDate").attr("value", varStartDate);
			jQuery("#ddiscontinuedDate").attr("value", varDiscDate);
			jQuery("#dinstructions").html(varInstructions);
			jQuery("#editingcreating").attr("value", "edit");
			});

		jQuery('.stop').click( function() {
			var index = this.id;
			var prefix = index.substring(0, index.indexOf("_"));
			var suffix = index.substring(index.indexOf("_") + 1);
			var stopDate = jQuery("#discontinuedDate_" + suffix).text();
			var varReason = document.getElementById("stopReasonId");
			var reason = jQuery("#discontinuedReason_" + suffix).text().trim();
			
			jQuery('#stopReasonId').val(reason).attr('selected', true);
			
			jQuery('#stopDateId').attr("value", stopDate);
			jQuery("#stopping").attr("value", suffix);
			jQuery("#stop").attr("value", "stop");
		});

		jQuery('#create').click(function() {
			jQuery("#editingcreating").attr("value", "create");
			var item = '';
			jQuery('#dname').change(function() {
				jQuery.getJSON('${pageContext.request.contextPath}/module/pharmacymanagement/drugSolde.htm?drugId='+jQuery("#dname").val(), function(data) {
					if(data[0].solde == 0) {
						item = 'No Such drug in store';
						jQuery('#soldeId').html(item).css('color','red');
					} else {
						item = 'Solde: ' + data[0].solde;
						jQuery('#soldeId').html(item).css('color','black');
					}
					
					
				});
			});
			jQuery('#dname').chosen({no_results_text: "No results matched"});
		});		
		
		jQuery('#print_ordonance').click(function() {
			var row = null;
			var s = '';
			var tableObject = null;
			var columns = jQuery('#example_do thead th').map(function() {
				return jQuery(this).text();
			});		
			tableObject = jQuery('#example_do tbody tr').map(function(i) {
				row = {};
				jQuery(this).find('td').each(function(i) {
					var rowName = columns[i];
				    row[rowName] = jQuery(this).text();
				    s += jQuery(this).text()+',';
				});
				s += ';';
			});
			var res = s.split(';');
			var tmpArr = null;
			var tmpStr = '';
			var count = 1;
			var date = '';
			var dose = '';
			var units = '';
			var frequency = '';
			var patientName = jQuery('#patientHeaderPatientName').text();
			for(var k = 0; k < res.length; k++) {
				tmpArr = res[k].split(',');
				for(var j = 0; j < tmpArr.length; j++) {
					if(tmpArr.length < 5) {
						break;
					}
					else {
						if(j == 1) {
							tmpStr = '<td height="30" align="left" style="background-color:#E5E5FF;">' + count + '. ' + tmpArr[j] + '</td>';
							date = tmpArr[6];
							tmpStr += '<td align="center" style="background-color:#E5E5FF;">' + tmpArr[2] + '</td>';
							tmpStr += '<td align="center" style="background-color:#E5E5FF;">' + tmpArr[3] + '</td>';
							tmpStr += '<td align="center" style="background-color:#E5E5FF;">' + tmpArr[4] + '</td>';
							tmpStr += '<td align="center" style="background-color:#E5E5FF;">' + tmpArr[5] + '</td>';
							tmpStr += '<td style="background-color:#E5E5FF;">&nbsp;</td><td style="background-color:#E5E5FF;">&nbsp;</td>';
							jQuery('#presc-drugs'+count).html(tmpStr);
							count++
						}
					}
				}
			}

			if(count > 5) {
				alert("More than 4 are not allowed");
			} else {
				jQuery('#presc-drugs').html(tmpStr);
				jQuery('#dateId').html(date);
				jQuery("#ordonance-modal-content").dialog();
				jQuery("#ordonance-modal-content").css({'width':'100%', 'height':'405px'});
				jQuery("#createditdialog-container").css({'width':'650px', 'height':'500px'});
				jQuery("#createditdialog-container").css({'top':'120px'});
				jQuery("#createditdialog-container").css({'background-color':'#ffffff'});
			}
		});
		
		jQuery("#print_button").click(function() {
			jQuery(".printArea").printArea();
		});


		jQuery('#medSetId').change(function() {
			var medSetId = jQuery('#medSetId');
			var sb = '<option value="">--Concept--</option>';
			jQuery.getJSON('${pageContext.request.contextPath}/module/pharmacymanagement/conceptdrug.htm?medSet=' + medSetId.val(), function(data) {				
				for(var i in data) {
					sb += '<option value="'+data[i].id+'">'+data[i].name+'</option>';
				}
				jQuery("#drugConceptId").html(sb);
			});
		});

		jQuery('#drugConceptId').change(function() {
			var drugConceptId = jQuery('#drugConceptId');
			var sb1 = '<option value="">--Concept--</option>';
			var opt = '';
			jQuery.getJSON('${pageContext.request.contextPath}/module/pharmacymanagement/conceptdrug.htm?drugConcept=' + drugConceptId.val(), function(data) {
				for(var i in data) {
					opt = '<span class="" ><option value="'+data[i].id+'">'+data[i].name+'</option></span>';
					for(var j in drugsId) {
						if(drugsId[j] == data[i].id) {
							sb1 += '<span class="in_store" ><option value="'+data[i].id+'">'+data[i].name+'</option></span>';
							break;
						} else {							
							sb1 += '<span class="not_in_store" ><option value="'+data[i].id+'">'+data[i].name+'</option></span>';
							break;
						}
					}				
				}
				jQuery('.not_in_store').css({'color':'red'});
				jQuery("#dname").html(sb1);
			});
		});
		
		jQuery('#daysId').change(function() {
			var qtyTakenAtOnce = jQuery('#qtyTakenAtOnceId').val();
			var timesPerDay = jQuery('#timesPerDayId').val();
			var days = jQuery('#daysId').val();
			var quantity = qtyTakenAtOnce * timesPerDay * days;
			
			jQuery('#frequencyId').val(qtyTakenAtOnce + 'X' + timesPerDay + 'X' + days);
			
			jQuery('#dquantity').val(quantity);
		});
		
	});
</script>

<div id="dt_example">
<div id="container">

<div style="float: right"><img id="print_ordonance" src="moduleResources/pharmacymanagement/images/print_preview.gif" style="cursor: pointer;" title="Print Preview" /></div>
<table cellpadding="0" cellspacing="0" border="0" class="display"
	id="example_do" style="width:100%">
	<thead>
		<tr>
			<th>Rendering engine</th>
			<th><spring:message code="pharmacymanagement.drugId" /></th>
			<th><spring:message code="pharmacymanagement.drug" /></th>
			<th><spring:message code="pharmacymanagement.dose" /></th>
			<th><spring:message code="pharmacymanagement.units" /></th>
			<th><spring:message code="pharmacymanagement.frequency" /></th>
			<th><spring:message code="pharmacymanagement.quantity" /></th>
			<th><spring:message code="pharmacymanagement.dispensedquantity" /></th>
			<th><spring:message code="pharmacymanagement.startDate" /></th>
			<th><spring:message code="pharmacymanagement.stopDate" /></th>
			<th><spring:message code="Stopped Reason" /></th>
			<th><spring:message code="pharmacymanagement.edit" /></th>
			<th><spring:message code="pharmacymanagement.stop" /></th>
			<th><spring:message code="Delete" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${model.map}" var="key" varStatus="num">
			<c:forEach items="${key.value}" var="dro" varStatus="num1">
			<tr>
				<td><openmrs:formatDate date="${key.key}" type="textbox" /></td>
				<td>
					<input type="hidden" id="instructions_${dro.orderId}" value="${dro.instructions}" /> 
					<span id="drugId_${dro.orderId}">
						${not empty dro.drug.drugId ? dro.drug.drugId : '<img id="stop_${dro.orderId}" class="stop" src="images/alert.gif"	style="cursor: pointer;" title="Needs to be updated" />'}
					</span>
				</td>
				<td><span id="name_${dro.orderId}">${not empty dro.drug ? dro.drug.name : dro.concept.name.name}</span></td>
				<td><span id="dose_${dro.orderId}">${dro.dose}</span></td>
				<td><span id="units_${dro.orderId}">${dro.doseUnits.name.name}</span></td>
				<td><span id="frequency_${dro.orderId}">${dro.frequency}</span></td>
				<td><span id="quantity_${dro.orderId}">${dro.quantity}</span></td>
				<td><span>

				<c:if test="${!empty model.dispensedQuantity}">

                                <c:forEach items="${model.dispensedQuantity}" var="elem">
                                            <c:if test="${elem.key eq dro.orderId}">
                                                ${elem.value}
                                            </c:if>
                                </c:forEach>
                </c:if>

				</span></td>
				<td><span id="startDate_${dro.orderId}"><openmrs:formatDate date="${dro.dateActivated}" type="textbox" /></span></td>
				<td><span id="discontinuedDate_${dro.orderId}"><openmrs:formatDate date="${dro.dateStopped}" type="textbox" /></span></td>
				<td><span id="discontinuedReason_${dro.orderId}">${dro.orderReason.name.name}</span></td>
				<td><img id="edit_${dro.orderId}" class="edit" src="${pageContext.request.contextPath}/images/edit.gif" style="cursor: pointer" title="Edit" /></td>
				<td><img id="stop_${dro.orderId}" class="stop" src="${pageContext.request.contextPath}/images/stop.gif" style="cursor: pointer;" title="Stop" /></td>
				<td><img id="delete_${dro.orderId}" class="delete" src="${pageContext.request.contextPath}/images/delete.gif" style="cursor: pointer;" title="Delete" /></td>
			</tr>
			</c:forEach>
		</c:forEach>
	</tbody>
	<tfoot>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>			
			<td>
			<button id="create" class="send"><spring:message
				code="pharmacymanagement.create" /></button>
			</td>
			<td></td>
		</tr>
	</tfoot>
</table>
</div>
</div>

<div id="edit-dialog-content">
<form method="post" action="module/pharmacymanagement/dopc.form">
<input type="hidden" name="orderId" id="editing" />
<input type="hidden" name="editcreate" id="editingcreating" />

<!-- Just created these two parameters in order to get them as they are in the Controller (KAMONYO)-->
<input type="hidden" name="appointmentId" value="${model.appointmentId}" />
<input type="hidden" name="patientId" value="${model.patientId}" />
<!-- End of this -->

<table>
<!-- 
	<tr class="toBRepl">		
		<td>Drug Family</td>
		<td>
			<input name="appointmentId" type="hidden" value="${param.appointmentId}" />
			<input name="serviceId" type="hidden" value="${param.appointmentId}" />
			<select name="medSet" id="medSetId" style="width:500px;">
				<option><center>--Drug Family--</center></option>
				<c:forEach items="${model.medSet}" var="medset">
					<option value="${medset.conceptId}">${medset.name.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr class="toBRepl">
		<td>Drug</td>
		<td>
			<select name="drugConcept" id="drugConceptId">
				<option value="">--Drug--</option>
			</select>
		</td>
	</tr>
	 -->
	<tr>
		<td><spring:message code="Drug Details" /></td>
		<td>
			<select name="drugs" id="dname" style="width:500px;">
				<option value="">--Drug--</option>
				<c:forEach items="${model.drugs}" var="drug">
					<option value="${drug.drugId}">${drug.name}</option>
				</c:forEach>
			</select>
		</td>
		<td id="soldeId"></td>
		<td id="soldeId"</td>
	</tr>
	<tr>
					<td ><spring:message code="Dose" />*:</td>
					<td>
						<input type="text" name="drugDose" id="drugDose" size="10"/>
						<select name="doseUnits" id="doseUnits">
							<option value=""></option>
							<c:forEach var="doseUnit" items="${model.drugDosingUnits}">
								<option value="${doseUnit.conceptId}">${doseUnit.displayString}</option>
							</c:forEach>
						</select>
					</td>
	</tr>
    <tr>
					<td><spring:message code="Route"/>:</td>
					<td>
						<select name="route" id="route">
							<option value=""></option>
							<c:forEach var="route" items="${model.drugRoutes}">
								<option value="${route.conceptId}">${route.displayString}</option>
							</c:forEach>
						</select>
					</td>
	</tr>
	<tr>
		<td><spring:message code="pharmacymanagement.frequency" /></td>
		<td>
			<select name="qtyTakenAtOnce" id="qtyTakenAtOnceId">
				<option value="">---</option>
				<c:forEach var="i" begin="1" end="4">
				  <option value="${i}"><c:out value="${i}"/></option>
				</c:forEach>
			</select> X
			
			<select name="timesPerDay" id="timesPerDayId">
				<option value="">---</option>
				<c:forEach var="i" begin="1" end="5">
				  <option value="${i}"><c:out value="${i}"/>/Day</option>
				</c:forEach>
			</select> X
			
			<select name="days" id="daysId">
				<option value="">---</option>
				<option value="1">1 Day</option>
				<c:forEach var="i" begin="2" end="30">
				  <option value="${i}"><c:out value="${i}"/> Days</option>
				</c:forEach>
			</select>
		<input id="frequencyId" type="hidden" name="frequency" /></td>
	</tr>
	
	<tr>
		<td><spring:message code="pharmacymanagement.quantity" /></td>
		<td><input id="dquantity" type="text" name="quantity" size="5" /></td>
	</tr>
	
	<tr>
		<td><spring:message code="pharmacymanagement.startDate" /></td>
		<td><input id="dstartDate" type="text" name="startdate" onfocus="showCalendar(this)" onchange="CompareDates('<openmrs:datePattern />', 'dstartDate');" class="date" size="11" />(dd/mm/yyyy)
		<span id="msgId" style="width"></span></td>
	</tr>
	
	<tr>
		<td valign="top"><spring:message code="pharmacymanagement.instructions" /></td>
		<td><textarea name="instructions" cols="50" rows="4"
			id="dinstructions"></textarea></td>
	</tr>

	<tr>
		<td><input type="submit" value="Submit" class="send" /></td>
	</tr>
</table>

</form>
</div>

<div id="stop-modal-content">
<form method="post" action="module/pharmacymanagement/dopc.form?patientId=${model.patientId}">
<input type="hidden" name="orderId" id="stopping" /> <input
	type="hidden" name="stopping" id="stop" />
<table>
	<tr>
		<td><spring:message code="pharmacymanagement.stopReason" /></td>
		<td><select name="reasons" id="stopReasonId">
			<c:forEach items="${model.reasonStoppedOptions}" var="sr">
				<option value="${sr.key}">${sr.value}</option>
			</c:forEach>
		</select></td>
	</tr>

	<tr>
		<td><spring:message code="pharmacymanagement.stopDate" /></td>
		<td><input id="cal" type="text" name="stopDate" size="12" id="stopDateId"
			onfocus="showCalendar(this)" class="date" size="11" /></td>
	</tr>
	<tr>
		<td><input type="submit" value="Update" class="send" /></td>
	</tr>
</table>

</form>
<br />
</div>

<div id="ordonance-modal-content" style="display: none">
<img id="print_button" src="${pageContext.request.contextPath}/images/printer.gif" style="cursor:pointer;" title="Print"/>
<div class="printArea">
<div id="ordonnanceModal" style="font-size: 10px;">
<center><u><h3>MEDICAL PRESCRIPTION</h3></u>
<table width="600" border="0" style="font-size: 10px;">
  <tr>
    <td width="83">Medical Center</td>
    <td width="411">: ${model.dftLoc.name}</td>
    <td width="40">&nbsp;</td>
    <td width="164">&nbsp;</td>
  </tr>
  <tr>
    <td>Insurance type</td>
    <td>: ${empty model.insuranceType ? 'None' : model.insuranceType}</td>
    <td>Id No</td>
    <td>: ${empty model.insuranceNumber ? 'None' : model.insuranceNumber}</td>
  </tr>
  <tr>
    <td>Names</td>
    <td>: ${model.patient.familyName} ${model.patient.middleName} ${model.patient.givenName}</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
<br />
<table width="600" border="1" cellpadding="0" cellspacing="0"  style="font-size: 10px;">
  <tr align="center" style="background-color:#8FABC7; font-weight:bold;">
    <td height="30" colspan="4">PRESCRIPTION</td>
    <td colspan="3">DISPENSATION (BY PHARMACY)</td>
  </tr>
  <tr align="center" style="background-color:#8FABC7; font-weight:bold;">
    <td height="30">Drug</td>
    <td>Dosage</td>
    <td width="80">Units</td>
    <td width="90">Frequency</td>
    <td width="39" height="24">QTY</td>
    <td width="62">U.P</td>
    <td>T.P</td>
  </tr>
  <tr id="presc-drugs1"></tr>
  <tr id="presc-drugs2"></tr>
  <tr id="presc-drugs3"></tr>
  <tr id="presc-drugs4"></tr>
  <tr>
    <td colspan="4" rowspan="3" valign="middle" style="background-color:#E5E5FF;"><p>Medical Doctor Names: ${model.provider.person.names}</p>
      <p>Stamp, signature and date.</p>
      <p>&nbsp;</p></td>
    <td height="30%" colspan="2" align="center" style="background-color:#E5E5FF;"><strong>Total</strong></td>
    <td width="87" style="background-color:#E5E5FF;">&nbsp;</td>
  </tr>
  <tr height="30%" style="background-color:#E5E5FF;">
    <td colspan="2" align="center"><strong>Client</strong></td>
    <td>&nbsp;</td>
  </tr>
  <tr height="30%" style="background-color:#E5E5FF;">
    <td colspan="2" align="center"><strong>Assurance</strong></td>
    <td>&nbsp;</td>
  </tr>
  <tr style="background-color:#E5E5FF;">
    <td colspan="4" valign="middle"><p><strong><em>Reception</em></strong></p>
      <p>Names, signature and date</p>
      <p>&nbsp;</p></td>
    <td height="68" colspan="3" align="left" valign="middle"><p><em><strong>Delivery</strong></em></p>
      <p>Names, signature and date</p>
      <p>&nbsp;</p></td>
    </tr>
</table>
</center>
</div>	
</div>
</div>

<!-- delete order modal -->
<div id="delete-modal-content">
<form method="post" action="module/pharmacymanagement/dopc.form?patientId=${model.patientId}&delete=on">
<input type="hidden" name="orderToDel" id="orderToDelId" />
<select name="deleteReason" id="deleteReasonId">
<option value="Date Error">Date Error</option>
<option value="Error">Error</option>
<option value="Other">Other</option>
</select>
<input type="submit" value="Delete" />
</form>
</div>