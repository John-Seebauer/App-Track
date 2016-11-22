package edu.illinois.logic;


import edu.illinois.util.JDBCResult;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public interface CommonModel {
	void init();
	String getProperty(String name);
	void reloadConfig();
	
}
