package edu.illinois.logic;

import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class DatabaseViewerPresenter<V extends DatabaseViewerView, M extends DatabaseViewerModel>
		extends CommonPresenter<V,M> implements DatabaseViewerView.ActionListener {
	private final static Logger logger = Logger.getLogger(DatabaseViewerPresenter.class.getName());
	@Override
	public void init(V view, M model) {
		this.view = view;
		this.model = model;
		model.init();
		view.setActionListener(this);
	}
	
	
	@Override
	public void initSearchrequst(String query) {
		model.runSELECTquery(query);
	}
}
