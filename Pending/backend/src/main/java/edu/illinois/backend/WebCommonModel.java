package edu.illinois.backend;

import edu.illinois.logic.CommonModel;

import java.sql.SQLException;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class WebCommonModel implements CommonModel{
	protected StorageService service;
	
	@Override
	public void init() {
		try {
			service = StorageService.getInstance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getProperty(String name) {
		return service.getProperty(name);
	}
	
	@Override
	public void reloadConfig() {
		service.reloadProperties();
	}
}
