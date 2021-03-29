package com.eluon.vepc.mano.web.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.eluon.vepc.mano.service.AccountService;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.vo.AccountGradeVO;
import com.eluon.vepc.mano.vo.AccountVO;
import com.eluon.vepc.mano.vo.LoginVO;
import com.eluon.vepc.mano.web.validator.AccountValidator;

/**
 * Account Controller (AccountController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountController.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
@Controller
@RequestMapping("/account")
public class AccountController{
	protected final Log logger = LogFactory.getLog(getClass());
	protected final Log loggerTra = LogFactory.getLog("MANO_PROCESS_TRA");
	protected final Log loggerSin = LogFactory.getLog("MANO_PROCESS_SIN");
	@Autowired
	private AccountService accountService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private AccountValidator accountValidator;

    @RequestMapping("/page1.do")
    public String page1() {
        return "account/page1";
    }
    
    @RequestMapping("/page2.do")
    public String page2() {
        return "account/page2";
    }
    
    @RequestMapping("/login.do")
    public String login() {
        return "account/login";
    }
    
    @RequestMapping("/logout.do")
    public String logout(HttpSession session) {
        session.setAttribute("accountInfo", null);
        return "redirect:/account/login.do";
    }
    
    @RequestMapping(value="/loginForm.do", method = RequestMethod.POST)
    public ModelAndView loginProcess(LoginVO loginVO, HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/account/login.do");
 
        AccountVO loginUser = accountService.findByUserIdAndPassword(loginVO);
 
        if (loginUser != null) {
            session.setAttribute("accountInfo", loginUser);
            session.removeAttribute("errorMessage");
        } else {
        	session.setAttribute("errorMessage", config.getString("ERROR_MESSAGE.USER_NOT_FOUND"));
        }
        return mav;
    }
    
	@RequestMapping(value = "/accountForm.do", method=RequestMethod.GET)
	public AccountVO editOrder(@RequestParam(value ="userId", required = false) String userId, Model model) {
		model.addAttribute("accountGradeList", (List<AccountGradeVO>)accountService.getAccountGradeList());
		if(userId == null) {
			return new AccountVO();
		} else {
			return accountService.getAccount(userId);
		}
	}

	@RequestMapping(value = "/accountForm.do", method=RequestMethod.POST)
	public String addAccount(@ModelAttribute AccountVO accountVO, BindingResult result, SessionStatus status) {
		accountValidator.validate(accountVO, result);
		if (result.hasErrors()) {
			return "account/accountForm.do";
		}
		if (accountVO.getUpdateFlag() == null ) {
			accountService.addAccount(accountVO);
		} else {
			accountService.updateAccount(accountVO);
		}
		status.setComplete();
		return "redirect:/account/accountList.do";
	}
	
	@RequestMapping(value = "/accountList.do", method = {RequestMethod.GET, RequestMethod.POST})
	public String accountList(
		@RequestParam(value ="currentPage", required = false, defaultValue = "1") Integer currentPage,
		@RequestParam(value ="gradeCodes", required = false) String[] gradeCodes, 
		@RequestParam(value ="userName", required = false, defaultValue = "") String userName, 
		@RequestParam(value ="handphone", required = false, defaultValue = "") String handphone, 
		@RequestParam(value ="registerStartDate", required = false, defaultValue = "") String registerStartDate, 
		@RequestParam(value ="registerEndDate", required = false, defaultValue = "") String registerEndDate, 
		Model model
		) {
		int pageSize = config.getInt("ACCOUNTLIST_DEFAULT_PAGESIZE");
		String strUri = "<a href=\"javascript:pageMove('";
		model.addAttribute("registerStartDate",registerStartDate);
		model.addAttribute("registerEndDate",registerEndDate);
		if (registerStartDate.equals("") || registerStartDate.length() != 8) {
			registerStartDate = config.getString("ACCOUNTLIST_DEFAULT_SEARCH_STARTDATE");
		} else {
			registerStartDate = registerStartDate + "000000";
		}
		if (registerEndDate.equals("") || registerEndDate.length() != 8) {
			registerEndDate = config.getString("ACCOUNTLIST_DEFAULT_SEARCH_ENDDATE");
		} else {
			registerEndDate = registerEndDate + "235959";
		}
			
		loggerTra.debug("gradeCodes="+gradeCodes);
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("gradeCodes", gradeCodes);
		hm.put("userName", userName);
		hm.put("handphone", handphone);
		hm.put("registerStartDate", registerStartDate);
		hm.put("registerEndDate", registerEndDate);
		int totalCount = accountService.getAccountListTotalCount(hm);
		hm.put("currentPage", currentPage);
		hm.put("pageSize", pageSize);
		model.addAttribute("paginationVO", commonService.getPaginationForm(totalCount, currentPage, pageSize, strUri));
		model.addAttribute("accountList",accountService.getAccountList(hm));
		model.addAttribute("gradeCodes",gradeCodes);
		model.addAttribute("userName",userName);
		model.addAttribute("handphone",handphone);
		return "account/accountList";
	}
	

	@RequestMapping(value = "/viewAccount.do", method=RequestMethod.GET)
	public String viewAccount(@RequestParam("userId") String userId, Model model) {
		model.addAttribute(accountService.getAccount(userId));
		return "account/viewAccount";
	}

	@RequestMapping(value = "/deleteAccount.do", method=RequestMethod.GET)
	public String deleteAccount(@RequestParam("userId") String userId) {
		accountService.deleteAccount(userId);
		return "redirect:/account/accountList.do";
	}
	
}