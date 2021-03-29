package com.eluon.vepc.mano.rest.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.api.Builders;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.compute.HostAggregate;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.compute.Keypair;
import org.openstack4j.model.compute.Limits;
import org.openstack4j.model.compute.RebootType;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.compute.VolumeAttachment;
import org.openstack4j.model.compute.ext.AvailabilityZone;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.model.compute.ext.HypervisorStatistics;
import org.openstack4j.model.identity.v2.Endpoint;
import org.openstack4j.openstack.OSFactory;
import org.openstack4j.openstack.compute.domain.NovaFlavor;
import org.openstack4j.openstack.compute.domain.NovaVolumeAttachment;
import org.openstack4j.openstack.compute.domain.ext.ExtHypervisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.common.MessageSender;
import com.eluon.vepc.mano.rest.validator.ComputeValidator;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ComputeService;
import com.eluon.vepc.mano.service.IdentityService;
import com.eluon.vepc.mano.service.ImageService;
import com.eluon.vepc.mano.service.VNFMService;
import com.eluon.vepc.mano.vo.AlarmNeutronAgentVO;
import com.eluon.vepc.mano.vo.AlarmNovaServicesVO;
import com.eluon.vepc.mano.vo.CommonRespVO;
import com.eluon.vepc.mano.vo.EndpointVO;
import com.eluon.vepc.mano.vo.EventLogVO;
import com.eluon.vepc.mano.vo.HypervisorManageVO;
import com.eluon.vepc.mano.vo.VNFDescriptorVO;
import com.eluon.vepc.mano.vo.VnfcVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Compute Controller (ComputeController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ComputeController.java,v 1.0 2015/01/13 00:00:00 SimByungChul
 *          Express $
 */
@RestController
@RequestMapping("/compute")
public class ComputeController {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	protected final static Logger loggerSin = LoggerFactory.getLogger("MANO_PROCESS_SIN");

	@Resource
	private MessageSender messageSender;
	
	@Autowired
	private ComputeService computeService;

	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private VNFMService vnfmService;

	@Autowired
	private CompositeConfiguration config;

	ObjectMapper mapper = new ObjectMapper();

	
	/**
	 * 로그인 관련
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ActionResponse login(@RequestBody Map<String, String> user){
		logger.debug("userInfo"+user);
		try {
			OSFactory.builderV2()
			.endpoint(config.getString("AUTH_ENDPOINT"))
			.credentials(user.get("userid"), user.get("pass"))
			.authenticate();
			return ActionResponse.actionSuccess();
		} catch (Exception e) {
			return ActionResponse.actionFailed(e.toString(), 400);
		}
	}
	
	/**
	 * Dashboard 메뉴의 서비스 정보
	 * @return
	 */
	@RequestMapping(value = "/serviceinfo", method = RequestMethod.GET)
	public List<AlarmNovaServicesVO> getComputeServiceInfo(){
		List<AlarmNovaServicesVO> listService = vnfmService.getAlarmNovaServicesList(null);
		logger.debug("serviceinfo =" + listService);
		return listService;
	}
	
	/**
	 * Dashboard메뉴의 네트워크 정보
	 * @return
	 */
	@RequestMapping(value = "/networkagentinfo", method = RequestMethod.GET)
	public List<AlarmNeutronAgentVO> getAlarmNertronInfo(){
		List<AlarmNeutronAgentVO> listService = vnfmService.getAlarmNertronList(null);
		logger.debug("networkagentinfo =" + listService);
		return listService;
	}
	
	/**
	 * Dashboard메뉴의 endpoint 정보
	 * @return
	 */
	@RequestMapping(value = "/endpointinfo", method = RequestMethod.GET)
	public List<EndpointVO> getEndpointList(){
		List<EndpointVO> listService = vnfmService.getEndpointList(null);
		List<? extends Endpoint> listEnd = identityService.listTokenEndpoints();
		for (int i = 0; i < listService.size(); i++) {
			for (int j = 0; j < listEnd.size(); j++) {
				String endPointUrl = null;
				if(listService.get(i).Interface.equals("admin")){
					endPointUrl = listEnd.get(j).getAdminURL().toString();
				}else if(listService.get(i).Interface.equals("public")){
					endPointUrl = listEnd.get(j).getPublicURL().toString();
				}else if(listService.get(i).Interface.equals("internal")){
					endPointUrl = listEnd.get(j).getInternalURL().toString();
				}
				if(listService.get(i).id.equals(listEnd.get(j).getId())
					||listService.get(i).url.split("/")[2].equals(endPointUrl.split("/")[2])){
					listService.get(i).service_name = listEnd.get(j).getName();
					listService.get(i).service_type = listEnd.get(j).getType();
					listService.get(i).url = endPointUrl;
					continue;
				}
			}
		}
		logger.debug("endpointinfo =" + listService);
		return listService;
	}
	
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/flavors", method = RequestMethod.GET)
	public List listFlavors() {

		List<VNFDescriptorVO> listvnfd = vnfmService.getVnfdList(null);

		ArrayList<HashMap<String, Serializable>> list = new ArrayList<HashMap<String, Serializable>>();
		HashMap<String, Serializable> desMap = null;

		ArrayList<HashMap<String, Serializable>> child = null;
		HashMap<String, ArrayList<HashMap<String, Serializable>>> childrenMap = null;
		HashMap<String, Serializable> childMap = null;

		for (int i = 0; i < listvnfd.size(); i++) {
			VNFDescriptorVO vnfdVO = listvnfd.get(i);
			desMap = new HashMap<String, Serializable>();
			desMap.put("title", vnfdVO.vnfd_id);
			desMap.put("name", vnfdVO.vnfd_id);
			desMap.put("type", "flavors");

			childrenMap = new HashMap<String, ArrayList<HashMap<String, Serializable>>>();
			child = new ArrayList<HashMap<String, Serializable>>();

			desMap.put("children", childrenMap);
			childrenMap.put("child", child);
			list.add(desMap);

			VnfcVO vnfParam = new VnfcVO();
			vnfParam.vnfd_id = vnfdVO.vnfd_id;
			List<VnfcVO> listVnfc = vnfmService.getVnfcList(vnfParam);
			for (int j = 0; j < listVnfc.size(); j++) {
				VnfcVO vnfcVO = listVnfc.get(j);
				if(vnfcVO.vnfc_name.lastIndexOf("_1") > -1){
					childMap = new HashMap<String, Serializable>();
					childMap.put("title", vnfcVO.flavor_name);
					childMap.put("id", vnfcVO.flavor_id);
					childMap.put("type", "flavor");
					childMap.put("info", computeService.getFlavor(vnfcVO.flavor_id));
					try {
						Map map = computeService.getListProperty(vnfcVO.flavor_id);
						if(map != null)
						childMap.put("spec", map.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
					child.add(childMap);
				}
			}

		}
		logger.debug("listFlavors="+ list);
		return list;

	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/flavors/detail", method = RequestMethod.GET)
	public ResponseEntity<String> listFlavorsDetail(
			@RequestParam Map<String, String> allRequestParams) {
		ResponseEntity<String> listFlavorsDetail = computeService
				.listFlavorsDetail(allRequestParams);
		logger.debug("listFlavorsDetail=" + listFlavorsDetail);
		return listFlavorsDetail;
	}

	/**
	 * @param flavorId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/flavors/{flavorId}", method = RequestMethod.GET)
	public Flavor getFlavor(@PathVariable("flavorId") String flavorId) {
		Flavor flavor = computeService.getFlavor(flavorId);
		logger.debug("flavor=" + flavor);
		return flavor;
	}

	/**
	 * @param novaFlavor
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@RequestMapping(value = "/{tenantId}/flavors", method = RequestMethod.POST)
	public Flavor createFlavor(@PathVariable("tenantId") String tenantId,
			@RequestBody String param) throws JsonParseException,JsonMappingException, IOException {

		HashMap<String, ?> paramMap = mapper.readValue(param,
				new TypeReference<HashMap<String, ?>>() {
				});
		NovaFlavor novaFlavor = mapper.readValue(param,
				new TypeReference<NovaFlavor>() {
				});
		List<Map> projectList = (List<Map>) paramMap.get("projects");
		boolean isPublic = true;
		if (projectList != null && projectList.size() > 0) {
			isPublic = false;
		}

		Flavor flavor = NovaFlavor.builder().from(novaFlavor)
				.isPublic(isPublic).build();

		Flavor newFlavor = computeService.createFlavor(flavor);

		if (projectList != null && projectList.size() > 0) {
			for (Map<String, ?> project : projectList) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("tenant", (String) project.get("id"));
				computeService.createFlavorAccess(tenantId, newFlavor.getId(), jsonObject.toString());
			}
		}

		logger.debug("newFlavor=" + newFlavor);
		return newFlavor;

	}

	/**
	 * @param novaFlavor
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@RequestMapping(value = "/{tenantId}/flavors/{flavorId}", method = RequestMethod.PUT)
	public Flavor updateFlavor(@PathVariable("tenantId") String tenantId,
			@PathVariable("flavorId") String flavorId, @RequestBody String param)
			throws JsonParseException, JsonMappingException, IOException {

		HashMap<String, ?> paramMap = mapper.readValue(param,new TypeReference<HashMap<String, ?>>() {});
		NovaFlavor novaFlavor = mapper.readValue(param,	new TypeReference<NovaFlavor>() {});
		List<Map<String, ?>> projectList = (List<Map<String, ?>>) paramMap.get("projects");
		boolean isPublic = true;
		if (projectList != null && projectList.size() > 0) {
			isPublic = false;
		}

		Flavor flavor = NovaFlavor.builder().from(novaFlavor).isPublic(isPublic).build();
		Flavor updatedFlavor = computeService.updateFlavor(flavor);

		// -- 리스트 초기화
		if (projectList != null && projectList.size() > 0) {

			List<Map> flavorAccessList = null;
			// 접근 권한 처리
			ResponseEntity<String> flavorAccessResponseEntity = computeService
					.listFlavorAccess(tenantId, flavorId);
			if (flavorAccessResponseEntity != null
					&& flavorAccessResponseEntity.getBody() != null
					&& "".equals(flavorAccessResponseEntity.getBody().trim()) == false) {
				Map flavorAccessObj = mapper.readValue(flavorAccessResponseEntity.getBody(),new TypeReference<Map>() {});
				flavorAccessList = (List<Map>) flavorAccessObj.get("flavor_access");
			}
			// -- 리스트 초기화
			if (flavorAccessList == null) {
				flavorAccessList = new ArrayList();
			}
			// --추가된 접근 권한 프로젝트는 추가한다.
			for (Map<String, ?> project : projectList) {
				boolean isExist = false;
				for (Map flavorAccess : flavorAccessList) {
					if (project.get("id").equals(flavorAccess.get("tenant_id"))) {
						isExist = true;
					}
				}

				if (!isExist) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("tenant", (String) project.get("id"));
					computeService.createFlavorAccess(tenantId,	updatedFlavor.getId(), jsonObject.toString());
				}
			}

			// --제거된 접근 권한 프로젝트는 삭제한다.
			for (Map flavorAccess : flavorAccessList) {
				boolean isExist = false;
				for (Map<String, ?> project : projectList) {
					if (project.get("id").equals(flavorAccess.get("tenant_id"))) {
						isExist = true;
					}
				}

				if (!isExist) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("tenant",
							(String) flavorAccess.get("tenant_id"));
					computeService.deleteFlavorAccess(tenantId,updatedFlavor.getId(), jsonObject.toString());
				}
			}
		}
		return updatedFlavor;
	}

	/**
	 * @param flavorId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/flavors/{flavorId}", method = RequestMethod.DELETE)
	public ActionResponse deleteFlavor(@PathVariable("flavorId") String flavorId) {
		logger.debug("delete!!!! flavorId: {}" + flavorId);
		ActionResponse actionResponse = computeService.deleteFlavor(flavorId);
		logger.debug("actionResponse=" + actionResponse);
		return actionResponse;
	}

	/**
	 * @param flavorId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/flavors/{flavorId}/extras", method = RequestMethod.GET)
	public Map<String, String> listFlavorExtraSpecs(
			@PathVariable("flavorId") String flavorId) {
		logger.debug("listFlavorsExtras !!!! flavorId: {}" + flavorId);
		Map<String, String> listFlavorExtras = computeService.listFlavorExtraSpecs(flavorId);
		logger.debug("listFlavorExtras=" + listFlavorExtras);
		return listFlavorExtras;
	}

	/**
	 * @param flavorId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/flavors/{flavorId}/extras", method = RequestMethod.POST)
	public Map<String, String> createAndUpdateFlavorExtraSpecs(
			@PathVariable("flavorId") String flavorId,
			@RequestBody Map<String, String> param) {
		logger.debug("createAndUpdateFlavorsExtras !!!! flavorId: {}"+ flavorId);
		Map<String, String> spec = new HashMap<String, String>();
		if ("custom".equals(param.get("keys"))) {
			spec.put(param.get("key"), param.get("value"));
		} else {
			spec.put(param.get("keys"), param.get("value"));
		}
		Map<String, String> listFlavorExtras = computeService.createAndUpdateFlavorExtraSpecs(flavorId, spec);
		logger.debug("createAndUpdateFlavorsExtras=" + listFlavorExtras);
		return listFlavorExtras;
	}

	/**
	 * @param flavorId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/flavors/{flavorId}/extras/{key}", method = RequestMethod.DELETE)
	public Map<String, String> deleteFlavorExtraSpecs(
			@PathVariable("flavorId") String flavorId,
			@PathVariable("key") String key) {
		logger.debug("deleteFlavorsExtras !!!! flavorId: {}" + flavorId);
		computeService.deleteFlavorExtraSpecs(flavorId, key);
		return new HashMap<String, String>();
	}

	/**
	 * List of Flavor Access
	 * 
	 * @method listFlavorAccess
	 * @param tenantId
	 * @param flavorId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/flavors/{flavorId}/os-flavor-access", method = RequestMethod.GET)
	public ResponseEntity<String> listFlavorAccess(
			@PathVariable("tenantId") String tenantId,
			@PathVariable("flavorId") String flavorId) {
		try {
			return computeService.listFlavorAccess(tenantId, flavorId);
		} catch (Exception e) {
			loggerTra.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Create Flavor Access
	 * 
	 * @method createFlavorAccess
	 * @param tenantName
	 * @param flavorId
	 * @return
	 */
	@RequestMapping(value = "/{tenantName}/flavors/{flavorId}/action", method = RequestMethod.POST)
	public ResponseEntity<String> createFlavorAccess(
			@PathVariable("tenantName") String tenantName,
			@PathVariable("flavorId") String flavorId,
			@RequestBody String reqBody) {
		return computeService.createFlavorAccess(tenantName, flavorId, reqBody);
	}

	/**
	 * Delete Flavor Access
	 * 
	 * @method deleteFlavorAccess
	 * @param tenantName
	 * @param flavorId
	 * @return
	 */
	@RequestMapping(value = "/{tenantName}/flavors/{flavorId}/action", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteFlavorAccess(
			@PathVariable("tenantName") String tenantName,
			@PathVariable("flavorId") String flavorId,
			@RequestBody String reqBody) {
		return computeService.deleteFlavorAccess(tenantName, flavorId, reqBody);
	}

	/**
	 *
	 * @method listImages
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/images", method = RequestMethod.GET)
	public List<? extends Image> listImages() {
		return computeService.listImages();
	}

	/**
	 *
	 * @method getImage
	 * @param imageId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/images/{imageId}", method = RequestMethod.GET)
	public Image getImage(@PathVariable(value = "imageId") String imageId) {
		return computeService.getImage(imageId);
	}

	/**
	 * VM 목록
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/servers", method = RequestMethod.GET)
	public List<HashMap<String, Serializable>> listServers() {
		List<VNFDescriptorVO> listVnfd = vnfmService.getVnfdList(null);

		ArrayList<HashMap<String, Serializable>> list = new ArrayList<HashMap<String, Serializable>>();
		HashMap<String, Serializable> desMap = null;

		ArrayList<HashMap<String, Object>> child = null;
		HashMap<String, ArrayList<HashMap<String, Object>>> childrenMap = null;
		HashMap<String, Object> childMap = null;

		VnfcVO vnfcParam = new VnfcVO();
		VnfcVO vnfcVO = null;
		List<? extends Server> listServers = computeService.listServers();
		for (int i = 0; i < listVnfd.size(); i++) {
			vnfcParam.vnfd_id = listVnfd.get(i).vnfd_id;
			List<VnfcVO> listVnfc = vnfmService.getVnfcList(vnfcParam);
			boolean check = false;
			for (int j = 0; j < listVnfc.size(); j++) {
				vnfcVO = listVnfc.get(j);
				if (vnfcVO.vnfc_id != null && !vnfcVO.vnfc_id.equals("0")) {
					check = true;
				}
			}
			if(!check)continue;
			
			VNFDescriptorVO vnfdVO = listVnfd.get(i);
			desMap = new HashMap<String, Serializable>();
			desMap.put("title", vnfdVO.vnfd_name);
			desMap.put("name", vnfdVO.vnfd_name);
			desMap.put("id", vnfdVO.vnfd_id);
			desMap.put("type", "vnf");

			childrenMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			child = new ArrayList<HashMap<String, Object>>();

			desMap.put("children", childrenMap);
			childrenMap.put("child", child);
			list.add(desMap);
			
			String status = "active";
			
			Date created = null;
			Date launched = null;
			String zone = null;
			HashSet<String> hsCompute = new HashSet<String>();
			
			for (int j = 0; j < listVnfc.size(); j++) {
				vnfcVO = listVnfc.get(j);
				if (vnfcVO.vnfc_id != null && !vnfcVO.vnfc_id.equals("0")) {
					childMap = new HashMap<String, Object>();
					childMap.put("title", vnfcVO.vnfc_name);
					childMap.put("name", vnfcVO.vnfc_name);
					childMap.put("id", vnfcVO.vnfc_id);
					
					for (int k = 0; k < listServers.size(); k++) {
						Server server = listServers.get(k);
						if(vnfcVO.vnfc_id.equals(server.getId())){
							if(zone == null){
								zone = server.getAvailabilityZone();
							}
							if (server.getCreated() != null ) {
								if(created == null){
									created = server.getCreated();
								}else if(created.getTime() > server.getCreated().getTime()){
									created = server.getCreated();
								}
							}
							if (server.getLaunchedAt() != null ) {
								if(launched == null){
									launched = server.getLaunchedAt();
								}else if(launched.getTime() > server.getLaunchedAt().getTime()){
									launched = server.getLaunchedAt();
								}
							}
							if(!hsCompute.contains(server.getHypervisorHostname())){
								hsCompute.add(server.getHypervisorHostname());
							}
							if(!server.getStatus().equals(Status.ACTIVE)){
								status = "Inactive";
							}
							childMap.put("info", server);
							childMap.put("actionLog", vnfmService.getInstanceActionLog(server.getId()));
						}
					}
					childMap.put("type", "vnfc");
					child.add(childMap);
				}
			}
			
			if (hsCompute.size() != 0) {
				List<String> hsCompute1 = new ArrayList<String>(hsCompute);
				Collections.sort(hsCompute1);
				desMap.put("OS-EXT-SRV-ATTR:hypervisor_hostname", hsCompute1.toString().replace("[", "").replace("]", ""));
			}
			desMap.put("OS-EXT-AZ:availability_zone", zone);
			desMap.put("created", created);
			desMap.put("OS-SRV-USG:launched_at", launched);
			desMap.put("status", status);
		}
		logger.debug("servers = " + list);
		return list;
	}

	/**
	 * VNF 정보
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/servergroup/{serverId}", method = RequestMethod.GET)
	public HashMap<String, Serializable> getServerGroup(@PathVariable("serverId") String serverId) {
		
		VNFDescriptorVO param = new VNFDescriptorVO();
		param.vnfd_id = serverId;
		VNFDescriptorVO vnfdVO = null;
		try {
			vnfdVO = vnfmService.getVnfdList(param).get(0);
			
		} catch (Exception e) {
			return null;
		}

		HashMap<String, Serializable> desMap = null;

		ArrayList<HashMap<String, Object>> child = null;
		HashMap<String, ArrayList<HashMap<String, Object>>> childrenMap = null;
		HashMap<String, Object> childMap = null;

		VnfcVO vnfcParam = new VnfcVO();
		VnfcVO vnfcVO = null;
		List<? extends Server> listServers = computeService.listServers();
		vnfcParam.vnfd_id = vnfdVO.vnfd_id;
		List<VnfcVO> listVnfc = vnfmService.getVnfcList(vnfcParam);
		boolean check = false;
		for (int j = 0; j < listVnfc.size(); j++) {
			vnfcVO = listVnfc.get(j);
			if (vnfcVO.vnfc_id != null && !vnfcVO.vnfc_id.equals("0")) {
				check = true;
			}
		}
		if(!check) return null;
		
		desMap = new HashMap<String, Serializable>();
		desMap.put("title", vnfdVO.vnfd_name);
		desMap.put("name", vnfdVO.vnfd_name);
		desMap.put("id", vnfdVO.vnfd_id);
		desMap.put("type", "vnf");

		childrenMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
		child = new ArrayList<HashMap<String, Object>>();

		desMap.put("children", childrenMap);
		childrenMap.put("child", child);
		
		String status = "active";
		
		Date created = null;
		Date launched = null;
		String zone = null;
		HashSet<String> hsCompute = new HashSet<String>();
		for (int j = 0; j < listVnfc.size(); j++) {
			vnfcVO = listVnfc.get(j);
			if (vnfcVO.vnfc_id != null && !vnfcVO.vnfc_id.equals("0")) {
				childMap = new HashMap<String, Object>();
				childMap.put("title", vnfcVO.vnfc_name);
				childMap.put("name", vnfcVO.vnfc_name);
				childMap.put("id", vnfcVO.vnfc_id);
				
				for (int k = 0; k < listServers.size(); k++) {
					Server server = listServers.get(k);
					if(vnfcVO.vnfc_id.equals(server.getId())){
						if(zone == null){
							zone = server.getAvailabilityZone();
						}
						if (server.getCreated() != null ) {
							if(created == null){
								created = server.getCreated();
							}else if(created.getTime() > server.getCreated().getTime()){
								created = server.getCreated();
							}
						}
						if (server.getLaunchedAt() != null ) {
							if(launched == null){
								launched = server.getLaunchedAt();
							}else if(launched.getTime() > server.getLaunchedAt().getTime()){
								launched = server.getLaunchedAt();
							}
						}
						hsCompute.add(server.getHypervisorHostname());
						if(!server.getStatus().equals(Status.ACTIVE)){
							status = "Inactive";
						}
						childMap.put("info", server);
						childMap.put("actionLog", vnfmService.getInstanceActionLog(server.getId()));
					}
				}
				childMap.put("type", "vnfc");
				child.add(childMap);
			}
		}
		
		if (hsCompute.size() != 0) {
			List<String> hsCompute1 = new ArrayList<String>(hsCompute);
			Collections.sort(hsCompute1);
			desMap.put("OS-EXT-SRV-ATTR:hypervisor_hostname", hsCompute1.toString().replace("[", "").replace("]", ""));
		}
		desMap.put("OS-EXT-AZ:availability_zone", zone);
		desMap.put("created", created);
		desMap.put("OS-SRV-USG:launched_at", launched);
		desMap.put("status", status);
		logger.debug("servergroup = " + desMap);
		return desMap;
	}
	
	/**
	 * @param serverId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/servers/{serverId}", method = RequestMethod.GET)
	public Server getServer(@PathVariable("serverId") String serverId) {
		Server server = computeService.getServer(serverId);
		logger.debug("server=" + server);
		return server;
	}

	/**
	 * @param serverId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/servers/{serverId}/os-volume_attachments", method = RequestMethod.POST)
	public VolumeAttachment attachVolume(
			@PathVariable("serverId") String serverId,
			@RequestBody NovaVolumeAttachment volumeAttachment) {
		VolumeAttachment createVolumeAttachment = computeService.attachVolume(
				serverId, volumeAttachment.getVolumeId(),
				volumeAttachment.getDevice());
		logger.debug("createVolumeAttachment=" + createVolumeAttachment);
		return createVolumeAttachment;
	}

	/**
	 * @param serverId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/servers/{serverId}/os-volume_attachments", method = RequestMethod.GET)
	public List listAttachVolume(@PathVariable("serverId") String serverId) {
		return new ArrayList();
	}

	/**
	 * VNF시작.
	 * @param novaServer
	 * @return
	 * @throws InterruptedException 
	 */
	@RequestMapping(value = "/{tenantId}/servers", method = RequestMethod.POST)
	public ActionResponse createServer(@RequestBody String param)
			throws JsonParseException, JsonMappingException, IOException, InterruptedException {

		HashMap<String, HashMap<String, String>> paramMap = mapper.readValue(param, new TypeReference<HashMap<String, ?>>() {});

		HashMap<String, String> paramMap2 = paramMap.get("availability_zone");
		HashMap<String, String> paramMap3 = paramMap.get("flavor");

		HashMap<String, ?> paramMap4 = paramMap.get("usebackup");
		
		String zoneName = paramMap2.get("zoneName");
		String descriptorId = paramMap3.get("name");
		
		EventLogVO event = new EventLogVO();
		event.descriptor = "Descriptor id :" +descriptorId+", Zone :  "+zoneName;
		event.name = "VNF 시작";
		event.log_id = descriptorId; 
		vnfmService.addEvnetLogInfo(event);
		
		boolean backUse = (boolean)paramMap4.get("use");
		
		VNFDescriptorVO vnfdParam = new VNFDescriptorVO();
		vnfdParam.vnfd_id = descriptorId;

		List<VNFDescriptorVO> listVnfd = vnfmService.getVnfdList(vnfdParam);
		String vnfdName = vnfmService.getVnfdName(descriptorId);
		
		if(vnfdName.equals("error")){
			return ActionResponse.actionFailed("vnfd 갯수 초과", 503);
		}
		
		List<? extends HostAggregate> listHost = computeService.listHostAggregate();
		HostAggregate selHostagg = null;
		for ( HostAggregate host : listHost ) {
			if(host.getName().equals(zoneName)){
				selHostagg = host;
				break;
			}
		}
		
		List<Hypervisor> selListHyper = new ArrayList<Hypervisor>();
		List<? extends Hypervisor> listHyper = computeService.getHypervisors();
		for ( Hypervisor hyper : listHyper ) {
			if(selHostagg.getHosts().contains(hyper.getHypervisorHostname())){
				selListHyper.add(hyper);
			}
		}
		
		int freeDisk = 0;
		int freeMem = 0;
		int freeCpu = 0;
		
		for ( Hypervisor hyper : selListHyper ) {
			freeDisk = freeDisk + hyper.getFreeDisk();
			freeMem = freeMem + hyper.getFreeRam();
			freeCpu = freeCpu +(hyper.getVirtualCPU() - hyper.getVirtualUsedCPU());
		}
		
		VnfcVO checkParam = new VnfcVO();
		checkParam.vnfd_id = descriptorId;
		List<VnfcVO> listCheckVnfc = vnfmService.getVnfcList(checkParam);
		
		for (VnfcVO vnfc: listCheckVnfc) {
			if(vnfc.vnfc_id == null || vnfc.vnfc_id.equals("0")){
				Flavor flavor = computeService.getFlavor(vnfc.flavor_id);
				freeDisk = freeDisk - flavor.getDisk();
				freeMem = freeMem - flavor.getRam();
				freeCpu = freeCpu - flavor.getVcpus();
			}
		}
		
		if(freeDisk < 0){
			logger.debug("Disk"+ (freeDisk*-1)+"G 부족");
			return ActionResponse.actionFailed("Disk"+ (freeDisk*-1)+"G 부족", 503);
		}else if(freeMem < 0){
			logger.debug("Mem "+ (freeMem/1000*-1)+"G 부족");
			return ActionResponse.actionFailed("Mem "+ (freeMem/1000*-1)+"G 부족", 503);
		}else if(freeCpu < 0){
			logger.debug("cpu "+ (freeCpu*-1)+"core 부족");
			return ActionResponse.actionFailed("cpu "+ (freeCpu*-1)+"core 부족", 503);
		}
		
		ArrayList<String> listVnfcId = new ArrayList<String>();
		VnfcVO vnfcParam = new VnfcVO();
		boolean isMaked = false;
		List<String> listZoneName = null;
		for (int i = 0; i < listVnfd.size(); i++) {
			VNFDescriptorVO vnfd = listVnfd.get(i);
			vnfcParam.vnfd_id = vnfd.vnfd_id;
			List<VnfcVO> listVnfc = vnfmService.getVnfcList(vnfcParam);
			
			for (int j = 0; j < listVnfc.size(); j++) {
				VnfcVO vnfcVO = listVnfc.get(j);
				if(!vnfcVO.vnfc_name.startsWith(vnfdName))
				vnfcVO.vnfc_name = vnfdName+"_"+ vnfcVO.vnfc_name;
				
				if (vnfcVO.vnfc_name != null && vnfd.image_id != null
						&& vnfcVO.flavor_id != null && vnfcVO.port_id != null
						&& (vnfcVO.vnfc_id == null || vnfcVO.vnfc_id.equals("0"))) {
					
					isMaked = true;
					String imageId = null;
					if(backUse && vnfcVO.snapshot_id != null){
						imageId = vnfcVO.snapshot_id;
					}else {
						imageId = vnfd.image_id;
					}
					if(j % 2 == 0 ){
						listZoneName = new ArrayList<String>();
						if(vnfcVO.affinity.equals("0")){
							listZoneName.add(zoneName);
							listZoneName.add(zoneName);
						}else {
							listHyper = computeService.getHypervisors();
							selListHyper.clear();
							for ( Hypervisor hyper : listHyper ) {
								if(selHostagg.getHosts().contains(hyper.getHypervisorHostname())){
									selListHyper.add(hyper);
								}
							}
							List<Object> ListHost = vnfmService.getHosts(vnfcVO.affinity, selListHyper, vnfcVO);
							if(vnfcVO.affinity.equals("1")){
								if(ListHost.get(0).getClass() == ExtHypervisor.class){
									listZoneName.add(zoneName+":"+((Hypervisor)ListHost.get(0)).getHypervisorHostname());
									listZoneName.add(zoneName+":"+((Hypervisor)ListHost.get(0)).getHypervisorHostname());
								}else {
									return ActionResponse.actionFailed((String)ListHost.get(0), 503);
								}
							}else if(vnfcVO.affinity.equals("2")){
								if(ListHost.get(0).getClass() == ExtHypervisor.class){
									listZoneName.add(0,zoneName+":"+((Hypervisor)ListHost.get(0)).getHypervisorHostname());
									listZoneName.add(1,zoneName+":"+((Hypervisor)ListHost.get(1)).getHypervisorHostname());
								}else {
									return ActionResponse.actionFailed((String)ListHost.get(0), 503);
								}
							}
						}
					}
					
					try {
						String[] data = vnfcVO.port_id.split(",");
						ServerCreate buildServer = Builders.server()
								.name(vnfcVO.vnfc_name).image(imageId)
								.flavor(vnfcVO.flavor_id)
								.availabilityZone(listZoneName.get(j%2)).build();
						for (int k = 0; k < data.length; k++) {
							buildServer.addNetworkPort(data[k]);
						}

						Server server = computeService.createServer(buildServer);
						
						listVnfcId.add(server.getId());
						VnfcVO vnfdData = new VnfcVO();
						vnfdData.vnfd_id = vnfcVO.vnfd_id;
						vnfdData.status = "CREATED_END";
						vnfdData.port_id = vnfcVO.port_id;
						vnfdData.vnfc_name_ref = vnfcVO.vnfc_name;
						
						vnfdData.vnfc_id = server.getId();
						vnfmService.updateVnfc(vnfdData);
					} catch (Exception e) {
						e.printStackTrace();
						return ActionResponse.actionFailed(e.toString(), 503);
					}
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		VNFDescriptorVO vnfdParam2 = new VNFDescriptorVO();
		vnfdParam2.vnfd_id = descriptorId;
		vnfdParam2.vnfd_name = vnfdName;
		vnfdParam2.zone = zoneName;
		vnfdParam2.created = new Date();
		vnfmService.updateVnfd(vnfdParam2);
		if(!isMaked){
			return ActionResponse.actionFailed("이미 생성된 VNF입니다.  ", 503);
		}
		messageSender.VnfStartListener(listVnfcId, descriptorId, vnfdName);
		return ActionResponse.actionSuccess();
	}

	/**
	 * 아마도 사용 안함
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/{id}/snapshot", method = RequestMethod.POST)
	public ActionResponse createRestore(@PathVariable("id") String id) {
		VnfcVO param = new VnfcVO();
		param.vnfd_id = id;
		List<VnfcVO> listVnfc = vnfmService.getVnfcList(param);
		for (int i = 0; i < listVnfc.size(); i++) {
			VnfcVO vnfc = listVnfc.get(i);
			
			if(vnfc.snapshot_id != null || !vnfc.snapshot_id.equals("0")){
				String arrPortId[] = vnfc.port_id.split(","); 
				Server server = computeService.getServer(vnfc.vnfc_id);
				computeService.deleteServer(vnfc.vnfc_id);
				
				ServerCreate buildServer = Builders.server()
						.name(server.getName()).image(vnfc.snapshot_id)
						.flavor(server.getFlavorId())
						.availabilityZone(server.getAvailabilityZone()).build();
				for (int j = 0; j < arrPortId.length; j++) {
					buildServer.addNetworkPort(arrPortId[i]);
				}
				Server createServer = computeService.createServer(buildServer);
				VnfcVO data = new VnfcVO();
				data.vnfc_id = createServer.getId();
				data.vnfc_id_ref = vnfc.vnfc_id;
				vnfmService.updateVnfc(data);
			}
		}
		
		return ActionResponse.actionSuccess();

	}
	/**
	 * @param novaServer
	 * @return
	 */
	@RequestMapping(value = "/{id}/snapshot/{name}", method = RequestMethod.POST)
	public ActionResponse createSnapShot(@PathVariable("id") String id,
			@PathVariable("name") String name) {
		VnfcVO param = new VnfcVO();
		param.vnfd_id = id;
		List<VnfcVO> listVnfc = vnfmService.getVnfcList(param);
		String snapShotId = null;
		for (int i = 0; i < listVnfc.size(); i++) {
			VnfcVO vnfc = listVnfc.get(i);
			if(vnfc.vnfc_id != null){
				try {
					snapShotId = computeService.createSnapshot(vnfc.vnfc_id, name+"_"+vnfc.vnfc_name);
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(snapShotId != null || snapShotId != ""){
					VnfcVO data = new VnfcVO();
					data.snapshot_id = snapShotId;
					data.vnfc_id_ref = vnfc.vnfc_id;
					vnfmService.updateVnfc(data);
				}
			}
		}
		
		return ActionResponse.actionSuccess();

	}

	/**
	 * VNF삭제.
	 * @param serverId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/servers/{serverId}", method = RequestMethod.DELETE)
	public ActionResponse deleteServer(@PathVariable("serverId") String serverId) {
		logger.debug("delete!!!! serverId: {}" + serverId);

		EventLogVO event = new EventLogVO();
		event.descriptor = "vnfd_id :" +serverId;
		event.name = "VNF 삭제 시작";
		event.log_id = serverId; 
		vnfmService.addEvnetLogInfo(event);		
		
		
		VnfcVO param = new VnfcVO();
		param.vnfd_id = serverId;
		List<VnfcVO> listVnfc = vnfmService.getVnfcList(param);

		/* TODO
		 * agent에 노티를 보내고 노티를 받으면 삭제.
		 */
		if(listVnfc.get(0).snapshot_id != null){
			for (int i = 0; i < listVnfc.size(); i++) {
				VnfcVO vnfcInfo = listVnfc.get(i);
				if (vnfcInfo.vnfc_id != null && !vnfcInfo.vnfc_id.equals("0")) {
					computeService.deleteServer(vnfcInfo.vnfc_id);
					VnfcVO param2 = new VnfcVO();
					param2.vnfc_id = "0";
					param2.vnfc_id_ref = vnfcInfo.vnfc_id;
					vnfmService.updateVnfc(param2);
					
					EventLogVO event2 = new EventLogVO();
					event2.descriptor = "VM만 삭제 완료(snapshot id :"+ vnfcInfo.snapshot_id+")" ;
					event2.name = vnfcInfo.vnfc_name;
					event2.log_id = vnfcInfo.vnfc_id; 
					vnfmService.addEvnetLogInfo(event2);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			EventLogVO event2 = new EventLogVO();
			event2.descriptor = "snapshot정보로 인하여 VM만 삭제";
			event2.name = "VNF 삭제 완료 ";
			event2.log_id = serverId; 
			vnfmService.addEvnetLogInfo(event2);	
			
		}else{
			for (int i = 0; i < listVnfc.size(); i++) {
				VnfcVO vnfcInfo = listVnfc.get(i);
				if (vnfcInfo.vnfc_id != null && !vnfcInfo.vnfc_id.equals("0")) {
					String resStr = vnfmService.deleteServerProcess(vnfcInfo.vnfc_id);
					if(resStr != null){
						EventLogVO event2 = new EventLogVO();
						event2.descriptor = resStr;
						event2.name = "VNF 삭제 실패 ";
						event2.log_id = serverId; 
						vnfmService.addEvnetLogInfo(event2);
						return ActionResponse.actionFailed(resStr, 503);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}else {
				}
			}
		}

		return ActionResponse.actionSuccess();
	}

	/**
	 * @param serverId
	 * @param action
	 * @return
	 * @throws InterruptedException 
	 */
	@RequestMapping(value = "/{tenantId}/servers/{serverId}/action", method = RequestMethod.POST)
	public ActionResponse actionServer(@PathVariable("serverId") String serverId,@RequestParam(required = true) String action) throws InterruptedException {
		
		VnfcVO vnfcParam = new VnfcVO();
		VnfcVO vnfc = null;
		if(!action.equals("ALLSTOP") && !action.equals("ALLSTART")){ 
			vnfcParam = new VnfcVO();
			vnfcParam.vnfc_id = serverId;
			vnfc = vnfmService.getVnfcList(vnfcParam).get(0);
		}
		
		if(!action.equals("ALLSTOP") && !action.equals("ALLSTART") &&
				!action.equals("SCALE_IN") && !action.equals("SCALE_OUT")){
			EventLogVO event = new EventLogVO();
			
			event.descriptor = "VM "+action+" start";
			event.name = vnfc.vnfc_name;
			event.log_id = serverId; 
			vnfmService.addEvnetLogInfo(event);
		}else if(action.equals("SCALE_IN") || action.equals("SCALE_OUT") ){
			long currentTime = System.currentTimeMillis();
			if(vnfc.guard_time != 1 && currentTime - vnfc.guard_time < Integer.parseInt(vnfc.break_policy)*1000){
				EventLogVO event = new EventLogVO();
				event.descriptor = "GuardTime : "+vnfc.break_policy+ "중  :"+ ((currentTime - vnfc.guard_time)/1000)+"초 경과";
				event.name = vnfc.vnfc_name;
				event.log_id = vnfc.vnfc_id; 
				vnfmService.addEvnetLogInfo(event);
				return ActionResponse.actionFailed(event.descriptor , 503);
			}
			if(action.equals("SCALE_IN")){
				String scaleOut[] = vnfc.out_policy.split(",");
				if(currentTime - vnfc.scale_out_time < Long.parseLong(scaleOut[2])*1000){
					EventLogVO event = new EventLogVO();
					event.descriptor = "SCALE_IN Time : "+scaleOut[2]+ "중  :"+ ((currentTime - vnfc.scale_out_time)/1000)+"초 경과";
					event.name = vnfc.vnfc_name;
					event.log_id = vnfc.vnfc_id; 
					vnfmService.addEvnetLogInfo(event);
					return ActionResponse.actionFailed(event.descriptor , 503);
				}
			}else if(action.equals("SCALE_OUT")){
				String scaleIn[] = vnfc.in_policy.split(",");
				if(currentTime - vnfc.scale_in_time < Long.parseLong(scaleIn[2])*1000){
					EventLogVO event = new EventLogVO();
					event.descriptor = "SCALE_OUT Time : "+scaleIn[2]+ "중  :"+ ((currentTime - vnfc.scale_in_time)/1000)+"초 경과";
					event.name = vnfc.vnfc_name;
					event.log_id = vnfc.vnfc_id; 
					vnfmService.addEvnetLogInfo(event);
					return ActionResponse.actionFailed(event.descriptor , 503);
				}
			}
		}

		ActionResponse actionResponse = null;
		if (action.equals("delete")) {
			computeService.deleteServer(serverId);
			VnfcVO param = new VnfcVO();
			vnfc.vnfc_id = "0";
			vnfc.vnfc_id_ref = serverId;
			vnfmService.updateVnfc(param);
			actionResponse =  ActionResponse.actionSuccess();
			
		}else if (action.equals("soft")) {
			actionResponse = computeService.reBootServer(serverId,RebootType.SOFT);
			messageSender.VnfcStatusListener(vnfc, "active", "soft");
		} else if (action.equals("hard")) {
			actionResponse = computeService.reBootServer(serverId,RebootType.HARD);
		} else if (action.equals("SCALE_IN")) {
			String response = null;
			if(vnfc != null){
				//agent에 삭제 노티를 보냄
				response = vnfmService.serviceScaleInProcess(vnfc);
			}
			if(response != null){
				actionResponse = ActionResponse.actionFailed(response, 503);
			}else {
				actionResponse = ActionResponse.actionSuccess();
			}
		} else if (action.equals("SCALE_OUT")) {
			if(vnfc != null){
				try {
					String rtnStr = vnfmService.serviceScaleOutProcess(vnfc);
					if(rtnStr.equals("0")){
						actionResponse = ActionResponse.actionSuccess();
					}else {
						actionResponse = ActionResponse.actionFailed(rtnStr, 503);
					}
				} catch (Exception e) {
					actionResponse = ActionResponse.actionFailed(e.toString(), 503);
				}
			}
			
		} else if (action.equals("ALLSTOP")) {
			VNFDescriptorVO param = new VNFDescriptorVO();
			param.vnfd_id = serverId;
			VNFDescriptorVO vnfd = vnfmService.getVnfdList(param).get(0);
			
			EventLogVO event = new EventLogVO();
			event.descriptor = "VNF Power off start";
			event.name = vnfd.vnfd_name;
			event.log_id = serverId; 
			vnfmService.addEvnetLogInfo(event);
			
			VnfcVO param2 = new VnfcVO();
			param2.vnfd_id = serverId;
			List<VnfcVO> listVnfc = vnfmService.getVnfcList(param2);
			
			ArrayList<String> listVnfcId = new ArrayList<String>();
			String message = ""; VnfcVO vnfcInfo = null;boolean successFlag = true;
			for (int i = 0; i < listVnfc.size(); i++) {
				try {
					vnfcInfo = listVnfc.get(i);
					listVnfcId.add(vnfcInfo.vnfc_id);
					if(computeService.getServer(vnfcInfo.vnfc_id).getStatus().value().equals("active")){
						actionResponse = computeService.actionServer(vnfcInfo.vnfc_id,Action.valueOf("STOP"));
						if(!actionResponse.isSuccess()){
							message = message + " "+ vnfcInfo.vnfc_name;
							successFlag = false;
						}
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
					successFlag = false;
					message = message + " "+ vnfcInfo.vnfc_name;
				}
			}
			if(!successFlag){
				event = new EventLogVO();
				event.descriptor = "VNF Power off 실패 :"+message;
				event.name = vnfd.vnfd_name;
				event.log_id = serverId; 
				vnfmService.addEvnetLogInfo(event);
				return ActionResponse.actionFailed(message+" Power Off에 실패하였습니다.", 503);
			}else {
				// arrVnfcId, vnfd_name,vnfd_id, status를 보내서 모두 shutdown.일 경우 insert
				messageSender.VnfPowerListener(listVnfcId, vnfd.vnfd_id, vnfd.vnfd_name, "shutoff");
			}
			actionResponse = ActionResponse.actionSuccess();
			
		} else if (action.equals("ALLSTART")) {
			VNFDescriptorVO param = new VNFDescriptorVO();
			param.vnfd_id = serverId;
			VNFDescriptorVO vnfd = vnfmService.getVnfdList(param).get(0);
			
			EventLogVO event = new EventLogVO();
			event.descriptor = "VNF Power on start";
			event.name = vnfd.vnfd_name;
			event.log_id = serverId; 
			vnfmService.addEvnetLogInfo(event);
			
			VnfcVO param2 = new VnfcVO();
			param2.vnfd_id = serverId;
			List<VnfcVO> listVnfc = vnfmService.getVnfcList(param2);
			
			ArrayList<String> listVnfcId = new ArrayList<String>();
			String message = ""; VnfcVO vnfcInfo = null;
			boolean successFlag = true;
			for (int i = 0; i < listVnfc.size(); i++) {
				try {
					vnfcInfo = listVnfc.get(i);
					listVnfcId.add(vnfcInfo.vnfc_id);
					if(computeService.getServer(vnfcInfo.vnfc_id).getStatus().value().equals("shutoff")){
						actionResponse = computeService.actionServer(vnfcInfo.vnfc_id,Action.valueOf("START"));
						if(!actionResponse.isSuccess()){
							message = message + " "+ vnfcInfo.vnfc_name;
							successFlag = false;
						}
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
					successFlag = false;
					message = message + " "+ vnfcInfo.vnfc_name;
				}
			}
			if(!successFlag){
				event = new EventLogVO();
				event.descriptor = "VNF Power on 실패 :"+message;
				event.name = vnfd.vnfd_name;
				event.log_id = serverId; 
				vnfmService.addEvnetLogInfo(event);
				
				return ActionResponse.actionFailed(message+" Power On에 실패하였습니다.", 503);
			}else {
				messageSender.VnfPowerListener(listVnfcId, vnfd.vnfd_id, vnfd.vnfd_name, "active");
			}
			actionResponse = ActionResponse.actionSuccess();
			
		} else {
			actionResponse = computeService.actionServer(serverId,Action.valueOf(action));
		}

		logger.debug("actionResponse=" + actionResponse);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!action.equals("ALLSTOP") && !action.equals("ALLSTART")&&
				!action.equals("SCALE_IN") && !action.equals("SCALE_OUT") &&
				!action.equals("soft")
				){
			EventLogVO event = new EventLogVO();
			event.descriptor = "VM "+action+" end";
			event.name = vnfc.vnfc_name;
			event.log_id = serverId; 
			vnfmService.addEvnetLogInfo(event);
		}
		
		return actionResponse;
	}

	/**
	 * Limit
	 * 
	 * @method limit
	 * @return
	 */
	@RequestMapping(value = "/limit", method = RequestMethod.GET)
	public Limits limit() {
		return computeService.limit();
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/zones", method = RequestMethod.GET)
	public List<? extends AvailabilityZone> listZones() {
		List<? extends AvailabilityZone> listZones = computeService.listZones();
		logger.debug("listZones=" + listZones);
		return listZones;
	}

	/**
	 * Zone의 System 정보
	 * @param zoneId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/hypervisors/{id}", method = RequestMethod.GET)
	public HashMap<String, ArrayList<String>> getHypervisors(@PathVariable("id") String zoneId) {
		
		HostAggregate listHostAggregate = computeService.getHostAggregate(zoneId);
		List<String> listHost = listHostAggregate.getHosts();
		
		ArrayList<String> items = new ArrayList<String>();
		ArrayList<String> selectItems = new ArrayList<String>();
		List<? extends Hypervisor> listHypervisors = computeService.getHypervisors();
		HashMap<String, ArrayList<String>> itemMap = new HashMap<String, ArrayList<String>>();

		for (int i = 0; i < listHypervisors.size(); i++) {
			Hypervisor hypervisor = listHypervisors.get(i);
			if (listHost.contains(hypervisor.getHypervisorHostname())) {
				selectItems.add(hypervisor.getHypervisorHostname());
			} else {
				List<? extends HostAggregate> listHosta = computeService.listHostAggregate();
				boolean isInHost = false;
				for (int j = 0; j < listHosta.size(); j++) {
					HostAggregate host = listHosta.get(j);
					if(host.getHosts().contains(hypervisor.getHypervisorHostname())){
						isInHost = true;
						continue;
					}
				}
				if(!isInHost)
				items.add(hypervisor.getHypervisorHostname());
			}
		}
		itemMap.put("items", items);
		itemMap.put("selectItems", selectItems);
		logger.debug("itemMap = " + itemMap);
		return itemMap;
	}

	/**
	 * System List
	 * 
	 * @method list
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/hypervisors/{id}", method = RequestMethod.POST)
	public ActionResponse manageHypervisors(@PathVariable("id") String zoneId,
			@RequestBody String reqBody) {
		
		Gson gson = new Gson();
		HypervisorManageVO data = gson.fromJson(reqBody,HypervisorManageVO.class);

		try {
			OSFactory.builderV2()
			.endpoint(config.getString("AUTH_ENDPOINT"))
			.credentials(config.getString("AUTH_CREDENTIALS_ID"), data.getPass())
			.authenticate();
		} catch (Exception e) {
			return ActionResponse.actionFailed(e.toString(), 503);
		}
		
		
		HostAggregate listHostAggregate = computeService.getHostAggregate(zoneId);
		List<String> listHost = listHostAggregate.getHosts();

		for (int i = 0; i < data.getSelectItems().size(); i++) {
			try {
				String hyper = data.getSelectItems().get(i);
				if (listHost.size() == 0 || !listHost.contains(hyper)) {
					computeService.addHost(zoneId, hyper);
				}
			} catch (Exception e) {
				ActionResponse.actionFailed(e.toString(), 503);
			}
		}

		for (int i = 0; i < listHost.size(); i++) {
			try {
				if (!data.getSelectItems().contains(listHost.get(i))) {
					computeService.removeHost(zoneId, listHost.get(i));
				}
			} catch (Exception e) {
				ActionResponse.actionFailed(e.toString(), 503);
			}
		}


		return ActionResponse.actionSuccess();
	}

	//add Zone api
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/hostaggregate", method = RequestMethod.GET)
	public List<? extends HostAggregate> listHostAggregate() {
		List<? extends HostAggregate> listZones = computeService.listHostAggregate();
		logger.debug("listZones=" + listZones);
		return listZones;
	}

	/**
	 * createZone
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/hostaggregate/{zoneId}", method = RequestMethod.POST)
	public ActionResponse createZone(@PathVariable("zoneId") String zoneId) {
		if(!zoneId.startsWith("nova") || zoneId.replace("nova", "").length() != 1){
			return ActionResponse.actionFailed("ZONE 생성 규칙에 위배됩니다. ex)nova1, nova2 nova3", 500);
		}
		computeService.createZone(zoneId, zoneId);
		return ActionResponse.actionSuccess();
	}

	/**
	 * addHost
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/hostaggregate/{zoneId}/host/{host}", method = RequestMethod.POST)
	public HostAggregate addHost(@PathVariable("zoneId") String zoneId,
			@PathVariable("host") String host) {
		HostAggregate hostAggregate = computeService.addHost(zoneId, host);
		return hostAggregate;
	}

	/**
	 * removeHost
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/hostaggregate/{zoneId}/host/{host}", method = RequestMethod.DELETE)
	public HostAggregate removeHost(@PathVariable("zoneId") String zoneId,
			@PathVariable("host") String host) {
		HostAggregate hostAggregate = computeService.removeHost(zoneId, host);
		logger.debug("removeHost = " + hostAggregate);
		return hostAggregate;
	}

	/**
	 * deleteZone
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/hostaggregate/{zoneId}", method = RequestMethod.DELETE)
	public ActionResponse deleteZone(@PathVariable("zoneId") String zoneId) {
		ActionResponse actionResponse = computeService.deleteZone(zoneId);
		return actionResponse;
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/os-hypervisors/statistics", method = RequestMethod.GET)
	public HypervisorStatistics getHypervisorStatistics() {
		HypervisorStatistics hypervisorStatistics = computeService
				.getHypervisorStatistics();
		logger.debug("hypervisorStatistics=" + hypervisorStatistics);
		return hypervisorStatistics;
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/os-services", method = RequestMethod.GET)
	public ResponseEntity<String> listOsServices() {
		ResponseEntity<String> listOsServices = computeService.listOsServices();
		logger.debug("listOsServices=" + listOsServices);
		return listOsServices;
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/os-floating-ip-pools", method = RequestMethod.GET)
	public List<String> listFloatingIpsPoolNames() {
		List<String> listFloatingIpsPoolNames = computeService
				.listFloatingIpsPoolNames();
		logger.debug("listFloatingIpsPoolNames=" + listFloatingIpsPoolNames);
		return listFloatingIpsPoolNames;
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/os-floating-ip-pools/{poolName}", method = RequestMethod.POST)
	public CommonRespVO<FloatingIP> allocateFloatingIp(
			@PathVariable("poolName") String poolName) {
		CommonRespVO<FloatingIP> result = null;
		try {
			FloatingIP floatingIP = computeService.allocateFloatingIp(poolName);
			logger.debug("floatingIP=" + floatingIP);
			result = new CommonRespVO<FloatingIP>(true, null, floatingIP);
		} catch (Exception e) {
			loggerTra.error(e.getMessage(), e);
			result = new CommonRespVO<FloatingIP>(false, e.getMessage(), null);
		}
		return result;
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/os-keypairs", method = RequestMethod.GET)
	public List<Keypair> listKeypairs() {
		List<Keypair> listKeypairs = computeService.listKeypairs();
		logger.debug("listKeypairs=" + listKeypairs);
		return listKeypairs;
	}

}