package edu.illinois.backend.databaseViewer;

import edu.illinois.backend.WebCommonModel;
import edu.illinois.logic.DatabaseViewerModel;
import edu.illinois.util.JDBCResult;

import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class WebDatabaseViewerModel extends WebCommonModel implements DatabaseViewerModel {
	private final static Logger logger = Logger.getLogger(WebDatabaseViewerModel.class.getName());
	private ActionListener actionListener;
	
	public WebDatabaseViewerModel() {
		
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
	
	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
}
