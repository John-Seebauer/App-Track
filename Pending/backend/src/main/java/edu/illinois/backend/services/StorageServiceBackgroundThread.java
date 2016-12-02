package edu.illinois.backend.services;

import com.google.common.eventbus.EventBus;
import edu.illinois.util.*;

import java.sql.*;
import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 11/14/16.
 */
public class StorageServiceBackgroundThread implements Runnable {
	private final static Logger logger = Logger.getLogger(StorageServiceBackgroundThread.class.getName());
	
	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String path = "jdbc:mysql://localhost:3306";
	private final static String superuser = "SUPER_USER";
	private final static String defaultUser = "USER";
	private final static String defaultPassword = "PASSWORD";
	private static String defaultDatabase = "MOVIE_MATCHER";
	
	
	private boolean askedToTerminate = false;
	private static ArrayBlockingQueue<JDBCTask> todoTasks;
	private Connection connect;
	private EventBus bus;
	private Statement statement;
	
	public static StorageServiceBackgroundThread create(EventBus bus) throws SQLException {
		StorageServiceBackgroundThread background = new StorageServiceBackgroundThread(bus);
		Thread backgroundThread = new Thread(background);
		backgroundThread.setName("StorageServiceBackgroundThread");
		backgroundThread.start();
		logger.info("Called start on Storage Service background thread.");
		return background;
	}
	
	private StorageServiceBackgroundThread(EventBus bus) throws SQLException {
		this.bus = bus;
		String databaseName = ConfigurationService.getInstance().getProperty("Default_Database");
		if(databaseName != null) {
			defaultDatabase = databaseName;
		}
		init();
	}
	
	private void init() throws SQLException {
		logger.info("Init StorageService background thread.");
		todoTasks = new ArrayBlockingQueue<>(50);
		connect = DriverManager.getConnection(path + "/" + defaultDatabase, defaultUser, defaultPassword);
		statement = connect.createStatement();
	}
	
	public void addTask(JDBCTask task) throws InterruptedException {
		logger.info("Adding task " + task.getQuery() + " to queue.");
		todoTasks.add(task);
		logger.info("Queue size is now " + todoTasks.size());
	}
	
	@Override
	public void run() {
		logger.info("Run called on StorageService Background thread.");
		Thread.currentThread().setUncaughtExceptionHandler( (thread, throwable) -> {
			logger.log(Level.SEVERE, String.format(
					"%s had an uncaught exception: %s", thread.getName(), throwable.getLocalizedMessage()),  throwable);
			logger.info("Restarting StorageService background thread.");
			try {
				create(bus);
			} catch (SQLException e) {
				logger.log(Level.SEVERE, "Could not restart StorageService background thread!", e);
			}
		});
		while (!askedToTerminate) {
			try {
				JDBCTask task = todoTasks.take();
				logger.info("Took a task off of queue: " + task.getQuery());
				JDBCResult result;
				if (task.getTaskType().equals(JDBCTask.TaskType.SELECT)) {
					try {
						result = JDBCResult.createSuccess(task, runSELECTquery(task.getQuery(), defaultDatabase));
					} catch (SQLException ex) {
						logger.log(Level.WARNING, "Storage Service's background thread caught an exception with a SELECT query.", ex);
						result = JDBCResult.createFailure(task, ex);
					}
				} else if(task.getTaskType().equals(JDBCTask.TaskType.UPDATE)) {
					try {
						runUPDATEquery(task.getQuery(), task.isLarge());
						result = JDBCResult.createSuccess(task);
					} catch (SQLException ex) {
						logger.log(Level.WARNING, "Storage Service's background thread caught an exception with an UPDATE query.", ex);
						result = JDBCResult.createFailure(task, ex);
					}
				} else {
					result = JDBCResult.createFailure(task,
							new UnsupportedOperationException("Could not determine query type."));
				}
				logger.info("Posting result of task " + task.getQuery());
				bus.post(result);
			} catch (InterruptedException e) {
				logger.log(Level.WARNING, "Storage Service's background was interrupted.", e);
				terminateThread();
			}
		}
	}
	
	protected void terminateThread() {
		logger.log(Level.INFO, "Ending Storage Service background thread.");
		askedToTerminate = true;
		if (statement != null) try {
			statement.close();
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Failed to close statement.", e);
		}
		if (connect != null) try {
			connect.close();
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Failed to close SQL connection.", e);
		}
	}
	
	private DatabaseTable runSELECTquery(String query, String database) throws SQLException {
		ResultSet results = statement.executeQuery(query);
		ResultSetMetaData metaData = results.getMetaData();
		
		DatabaseRequestFormat format = new DatabaseRequestFormat(database);
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = metaData.getColumnName(i);
			switch (metaData.getColumnType(i)) {
				case Types.CHAR:
				case Types.VARCHAR:
					format.addAttribute(columnName, String.class);
					break;
				case Types.INTEGER:
					format.addAttribute(columnName, Integer.class);
					break;
				case Types.DATE:
					format.addAttribute(columnName, Date.class);
					break;
				case Types.FLOAT:
					format.addAttribute(columnName, Float.class);
					break;
				case -1:
					//For TEXT data type
					if(metaData.getColumnTypeName(i).equals("VARCHAR")) {
						format.addAttribute(columnName, String.class);
						break;
					}
						
				default:
					throw new UnsupportedOperationException("Unknown type:\t" + metaData.getColumnType(i));
			}
			
		}
		
		DatabaseTable decodedQuery = new DatabaseTable(database, format);
		
		while (results.next()) {
			DatabaseEntry dbEntry = new DatabaseEntry(database);
			for (Pair<String, Class<?>> attribute : format.getAttributeList()) {
				String name = attribute.getOne();
				Class<?> type = attribute.getTwo();
				if (type.equals(String.class)) {
					dbEntry.addAttribute(name, type, results.getString(name));
				} else if (type.equals(Integer.class)) {
					dbEntry.addAttribute(name, type, results.getInt(name));
				} else if (type.equals(Instant.class)) {
					dbEntry.addAttribute(name, type, results.getDate(name));
				} else if (type.equals(Float.class)) {
					dbEntry.addAttribute(name, type, results.getFloat(name));
				} else {
					throw new UnsupportedOperationException("Unknown type:\t" + type);
				}
			}
			decodedQuery.addRow(dbEntry);
		}
		return decodedQuery;
	}
	
	private void runUPDATEquery(String query, boolean large) throws SQLException {
		if (large) {
			statement.executeLargeUpdate(query);
		} else {
			statement.executeUpdate(query);
		}
	}
}

