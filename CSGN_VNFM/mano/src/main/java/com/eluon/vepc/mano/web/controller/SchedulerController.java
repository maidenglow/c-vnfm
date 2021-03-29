package com.eluon.vepc.mano.web.controller;

import org.apache.commons.configuration.CompositeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.SchedulerService;

/**
 * Scheduler Controller (SchedulerController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: SchedulerController.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
@Controller
public class SchedulerController{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	protected final static Logger loggerSin = LoggerFactory.getLogger("MANO_PROCESS_SIN");
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private SchedulerService schedulerService;

	/**
	 * 스케줄에 따라서 일정기간에 시작한다.
	 */
	//@Scheduled(cron="1/12 * * * * * ")
	 public void doSchedule() {
	    logger.info("Mano Schedule Start!");
	    logger.info("Mano Schedule End!");
	 }
    
	
}