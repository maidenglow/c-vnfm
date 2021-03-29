package com.eluon.vepc.mano.batch.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * JobFailureListener (JobFailureListener)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: JobFailureListener.java,v1.0 2015/04/17 00:00:00 SimByungChul Express $
 */
public class JobFailureListener implements JobExecutionListener {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (!jobExecution.getAllFailureExceptions().isEmpty()) {
			ExitStatus exitStatus = ExitStatus.FAILED;
			for (Throwable e : jobExecution.getAllFailureExceptions()) {
				exitStatus = exitStatus.addExitDescription(e);
			}
			jobExecution.setExitStatus(exitStatus);
		}
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// do nothing
	}

}
