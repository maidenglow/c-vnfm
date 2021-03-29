package com.eluon.vepc.mano.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.dao.TelemetryDAO;
import com.eluon.vepc.mano.vo.InstanceCpuVO;
import com.eluon.vepc.mano.vo.InstanceDiskVO;
import com.eluon.vepc.mano.vo.InstanceMemoryVO;
import com.eluon.vepc.mano.vo.InstanceNetworkVO;
import com.eluon.vepc.mano.vo.InstanceVO;

/**
 * TelemetryDAO Implement (TelemetryDAOImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: TelemetryDAOImpl.java,v 1.1 2015/03/25 00:00:00 SimByungChul Express $
 */
@Repository("telemetryDAO")
public class TelemetryDAOImpl implements TelemetryDAO {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private SqlSession sqlSession;

	public TelemetryDAOImpl() {
	}

	@Override
	public List<InstanceVO> listInstances(InstanceVO instanceVO) {
		return sqlSession.selectList("Telemetry.listInstances", instanceVO);
	}
	
	@Override
	public InstanceVO getInstance(String instanceId) {
		return (InstanceVO) sqlSession.selectOne("Telemetry.getInstance", instanceId);
	}
	
	@Override
	public List<InstanceCpuVO> listInstanceCpusByInstanceId(String instanceId) {
		return sqlSession.selectList("Telemetry.listInstanceCpusByInstanceId", instanceId);
	}
	
	@Override
	public List<InstanceMemoryVO> listInstanceMemorysByInstanceId(String instanceId) {
		return sqlSession.selectList("Telemetry.listInstanceMemorysByInstanceId", instanceId);
	}
	
	@Override
	public List<InstanceDiskVO> listInstanceDisksByInstanceId(String instanceId) {
		return sqlSession.selectList("Telemetry.listInstanceDisksByInstanceId", instanceId);
	}
	
	@Override
	public List<InstanceNetworkVO> listInstanceNetworksByInstanceId(String instanceId) {
		return sqlSession.selectList("Telemetry.listInstanceNetworksByInstanceId", instanceId);
	}

	@Override
	public void updateInstanceInfo(InstanceVO instanceVO) {
		sqlSession.update("Telemetry.updateInstanceInfo", instanceVO);
	}

	@Override
	public void deleteInstanceInfoAll() {
		sqlSession.delete("Telemetry.deleteInstanceInfoAll");
	}

	@Override
	public void insertInstanceInfo(InstanceVO instanceVO) {
		sqlSession.insert("Telemetry.insertInstanceInfo", instanceVO);
	}
	
	
}