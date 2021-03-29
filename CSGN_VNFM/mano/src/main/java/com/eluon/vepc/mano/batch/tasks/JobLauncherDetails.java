package com.eluon.vepc.mano.batch.tasks;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * JobLauncherDetails (JobLauncherDetails)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: JobLauncherDetails.java,v1.0 2015/04/17 00:00:00 SimByungChul Express $
 */

@DisallowConcurrentExecution
//because we store job state between executions
@PersistJobDataAfterExecution
//because we store last fire time between executions
public class JobLauncherDetails extends QuartzJobBean {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");

	static final String JOB_NAME = "jobName";

	private JobLocator jobLocator;

	private JobLauncher jobLauncher;

	public void setJobLocator(JobLocator jobLocator) {
		this.jobLocator = jobLocator;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	@SuppressWarnings("unchecked")
	protected void executeInternal(JobExecutionContext context) {
		Map<String, Object> jobDataMap = context.getMergedJobDataMap();
		String jobName = (String) jobDataMap.get(JOB_NAME);
		JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);
		try {
			jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
		} catch (JobExecutionException e) {
			e.printStackTrace();
		}
	}

	// get params from jobDataAsMap property, spring-batch.xml
	private JobParameters getJobParametersFromJobMap(Map<String, Object> jobDataMap) {
		JobParametersBuilder builder = new JobParametersBuilder();

		for (Entry<String, Object> entry : jobDataMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String && !key.equals(JOB_NAME)) {
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

}
