package edu.illinois.web;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import edu.illinois.logic.CommonView;
import edu.illinois.web.util.DialogBuilder;
import edu.illinois.web.util.DialogType;

/**
 * Created by John Seebauer (seebaue2) on 9/24/16.
 */
public interface WebAbstractLayout extends CommonView, View {
	@Override
	default void showMessage(String message) {
		DialogBuilder builder = new DialogBuilder( UI.getCurrent(), new Label(message), DialogType.INFO)
				.title("Info")
				.yesText("OK");
		builder.display();
	}
	
	@Override
	default void showError(Throwable error) {
		Throwable parent = error;
		while (parent.getCause() != null) {
			parent = parent.getCause();
		}
		
		TextArea output = new TextArea();
		StringBuilder outputText = new StringBuilder( error.getLocalizedMessage());
		for (StackTraceElement stackTraceElement : error.getStackTrace()) {
			outputText.append(stackTraceElement.toString()).append("<br>");
		}
		output.setData(outputText.toString());
		output.setIcon(FontAwesome.EXCLAMATION_TRIANGLE);
		
		DialogBuilder builder = new DialogBuilder( UI.getCurrent(), output, DialogType.ERROR)
				.title(parent.getLocalizedMessage())
				.yesText("OK");
		builder.display();
	}
	
	void init(UI ui);
}
