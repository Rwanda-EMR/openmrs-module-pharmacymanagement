<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Drug Store management" otherwise="/login.htm" redirect="/module/pharmacymanagement/return.form" />

<openmrs:htmlInclude file="/scripts/jquery/jquery.min.js" />

<!-- 
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/calendar.js" />
 -->

<div>
<div id="outer"><%@ include file="template/leftMenu.jsp"%>
</div>
<!--  
<openmrs:htmlInclude file="/moduleResources/pharmacymanagement/scripts/jquery.js" />
-->
<div id="middle"><%@ include file="template/localHeader.jsp"%>

<script type="text/javascript">
var $sr = jQuery.noConflict();

var returnType = null;

var dftLocId = "<c:out value="${dftLoc.locationId}"/>";
var dftLocName = "<c:out value="${dftLoc.name}"/>";

//populating pharmacies into an array
var pharmaNameArray = new Array();
var pharmaIdArray = new Array();
<c:forEach var="pharmacy" items="${pharmacies}">
	pharmaNameArray.push("<c:out value="${pharmacy.name}"/>");
	pharmaIdArray.push("<c:out value="${pharmacy.pharmacyId}"/>");
</c:forEach> 

//populating locations into an array
var locNameArray = new Array();
var locIdArray = new Array();
<c:forEach var="location" items="${locations}">
	locNameArray.push("<c:out value="${location.name}"/>");
	locIdArray.push("<c:out value="${location.locationId}"/>");
</c:forEach>

//populating drugs from the store into an array
var storeDrugArray = new Array();
var storeDrugIdArray = new Array();
<c:forEach var="drug" items="${drugMapStore}">
	storeDrugArray.push("<c:out value="${drug.value}"/>");
	storeDrugIdArray.push("<c:out value="${drug.key}"/>");
</c:forEach> 


//populating drugs from the pharmacy into an array
var pharmaDrugArray = new Array();
var pharmaDrugIdArray = new Array();
<c:forEach var="drug" items="${drugMapPharma}">
	pharmaDrugArray.push("<c:out value="${drug.value}"/>");
	pharmaDrugIdArray.push("<c:out value="${drug.key}"/>");
</c:forEach> 

//populating all kind of drugs into an array
var drugArray = new Array();
var drugIdArray = new Array();
<c:forEach var="drug" items="${drugs}">
	drugArray.push("<c:out value="${drug.name}"/>");
	drugIdArray.push("<c:out value="${drug.drugId}"/>");
</c:forEach> 

