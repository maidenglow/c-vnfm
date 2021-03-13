package com.eluon.pim.snmp.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

public class SnmpConnectionUtil {

	private static SnmpConnectionUtil snmpService;

	private static Snmp snmp;
	private static TransportMapping<?> transport;
	private static CommunityTarget target; 

	private static final Logger logger = LoggerFactory.getLogger(SnmpConnectionUtil.class);


	private SnmpConnectionUtil(){

		// 초기화
		if(snmp == null){
			// SNMP 객체 생성
			try {
				transport = new DefaultUdpTransportMapping();
				snmp = new Snmp(transport);
				transport.listen();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(target == null){
			target = new CommunityTarget();
			target.setCommunity(new OctetString("public"));
			target.setRetries(2);
			target.setTimeout(1000);
			target.setVersion(SnmpConstants.version2c);
		}
	}

	public static synchronized SnmpConnectionUtil getInstance(){
		if(snmpService == null){
			snmpService = new SnmpConnectionUtil();
		}
		return snmpService;
	}


	private Snmp getSnmp() throws IOException {

		if(!transport.isListening()){

			transport.listen();

		}
		return snmp;
	}

	private void closeSnmp(){
		if(transport.isListening()){
			try {
				transport.close();
			} catch (IOException e) {
				logger.error("Snmp transport close error " + e.getMessage());
			}
		}
	}

	private CommunityTarget getTarget(String serverIp){

		Address targetAddress = GenericAddress.parse(serverIp);
		target.setAddress(targetAddress);
		return target;
	}

	public static synchronized Map<String,String> getResultMap(String serverIp, OID[] param) throws Exception{
		Map<String,String> result = new HashMap<String, String>();

		SnmpConnectionUtil util = SnmpConnectionUtil.getInstance();

		TableUtils utils = new TableUtils(util.getSnmp(), new DefaultPDUFactory(PDU.GETNEXT));
		List<TableEvent> events = utils.getTable(util.getTarget(serverIp), param, null, null);

		for(TableEvent event : events){
			if (!event.isError()) { 

				for (VariableBinding varBinding : event.getColumns()) { 
					if(varBinding != null)
						result.put(varBinding.getOid().toString(), varBinding.toValueString());
				}
				
			}else{
				logger.error("Snmp get Fail | target Ip:" +  serverIp +" | message :"+ event.getErrorMessage());
				Exception e = new Exception(event.getErrorMessage());
				throw e;
			}
		}
		util.closeSnmp();

		return result;
	}
}
