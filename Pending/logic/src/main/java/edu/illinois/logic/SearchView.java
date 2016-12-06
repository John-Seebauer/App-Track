package edu.illinois.logic;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Image;
import edu.illinois.util.DatabaseEntry;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public interface SearchView extends CommonView {
	
	void setActionListener(SearchView.ActionListener actionListener);
	
	void displaySearchResponse(IndexedContainer container);
	
	void queryFailedCleanup();
	
	void displayRatingWindow(String name, String genre, String plot, Integer movie_id, Image image);
	
	interface ActionListener {
		String getProperty(String name);
		
		void initSearchRequest(String query, boolean exactMatch, String og2Val);
		
		void rateMovie(Integer movieID, Double value, String name);
		
		void initRatingWindow(DatabaseEntry selected);
	}
}
