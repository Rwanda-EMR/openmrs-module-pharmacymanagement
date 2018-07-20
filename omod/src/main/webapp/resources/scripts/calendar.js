function showCalendar(obj) {
	makeCalendar(obj);
	if(self.gfPop)
		gfPop.fPopCalendar(obj);
	return false;
}

function makeCalendar(obj) {

	// turn off auto complete on inputs using this calendar
	if (document.getElementsByTagName) {
		var inputs = document.getElementsByTagName("input");
		for (var i=0;i<inputs.length; i++) {
			if (inputs[i].onclick &&
				inputs[i].onclick.toString().indexOf("showCalendar") != -1) {
					inputs[i].setAttribute("autocomplete", "off");
			}
		}
	}

	// make the iframe to contain the calendar
	var id = "gToday:normal.jsp";
	if (document.getElementById(id) == null) {
		var iframe = document.createElement("iframe");
		iframe.width=174;
		iframe.height=189;
		iframe.name=id;	// also defined in ipopeng.jsp as an IE hack.
		iframe.id = id;
		iframe.src= '../ipopeng.html'; // redirect the link of this file to the one in pharmacy module??????!?!?!?!?!??!?!
		iframe.scrolling='no';
		iframe.frameBorder='0';
		iframe.style.visibility = 'visible';
		iframe.style.position='absolute';
		iframe.style.zIndex='3000';
		iframe.style.top='-500px';
		iframe.style.left= '-500px';
		var bodies = document.getElementsByTagName("body");
		//var bodies = document.getElementById("stop-modal-content");
		var body = bodies[0];
		iframe.name = id;
		body.appendChild(iframe);
	}
}

if (addEvent) {
	addEvent(window, "load", makeCalendar);
}
else {
	makeCalendar();
}

function CompareDates(dateFormat)
{
    var str1 = document.getElementById("encDateId").value;
    var str2 = document.getElementById("nowId").value;
    var dt1 = null;
    var mon1 = null;
    var yr1 = null;
    var dt2 = null;
    var mon2 = null;
    var yr2 = null;
	if(dateFormat=='dd/mm/yyyy' || dateFormat=='jj/mm/aaaa') {
	    dt1  = parseInt(str1.substring(0,2),10);
	    mon1 = parseInt(str1.substring(3,5),10);
	    yr1  = parseInt(str1.substring(6,10),10);
	    dt2  = parseInt(str2.substring(0,2),10);
	    mon2 = parseInt(str2.substring(3,5),10);
	    yr2  = parseInt(str2.substring(6,10),10);
	} else if(dateFormat=='mm/dd/yyyy' || dateFormat=='mm/jj/aaaa') {
	    mon1  = parseInt(str1.substring(0,2),10);
	    dt1 = parseInt(str1.substring(3,5),10);
	    yr1  = parseInt(str1.substring(6,10),10);
	    mon2  = parseInt(str2.substring(0,2),10);
	    dt2 = parseInt(str2.substring(3,5),10);
	    yr2  = parseInt(str2.substring(6,10),10);
	} else{
		alert("Invalid date : "+dateFormat+": not supported !");
		$("#encDateId").val("");
		return;
	}
    var date1 = new Date(yr1, mon1, dt1);
    var date2 = new Date(yr2, mon2, dt2);
    
    if(date2 < date1)
    {
    	 $("#msgId").html("The date can't be in future");
    	 $("#msgId").addClass("error");
    	 $("#encDateId").val("");    	 
    }
    else
    {
    	$("#msgId").html("");
   	 	$("#msgId").removeClass("error");
    }
} 
