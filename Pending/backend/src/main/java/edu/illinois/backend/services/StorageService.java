package edu.illinois.backend.services;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import edu.illinois.util.JDBCResult;
import edu.illinois.util.JDBCTask;

import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class StorageService {
	private final static Logger logger = Logger.getLogger(StorageService.class.getName());
	private static StorageService service;
	private EventBus bus;
	
	
	public static StorageService getInstance() {
		if (service == null) {
			service = new StorageService();
			try {
				service.init();
			} catch (SQLException e) {
				logger.log(Level.SEVERE, "Could not initialize StorageService!", e);
			}
		}
		return service;
	}
	
	private StorageService() {
		
	}
	
	private void init() throws SQLException {
		ConfigurationService.getInstance().reloadProperties();
		bus = new EventBus();
		bus.register(this);
	}
	
	public void runSELECTquery(String query, Consumer<JDBCResult> successAction, Consumer<JDBCResult> failureAction) {
		runSELECTquery(query, successAction, failureAction, null);
	}
	
	
	public void runSELECTquery(String query, Consumer<JDBCResult> successAction, Consumer<JDBCResult> failureAction, Object additionalArgs) {
		try {
			JDBCTask task = additionalArgs == null ?
					JDBCTask.createSelectQuery(query) : JDBCTask.createSelectQueryWithAdditionalArgs(query, additionalArgs);
			task.setSuccessAction(successAction);
			task.setFailureAction(failureAction);
			StorageServiceBackgroundThread.getInstance(bus).addTask(task);
		} catch (InterruptedException | SQLException e) {
			logger.log(Level.FINE, "Could not add task to queue: " + query, e);
		}
	}
	
	public void runUPDATEquery(String query, boolean large, Consumer<JDBCResult> successAction, Consumer<JDBCResult> failureAction) {
		runUPDATEquery(query, large, successAction, failureAction, null);
	}
	
	public void runUPDATEquery(String query, boolean large, Consumer<JDBCResult> successAction, Consumer<JDBCResult> failureAction, Object additionalArgs) {
		try {
			JDBCTask task = JDBCTask.createUpdateQuery(query, large, additionalArgs);
			task.setSuccessAction(successAction);
			task.setFailureAction(failureAction);
			StorageServiceBackgroundThread.getInstance(bus).addTask(task);
		} catch (InterruptedException | SQLException e) {
			logger.log(Level.FINE, "Could not add task to queue: " + query, e);
		}
	}
	
	@Subscribe
	public void eventHandler(final JDBCResult result) {
		logger.info("Received result for query " + result.getOriginalQuery().getQuery());
		if (result.hadFailure()) {
			Optional<Consumer<JDBCResult>> possibleFailureAction = result.getOriginalQuery().getFailureAction();
			possibleFailureAction.ifPresent(jdbcResultConsumer -> jdbcResultConsumer.accept(result));
		} else {
			Optional<Consumer<JDBCResult>> possibleSuccessAction = result.getOriginalQuery().getSuccessAction();
			possibleSuccessAction.ifPresent(jdbcResultConsumer -> jdbcResultConsumer.accept(result));
			
		}
	}
}
