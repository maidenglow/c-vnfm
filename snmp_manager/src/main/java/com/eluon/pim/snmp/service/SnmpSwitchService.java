package com.eluon.pim.snmp.service;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.OID;

/**
 * <pre>
 * com.eluon.pim.snmp.service
 * _SnmpSwitchService.java
 * </pre>
 * 
 * Desc : 
 * @author : moonsj
 * @Date 2017. 7. 6.
 * @Version : 1.0
 */
public class SnmpSwitchService extends SnmpCommonService{
	//Nic
	//	private String oidNicCount;
	private String oidNicDesc;
	private String oidNicStatus;
	private String oidNicRx;
	private String oidNicTx;
	private final Logger logger = LoggerFactory.getLogger(SnmpSwitchService.class);
	
	public SnmpSwitchService(String ipAddress){
		super(ipAddress);
		
		try{
			Configuration config = new PropertiesConfiguration("property/snmp.properties");
			//NIC 관련
			oidNicDesc = config.getString("oid.switch.nic.desc");
			oidNicStatus = config.getString("oid.switch.nic.status");
			//NIC Usage관련
			oidNicRx = config.getString("oid.switch.nic.rx");
			oidNicTx = config.getString("oid.switch.nic.tx");
			
			getSnmpInfo();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO : 로그 처리 요망
		}
		
	}
	
	protected OID[] getOids() throws Exception{
		OID[] oids = new OID[]{
			new OID(oidNicDesc),
			new OID(oidNicStatus),
			new OID(oidNicRx),
			new OID(oidNicTx)
		};
		return oids;
	}
	
	public Map<String, String> getNicInfo() throws Exception{
		Map<String, String> desc =  getResultMap(oidNicDesc);
		Map<String, String> status =  getResultMap(oidNicStatus);

		Map<String, String> nicInfo = new HashMap<>();

		logger.debug("desc={}", desc.toString());
		// Desc 기준으로 Nic Status 정보 추출
		for(String key : desc.keySet()){
			String statusStr = status.get(key).equals("1") ? "on" : "off";
			nicInfo.put(desc.get(key), statusStr);
			
		}
		return nicInfo;
	}
	public Map<String, String> getNicUsage() throws Exception{
		Map<String, String> desc =  getResultMap(oidNicDesc);
		Map<String, String> rx = getResultMap(oidNicRx);
		Map<String, String> tx = getResultMap(oidNicTx);

		Map<String, String> nicUsage = new HashMap<>();
		String usageStr;

		// Desc 기준으로 Nic Status 정보 추출
		for(String key : desc.keySet()){
			usageStr=null;

			// SWITCH_STAT_10SEC -- NIC_USAGE RX/TX
			usageStr = String.format("%s/%s", rx.get(key), tx.get(key));
			nicUsage.put(desc.get(key), usageStr);
			logger.debug("key={}, rx={}, tx={}", key, rx.get(key), tx.get(key));
			
		}
		return nicUsage;
	}
}
