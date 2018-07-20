/**
 * Auto generated file comment
 */
package org.openmrs.module.pharmacymanagement.view.extn;

import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.View;

/**
 *
 */
public class AjaxView implements View {
	private static final Log log = LogFactory.getLog(AjaxView.class);
	
	
	public String getContentType() {
		return "application/json";
	}
	
	

	@SuppressWarnings({ "unchecked"})
	public void render(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info(map);
		
		// Disable caching
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/json");
		
		
		List<Object[]> rows = (List<Object[]>) map.get(1);
		Iterator<Object[]> rowIterator = rows.iterator();
		StringBuffer sb = new StringBuffer();
		Object[] row = null;
		int columnIndex = 0;
		//[{"columnIndex_0":"08/08","columnIndex_1":"2016-06-01","columnIndex_2":"12",}]
		//sb.append("{\"rows\":");
		sb.append("[");
		while(rowIterator != null && rowIterator.hasNext()){
			sb.append("{");
			row = rowIterator.next();
			if(row != null){
				columnIndex = 0;
				int size = row.length;
				for(Object element:row){
					sb.append("\"columnIndex_"+columnIndex++ +"\":");
					sb.append("\"");
					if(element != null){
						sb.append(element);
					}
					sb.append("\"");
					if(--size != 0) {
						sb.append(",");
					}
				}
			}	
			sb.append("}");
			if(rowIterator.hasNext()){
				sb.append(",");
			}
		}
		sb.append("]");
//		sb.append("}");
		OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream());
		osw.write(sb.toString());
		osw.flush();
	}
}
