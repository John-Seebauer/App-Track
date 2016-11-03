package edu.illinois.web.util;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class UncaughtExceptionDialog implements Thread.UncaughtExceptionHandler {
	public static UI ui;
	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		Throwable parent = e;
		while (parent.getCause() != null) {
			parent = parent.getCause();
		}
		
		TextArea output = new TextArea();
		StringBuilder outputText = new StringBuilder( e.getLocalizedMessage());
		for (StackTraceElement stackTraceElement : e.getStackTrace()) {
			outputText.append(stackTraceElement.toString()).append("<br>");
		}
		output.setData(outputText.toString());
		output.setIcon(FontAwesome.EXCLAMATION_TRIANGLE);
		
		DialogBuilder builder = new DialogBuilder( UI.getCurrent(), output, DialogType.ERROR)
				.title(parent.getLocalizedMessage())
				.yesText("OK");
		builder.display();
	}
}
