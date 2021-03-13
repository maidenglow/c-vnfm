package com.eluon.pim.snmp.job.switches;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@SuppressWarnings("unchecked")
public class SwitchUsageCal {
	private final Logger logger = LoggerFactory.getLogger(SwitchUsageCal.class);
	
	// for 5minute stat
	public Map<Integer, String> getNicStatUsage(Map<Integer, List<String>> swMap)
	{
		String firstNic=null;
		String lastNic=null;
		String resultStr=null;
		List <String> listStr = null;
		Map<Integer, String> resultMap=new HashMap<>();
		// SWITCH쩡보들을 돌면서 통계를 내기 위한 첫 번째 값과 나중 값을 가져온다. 
		for (Integer key : swMap.keySet()) { 
			listStr = swMap.get(key);
			firstNic = listStr.get(0);							// 첫 번쨰 통계값.
			lastNic = listStr.get(listStr.size()-1); // 나중 통계 값. 
			//resultStr = getNicSumResult(listStr);
			resultStr= getNicStatResult(firstNic, lastNic);
			resultMap.put(key, resultStr);
			logger.error("key={}, resultStr={}", key, resultStr);
		}

		return resultMap;
	}
	// for 1hour, 1day statistics
	public Map<Integer, String> getNicSumUsage(Map<Integer, List<String>> swMap)
	{
		String resultStr=null;
		List <String> listStr = null;
		Map<Integer, String> resultMap=new HashMap<>();

		// SWITCH쩡보들을 돌면서 통계를 내기 위한 첫 번째 값과 나중 값을 가져온다. 
		for (Integer key : swMap.keySet()) { 
			listStr = swMap.get(key);
			resultStr = getNicSumResult(listStr);
			resultMap.put(key, resultStr);
			logger.error("key={}, resultStr={}", key, resultStr);
		}

		return resultMap;
	}
	
	// for 1hour, 1day statistics
	private String getNicSumResult(List <String> nicList)
	{
		Map<String, String> nicUsage = null;// = new Gson().fromJson( listStr, Map.class);
		Map<String, String> nicResult = new HashMap<>();
		//Map<String, Integer[]> nicSum = new HashMap<>();
		String rtxVal=null;

		String resultStr = null;
		String sumStr=null;
		
		//Integer[] usageSum = new Integer[2];
		
		long nRx=0;
		long nTx=0;
		
		long sumRx=0;
		long sumTx=0;
		
		
		for(String nicStr : nicList) // key 시간별 NIC_USAGE별로..  
		{
			nicUsage = new Gson().fromJson( nicStr, Map.class); 	// ex) (key, nicStr) - ("lo","0/0")

			for(String Intf : nicUsage.keySet())
			{
				rtxVal = nicUsage.get(Intf);
				// get DB DATA
				if( rtxVal.contains("/") ) 
				{
					nRx = Long.parseLong(rtxVal.split("/")[0]);
					nTx = Long.parseLong(rtxVal.split("/")[1]);
				}
				// sum을 누적하고 있는 local data(hashmap)가져온다. 
				sumStr = nicResult.get(Intf);
				if( sumStr == null )
				{
					nicResult.put(Intf, "0/0");
					sumStr = nicResult.get(Intf);

				}
				if(sumStr.contains("/"))
				{
					sumRx = Long.parseLong(sumStr.split("/")[0]);
					sumTx = Long.parseLong(sumStr.split("/")[1]);

				}
				sumRx+=nRx;
				sumTx+=nTx;
				resultStr = sumRx + "/" +sumTx;
				nicResult.put(Intf, resultStr);
			}
			logger.debug("nicResult=" + nicResult);
		}
		return new Gson().toJson(nicResult);
	}
	// for 5minute stat
	private String getNicStatResult(String firstNic, String lastNic)
	{
		Map<String, String> firstNicStr = new Gson().fromJson( firstNic, Map.class);
		Map<String, String> lastNicStr = new Gson().fromJson( lastNic, Map.class);
		Map<String, String> nicResult = new HashMap<>();;
		
		String usageStr=null;
		String resultStr = null;
		
		long firstRx=0;
		long firstTx=0;
		long lastRx=0;
		long lastTx=0;
		
		for(String key : firstNicStr.keySet())
		{
			logger.debug("firstNicStr={}", firstNicStr);
			logger.debug("lastNicStr={}", lastNicStr);

			usageStr = firstNicStr.get(key);
			if( usageStr.contains("/") ) 
			{
				firstRx = Long.parseLong(usageStr.split("/")[0]);
				firstTx = Long.parseLong(usageStr.split("/")[1]);
			}
			usageStr = lastNicStr.get(key);
			if( usageStr.contains("/") ) 
			{
				lastRx = Long.parseLong(usageStr.split("/")[0]);
				lastTx = Long.parseLong(usageStr.split("/")[1]);
			}
			//resultStr = String.format("%ld/%ld", (long)(lastRx-firstRx), (long)(lastTx-firstTx));
			resultStr = (long)(lastRx-firstRx) + "/" + (long)(lastTx-firstTx);
			logger.debug("firstRx={}, firstTx={}, lastRx={}, lastTx={}, resultStr={}", 
					firstRx, firstTx, lastRx, lastTx, resultStr);
			nicResult.put(key, resultStr );
		}
			
		return new Gson().toJson(nicResult);
	}
	
}
