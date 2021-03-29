package com.eluon.vepc.mano.rest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.Addresses;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.network.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.adapter.ComputeAdapter;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ComputeService;
import com.eluon.vepc.mano.service.NetworkingService;
import com.eluon.vepc.mano.service.ScaleService;
import com.eluon.vepc.mano.vo.RealTimeScaleVO;
import com.eluon.vepc.mano.vo.ScaleGroupVO;

@RestController
@RequestMapping("/scale")
public class ScaleController {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ScaleService scaleService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ComputeService computeService;
	
	@Autowired
	private NetworkingService networkingService;
	
	@RequestMapping(value="/{tenantId}/server/groups", method = RequestMethod.GET)
	public List<ScaleGroupVO> getlistGroups() {
		List<ScaleGroupVO> listServerGroups= scaleService.getServerGroups();
		logger.debug("listServerGroups="+ listServerGroups);

		return listServerGroups;
	}	
	
	
	@RequestMapping(value="/{tenantId}/server/groups", method = RequestMethod.POST)
	public void postScaleInfo(HttpServletResponse httpServletResponse, @RequestBody ScaleGroupVO bodyContent) throws IOException {
		logger.debug("bodyContent: "+ bodyContent.toString());
		logger.debug("Group_name: "+ bodyContent.getGroup_name());
		logger.debug("Group_no: "+ bodyContent.getGroup_no());
		
		httpServletResponse.getWriter().println(scaleService.postScaleInfo(bodyContent));
	}	
	
	
	@RequestMapping(value = "/core/eluon/realtime", method = RequestMethod.POST)
	public void postRealtimeScale(HttpServletResponse httpServletResponse, @RequestBody RealTimeScaleVO bodyContent) throws IOException {
		logger.debug("instance_id: "+ bodyContent.getInstance_id());
		logger.debug("instance_ip: "+ bodyContent.getInstance_ip());
		logger.debug("cpu: "+ bodyContent.getCpu());
		logger.debug("memory: "+ bodyContent.getMemory());
		logger.debug("cps: "+ bodyContent.getCps());
		
		if(bodyContent != null) {
			if("".equals(bodyContent.getInstance_id()) || null == bodyContent.getInstance_id()) {
				// instance_id가 null 일때 OpenStack에서 모든 instance 정보를 가져와 ip 비교하여 instance_id 찾기
				if(!"".equals(bodyContent.getInstance_ip()) && null != bodyContent.getInstance_ip()) {
					String instance_ip = bodyContent.getInstance_ip();
					List<String> networkNameList = new ArrayList<String>();
					
					List<? extends Network> listNetworks = networkingService.listNetworks();
					if(null != listNetworks && listNetworks.size() > 0) {
						for(int i=0; i<listNetworks.size(); i++) {
							networkNameList.add(listNetworks.get(i).getName());
						}
						
						List<? extends Server> listServers = computeService.listServers();
						for(int i=0; i<listServers.size(); i++) {
							logger.debug("InstanceName(" + i + "): "+ listServers.get(i).getName());

							Map<String, List<? extends Address>> addressessList = listServers.get(i).getAddresses().getAddresses();
							logger.debug("listServers.get(i).getAddresses().getAddresses().size(): "+ addressessList.size());
							
							for(int j=0; j<networkNameList.size(); j++) {
								List<? extends Address> addressList = listServers.get(i).getAddresses().getAddresses(networkNameList.get(j));
								if(null != addressList && addressList.size() > 0) {
									logger.debug(networkNameList.get(j) + "_addressList.size(): "+ addressList.size());
									
									for(int k=0; k<addressList.size(); k++) {
										// 동일한 instance_ip를 찾으면 instance_id setting
										if(instance_ip.equals(addressList.get(k).getAddr())) {
											logger.debug("instance_ip == addressList.get(" + k + ").getAddr(), instance_id: " + listServers.get(i).getId());
											logger.debug("instance_ip: " + instance_ip + ", addressList.get(" + k + ").getAddr(): "+ addressList.get(k).getAddr());
											// instance_id 찾음
											bodyContent.setInstance_id(listServers.get(i).getId());
											
											// 모든 반복문 중단
											j = networkNameList.size();
											i = listServers.size();
											break;
										} else {
											logger.debug("instance_ip != addressList.get(" + k + ").getAddr()");
										}
									}
								}
							}
						}
					}
					
					if("".equals(bodyContent.getInstance_id()) || null == bodyContent.getInstance_id()) {
						// OpenStack에서 가져온 정보와 비교 후에도 instance_id가 null 이면 request error
						httpServletResponse.getWriter().println(-1);
					}
					
				} else {
					// instance_id, instance_ip 모두 null 이면 request error
					httpServletResponse.getWriter().println(-1);
				}
			}
		}
		
		httpServletResponse.getWriter().println(scaleService.postRealtimeScale(bodyContent));
	}
	

}
