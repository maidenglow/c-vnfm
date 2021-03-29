package com.eluon.vepc.mano.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * AccountService Interface(AccountService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountService.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */

public interface ParserDao {
	
	public List<LinkedHashMap<String, String>> getVldOne(HashMap<String, String> hm);
	public int getVldCount(HashMap<String, String> hm);
	public int getVldCount();
	public List<String> getVldAllId();
	
	public List<LinkedHashMap<String, String>> getPnfdOne(HashMap<String, String> hm);
	public int getPnfdCount(HashMap<String, String> hm);
	public int getPnfdCount();
	public List<String> getPnfdAllId();
	
	public LinkedHashMap<String, String> getCpOne(HashMap<String, String> hm);
	public int getCpCount(HashMap<String, String> hm);
	public int getCpCount();
	public List<String> getCpAllId();
	
	public List<LinkedHashMap<String, String>> getNfpOne(HashMap<String, String> hm);
	public int getNfpCount(HashMap<String, String> hm);
	public int getNfpCount();
	public List<String> getNfpAllId();
	
	public List<LinkedHashMap<String, String>> getVnffgdOne(HashMap<String, String> hm);	
	public int getVnffgdCount(HashMap<String, String> hm);
	public int getVnffgdCount();
	public List<String> getVnffgdAllId();
	
	public List<LinkedHashMap<String, String>> getVnfcList(HashMap<String, String> hm);	
	public int getVnfcCount(HashMap<String, String> hm);
	public int getVnfcCount();
	public List<String> getVnfcAllId();
	
	public List<LinkedHashMap<String, String>> getVduOne(HashMap<String, String> hm);	
	public int getVduCount(HashMap<String, String> hm);
	public int getVduCount();
	public List<String> getVduAllId();
	
	public List<LinkedHashMap<String, String>> getDfOne(HashMap<String, String> hm);	
	public int getDfCount(HashMap<String, String> hm);
	public int getDfCount();
	public List<LinkedHashMap<String, String>> getDfAllId();
	
	public List<LinkedHashMap<String, String>> getVnfdOne(HashMap<String, String> hm);	
	public int getVnfdCount(HashMap<String, String> hm);
	public int getVnfdCount();
	public List<String> getVnfdAllId();
	
	public List<LinkedHashMap<String, String>> getSdfOne(HashMap<String, String> hm);	
	public int getSdfCount(HashMap<String, String> hm);
	public int getSdfCount();
	public List<String> getSdfAllId();
	
	public List<LinkedHashMap<String, String>> getNsdOne(HashMap<String, String> hm);	
	public int getNsdCount(HashMap<String, String> hm);
	public int getNsdCount();
	public List<String> getNsdAllId();
	
	public List<LinkedHashMap<String, String>> getVnfrtdOne(HashMap<String, String> hm);	
	public int getVnfrtdCount(HashMap<String, String> hm);
	public int getVnfrtdCount();
	public List<String> getVnfrtdAllId();
	
	// nfvi(기존 nfvid)
	public List<LinkedHashMap<String, String>> getNfvidOne(HashMap<String, String> hm);	
	public int getNfvidCount(HashMap<String, String> hm);
	public int getNfvidCount();
	public List<String> getNfvidAllId();
	
	// nfvic(기존 nfvidc)
	public List<LinkedHashMap<String, String>> getNfvicOne(HashMap<String, String> hm);	
	public int getNfvicCount(HashMap<String, String> hm);
	public int getNfvicCount();
	public List<String> getNfvicAllId();
	
	//constituent
	public List<LinkedHashMap<String, String>> getConstituenOne(HashMap<String, String> hm);	
	public int getConstituenCount(HashMap<String, String> hm);
	public int getConstituenCount();
	public List<String> getConstituenAllId();
	
	//cvnf
	public List<LinkedHashMap<String, String>> getCvnfOne(HashMap<String, String> hm);	
	public int getCvnfCount(HashMap<String, String> hm);
	public int getCvnfCount();
	public List<LinkedHashMap<String, String>> getCvnfAllId();
	
}
