package edu.illinois.backend.search;

import edu.illinois.backend.WebCommonModel;
import edu.illinois.logic.SearchModel;
import edu.illinois.util.JDBCResult;

import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class WebSearchModel extends WebCommonModel implements SearchModel {
	private final static Logger logger = Logger.getLogger(WebSearchModel.class.getName());
	private ActionListener actionListener;
	
	public WebSearchModel() {
		
	}
	
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
	
	@Override
	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
}
