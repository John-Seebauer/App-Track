package edu.illinois.logic;

/**
 * Created by john on 10/22/16.
 */
public interface LoginView extends CommonView {
	
	void setActionListener(ActionListener listener);
	
	interface ActionListener {
		boolean authenticate(String username, String password);
		void addNewUser(String name, String username, String password, String language);
		void reloadConfig();
	}
	
	
}
