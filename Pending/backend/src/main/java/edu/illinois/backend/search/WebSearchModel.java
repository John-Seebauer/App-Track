package edu.illinois.backend.search;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import edu.illinois.backend.WebCommonModel;
import edu.illinois.logic.SearchModel;
import edu.illinois.util.DatabaseTable;

import java.sql.SQLException;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class WebSearchModel extends WebCommonModel implements SearchModel {
	
	public WebSearchModel() {
		
	}
	
	@Override
	public String getProperty(String name) {
		return service.getProperty(name);
	}
	
	@Override
	public DatabaseTable runQuery(String query) {
		return service.runSELECTquery(query);
	}
	
	public SQLContainer requestQuery(String query) throws SQLException {
		return service.requestQuery(query);
	}
	
	@Override
	public SQLContainer getConstraintBasedContainer(String table) throws SQLException {
		return service.getConstraintBasedContainer(table);
	}
}
