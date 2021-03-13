package com.eluon.pim.snmp.service;

import org.snmp4j.Snmp;
import org.snmp4j.smi.Address;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.OctetString;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.mp.MPv3;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.security.USM;
import org.snmp4j.smi.GenericAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import java.io.IOException;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import com.eluon.pim.snmp.dao.ServerRepository;
import com.eluon.pim.snmp.dao.SwitchRepository;
import com.eluon.pim.snmp.dao.EventProfileRepository;
import com.eluon.pim.snmp.value.switches.PimSwitchVO;
import com.google.gson.Gson;
import com.eluon.pim.snmp.value.PimEventInfoVO;
import com.eluon.pim.snmp.value.PimEventProfileVO;
import com.eluon.pim.snmp.value.server.PimServerVO;

import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SnmpTrapReceiver implements CommandResponder {
	private MultiThreadedMessageDispatcher dispatcher;
	private Snmp snmp = null;
	private Address listenAddress;
	private ThreadPool threadPool;
	private final Logger logger = LoggerFactory.getLogger(SnmpTrapReceiver.class);
	private String oidSysDescr;
	
	public enum EVENT_TYPE{
		NONE, SERVER, SWITCH, STORAGE
	}
	
	public enum SUBS_TYPE{
		ALARM(1), EVENT(2);
		private int val;
		private SUBS_TYPE(int val){
			this.val = val;
		}
		public int getValue(){
			return this.val;
		}
	}
	
	private EVENT_TYPE eventType;
	private int systemId;
	
	@Override
	public synchronized void processPdu(CommandResponderEvent event) {
		int no;
		String [] msgStr=null;
		EventProfileRepository eventRepo = new EventProfileRepository();
		List<PimEventProfileVO> eventList = null;
		PimEventInfoVO 	eventInsert=null;
		StringBuffer msg = new StringBuffer();
		Date now = new Date();
		SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		msg.append(event.toString());
		Vector<? extends VariableBinding> varBinds = event.getPDU().getVariableBindings();
		if (varBinds != null && !varBinds.isEmpty()) {
			Iterator<? extends VariableBinding> varIter = varBinds.iterator();
			
			while (varIter.hasNext()) {
				VariableBinding var = varIter.next();
				try{
					msgStr = var.toString().split("\\s*=\\s*");
				} catch(Exception e) {
					e.printStackTrace();
					continue;
				}
				logger.error("msgStr[0]={}, [1]={}.", msgStr[0], msgStr[1]);
				
				// Message를 가져와서 
				if(msgStr[0].equals(this.getOidSysDescr()))
				{					
					try {
						// event_profile_info에 일치하는 값이 있을 경우, 
						eventList  = eventRepo.getEventProfile();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					no = getEventProfile(eventList, msgStr[1]);
					if( no < 0)
					{
						logger.debug("not match event_profile_info ");
						break;
					}					
					// DB로부터 SERVER, SWITCH, STORAGE에 일치하는 정보를 가져온다. 
					getSystemInfo(event.getPeerAddress().toString());
					
					// SERVER or SWITCH or STORAGE EVENT_INFO Insert parameter set
					eventInsert = new PimEventInfoVO();
					eventInsert.setTargetNo(this.getsystemId());
					eventInsert.setEventTime(dateForm.format(now));
					eventInsert.setProfileNo(no);
					eventInsert.setDescription(msgStr[1]);
					logger.debug("eventtype={}, eventInsert={}",this.geteventType(), eventInsert.toString());

					// 각 장비에의 EVENT_INFO테이블에 insert한다. 
					try {
						if (this.geteventType() == EVENT_TYPE.SERVER) {
							eventRepo.insertEventServerInfo(eventInsert);
						}
						else if ( this.geteventType() == EVENT_TYPE.SWITCH) {
							eventRepo.insertEventSwitchInfo(eventInsert);
						}
						else if( this.geteventType() == EVENT_TYPE.STORAGE) {
							eventRepo.insertEventStorageInfo(eventInsert);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// HTTP notify
					SubscriptService subsSvc = new SubscriptService();
					subsSvc.sendSubscription(SUBS_TYPE.EVENT.getValue(), 
							no, (new Gson().toJson(eventRepo)));		
				}
				msg.append(var.toString()).append(";\n");
			}
		}
		logger.error("Message Received: " + msg.toString());
		logger.error(" --- TYPE :  " + event.getPDU().getType());
		logger.error(" --- PeerIP : " + event.getPeerAddress());

	}


	private void init() throws UnknownHostException, IOException, ConfigurationException {
		threadPool = ThreadPool.create("Trap", 2);
		dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
		listenAddress = GenericAddress.parse(System.getProperty("snmp4j.listenAddress", "udp:0.0.0.0/162"));
		TransportMapping<?> transport;
		if (listenAddress instanceof UdpAddress) {
			transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
		} else {
			transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
		}
		snmp = new Snmp(dispatcher, transport);
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
		USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
		SecurityModels.getInstance().addSecurityModel(usm);
		snmp.listen();
	}

	public void run() {
		try {
			init();
			snmp.addCommandResponder(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public String getOidSysDescr() {
		return oidSysDescr;
	}

	public void setOidSysDescr(String oidSysDescr) {
		this.oidSysDescr = oidSysDescr;
	}
	
	public int getServerId(List<PimServerVO> serverList, String IpAddr)
	{
		int Id=-1;
		int nIdx=0;
		String strIp[]=IpAddr.split("/");
		String svrIp[]=null;
		for(PimServerVO server : serverList) { 		// DB의 서버정보를 돌면서
			nIdx = (server.getServerIP().contains(":")?1:0);

			svrIp = server.getServerIP().split(":|/");
			logger.debug("svrIp=[{}] <=> trapIp=[{}]", svrIp[nIdx], strIp[0]);

			if(svrIp[nIdx].equals(strIp[0])) // 수신한 IP와 DB상의 IP가 일치하면 
			{
				Id = server.getServerId();			// 해당하는 ID를 가져옴. 
				logger.debug("Id:"+Id);
				break;
			}
		}
		return Id;
	}
	
	public int getSwitchId(List<PimSwitchVO> switchList, String IpAddr)
	{
		int Id=-1;
		int nIdx=0;
		String strIp[]=IpAddr.split("/");
		String swIp[]=null;

		for(PimSwitchVO sw : switchList) {			// DB의 서버정보를 돌면서
			nIdx = (sw.getIp().contains(":")?1:0);
			swIp = sw.getIp().split(":|/");
			logger.debug("swIp=[{}] <=> trapIp=[{}]", swIp[nIdx], strIp[0]);
			if(swIp[nIdx].equals(strIp[0]))			// 수신한 IP와 DB상의 IP가 일치하면 
			{
				Id = sw.getSwitchId();				// 해당하는 ID를 가져옴.
				logger.debug("Id:"+Id);
				break;
			}
		}
		return Id;
	}
	
	public void getSystemInfo(String IpAddr)
	{
		int nId;
		ServerRepository svRepo = new ServerRepository();
		SwitchRepository swRepo = new SwitchRepository();
		List<PimServerVO> serverList = null;
		List<PimSwitchVO> switchList = null;	
		try {
			switchList = swRepo.getSwitchList();
			serverList = svRepo.getServerList();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}
		
		// Server의 IP 와 비교. 
		nId = getServerId(serverList, IpAddr);
		if( nId >=0 ) {
			this.seteventType(EVENT_TYPE.SERVER);
			this.setsystemId(nId);
			return ;
		}
		// Switch의 IP와 비교. 
		nId = getSwitchId(switchList, IpAddr);
		if( nId >= 0)
		{
			this.seteventType(EVENT_TYPE.SERVER);
			this.setsystemId(nId);
			return ;
		}
		
		// STORAGE TO DO 
		logger.error("Not found information about "+IpAddr);
		this.seteventType(EVENT_TYPE.NONE);
		this.setsystemId(0);
		return ;
	}
	
	public int getEventProfile(List<PimEventProfileVO> eventResult, String msg)
	{
		int idx=-1;
		for(PimEventProfileVO ev : eventResult )
		{
			logger.debug("EVENT_PROFILE={}, TrapMsg={}", ev.getKeyword(), msg);
			if( msg.contains( ev.getKeyword() ))
			{
				idx = ev.getNo();
				return idx;
			}
		}
		return idx;
	}
	
	public EVENT_TYPE geteventType() {
		return eventType;
	}


	public void seteventType(EVENT_TYPE eventType) {
		this.eventType = eventType;
	}


	public int getsystemId() {
		return systemId;
	}


	public void setsystemId(int systemId) {
		this.systemId = systemId;
	}
	
}
