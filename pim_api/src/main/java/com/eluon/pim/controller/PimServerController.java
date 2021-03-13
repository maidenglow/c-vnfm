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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.pim.service.PimServerService;
import com.eluon.pim.value.ServerStatVO;
import com.eluon.pim.value.ServerVO;


@RestController
@RequestMapping("/pim/v1/servers")
public class PimServerController{
	
	@Autowired
	PimServerService service;
	
	private static final Logger logger = LoggerFactory.getLogger(PimServerController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public Object getServers() throws Exception{
		
		List<ServerVO>  result = service.serverList();
		logger.debug(result.toString());
		return result;
	}
	
	@RequestMapping(value="/{serverNo}",method=RequestMethod.GET)
	public Object getServer(@PathVariable String serverNo) throws Exception{
		
		ServerVO result  = service.server(Integer.parseInt(serverNo));
		
		return result;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Object createServer(HttpServletRequest request, @RequestBody ServerVO param, BindingResult bindingResult) throws Exception{
		
		logger.debug("input Data", param);
		
		//validation 		
		if(bindingResult.hasErrors()){
			logger.error("input Data error");
			List<ObjectError> errors = bindingResult.getAllErrors();
			for(ObjectError err : errors)
				logger.error(err.getDefaultMessage());
			return errors;
		}
		
		if(0 < service.createServer(param)){
			return "server create ok";
		}		
		return "server insert error";
	}
	
	@RequestMapping(value="/{serverNo}",method=RequestMethod.DELETE)
	public Object deleteServer(@PathVariable String serverNo){
		return "서버"+ serverNo;
	}
	
	@RequestMapping(value="/static/{serverNo}",method=RequestMethod.GET)
	public @ResponseBody Object detailServer(@PathVariable String serverNo){
		
		List<ServerStatVO> result = service.serverStatList(Integer.parseInt(serverNo));
		
		System.out.println(result.toString());
		return result;
	}
}
