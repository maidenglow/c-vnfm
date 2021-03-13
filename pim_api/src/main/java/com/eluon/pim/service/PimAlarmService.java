package com.eluon.pim.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.pim.repository.AlarmRepository;
import com.eluon.pim.value.ServerAlarmVO;
import com.eluon.pim.value.StorageAlarmVO;
import com.eluon.pim.value.SwitchAlarmVO;

@Service
public class PimAlarmService {
	@Autowired
	AlarmRepository repository;
	
	public List<ServerAlarmVO> serverAlarmList(int param) throws Exception{
		return repository.getServerAlarmList(param);
	}
	public List<SwitchAlarmVO> switchAlarmList(int param) throws Exception{
		return repository.getSwitchAlarmList(param);
	}
	public List<StorageAlarmVO> storageAlarmList(int param) throws Exception{
		return repository.getStorageAlarmList(param);
	}
}
