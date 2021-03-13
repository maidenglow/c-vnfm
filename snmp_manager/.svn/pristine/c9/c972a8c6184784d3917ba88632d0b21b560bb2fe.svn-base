package com.eluon.pim.snmp.util;

import java.io.IOException;
import java.util.Date;
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
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

public class Test {

	private static Test snmpService;

	private static Snmp snmp;
	private static TransportMapping<?> transport;
	private static CommunityTarget target; 

	private static final Logger logger = LoggerFactory.getLogger(SnmpConnectionUtil.class);


	private Test(){

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
			target.setTimeout(1500);
			target.setVersion(SnmpConstants.version2c);
		}
	}

	public static synchronized Test getInstance(){
		if(snmpService == null){
			snmpService = new Test();
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

	public Map<String,Object> getResultMap(String serverIp, OID[] param) throws Exception{
		Map<String,Object> result = new HashMap<String, Object>();
		
		
		TableUtils utils = new TableUtils(getSnmp(), new DefaultPDUFactory(PDU.GETNEXT));
		List<TableEvent> events = utils.getTable(getTarget(serverIp), param, null, null);
		
		
		
		for(TableEvent event : events){
			if (!event.isError()) { 
				
				for (VariableBinding varBinding : event.getColumns()) { 
					
					if(varBinding != null)
					result.put(varBinding.getOid().toString(), varBinding.toValueString());
				}
			}
		}
		
		
		
//		TreeUtils treeUtils = new TreeUtils(snmpService.getSnmp(), new DefaultPDUFactory(PDU.GETNEXT));
		
//		List<TreeEvent> events = treeUtils.getSubtree(snmpService.getTarget(serverIp), new OID(oid));
//
//		for (TreeEvent event : events) {
//			if (!event.isError()) { 
//				VariableBinding[] varBindings = event.getVariableBindings(); 
//
//				for (VariableBinding varBinding : varBindings) { 
//					result.put(varBinding.getOid().toString().split(oid+".")[1], varBinding);
//				}
//			}else{
//				logger.error("Snmp get Fail | target Ip:" +  serverIp +" | message :"+ event.getErrorMessage());
//				throw new Exception();
//			}
//		}

		snmpService.closeSnmp();

		return result;
	}
	
	public Map<String,String> getResultMap(String serverIp, OID oid) throws Exception{
		Map<String,String> result = new HashMap<String, String>();
		
		TreeUtils treeUtils;

		treeUtils = new TreeUtils(snmpService.getSnmp(), new DefaultPDUFactory(PDU.GETNEXT));
		List<TreeEvent> events = treeUtils.getSubtree(snmpService.getTarget(serverIp), oid);

		for (TreeEvent event : events) {
			if (!event.isError()) { 
				VariableBinding[] varBindings = event.getVariableBindings(); 

				for (VariableBinding varBinding : varBindings) { 
					varBinding.toValueString(); 
					result.put(varBinding.getOid().toString().split(oid+".")[1], varBinding.toValueString());
				}
			}else{
				logger.error("Snmp get Fail | target Ip:" +  serverIp +" | message :"+ event.getErrorMessage());
				throw new Exception();
			}
		}

		snmpService.closeSnmp();

		return result;
	}
	
	public static void main(String[] args) throws Exception{
		
		OID[] param = new OID[]{new OID("1.3.6.1.4.1.2021.4"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.2.3.1.2"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.2.1"),new OID("1.3.6.1.2.1.2.2.1.2"), new OID("1.3.6.1.2.1.2.2.1.10"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.2.2.1.16"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2"),new OID("1.3.6.1.2.1.25.3.2.1.1"), new OID("1.3.6.1.2.1.25.3.2.1.2"), new OID("1.3.6.1.2.1.25.3.2.1.3"), new OID("1.3.6.1.2.1.25.3.3.1.2")};
		
		System.out.println("length :"+param.length);
		System.out.println("Start 1 : " + new Date() );
		for(OID i : param){
			
			Map<String,String> resutl1 = Test.getInstance().getResultMap("udp:192.168.2.175/161", i);
		}
		System.out.println("END 1 : " + new Date() );
		
		
		
		System.out.println("Start 2 : " + new Date() );
		Map<String,Object>  result = Test.getInstance().getResultMap("udp:192.168.2.175/161", param);
		System.out.println("END 2 : " + new Date() );
		
		
		
		
		for(String key : result.keySet()){
			System.out.println(key + ":" + result.get(key));
		}
		
//		Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
//	    snmp.listen();
//
//	    CommunityTarget target = new CommunityTarget();
//	    target.setCommunity(new OctetString("public"));
//	    target.setVersion(SnmpConstants.version2c);
//	    
//	    Address targetAddress = GenericAddress.parse("udp:192.168.2.175/161");
//		target.setAddress(targetAddress);
//	    target.setTimeout(3000);    //3s
//	    target.setRetries(1);
//
//	    PDU pdu = new PDU();
//	    pdu.setType(PDU.GETBULK);
//	    pdu.setMaxRepetitions(1); 
//	    pdu.setNonRepeaters(0);
//	    VariableBinding[] array = {new VariableBinding(new OID("1.3.6.1.2.1.25")),
//	                               new VariableBinding(new OID("1.3.6.1.2.1.25.3.2.1.1")),
//	                               new VariableBinding(new OID("1.3.6.1.2.1.25.3.2.1.2")),
//	                               new VariableBinding(new OID("1.3.6.1.2.1.25.3.2.1.3")),
//	                               new VariableBinding(new OID("1.3.6.1.2.1.25.3.3.1.2"))};
//	    pdu.addAll(array);
//
//	    //pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.2000.1.2.5.1.3"))); 
//
//	    ResponseEvent responseEvent = snmp.send(pdu, target);
//	    
//	    
//	    PDU response = responseEvent.getResponse();
//
//	    if (response == null) {
//		    System.out.println("TimeOut...");
//	    } else {
//		    if (response.getErrorStatus() == PDU.noError) {
//	               Vector<? extends VariableBinding> vbs = response.getVariableBindings();
//	               for (VariableBinding vb : vbs) {
//	                   System.out.println(vb.getVariable().toString());
//		        }
//		    } else {
//		        System.out.println("Error:" + response.getErrorStatusText());
//		    }
//	    }
	}
}
