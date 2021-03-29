package com.eluon.vepc.mano.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.dao.SystemDAO;
import com.eluon.vepc.mano.vo.AlarmVO;
import com.eluon.vepc.mano.vo.CodeVO;
import com.eluon.vepc.mano.vo.SystemCpuVO;
import com.eluon.vepc.mano.vo.SystemDiskVO;
import com.eluon.vepc.mano.vo.SystemMemoryVO;
import com.eluon.vepc.mano.vo.SystemNetworkVO;
import com.eluon.vepc.mano.vo.SystemVO;

/**
 * SystemDAO Implement (SystemDAOImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: SystemDAOImpl.java,v 1.1 2015/03/25 00:00:00 SimByungChul Express $
 */
@Repository("systemDAO")
public class SystemDAOImpl implements SystemDAO {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private SqlSession sqlSession;

	public SystemDAOImpl() {
	}
	
	@Override
	public List<SystemVO> listSystems(SystemVO systemVO) {
		return sqlSession.selectList("System.listSystems", systemVO);
	}
	
	@Override
	public SystemVO getSystem(String systemId) {
		return (SystemVO) sqlSession.selectOne("System.getSystem", systemId);
	}
	
	@Override
	public List<SystemCpuVO> listSystemCpusBySystemId(String systemId) {
		return sqlSession.selectList("System.listSystemCpusBySystemId", systemId);
	}
	
	@Override
	public List<SystemMemoryVO> listSystemMemorysBySystemId(String systemId) {
		return sqlSession.selectList("System.listSystemMemorysBySystemId", systemId);
	}
	
	@Override
	public List<SystemDiskVO> listSystemDisksBySystemId(String systemId) {
		return sqlSession.selectList("System.listSystemDisksBySystemId", systemId);
	}
	
	@Override
	public List<SystemNetworkVO> listSystemNetworksBySystemId(String systemId) {
		return sqlSession.selectList("System.listSystemNetworksBySystemId", systemId);
	}
	
	@Override
	public void registAlarm(AlarmVO alarmVO) {
		sqlSession.selectList("System.registAlarm", alarmVO);
	}
	
	@Override
	public List<AlarmVO> listAlarms(AlarmVO alarmVO) {
		return sqlSession.selectList("System.listAlarms", alarmVO);
	}
	
	@Override
	public AlarmVO getAlarm(String alarmId) {
		return (AlarmVO) sqlSession.selectOne("System.getAlarm", alarmId);
	}

	@Override
	public void deleteAlarm(AlarmVO alarmVO) {
		sqlSession.delete("System.deleteAlarm", alarmVO);
	}
	
	@Override
	public int updateAlarm(AlarmVO alarmVO) {
		return sqlSession.update("System.updateAlarm", alarmVO);
	}

	@Override
	public List<CodeVO> listCodes(CodeVO codeVO) {
		return sqlSession.selectList("System.listCodes", codeVO);
	}

	@Override
	public CodeVO getCode(String code) {
		return (CodeVO) sqlSession.selectOne("System.getCode", code);
	}

	@Override
	public void updateSystemInfo(SystemVO systemVO) {
		sqlSession.update("System.updateSystemInfo", systemVO);
	}

	@Override
	public void deleteSystemInfoAll() {
		sqlSession.insert("System.deleteSystemInfoAll");
	}

	@Override
	public void insertSystemInfo(SystemVO systemVO) {
		sqlSession.insert("System.insertSystemInfo", systemVO);
	}

}