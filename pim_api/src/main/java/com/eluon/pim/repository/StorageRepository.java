package com.eluon.pim.repository;

import java.util.List;

import com.eluon.pim.value.StatParamVO;
import com.eluon.pim.value.StorageStatVO;
import com.eluon.pim.value.StorageVO;

public interface StorageRepository {
	public List<StorageVO> getStorageList() throws Exception;
	public StorageVO getStorage(int storageId) throws Exception;
	public int insertStorageInfo(StorageVO param);
	public int deleteStorageInfo(int storageId);
	public int deleteStorageStat10S(int storageId);
	public int deleteStorageStatInfo(int storageId);
	public int deleteStorageEventInfo(int storageId);
	public int deleteStorageAlarmInfo(int storageId);
	public List<StorageStatVO> getStorageStatInfo(StatParamVO param) throws Exception;
}
