package edu.illinois.logic;

import edu.illinois.util.JDBCResult;
import edu.illinois.util.Pair;

import java.util.HashMap;
import java.util.List;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public interface SingleRecommendModel extends CommonModel {
	
	
	void runGetRatingsTable();

	void notifyGetRatingsTable(final JDBCResult ratingsTableResult);
	
	void setActionListener(ActionListener actionListener);
	
	interface ActionListener {

		void notifyFailure(final JDBCResult result);

		void createSingleRecommendationEngine(HashMap<String, List<Pair<Integer, Float>>> dataset);
	}


}
