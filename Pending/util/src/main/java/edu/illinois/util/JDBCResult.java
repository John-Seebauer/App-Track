package edu.illinois.util;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
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
		return new JDBCResult(false, query, null, null);
	}
	
	public static JDBCResult createFailure(JDBCTask query, Throwable ex) {
		return new JDBCResult(true, query, null, ex);
	}
	
	public IndexedContainer formatToContainer() {
		IndexedContainer container = new IndexedContainer();
		
		for (Pair<String, Class<?>> entry : result.getColumns()) {
			container.addContainerProperty(entry.getOne(), entry.getTwo(), null);
		}
		
		for (DatabaseEntry row : result.getRows()) {
			//Looks weird, but it's in the docs
			Object itemID = container.addItem();
			Item item = container.getItem(itemID);
			for (Pair<String, Class<?>> entry : result.getColumns()) {
				item.getItemProperty(entry.getOne()).setValue(row.getAttribute(entry.getOne(), entry.getTwo()));
			}
		}
		
		return container;
	}
}
