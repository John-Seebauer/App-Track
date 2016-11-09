package edu.illinois.backend;

import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public class ConfigurationService {
	private static ConfigurationService service;
	private WatchService watchService;
	
	public ConfigurationService getInstance() {
		if(service == null) {
			service = new ConfigurationService();
		}
		return service;
	}
	
	private ConfigurationService() {
		init();
	}
	
	public void init() {
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Path configFilePath = FileSystems.getDefault().getPath("src/resources/", "properties.conf");
		try {
			configFilePath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
						try {
							StorageService.getInstance().reloadProperties();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				if(!key.reset()) {
					System.err.println("Configuration Service could not reset key!");
				};
			}
		});
		fileChangeWatcher.start();
	}
	
}
