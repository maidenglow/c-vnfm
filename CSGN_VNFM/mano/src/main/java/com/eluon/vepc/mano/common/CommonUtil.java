package com.eluon.vepc.mano.common;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

	private static List<String> flavour_flag = null;
	
	public static String getDfFlag(String pahtParam){
		
		if(flavour_flag == null){
			flavour_flag = new ArrayList<String>();
			flavour_flag.add("mme");
			flavour_flag.add("pgw_cp");
			flavour_flag.add("pgw_dp");
			flavour_flag.add("sgw_cp");
			flavour_flag.add("sgw_dp");
		}
		
		String temp = null;
		
		int index = 0;
		for(String s : flavour_flag){
			
			if(pahtParam.indexOf(s) == 0){
				temp = flavour_flag.get(index);
				break;
			}
			
			index++;
		}

		return temp;
	}
}
