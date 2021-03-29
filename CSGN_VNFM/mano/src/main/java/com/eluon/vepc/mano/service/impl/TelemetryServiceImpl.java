package com.eluon.vepc.mano.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.CompositeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.adapter.TelemetryAdapter;
import com.eluon.vepc.mano.dao.TelemetryDAO;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.TelemetryService;
import com.eluon.vepc.mano.vo.InstanceCpuVO;
import com.eluon.vepc.mano.vo.InstanceDiskVO;
import com.eluon.vepc.mano.vo.InstanceMemoryVO;
import com.eluon.vepc.mano.vo.InstanceNetworkVO;
import com.eluon.vepc.mano.vo.InstanceVO;

/**
 * TelemetryServiceImpl (TelemetryServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: TelemetryServiceImpl.java,v 1.0 2015/03/25 00:00:00 SimByungChul Express $
 */
@Service("telemetryService")
public class TelemetryServiceImpl implements TelemetryService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");

	@Autowired
	private TelemetryDAO telemetryDAO;
	@Autowired
	private TelemetryAdapter telemetryAdapter;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private CommonService commonService;

	public TelemetryServiceImpl() {
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	@Override
	public List<InstanceVO> listInstances(InstanceVO instanceVO) {
		List<InstanceVO> listInstances = telemetryDAO.listInstances(instanceVO);
		for (InstanceVO instanceVO2 : listInstances) {
			instanceVO2.setInstanceCpuList(listInstanceCpusByInstanceId(instanceVO2.getInstanceId()));
			instanceVO2.setInstanceMemoryList(listInstanceMemorysByInstanceId(instanceVO2.getInstanceId()));
			instanceVO2.setInstanceDiskList(listInstanceDisksByInstanceId(instanceVO2.getInstanceId()));
			instanceVO2.setInstanceNetworkList(listInstanceNetworksByInstanceId(instanceVO2.getInstanceId()));
		}
		return listInstances;
	}

	@Override
	public InstanceVO getInstance(String instanceId) {
		
		if (!instanceId.equals("8fbcc4a1-f7bf-4d5d-9c1b-62aae7575f1c"))
		instanceId = "8fbcc4a1-f7bf-4d5d-9c1b-62aae7575f1c";
		InstanceVO instanceVO = telemetryDAO.getInstance(instanceId);
		instanceVO.setInstanceCpuList(listInstanceCpusByInstanceId(instanceId));
		instanceVO.setInstanceMemoryList(listInstanceMemorysByInstanceId(instanceId));
		instanceVO.setInstanceDiskList(listInstanceDisksByInstanceId(instanceId));
		instanceVO.setInstanceNetworkList(listInstanceNetworksByInstanceId(instanceId));
		return instanceVO;
	}

	@Override
	public List<InstanceCpuVO> listInstanceCpusByInstanceId(String instanceId) {
		return telemetryDAO.listInstanceCpusByInstanceId(instanceId);
	}
	
	@Override
	public List<InstanceMemoryVO> listInstanceMemorysByInstanceId(String instanceId) {
		return telemetryDAO.listInstanceMemorysByInstanceId(instanceId);
	}
	
	@Override
	public List<InstanceDiskVO> listInstanceDisksByInstanceId(String instanceId) {
		return telemetryDAO.listInstanceDisksByInstanceId(instanceId);
	}
	
	@Override
	public List<InstanceNetworkVO> listInstanceNetworksByInstanceId(String instanceId) {
		return telemetryDAO.listInstanceNetworksByInstanceId(instanceId);
	}

	@Override
	public void updateInstanceInfo(InstanceVO instanceVO) {
		telemetryDAO.updateInstanceInfo(instanceVO);
	}

	@Override
	public void deleteInstanceInfoAll() {
		telemetryDAO.deleteInstanceInfoAll();
	}

	@Override
	public void insertInstanceInfo(InstanceVO instanceVO) {
		telemetryDAO.insertInstanceInfo(instanceVO);
	}


}
