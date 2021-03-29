package com.eluon.vepc.mano.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.dao.VeryfyDAO;
import com.eluon.vepc.mano.service.VeryfyService;
import com.eluon.vepc.mano.vo.AccountVO;
import com.eluon.vepc.mano.vo.VeryfyVO;

/**
 * VeryfyServiceImpl (VeryfyServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: VeryfyServiceImpl.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
@Service("veryfyService")
public class VeryfyServiceImpl implements VeryfyService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private VeryfyDAO veryfyDAO;

	public VeryfyServiceImpl() {
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}
	
	@Override
	public void getQosUpdate(HashMap<String, Object> hm) {
		veryfyDAO.getQosUpdate(hm);
	}
	
	@Override
	public void getQosDelete(HashMap<String, Object> hm) {
		veryfyDAO.getQosDelete(hm);
	}
	
	@Override
	public void getQosSave(HashMap<String, Object> hm) {
		veryfyDAO.getSecuritySave(hm);
	}
	
	
	/*한서구*/
	@Override
	public List<VeryfyVO> getVldTree(HashMap<String, Object> hm) {
		return veryfyDAO.getVldTree(hm);
	}
	@Override
	public List<VeryfyVO> getVld(HashMap<String, Object> hm) {
		return veryfyDAO.getVld(hm);
	}
	@Override
	public void getVldUpdate(HashMap<String, Object> hm) {
		veryfyDAO.getVldUpdate(hm);
	}
	@Override
	public void QosDelete(HashMap<String, Object> hm) {
		veryfyDAO.QosDelete(hm);
	}
	@Override
	public void QosInsert(HashMap<String, Object> hm) {
		veryfyDAO.QosInsert(hm);
	}
	@Override
	public void ConnectionDelete(HashMap<String, Object> hm) {
		veryfyDAO.ConnectionDelete(hm);
	}
	@Override
	public void ConnectionInsert(HashMap<String, Object> hm) {
		veryfyDAO.ConnectionInsert(hm);
	}
	@Override
	public void VldSecurityDelete(HashMap<String, Object> hm) {
		veryfyDAO.VldSecurityDelete(hm);
	}
	@Override
	public void VldSecurityInsert(HashMap<String, Object> hm) {
		veryfyDAO.VldSecurityInsert(hm);
	}
	@Override
	public List<VeryfyVO> getVeryfyList(HashMap<String, Object> hm) {
		return veryfyDAO.getVeryfyList(hm);
	}
	@Override
	public List<VeryfyVO> getQosList(HashMap<String, Object> hm) {
		return veryfyDAO.getQosList(hm);
	}
	@Override
	public List<VeryfyVO> getConnectionList(HashMap<String, Object> hm) {
		return veryfyDAO.getConnectionList(hm);
	}
	@Override
	public List<VeryfyVO> getSecurityList(HashMap<String, Object> hm) {
		return veryfyDAO.getSecurityList(hm);
	}
	@Override
	public void getSecurityUpdate(HashMap<String, Object> hm) {
		veryfyDAO.getSecurityUpdate(hm);
	}
	@Override
	public void getSecurityDelete(HashMap<String, Object> hm) {
		veryfyDAO.getSecurityDelete(hm);
	}
	@Override
	public void getSecuritySave(HashMap<String, Object> hm) {
		veryfyDAO.getSecuritySave(hm);
	}
	@Override
	public void getVersionInsert(HashMap<String, Object> hm) {
		veryfyDAO.getVersionInsert(hm);
	}
	@Override
	public void getVldInsert(HashMap<String, Object> hm) {
		veryfyDAO.getVldInsert(hm);
	}
	@Override
	public void getVldDelete(HashMap<String, Object> hm) {
		veryfyDAO.getVldDelete(hm);
	}
	@Override
	public void getVersionDelete(HashMap<String, Object> hm) {
		veryfyDAO.getVersionDelete(hm);
	}
	/*한서구*/

}
