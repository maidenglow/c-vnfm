package com.eluon.vepc.mano.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * AccountDAO Interface (AccountDAO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountDAO.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express
 *          $
 */
public interface AtViewDAO {
	public List<LinkedHashMap<String, String>> getVldAtViewMeta(HashMap<String, String> hm);
	public List<LinkedHashMap<String, String>> getVmAtViewMeta(HashMap<String, String> hm);
	
	public List<LinkedHashMap<String, String>> getTmenu(HashMap<String, String> hm);
	public List<LinkedHashMap<String, String>> getToption(HashMap<String, String> hm);
	
	//tmenu
	public LinkedHashMap<String, String> getTmenuOne(HashMap<String, Object> hm);
	public int postTmenu(HashMap<String, Object> hm);
	public int deleteTmenu(HashMap<String, Object> hm);
		
	//tmenuRef
	public int postTmenuRef(HashMap<String, Object> hm);
	public int deleteTmenuRef(HashMap<String, Object> hm);
	
	//main content
	
	public int passableDeleteMainContent(HashMap<String, Object> hm);
	
	//vld
	public List<LinkedHashMap<String, String>> getVld(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getVersionOne(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getVldOne(HashMap<String, Object> hm);
	
	public int postVld(HashMap<String, Object> hm);
	public int postVersion(HashMap<String, Object> hm);
	public int postQosRef(HashMap<String, Object> hm);
	public int postCpRef(HashMap<String, Object> hm);
	public int postSecurityRef(HashMap<String, Object> hm);
	
	public int putVld(HashMap<String, Object> hm);
	public int putVersion(HashMap<String, Object> hm);
	
	public int deleteVersion(HashMap<String, Object> hm);
	public int deleteVld(HashMap<String, Object> hm);
	public int deleteQosRef(HashMap<String, Object> hm);
	public int deleteCpRef(HashMap<String, Object> hm);
	public int deleteSecurityRef(HashMap<String, Object> hm);
	
	//cp
	public List<LinkedHashMap<String, String>> getCp(HashMap<String, Object> hm);
	public int postCp(HashMap<String, Object> hm);
	public int putCp(HashMap<String, Object> hm);
	public int passableDeleteCp(HashMap<String, Object> hm);
	public int deleteCp(HashMap<String, Object> hm);
	
	//pnfd
	public List<LinkedHashMap<String, String>> getPnfd(HashMap<String, Object> hm);
	public int postPnfd(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getPnfdOne(HashMap<String, Object> hm);
	public int putPnfd(HashMap<String, Object> hm);
	public int deletePnfd(HashMap<String, Object> hm);
	
	//security
	public List<LinkedHashMap<String, String>> getSecurity(HashMap<String, Object> hm);
	public int postSecurity(HashMap<String, Object> hm);
	public int putSecurity(HashMap<String, Object> hm);
	public int passableDeleteSecurity(HashMap<String, Object> hm);
	public int deleteSecurity(HashMap<String, Object> hm);
	
	//policy
	public List<LinkedHashMap<String, String>> getPolicy(HashMap<String, Object> hm);
	public int postPolicy(HashMap<String, Object> hm);
	public int putPolicy(HashMap<String, Object> hm);
	public int passableDeletePolicy(HashMap<String, Object> hm);
	public int deletePolicy(HashMap<String, Object> hm);
	
	//vdu
	public List<LinkedHashMap<String, String>> getVdu(HashMap<String, Object> hm);
	public int postVdu(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getVduOne(HashMap<String, Object> hm);
	public int putVdu(HashMap<String, Object> hm);
	public int deleteVdu(HashMap<String, Object> hm);
	
	public int postLifecycleRef(HashMap<String, Object> hm);
	public int postVnfcRef(HashMap<String, Object> hm);
	public int postMonitoringRef(HashMap<String, Object> hm);
	
	public int deleteLifecycleRef(HashMap<String, Object> hm);
	public int deleteVnfcRef(HashMap<String, Object> hm);
	public int deleteMonitoringRef(HashMap<String, Object> hm);
	
	//nfvi(기존 nfvid)
	public List<LinkedHashMap<String, String>> getNfvi(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getNfviOne(HashMap<String, Object> hm);
	
	public int postNfvi(HashMap<String, Object> hm);
	public int postMntRef(HashMap<String, Object> hm);
	public int postFuncRef(HashMap<String, Object> hm);
	
	public int deleteNfvi(HashMap<String, Object> hm);
	public int deleteMntRef(HashMap<String, Object> hm);
	public int deleteFuncRef(HashMap<String, Object> hm);
	//신규 추가 ImageTab
	public int postImageTab(HashMap<String, Object> hm);
	public int deleteImageTab(HashMap<String, Object> hm);
	
	//vnfd
	public List<LinkedHashMap<String, String>> getVnfd(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getVnfdOne(HashMap<String, Object> hm);
	
	public int postVnfd(HashMap<String, Object> hm);
	public int postFlavourRef(HashMap<String, Object> hm);
	public int postPolicyRef(HashMap<String, Object> hm);

	public int putVnfd(HashMap<String, Object> hm);
	
	public int deleteVnfd(HashMap<String, Object> hm);
	public int deleteFlavourRef(HashMap<String, Object> hm);
	public int deletePolicyRef(HashMap<String, Object> hm);
	
	//nsd
	public List<LinkedHashMap<String, String>> getNsd(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getNsdOne(HashMap<String, Object> hm);
	
	public int postNsd(HashMap<String, Object> hm);
	public int postVnfdepRef(HashMap<String, Object> hm);
	public int postSdfRef(HashMap<String, Object> hm);
	
	public int deleteNsd(HashMap<String, Object> hm);
	public int deleteVnfdepRef(HashMap<String, Object> hm);
	public int deleteSdfRef(HashMap<String, Object> hm);
	

	
}
