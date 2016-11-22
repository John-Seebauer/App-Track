package edu.illinois.util;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by John Seebauer (seebaue2) on 11/14/16.
 */
public class JDBCTask {
	private final TaskType taskType;
	private final String query;
	private final boolean large;
	private Consumer<JDBCResult> successAction;
	private Consumer<JDBCResult> failureAction;
	private Object additionalArgs;
	
	public String getQuery() {
		return query;
	}
	
	public boolean isLarge() {
		return large;
	}
	
	public TaskType getTaskType() {
		return taskType;
	}
	
	public Optional<Consumer<JDBCResult>> getSuccessAction() {
		return Optional.ofNullable(successAction);
	}
	
	public Optional<Consumer<JDBCResult>> getFailureAction() {
		return Optional.ofNullable(failureAction);
	}
	
	public void setSuccessAction(Consumer<JDBCResult> successAction) {
		this.successAction = successAction;
	}
	
	public void setFailureAction(Consumer<JDBCResult> failureAction) {
		this.failureAction = failureAction;
	}
	
	public Optional<Object> getAdditionalArgs() {
		return Optional.ofNullable(additionalArgs);
	}
	
	public enum TaskType {
		SELECT,
		UPDATE
	}
	
	public static JDBCTask createSelectQuery(String query) {
		return new JDBCTask(TaskType.SELECT, query, false, null);
	}
	
	public static JDBCTask createUpdateQuery(String query, boolean large) {
		return new JDBCTask(TaskType.UPDATE, query, large, null);
	}
	
	public static JDBCTask createSelectQueryWithAdditionalArgs(String query, Object supplemental) {
		return new JDBCTask(TaskType.SELECT, query, false, supplemental);
	}
	
	private JDBCTask(TaskType taskType, String query, boolean large, Object additionalArgs) {
		
		this.taskType = taskType;
		this.query = query;
		this.large = large;
		this.additionalArgs = additionalArgs;
	}
}
