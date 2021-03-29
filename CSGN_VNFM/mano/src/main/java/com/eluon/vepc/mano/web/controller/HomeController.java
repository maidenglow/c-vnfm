package com.eluon.vepc.mano.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home Controller
 * @version : 1.0
 * @author :  Copyright (c) 2014 by MIRINCOM CORP. All Rights Reserved.
 */
@Controller
@RequestMapping("/")
public class HomeController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * Index Controller
	 * @method home
	 * @return
	 */
	@RequestMapping(value = { "", "index.html" })
	public String home() {
		return "redirect:/static/index.html";
	}
}
