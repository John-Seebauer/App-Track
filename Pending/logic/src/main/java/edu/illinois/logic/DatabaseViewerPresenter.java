package edu.illinois.logic;

import com.vaadin.data.util.sqlcontainer.SQLContainer;

import java.sql.SQLException;
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
	public SQLContainer requestQuery(String query) {
		try {
			return model.requestQuery(query);
		} catch (SQLException ex) {
			view.showError(ex);
		}
		return null;
	}
	
	@Override
	public SQLContainer getConstraintBasedContainer(String table) {
		try {
			return model.getConstraintBasedContainer(table);
		} catch (SQLException ex) {
			view.showError(ex);
		}
		return null;
	}
	
	
}
