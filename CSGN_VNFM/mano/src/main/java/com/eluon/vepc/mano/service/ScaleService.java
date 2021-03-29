package com.eluon.vepc.mano.service;

import java.util.List;

import com.eluon.vepc.mano.vo.RealTimeScaleVO;
import com.eluon.vepc.mano.vo.ScaleGroupVO;

public interface ScaleService {
	
	public List<ScaleGroupVO> getServerGroups();
	
	public int postScaleInfo(ScaleGroupVO scaleGroupVO);

	public int postRealtimeScale(RealTimeScaleVO realTimeScaleVO);
}
