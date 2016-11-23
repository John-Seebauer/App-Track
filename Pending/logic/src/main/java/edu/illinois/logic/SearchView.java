package edu.illinois.logic;

import com.vaadin.data.util.IndexedContainer;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public interface SearchView extends CommonView {
	
	void setActionListener(SearchView.ActionListener actionListener);
	
	void notifySELECTresponse(IndexedContainer container);
	
	void queryFailedCleanup();
	
	interface ActionListener {
		String getProperty(String name);
		
		void initSearchrequst(String query);
	}
}
