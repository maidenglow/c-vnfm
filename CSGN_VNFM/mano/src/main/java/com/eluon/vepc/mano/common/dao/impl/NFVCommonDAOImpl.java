package com.eluon.vepc.mano.common.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.common.dao.NFVCommonDAO;

/**
 * @author rochas
 * @version 2015/10/13
 */

@Repository("nfvCommonDAO")
public class NFVCommonDAOImpl implements NFVCommonDAO {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SqlSession sqlSession;

	public NFVCommonDAOImpl() {
	}
	
	@Override
	public String getSeq(String table_seq) {
		
		String result_seq;
		
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
		
		result_seq = format.format(now).toString() + table_seq + String.format("%04d", Integer.parseInt(sqlSession.selectOne("NFVCommon.getSeq", "tab"+table_seq).toString()));
		
		return result_seq;
	}
	
	@Override
	public String getTmenuRefNo(String tmenu_Id) {
		
		return sqlSession.selectOne("NFVCommon.getTmenuRefNo", tmenu_Id);
	}
	
}