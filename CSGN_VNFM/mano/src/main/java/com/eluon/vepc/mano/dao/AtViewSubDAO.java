package com.eluon.vepc.mano.dao;

/**
 * @author seon
 *
 */
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface AtViewSubDAO {
	
	/* constituent */
	public List<LinkedHashMap<String, String>> getConstituent(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getConstituentOne(HashMap<String, Object> hm);
	
	public int postConstituent(HashMap<String, Object> hm);
	public int postVnfcRef(HashMap<String, Object> hm);
	
	public int putConstituent(HashMap<String, Object> hm);
	
	public int passableDeleteConstituent(HashMap<String, Object> hm);
	public int deleteConstituent(HashMap<String, Object> hm);
	public int deleteVnfcRef(HashMap<String, Object> hm);
	
	/* nfvic */
	public List<LinkedHashMap<String, String>> getNfvic(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getNfvicOne(HashMap<String, Object> hm);
	
	public int postNfvic(HashMap<String, Object> hm);
	public int postMntRef(HashMap<String, Object> hm);
	public int postFuncRef(HashMap<String, Object> hm);
	
	public int deleteNfvic(HashMap<String, Object> hm);
	public int deleteMntRef(HashMap<String, Object> hm);
	public int deleteFuncRef(HashMap<String, Object> hm);
	
	/* cvnf(constituent vnf) */
	public LinkedHashMap<String, String> getCvnf(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getCvnfOne(HashMap<String, Object> hm);
	public int getCvnfCount(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getVersion(HashMap<String, Object> hm);
	
	public int postCvnf(HashMap<String, Object> hm);
	
	public int putCvnf(HashMap<String, Object> hm);
	
	public int passableDeleteCvnf(HashMap<String, Object> hm);
	public int deleteCvnf(HashMap<String, Object> hm);
	
}