$sr(document).ready(function() {
	$sr("#fromId").hide();
	$sr("#toId").hide();	
	$sr("#lotId").attr("disabled", "disabled");
	

	$sr("#showStockId").click(function() {
		var dateArr = new Array();
		var dateVal = $sr("#returnDateId").val()
		dateArr = dateVal.split("/");
		var date = dateArr[2]+"-"+dateArr[1]+"-"+dateArr[0];
		var isChecked;
		if($sr("#showStockId").attr("checked")) {
			isChecked = 1;
		} else {
			isChecked = 0;
		}
		$sr("#storeReturnId").load("printReturnStock.list?isChecked="+isChecked+"&retDate="+date+" #return_stock", function() {
			var LocOptions = '';
			var pharmaOptions = '';
			$sr('#example').dataTable();
			$sr('#example_filter').html("<a href='${pageContext.request.contextPath}/module/pharmacymanagement/printReturnStock.list?isChecked=1&retDate="+date+"'>Print page</a>");

			/** The class was ars and I changed it to ars1 to avoid the event to be triggered. 
			  * In case you want to update just rename the class to the original "ars"
			**/
			$sr('.ars1').click(function() {
				var trId = $sr(this).attr('id');
				var trIdArr = new Array();
				trIdArr = trId.split("_");
				var suffix = trIdArr[1];
				$sr('#arsId').val(suffix);
				
				//getting values from the table
				  var returnDate = $sr(this).children("td:nth-child(2)").text();
				  var drug_name = $sr(this).children("td:nth-child(3)").text();
				  var origin = $sr(this).children("td:nth-child(5)").text();
				  var pharmacy = $sr(this).children("td:nth-child(6)").text();
				  var destination = $sr(this).children("td:nth-child(7)").text();
				  var qty = $sr(this).children("td:nth-child(8)").text();
				  var lot = $sr(this).children("td:nth-child(9)").text();
				  var expDate = $sr(this).children("td:nth-child(10)").text();
				  var obs = $sr(this).children("td:nth-child(11)").text();
										  
				//populating the fields from table values
					$sr('input[name=returnDate]').val(returnDate);
					$sr('input[name=lot]').val(lot);
					$sr('input[name=expDate]').val(expDate);
					$sr('#obsId').text(obs);
					
					
					if(qty) {
						$sr('#qtyId').attr("disabled", "");
						$sr('#qtyId').val(qty);
					}

					var locOptions = '<option value="">-- select --</option>';
					for(var i in locNameArray) {
						locOptions += '<option value="'+locIdArray[i]+'">'+locNameArray[i]+'</option>';
					}

					if(pharmacy) {
						returnType = 'internal';
							
						$sr('#intId').attr("checked", "checked");
						
						pharmaOptions += '<option value="">-- select --</option>';
						for(var i in pharmaNameArray) {
							pharmaOptions += '<option value="'+pharmaIdArray[i]+'">'+pharmaNameArray[i]+'</option>';
						}

						$sr("#fromId").html(pharmaOptions);
						$sr("#toId").html(locOptions);
						$sr("#fromId option[text=" + pharmacy +"]").attr("selected","selected");
						$sr("#toId option[text=" + destination +"]").attr("selected","selected");

						$sr("#fromId").show();
						$sr("#toId").show();
					} else {
						returnType = 'external';
						$sr('#extId').attr("checked", "checked");	
						
						$sr("#toId").html(locOptions);
						$sr("#fromId").html(locOptions);
						$sr("#toId").show();
						$sr("#fromId").show();
						$sr("#toId option[text='" + destination + "']").attr("selected","selected");
						$sr("#fromId option[text='" + origin + "']").attr("selected","selected");						
					}
					//populating drugs
					var fromId = $sr('#fromId');
					var toId = $sr('#toId');
					var sb = '<option value="">-- select --</option>';
					/**
					$.getJSON('json.htm?fromId=' + fromId.val() + '&toId=' + toId.val() + '&retType=' + returnType, function(data) {
						for(var i in data) {
							if(drug_name === data[i].drugName) {
								alert("drug_name: "+drug_name+" drugName: "+data[i].drugName);
								sb += '<option value="'+data[i].drugId+'" selected="selected">'+data[i].drugName+'</option>';
							} else {
								sb += '<option value="'+data[i].drugId+'">'+data[i].drugName+'</option>';
							}
									
						}
						$sr("#drugId").html(sb);
					}); **/

					for(var i in drugArray) {
						if(drug_name === drugArray[i]) {
							sb += '<option value="'+drugIdArray[i]+'" selected="selected">'+drugArray[i]+'</option>';
						} else {
							sb += '<option value="'+drugIdArray[i]+'">'+drugArray[i]+'</option>';
						}
					}
					$sr("#drugId").html(sb);
						
					if(lot) {
						var drugId = $sr("#drugId");
						var items = '';
						if(origin != dftLocName) {
							$sr.getJSON('lot.htm?drugId=' + drugId.val(), function(data) {
								$sr.each(data, function(key, value) {
									items += '<option value="">-- Select --</option>';
									for(var i in value) {
										var dateArr = value[i].columnIndex_1.split('-');
										var dat = dateArr[2]+'/'+dateArr[1]+'/'+dateArr[0]
										if(lot === value[i].columnIndex_0) {
											items += '<option value="' + dat + '_' + value[i].columnIndex_2 + '" selected="selected" >' + value[i].columnIndex_0 + '</option>';
										} else {
											items += '<option value="' + dat + '_' + value[i].columnIndex_2 + '" >' + value[i].columnIndex_0 + '</option>'
										}
									}				
								});
							$sr("select#lotId").html(items);
							$sr("#lotId").attr("disabled", "");
							});
						} else {
							$sr("#toReplace").html('<input type="text" name="lot" id="lotId" size="11" />');
							$sr("#expDateId").attr("disabled", "");
							$sr("#qtyId").attr("disabled", "");
						}
					}
			});
		});
	});	
	
	$sr("input[name=retType]").change(function() {
		populateLocations();
	});
	
	$sr("#fromId").change(function() {
		var retVar = $sr("input[name=retType]:checked").val();			
		var fromId = $sr('#fromId');
		var toId = $sr('#toId');
		var sb = '<option value="">-- select --</option>';
		$sr("#toId").val(dftLocId).attr("selected","selected");
		$sr.getJSON('json.htm?fromId=' + fromId.val() + '&toId=' + toId.val() + '&retType=' + returnType, function(data) {
			if(retVar == 'external') {
				for(var i in drugArray) {
					sb += '<option value="'+drugIdArray[i]+'">'+drugArray[i]+'</option>';
				}
				$sr("#toReplace").html('<input type="text" name="lot" id="lotId" size="11" value="" />');
				$sr("#expDateId").val("").attr("disabled", "");
				$sr("#qtyId").val("").attr("disabled", "");
			} else {
				$sr("#toReplace").html('<select name="lot" id="lotId"><option value="">-- select --</option></select>');
				$sr("#expDateId").val("").attr("disabled", "disabled");
				$sr("#qtyId").val("").attr("disabled", "disabled");
				for(var i in data) {
					sb += '<option value="'+data[i].id+'">'+data[i].name+'</option>';
				}
			}
			
			$sr("#drugId").html(sb);
		});
	});
	$sr("#toId").change(function() {
		var fromId = $sr('#fromId');
		var toId = $sr('#toId');
		var sb = '<option value="">-- select --</option>';
		$sr("#fromId").val(dftLocId).attr("selected","selected");
		$sr.getJSON('json.htm?fromId=' + fromId.val() + '&toId=' + toId.val() + '&retType=' + returnType, function(data) {			
			for(var i in data) {
				sb += '<option value="'+data[i].id+'">'+data[i].name+'</option>';
			}
			$sr("#drugId").html(sb);
			$sr("#toReplace").html('<select name="lot" id="lotId"><option value="">-- select --</option></select>');
			$sr("#expDateId").val("").attr("disabled", "disabled");
			$sr("#qtyId").val("").attr("disabled", "disabled");
		});
		
	});	
});

