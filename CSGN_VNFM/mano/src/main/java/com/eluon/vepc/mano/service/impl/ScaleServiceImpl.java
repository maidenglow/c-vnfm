package com.eluon.vepc.mano.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eluon.vepc.mano.adapter.ComputeAdapter;
import com.eluon.vepc.mano.common.dao.NFVCommonDAO;
import com.eluon.vepc.mano.dao.ScaleDao;
import com.eluon.vepc.mano.service.ScaleService;
import com.eluon.vepc.mano.vo.InstanceCreateInfoVO;
import com.eluon.vepc.mano.vo.RealTimeScaleVO;
import com.eluon.vepc.mano.vo.ScaleGroupVO;
import com.eluon.vepc.mano.vo.ScaleInVO;
import com.eluon.vepc.mano.vo.ScaleNetworkRefItemVO;
import com.eluon.vepc.mano.vo.ScaleOutVO;

@Service("scaleService")
public class ScaleServiceImpl implements ScaleService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ScaleDao scaleDao;
	
	@Autowired
	private ComputeAdapter computeAdapter;
	
	@Autowired
	private NFVCommonDAO nfvCommonDAO;

	@Transactional
	public List<ScaleGroupVO> getServerGroups() {
		// 1. group 정보 select
		List<ScaleGroupVO> resultList = scaleDao.getGroupInfo();
		
		if(resultList != null && resultList.size() > 0) {
			for(int i=0; i<resultList.size(); i++) {
				// 1.1 networking 정보 setting
				List<ScaleNetworkRefItemVO> networkResult = scaleDao.getNetworkRefItem(resultList.get(i).getCreate_no());
				resultList.get(i).setNetwork_info(networkResult);
				
				// 1.2 instance_name setting
				List<LinkedHashMap<String, String>> instanceResult = scaleDao.getInstaceInfoGroupNo(resultList.get(i).getGroup_no());
				List<String> instance_id = new ArrayList<String>();
				List<String> instance_name = new ArrayList<String>();
				
				if(instanceResult != null && instanceResult.size() > 0) {
					for(LinkedHashMap<String, String> map : instanceResult) {
						instance_id.add(map.get("instance_id"));
						instance_name.add(map.get("instance_name"));
					}

					resultList.get(i).setInstance_name(instance_name);
				}
			}
		}
		
		return resultList;
	}

	@Transactional
	public int postScaleInfo(ScaleGroupVO scaleGroupVO) {
		HashMap<String, Object> hm = new HashMap<String,Object>();
	
		// 1. nfv_group_info check(없으면 return -1)
		hm.put("group_no", scaleGroupVO.getGroup_no());
		hm.put("group_name", scaleGroupVO.getGroup_name());
		int groupCnt = scaleDao.getGroupInfoCnt(hm);
		if(groupCnt < 1) {
			return -1;
		}
		
		// 2. nfv_instance_creation_info check(없으면 insert, 있으면 update)
		checkInstanceCreateInfo(scaleGroupVO);
		
		// 3. nfv_scale_in check(없으면 insert, 있으면 update)
		checkScaleIn(scaleGroupVO);

		// 4. nfv_scale_out check(없으면 insert, 있으면 update)
		checkScaleOut(scaleGroupVO);
	
		return 1;
	}
	
	private int checkInstanceCreateInfo(ScaleGroupVO scaleGroupVO) {
		HashMap<String,Object> refMap = null;
		String create_no = null;
		String network_no = null;
		
		InstanceCreateInfoVO instanceCreateInfoVO = new InstanceCreateInfoVO();
		instanceCreateInfoVO.setGroup_no(scaleGroupVO.getGroup_no());
		instanceCreateInfoVO.setImg_id(scaleGroupVO.getImg_id());
		instanceCreateInfoVO.setImg_name(scaleGroupVO.getImg_name());
		instanceCreateInfoVO.setFlavor_id(scaleGroupVO.getFlavor_id());
		instanceCreateInfoVO.setFlavor_name(scaleGroupVO.getFlavor_name());
		instanceCreateInfoVO.setKeypair_name(scaleGroupVO.getKeypair_name());
		
		InstanceCreateInfoVO instanceCreationResult = scaleDao.getInstanceCreationInfoOne(scaleGroupVO.getGroup_no());
		if(instanceCreationResult == null) {
			// 1. nfv_instance_creation_info insert
			//table_seq
			create_no = nfvCommonDAO.getSeq("A3");
			instanceCreateInfoVO.setCreate_no(create_no);
			scaleDao.postInstanceCreateInfo(instanceCreateInfoVO);
			
		} else {
			// 1. nfv_instance_creation_info update
			create_no = instanceCreationResult.getCreate_no();
			instanceCreateInfoVO.setCreate_no(create_no);
			scaleDao.putInstanceCreateInfo(instanceCreateInfoVO);
			
			// 1.1. nfv_networking_ref delete
			scaleDao.deleteNetworkRef(create_no);

		}
		
		// 2. nfv_networking_ref insert
		for(int i=0; i<scaleGroupVO.getNetwork_info().size(); i++) {
			network_no = nfvCommonDAO.getSeq("A7");
			
			refMap = new HashMap<String,Object>();
			refMap.put("network_no", network_no);
			refMap.put("create_no", create_no);
			refMap.put("network_id", scaleGroupVO.getNetwork_info().get(i).getNetwork_id());
			refMap.put("network_name", scaleGroupVO.getNetwork_info().get(i).getNetwork_name());
			scaleDao.postNetworkRef(refMap);
		}
		
		return 1;
	}
	
	private int checkScaleIn(ScaleGroupVO scaleGroupVO) {
		String in_no = null;
		
		ScaleInVO scaleInVO = new ScaleInVO();
		scaleInVO.setGroup_no(scaleGroupVO.getGroup_no());
		scaleInVO.setIn_cpu(scaleGroupVO.getIn_cpu());
		scaleInVO.setIn_memory(scaleGroupVO.getIn_memory());
		scaleInVO.setIn_cps(scaleGroupVO.getIn_cps());
		
		ScaleInVO scaleInResult = scaleDao.getScaleInOne(scaleGroupVO.getGroup_no());
		if(scaleInResult == null) {
			// insert
			//table_seq
			in_no = nfvCommonDAO.getSeq("A4");
			scaleInVO.setIn_no(in_no);
			scaleDao.postScaleIn(scaleInVO);
			
		} else {
			// update
			in_no = scaleInResult.getIn_no();
			scaleInVO.setIn_no(in_no);
			scaleDao.putScaleIn(scaleInVO);
		}
		
		return 1;
	}
	
	private int checkScaleOut(ScaleGroupVO scaleGroupVO) {
		String out_no = null;
		
		ScaleOutVO scaleOutVO = new ScaleOutVO();
		scaleOutVO.setGroup_no(scaleGroupVO.getGroup_no());
		scaleOutVO.setOut_cpu(scaleGroupVO.getOut_cpu());
		scaleOutVO.setOut_memory(scaleGroupVO.getOut_memory());
		scaleOutVO.setOut_cps(scaleGroupVO.getOut_cps());
		
		ScaleOutVO scaleOutResult = scaleDao.getScaleOutOne(scaleGroupVO.getGroup_no());
		if(scaleOutResult == null) {
			// insert
			//table_seq
			out_no = nfvCommonDAO.getSeq("A5");
			scaleOutVO.setOut_no(out_no);
			scaleDao.postScaleOut(scaleOutVO);
			
		} else {
			// update
			out_no = scaleOutResult.getOut_no();
			scaleOutVO.setOut_no(out_no);
			scaleDao.putScaleOut(scaleOutVO);
		}
		return 1;
	}

	@Transactional
	public int postRealtimeScale(RealTimeScaleVO realTimeScaleVO) {
		// 1. nfv_instance_info에 저장되어 있는 정보가 있는지 select
		LinkedHashMap<String, String> instanceResult= scaleDao.getInstaceInfoId(realTimeScaleVO.getInstance_id());
		if(instanceResult != null && instanceResult.size() > 0) {
			realTimeScaleVO.setInstance_no(instanceResult.get("instance_no"));
			realTimeScaleVO.setInstance_name(instanceResult.get("instance_name"));
		}
		//table_seq
		String history_no  = nfvCommonDAO.getSeq("A6");
		realTimeScaleVO.setHistory_no(history_no);
		realTimeScaleVO.setScale_info("N/A");
		
		// 2. nfv_instance_scale_history insert
		scaleDao.postScaleHistory(realTimeScaleVO);
		
		return 1;
	}

	
}
