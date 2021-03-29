package com.eluon.vepc.mano.rest.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.xml.JDOMParser;
import org.openstack4j.api.Builders;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.VNCConsole;
import org.openstack4j.openstack.image.domain.GlanceImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eluon.vepc.mano.rest.validator.ComputeValidator;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ComputeService;
import com.eluon.vepc.mano.service.ImageService;
import com.eluon.vepc.mano.service.NetworkingService;
import com.eluon.vepc.mano.service.VNFMService;
import com.eluon.vepc.mano.vo.EventLogVO;
import com.eluon.vepc.mano.vo.RepositoryVO;
import com.eluon.vepc.mano.vo.VNFDescriptorVO;
import com.eluon.vepc.mano.vo.VnfcVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Compute Controller (VNFMController)
 *
 * @version $Id: VNFMController.java
 */
@RestController
@RequestMapping("/vnfd")
public class VNFMController{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	protected final static Logger loggerSin = LoggerFactory.getLogger("MANO_PROCESS_SIN");
	
	public final static String FlavorName = "name";
	public final static String FlavorId = "id";
	public final static String vMem = "ram";
	public final static String vCpu = "vcpus";
	public final static String RootDisk = "disk";
	public final static String ephemeral = "ephemeral";
	public final static String ephemeral2 = "OS-FLV-EXT-DATA:ephemeral";
	public final static String SwapDisk = "swap";
	public final static String projects = "projects";
	
	@Autowired
	private ComputeService computeService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private NetworkingService networkingService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private VNFMService vnfmService;
	
	ObjectMapper mapper = new ObjectMapper();
	private String errorMessage;
	
	/**
	 * 이미지 생성
	 * @param minRam
	 * @param minDisk
	 * @param vnfdVO
	 * @return
	 */
	
	public String createImages(int minRam, int minDisk, VNFDescriptorVO vnfdVO) {
		
		GlanceImage glImage = (GlanceImage) Builders.image()
				.name(vnfdVO.vnfd_id)
				.minRam((long)minRam)
				.minDisk((long)minDisk)
				.isPublic(true)
				.property("description", "image").build();
		glImage.isDeleted(false);
		glImage.isProtected(false);
		
		org.openstack4j.model.image.Image image = imageService.createImage(glImage, null);
		
		VNFDescriptorVO vnfdInfoVO = new VNFDescriptorVO();
		vnfdInfoVO.vnfd_id = vnfdVO.vnfd_id;
		vnfdInfoVO.image_id =image.getId();
		
		vnfmService.updateVnfd(vnfdInfoVO);
		
		return image.getId();
	}
	
	
	/**
	 * Flavor 정보를 삭제 
	 * @param arrFlavorId
	 */
	public void deleteFlavorArr(ArrayList<String> arrFlavorId){
		for (int i = 0; i < arrFlavorId.size(); i++) {
			computeService.deleteFlavor(arrFlavorId.get(i));
		}
	}
	
	/**
	 * Port 정보를 삭제
	 * @param arrPortId
	 */
	public void deletePortArr(ArrayList<String> arrPortId){
		
		for (int i = 0; i < arrPortId.size(); i++) {
			networkingService.deletePort(arrPortId.get(i));
		}
	}
	
	/**
	 * 생성하다가 문제가 생겼을 시 롤백
	 * @param vnfdId
	 * @param arrFlavorId
	 * @param arrPortId
	 */
	public void rollBackProcess(String vnfdId, ArrayList<String> arrFlavorId, ArrayList<String> arrPortId){
		logger.debug("rollBackProcess"+vnfdId+"::"+arrFlavorId.toString()+"::"+arrPortId.toString());
		for (int i = 0; i < arrFlavorId.size(); i++) {
			computeService.deleteFlavor(arrFlavorId.get(i));
			VnfcVO param2 = new VnfcVO();
			param2.flavor_id = arrFlavorId.get(i);
			vnfmService.deleteVnfc(param2);
		}
		
		for (int i = 0; i < arrPortId.size(); i++) {
			networkingService.deletePort(arrPortId.get(i));
		}
	}
	
