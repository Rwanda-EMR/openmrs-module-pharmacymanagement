<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="View Drug Store management" otherwise="/login.htm" redirect="/module/pharmacymanagement/storequest.form"/>

<div>
<div id="outer">
	<%@ include file="template/leftMenu.jsp"%>
</div>
<div id="middle">

<%@ include file="template/localHeader.jsp"%>

<!--  
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/dataentrystyle.css" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/jquery.validate.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/create_dynamic_field.js" />
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/validator.js" />
-->

	<script type="text/javascript">
	
	var $dsm = jQuery.noConflict();
	$dsm(function() {
		$dsm('#productCategory').tabs();
	});
		var fieldGroupCount = 0;
		function fgc() {
			return fieldGroupCount;
		}
		
		//var storeQnty = 233;
			
		// drugs options
	    var drugsArray = new Array();
	    var drugsIdArray = new Array();
	    <c:forEach var="drug" items="${drugs}">
	    	drugsArray.push("<c:out value="${drug.value}"/>");
	    	drugsIdArray.push(<c:out value="${drug.key}"/>);
	    </c:forEach>

		//Consumable options
		var consArray = new Array();
	    var consIdArray = new Array();
	    <c:forEach var="cs" items="${css}">
	    	consArray.push("<c:out value="${cs.answerConcept.name.name}"/>");
	    	consIdArray.push(<c:out value="${cs.answerConcept.conceptId}"/>);
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
				        'fosaName' : {
				            callback: 'required',
				            msg: 'The FOSA Name is required.'
				        },
				        'month' : {
				            callback: 'required',
				            msg: 'The Month is required.'
				        }
				    });						
	});
	
	</script>
	
	
	<form method="post" id="drugstore" action="storequest.form">
	
	<fieldset id="parent_field"><legend><spring:message code="pharmacymanagement.hfReqForm" /></legend> <input
		id="hiddenfield" type="hidden" name="fieldCount" />
	
	<table>
		<tr>
			<td><spring:message code="pharmacymanagement.from" /></td>
			<td>
				<input type="hidden" name="fosaName" value="${dftLoc.locationId}" />
				<input type="text" name="fosaNameTxt" value="${dftLoc.name}" readonly="readonly" size="${fn:length(dftLoc.name) + 5}"/>
			</td>
		</tr>
		<tr>
			<td><spring:message code="pharmacymanagement.to" /></td>
			<td>
			<!-- <select name="destination">
				<c:forEach var="location" items="${locations}">
					<option value="${location.locationId}">${location.name}</option>
				</c:forEach>
			</select>
					 -->
					 		<input type="text" name="destination" value="${dftLoc.name}" readonly="readonly" size="${fn:length(dftLoc.name) + 5}"/>

			</td>
		</tr>
		<tr>
			<td><spring:message code="pharmacymanagement.supportProg" /></td>
			<td><input type="text" name="supporter" /></td>
		</tr>
		<tr>
			<td><spring:message code="pharmacymanagement.month" /></td>
			<td><input type="text" name="month" onfocus="showCalendar(this)" size="10" /></td>
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
