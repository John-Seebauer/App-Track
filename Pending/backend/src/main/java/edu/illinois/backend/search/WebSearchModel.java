package edu.illinois.backend.search;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import edu.illinois.backend.WebCommonModel;
import edu.illinois.logic.SearchModel;
import edu.illinois.util.DatabaseTable;

import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class WebSearchModel extends WebCommonModel implements SearchModel {
	private final static Logger logger = Logger.getLogger(WebSearchModel.class.getName());
	
	public WebSearchModel() {
		
	}
	
	@Override
	public DatabaseTable runQuery(String query) {
		return storageService.runSELECTquery(query);
	}
	
	public SQLContainer requestQuery(String query) throws SQLException {
		return storageService.requestQuery(query);
	}
	
	@Override
	public SQLContainer getConstraintBasedContainer(String table) throws SQLException {
		return storageService.getConstraintBasedContainer(table);
	}
}
