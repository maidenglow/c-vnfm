package com.eluon.vepc.mano.service;

import java.util.HashMap;

/**
 * AccountService Interface(AccountService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountService.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */

public interface ParserService {
	
	public String getVldOne(HashMap<String, String> hm);
	public String getVldAll();
	
	public String getPnfdOne(HashMap<String, String> hm);
	public String getPnfdAll();
	
	public String getCpOne(HashMap<String, String> hm);
	public String getCpAll();
	
	public String getNfpOne(HashMap<String, String> hm);
	public String getNfpAll();
	
	public String getVnffgdOne(HashMap<String, String> hm);
	public String getVnffgdAll();
	
	public String getVnfcOne(HashMap<String, String> hm);
	public String getVnfcAll();
	
	public String getVduOne(HashMap<String, String> hm);
	public String getVduAll();
	
	public String getDfOne(HashMap<String, String> hm);
	public String getDfAll();
	
	public String getVnfdOne(HashMap<String, String> hm);
	public String getVnfdAll();
	
	public String getSdfOne(HashMap<String, String> hm);
	public String getSdfAll();
	
	public String getNsdOne(HashMap<String, String> hm);
	public String getNsdAll();
	
	public String getVnfrtdOne(HashMap<String, String> hm);
	public String getVnfrtdAll();
	
	// nfvi(기존 nfvid)
	public String getNfvidOne(HashMap<String, String> hm);
	public String getNfvidAll();
	
	// nfvic(기존 nfvidc)
	public String getNfvicOne(HashMap<String, String> hm);
	public String getNfvicAll();
	
	// constituent
	public String getConstituentOne(HashMap<String, String> hm);
	public String getConstituentAll();
	
	//cvnf(constituent_vnf)
	public String getCvnfOne(HashMap<String, String> hm);
	public String getCvnfAll();
	
}
