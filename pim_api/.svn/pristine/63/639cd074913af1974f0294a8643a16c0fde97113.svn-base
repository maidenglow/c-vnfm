package com.eluon.pim.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.pim.repository.ServerRepository;
import com.eluon.pim.value.ServerEventVO;
import com.eluon.pim.value.ServerStatVO;
import com.eluon.pim.value.ServerVO;

@Service
public class PimServerService {
	@Autowired
	ServerRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(PimServerService.class);
	public int createServer(ServerVO param) {
		String serverIP = param.getServerIP();
		String port = param.getServerPort();
		
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		param.setRegDate(format.format(now));
		
		param.setServerIP("udp:"+serverIP+"/"+port);
		
		int result = 0;
		try {
			result = repository.insertServerInfo(param);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return result;
	}

	public List<ServerVO> serverList() throws Exception{
		return repository.getServerList();
	}

	public ServerVO server(int serverId) throws Exception{
		return repository.getServer(serverId);
	}
	
	public List<ServerStatVO> serverStatList(int serverId){
		return repository.getServerStat(serverId);
	}
	
	public void createServerEvent(ServerEventVO param){
		
		
		repository.insertServerEvent(param);
	}
}
