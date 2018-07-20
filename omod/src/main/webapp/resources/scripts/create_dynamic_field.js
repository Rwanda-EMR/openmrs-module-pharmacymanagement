function createDeleteButton(baseName) {
	var deleteButton = $dsm(document.createElement("span")).attr("id",
			"delete_" + fieldGroupCount).attr("class", "redbox").text("X");
	deleteButton.click( function() {
		var idString = $dsm(this).attr("id");
		var selectorText = "#" + baseName + "tableid_"
				+ idString.substring(idString.indexOf("_") + 1);

		$dsm(selectorText).hide(200, function() {
			$dsm(selectorText).remove();
		});
	});
	return deleteButton;
}

function addOptionsToSelect(selectElement, displayArray, valueArray) {
	selectElement.append($dsm(document.createElement("option")).attr("value", "")
			.text("-- Select --"));
	for ( var j = 0; j < displayArray.length; j++) {
		selectElement.append($dsm(document.createElement("option")).attr("value",
				valueArray[j]).text(displayArray[j]));
	}
}

function createNakedOptionSelect(nameValue, displayArray, valueArray, classAttr) {
	var selectElement = $dsm(document.createElement("select")).attr("id",
			fieldGroupCount).attr("class", classAttr).attr("name", nameValue);
	addOptionsToSelect(selectElement, displayArray, valueArray);
	var tableRow = $dsm(document.createElement("tr")).append(
			$dsm(document.createElement("td"))).append(
			$dsm(document.createElement("td")).append(selectElement));

	return tableRow;
}

function createDrug(baseName, displayArray, valueArray, classAttr) {
	// the containing table
	var table = $dsm(document.createElement("table")).attr("id",
			baseName + "tableid_" + ++fieldGroupCount).attr("class", "drugs_tables").attr("width", "100%");

	// designation selector
	var optionSelectRow = createNakedOptionSelect("drugs_" + fieldGroupCount,
			displayArray, valueArray, classAttr);

	// amount requisitioned
	var amountReqInputElement = $dsm(document.createElement("input")).attr("type",
			"text").attr("name", "drugneeded_" + fieldGroupCount).attr("size",
			"3");	

	// delete button
	var deleteButton = createDeleteButton(baseName);
		
	// adding row fields
	table.append($dsm(document.createElement("tr")).attr("align", "center")
			.append($dsm(document.createElement("td")).attr("width", "70%").append(optionSelectRow))
			.append($dsm(document.createElement("td")).attr("width", "20%").append(amountReqInputElement))
			.append($dsm(document.createElement("td")).attr("width", "10%").append(deleteButton)));

	// add the line separator between tables
	table.append($dsm(document.createElement("tr")).append(
			$dsm(document.createElement("td")).attr("colspan", "5").append(
					$dsm(document.createElement("hr")).attr("color", "#C9C9C9"))));

	// add the entire set of elements to the div
	table.hide();
	$dsm("#" + baseName).append(table);

	$dsm(".drugs_tables").removeAttr("style");
	table.show(200);
}

function createConsumable(baseName, displayArray, valueArray, classAttr) {
	// the containing table
	var table = $dsm(document.createElement("table")).attr("id",
			baseName + "tableid_" + ++fieldGroupCount).attr("class", "consumable_tables").attr("width", "100%");

	// designation selector
	var optionSelectRow = createNakedOptionSelect("consumable_" + fieldGroupCount,
			displayArray, valueArray, classAttr);

	// amount requisitioned
	var amountReqInputElement = $dsm(document.createElement("input")).attr("type",
			"text").attr("name", "consneeded_" + fieldGroupCount).attr("size",
			"3");	

	// delete button
	var deleteButton = createDeleteButton(baseName);
		
	// adding row fields
	table.append($dsm(document.createElement("tr")).attr("align", "center")
			.append($dsm(document.createElement("td")).attr("width", "50%").append(optionSelectRow))
			.append($dsm(document.createElement("td")).attr("width", "20%").append(amountReqInputElement))
			.append($dsm(document.createElement("td")).attr("width", "10%").append(deleteButton)));

	// add the line separator between tables
	table.append($dsm(document.createElement("tr")).append(
			$dsm(document.createElement("td")).attr("colspan", "5").append(
					$dsm(document.createElement("hr")).attr("color", "#C9C9C9"))));

	// add the entire set of elements to the div
	table.hide();
	$dsm("#" + baseName).append(table);

	$dsm(".consumable_tables").removeAttr("style");
	table.show(200);
}


