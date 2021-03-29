package com.eluon.vepc.mano.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.eluon.vepc.mano.vo.GroupVO;
import com.eluon.vepc.mano.vo.InstanceCreateInfoVO;
import com.eluon.vepc.mano.vo.RealTimeScaleVO;
import com.eluon.vepc.mano.vo.ScaleGroupVO;
import com.eluon.vepc.mano.vo.ScaleInVO;
import com.eluon.vepc.mano.vo.ScaleNetworkRefItemVO;
import com.eluon.vepc.mano.vo.ScaleOutVO;

public interface ScaleDao {
	public List<LinkedHashMap<String, String>> getInstaceInfo();
	public LinkedHashMap<String, String> getInstaceInfoId(String instance_id);
	public List<LinkedHashMap<String, String>> getInstaceInfoGroupNo(String group_no);
	public int getInstaceInfoCnt(HashMap<String, Object> hm);
	public int getGroupInstaceInfoCnt(String group_no);
	public int postInstacneInfo(HashMap<String, Object> hm);
	public int deleteInstacneInfo(String instance_no);
	
	public List<ScaleGroupVO> getGroupInfo();
	public List<GroupVO> getGroupInfoSyns();
	public GroupVO getGroupInfoGroupNo(String group_no);
	public GroupVO getGroupInfoImgInfo(HashMap<String, Object> hm);
	public int getGroupInfoCnt(HashMap<String, Object> hm);
	public int postGroupInfo(GroupVO groupVO);
	public int deleteGroupInfo(String group_no);
	
	public InstanceCreateInfoVO getInstanceCreationInfoOne(String group_no);
	public int postInstanceCreateInfo(InstanceCreateInfoVO instanceCreateInfoVO);
	public int putInstanceCreateInfo(InstanceCreateInfoVO instanceCreateInfoVO);
	public int deleteInstanceCreateInfo(String group_no);
	
	public ScaleInVO getScaleInOne(String group_no);
	public int postScaleIn(ScaleInVO scaleInVO);
	public int putScaleIn(ScaleInVO scaleInVO);
	public int deleteScaleIn(String group_no);
	
	public ScaleOutVO getScaleOutOne(String group_no);
	public int postScaleOut(ScaleOutVO scaleOutVO);
	public int putScaleOut(ScaleOutVO scaleOutVO);
	public int deleteScaleOut(String group_no);
	
	public List<RealTimeScaleVO> getScaleHistory10Cycle();
	public int postScaleHistory(RealTimeScaleVO realTimeScaleVO);
	public int putScaleHistory(RealTimeScaleVO realTimeScaleVO);
	
	public List<ScaleNetworkRefItemVO> getNetworkRefItem(String create_no);
	public int postNetworkRef(HashMap<String, Object> hm);
	public int deleteNetworkRef(String create_no);
	
}
