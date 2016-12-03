package edu.illinois.logic;

import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.JDBCResult;

import java.util.Collection;

/**
 * Created by John Seebauer(seebaue2) on 9/20/16.
 */
public interface SearchModel extends CommonModel {
	
	void runSELECTquery(String query);
	
	void saveRating(String query, Object additionalArgs);
	
	void notifySELECTresponse(final JDBCResult result);
	
	void saveRatingSuccess(final JDBCResult result);
	
	void getGenreAndPlotForMovie(String genreQuery, String plotQuery, DatabaseEntry selected);
	
	void setActionListener(ActionListener actionListener);
	
	interface ActionListener {
		void notifySELECTresponse(final JDBCResult result);
		
		void saveRatingSuccess(final JDBCResult result);
		
		void saveRatingFailure(final JDBCResult result);
		
		void notifyGenreAndPlot(Collection<String> genre, Collection<String> plot, DatabaseEntry selected);
	}
}
