package com.eluon.vepc.mano.batch.tasks;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * BatchJobDetail (BatchJobDetail)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: BatchJobDetail.java,v1.0 2015/04/17 00:00:00 SimByungChul Express $
 */
@DisallowConcurrentExecution
// because we store job state between executions
@PersistJobDataAfterExecution
// because we store last fire time between executions
public class BatchJobDetail extends QuartzJobBean implements ApplicationContextAware {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	private JobLocator jobLocator;
	private JobLauncher jobLauncher;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		JobParameters jobParameters = getJobParametersFromJobMap(context
				.getMergedJobDataMap());

		String jobName = context.getJobDetail().getKey().getName();

		try {
			jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
		} catch (JobExecutionAlreadyRunningException e) {
		} catch (JobRestartException e) {
		} catch (JobInstanceAlreadyCompleteException e) {
		} catch (JobParametersInvalidException e) {
		} catch (NoSuchJobException e) {
		}

	}

	// get params from jobDataAsMap property, job-quartz.xml
	private JobParameters getJobParametersFromJobMap(
			Map<String, Object> jobDataMap) {

		JobParametersBuilder builder = new JobParametersBuilder();

		for (Entry<String, Object> entry : jobDataMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String) {
				builder.addString(key, (String) value);
			} else if (value instanceof Float || value instanceof Double) {
				builder.addDouble(key, ((Number) value).doubleValue());
			} else if (value instanceof Integer || value instanceof Long) {
				builder.addLong(key, ((Number) value).longValue());
			} else if (value instanceof Date) {
				builder.addDate(key, (Date) value);
			} else {
				// JobDataMap contains values which are not job parameters
				// (ignoring)
			}
		}

		// need unique job parameter to rerun the completed job
		builder.addDate("run date", new Date());

		return builder.toJobParameters();

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		jobLauncher = applicationContext.getBean(JobLauncher.class);
		jobLocator = applicationContext.getBean(JobLocator.class);

	}

}
