package edu.illinois.logic;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import edu.illinois.util.DatabaseTable;

import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class SearchPresenter<V extends SearchView, M extends SearchModel> extends CommonPresenter<V, M>
		implements SearchView.ActionListener {
	private final static Logger logger = Logger.getLogger(SearchPresenter.class.getName());
	@Override
	public void init(V view, M model) {
		this.view = view;
		this.model = model;
		model.init();
		view.setActionListener(this);
	}
	
	@Override
	public DatabaseTable getDatabaseTable(String name) {
		return null;
	}
	
	@Override
	public SQLContainer requestQuery(String query) throws SQLException {
		System.out.format("Requesting query: %s\n", query);
		return model.requestQuery(query);
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
	
	@Override
	public String getProperty(String name) {
		return super.getProperty(name);
	}
}
