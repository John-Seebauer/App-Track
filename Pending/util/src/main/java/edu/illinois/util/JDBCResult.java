package edu.illinois.util;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Created by John Seebauer (seebaue2) on 11/16/16.
 */
public class JDBCResult {
	private final boolean failed;
	private final Throwable exception;
	private final JDBCTask query;
	private final DatabaseTable result;
	
	public JDBCResult() {
		//Needed for EventBus to register JDBCResult Objects
		result = null;
		failed = false;
		exception = null;
		query = null;
	}
	
	private JDBCResult(boolean failed, JDBCTask query, DatabaseTable result, Throwable error) {
		this.failed = failed;
		this.query = query;
		this.result = result;
		this.exception = error;
	}
	
	public JDBCTask getOriginalQuery() {
		return query;
	}
	
	public boolean hadFailure() {
		return failed;
	}
	
	public @NotNull Optional<Throwable> getException() {
		return Optional.ofNullable(exception);
	}
	
	public @NotNull Optional<DatabaseTable> getResult() {
		return Optional.ofNullable(result);
	}
	
	public static JDBCResult createSuccess(JDBCTask query, DatabaseTable result) {
		return new JDBCResult(false, query, result, null);
	}
	
	public static JDBCResult createSuccess(JDBCTask query) {
		return new JDBCResult(true, query, null, null);
	}
	
	public static JDBCResult createFailure(JDBCTask query, Throwable ex) {
		return new JDBCResult(true, query, null, ex);
	}
}
