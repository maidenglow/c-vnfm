package com.eluon.pim.repository;

import java.util.List;

import com.eluon.pim.value.ServerEventVO;
import com.eluon.pim.value.StorageEventVO;
import com.eluon.pim.value.SwitchEventVO;

public interface EventRepository {
	public List<ServerEventVO> getServerEventList(int param) throws Exception;
	public List<SwitchEventVO> getSwitchEventList(int param) throws Exception;
	public List<StorageEventVO> getStorageEventList(int param) throws Exception;
}