	/**
	 * Descriptor 파일 정보를 파싱하여 저장 및 관련 정보 생성
	 * @param in
	 * @param tenantId
	 * @param fileName
	 * @return
	 */
	public boolean setVnfdInfo(InputStream in, String tenantId, String fileName){
		Gson gson = new Gson();

		JDOMParser dom = new JDOMParser();
		 
		Object obj = dom.parseXML(in);
		JXPathContext context = JXPathContext.newContext(obj);
		String vnfd_id = fileName.replace(".xml", "");
		String vnfd_name = (String)context.getValue("vnfd/name");
		
		HashMap<String,Object> flavor = new HashMap<String,Object>();
		//List Node를 새로 구성할 때
		int minRam = 0;
		int minDisk = 0;
		VNFDescriptorVO vnfdVO = null;
		VnfcVO vnfcVO = null;
		ArrayList<String> arrFlavorId = new ArrayList<String>();
		ArrayList<String> arrPortId = new ArrayList<String>();
		
		vnfdVO = new VNFDescriptorVO();
		vnfdVO.vnfd_id = vnfd_id.trim();
		vnfdVO.vnfd_name = vnfd_name.trim();
		
		for(Iterator<Pointer> iter = context.iteratePointers("vnfd/vnfcs/vnfc"); iter.hasNext();)
		{
			vnfcVO = new VnfcVO();
			Pointer flavourPath = iter.next();
			JXPathContext relativeContext = context.getRelativeContext(flavourPath);
			
			String vnfc_name = (String)relativeContext.getValue("name");
			String affinity = (String)relativeContext.getValue("affinity_type");
			String vmem = (String)relativeContext.getValue("flavor/vmem");
			String vcpu = (String)relativeContext.getValue("flavor/vcpu");
			String cpu_pinning = (String)relativeContext.getValue("flavor/cpu_pinning");
			String vdisk = (String)relativeContext.getValue("flavor/vdisk");
//			String network = (String)relativeContext.getValue("network");
			String num_instances = (String)relativeContext.getValue("num_instances");
			String max_instances = (String)relativeContext.getValue("max_instances");
			String priority = (String)relativeContext.getValue("priority");
			String scaling_flag = (String)relativeContext.getValue("scaling_flag");
			String in_policy = (String)relativeContext.getValue("scaling/in_policy");
			String out_policy = (String)relativeContext.getValue("scaling/out_policy");
			String break_policy = (String)relativeContext.getValue("break_policy");
			
			
			vnfcVO.vnfd_id = vnfd_id.trim();
			vnfcVO.vnfc_real_name = vnfc_name.trim();
			if(break_policy != null)
			vnfcVO.break_policy = break_policy.trim();
			if(affinity != null && affinity != ""){
				vnfcVO.affinity = affinity.trim();
			}
			
			vnfcVO.flavor_real_name = vnfc_name.trim();
			vnfcVO.flavor_vmem = vmem.trim();
			vnfcVO.flavor_vcpu = vcpu.trim();
			vnfcVO.flavor_vdisk = vdisk.trim();
			
			vnfcVO.num_instances = num_instances.trim();
			vnfcVO.max_instances = max_instances.trim();
			vnfcVO.priority = priority.trim();
			vnfcVO.scaling_flag = scaling_flag.trim();
			vnfcVO.in_policy = in_policy.trim();
			vnfcVO.out_policy = out_policy.trim();
			
			if(minRam == 0){
				minRam = Integer.parseInt(vmem.trim());
			}else if(minRam > Integer.parseInt(vmem.trim())){
				minRam = Integer.parseInt(vmem.trim());
			}
			
			if(minDisk == 0){
				minDisk = Integer.parseInt(vdisk.trim());
			}else if(minDisk > Integer.parseInt(vdisk.trim())){
				minDisk = Integer.parseInt(vdisk.trim());
			}
			
			flavor.put(FlavorName,vnfd_id+vnfc_name.trim());
			flavor.put(vCpu,vcpu.trim());
			int memory = Integer.parseInt(vmem.trim())*1024;
			flavor.put(vMem,String.valueOf(memory));
			flavor.put(SwapDisk,memory*2);
			flavor.put(RootDisk,vdisk.trim());
			flavor.put(ephemeral,"0");
			flavor.put(ephemeral2,"0");
			List<String> list = new ArrayList<String>(); 
			flavor.put(projects, list);
			flavor.put("public", true);
			
			//List Node를 새로 구성할 때
			String networkIp = null;
			String network_name = null;
			List<VnfcVO> listVnfc = vnfmService.getVnfcList(null);
			for(Iterator<Pointer> initer = relativeContext.iteratePointers("network/network_info"); initer.hasNext();)
			{
				Pointer networkPath = initer.next();
				JXPathContext netContext = relativeContext.getRelativeContext(networkPath);
				for(Iterator<Pointer> initer2 = netContext.iteratePointers("net_info"); initer2.hasNext();)
				{
					Pointer networkInfoPath = initer2.next();
					JXPathContext netInfoContext = netContext.getRelativeContext(networkInfoPath);
					network_name = (String)netInfoContext.getValue("network_name");
					String ip_addr = (String)netInfoContext.getValue("ip_addr");
					
					for (int i = 0; i < listVnfc.size(); i++) {
						if(listVnfc.get(i).network.indexOf(ip_addr)> -1){
							errorMessage = "중복된 네트워크  오류 : " + ip_addr;
							logger.debug(errorMessage);
							rollBackProcess(vnfdVO.vnfd_id, arrFlavorId, arrPortId);
							return false;
						}
					}
					if(ip_addr != null){
						if(networkIp == null ){
							networkIp = ip_addr+";"+network_name;
						}else {
							networkIp = networkIp+";"+ip_addr+";"+network_name;
						}
					}
				}
			}
			
			String arrTypeNetIp[] = networkIp.split(";"); 
			String flavorId = null;
			for (int a = 0; a < Integer.parseInt(vnfcVO.num_instances); a++) {
				String vnfcName = null;
				vnfcName = vnfc_name+"_"+(a+1);
				String portName = null;
				String pid = null;
				try {
					for (int i = 0; i < arrTypeNetIp.length; i = i+2 ) {
						portName = vnfcName+"_"+arrTypeNetIp[i+1];
						String portId = vnfmService.setNetwork(portName, arrTypeNetIp[i+1], arrTypeNetIp[i].split(",")[a]);
						arrPortId.add(portId);
						if(pid == null){
							pid = portId;
						}else {
							pid = pid+","+portId;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorMessage = "createPort error... name : " + portName;
					logger.debug(errorMessage);
					rollBackProcess(vnfdVO.vnfd_id, arrFlavorId, arrPortId);
					return false;
				}
				
				try {
					vnfcVO.vnfc_name = vnfcName;
					vnfcVO.flavor_name = vnfd_id+"_"+vnfcVO.vnfc_real_name;
					flavor.put(FlavorName,vnfcVO.flavor_name);
					if(a == 0){
						Flavor flavorInfo = vnfmService.createFlavor(tenantId, gson.toJson(flavor));
						flavorId = flavorInfo.getId();
						if(cpu_pinning != null || cpu_pinning != ""){
							Map<String, String> map = new HashMap<String, String>();
							String arrPining[] = cpu_pinning.split(";");
							for (int i = 0; i < arrPining.length; i++) {
								String piningData[] = arrPining[i].split(",");
								map.put(piningData[0], piningData[1]);
							}
							computeService.setProperty(flavorId, map);
						}
						arrFlavorId.add(flavorId);
					}
					vnfcVO.flavor_id = flavorId;
					vnfcVO.port_id = pid;
					vnfcVO.network = networkIp;
					vnfmService.addVnfcInfo(vnfcVO);
				} catch (IOException e) {
					e.printStackTrace();
					errorMessage = "createFlavor error....  name : " + flavor.get(FlavorName);
					logger.debug(errorMessage);
					rollBackProcess(vnfdVO.vnfd_id, arrFlavorId, arrPortId);
					return false;
				}
			}
			
		}
		try {
			vnfdVO.image_id = createImages(minRam, minDisk, vnfdVO);
			vnfmService.addVnfdInfo(vnfdVO);
		} catch (Exception e2) {
			e2.printStackTrace();
			errorMessage = "createImages error....  name : "+vnfdVO.vnfd_id;
			logger.debug(errorMessage);
			rollBackProcess(vnfdVO.vnfd_id, arrFlavorId, arrPortId);
			return false;
		}
		return true;
	}
	
	/**
	 * Descriptor 파일 업로드
	 * @param data
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException
	 */
	@RequestMapping(value = "/descriptor/upload", method = RequestMethod.POST)
	public ActionResponse createOnBoard(@RequestPart(value = "file") MultipartFile file ,
			@RequestPart(required = false, value = "data") String data) throws IOException {
		
		String tenantid = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			HashMap<String,String> paramMap = mapper.readValue(data, new TypeReference<HashMap<String,String>>() {});
			tenantid = paramMap.get("tenantid");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String fileName = file.getOriginalFilename();
		InputStream in = file.getInputStream();
		
		List<VNFDescriptorVO> listVnfD = vnfmService.getVnfdList(null);
		if(listVnfD.size() > 5){
			ActionResponse.actionFailed("Descriptor 갯수는 6개를 초과 할 수 없습니다.", 500);
		}
		
		if(!setVnfdInfo(in, tenantid, fileName)){
			return ActionResponse.actionFailed(errorMessage, 500);
		}
		
		String FILE_PATH = config.getString("FILE_PATH");
		String saveFilePath = FILE_PATH+fileName;
		
		File savefile = new File(FILE_PATH);
		if(!savefile.isDirectory()){
			savefile.mkdirs();
		}
		
		// opens an output stream to save into file
		FileOutputStream os = new FileOutputStream(saveFilePath);
		in = file.getInputStream();
		int bytesRead = -1;
		byte[] buffer = new byte[4096];
		while ((bytesRead = in.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();in.close();
		
//		vnfmService.setVnfcInfo();
		return ActionResponse.actionSuccess();
	}
	
	/**
	 * 파일의 내용을 읽어온다.
	 * @param filePath
	 * @return
	 */
	public String getContents(String filePath){
		filePath = config.getString("FILE_PATH")+filePath+".xml";
		BufferedReader fin = null; // 파일 스트림 
        String line = null;  // 파일에서 한줄 읽어 들인 문자열
        StringBuilder  sb = new StringBuilder();
        try{
            fin = new BufferedReader(new FileReader(filePath));
            while((line = fin.readLine()) != null) {
            	sb.append(line); // 읽어온 한줄을 추가 한다.
            	sb.append("\r\n");
            } 
            fin.close(); // 스트림을 닫는다..
        } catch(IOException e) {
        	logger.info(e.toString());
//            System.out.println("파일 읽는 중 오류!");
            e.printStackTrace();
        }
        return sb.toString();
	}
	
	/**
	 * Descriptor파일들의 정보를 가져온다.
	 * @return
	 */
	@RequestMapping(value="/descriptors", method = RequestMethod.GET)
	public List<HashMap<String, Serializable>> listDescriptors() {
		
		List<VNFDescriptorVO> listvnfd = vnfmService.getVnfdList(null); 
		
		ArrayList<HashMap<String, Serializable>> list = new ArrayList<HashMap<String, Serializable>>();
		HashMap<String, Serializable> desMap = null;
		
		for (int i = 0; i < listvnfd.size(); i++) {
			VNFDescriptorVO vnfdVO = listvnfd.get(i);
				desMap = new HashMap<String, Serializable>();
				desMap.put("id", vnfdVO.vnfd_id);
				desMap.put("name", vnfdVO.vnfd_id);
				desMap.put("type", "descriptor");
				desMap.put("contents", getContents(vnfdVO.vnfd_id));
				list.add(desMap);
		}
		return list;
	}

	/**
	 * Descriptor파일 삭제
	 * @param fileName
	 */
	public void deleteOnboard(String fileName){
		String vnfdId = fileName;
		VnfcVO vnfcParam = new VnfcVO();
		vnfcParam.vnfd_id = vnfdId;
		List<VnfcVO> listVnfc = vnfmService.getVnfcList(vnfcParam);
		
		VnfcVO desVO = null;
		for (int i = 0; i < listVnfc.size(); i++) {
			desVO = listVnfc.get(i);
			try {
				if(desVO.vnfc_id != null && !desVO.vnfc_id.equals("0")){
					computeService.deleteServer(desVO.vnfc_id);
				}
				if(desVO.snapshot_id != null){
					imageService.deleteImage(desVO.snapshot_id);
				}
				computeService.deleteFlavor(desVO.flavor_id);
				vnfmService.deletePortSplit(desVO.port_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		VNFDescriptorVO vnfdVO = new VNFDescriptorVO();
		vnfdVO.vnfd_id = vnfdId;
		VNFDescriptorVO desVnfdVO = vnfmService.getVnfdList(vnfdVO).get(0);
		
		if(desVnfdVO.image_id != null)
		imageService.deleteImage(desVnfdVO.image_id);
		
		VnfcVO vnfcVO = new VnfcVO();
		vnfcVO.vnfd_id = vnfdId;
		vnfmService.deleteVnfc(vnfcVO);
		
		VNFDescriptorVO vnfdInfoVO = new VNFDescriptorVO();
		vnfdInfoVO.vnfd_id = vnfdId;
		vnfmService.deleteVnfd(vnfdVO);
		
		vnfmService.deleteFolder(config.getString("FILE_PATH")+vnfdId);
		vnfmService.deleteFile(config.getString("FILE_PATH")+fileName+".xml");
		
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value="/descriptors/{id:.+}", method = RequestMethod.DELETE)
	public String deleteDescriptor(@PathVariable(value = "id") String fileName) {
		deleteOnboard(fileName);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", fileName);
		jsonObject.addProperty("success", true);
		return jsonObject.toString();
	}
	
	
	public List<HashMap<String, String>> getFiles(String path){
		String FILE_PATH = config.getString("FILE_PATH")+path;
		File file = new File(FILE_PATH);
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		if(!file.exists()){
			return list;
		}
		HashMap<String, String> map = null;
		File[] listFiles = file.listFiles();
		if(listFiles.length == 0){
			return list;
		}
		for (int i = 0; i < listFiles.length; i++) {
			if(listFiles[i].isFile()){
				map = new HashMap<String, String>();
				map.put("name",listFiles[i].getName());
				map.put("pathName",listFiles[i].getParentFile().getPath());
				map.put("path",listFiles[i].getPath());
				list.add(map);
		    }
		}
		return list;
	}
	
	/**
	 * repository 정보를 얻어온다.
	 * @return
	 */
	@RequestMapping(value="/repositorys", method = RequestMethod.GET)
	public List<HashMap<String, Serializable>> listRepositorys() {
		
		List<VNFDescriptorVO> listVnfd = vnfmService.getVnfdList(null);
		
		ArrayList<HashMap<String, Serializable>> list = new ArrayList<HashMap<String, Serializable>>();
		HashMap<String, Serializable> desMap = null;

		ArrayList<HashMap<String, Object>> child = null;
		HashMap<String, ArrayList<HashMap<String, Object>>> childrenMap = null;
		HashMap<String, Object> childMap = null;
		
		for (int i = 0; i < listVnfd.size(); i++) {
			VNFDescriptorVO vnfdVO = listVnfd.get(i);

			desMap = new HashMap<String, Serializable>();
			desMap.put("title", vnfdVO.vnfd_id);
			desMap.put("name", vnfdVO.vnfd_id);
			desMap.put("type", "repositorys");
			
			childrenMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			child = new ArrayList<HashMap<String, Object>>();
			
			desMap.put("children",childrenMap);
			childrenMap.put("child", child);
			list.add(desMap);
			
			VnfcVO vnfParam = new VnfcVO();
			vnfParam.vnfd_id = vnfdVO.vnfd_id;
			List<VnfcVO> listVnfc = vnfmService.getVnfdRepoList(vnfParam);
			for (int j = 0; j < listVnfc.size(); j++) {
				VnfcVO vnfcVO = listVnfc.get(j);
				childMap = new HashMap<String, Object>();
				childMap.put("title", vnfcVO.vnfc_real_name);
				childMap.put("id", vnfcVO.flavor_id);
				childMap.put("type", "repository");
				childMap.put("parent", vnfdVO.vnfd_id);
				childMap.put("info", getFiles(vnfcVO.vnfd_id+"/"+vnfcVO.vnfc_real_name));
				child.add(childMap);
			}
		}
		return list;
	}
	
	/**
	 * Repository에 파일 업로드 
	 * @param file
	 * @param data
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/repository/upload", method = RequestMethod.POST)
	public List<HashMap<String, String>> saveRepository(@RequestPart(value = "file") MultipartFile file ,
			@RequestPart(required = false, value = "data") RepositoryVO data) throws IOException {
		
		String repoPath = data.parent+"/"+data.title+"/"; 
		String fileName = file.getOriginalFilename();
		
		InputStream in = file.getInputStream();
		
		String FILE_PATH = config.getString("FILE_PATH")+repoPath;
		String saveFilePath = FILE_PATH+fileName;
		
		File savefile = new File(FILE_PATH);
		if(!savefile.isDirectory()){
			savefile.mkdirs();
		}
		
		FileOutputStream os = new FileOutputStream(saveFilePath);
		in = file.getInputStream();
		int bytesRead = -1;
		byte[] buffer = new byte[4096];
		while ((bytesRead = in.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();in.close();

		List<HashMap<String, String>> list = getFiles(repoPath); 
		return list;
	}
	
	/**
	 * repository 삭제
	 * @return
	 */
	@RequestMapping(value="/repositorys/{name:.+}", method = RequestMethod.DELETE)
	public String deleteRepository(HttpServletRequest request,@PathVariable(value = "name") String fileName) {
		String path = request.getParameter("path");
		JsonObject jsonObject = new JsonObject();
		File file = new File(path);
		jsonObject.addProperty("name", fileName);
		boolean flag = false;
		if(file.delete()){
			flag = true;
		}
		jsonObject.addProperty("name", fileName);
		jsonObject.addProperty("success", flag);
		return jsonObject.toString();
	}

	/**
	 * Console url 정보를 가져온다.
	 * @param serverId
	 * @return
	 */
	@RequestMapping(value = "/console/{serverId}", method = RequestMethod.GET)
	public VNCConsole getConsoleUrl(@PathVariable("serverId") String serverId) {
		VNCConsole console = computeService.getConsole(serverId, VNCConsole.Type.NOVNC);
		logger.debug("console data ==== "+ console.toString());
		return console;
	}
	
	
	/**
	 * 하단의 history 정보를 가져온다.
	 * @return
	 */
	@RequestMapping(value="/historys", method = RequestMethod.GET)
	public List<EventLogVO> listHistorys() {
		return vnfmService.getEvnetLogList(null);
	}
	
}

