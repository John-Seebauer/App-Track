package edu.illinois.backend;

import edu.illinois.logic.LoginModel;
import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.DatabaseTable;

import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public class WebLoginModel extends WebCommonModel implements LoginModel {
	private final static Logger logger = Logger.getLogger(WebLoginModel.class.getName());
	
	public boolean authenticateUser(String username, String password) {
		DatabaseTable response = storageService.runSELECTquery(
				String.format(getProperty("backend.GET_PASSWORD_FOR_USER"), username));
		for(DatabaseEntry pword : response.getRows()) {
			if(pword.getAttribute("password", String.class).equals(password)) return true;
		}
		return false;
	}
	
	@Override
	public void addNewUser(String name, String username, String password, String language) {
		storageService.runUPDATEquery(String.format(getProperty("backend.ADD_NEW_USER"),
				name, username, password, language), false);
	}
}
