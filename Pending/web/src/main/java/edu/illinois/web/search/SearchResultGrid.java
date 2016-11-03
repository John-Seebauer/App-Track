package edu.illinois.web.search;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Grid;
import edu.illinois.util.DatabaseEntryContainer;
import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.Triple;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by John Seebauer (seebaue2) on 9/24/16.
 */
public class SearchResultGrid extends Grid {
	
	SearchResultGrid(DatabaseEntryContainer<DatabaseEntry> dbContainer) {
		setSizeFull();
		setSelectionMode(SelectionMode.MULTI);
		setContainerDataSource( dbContainer );
		
		try {
			dbContainer.addColumnWithDefaultValue(
					new Triple<>("Name", DatabaseEntry.class.getMethod("getTitle"), ""));
			dbContainer.addColumnWithDefaultValue(
					new Triple<>("Views", DatabaseEntry.class.getMethod("getViews"), new Integer(0)));
			dbContainer.addColumnWithDefaultValue(
					new Triple<>("Length", DatabaseEntry.class.getMethod("getLength"), Duration.ZERO));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
	}
	
	public void applyFilter(String filter) {
		getContainer().removeAllContainerFilters();
		if(filter.length() > 0) {
			List<SimpleStringFilter> filterList = this.getColumns().stream().map(
					attribute -> new SimpleStringFilter(attribute.getPropertyId(), filter, true, false))
					.collect(Collectors.toList());
			Container.Filter[] filters = filterList.toArray(new Container.Filter[filterList.size()]);
			getContainer().addContainerFilter(new Or(filters));
		}
	}
	
	public DatabaseEntry getSelectedRow() {
		return (DatabaseEntry) super.getSelectedRow();
	}
	
	public DatabaseEntryContainer<DatabaseEntry> getContainer() {
		return (DatabaseEntryContainer<DatabaseEntry>) super.getContainerDataSource();
	}
}
