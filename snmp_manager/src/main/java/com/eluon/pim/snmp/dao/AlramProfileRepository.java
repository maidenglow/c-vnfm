package com.eluon.pim.snmp.dao;

import java.util.List;

import com.eluon.pim.snmp.value.PimAlramProfileVO;

public class AlramProfileRepository {

	public List<PimAlramProfileVO> getAlramProfile(PimAlramProfileVO param){
		List<PimAlramProfileVO> result = SqlConnector.selectList("alram.selectAlramProfile", param);
		return result;
	}
}
