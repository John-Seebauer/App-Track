package edu.illinois.logic;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public interface LoginModel extends CommonModel {
	
	boolean authenticateUser(String username, String password);
}
