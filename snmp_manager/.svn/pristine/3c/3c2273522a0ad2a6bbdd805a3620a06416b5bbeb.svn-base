package com.eluon.pim.snmp.service;

import java.util.HashMap;
import java.util.Map;

import org.snmp4j.smi.OID;

import com.eluon.pim.snmp.util.SnmpConnectionUtil;

public abstract class SnmpCommonService {
	
	protected Map<String,String> snmpInfo;
	protected String ipAddress;
	
	
	public SnmpCommonService(String ipAddress){
		this.ipAddress = ipAddress;
	}
	
	protected abstract OID[] getOids() throws Exception;
	
	protected void getSnmpInfo() throws Exception{
		try {
			snmpInfo = SnmpConnectionUtil.getResultMap(this.ipAddress, getOids());
		} catch (Exception e) {
			throw e;
		}		
	}
	
	protected Map<String, String> getResultMap(String oid){
		
		Map<String, String> result = new HashMap<>(); 
		for(String key : snmpInfo.keySet()){
			if(key.contains(oid)){
				result.put(key.split(oid+".")[1], snmpInfo.get(key));
			}
		}
		
		return result;
	}
	
	public static int parseInt(String num){
		int result = 0;
		try{
			result = Integer.parseInt(num);
		}catch(Exception e){}
		return result;
	}
	
	public static long parseLong(String num){
		long result = 0;
		try{
			result = Long.parseLong(num);
		}catch(Exception e){}
		return result;
	}
}
