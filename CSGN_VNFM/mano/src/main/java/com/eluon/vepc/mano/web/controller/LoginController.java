package com.eluon.vepc.mano.web.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.vo.AccountUserDetailsVO;

/**
 * Login Controller (LoginController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: LoginController.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
@Controller
@RequestMapping("/login")
public class LoginController{
	protected final Log logger = LogFactory.getLog(getClass());
	protected final Log loggerTra = LogFactory.getLog("MANO_PROCESS_TRA");
	protected final Log loggerSin = LogFactory.getLog("MANO_PROCESS_SIN");
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public String login(HttpSession session) {
        loggerTra.info("Welcome login! {}" + session.getId());
        return "login/login";
    }
     
    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        AccountUserDetailsVO accountInfo = (AccountUserDetailsVO)session.getAttribute("accountInfo");
         
        loggerTra.info("Welcome logout! {}, {}" + session.getId() + accountInfo.getUsername());
        session.invalidate();
        return "redirect:/login/login.do";
    }
     
    @RequestMapping(value = "/loginSuccess.do", method = RequestMethod.GET)
    public String loginSuccess(HttpSession session) {
    	AccountUserDetailsVO accountInfo = (AccountUserDetailsVO)SecurityContextHolder.getContext().getAuthentication().getDetails();
         
        loggerTra.info("Welcome login_success! {}, {}" + session.getId() + accountInfo.getUsername() + "/" + accountInfo.getPassword());
        session.setAttribute("accountInfo", accountInfo);
        return "redirect:/account/accountList.do";
    }
     
    @RequestMapping(value = "/page1.do", method = RequestMethod.GET)
    public void page1() {      
    }
     
    @RequestMapping(value = "/loginDuplicate.do", method = RequestMethod.GET)
    public void login_duplicate() {    
    	loggerTra.info("Welcome login_duplicate!");
    }
}