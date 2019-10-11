<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="View Pharmacy management" otherwise="/login.htm"
	redirect="/module/pharmacymanagement/pharmacyrequest.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>

<div>

<div id="outer"><%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle">

<%@ include file="template/dispensingLocalHeader.jsp"%>



<script type="text/javascript">

var $dsm = jQuery.noConflict();
$dsm(function() {
	$dsm('#productCategory').tabs();
});

var dftLocationName = "<c:out value="${dftLoc.name}"/>"
var dftLocationId = "<c:out value="${dftLoc.locationId}"/>"
	var fieldGroupCount = 0;
	function fgc() {
		return fieldGroupCount;
	}
		
	// drugs options
    var drugsArray = new Array();
    var drugsIdArray = new Array();
    <c:forEach var="dp" items="${dpSet}">
    	drugsArray.push("<c:out value="${dp.drugId.name}"/>");
    	drugsIdArray.push(<c:out value="${dp.drugproductId}"/>);
    </c:forEach>

	//Consumable options
	var consArray = new Array();
    var consIdArray = new Array();
    <c:forEach var="cs" items="${consumableSet}">
    	consArray.push("<c:out value="${cs.conceptId.name.name}"/>");
    	consIdArray.push(<c:out value="${cs.drugproductId}"/>);
    </c:forEach>
    
	/************************************************************************** 
	*	function for adding options to the select.
	***************************************************************************/
	
    var condUnitArray = new Array(    	    
                         "1 boite(270 ces)",
                         "1 boite(180 ces)",
                         "1 boite(90 ces)",
                         "1 boite(60 ces)",
                         "1 boite(56 ces)",
                         "1 boite(30 ces)",
                         "1 flacon de 240 ml",
                         "1 flacon de 200 ml",
                         "1 flacon de 180 ml",
                         "1 flacon de 144 ml",
                         "1 flacon de 100 ml"
                         );
    var condUnitIdArray = new Array("1","2","3","4","5","6","7","8","9","10","11");
    
   
	
	$dsm(document).ready(
			function(){			
				$dsm("input").focus(function () {
			         $dsm(this).css('background-color','#abcdef');
			    });

				$dsm("input").blur(function () {
			         $dsm(this).css('background-color','white');
			    });
			    	
				$dsm("#adddrugid").click(function(){
					createDrug("drugs",drugsArray,drugsIdArray,"dynamicDrug");
					$dsm("#hiddenfield").attr("value", fieldGroupCount);
				});
							
				$dsm("#addconsumableid").click(function() {
					createConsumable("consumable",consArray,consIdArray,"dynamicConsumable");
				});
				
				$dsm('#drugstore').salidate({
			        'month' : {
			            callback: 'required',
			            msg: 'The Month is required.'
			        }
			    });
			     $dsm('#drugstore').salidate({
                    'pharmacy' : {
                        callback: 'required',
                        msg: 'The pharmacy is required.'
                    }
                });

				$dsm("#locat").html(dftLocationName);
			    $dsm("#destin").attr("value", dftLocationId);
		}
);

</script>

<form method="post" id="drugstore" action="pharmacyrequest.form?on=true">

<fieldset><legend><spring:message code="pharmacymanagement.phcyReqForm" /></legend> <input
	id="hiddenfield" type="hidden" name="fieldCount" />

<table>
	<tr>
		<td><spring:message code="pharmacymanagement.from" /></td>
		<td><input id="destin" type="hidden" name="destination" /> 
		
		<select name="pharmacy">
			<option value="">-- select --</option>
			<c:forEach items="${pharmacyList}" var="pharmacy">
				<option value="${pharmacy.pharmacyId}">${pharmacy.name}</option>
			</c:forEach>
		</select>
		 
		 
		</td>
		<td><spring:message code="pharmacymanagement.to" /></td>
		<td>
		<div id="locat"></div>
		</td>
	</tr>
	<tr>
		<td><spring:message code="pharmacymanagement.supportProg" /></td>
		<td><input type="text" name="supporter" /></td>
	</tr>
	<tr>
		<td><spring:message code="pharmacymanagement.month" /></td>
		<td><input type="text" name="month" onfocus="showCalendar(this)"
			size="10" /></td>
	</tr>
</table>

<!-- Tabs -->
<div id="productCategory">

<ul>
	<li><a href="#fragment-1"><span><spring:message
		code="Drug Products" /></span></a></li>
	<li><a href="#fragment-2"><span><spring:message
		code="Consumable Products" /></span></a></li>
</ul>

<div id="fragment-1">
<div id="drugs">
	<table width="100%">
		<tr align="left">
        			<th style="width:30%"><spring:message code="pharmacymanagement.designation" /></th>
        			<th style="width:20%"><spring:message code="pharmacymanagement.qntyReq" /></th>
                    <th style="width:20%">Reason</th>
                    <th style="width:20%">Type</th>
        			<th style="width:10%">Cancel</th>
        </tr>
	</table>
</div>
	<p id="adddrugid" class="greenbox"></p>
</div>

<div id="fragment-2">
<div id="consumable">
	<table width="100%">
	<tr align="left">
    			<th style="width:30%"><spring:message code="pharmacymanagement.designation" /></th>
    			<th style="width:20%"><spring:message code="pharmacymanagement.qntyReq" /></th>
                <th style="width:20%">Reason</th>
                <th style="width:20%">Type</th>
    			<th style="width:10%">Cancel</th>
    </tr>
	</table>
</div>
	<p id="addconsumableid" class="greenbox"></p>
</div>
</div>
<!--/ Tabs -->

</fieldset>
<table>
	<tr>
		<td><input id="formSubmitId" type="submit" value="Enter" class="send" /></td>
	</tr>
</table>

</form>
</div>

<div style="clear: both;"></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>