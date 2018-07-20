<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Pharmacy" otherwise="/login.htm" redirect="/module/pharmacymanagement/drugDetail.form"/>
<div>
<div id="outer">
<%@ include file="template/leftMenu.jsp"%>
</div>

<div id="middle">
<%@ include file="template/adminLocalHeader.jsp"%>

<form class="box" method="post">
<table>

	<tr>
		<td>Drug</td>
		<td colspan="5"><input type="hidden" name="dopId" value="${dopId}" /><openmrs:fieldGen
			type="org.openmrs.Drug" formFieldName="drug"
			val="${drugDetails.drug}" /></td>
	</tr>

	<tr>
		<td>Tablet</td>
		<td><input type="radio" name="forme" value="tablet" ${drugDetails.forme eq 'tablet' ? 'checked="checked"' : '' } /></td>
		<td>Bottle</td>
		<td><input type="radio" name="forme" value="bottle" ${drugDetails.forme eq 'bottle' ? 'checked="checked"' : '' } /></td>
		<td>Powder</td>
		<td><input type="radio" name="forme" value="powder" ${drugDetails.forme eq 'powder' ? 'checked="checked"' : '' } /></td>
	</tr>
	
	<tr>
		<td>Units</td>
		<td colspan="5"><input type="text" name="units" value="${drugDetails.units}" /></td>
	</tr>	

	<tr>
		<td>Measurement Unit</td>
		<td colspan="5"><input type="text" name="m_unit" value="${drugDetails.measurementUnit}" /></td>
	</tr>
	

	<tr>
		<td><input type="submit" value="Save" class="send" /></td>
	</tr>

</table>
</form>
</div>
<div style="clear: both;"></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>