function createReturnDrug(baseName, displayArray, valueArray, classAttr) {
	// the containing table
	var table = $dsm(document.createElement("table")).attr("id",
			baseName + "tableid_" + ++fieldGroupCount).attr("class", "drugs_tables").attr("width", "100%");

	// designation selector
	var designation = createNakedOptionSelect("drugs_" + fieldGroupCount,
			displayArray, valueArray, classAttr);

	// amount to be returned
	var amountToRetInputElement = $dsm(document.createElement("input")).attr("type",
			"text").attr("name", "amounttoret_" + fieldGroupCount).attr("size",
			"3");
	
	// observation
	var observationInputElmt = $dsm(document.createElement("textArea")).attr("name","observation_"+fieldGroupCount).attr("rows","5").attr("cols","50");	

	// delete button
	var deleteButton = createDeleteButton(baseName);
	
	// adding row fields
	table.append($dsm(document.createElement("tr")).attr("align", "center")
			.append($dsm(document.createElement("td")).attr("width", "50%").append(designation))
			.append($dsm(document.createElement("td")).attr("width", "10%").append(amountToRetInputElement))
			.append($dsm(document.createElement("td")).attr("width", "35%").append(observationInputElmt))
			.append($dsm(document.createElement("td")).attr("width", "50%").append(deleteButton)));

	// add the line separator between tables
	table.append($dsm(document.createElement("tr")).append(
			$dsm(document.createElement("td")).attr("colspan", "5").append(
					$dsm(document.createElement("hr")).attr("color", "#C9C9C9"))));

	// add the entire set of elements to the div
	table.hide();
	$dsm("#" + baseName).append(table);

	$dsm(".drugs_tables").removeAttr("style");
	table.show(200);
}
function createDrugField(baseName, displayArray, valueArray, classAttr,
		titleArray) {
	// the containing table
	var table = $dsm(document.createElement("table")).attr("id",
			baseName + "tableid_" + ++fieldGroupCount).attr("width", "100%");

	// designation selector
	var optionSelectRow = createNakedOptionSelect("drugs_" + fieldGroupCount,
			displayArray, valueArray, classAttr);
	
	// field for the quantity to request
	var quantity = $dsm(document.createElement("input")).attr("type", "text")
			.attr("name", "qnty_" + fieldGroupCount).attr("size", "5");
	
	// delete button
	var deleteButton = createDeleteButton(baseName);

	// adding row fields
	table.append($dsm(document.createElement("tr")).attr("align", "center")
			.append(
					$dsm(document.createElement("td")).attr("width", "30%").text(
							titleArray[0])).append(
					$dsm(document.createElement("td")).attr("width", "15%")
							.append(optionSelectRow)).append(
					$dsm(document.createElement("td")).attr("width", "15%")
							.append(titleArray[1])).append(
					$dsm(document.createElement("td")).attr("width", "30%")
							.append(quantity)).append(
					$dsm(document.createElement("td")).attr("width", "10%")
							.append(deleteButton))
							);

	// add the line separator between tables
	table.append($dsm(document.createElement("tr")).append(
			$dsm(document.createElement("td")).attr("colspan", "5").append(
					$dsm(document.createElement("hr")).attr("color", "#C9C9C9"))));

	// add the entire set of elements to the div
	table.hide();
	$dsm("#" + baseName).append(table);
	table.show(200);
}

function fromLocPharmaReturn(baseName, displayArray, valueArray, classAttr) {
	// the containing table
	var table = $(document.createElement("table")).attr("id",
			baseName + "tableid_" + ++fieldGroupCount);

	// designation selector
	var optionSelectRow = createNakedOptionSelect("locPharma_" + fieldGroupCount,
			displayArray, valueArray, classAttr);

	// adding row fields
	table.append($(document.createElement("tr")).attr("align", "center")
			.append($(document.createElement("td")).attr("width", "30%").append(optionSelectRow)));

	// add the line separator between tables
	table.append($(document.createElement("tr")).append(
			$(document.createElement("td")).attr("colspan", "5").append(
					$(document.createElement("hr")).attr("color", "#C9C9C9"))));

	// add the entire set of elements to the div
	table.hide();
	$("#" + baseName).append(table);
	table.show(200);
}

/** a function which helps in validation to avoid that user input a future date **/

function CompareDates(dateFormat)
{
	var dt1 = null;
    var mon1 = null;
    var yr1 = null;
    var str2 = null;
    
	var str1 = document.getElementById("encDateId").value;
    var now  = new Date();
    var nowDay = now.getDate().toString().length == 1 ? '0'+now.getDate() : now.getDate();
    var month = now.getMonth()+1;
    var nowMonth = month.toString().length == 1 ? '0' + month : month;
    var nowYear = now.getYear()+1900;
    
    //var str2 = nowDay + "/" + nowMonth + "/" + nowYear;
    
    //alert("str1: -" + str1 + "- str2: -" + str2 + "-");
    
    
	if(dateFormat=='dd/mm/yyyy' || dateFormat=='jj/mm/aaaa') { 
		str2 = nowDay + "/" + nowMonth + "/" + nowYear;
	    dt1  = parseInt(str1.substring(0,2),10);
	    mon1 = parseInt(str1.substring(3,5),10) - 1;
	    yr1  = parseInt(str1.substring(6,10),10);
	    dt2  = parseInt(nowDay);
	    mon2 = parseInt(nowMonth);
	    yr2  = parseInt(nowYear);
	} else if(dateFormat=='mm/dd/yyyy' || dateFormat=='mm/jj/aaaa') {
		str2 = nowMonth + "/" + nowDay + "/" +  nowYear;
	    mon1  = parseInt(str1.substring(0,2),10) - 1;
	    dt1 = parseInt(str1.substring(3,5),10);
	    yr1  = parseInt(str1.substring(6,10),10);
	    mon2  = parseInt(nowMonth);
	    dt2 = parseInt(nowDay);
	    yr2  = parseInt(nowYear);
	} else{
		alert("Invalid date : "+dateFormat+": not supported !");
		$dsm("#encDateId").val("");
		return;
	}
    var date1 = new Date(yr1, mon1, dt1);
    if(now < date1)
    {
    	 $dsm("#msgErrorId").html("The date can't be in future");
    	 $dsm("#msgErrorId").addClass("error");
    	 $dsm("#msgErrorId").css({'font-size':'10px'});
    	 $dsm("#encDateId").val("");    	 
    }
    else
    {
    	$dsm("#msgErrorId").html("");
   	 	$dsm("#msgErrorId").removeClass("error");
    }
} 
