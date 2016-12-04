package edu.illinois.backend;

import edu.illinois.logic.GroupRecommendModel;
import edu.illinois.util.JDBCResult;

import java.util.logging.Logger;

/**
 * Created by John Seebauer on 11/8/16.
 */
public class WebGroupRecommendModel extends WebCommonModel implements GroupRecommendModel {
	private final static Logger logger = Logger.getLogger(WebGroupRecommendModel.class.getName());
	private ActionListener actionListener;
	
	public void runSELECTquery(String query) {
		storageService.runSELECTquery(query, this::notifySELECTresponse,  actionListener::notifyFailure);
	}
	
	@Override
	public void runUPDATEquery(String query) {
		storageService.runUPDATEquery(query, false, this::notifyUPDATEresponse, actionListener::notifyFailure);
	}
	
	@Override
	public void notifySELECTresponse(final JDBCResult result) {
		actionListener.notifySELECTresponse(result);
	}
	
	@Override
	public void notifyUPDATEresponse(JDBCResult result) {
		actionListener.notifyUPDATEresponse(result);
	}
	
	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

}
