package edu.illinois.backend.services;

import edu.illinois.backend.LogFormatter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Properties;
import java.util.logging.*;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public class ConfigurationService {
	private final static Logger logger = Logger.getLogger(ConfigurationService.class.getName());
	private final static String CONF_FILE_PATH = "src/resources/properties.conf";
	private static ConfigurationService service;
	private WatchService watchService;
	private Properties properties;
	private static FileHandler logfile = null;
	
	public static ConfigurationService getInstance()  {
		if(service == null) {
			service = new ConfigurationService();
			service.init();
		}
		return service;
	}
	
	private ConfigurationService() {
		
	}
	
	private void init()  {
		reloadProperties();
		initLogger();
		try {
			initWatcher();
		} catch (IOException e) {
			logger.log(Level.FINER, "Could not initialize WatcherService.", e);
		}
	}
	
	private void initLogger() {
		try {
			logfile=new FileHandler(getProperty("backend.logfile"), false);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		Logger defaultLogger = Logger.getLogger("");
		logfile.setFormatter(new LogFormatter());
		defaultLogger.addHandler(logfile);
		defaultLogger.setLevel(Level.CONFIG);
	}
	
	private void initWatcher() throws IOException{
		watchService = FileSystems.getDefault().newWatchService();
		
		Path configFilePath = FileSystems.getDefault().getPath("src/resources/", "properties.conf");
		configFilePath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
		
		
		Thread fileChangeWatcher = new Thread( () -> {
			while (true) {
				WatchKey key;
				try {
					key = watchService.take();
				} catch (InterruptedException e) {
					return;
				}
				
				for(WatchEvent<?> event : key.pollEvents()) {
					if(event.kind() == StandardWatchEventKinds.OVERFLOW) continue;
					
					WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
					Path changedFilePath = pathEvent.context();
					
					Path filename = configFilePath.resolve(changedFilePath);
					if(filename.toString().equals("properties.conf")) {
						reloadProperties();
					}
				}
				if(!key.reset()) {
					logger.log(Level.SEVERE, "Configuration Service could not reset key!");
				};
			}
		});
		fileChangeWatcher.start();
	}
	
	public void reloadProperties() {
		logger.log(Level.INFO, "Reloading configuration file.");
		properties = new Properties();
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(CONF_FILE_PATH);
			properties.load(inputStream);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Unable to open configuration file.", e);
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.log(Level.WARNING, "Unable to close stream to configuration file.", e);
				}
			}
		}
	}
	
	
	public String getProperty(String name) {
		return properties.getProperty(name);
	}
}
