package com.eluon.pim.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.pim.repository.EventRepository;
import com.eluon.pim.value.ServerEventVO;
import com.eluon.pim.value.StorageEventVO;
import com.eluon.pim.value.SwitchEventVO;

@Service
public class PimEventService {
	@Autowired
	EventRepository repository;
	
	public List<ServerEventVO> serverEventList(int param) throws Exception{
		return repository.getServerEventList(param);
	}
	public List<SwitchEventVO> switchEventList(int param) throws Exception{
		return repository.getSwitchEventList(param);
	}
	public List<StorageEventVO> storageEventList(int param) throws Exception{
		return repository.getStorageEventList(param);
	}
}
