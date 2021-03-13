package com.eluon.pim.repository;

import java.util.List;

import com.eluon.pim.value.ServerEventVO;
import com.eluon.pim.value.ServerStatVO;
import com.eluon.pim.value.ServerVO;

public interface ServerRepository {
	
	public List<ServerVO> getServerList() throws Exception;
	
	public List<ServerStatVO> getServerStat(int serverId);
	
	public ServerVO getServer(int serverId) throws Exception;
	
	public int insertServerInfo(ServerVO param);
	
	public int insertServerStat(ServerStatVO param);
	
	public void updateServerInfo(ServerVO param) throws Exception;
	
	public int insertServerEvent(ServerEventVO param);
}