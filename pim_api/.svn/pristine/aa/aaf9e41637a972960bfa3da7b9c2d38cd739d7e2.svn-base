package com.eluon.pim.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.pim.repository.SampleRepository;
import com.eluon.pim.value.ServerVO;

@Service
public class SampleService {
	@Autowired
	SampleRepository repository;
	
	
	public String getTimeService() throws Exception{
		return repository.getTimeRepo();
	}
	
	public List<ServerVO> getServerInfoService() throws Exception{
		return repository.getServerRepo();
	}
}	
