package edu.illinois.backend;

import edu.illinois.logic.LoginModel;
import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.DatabaseTable;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public class WebLoginModel extends WebCommonModel implements LoginModel {
	
	public boolean authenticateUser(String username, String password) {
		DatabaseTable response = service.runSELECTquery(
				String.format(getProperty("backend.GET_PASSWORD_FOR_USER"), username));
		for(DatabaseEntry pword : response.getRows()) {
			if(pword.getAttribute("password", String.class).equals(password)) return true;
		}
		return false;
	}
	
	@Override
	public void addNewUser(String name, String username, String password, String language) {
		service.runUPDATEquery(String.format(getProperty("backend.ADD_NEW_USER"),
				name, username, password, language), false);
	}
}
