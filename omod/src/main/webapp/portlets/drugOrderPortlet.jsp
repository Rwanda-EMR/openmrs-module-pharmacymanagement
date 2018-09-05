<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Patient Dashboard - View Drug Order Section" otherwise="/login.htm" redirect="/module/pharmacymanagement/storequest.form"/>

<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/demo_page.css"/>
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/demo_table.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/jquery.simplemodal.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/basic.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/style/basic.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/jquery.PrintArea.js" />

<script type="text/javascript">
   var $dm = jQuery.noConflict();

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
   
   $dm('#patientRegimen').hide();
   /** $dm('#patientRegimenTab').hide();

   $dm('#patientHeaderRegimen').html(drName); **/
   
   $dm(document).ready( function() {
      $dm('.searchBox').hide();
      oTable = $dm('#example_do').dataTable({
         "fnDrawCallback": function ( oSettings ) {
            if ( oSettings.aiDisplay.length == 0 )
            {
               $dm('table#example_do').css({'width':'100%'});
               return;
            }

            var nTrs = $dm('tbody tr', oSettings.nTable);
            $dm('table#example_do').css({'width':'100%'});
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

      $dm('.edit').click( function() {
         $dm('.toBRepl').hide();
         var drugsOption = '<option value="">-- Drugs --</option>';       
         for(var i = 0; i < drugsName.length; i++) {
            drugsOption += '<option value="'+drugsId[i]+'">'+drugsName[i]+'</option>';
         }
         $dm('#dname').html(drugsOption);
         var index = this.id;
         var prefix = index.substring(0, index.indexOf("_"));
         var suffix = index.substring(index.indexOf("_") + 1);

         var varDose = $dm("#dose_" + suffix).text();
         var drugId = $dm("#drugId_" + suffix).text().trim();
         var varUnits = $dm("#units_" + suffix).text();
         var varQuantity = $dm("#quantity_" + suffix).text();
         var varStartDate = $dm("#startDate_" + suffix).text();
         var varDiscDate = $dm("#discontinuedDate_" + suffix).text();
         var varInstructions = $dm("#instructions_" + suffix).val();
         
         var varFrequency = $dm("#frequency_" + suffix).text();
         var varFrequencyArray = varFrequency.split('X');
         var freqDrugQty = varFrequencyArray[0];
         var freqTimesperday = varFrequencyArray[1];
         var freqDays = varFrequencyArray[2];
         $dm('#qtyTakenAtOnceId').val(freqDrugQty).attr('selected', true);
         $dm('#timesPerDayId').val(freqTimesperday).attr('selected',true);
         $dm('#daysId').val(freqDays).attr('selected',true);

         $dm("#editing").attr("value", suffix);
         
         $dm('#dname').val(drugId).attr('selected', true);                    
         $dm("#dquantity").attr("value", varQuantity);
         $dm("#dstartDate").attr("value", varStartDate);
         $dm("#ddiscontinuedDate").attr("value", varDiscDate);
         $dm("#dinstructions").html(varInstructions);
         $dm("#editingcreating").attr("value", "edit");
         });

      $dm('.stop').click( function() {
         var index = this.id;
         var prefix = index.substring(0, index.indexOf("_"));
         var suffix = index.substring(index.indexOf("_") + 1);
         var stopDate = $dm("#discontinuedDate_" + suffix).text();
         var varReason = document.getElementById("stopReasonId");
         var reason = $dm("#discontinuedReason_" + suffix).text().trim();
         
         $dm('#stopReasonId').val(reason).attr('selected', true);
         
         $dm('#stopDateId').attr("value", stopDate);
         $dm("#stopping").attr("value", suffix);
         $dm("#stop").attr("value", "stop");
      });

      $dm('#create').click(function() {
         $dm("#editingcreating").attr("value", "create");
         var item = '';
         $dm('#dname').change(function() {
            $dm.getJSON('${pageContext.request.contextPath}/module/pharmacymanagement/drugSolde.htm?drugId='+$dm("#dname").val(), function(data) {
               if(data[0].solde == 0) {
                  item = 'No Such drug in store';
                  $dm('#soldeId').html(item).css('color','red');
               } else {
                  item = 'Solde: ' + data[0].solde;
                  $dm('#soldeId').html(item).css('color','black');
               }
               
               
            });
         });
         $dm('#dname').chosen({no_results_text: "No results matched"});
      });       
      
      $dm('#print_ordonance').click(function() {
         var row = null;
         var s = '';
         var tableObject = null;
         var columns = $dm('#example_do thead th').map(function() {
            return $dm(this).text();
         });       
         tableObject = $dm('#example_do tbody tr').map(function(i) {
            row = {};
            $dm(this).find('td').each(function(i) {
               var rowName = columns[i];
                row[rowName] = $dm(this).text();
                s += $dm(this).text()+',';
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
         var patientName = $dm('#patientHeaderPatientName').text();
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
                     $dm('#presc-drugs'+count).html(tmpStr);
                     count++
                  }
               }
            }
         }

         if(count > 5) {
            alert("More than 4 are not allowed");
         } else {
            $dm('#presc-drugs').html(tmpStr);
            $dm('#dateId').html(date);
            $dm("#ordonance-modal-content").dialog();
            $dm("#ordonance-modal-content").css({'width':'100%', 'height':'405px'});
            $dm("#createditdialog-container").css({'width':'650px', 'height':'500px'});
            $dm("#createditdialog-container").css({'top':'120px'});
            $dm("#createditdialog-container").css({'background-color':'#ffffff'});
         }
      });
      
      $dm("#print_button").click(function() {
         $dm(".printArea").printArea();
      });


      $dm('#medSetId').change(function() {
         var medSetId = $dm('#medSetId');
         var sb = '<option value="">--Concept--</option>';
         $dm.getJSON('${pageContext.request.contextPath}/module/pharmacymanagement/conceptdrug.htm?medSet=' + medSetId.val(), function(data) {           
            for(var i in data) {
               sb += '<option value="'+data[i].id+'">'+data[i].name+'</option>';
            }
            $dm("#drugConceptId").html(sb);
         });
      });

      $dm('#drugConceptId').change(function() {
         var drugConceptId = $dm('#drugConceptId');
         var sb1 = '<option value="">--Concept--</option>';
         var opt = '';
         $dm.getJSON('${pageContext.request.contextPath}/module/pharmacymanagement/conceptdrug.htm?drugConcept=' + drugConceptId.val(), function(data) {
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
            $dm('.not_in_store').css({'color':'red'});
            $dm("#dname").html(sb1);
         });
      });
      
      $dm('#daysId').change(function() {
         var qtyTakenAtOnce = $dm('#qtyTakenAtOnceId').val();
         var timesPerDay = $dm('#timesPerDayId').val();
         var days = $dm('#daysId').val();
         var quantity = qtyTakenAtOnce * timesPerDay * days;
         
         $dm('#frequencyId').val(qtyTakenAtOnce + 'X' + timesPerDay + 'X' + days);
         
         $dm('#dquantity').val(quantity);
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
                  ${not empty dro.drug.drugId ? dro.drug.drugId : '<img id="stop_${dro.orderId}" class="stop" src="images/alert.gif" style="cursor: pointer;" title="Needs to be updated" />'}
               </span>
            </td>
            <td><span id="name_${dro.orderId}">${not empty dro.drug ? dro.drug.name : dro.concept.name.name}</span></td>
            <td><span id="dose_${dro.orderId}">${dro.dose}</span></td>
            <td><span id="units_${dro.orderId}">${dro.units}</span></td>
            <td><span id="frequency_${dro.orderId}">${dro.frequency}</span></td>
            <td><span id="quantity_${dro.orderId}">${dro.quantity}</span></td>
            <td><span id="startDate_${dro.orderId}"><openmrs:formatDate date="${dro.startDate}" type="textbox" /></span></td>
            <td><span id="discontinuedDate_${dro.orderId}"><openmrs:formatDate date="${dro.discontinuedDate}" type="textbox" /></span></td>
            <td><span id="discontinuedReason_${dro.orderId}">${dro.discontinuedReason.name}</span></td>
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
    <td colspan="4" rowspan="3" valign="middle" style="background-color:#E5E5FF;"><p>Medical Doctor Names: ${model.provider.familyName} ${model.provider.firstName}</p>
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