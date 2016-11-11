package edu.illinois.util;

import com.vaadin.data.util.IndexedContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/24/16.
 */
public class DatabaseEntryContainer<T extends DatabaseEntry> extends IndexedContainer {
	private final static Logger logger = Logger.getLogger(DatabaseEntryContainer.class.getName());
	private final Map<String, Method> fieldGetters;
	
	public DatabaseEntryContainer() {
		super();
		fieldGetters = new HashMap<>();
	}
	
	public void addColumnWithDefaultValue(Triple<String, Method, ?> column) {
		fieldGetters.put(column.getOne(), column.getTwo());
		addContainerProperty(column.getOne(), column.getThree().getClass(), column.getThree());
	}
	
	public void add(T item) {
		addItem(item);
		try {
			for (Map.Entry<String, Method> entry : fieldGetters.entrySet()) {
				getContainerProperty(item, entry.getKey()).setValue(entry.getValue().invoke(item));
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.log(Level.FINE, "Failed to add item to Database Entry Container.", e);
		}
	}
	
	public void remove(T item) {
		removeItem(item);
	}
	
	
}
