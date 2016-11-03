package edu.illinois.backend;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.DatabaseRequestFormat;
import edu.illinois.util.DatabaseTable;
import edu.illinois.util.Pair;

import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class StorageService {
	private static StorageService service;
	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String path = "jdbc:mysql://localhost:3306";
	private final static String superuser = "SUPER_USER";
	private final static String defaultUser = "SUPER_USER";
	private final static String defaultPassword = "PASSWORD";
	private final static String defaultDatabase = "MOVIE_MATCHER";
	private JDBCConnectionPool pool;
	private Map<String, TableQuery> tableQueryDelegates;
	
	public static StorageService getInstance() throws SQLException {
		if(service == null) {
			service = new StorageService();
			service.init();
		}
		return service;
	}
	
	private StorageService() {
		tableQueryDelegates = new HashMap<>();
	}
	
	private void init() throws SQLException {
		pool = new SimpleJDBCConnectionPool(driver, String.format("%s/%s", path, defaultDatabase),
				defaultUser, defaultPassword);
		
		Connection arbitraryCommand = DriverManager.getConnection(String.format("%s/%s", path, defaultDatabase),
				defaultUser, defaultPassword);
		DatabaseMetaData databaseMetaData = arbitraryCommand.getMetaData();
		ResultSet tables = databaseMetaData.getTables(null, null, "%", null);
		while (tables.next()) {
			try {
				String tableName = tables.getString(3);
				TableQuery tableQuery = new TableQuery(tableName, pool);
				tableQuery.setVersionColumn("Version");
				tableQueryDelegates.put(tableName, tableQuery);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}
		arbitraryCommand.close();
	}
	
	
	public DatabaseTable runSELECTquery(String query) {
		return queryHelper(query, defaultUser, defaultPassword, defaultDatabase);
	}
	
	public void runUPDATEquery(String query, boolean large) {
		DatabaseTable decodedQuery = null;
		Connection connect = null;
		Statement statement = null;
		
		try {
			connect = DriverManager.getConnection(path+"/" + defaultDatabase, defaultUser, defaultPassword);
			
			statement = connect.createStatement();
			
			if(large) {
				statement.executeLargeUpdate(query);
			} else {
				statement.executeUpdate(query);
			}
			
		} catch (SQLException e) {
			System.err.print("Had an exception in StorageService");
			e.printStackTrace();
		} finally {
			if (statement != null) try {
				statement.close();
			} catch (SQLException e) {
				System.err.println("Failed to close SQL statement.");
				e.printStackTrace();
			}
			if (connect != null) try {
				connect.close();
			} catch (SQLException e) {
				System.err.println("Failed to close SQL connection.");
				e.printStackTrace();
			}
		}
	}
	
	private static DatabaseTable queryHelper(String query, String user, String password,
	                                         String database ) {
		DatabaseTable decodedQuery = null;
		Connection connect = null;
		Statement statement = null;
		
		try {
			connect = DriverManager.getConnection(path+"/" + database, user, password);
			
			statement = connect.createStatement();
			
			ResultSet results = statement.executeQuery(query);
			ResultSetMetaData metaData = results.getMetaData();
			
			DatabaseRequestFormat format = new DatabaseRequestFormat(database);
			int columnCount = metaData.getColumnCount();
			for(int i = 1; i <= columnCount; i++) {
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
					default:
						throw new UnsupportedOperationException("Unknown type:\t" + metaData.getColumnType(i));
				}
				
			}
			
			decodedQuery = new DatabaseTable(database, format);
			
			while (results.next()) {
				DatabaseEntry dbEntry = new DatabaseEntry(database);
				for(Pair<String, Class<?>> attribute : format.getAttributeList()) {
					String name = attribute.getOne();
					Class<?> type = attribute.getTwo();
					if(type.equals(String.class)) {
						dbEntry.addAttribute(name, type, results.getString(name) );
					} else if(type.equals(Integer.class)) {
						dbEntry.addAttribute(name, type, results.getInt(name) );
					} else if(type.equals(Instant.class)) {
						dbEntry.addAttribute(name, type, results.getDate(name) );
					} else if(type.equals(Float.class)) {
						dbEntry.addAttribute(name, type, results.getFloat(name) );
					} else {
						throw new UnsupportedOperationException("Unknown type:\t" + type);
					}
				}
				decodedQuery.addRow(dbEntry);
			}
		} catch (SQLException e) {
			System.err.print("Had an exception in StorageService");
			e.printStackTrace();
		} finally {
			if (statement != null) try {
				statement.close();
			} catch (SQLException e) {
				System.err.println("Failed to close SQL statement.");
				e.printStackTrace();
			}
			if (connect != null) try {
				connect.close();
			} catch (SQLException e) {
				System.err.println("Failed to close SQL connection.");
				e.printStackTrace();
			}
		}
		return decodedQuery;
	}
	
	
	public SQLContainer requestQuery(String query) throws SQLException {
		FreeformQuery constructedQuery = new FreeformQuery(query, pool);
		return new SQLContainer(constructedQuery);
	}
	
	public SQLContainer getConstraintBasedContainer(String tableName) throws SQLException {
		return new SQLContainer(tableQueryDelegates.get(tableName));
	}
}
