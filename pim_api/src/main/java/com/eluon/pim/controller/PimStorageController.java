package com.eluon.pim.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.pim.service.PimStorageService;
import com.eluon.pim.value.StatParamVO;
import com.eluon.pim.value.StorageVO;
import com.google.gson.Gson;

@RestController
@RequestMapping("/pim/v1/storages")
public class PimStorageController {
	@Autowired
	PimStorageService storage;
	
	
	private static final Logger logger = LoggerFactory.getLogger(PimStorageController.class);

	@RequestMapping(method = RequestMethod.GET)
	public Object getStorages() throws Exception {
		List<StorageVO> result = storage.storageList();
		return result;
	}

	@RequestMapping(value = "/{storageNo}", method = RequestMethod.GET)
	public Object getStorage(@PathVariable String storageNo) throws Exception {

		StorageVO result = storage.storageOne(Integer.parseInt(storageNo));

		return result;
	}
	@RequestMapping(method=RequestMethod.POST)
	public Object createStorage(HttpServletRequest request, 
								@RequestBody StorageVO param,
								BindingResult bindingResult)
								throws Exception {
		int result;
		// validation
		if (bindingResult.hasErrors()) {
			logger.error("input Data error");
			List<ObjectError> errors = bindingResult.getAllErrors();

			for (ObjectError err : errors)
				logger.error(err.getDefaultMessage());
			return errors;
		}
		result = storage.createStorage(param);
		if( result > 0 )
			return new Gson().toJson(param);
		return "storage insert failed";
	}
	@RequestMapping(value="/{storageNo}",method=RequestMethod.DELETE) 
	public Object deleteStorage(@PathVariable String storageNo) throws Exception{
		Integer [] result = null;
		String resultStr = new String();
		result = storage.deleteStorage(Integer.parseInt(storageNo));
		
		resultStr = String.format(
				  "STORAGE_ALARM_INFO : %d rows deleted\n"
				+ "STORAGE_EVENT_INFO : %d rows deleted\n"
				+ "STORAGE_STAT_INFO : %d rows deleted\n"
				+ "STORAGE_STAT_10S : %d rows deleted\n"
				+ "STORAGE_INFO : %d rows deletedd\n", 
				 result[4], result[3], result[2], result[1], result[0]);
		
		return resultStr;
	}
	
	
	@RequestMapping(value="/statics/{storageNo}",method=RequestMethod.GET) 
	public Object getStroageStat(@PathVariable String storageNo, String Type, String Start_date, String End_date ) throws Exception{
		StatParamVO param = new StatParamVO();
		logger.debug("storageNo={}, Type={}, start={}, end={}", storageNo, Type, Start_date, End_date);
	
		if(param.isValidStatType(Type) == false)
		{
			return "Invald Type";
		}
		param.setStatType(Integer.parseInt(Type));
		param.setStartTime(Start_date);
		param.setEndTime(End_date);
		return storage.StorageStatList(param);
	}
}
