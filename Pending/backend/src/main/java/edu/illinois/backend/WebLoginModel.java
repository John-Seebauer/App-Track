package edu.illinois.backend;

import edu.illinois.logic.LoginModel;
import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.DatabaseTable;

import java.sql.SQLException;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public class WebLoginModel implements LoginModel {
	
	private StorageService service;
	
	@Override
	public void init() {
		try {
			service = StorageService.getInstance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean authenticateUser(String username, String password) {
		DatabaseTable response = service.runSELECTquery(String.format("SELECT password FROM User WHERE username = '%s';", username));
		for(DatabaseEntry pword : response.getRows()) {
			if(pword.getAttribute("password", String.class).equals(password)) return true;
		}
		return false;
	}
	
	@Override
	public void addNewUser(String name, String username, String password, String language) {
		service.runUPDATEquery(String.format("INSERT INTO User VALUES(\"%s\",\"%s\",\"%s\",\"%s\");",
				name, username, password, language), false);
	}
}
