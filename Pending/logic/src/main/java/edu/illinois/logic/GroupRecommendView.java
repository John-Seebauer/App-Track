package edu.illinois.logic;

import java.util.List;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public interface GroupRecommendView extends CommonView {
	void setActionListener(ActionListener actionListener);
	interface ActionListener {
		void setupRecommendationEngine(List<String> users);
	}
}
