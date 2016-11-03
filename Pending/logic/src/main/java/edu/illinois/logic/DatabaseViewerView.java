package edu.illinois.logic;

import com.vaadin.data.util.sqlcontainer.SQLContainer;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public interface DatabaseViewerView extends CommonView {
	
	void setActionListener(DatabaseViewerView.ActionListener actionListener);
	
	interface ActionListener {
		SQLContainer requestQuery(String query);
		SQLContainer getConstraintBasedContainer(String table);
	}
}
