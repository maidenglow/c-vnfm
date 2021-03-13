package com.eluon.pim.snmp.dao;

import java.util.List;

import com.eluon.pim.snmp.value.server.PimServerSecVO;
import com.eluon.pim.snmp.value.server.PimServerStatVO;
import com.eluon.pim.snmp.value.server.PimServerVO;

public class ServerRepository {

	public List<PimServerVO> getServerList(){
		List<PimServerVO> result = SqlConnector.selectList("server.getServerList");
		return result;
	}

	public PimServerVO getServer(int serverId){
		PimServerVO result =  SqlConnector.selectOne("server.getServer", serverId);
		return result;
	}

	public void insertServerInfo(PimServerVO param) {
		SqlConnector.insert("server.insertServerInfo", param);
	}

	public void insertServerSec(PimServerSecVO param){
		SqlConnector.insert("server.insertServerStatSec", param);
	}
	public void insertServerStat(PimServerStatVO param){
		SqlConnector.insert("server.insertServerStat", param);
	}
	
	public void updateServerInfo(PimServerVO param) {
		SqlConnector.update("server.updateServerInfo", param);
	}
	
	public List<PimServerSecVO> getServerStatSec(int serverId){
		List<PimServerSecVO> result = SqlConnector.selectList("server.getServerStatSec",serverId);
		return result;
	}
	
	public List<PimServerStatVO> getServerStat(PimServerStatVO param){
		List<PimServerStatVO> result = SqlConnector.selectList("server.getServerStat",param);
		return result;
	}

}
