package edu.illinois.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class DatabaseRequestFormat {
	private final static Logger logger = Logger.getLogger(DatabaseRequestFormat.class.getName());
	private final String database;
	Collection<Pair<String, Class<?>>> attributeList;
	
	public DatabaseRequestFormat(String database) {
		
		this.database = database;
		attributeList = new ArrayList<>();
	}
	
	public String getDatabase() {
		return database;
	}
	
	public <T> void addAttribute(String attributeName, Class<T> type) {
		attributeList.add(new Pair<>(attributeName, type));
	}
	
	public Collection<Pair<String, Class<?>>> getAttributeList() {
		return attributeList;
	}
}
