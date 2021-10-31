package com.batch.SpringBatchExmaple.batch.tasklet;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClearLogTasklet implements Tasklet, StepExecutionListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClearLogTasklet.class);

	/** 指令傳入之刪除月份範圍 */
	@Value("#{jobParameters['clsMonth']}")
	private String clsMonth;

	/** 預設刪除區間 */
	@Value("${defaultMonth}")
	private String defaultMonth;

	/** 查詢指定區間內的 JobExecution Id */
	private static final String SQL_QUERY_JOB_EXECUTION_ID = "select BATCH_JOB_EXECUTION.JOB_EXECUTION_ID from OTRLXFXS01.BATCH_JOB_EXECUTION"
			+ " where BATCH_JOB_EXECUTION.CREATE_TIME < :clearDate";

	/** 依照 JobExecution Id 取得 JobInstance Id*/
	private static final String SQL_QUERY_JOB_INSTANCE_ID = "select BATCH_JOB_INSTANCE.JOB_INSTANCE_ID"
			+ " from OTRLXFXS01.BATCH_JOB_INSTANCE"
			+ " join OTRLXFXS01.BATCH_JOB_EXECUTION on BATCH_JOB_EXECUTION.JOB_INSTANCE_ID = BATCH_JOB_INSTANCE.JOB_INSTANCE_ID"
			+ " where BATCH_JOB_EXECUTION.JOB_EXECUTION_ID in (:jobExecutionIdList)";

	/** 依照 JobExecution Id 取得 StepExecution Id*/
	private static final String SQL_QUERY_STEP_EXECUTION_ID = "select BATCH_STEP_EXECUTION.STEP_EXECUTION_ID"
			+ " from OTRLXFXS01.BATCH_STEP_EXECUTION"
			+ " join OTRLXFXS01.BATCH_JOB_EXECUTION on BATCH_JOB_EXECUTION.JOB_EXECUTION_ID = BATCH_STEP_EXECUTION.JOB_EXECUTION_ID"
			+ " where BATCH_JOB_EXECUTION.JOB_EXECUTION_ID in (:jobExecutionIdList)";

	/** 1. 刪除 STEP_EXECUTION_CONTEXT */
	private static final String SQL_DELETE_BATCH_STEP_EXECUTION_CONTEXT = "delete from OTRLXFXS01.BATCH_STEP_EXECUTION_CONTEXT where STEP_EXECUTION_ID in (:stepExecutionIdList)";

	/** 2. 刪除 JOB_EXECUTION_CONTEXT */
	private static final String SQL_DELETE_BATCH_JOB_EXECUTION_CONTEXT = "delete from OTRLXFXS01.BATCH_JOB_EXECUTION_CONTEXT where JOB_EXECUTION_ID in (:jobExecutionIdList)";

	/** 3. 刪除 STEP_EXECUTION */
	private static final String SQL_DELETE_BATCH_STEP_EXECUTION = "delete from OTRLXFXS01.BATCH_STEP_EXECUTION where STEP_EXECUTION_ID in (:stepExecutionIdList)";

	/** 4. 刪除 JOB_EXECUTION_PARAMS */
	private static final String SQL_DELETE_BATCH_JOB_EXECUTION_PARAMS = "delete from OTRLXFXS01.BATCH_JOB_EXECUTION_PARAMS where JOB_EXECUTION_ID in (:jobExecutionIdList)";

	/** 5. 刪除 JOB_EXECUTION */
	private static final String SQL_DELETE_BATCH_JOB_EXECUTION = "delete from OTRLXFXS01.BATCH_JOB_EXECUTION where CREATE_TIME < :clearDate";

	/** 6. 刪除 JOB_INSTANCE */
	private static final String SQL_DELETE_BATCH_JOB_INSTANCE = "delete from OTRLXFXS01.BATCH_JOB_INSTANCE where JOB_INSTANCE_ID in (:jobInstanceIdList)";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.info("清除資料庫Log開始");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("清除資料庫Log結束");
		return null;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		int totalCount = 0;

		LocalDate clearDate = LocalDate.now().minusMonths(Long.parseLong(defaultMonth));

		if (StringUtils.isNotBlank(clsMonth)) {
			clearDate = LocalDate.now().minusMonths(Long.parseLong(clsMonth));
		}

		LOGGER.info("清除" + clearDate + "之前Spring Batch history log");

		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("clearDate", Timestamp.valueOf("2021-10-09 00:00:00"));
//    paramsMap.put("clearDate", Timestamp.valueOf(clearDate.atStartOfDay()));

		// get JOB_EXECUTION_ID
		List<BigDecimal> jobExecutionIdList = jdbcTemplate.queryForList(SQL_QUERY_JOB_EXECUTION_ID, paramsMap).stream()
				.map(map -> (BigDecimal) map.get("JOB_EXECUTION_ID")).collect(Collectors.toList());
		if (jobExecutionIdList.isEmpty()) {
			LOGGER.info("該日期以前無EXECUTION_ID資料");
			return RepeatStatus.FINISHED;
		}

		// get STEP_EXECUTION_ID
		paramsMap.put("jobExecutionIdList", jobExecutionIdList);
		List<BigDecimal> stepExecutionIdList = jdbcTemplate.queryForList(SQL_QUERY_STEP_EXECUTION_ID, paramsMap)
				.stream().map(map -> (BigDecimal) map.get("STEP_EXECUTION_ID")).collect(Collectors.toList());

		// get JOB_INSTANCE_ID
		List<BigDecimal> jobInstanceIdList = jdbcTemplate.queryForList(SQL_QUERY_JOB_INSTANCE_ID, paramsMap).stream()
				.map(map -> (BigDecimal) map.get("JOB_INSTANCE_ID")).collect(Collectors.toList());

		// 1. clear BATCH_STEP_EXECUTION_CONTEXT
		paramsMap.put("stepExecutionIdList", stepExecutionIdList);
		int rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_STEP_EXECUTION_CONTEXT, paramsMap);
		LOGGER.debug("BATCH_STEP_EXECUTION_CONTEXT: {}", rowCount);
		totalCount += rowCount;

		// 2. clear BATCH_JOB_EXECUTION_CONTEXT
		rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_JOB_EXECUTION_CONTEXT, paramsMap);
		LOGGER.debug("JOB_EXECUTION_CONTEXT: {}", rowCount);
		totalCount += rowCount;

		// 3. clear BATCH_STEP_EXECUTION
		rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_STEP_EXECUTION, paramsMap);
		LOGGER.debug("BATCH_STEP_EXECUTION: {}", rowCount);
		totalCount += rowCount;

		// 4. clear BATCH_JOB_EXECUTION_PARAMS
		rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_JOB_EXECUTION_PARAMS, paramsMap);
		LOGGER.debug("JOB_EXECUTION_PARAMS: {}", rowCount);
		totalCount += rowCount;

		// 5. clear BATCH_JOB_EXECUTION
		rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_JOB_EXECUTION, paramsMap);
		LOGGER.debug("BATCH_JOB_EXECUTION: {}", rowCount);
		totalCount += rowCount;

		// 6. clear BATCH_JOB_INSTANCE
		paramsMap.put("jobInstanceIdList", jobInstanceIdList);
		rowCount = jdbcTemplate.update(SQL_DELETE_BATCH_JOB_INSTANCE, paramsMap);
		LOGGER.debug("BATCH_JOB_INSTANCE: {}", rowCount);
		totalCount += rowCount;

		contribution.incrementWriteCount(totalCount);

		return RepeatStatus.FINISHED;
	}
}
