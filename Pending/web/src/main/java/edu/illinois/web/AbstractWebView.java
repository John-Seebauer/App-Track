package edu.illinois.web;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import edu.illinois.logic.CommonView;
import edu.illinois.web.util.DialogBuilder;
import edu.illinois.web.util.DialogType;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public abstract class AbstractWebView extends VerticalLayout implements CommonView {
	private final static Logger logger = Logger.getLogger(AbstractWebView.class.getName());
	
	protected UI ui;
	
	public void init(UI ui) {
		this.ui = ui;
	}
	
	public void showError(Throwable error) {
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
		builder.display();
	}
	
	public void showMessage(String message) {
		DialogBuilder builder = new DialogBuilder( UI.getCurrent(), new Label(message), DialogType.INFO)
				.title("Info")
				.yesText("OK");
		builder.display();
	}
	
	public void showAndLogError(Logger log, Level level, String message, Throwable error) {
		log.log(level, message, error);
		showError(error);
	}
	
	public void showNotification(String title, String description) {
		Notification notification = new Notification(title, description, Notification.Type.TRAY_NOTIFICATION);
		
		getUI().access(() -> notification.show(getUI().getPage()));
	}
	
	public void showNotification(String description) {
		Notification notification = new Notification(description, Notification.Type.TRAY_NOTIFICATION);
		getUI().access(() -> notification.show(getUI().getPage()));
	}
}
