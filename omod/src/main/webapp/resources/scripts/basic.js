/*
 * SimpleModal Basic Modal Dialog
 * http://www.ericmmartin.com/projects/simplemodal/
 * http://code.google.com/p/simplemodal/
 *
 * Copyright (c) 2010 Eric Martin - http://ericmmartin.com
 *
 * Licensed under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 *
 * Revision: $Id: basic.js 243 2010-03-15 14:23:14Z emartin24 $
 *
 */

jQuery(function ($) {
	$('.edit').click(function (e) {
		$('#edit-dialog-content').dialog();
		$('.ui-dialog').css({'width':'650px', 'height':'280px'});
		  return false;
	});
	
	$('.stop').click(function (e) {
		var index = this.id;
		var suffix = index.substring(index.indexOf("_") + 1);
		var varReason = document.getElementById("stopReasonId");
		var reason = $dm('#discontinuedReason_'+suffix).text();
		for ( var i = 0; i < varReason.options.length; i++) {
			if (varReason.options[i].value == reason) {
				varReason.selectedIndex = i;
				break;
			}
		}
		
		$('#stop-modal-content').dialog();
		$('.ui-dialog').css({'width':'400px', 'height':'auto'});
		  return false;
	});
	
	$('#create').click(function(e) {
		$('#edit-dialog-content').dialog();
		$('.ui-dialog').css({'width':'700px', 'height':'280px'});
		return false;
	});
	
	$('.popit').click(function(e) {
		$('#rpt-dialog-content').dialog();		
		  return false;
	});	
	
	$(".delete").click(function(e) {
		var deleteBtnId = this.id;
		var orderId = deleteBtnId.substring(deleteBtnId.indexOf("_") + 1);
		$dm('#orderToDelId').val(orderId);
		
		$("#delete-modal-content").dialog();
		$('.ui-dialog').css({'width':'200px', 'height':'100px'});
	});
	
	$(".edit-basic").click(function(e) {		
		$("#edit-modal-content").dialog();
		$('.ui-dialog').width(840);
		$('.ui-dialog').height(180);
		$('.ui-dialog').css({'left':'428.5px'});
		return false;
	});
	
	$('.basic').click(function (e) {
		$('#basic-modal-content').dialog();
		$('.ui-dialog').height(180);
		$('.ui-dialog').width(420);
		return false;
	});
});