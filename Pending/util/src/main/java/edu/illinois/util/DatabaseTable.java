package edu.illinois.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class DatabaseTable {
	private final static Logger logger = Logger.getLogger(DatabaseTable.class.getName());
	private String database;
	private final DatabaseRequestFormat columns;
	private final List<DatabaseEntry> rows;
	
	public DatabaseTable(String database, DatabaseRequestFormat columns) {
		
		this.database = database;
		this.columns = columns;
		rows = new ArrayList<>();
	}
	
	public Collection<Pair<String, Class<?>>> getColumns() {
		return columns.getAttributeList();
	}
	
	public void addRow(DatabaseEntry row) {
		rows.add(row);
	}
	
	public Iterator<Pair<String, Class<?>>> getColumnIterator() {
		return columns.getAttributeList().iterator();
	}
	
	public Iterator<DatabaseEntry> getRowIterator() {
		return rows.iterator();
	}
	
	public Collection<DatabaseEntry> getRows() {
		return rows;
	}
	
	public String getDatabase() {
		return database;
	}
}
