package com.eluon.vepc.mano.service;


/**
 * SchedulerService Interface(SchedulerService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: SchedulerService.java,v 1.0 2015/04/15 00:00:00 SimByungChul Express $
 */
public interface SchedulerService {
	public void updateSystemInfo();
	public void updateInstanceInfo();
	public void deleteSystemInfoAll();
	public void insertSystemInfo();
	public void deleteInstanceInfoAll();
	public void insertInstanceInfo();
}
