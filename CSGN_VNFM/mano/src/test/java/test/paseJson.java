package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.TimerTask;

public class paseJson {

	public static void main(String args[]){
		
		
		String name = "ext03";
		String name3 = name.substring(0, name.length()-2);
		System.out.println(name3);
		String name2 = name.substring(name.length()-2);
		System.out.println(name2);
		int index = Integer.parseInt(name2);
		String desName = null;
		if(index%2 == 0){
			System.out.println(index -1);
			desName = name3+"0"+(index-1);
		}else {
			desName = name3+"0"+(index+1);
		}
		
		System.out.println(desName);
//		/**
//		 * 1. 체크한 알람(arrCheckArm)과 DB에 있는 알람(arrSeleDB)을 비교하여 중복된 알람(arrAreadyHave)을 뽑아낸다
//		 * 
//		 */
//		for (int i = 0; i < arrSeleDB.size(); i++) {
//			
//			for (int j = 0; j < arrCheckArm.size(); j++) {
//				if(arrSeleDB.get(i).equals(arrCheckArm.get(j))){
//					arrAreadyHave.add(arrSeleDB.get(i));
//				}
//			}
//		}
//		/**
//		 * 2. 체크한 알람(arrCheckArm)에서 중복된 알람을 제거 
//		 */
//		for (int i = 0; i < arrCheckArm.size(); i++) {
//			arrCheckArm.remove(arrAreadyHave.get(i));
//		}
//
//		/**
//		 * 3. 체크한 알람(arrCheckArm)을 insert
//		 */
//		
//		for (int i = 0; i < arrCheckArm.size(); i++) {
//			System.out.println(arrCheckArm.get(i));
//		}
//		
//		/**
//		 * 4. DB에 있는 알람에서 체크한 알람과 중복된 알람이 없는 것은 제거
//		 */
//		for (int i = 0; i < arrSeleDB.size(); i++) {
//			if
//		}
		
		
//		String test = "test.xml";
//		
//		System.out.println(test.replace(".xml", ""));
		
//		String vm_image = "http://112.170.41.50/image/mme_1.0.0.img";
//		String image = vm_image.substring(vm_image.lastIndexOf("/")+1, vm_image.lastIndexOf("."));
//		System.out.println(image);
//		
//		
//		String fileType = vm_image.substring(vm_image.lastIndexOf(".")+1);
//		System.out.println(fileType);
		
		
//		
//		System.out.println(imgName);
		
		
//		String test = "{\"tenantid\":\"admin\"}";
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			HashMap<String,?> paramMap = mapper.readValue(test, new TypeReference<HashMap<String,?>>() {});
//			System.out.println(paramMap.get("tenantid"));
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
}
