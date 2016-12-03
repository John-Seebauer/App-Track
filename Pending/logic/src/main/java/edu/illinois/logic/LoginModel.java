package edu.illinois.logic;

import edu.illinois.util.JDBCResult;
import edu.illinois.util.Pair;

import java.util.List;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public interface LoginModel extends CommonModel {
	
	void authenticateUser(String username, String password);
	void addNewUser(String name, String username, String password);
	
	void newUserAccepted(JDBCResult result);
	
	void setActionListener(ActionListener actionListener);
	
	interface ActionListener {
		void authenticateUserResponse(List<Pair<String, String>> usernamePasswordMatches, String password);
		void loginUser(String username);
		
		void unableToCreateUser(String username);
	}
}
