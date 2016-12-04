package edu.illinois.logic;

import edu.illinois.util.JDBCResult;
import edu.illinois.util.Pair;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Erik Beitel on 11/8/16.
 */
public interface GroupRecommendModel extends CommonModel {
	
	void runGetRatingsTable();

	void notifyGetRatingsTable(final JDBCResult ratingsTableResult);

	void setActionListener(ActionListener actionListener);

	interface ActionListener {

		void notifyFailure(final JDBCResult result);

		void getGroupRecomendation(HashMap<String, List<Pair<Integer, Float>>> dataset);
	}
}
