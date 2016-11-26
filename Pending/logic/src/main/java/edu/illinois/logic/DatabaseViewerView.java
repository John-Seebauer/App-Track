package edu.illinois.logic;

import com.vaadin.data.util.IndexedContainer;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public interface DatabaseViewerView extends CommonView {
	
	void setActionListener(DatabaseViewerView.ActionListener actionListener);
	
	void notifySELECTresponse(IndexedContainer container);
	
	interface ActionListener {
		void initSearchrequst(String query);
	}
}
