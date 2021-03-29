package com.eluon.vepc.mano.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.CompositeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.dao.SystemDAO;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.SystemService;
import com.eluon.vepc.mano.vo.AlarmVO;
import com.eluon.vepc.mano.vo.CodeVO;
import com.eluon.vepc.mano.vo.SystemCpuVO;
import com.eluon.vepc.mano.vo.SystemDiskVO;
import com.eluon.vepc.mano.vo.SystemMemoryVO;
import com.eluon.vepc.mano.vo.SystemNetworkVO;
import com.eluon.vepc.mano.vo.SystemVO;

/**
 * SystemServiceImpl (SystemServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: SystemServiceImpl.java,v 1.0 2015/03/25 00:00:00 SimByungChul Express $
 */
@Service("systemService")
public class SystemServiceImpl implements SystemService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");

	@Autowired
	private SystemDAO systemDAO;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private CommonService commonService;

	public SystemServiceImpl() {
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	@Override
	public List<SystemVO> listSystems(SystemVO systemVO) {
		List<SystemVO> listSystems = systemDAO.listSystems(systemVO);
		for (SystemVO systemVO2 : listSystems) {
			systemVO2.setSystemCpuList(listSystemCpusBySystemId(systemVO2.getSystemId()));
			systemVO2.setSystemMemoryList(listSystemMemorysBySystemId(systemVO2.getSystemId()));
			systemVO2.setSystemDiskList(listSystemDisksBySystemId(systemVO2.getSystemId()));
			systemVO2.setSystemNetworkList(listSystemNetworksBySystemId(systemVO2.getSystemId()));
		}
		return listSystems;
	}

	@Override
	public SystemVO getSystem(String systemId) {
		SystemVO systemVO = systemDAO.getSystem(systemId);
		systemVO.setSystemCpuList(listSystemCpusBySystemId(systemVO.getSystemId()));
		systemVO.setSystemMemoryList(listSystemMemorysBySystemId(systemVO.getSystemId()));
		systemVO.setSystemDiskList(listSystemDisksBySystemId(systemVO.getSystemId()));
		systemVO.setSystemNetworkList(listSystemNetworksBySystemId(systemVO.getSystemId()));
		return systemVO;
	}

	@Override
	public List<SystemCpuVO> listSystemCpusBySystemId(String systemId) {
		return systemDAO.listSystemCpusBySystemId(systemId);
	}
	
	@Override
	public List<SystemMemoryVO> listSystemMemorysBySystemId(String systemId) {
		return systemDAO.listSystemMemorysBySystemId(systemId);
	}
	
	@Override
	public List<SystemDiskVO> listSystemDisksBySystemId(String systemId) {
		return systemDAO.listSystemDisksBySystemId(systemId);
	}
	
	@Override
	public List<SystemNetworkVO> listSystemNetworksBySystemId(String systemId) {
		return systemDAO.listSystemNetworksBySystemId(systemId);
	}
	
	@Override
	public void registAlarm(AlarmVO alarmVO) {
		systemDAO.registAlarm(alarmVO);
	}
	
	@Override
	public List<AlarmVO> listAlarms(AlarmVO alarmVO) {
		return systemDAO.listAlarms(alarmVO);
	}
	
	@Override
	public AlarmVO getAlarm(String alarmId) {
		return systemDAO.getAlarm(alarmId);
	}

	@Override
	public void deleteAlarm(AlarmVO alarmVO) {
		systemDAO.deleteAlarm(alarmVO);		
	}
	
	@Override
	public AlarmVO updateAlarm(AlarmVO alarmVO) {
		int res = systemDAO.updateAlarm(alarmVO);
		if (res ==1) return systemDAO.getAlarm(alarmVO.getAlarmId());
		return null;
	}

	@Override
	public List<CodeVO> listCodes(CodeVO codeVO) {
		return systemDAO.listCodes(codeVO);
	}

	@Override
	public CodeVO getCode(String code) {
		return systemDAO.getCode(code);
	}

	@Override
	public void updateSystemInfo(SystemVO systemVO) {
		systemDAO.updateSystemInfo(systemVO);
		
	}

	@Override
	public void deleteSystemInfoAll() {
		systemDAO.deleteSystemInfoAll();
		
	}

	@Override
	public void insertSystemInfo(SystemVO systemVO) {
		systemDAO.insertSystemInfo(systemVO);
		
	}

}
