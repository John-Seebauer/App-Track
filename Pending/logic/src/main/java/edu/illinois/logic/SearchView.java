package edu.illinois.logic;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import edu.illinois.util.DatabaseTable;

import java.sql.SQLException;

/**
 * Created by john on 9/20/16.
 */
public interface SearchView extends CommonView {
	
	void setActionListener(SearchView.ActionListener actionListener);
	
	interface ActionListener {
		DatabaseTable getDatabaseTable(String name);
		SQLContainer requestQuery(String query) throws SQLException;
		SQLContainer getConstraintBasedContainer(String table);
		String getProperty(String name);
	}
}
