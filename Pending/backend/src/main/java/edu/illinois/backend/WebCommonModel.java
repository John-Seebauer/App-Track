package edu.illinois.backend;

import edu.illinois.backend.services.ConfigurationService;
import edu.illinois.backend.services.StorageService;
import edu.illinois.logic.CommonModel;

import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public abstract class WebCommonModel implements CommonModel {
	private final static Logger logger = Logger.getLogger(WebCommonModel.class.getName());
	protected StorageService storageService;
	protected ConfigurationService configurationService;
	
	@Override
	public void init() {
		configurationService = ConfigurationService.getInstance();
		storageService = StorageService.getInstance();
	}
	
	@Override
	public String getProperty(String name) {
		return configurationService.getProperty(name);
	}
	
	@Override
	public void reloadConfig() {
		configurationService.reloadProperties();
	}
}