function fillOptions(drugId, lotId, c) {
	var drugId = $sr('#' + drugId);
	var lotId = $sr('#' + lotId);
	var items = '';
	
	$sr.getJSON('lot.htm?drugId=' + drugId.val(), function(data) {
		$sr.each(data, function(key, value) {
			items += '<option value="">-- Select --</option>';
			for(var i in value) {
				var dateArr = value[i].columnIndex_1.split('-');
				var dat = dateArr[2]+'/'+dateArr[1]+'/'+dateArr[0]
				if(c == value[i].columnIndex_0) {
					items += '<option value="' + dat + '_' + value[i].columnIndex_2 + '" selected="selected" >' + value[i].columnIndex_0 + '</option>';
				} else {
					items += '<option value="' + dat + '_' + value[i].columnIndex_2 + '" >' + value[i].columnIndex_0 + '</option>'
				}
			}		
		});
	$sr("#lotId").html(items);
	alert(items)
	
	$sr("#lotId").change(function() {
		var lotVal = $sr('#lotId').val();
		var arr = new Array();
		arr = lotVal.split("_");
		if(arr.length > 1) {
			$sr("#expDateId").val(arr[0]);
			$sr("#dpId").val(arr[1]);
			$sr("#qtyId").attr("disabled", "");
		} else {
			//$sr("#expDateId").val("");
			//$sr("#qtyId").attr("disabled", "");
		}
	});
	});
	return false;
}

function populateLocations() {
	var retVar = $sr("input[name=retType]:checked").val();
	var options = '';
	var pharmaOptions = '';
	var drugFromStore = '';
	var drugFromPharma = '';
	
	if(retVar == 'internal') {
		$sr("#expDateId").val("").attr("disabled", "disabled");
		$sr("#qtyId").val("").attr("disabled", "disabled");
		$sr("#lotId").val("");
		$sr("#drugId").val("");
		
		returnType = 'internal';
		options += '<option value="">-- Select --</option>';
		drugFromPharma += '<option value="">-- Select --</option>';
		for(var i in pharmaIdArray) {
			options += '<option value="'+pharmaIdArray[i]+'">'+pharmaNameArray[i]+'</option>';
		}
	
		for(var i in pharmaDrugArray) {
			drugFromPharma += '<option value="'+pharmaDrugIdArray[i]+'">'+pharmaDrugArray[i]+'</option>';
		}

		pharmaOptions += '<option value="">-- Select --</option>';
		for(var i in locIdArray) {
			if(locIdArray[i] == dftLocId) {
				pharmaOptions += '<option value="'+locIdArray[i]+'" selected="selected">'+locNameArray[i]+'</option>';
			} else {
				pharmaOptions += '<option value="'+locIdArray[i]+'">'+locNameArray[i]+'</option>';
			}			
		}
		
		$sr("#fromId").html(options);
		$sr("#toId").html(pharmaOptions);
		//$sr("#drugId").html(drugFromPharma);


		$sr("#fromId").show();
		$sr("#toId").show();	
	}

	if(retVar == 'external') {
		$sr("#expDateId").val("").attr("disabled", "disabled");
		$sr("#qtyId").val("").attr("disabled", "disabled");
		$sr("#lotId").val("");
		$sr("#drugId").val("");
		
		returnType = 'external';
		options += '<option value="">-- Select --</option>';
		drugFromStore += '<option value="">-- Select --</option>';
		for(var i in locIdArray) {
			options += '<option value="'+locIdArray[i]+'">'+locNameArray[i]+'</option>';
		}
		$sr("#fromId").html(options);
		$sr("#toId").html(options);

		$sr("#fromId").show();
		$sr("#toId").show();
	}
}

