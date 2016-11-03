package edu.illinois.logic;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import edu.illinois.util.DatabaseTable;

import java.sql.SQLException;

/**
 * Created by John Seebauer on 10/16/16.
 */
public interface DatabaseViewerModel extends CommonModel {
	
	@Override
	void init();
	
	DatabaseTable runQuery(String query);
	SQLContainer requestQuery(String query) throws SQLException;
	SQLContainer getConstraintBasedContainer(String table) throws SQLException;
}
