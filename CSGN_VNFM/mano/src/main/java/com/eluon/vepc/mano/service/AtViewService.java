package com.eluon.vepc.mano.service;

import java.util.HashMap;

/**
 * AccountService Interface(AccountService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountService.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
public interface AtViewService {
	public String getVldAtViewMeta(HashMap<String, String> hm);
	public String getVmAtViewMeta(HashMap<String, String> hm);
	
	public String getTmenu();
	public String getToption();
	
	public String getMainMenu();
	public String getSubMenu();
	public String getOptionMenu();
	
	//tmenu
	public int postTmenu(HashMap<String, Object> hm);
	public int deleteTmenu(HashMap<String, Object> hm);
		
	//vld
	public String getVld(HashMap<String, Object> hm);
	public int postVld(HashMap<String, Object> hm);
	public int putVld(HashMap<String, Object> hm);
	public int deleteVld(HashMap<String, Object> hm);
	
	//cp
	public String getCp(HashMap<String, Object> hm);
	public int postCp(HashMap<String, Object> hm);
	public int putCp(HashMap<String, Object> hm);
	public int deleteCp(HashMap<String, Object> hm);
	
	//pnfd
	public String getPnfd(HashMap<String, Object> hm);
	public int postPnfd(HashMap<String, Object> hm);
	public int putPnfd(HashMap<String, Object> hm);
	public int deletePnfd(HashMap<String, Object> hm);
	
	//security
	public String getSecurity(HashMap<String, Object> hm);
	public int postAndPutSecurity(HashMap<String, Object> hm);
	public int deleteSecurity(HashMap<String, Object> hm);
	
	//vdu
	public String getVdu(HashMap<String, Object> hm);
	public int postVdu(HashMap<String, Object> hm);
	public int putVdu(HashMap<String, Object> hm);
	public int deleteVdu(HashMap<String, Object> hm);
	
	//nfvi(기존 nfvid)
	public String getNfvi(HashMap<String, Object> hm);
	public int postNfvi(HashMap<String, Object> hm);
	public int putNfvi(HashMap<String, Object> hm);
	public int deleteNfvi(HashMap<String, Object> hm);
	
	//vnfd
	public String getVnfd(HashMap<String, Object> hm);
	public int postVnfd(HashMap<String, Object> hm);
	public int putVnfd(HashMap<String, Object> hm);
	public int deleteVnfd(HashMap<String, Object> hm);
	
	//nsd
	public String getNsd(HashMap<String, Object> hm);
	public int postNsd(HashMap<String, Object> hm);
	public int putNsd(HashMap<String, Object> hm);
	public int deleteNsd(HashMap<String, Object> hm);
}
