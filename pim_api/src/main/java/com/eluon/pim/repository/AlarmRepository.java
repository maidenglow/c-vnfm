package com.eluon.pim.repository;

import java.util.List;

import com.eluon.pim.value.ServerAlarmVO;
import com.eluon.pim.value.StorageAlarmVO;
import com.eluon.pim.value.SwitchAlarmVO;

public interface AlarmRepository {
	public List<ServerAlarmVO> getServerAlarmList(int param) throws Exception;
	public List<SwitchAlarmVO> getSwitchAlarmList(int param) throws Exception;
	public List<StorageAlarmVO> getStorageAlarmList(int param) throws Exception;

}
