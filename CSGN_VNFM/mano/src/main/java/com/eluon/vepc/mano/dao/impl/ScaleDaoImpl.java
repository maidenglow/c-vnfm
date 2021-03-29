package com.eluon.vepc.mano.dao.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.dao.ScaleDao;
import com.eluon.vepc.mano.vo.GroupVO;
import com.eluon.vepc.mano.vo.InstanceCreateInfoVO;
import com.eluon.vepc.mano.vo.RealTimeScaleVO;
import com.eluon.vepc.mano.vo.ScaleGroupVO;
import com.eluon.vepc.mano.vo.ScaleInVO;
import com.eluon.vepc.mano.vo.ScaleNetworkRefItemVO;
import com.eluon.vepc.mano.vo.ScaleOutVO;

@Repository("scaleDao")
public class ScaleDaoImpl implements ScaleDao{
	@Autowired
	private SqlSession sqlSession;

	// nfv_instance_info
	@Override
	public List<LinkedHashMap<String, String>> getInstaceInfo() {
		return sqlSession.selectList("Scale.getInstaceInfo");
	}

	@Override
	public LinkedHashMap<String, String> getInstaceInfoId(String instance_id) {
		return sqlSession.selectOne("Scale.getInstaceInfoId", instance_id);
	}

	@Override
	public List<LinkedHashMap<String, String>> getInstaceInfoGroupNo(String group_no) {
		return sqlSession.selectList("Scale.getInstaceInfoGroupNo", group_no);
	}

	@Override
	public int getInstaceInfoCnt(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Scale.getInstaceInfoCnt", hm);
	}

	@Override
	public int getGroupInstaceInfoCnt(String group_no) {
		return sqlSession.selectOne("Scale.getGroupInstaceInfoCnt", group_no);
	}

	@Override
	public int postInstacneInfo(HashMap<String, Object> hm) {
		return sqlSession.insert("Scale.postInstacneInfo", hm);
	}
	
	@Override
	public int deleteInstacneInfo(String instance_no) {
		return sqlSession.insert("Scale.deleteInstacneInfo", instance_no);
	}

	// nfv_group_info
	@Override
	public List<ScaleGroupVO> getGroupInfo() {
		return sqlSession.selectList("Scale.getGroupInfo");
	}

	@Override
	public List<GroupVO> getGroupInfoSyns() {
		return sqlSession.selectList("Scale.getGroupInfoSyns");
	}
	
	@Override
	public GroupVO getGroupInfoGroupNo(String group_no) {
		return sqlSession.selectOne("Scale.getGroupInfoGroupNo", group_no);
	}
	
	@Override
	public int getGroupInfoCnt(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Scale.getGroupInfoCnt", hm);
	}
	
	@Override
	public GroupVO getGroupInfoImgInfo(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Scale.getGroupInfoImgInfo", hm);
	}
	
	@Override
	public int postGroupInfo(GroupVO groupVO) {
		return sqlSession.insert("Scale.postGroupInfo", groupVO);
	}

	@Override
	public int deleteGroupInfo(String group_no) {
		return sqlSession.insert("Scale.deleteGroupInfo", group_no);
	}

	// nfv_instance_creation_info
	@Override
	public InstanceCreateInfoVO getInstanceCreationInfoOne(String group_no) {
		return sqlSession.selectOne("Scale.getInstanceCreationInfoOne", group_no);
	}

	@Override
	public int postInstanceCreateInfo(InstanceCreateInfoVO instanceCreateInfoVO) {
		return sqlSession.insert("Scale.postInstanceCreateInfo", instanceCreateInfoVO);
	}

	@Override
	public int putInstanceCreateInfo(InstanceCreateInfoVO instanceCreateInfoVO) {
		return sqlSession.update("Scale.putInstanceCreateInfo", instanceCreateInfoVO);
	}
	
	@Override
	public int deleteInstanceCreateInfo(String group_no) {
		return sqlSession.insert("Scale.deleteInstanceCreateInfo", group_no);
	}

	// nfv_scale_in
	@Override
	public ScaleInVO getScaleInOne(String group_no) {
		return sqlSession.selectOne("Scale.getScaleInOne", group_no);
	}
	
	@Override
	public int postScaleIn(ScaleInVO scaleInVO) {
		return sqlSession.insert("Scale.postScaleIn", scaleInVO);
	}

	@Override
	public int putScaleIn(ScaleInVO scaleInVO) {
		return sqlSession.update("Scale.putScaleIn", scaleInVO);
	}
	
	@Override
	public int deleteScaleIn(String group_no) {
		return sqlSession.delete("Scale.deleteScaleIn", group_no);
	}
	
	// nfv_scale_out
	@Override
	public ScaleOutVO getScaleOutOne(String group_no) {
		return sqlSession.selectOne("Scale.getScaleOutOne", group_no);
	}

	@Override
	public int postScaleOut(ScaleOutVO scaleOutVO) {
		return sqlSession.insert("Scale.postScaleOut", scaleOutVO);
	}

	@Override
	public int putScaleOut(ScaleOutVO scaleOutVO) {
		return sqlSession.update("Scale.putScaleOut", scaleOutVO);
	}

	@Override
	public int deleteScaleOut(String group_no) {
		return sqlSession.delete("Scale.deleteScaleOut", group_no);
	}
	
	// nfv_instance_scale_history
	@Override
	public List<RealTimeScaleVO> getScaleHistory10Cycle() {
		return sqlSession.selectList("Scale.getScaleHistory10Cycle");
	}

	@Override
	public int postScaleHistory(RealTimeScaleVO realTimeScaleVO) {
		return sqlSession.insert("Scale.postScaleHistory", realTimeScaleVO);
	}
	
	@Override
	public int putScaleHistory(RealTimeScaleVO realTimeScaleVO) {
		return sqlSession.delete("Scale.putScaleHistory", realTimeScaleVO);
	}

	// nfv_networking_ref
	@Override
	public List<ScaleNetworkRefItemVO> getNetworkRefItem(String create_no) {
		return sqlSession.selectList("Scale.getNetworkRefItem", create_no);
	}

	@Override
	public int postNetworkRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Scale.postNetworkRef", hm);
	}

	@Override
	public int deleteNetworkRef(String create_no) {
		return sqlSession.delete("Scale.deleteNetworkRef", create_no);
	}


}
