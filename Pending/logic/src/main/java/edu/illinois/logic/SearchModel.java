package edu.illinois.logic;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import edu.illinois.util.DatabaseTable;

import java.sql.SQLException;

/**
 * Created by john on 9/20/16.
 */
public interface SearchModel extends CommonModel {
	
	DatabaseTable runQuery(String query);
	
	SQLContainer requestQuery(String query) throws SQLException;
	
	SQLContainer getConstraintBasedContainer(String table) throws SQLException;
}
