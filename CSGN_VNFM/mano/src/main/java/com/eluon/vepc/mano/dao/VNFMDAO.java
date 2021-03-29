package com.eluon.vepc.mano.dao;

import java.util.HashMap;
import java.util.List;

import com.eluon.vepc.mano.vo.ActionLogVO;
import com.eluon.vepc.mano.vo.AlarmNeutronAgentVO;
import com.eluon.vepc.mano.vo.AlarmNovaServicesVO;
import com.eluon.vepc.mano.vo.EndpointVO;
import com.eluon.vepc.mano.vo.EventLogVO;
import com.eluon.vepc.mano.vo.VNFDescriptorVO;
import com.eluon.vepc.mano.vo.VnfcVO;

/**
 * AccountDAO Interface (AccountDAO)
 *
 */
public interface VNFMDAO {

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
	
}


