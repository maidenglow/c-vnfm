package com.eluon.vepc.mano.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.dao.VNFMDAO;
import com.eluon.vepc.mano.vo.ActionLogVO;
import com.eluon.vepc.mano.vo.AlarmNeutronAgentVO;
import com.eluon.vepc.mano.vo.AlarmNovaServicesVO;
import com.eluon.vepc.mano.vo.EndpointVO;
import com.eluon.vepc.mano.vo.EventLogVO;
import com.eluon.vepc.mano.vo.VNFDescriptorVO;
import com.eluon.vepc.mano.vo.VnfcVO;

/**
 * AccountDAO Implement (AccountDAOImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountDAOImpl.java,v 1.1 2014/12/15 00:00:00 SimByungChul Express $
 */
@Repository("vnfmDAO")
public class VNFMDAOImpl implements VNFMDAO {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private SqlSession sqlSessionNova;
	
	/*@Autowired
	private SqlSession sqlSessionNeutron;*/
	
	
	public VNFMDAOImpl() {
	}

	@Override
	public void addVnfdInfo(VNFDescriptorVO vnfdVO) {
		sqlSession.insert("Vnfm.addVnfdInfo", vnfdVO);
	}

	@Override
	public int updateVnfd(VNFDescriptorVO vnfdVO) {
		return sqlSession.update("Vnfm.updateVnfd", vnfdVO);
	}

	@Override
	public List<VNFDescriptorVO> getVnfdList(VNFDescriptorVO vnfdVO) {
		return sqlSession.selectList("Vnfm.getVnfdList", vnfdVO);
	}

	@Override
	public int deleteVnfd(VNFDescriptorVO vnfdVO) {
		return sqlSession.delete("Vnfm.deleteVnfd", vnfdVO);
	}

	@Override
	public String getVnfdNamefromVnfcId(String inData) {
		return sqlSession.selectOne("Vnfm.getVnfdNamefromVnfcId", inData);
	}
	
	@Override
	public void addVnfcInfo(VnfcVO vnfdVO) {
		sqlSession.insert("Vnfm.addVnfcInfo", vnfdVO);
	}

	@Override
	public int updateVnfc(VnfcVO vnfdVO) {
		return sqlSession.update("Vnfm.updateVnfc", vnfdVO);
	}

	@Override
	public List<VnfcVO> getVnfcList(VnfcVO vnfdVO) {
		return sqlSession.selectList("Vnfm.getVnfcList", vnfdVO);
	}
	
	@Override
	public List<VnfcVO> notCompleteList(String inData) {
		return sqlSession.selectList("Vnfm.notCompleteList", inData);
	}
	
	@Override
	public List<VnfcVO> getVnfdRepoList(VnfcVO vnfdVO) {
		return sqlSession.selectList("Vnfm.getVnfdRepoList", vnfdVO);
	}
	
	@Override
	public int deleteVnfc(VnfcVO vnfdVO) {
		return sqlSession.delete("Vnfm.deleteVnfc", vnfdVO);
	}
	
	@Override
	public String getVnfcLastIndex(HashMap<String, Object> hm){
		return sqlSession.selectOne("Vnfm.getVnfcLastIndex", hm);
	}
	
	@Override
	public List<AlarmNovaServicesVO> getAlarmNovaServicesList(AlarmNovaServicesVO vnfdVO) {
		return sqlSessionNova.selectList("Vnfm.getAlarmNovaServicesList", vnfdVO);
	}
	
	@Override
	public List<AlarmNeutronAgentVO> getAlarmNertronList(AlarmNeutronAgentVO vnfdVO) {
		return sqlSessionNova.selectList("Vnfm.getAlarmNertronList", vnfdVO);
	}
	
	@Override
	public List<EndpointVO> getEndpointList(EndpointVO vnfdVO) {
		return sqlSessionNova.selectList("Vnfm.getEndpointList", vnfdVO);
	}
	
	@Override
	public List<ActionLogVO> getInstanceActionLog(String inData) {
		return sqlSessionNova.selectList("Vnfm.getInstanceActionLog", inData);
	}
	
	@Override
	public void addEvnetLogInfo(EventLogVO vnfdVO) {
		sqlSession.insert("Vnfm.addEvnetLogInfo", vnfdVO);
	}

	@Override
	public List<EventLogVO> getEvnetLogList(EventLogVO vnfdVO) {
		return sqlSession.selectList("Vnfm.getEvnetLogList", vnfdVO);
	}
	
}