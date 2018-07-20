package org.openmrs.module.pharmacymanagement.utils;

import java.text.ParseException;

public class Test {

	/**
	 * Auto generated method comment
	 * 
	 * @param args
	 * @throws ParseException
	 */

	public static void main(String[] args) {		
			String[] arr = {"28","22","23","89","44","75"};
			StringBuffer sb = new StringBuffer();
			int size = arr.length;
			int columnIndex = 0;
			for(String str : arr) {
				sb.append("\"columnIndex_"+columnIndex++ +"\":");
				sb.append("\"");
				if(str != null){
					sb.append(str);
				}
				sb.append("\"");
//				if(--size != 0) {
					sb.append(",");
//				}
			}
			
			System.out.println(sb);
	}

}
