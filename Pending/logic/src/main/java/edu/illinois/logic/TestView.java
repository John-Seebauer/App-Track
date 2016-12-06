package edu.illinois.logic;

import com.vaadin.ui.Image;

/**
 * Created by John Seebauer on 11/6/16.
 */
public interface TestView extends CommonView {
	void setActionListener(TestView.ActionListener listener);
	
	interface ActionListener {
		void reloadConfig();
		
		Image getImage(String movie, Integer year);
	}
}
