package edu.illinois.logic;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public interface LoginView extends CommonView {
	
	void setActionListener(ActionListener listener);
	
	void loginUser(String username);
	
	interface ActionListener {
		void authenticate(String username, String password);
		void addNewUser(String name, String username, String password, String language);
		
		void reloadConfig();
	}
	
	
}
