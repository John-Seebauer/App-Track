package edu.illinois.logic;


/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public interface CommonModel {
	void init();
	String getProperty(String name);
	void reloadConfig();
	void setUser(String user);
	String getUser();
}
