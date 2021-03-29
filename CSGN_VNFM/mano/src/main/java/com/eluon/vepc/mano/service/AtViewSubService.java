package com.eluon.vepc.mano.service;

import java.util.HashMap;

/**
 * Sub 하위부분 서비스
 * @author seon
 *
 */
public interface AtViewSubService {
	
	// constituent
	public String getConstituent(HashMap<String, Object> hm);
	public int postConstituent(HashMap<String, Object> hm);
	public int putConstituent(HashMap<String, Object> hm);
	public int deleteConstituent(HashMap<String, Object> hm);
	
	// nfvic
	public String getNfvic(HashMap<String, Object> hm);
	public int postNfvic(HashMap<String, Object> hm);
	public int putNfvic(HashMap<String, Object> hm);
	public int deleteNfvic(HashMap<String, Object> hm);
	
	// cvnf(constituent vnf)
	public String getCvnf(HashMap<String, Object> hm);
	public int postCvnf(HashMap<String, Object> hm);
	public int putCvnf(HashMap<String, Object> hm);
	public int deleteCvnf(HashMap<String, Object> hm);
	
}