</script>
<form method="post" id="drugstore">

<fieldset><legend><spring:message
	code="pharmacymanagement.returnForm" /></legend>
<table>
	<tr>
		<td><spring:message code="pharmacymanagement.returnDate" /></td>
		<td><input type="text" name="returnDate"
			onfocus="showCalendar(this)" size="10" id="returnDateId" /></td>
		<td colspan="2"><input type="checkbox" name="checkStock"
			id="showStockId" value="showStock" /><spring:message
			code="pharmacymanagement.checkToC" /></td>
	</tr>
	<tr>
		<td>Return type</td>
		<td><input id="intId" type="radio" name="retType"
			value="internal" />Internal</td>
		<td colspan="2"><input id="extId" type="radio" name="retType"
			value="external" />External</td>
	</tr>
	<tr>
		<td><spring:message code="pharmacymanagement.from" /></td>
		<td><select name="from" id="fromId">
		</select></td>
		<td><spring:message code="pharmacymanagement.to" /></td>
		<td><select name="to" id="toId">
		</select></td>
	</tr>
	<tr>
		<td colspan="6">
		<table class="return">
			<thead>
				<tr>
					<th><spring:message code="pharmacymanagement.designation" /></th>
					<th><spring:message code="pharmacymanagement.lotNr" /></th>
					<th><spring:message code="pharmacymanagement.expDate" /></th>
					<th><spring:message code="pharmacymanagement.qntyReturned" /></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
					<center><select name="drug" id="drugId">
						<option value="">-- Select --</option>
						<c:forEach items="${drugMap}" var="drug">
							<option value="${drug.key}">${drug.value}</option>
						</c:forEach>
					</select></center>
					</td>
					<td><span id="toReplace"> <select name="lot"
						id="lotId">
						<option>-- Select --</option>
					</select> </span>
					</td>
					<td>
					<center><input type="hidden" name="ars" id="arsId" value="" /> <input
						type="hidden" name="dp" id="dpId" value="" /> <input type="text"
						name="expDate" disabled="disabled" id="expDateId" size="11" value="" /></center>
					</td>
					<td>
					<center><input type="text" name="qtyRet"
						disabled="disabled" size="6" id="qtyId" value="" /></center>
					</td>
				</tr>
			</tbody>
		</table>
		</td>
	</tr>
	<tr>
		<td valign="top">Observation</td>
		<td colspan="6"><textarea rows="3" cols="43" name="observation"
			id="obsId"></textarea></td>
	</tr>
	<tr>
		<td><input id="formSubmitId" type="submit" value="Add"
			class="send" /></td>
	</tr>
</table>
</fieldset>
</form>
<br />
<br />
<div id="storeReturnId"></div>

</div>
<div style="clear: both;"></div>
</div>
<script>
$sr('#drugId').change(function() {
	var drToId = $sr('#fromId').val();
	$sr("#lotId").attr("disabled", "");
	$sr("#qtyId").val("");
	$sr("#expDateId").val("");
	if(drToId == dftLocName) {
		$sr('qtyId').attr('disabled', 'disabled');
	}
	//fillOptions('drugId', 'lotId', null);
	var drugId = $sr('#drugId');
	var lotId = $sr('#lotId');
	var items = '';
	$sr.getJSON('lot.htm?drugId=' + drugId.val(), function(value) {			
			
			items += '<option value="">-- Select --</option>';
			for(var i in value) {
				var dateArr = value[i].columnIndex_1.split('-');
				var dat = dateArr[2]+'/'+dateArr[1]+'/'+dateArr[0]
				//if(c == value[i].columnIndex_0) {
					//items += '<option value="' + dat + '_' + value[i].columnIndex_2 + '" selected="selected" >' + value[i].columnIndex_0 + '</option>';
				//} else {
					items += '<option value="' + dat + '_' + value[i].columnIndex_2 + '" >' + value[i].columnIndex_0 + '</option>'
				//}
			}		
		
	$sr("#lotId").html(items);
	
	$sr("#lotId").change(function() {
		var lotVal = $sr('#lotId').val();
		var arr = new Array();
		arr = lotVal.split("_");
		if(arr.length > 1) {
			$sr("#expDateId").val(arr[0]);
			$sr("#dpId").val(arr[1]);
			$sr("#qtyId").attr("disabled", "");
		} else {
			//$sr("#expDateId").val("");
			//$sr("#qtyId").attr("disabled", "");
		}
	});
	});
	
});
</script>
<%@ include file="/WEB-INF/template/footer.jsp"%>
