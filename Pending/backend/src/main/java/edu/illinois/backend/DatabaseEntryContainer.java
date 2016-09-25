package edu.illinois.backend;

import com.vaadin.data.util.IndexedContainer;
import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.Triple;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by John Seebauer (seebaue2) on 9/24/16.
 */
public class DatabaseEntryContainer<T extends DatabaseEntry> extends IndexedContainer {
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
			e.printStackTrace();
		}
	}
	
	public void remove(T item) {
		removeItem(item);
	}
	
	
}
