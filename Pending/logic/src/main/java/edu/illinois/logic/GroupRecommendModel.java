package edu.illinois.logic;

import edu.illinois.util.JDBCResult;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public interface GroupRecommendModel extends CommonModel {
	
	void runSELECTquery(String query);
	
	void runUPDATEquery(String query);
	
	void notifySELECTresponse(final JDBCResult result);
	
	void notifyUPDATEresponse(final JDBCResult result);
	
	void setActionListener(ActionListener actionListener);
	
	interface ActionListener {
		void notifySELECTresponse(final JDBCResult result);
		
		void notifyUPDATEresponse(final JDBCResult result);
		
		void notifyFailure(final JDBCResult result);
	}
}
