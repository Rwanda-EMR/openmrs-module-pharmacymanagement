<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<div id="tracerDrug">
	<table width="1119" cellspacing="0" border="1">
		<tbody>
			<tr class="tableHeadBlue">
	    		<td colspan="6">XIX. Stock Of Tracer Drugs </td>
	  		</tr>
		  <tr class="tbleHead">
			    <td width="16">&nbsp;</td>
			    <td width="203" class="style1">Tracer drug </td>
			    <td width="142" class="style1">Quantity Dispensed </td>
			    <td width="219" class="style1">Quantity Expired/Damaged/Lost</td>
			    <td width="165" class="style1">Stock at End of Month </td>
			    <td width="348" class="style1">Days of Stock out </td>
		  </tr>
		  	<c:forEach items="${reportList}" var="report" varStatus="status">
		  		<tr>
				    <td>${status.count}</td>
				    <td>${report.drugName}</td>
				    <td>${report.quantityDispensed}</td>
				    <td>${report.edl}</td>
				    <td>${report.solde}</td>
				    <td>${report.stockout}</td>				    
		  		</tr>
			</c:forEach>
	  </tbody>
	</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>