package edu.illinois.logic;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public interface SingleRecommendView extends AbstractRecommendView {

	void setActionListener(ActionListener actionListener);
	
	void populateUI(String[] movies);
	
	interface ActionListener {
		void setupRecommendationEngine();
		void getRecommendation();
	}
}
