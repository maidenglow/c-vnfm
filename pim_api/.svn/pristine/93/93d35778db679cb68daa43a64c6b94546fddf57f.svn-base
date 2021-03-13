package com.eluon.pim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {
	@RequestMapping(value="test",method=RequestMethod.GET)
	public ModelAndView test(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		
		return mv;
	}
}
