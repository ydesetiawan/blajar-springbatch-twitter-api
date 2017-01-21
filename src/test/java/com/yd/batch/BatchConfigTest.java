package com.yd.batch;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.yd.apps.Application;
import com.yd.config.H2DataSourceConfig;
/**
 * @author edys
 * @version 1.0, Jan 21, 2017
 * @since
 */
@Configuration
@Import({ Application.class, BatchConfig.class, H2DataSourceConfig.class })
public class BatchConfigTest {

	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils() {
		return new JobLauncherTestUtils();
	}
}
