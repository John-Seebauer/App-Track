package edu.illinois.logic;

import edu.illinois.util.JDBCResult;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class DatabaseViewerPresenter<V extends DatabaseViewerView, M extends DatabaseViewerModel>
		extends CommonPresenter<V,M> implements DatabaseViewerView.ActionListener, DatabaseViewerModel.ActionListener {
	private final static Logger logger = Logger.getLogger(DatabaseViewerPresenter.class.getName());
	@Override
	public void init(V view, M model) {
		this.view = view;
		this.model = model;
		model.init();
		view.setActionListener(this);
		model.setActionListener(this);
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
