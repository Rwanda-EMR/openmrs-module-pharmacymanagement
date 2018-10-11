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
 * Revision: jQueryId: basic.js 243 2010-03-15 14:23:14Z emartin24 jQuery
 *
 */

jQuery(function (jQuery) {
	jQuery('.edit').click(function (e) {
		jQuery('#edit-dialog-content').dialog();
		jQuery('.ui-dialog').css({'width':'650px', 'height':'280px'});
		  return false;
	});
	
	jQuery('.stop').click(function (e) {
		var index = this.id;
		var suffix = index.substring(index.indexOf("_") + 1);
		var varReason = document.getElementById("stopReasonId");
		var reason = jQuery('#discontinuedReason_'+suffix).text();
		for ( var i = 0; i < varReason.options.length; i++) {
			if (varReason.options[i].value == reason) {
				varReason.selectedIndex = i;
				break;
			}
		}
		
		jQuery('#stop-modal-content').dialog();
		jQuery('.ui-dialog').css({'width':'400px', 'height':'auto'});
		  return false;
	});
	
	jQuery('#create').click(function(e) {
		jQuery('#edit-dialog-content').dialog();
		jQuery('.ui-dialog').css({'width':'700px', 'height':'280px'});
		return false;
	});
	
	jQuery('.popit').click(function(e) {
		jQuery('#rpt-dialog-content').dialog();		
		  return false;
	});	
	
	jQuery(".delete").click(function(e) {
		var deleteBtnId = this.id;
		var orderId = deleteBtnId.substring(deleteBtnId.indexOf("_") + 1);
		jQuery('#orderToDelId').val(orderId);
		
		jQuery("#delete-modal-content").dialog();
		jQuery('.ui-dialog').css({'width':'200px', 'height':'100px'});
	});
	
	jQuery(".edit-basic").click(function(e) {		
		jQuery("#edit-modal-content").dialog();
		jQuery('.ui-dialog').width(840);
		jQuery('.ui-dialog').height(180);
		jQuery('.ui-dialog').css({'left':'428.5px'});
		return false;
	});
	
	jQuery('.basic').click(function (e) {
		jQuery('#basic-modal-content').dialog();
		jQuery('.ui-dialog').height(180);
		jQuery('.ui-dialog').width(420);
		return false;
	});
});