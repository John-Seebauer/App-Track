package edu.illinois.logic;

import edu.illinois.util.JDBCResult;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class SearchPresenter<V extends SearchView, M extends SearchModel> extends CommonPresenter<V, M>
		implements SearchView.ActionListener, SearchModel.ActionListener {
	private final static Logger logger = Logger.getLogger(SearchPresenter.class.getName());
	
	@Override
	public void init(V view, M model) {
		this.view = view;
		this.model = model;
		model.init();
		view.setActionListener(this);
		model.setActionListener(this);
	}
	
	@Override
	public String getProperty(String name) {
		return super.getProperty(name);
	}
	
	@Override
	public void initSearchrequst(String query) {
		model.runSELECTquery(query);
	}
	
	@Override
	public void notifySELECTresponse(JDBCResult result) {
		if (result.getResult().isPresent()) {
			view.notifySELECTresponse(result.formatToContainer());
		}
	}
	
	@Override
	public void notifyUPDATEresponse(JDBCResult result) {
		String original =  result.getOriginalQuery().getQuery();
		String resultString = result.getResult().isPresent() ? String.valueOf(result.hadFailure()) : "unknown";
		
		logger.info(String.format("Received notification of UPDATE query's result: %s for\n\t%s\n\t", resultString, original));
	}
	
	@Override
	public void notifyFailure(JDBCResult result) {
		String query = " for query: " + result.getOriginalQuery().getQuery();
		
		if (!result.hadFailure()) {
			if (result.getException().isPresent()) {
				view.showAndLogError(logger, Level.WARNING, "JDBCResult" + query +
						" failed but was not marked as such.", result.getException().get());
			} else {
				logger.log(Level.WARNING, "JDBCResult" + query + " failed but was not marked as such.");
				view.showMessage("JDBCResult" + query + " failed but was not marked as such.");
			}
		} else {
			if (result.getException().isPresent()) {
				view.showAndLogError(logger, Level.WARNING, "JDBCResult" + query + " failed.", result.getException().get());
			} else {
				logger.log(Level.WARNING, "JDBCResult" + query + " failed.");
				view.showMessage("JDBCResult" + query + " failed.");
			}
		}
	}
}
