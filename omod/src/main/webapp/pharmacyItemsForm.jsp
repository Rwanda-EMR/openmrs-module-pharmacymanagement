<%@ include file="/WEB-INF/template/include.jsp"%>
<script type="text/javascript">
 alert("Tested IN");
</script>
<openmrs:require privilege="View Pharmacy management" otherwise="/login.htm"
	redirect="/module/pharmacymanagement/pharmacyrequestAdjust.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>



<%@ include file="/WEB-INF/template/footer.jsp"%>