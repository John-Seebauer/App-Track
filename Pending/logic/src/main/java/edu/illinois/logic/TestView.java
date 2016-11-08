package edu.illinois.logic;

/**
 * Created by John Seebauer on 11/6/16.
 */
public interface TestView extends CommonView {
	void setActionListener(TestView.ActionListener listener);
	
	interface ActionListener {
		void reloadConfig();
	}
}
