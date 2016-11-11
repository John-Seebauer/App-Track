package edu.illinois.web.util;

import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;

import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class UncaughtExceptionDialog extends DefaultErrorHandler {
	private final static Logger logger = Logger.getLogger(UncaughtExceptionDialog.class.getName());
	public static UI ui;
	
	
	public void error(ErrorEvent event) {
		formatException(event.getThrowable()).display();
		doDefault(event);
	}
	
	
	private DialogBuilder formatException(Throwable error) {
		Throwable parent = error;
		while (parent.getCause() != null) {
			parent = parent.getCause();
		}
		
		TextArea output = new TextArea();
		output.setSizeFull();
		output.setWordwrap(false);
		
		StringBuilder outputText = new StringBuilder( error.getLocalizedMessage()).append('\n');
		for (StackTraceElement stackTraceElement : error.getStackTrace()) {
			outputText.append(stackTraceElement.toString()).append("\n");
		}
		output.setValue(outputText.toString());
		output.setIcon(FontAwesome.EXCLAMATION_TRIANGLE);
		output.setEnabled(false);
		DialogBuilder builder = new DialogBuilder( UI.getCurrent(), output, DialogType.ERROR)
				.title(parent.getLocalizedMessage())
				.yesText("OK");
		return builder;
	}
}
