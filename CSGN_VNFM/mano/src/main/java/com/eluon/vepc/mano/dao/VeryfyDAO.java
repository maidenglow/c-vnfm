package com.eluon.vepc.mano.dao;

import java.util.HashMap;
import java.util.List;

import com.eluon.vepc.mano.vo.AccountVO;
import com.eluon.vepc.mano.vo.VeryfyVO;

/**
 * VeryfyDAO Interface (VeryfyDAO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: VeryfyDAO.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express
 *          $
 */
public interface VeryfyDAO {
	public int getQosUpdate(HashMap<String, Object> hm);
	public int getQosDelete(HashMap<String, Object> hm);
	public int getQosSave(HashMap<String, Object> hm);
	
	/*한서구*/
	public List<VeryfyVO> getVldTree(HashMap<String, Object> hm);
	public List<VeryfyVO> getVld(HashMap<String, Object> hm);
	public int getVldUpdate(HashMap<String, Object> hm);
	public int QosDelete(HashMap<String, Object> hm);
	public int QosInsert(HashMap<String, Object> hm);
	public int ConnectionDelete(HashMap<String, Object> hm);
	public int ConnectionInsert(HashMap<String, Object> hm);
	public int VldSecurityDelete(HashMap<String, Object> hm);
	public int VldSecurityInsert(HashMap<String, Object> hm);
	public List<VeryfyVO> getVeryfyList(HashMap<String, Object> hm);
	public List<VeryfyVO> getQosList(HashMap<String, Object> hm);
	public List<VeryfyVO> getConnectionList(HashMap<String, Object> hm);
	public List<VeryfyVO> getSecurityList(HashMap<String, Object> hm);
	public int getSecurityUpdate(HashMap<String, Object> hm);
	public int getSecurityDelete(HashMap<String, Object> hm);
	public int getSecuritySave(HashMap<String, Object> hm);
	public int getVersionInsert(HashMap<String, Object> hm);
	public int getVldInsert(HashMap<String, Object> hm);
	public int getVldDelete(HashMap<String, Object> hm);
	public int getVersionDelete(HashMap<String, Object> hm);
	
	/*한서구*/
}
