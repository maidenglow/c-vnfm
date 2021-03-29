package com.eluon.vepc.mano.service;
import java.util.HashMap;
import java.util.List;

import com.eluon.vepc.mano.vo.AccountVO;
import com.eluon.vepc.mano.vo.VeryfyVO;

/**
 * VeryfyService Interface(VeryfyService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: VeryfyService.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
public interface VeryfyService {
	
	public void getQosUpdate(HashMap<String, Object> hm);
	
	public void getQosDelete(HashMap<String, Object> hm);
	
	public void getQosSave(HashMap<String, Object> hm);
	
	
	/*한서구*/
	public List<VeryfyVO> getVldTree(HashMap<String, Object> hm);
	public List<VeryfyVO> getVld(HashMap<String, Object> hm);
	public void getVldUpdate(HashMap<String, Object> hm);
	public void QosDelete(HashMap<String, Object> hm);
	public void QosInsert(HashMap<String, Object> hm);
	public void ConnectionDelete(HashMap<String, Object> hm);
	public void ConnectionInsert(HashMap<String, Object> hm);
	public void VldSecurityDelete(HashMap<String, Object> hm);
	public void VldSecurityInsert(HashMap<String, Object> hm);
	public List<VeryfyVO> getVeryfyList(HashMap<String, Object> hm);
	public List<VeryfyVO> getQosList(HashMap<String, Object> hm);
	public List<VeryfyVO> getConnectionList(HashMap<String, Object> hm);
	public List<VeryfyVO> getSecurityList(HashMap<String, Object> hm);
	public void getSecurityUpdate(HashMap<String, Object> hm);
	public void getSecurityDelete(HashMap<String, Object> hm);
	public void getSecuritySave(HashMap<String, Object> hm);
	public void getVersionInsert(HashMap<String, Object> hm);
	public void getVldInsert(HashMap<String, Object> hm);
	public void getVldDelete(HashMap<String, Object> hm);
	public void getVersionDelete(HashMap<String, Object> hm);
	/*한서구*/
}
