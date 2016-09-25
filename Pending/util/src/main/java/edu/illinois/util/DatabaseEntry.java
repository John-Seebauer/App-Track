package edu.illinois.util;

import java.io.Serializable;
import java.time.Duration;

/**
 * Created by John Seebauer (seebaue2) on 9/24/16.
 */
public class DatabaseEntry implements Serializable {
	final String title;
	final Integer views;
	final Duration length;
	
	public DatabaseEntry(String title, Integer views, Duration length) {
		
		this.title = title;
		this.views = views;
		this.length = length;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Integer getViews() {
		return views;
	}
	
	public Duration getLength() {
		return length;
	}
}
