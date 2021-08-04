package com.loic.dev.spring.batch.batchutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BatchStepSkipper implements SkipPolicy {

	Logger log = LoggerFactory.getLogger(BatchStepSkipper.class);

	private static final Integer SKIP_THRESHOLD = 3;

	@Override
	public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
		if (t instanceof FlatFileParseException && skipCount < SKIP_THRESHOLD) {
			log.info(((FlatFileParseException) t).getLineNumber() + ": " + ((FlatFileParseException) t).getInput()
					+ " -- " + ((FlatFileParseException) t).getMessage());
			return true;
		}
		return false;
	}
}
