package edu.illinois.logic;

import com.vaadin.navigator.View;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by John Seebauer on 9/20/16.
 */
public interface CommonView extends View {
	void showMessage(String message);
	void showError(Throwable error);
	void showAndLogError(Logger log, Level level, String message, Throwable error);
}
