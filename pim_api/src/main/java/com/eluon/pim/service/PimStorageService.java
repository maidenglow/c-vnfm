package com.eluon.pim.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.pim.repository.StorageRepository;
import com.eluon.pim.value.StatParamVO;
import com.eluon.pim.value.StorageStatVO;
import com.eluon.pim.value.StorageVO;

@Service
public class PimStorageService {
	@Autowired
	StorageRepository repository;
	
	private static final Logger logger = LoggerFactory.getLogger(PimStorageService.class);

	public List<StorageVO> storageList() throws Exception{
		return repository.getStorageList();
	}
	
	public StorageVO storageOne(int storageId) throws Exception{
		return repository.getStorage(storageId);
	}
	
	public int createStorage(StorageVO param)
	{
		int result=0;
		Date now = new Date();

		// IP/PORT
		param.setStorageIP("udp:"+param.getStorageIP()+"/"+param.getStoragePort());

		// REG_TIME
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		param.setRegDate(format.format(now));
		
		try {
			result = repository.insertStorageInfo(param);
			
			logger.debug("StroageNo"+param.getStorageNo());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}
	
	public Integer[] deleteStorage(int storageId) throws Exception{
		Integer[] result = new Integer[5];
		
		try {
			result[4] = repository.deleteStorageAlarmInfo(storageId);
			result[3] = repository.deleteStorageEventInfo(storageId);
			result[2] = repository.deleteStorageStatInfo(storageId);
			result[1] = repository.deleteStorageStat10S(storageId);
			result[0] = repository.deleteStorageInfo(storageId);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		logger.debug(result.toString());
		return result;
	}
	
	public List<StorageStatVO> StorageStatList(StatParamVO param) throws Exception {
		return repository.getStorageStatInfo(param);
	}
}
