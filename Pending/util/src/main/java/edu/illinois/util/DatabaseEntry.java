package edu.illinois.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by John Seebauer (seebaue2) on 9/24/16.
 */
public class DatabaseEntry implements Serializable {
	private final String database;
	private Map<Pair<String, Class<?>>, Object> attributes;
	
	public DatabaseEntry(String database) {
		this.database = database;
		
		this.attributes = new HashMap();
	}
	
	public <T> T getAttribute(String attributeName, Class<T> type) {
		if(type == null) {
			throw new NullPointerException("Null class type is invalid.");
		}
		Object retrieved = attributes.get(new Pair<>(attributeName, type));
		if(retrieved == null) {
			throw  new NullPointerException("No attribute with that name and type exist.");
		}
		return type.cast(retrieved);
	}
	
	public <T> void addAttribute(String attributeName, Class<T> type, Object value) {
		attributes.put(new Pair<>(attributeName, type), value);
	}
	
	public String getDatabase() {
		return database;
	}
	
}
