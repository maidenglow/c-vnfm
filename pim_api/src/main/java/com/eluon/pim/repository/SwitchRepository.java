package com.eluon.pim.repository;

import java.util.List;

import com.eluon.pim.value.StatParamVO;
import com.eluon.pim.value.SwitchStatVO;
import com.eluon.pim.value.SwitchVO;

public interface SwitchRepository {
	public List<SwitchVO> getSwitchList() throws Exception;
	public SwitchVO getSwitch(int switchId) throws Exception;
	public int insertSwitchInfo(SwitchVO param);
	public int deleteSwitchInfo(int switchId);
	public int deleteSwitchStat10S(int switchId);
	public int deleteSwitchStatInfo(int switchId);
	public int deleteSwitchEventInfo(int switchId);
	public int deleteSwitchAlarmInfo(int switchId);
	public List<SwitchStatVO> getSwitchStatInfo(StatParamVO param) throws Exception;

}
