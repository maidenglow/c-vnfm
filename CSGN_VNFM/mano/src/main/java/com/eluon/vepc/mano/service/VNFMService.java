package com.eluon.vepc.mano.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.ext.Hypervisor;

import com.eluon.vepc.mano.vo.ActionLogVO;
import com.eluon.vepc.mano.vo.AlarmNeutronAgentVO;
import com.eluon.vepc.mano.vo.AlarmNovaServicesVO;
import com.eluon.vepc.mano.vo.EndpointVO;
import com.eluon.vepc.mano.vo.EventLogVO;
import com.eluon.vepc.mano.vo.VNFDescriptorVO;
import com.eluon.vepc.mano.vo.VnfcVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * VNFMService Interface(VNFMService)
 *
 */
public interface VNFMService {
	
	public void addVnfdInfo(VNFDescriptorVO vnfdVO);
	public int updateVnfd(VNFDescriptorVO vnfdVO);
	public List<VNFDescriptorVO> getVnfdList(VNFDescriptorVO vnfdVO);
	public int deleteVnfd(VNFDescriptorVO vnfdVO);
	public String getVnfdNamefromVnfcId(String inData);
	
	
	public void addVnfcInfo(VnfcVO vnfdVO);
	public int updateVnfc(VnfcVO vnfdVO);
	public List<VnfcVO> getVnfcList(VnfcVO vnfdVO);
	public List<VnfcVO> notCompleteList(String inData);
	public List<VnfcVO> getVnfdRepoList(VnfcVO vnfdVO);
	public int deleteVnfc(VnfcVO vnfdVO);
	public String getVnfcLastIndex(HashMap<String, Object> hm);
	public List<AlarmNovaServicesVO> getAlarmNovaServicesList(AlarmNovaServicesVO vnfdVO);
	public List<AlarmNeutronAgentVO> getAlarmNertronList(AlarmNeutronAgentVO vnfdVO);
	public List<EndpointVO> getEndpointList(EndpointVO vnfdVO);
	public List<ActionLogVO> getInstanceActionLog(String inData);
	public void addEvnetLogInfo(EventLogVO vnfdVO);
	public List<EventLogVO> getEvnetLogList(EventLogVO vnfdVO);
	
	public String setNetwork(String name, String network_name, String ipAddress);
	public Flavor createFlavor(String tenantId, String param) throws JsonParseException, JsonMappingException, IOException;
	public String serviceScaleOutProcess(VnfcVO vnfc);
	public String serviceScaleInProcess(VnfcVO vnfc);
	
	public void deletePortSplit(String portId);
	public void deleteFolder(String parentPath);
	public void deleteFile(String filePath);
	public String deleteServerProcess(String vnfc_id);
	
	public String getVnfcInfo(String vnfd_id);
	public void setVnfcInfo();
	public void setAgentData(String msg, String ip);
	public List<Object> getHosts(String type, List<Hypervisor> selListhyper, VnfcVO vnfc);
	public String getVnfdName(String vnfd_id);
}
