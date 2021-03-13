package com.eluon.pim.snmp.dao;

import java.util.List;

import com.eluon.pim.snmp.value.switches.PimSwitchSecVO;
import com.eluon.pim.snmp.value.switches.PimSwitchStatVO;
import com.eluon.pim.snmp.value.switches.PimSwitchVO;

public class SwitchRepository {

	public List<PimSwitchVO> getSwitchList() throws Exception{
		List<PimSwitchVO> result = SqlConnector.selectList("switch.getSwitchList");
		return result;
	}

	public PimSwitchVO getSwitch(int serverID) throws Exception{
		PimSwitchVO result =  SqlConnector.selectOne("switch.getSwitch");
		return result;
	}
	public List<PimSwitchSecVO> getSwitchSecMin(String date) throws Exception{
		List<PimSwitchSecVO> result =  SqlConnector.selectList("switch.getSwitchSecMin", date);
		return result;
	}
	public List<PimSwitchStatVO> getSwitchStatInfoHour(String date) throws Exception{
		List<PimSwitchStatVO> result =  SqlConnector.selectList("switch.getSwitchStatInfoHour", date);
		return result;
	}
	public List<PimSwitchStatVO> getSwitchStatInfoDay(String date) throws Exception{
		List<PimSwitchStatVO> result =  SqlConnector.selectList("switch.getSwitchStatInfoDay", date);
		return result;
	}
	public void insertSwitchnfo(PimSwitchVO param) throws Exception{
		SqlConnector.insert("switch.insertSwitchInfo", param);
	}

	public void updateSwitchInfo(PimSwitchVO param) throws Exception{
		SqlConnector.update("switch.updateSwitchInfo", param);
	}
	
	public void insertSwitchStat10Sec(PimSwitchSecVO param) throws Exception{
		SqlConnector.insert("switch.insertSwitchStat10Sec", param);
	}
	
	public void insertSwitchStat(PimSwitchStatVO param) throws Exception{
		SqlConnector.insert("switch.insertSwitchStatInfo", param);
	}
}
