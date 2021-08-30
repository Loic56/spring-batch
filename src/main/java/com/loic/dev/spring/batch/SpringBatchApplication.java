package com.loic.dev.spring.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import com.loic.dev.spring.batch.batchutils.BatchJobListener;
import com.loic.dev.spring.batch.batchutils.BatchStepSkipper;
import com.loic.dev.spring.batch.dto.ConvertedInputData;
import com.loic.dev.spring.batch.dto.InputData;
import com.loic.dev.spring.batch.processor.BatchProcessor;
import com.loic.dev.spring.batch.reader.BatchReader;
import com.loic.dev.spring.batch.writer.BatchWriter;

/**
 * Tuto
 * https://gkemayo.developpez.com/tutoriels/java/tutoriel-sur-mise-oeuvre-spring-batch-avec-spring-boot/
 * 
 * Spring batch - JSR-352 :
 * https://docs.spring.io/spring-batch/docs/current/reference/html/jsr-352.html
 * 
 * Reader Spring :
 * https://docs.spring.io/spring-batch/docs/current-SNAPSHOT/reference/html/appendix.html
 * 
 * @author crussonl
 *
 */
@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

	@Value("${path.to.the.work.dir}")
	private String workDirPath;

	@Autowired
	private DataSource dataSource;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Bean
	public JobRepository jobRepositoryObj() throws Exception {
		JobRepositoryFactoryBean jobRepoFactory = new JobRepositoryFactoryBean();
		jobRepoFactory.setTransactionManager(transactionManager);
		jobRepoFactory.setDataSource(dataSource);
		return jobRepoFactory.getObject();
	}

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Bean
	public BatchReader batchReader() {
		return new BatchReader(workDirPath);
	}

	@Bean
	public BatchProcessor batchProcessor() {
		return new BatchProcessor();
	}

	@Bean
	public BatchWriter batchWriter() {
		return new BatchWriter();
	}

	@Bean
	public BatchJobListener batchJobListener() {
		return new BatchJobListener();
	}

	@Bean
	public BatchStepSkipper batchStepSkipper() {
		return new BatchStepSkipper();
	}

	@Bean
	public Step batchStep() {
		return stepBuilderFactory.get("stepDatawarehouseLoader").transactionManager(transactionManager)
				.<InputData, ConvertedInputData>chunk(1).reader(batchReader()).processor(batchProcessor())
				.writer(batchWriter()).faultTolerant().skipPolicy(batchStepSkipper()).build();
	}

	@Bean
	public Job jobStep() throws Exception {
		return jobBuilderFactory.get("jobDatawarehouseLoader").repository(jobRepositoryObj())
				.incrementer(new RunIdIncrementer()).listener(batchJobListener()).flow(batchStep()).end().build();
	}
}
