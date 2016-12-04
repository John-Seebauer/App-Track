package edu.illinois.logic;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public interface SingleRecommendView extends CommonView {

	void setActionListener(ActionListener actionListener);
	interface ActionListener {
		void setupRecommendationEngine();
		void getRecommendation();
	}
}
