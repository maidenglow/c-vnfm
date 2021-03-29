package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.xml.JDOMParser;

import com.eluon.vepc.mano.vo.OnBoardVO;
import com.google.gson.Gson;

public class JxPath{

	public final static String FlavorName = "name";
	public final static String FlavorId = "id";
	public final static String vMem = "ram";
	public final static String vCpu = "vcpus";
	public final static String RootDisk = "disk";
	public final static String ephemeral = "ephemeral";
	public final static String ephemeral2 = "OS-FLV-EXT-DATA:ephemeral";
	public final static String SwapDisk = "swap";
	public final static String projects = "projects";
	
	public void createFlavor(String file){
//		String FILE_PATH = "C://vnfd_mme.xml";
		
		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JDOMParser dom = new JDOMParser();
		 
		Object obj = dom.parseXML(in);
		JXPathContext context = JXPathContext.newContext(obj);
		String vm_image = (String)context.getValue("vnfd/vdu/vdu_info/vm_image");
		System.out.println("vm_image" +vm_image);
		
		
		ArrayList<HashMap<String,Object>> flavorList = new ArrayList<HashMap<String,Object>>();
		//List Node를 새로 구성할 때
		for(Iterator<Pointer> iter = context.iteratePointers("vnfd/deployment_flavour/deployment_flavour_info"); iter.hasNext();)
		{
			HashMap<String,Object> flavor = new HashMap<String,Object>();
//			FlavorVO flavor = new FlavorVO();
			Pointer flavourPath = iter.next();
			JXPathContext relativeContext = context.getRelativeContext(flavourPath);
			String name = (String)relativeContext.getValue("id");
			flavor.put(FlavorName,name.trim());
		    //한개의 값을 가져올때
//			String flavour_key = (String)relativeContext.getValue("flavour_key");
//			flavor.flavour_key = flavour_key.trim();
		   
		   //하나의 엘리먼트 반복 List
		   Iterator items  = relativeContext.iterate("constraint/item");
		   while (items.hasNext()) {
				String[] item = ((String) items.next()).split(",");
				if (item != null) {
					if (item[1].indexOf("vcpu") > -1) {
						flavor.put(vCpu,item[2].trim());
					} else if (item[1].indexOf("vmemory") > -1) {
						flavor.put(vMem,item[2].trim());
						flavor.put(SwapDisk,String.valueOf(Integer.parseInt(item[2].trim())*2));
					} else if (item[1].indexOf("vdisk") > -1) {
						flavor.put(RootDisk,item[2].trim());
					} 
					System.out.println(item);
				}
			}
			flavor.put(ephemeral,"0");
			flavor.put(ephemeral2,"0");
			List<String> list = new ArrayList<String>(); 
			flavor.put(projects, list);
			flavor.put("public", true);
			
			flavorList.add(flavor);
		}
//		printFlavorList(flavorList);
		Gson gson = new Gson();
		for (int i = 0; i < flavorList.size(); i++) {
			String param = gson.toJson(flavorList.get(i));
			System.out.println(param);
		}
	}
	
	public void printFlavorList(ArrayList<HashMap<String,Object>> flavorList){
		for (int i = 0; i < flavorList.size(); i++) {
			HashMap<String,Object> flavor = flavorList.get(i);
			System.out.println(flavor.get(ephemeral));
			System.out.println(flavor.get(ephemeral2));
			System.out.println(flavor.get(FlavorName));
//			System.out.println(flavor.flavour_key);
			System.out.println(flavor.get(RootDisk));
			System.out.println(flavor.get(SwapDisk));
			System.out.println(flavor.get(vCpu));
			System.out.println(flavor.get(vMem));
			System.out.println(flavor.get(projects));
			System.out.println(flavor.get("public"));
		}

	}
	
	
	public String onBoardList(String strFile){
		List<OnBoardVO> onBoardList = new ArrayList<OnBoardVO>();
		OnBoardVO vo = new OnBoardVO();
		String FILE_PATH = "C://test";
		
		File file = new File(FILE_PATH);
		File[] listFiles = file.listFiles();
		String outString = null;
		for (int i = 0; i < listFiles.length; i++) {
			if(listFiles[i].isFile()&& listFiles[i].getName().indexOf("xml") > -1){
				System.out.println("filename : "+listFiles[i].getName());
				System.out.println("PathAndName : " +listFiles[i].getPath());
				System.out.println("Path : "+ listFiles[i].getParentFile());
				vo.setName(listFiles[i].getName());
				vo.setPath(listFiles[i].getParentFile().getPath());
				vo.setPathAndName(listFiles[i].getPath());
				
				BufferedReader fin = null; // 파일 스트림 
				String line = null;  // 파일에서 한줄 읽어 들인 문자열
				StringBuilder  sb = new StringBuilder();
				try{
					fin = new BufferedReader(new FileReader(listFiles[i].getPath()));
					while((line = fin.readLine()) != null) {
						sb.append(line); // 읽어온 한줄을 추가 한다.
						sb.append("\n");
					} 
					fin.close(); // 스트림을 닫는다..
					System.out.println(sb.toString());
					vo.setContents(sb.toString());
					
				} catch(IOException e) {
					System.out.println("파일 읽는 중 오류!");
					e.printStackTrace();
				}
				onBoardList.add(vo);
				
			}
			Gson gson = new Gson();
			outString = gson.toJson(onBoardList);
			System.out.println(outString);
		}
		
		return outString;
		
	}
	
	public static void main(String args[]) {
		
		JxPath jxPath = new JxPath();
		String FILE_PATH = "C://vnfd_mme.xml";
		jxPath.createFlavor(FILE_PATH);
		
//		jxPath.onBoardList(FILE_PATH);
		
	}
}


