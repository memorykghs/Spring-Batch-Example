package com.batch.SpringBatchExmaple.batch.skipPolicy;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

import com.batch.SpringBatchExmaple.exception.DataNotFoundException;
import com.batch.SpringBatchExmaple.exception.RangeLimitExcpetion;

/**
 * 自訂 SkipPolicy
 * 
 * @author memorykghs
 */
public class CustomSkipPolicy implements SkipPolicy {

	/** 設定可容忍錯誤的次數 */
	private static final int MAX_SKIP_COUNT = 1;

	@Override
	public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {

		// 可以跳過 RangeLimitExcpetion
		if (t instanceof RangeLimitExcpetion && skipCount < MAX_SKIP_COUNT) {
			return true;
		}

		// DataNotFoundException 不可被跳過
		if (t instanceof DataNotFoundException && skipCount < MAX_SKIP_COUNT) {
			return false;
		}

		return false;
	}

}
