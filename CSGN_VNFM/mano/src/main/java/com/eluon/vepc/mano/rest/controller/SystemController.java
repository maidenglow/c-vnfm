package com.eluon.vepc.mano.rest.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.model.compute.HostAggregate;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.rest.validator.SystemValidator;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ComputeService;
import com.eluon.vepc.mano.service.SystemService;
import com.eluon.vepc.mano.vo.AlarmVO;
import com.eluon.vepc.mano.vo.CodeVO;
import com.eluon.vepc.mano.vo.SystemVO;

/**
 * Systems Controller (SystemController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: SystemController.java,v 1.0 2015/01/27 00:00:00 SimByungChul Express $
 */
@RestController
@RequestMapping("/system")
public class SystemController{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	protected final static Logger loggerSin = LoggerFactory.getLogger("MANO_PROCESS_SIN");
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private ComputeService computeService;
	
	
	/**
	 * System List
	 * @method list
	 * @return
	 */
	@RequestMapping(value = "/systems", method = RequestMethod.GET)
	public List<HashMap<String, Serializable>> listSystems(@ModelAttribute SystemVO systemVO) {
		List<? extends Hypervisor> listSystems = computeService.getHypervisors();
		loggerTra.debug("ListHypervisor data="+ listSystems);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		List<? extends HostAggregate > listHostAggregate =  computeService.listHostAggregate();
		
		ArrayList<HashMap<String, Serializable>> list = new ArrayList<HashMap<String, Serializable>>();
		HashMap<String, Serializable> desMap = null;

		ArrayList<HashMap<String, Object>> child = null;
		HashMap<String, ArrayList<HashMap<String, Object>>> childrenMap = null;
		HashMap<String, Object> childMap = null;
		
		for (int i = 0; i < listHostAggregate.size(); i++) {
			HostAggregate hostAggregate = listHostAggregate.get(i);
		
			desMap = new HashMap<String, Serializable>();
			desMap.put("title", hostAggregate.getName());
			desMap.put("name", hostAggregate.getName());
			desMap.put("id", hostAggregate.getId());
			desMap.put("type", "hostAggregates");
			desMap.put("data", hostAggregate);
			
			childrenMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
			child = new ArrayList<HashMap<String, Object>>();
			
			desMap.put("children",childrenMap);
			childrenMap.put("child", child);
			list.add(desMap);
			
			int vcpus = 0;
			int vcpus_used = 0;
			int memory_mb_used = 0;
			int memory_mb = 0;
			int free_ram_mb = 0;
			int local_gb = 0;
			int local_gb_used = 0;
			int free_disk_gb = 0;
			
			for (int j = 0; j < listSystems.size(); j++) {
				Hypervisor hypervisor = listSystems.get(j);
				if(hostAggregate.getHosts().contains(hypervisor.getHypervisorHostname())){
					childMap = new HashMap<String, Object>();
					childMap.put("title", hypervisor.getHypervisorHostname());
					childMap.put("id", hypervisor.getId());
					childMap.put("type", "hypervisor");
					childMap.put("zoneId", hostAggregate.getId());
					childMap.put("info", hypervisor);
					child.add(childMap);
					
					vcpus = vcpus + hypervisor.getVirtualCPU();
					vcpus_used = vcpus_used + hypervisor.getVirtualUsedCPU();
					memory_mb_used = memory_mb_used + hypervisor.getLocalMemoryUsed();
					memory_mb = memory_mb + hypervisor.getLocalMemory();
					free_ram_mb = free_ram_mb + hypervisor.getFreeRam();
					local_gb = local_gb + hypervisor.getLocalDisk();
					local_gb_used = local_gb_used + hypervisor.getLocalDiskUsed();
					free_disk_gb = free_disk_gb + vcpus_used+hypervisor.getFreeDisk();
				}
			}
			
			desMap.put("vcpus", vcpus);
			desMap.put("vcpus_used", vcpus_used);
			desMap.put("memory_mb_used", memory_mb_used);
			desMap.put("memory_mb", memory_mb);
			desMap.put("free_ram_mb", free_ram_mb);
			desMap.put("local_gb", local_gb);
			desMap.put("local_gb_used", local_gb_used);
			desMap.put("free_disk_gb", free_disk_gb);
		}
		
		return list;
	}

	/**
	 * @param systemId
	 * @return
	 */
	@RequestMapping(value = "/systems/{id}", method = RequestMethod.GET)
	public Hypervisor getSystem(@PathVariable("id") String systemId) {
		List<? extends Hypervisor> listSystems = computeService.getHypervisors();
		Hypervisor hypervisor = null;
		for (int i = 0; i < listSystems.size(); i++) {
			Hypervisor hypervisorVO = listSystems.get(i);
			if(hypervisorVO.getId().equals(systemId)){
				hypervisor =  hypervisorVO;
			}
		}
		loggerTra.debug("Hypervisor data="+ hypervisor);
		return hypervisor;
	}
	
	/**
	 * Alarm List
	 * @method list
	 * @return
	 */
	@RequestMapping(value = "/alarms", method = RequestMethod.GET)
	public List<AlarmVO> listAlarms(@ModelAttribute AlarmVO alarmVO) {

		AlarmVO alarmParam = new AlarmVO();
		alarmParam.setAlarmStatus("AS001");
		List<AlarmVO> listAlarms = systemService.listAlarms(alarmParam);
		
		
		loggerTra.debug("listAlarms="+ listAlarms);
		return listAlarms;
	}

	/**
	 * @param alarmId
	 * @return
	 */
	@RequestMapping(value = "/alarms/{alarmId}", method = RequestMethod.GET)
	public AlarmVO getAlarm(@PathVariable("alarmId") String alarmId) {
		AlarmVO alarmVO = systemService.getAlarm(alarmId);
		loggerTra.debug("alarmVO="+ alarmVO);
		return alarmVO;
	}
	
	/**
	 * @param alarmId
	 * @return
	 */
	@RequestMapping(value="/alarms/{alarmId}", method = RequestMethod.PUT)
	public AlarmVO updateAlarm(@PathVariable("alarmId") String alarmId, @RequestBody AlarmVO alarmVO) {
		AlarmVO alarmVO2 = systemService.updateAlarm(alarmVO);
		loggerTra.debug("alarmVO2="+ alarmVO2);
		return alarmVO2;
	}

	/**
	 * Code List
	 * @method list
	 * @return
	 */
	@RequestMapping(value = "/codes", method = RequestMethod.GET)
	public List<CodeVO> listCodes(@ModelAttribute CodeVO codeVO) {
		List<CodeVO> listCodes = systemService.listCodes(codeVO);
		loggerTra.debug("listCodes="+ listCodes);
		return listCodes;
	}

	/**
	 * @param codeId
	 * @return
	 */
	@RequestMapping(value = "/codes/{code}", method = RequestMethod.GET)
	public CodeVO getCode(@PathVariable("code") String code) {
		CodeVO codeVO = systemService.getCode(code);
		loggerTra.debug("codeVO="+ codeVO);
		return codeVO;
	}
	
}