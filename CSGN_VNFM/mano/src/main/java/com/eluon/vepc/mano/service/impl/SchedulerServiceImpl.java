package com.eluon.vepc.mano.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.CompositeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.SchedulerService;
import com.eluon.vepc.mano.service.SystemService;
import com.eluon.vepc.mano.service.TelemetryService;
import com.eluon.vepc.mano.vo.InstanceVO;
import com.eluon.vepc.mano.vo.SystemVO;

/**
 * SchedulerServiceImpl (SchedulerServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: SchedulerServiceImpl.java,v 1.0 2015/04/15 00:00:00 SimByungChul Express $
 */
@Service("schedulerService")
public class SchedulerServiceImpl implements SchedulerService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private TelemetryService telemetryService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private CommonService commonService;

	public SchedulerServiceImpl() {
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	@Override
	public void updateSystemInfo() {
		SystemVO systemVO = new SystemVO();
		systemVO.setSystemId("SY000000000001");
		systemVO.setHostName("compute");
		systemVO.setHostType("linux");
		systemVO.setPrivateIp("127.0.0.1");
		systemVO.setPublicIp("112.133.98.5");
		systemVO.setOsName("redhat");
		systemVO.setTotalCoreCpu("16");
		systemVO.setUsedCoreCpu("8");
		systemVO.setFreeCoreCpu("8");
		systemVO.setUsedCpuPer(getRandomUsedPer());
		systemVO.setTotalMemory("32GB");
		systemVO.setUsedMemory("16GB");
		systemVO.setFreeMemory("16GB");
		systemVO.setUsedMemoryPer(getRandomUsedPer());
		systemVO.setTotalDisk("200GB");
		systemVO.setUsedDisk("100GB");
		systemVO.setFreeDisk("100GB");
		systemVO.setUsedDiskPer(getRandomUsedPer());
		systemVO.setTotalNetwork("34MB");
		systemVO.setTotalSendNetwork("17MB");
		systemVO.setTotalRecvNetwork("17MB");
		systemService.updateSystemInfo(systemVO);
	}

	@Override
	public void updateInstanceInfo() {
		InstanceVO instanceVO = new InstanceVO();
		instanceVO.setInstanceId("8fbcc4a1-f7bf-4d5d-9c1b-62aae7575f1c");
		instanceVO.setHostName("compute");
		instanceVO.setHostType("linux");
		instanceVO.setPrivateIp("127.0.0.1");
		instanceVO.setPublicIp("112.133.98.5");
		instanceVO.setOsName("redhat");
		instanceVO.setTotalCoreCpu("16");
		instanceVO.setUsedCoreCpu("8");
		instanceVO.setFreeCoreCpu("8");
		instanceVO.setUsedCpuPer(getRandomUsedPer());
		instanceVO.setTotalMemory("32GB");
		instanceVO.setUsedMemory("16GB");
		instanceVO.setFreeMemory("16GB");
		instanceVO.setUsedMemoryPer(getRandomUsedPer());
		instanceVO.setTotalDisk("200GB");
		instanceVO.setUsedDisk("100GB");
		instanceVO.setFreeDisk("100GB");
		instanceVO.setUsedDiskPer(getRandomUsedPer());
		instanceVO.setTotalNetwork("34MB");
		instanceVO.setTotalSendNetwork("17MB");
		instanceVO.setTotalRecvNetwork("17MB");
		telemetryService.updateInstanceInfo(instanceVO);
	}

	@Override
	public void deleteSystemInfoAll() {
		systemService.deleteSystemInfoAll();
	}

	@Override
	public void insertSystemInfo() {
		SystemVO systemVO = new SystemVO();
		systemVO.setSystemId("SY000000000001");
		systemVO.setHostName("compute");
		systemVO.setHostType("linux");
		systemVO.setPrivateIp("127.0.0.1");
		systemVO.setPublicIp("112.133.98.5");
		systemVO.setOsName("redhat");
		systemVO.setTotalCoreCpu("16");
		systemVO.setUsedCoreCpu("8");
		systemVO.setFreeCoreCpu("8");
		systemVO.setUsedCpuPer(getRandomUsedPer());
		systemVO.setTotalMemory("32GB");
		systemVO.setUsedMemory("16GB");
		systemVO.setFreeMemory("16GB");
		systemVO.setUsedMemoryPer(getRandomUsedPer());
		systemVO.setTotalDisk("200GB");
		systemVO.setUsedDisk("100GB");
		systemVO.setFreeDisk("100GB");
		systemVO.setUsedDiskPer(getRandomUsedPer());
		systemVO.setTotalNetwork("34MB");
		systemVO.setTotalSendNetwork("17MB");
		systemVO.setTotalRecvNetwork("17MB");
		systemService.insertSystemInfo(systemVO);
	}

	private String getRandomUsedPer() {
		return Integer.toString((int)(Math.random() * 100))+"%";
	}

	@Override
	public void deleteInstanceInfoAll() {
		telemetryService.deleteInstanceInfoAll();
	}

	@Override
	public void insertInstanceInfo() {
		InstanceVO instanceVO = new InstanceVO();
		instanceVO.setInstanceId("IN000000000001");
		instanceVO.setHostName("compute");
		instanceVO.setHostType("linux");
		instanceVO.setPrivateIp("127.0.0.1");
		instanceVO.setPublicIp("112.133.98.5");
		instanceVO.setOsName("redhat");
		instanceVO.setTotalCoreCpu("16");
		instanceVO.setUsedCoreCpu("8");
		instanceVO.setFreeCoreCpu("8");
		instanceVO.setUsedCpuPer(getRandomUsedPer());
		instanceVO.setTotalMemory("32GB");
		instanceVO.setUsedMemory("16GB");
		instanceVO.setFreeMemory("16GB");
		instanceVO.setUsedMemoryPer(getRandomUsedPer());
		instanceVO.setTotalDisk("200GB");
		instanceVO.setUsedDisk("100GB");
		instanceVO.setFreeDisk("100GB");
		instanceVO.setUsedDiskPer(getRandomUsedPer());
		instanceVO.setTotalNetwork("34MB");
		instanceVO.setTotalSendNetwork("17MB");
		instanceVO.setTotalRecvNetwork("17MB");
		telemetryService.insertInstanceInfo(instanceVO);
	}

}