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
import com.eluon.vepc.mano.vo.VNFDescriptorVO;
import com.google.gson.Gson;


public class JxPathcsgn{

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
//		String FILE_PATH = "C://vnfd_csgn.xml";
		
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
		String vnfd_id = (String)context.getValue("vnfd/id");
		String vnfd_name = (String)context.getValue("vnfd/name");
		System.out.println("id :" +vnfd_id);
		
		//List Node를 새로 구성할 때
		for(Iterator<Pointer> iter = context.iteratePointers("vnfd/vnfcs/vnfc"); iter.hasNext();)
		{

			VNFDescriptorVO vnfVO = new VNFDescriptorVO();
			Pointer flavourPath = iter.next();
			JXPathContext relativeContext = context.getRelativeContext(flavourPath);
			
			String vnfc_name = (String)relativeContext.getValue("name");
			String high_availability = (String)relativeContext.getValue("high_availability");
			String vmem = (String)relativeContext.getValue("flavor/vmem");
			String vcpu = (String)relativeContext.getValue("flavor/vcpu");
			String vdisk = (String)relativeContext.getValue("flavor/vdisk");
			String num_instances = (String)relativeContext.getValue("num_instances");
			String max_instances = (String)relativeContext.getValue("max_instances");
			String priority = (String)relativeContext.getValue("priority");
			String scaling_flag = (String)relativeContext.getValue("scaling_flag");
			String in_policy = (String)relativeContext.getValue("scaling/in_policy");
			String out_policy = (String)relativeContext.getValue("scaling/out_policy");
			
			vnfVO.vnfd_id = vnfd_id;
			vnfVO.vnfd_name = vnfd_name;
//			vnfVO.vnfc_name = vnfd_id+"_"+vnfc_name;
//			vnfVO.vnfc_real_name = vnfc_name;
//			vnfVO.high_availability = high_availability;
//			
//			vnfVO.flavor_name = vnfd_id+"_"+vnfc_name;
//			vnfVO.flavor_real_name = vnfc_name;
//			vnfVO.flavor_vmem = vmem;
//			vnfVO.flavor_vcpu = vcpu;
//			vnfVO.flavor_vdisk = vdisk;
//			vnfVO.num_instances = num_instances;
//			vnfVO.max_instances = max_instances;
//			vnfVO.priority = priority;
//			vnfVO.scaling_flag = scaling_flag;
			
			System.out.println("name			: "+vnfc_name);
			System.out.println("high_availability	: "+high_availability);
			System.out.println("vmem			: "+vmem);
			System.out.println("vcpu			: "+vcpu);
			System.out.println("vdisk			: "+vdisk);
			System.out.println("num_instances		: "+num_instances);
			System.out.println("max_instances		: "+max_instances);
			System.out.println("priority		: "+priority);
			System.out.println("scaling_flag		: "+scaling_flag);
			System.out.println("in_policy		: "+in_policy);
			System.out.println("out_policy		: "+out_policy);
			
			
			//List Node를 새로 구성할 때
			for(Iterator<Pointer> initer = relativeContext.iteratePointers("network/network_info"); initer.hasNext();)
			{
				Pointer networkPath = initer.next();
				JXPathContext netContext = relativeContext.getRelativeContext(networkPath);
				System.out.println((String)netContext.getValue("name"));
				for(Iterator<Pointer> initer2 = netContext.iteratePointers("net_info"); initer2.hasNext();)
				{
					Pointer networkInfoPath = initer2.next();
					JXPathContext netInfoContext = netContext.getRelativeContext(networkInfoPath);
					System.out.println((String)netInfoContext.getValue("network_name"));
					System.out.println((String)netInfoContext.getValue("ip_addr"));
				}
			}
			
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
		
		JxPathcsgn jxPath = new JxPathcsgn();
		String FILE_PATH = "C://Users/chungho/Downloads/vnfd_csgn_20160822.xml";
//		String FILE_PATH = "C://vnfd_mme.xml";
		jxPath.createFlavor(FILE_PATH);
		
//		jxPath.onBoardList(FILE_PATH);
		
	}
}


