package com.eluon.pim.snmp.dao;

import java.util.List;

import com.eluon.pim.snmp.value.PimEventInfoVO;
import com.eluon.pim.snmp.value.PimEventProfileVO;

public class EventProfileRepository {
	public List<PimEventProfileVO> getEventProfile() {
		List<PimEventProfileVO> result = SqlConnector.selectList("event.getEventProfile");
		return result;
	}
	
	public void insertEventServerInfo(PimEventInfoVO param) throws Exception {
		SqlConnector.insert("event.insertEventServer", param);
	}
	public void insertEventSwitchInfo(PimEventInfoVO param) throws Exception {
		SqlConnector.insert("event.insertEventSwitch", param);
	}
	public void insertEventStorageInfo(PimEventInfoVO param) throws Exception {
		SqlConnector.insert("event.insertEventStorage", param);
	}
}
