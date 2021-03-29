package com.eluon.vepc.mano.batch.tasks;

import org.apache.commons.configuration.CompositeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.SystemService;

/**
 * UpdateSystemInfoTask (UpdateSystemInfoTask)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: UpdateSystemInfoTask.java,v1.0 2015/04/17 00:00:00 SimByungChul Express $
 */
public class UpdateSystemInfoTask implements Tasklet {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private SystemService systemService;

	public UpdateSystemInfoTask() {
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        ExitStatus exitStatus = stepExecution.getExitStatus();

        try {
        	logger.debug("String Batch Quartz Test UpdateSystemInfoTask");
            stepExecution.setExitStatus(exitStatus.addExitDescription("good job"));
        } catch (RuntimeException e) {
        	logger.error("Exception occurred during UpdateSystemInfoTask.execute", e);
            stepExecution.setExitStatus(exitStatus.addExitDescription(e));
            throw e;
        }

		return RepeatStatus.FINISHED;
	}
}