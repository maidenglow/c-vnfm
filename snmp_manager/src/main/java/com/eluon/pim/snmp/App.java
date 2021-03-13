package com.eluon.pim.snmp;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.eluon.pim.snmp.job.server.ServerStatDay;
import com.eluon.pim.snmp.job.server.ServerStatHr;
import com.eluon.pim.snmp.job.server.ServerStatMin;
import com.eluon.pim.snmp.job.server.ServerStatSec;
import com.eluon.pim.snmp.job.switches.SwitchStatDay;
import com.eluon.pim.snmp.job.switches.SwitchStatHr;
import com.eluon.pim.snmp.job.switches.SwitchStatMin;
import com.eluon.pim.snmp.job.switches.SwitchStatSec;
import com.eluon.pim.snmp.service.SnmpTrapReceiver;
/**
 * Hello world!
 *
 */
public class App {

	private SchedulerFactory schedFact;
	private Scheduler sched;


	public App(){
		schedFact = new StdSchedulerFactory();
		try {
			sched = schedFact.getScheduler();
			sched.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main( String[] args )throws Exception{

		App app = new App();
		Configuration property = new PropertiesConfiguration("property/schedule.properties");
		app.serverSchedule(property);
		app.switchSchedule(property);
		
		app.trapSchedule(property);

	}


	public void serverSchedule(Configuration prop) throws Exception{

		String serverSecCron = prop.getString("schedule.server.info");
		String serverMinCron = prop.getString("schedule.server.stat.min");
		String statHrCon = prop.getString("schedule.server.stat.hour");
		String statDayCon = prop.getString("schedule.server.stat.day");

		//////////////////////////////////////////////////////////////////
		JobDetail infoJob= new JobDetail("serverSecJob","server1", ServerStatSec.class);
		CronTrigger infoTrigger = new CronTrigger("serverSecTrigger","server1", serverSecCron);
		sched.scheduleJob(infoJob, infoTrigger);


		/////////////////////////////////////////////////////////////////
		JobDetail minStatJob = new JobDetail("serverMinJob","server2", ServerStatMin.class);
		CronTrigger minStatTrigger = new CronTrigger("serverMinTrigger","server2", serverMinCron);
		sched.scheduleJob(minStatJob, minStatTrigger);

		/////////////////////////////////////////////////////////////////
		JobDetail hourStatJob = new JobDetail("serverHourJob","server3", ServerStatHr.class);
		CronTrigger hourStatTrigger = new CronTrigger("serverHourTrigger","server3", statHrCon);
		sched.scheduleJob(hourStatJob, hourStatTrigger);

		/////////////////////////////////////////////////////////////////
		JobDetail dayStatJob = new JobDetail("serverDayJob","server4", ServerStatDay.class);
		CronTrigger dayStatTrigger = new CronTrigger("serverDayTrigger","server4", statDayCon);
		sched.scheduleJob(dayStatJob, dayStatTrigger);
	}

	public void switchSchedule(Configuration cfg) throws Exception{
		String infoCron = cfg.getString("schedule.switch.info");
		String switchSecCron = cfg.getString("schedule.switch.stat.min");
		String switchHourCron = cfg.getString("schedule.switch.stat.hour");
		String switchDayCron = cfg.getString("schedule.switch.stat.day");


		JobDetail infoJob= new JobDetail("infoJob","switch1", SwitchStatSec.class);
		CronTrigger infoTrigger = new CronTrigger("infoTrigger","switch1", infoCron);
		sched.scheduleJob(infoJob, infoTrigger);
		
		JobDetail minStatJob= new JobDetail("switchMinJob","switch2", SwitchStatMin.class);
		CronTrigger minStatTrigger = new CronTrigger("switchMinTrigger","switch2", switchSecCron);
		sched.scheduleJob(minStatJob, minStatTrigger);
		
		JobDetail hourStatJob = new JobDetail("switchHourJob","switch3", SwitchStatHr.class);
		CronTrigger hourStatTrigger = new CronTrigger("switchHourTrigger","switch3", switchHourCron);
		sched.scheduleJob(hourStatJob, hourStatTrigger);

		JobDetail dayStatJob = new JobDetail("switchDayJob","switch4", SwitchStatDay.class);
		CronTrigger dayStatTrigger = new CronTrigger("switchDayTrigger","switch4", switchDayCron);
		sched.scheduleJob(dayStatJob, dayStatTrigger);
	}
	
	public void trapSchedule(Configuration cfg) throws Exception{
		SnmpTrapReceiver SnmpTrapRcv = new SnmpTrapReceiver();
		
		Configuration config = new PropertiesConfiguration("property/snmp.properties");
		SnmpTrapRcv.setOidSysDescr(config.getString("oid.trap.sysdescr"));
			
		SnmpTrapRcv.run();
	}
}
