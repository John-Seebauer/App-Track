package edu.illinois.web;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;

import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class WebErrorView extends AbstractWebView implements View {
	private final static Logger logger = Logger.getLogger(WebErrorView.class.getName());
	
	public static final String VIEW_NAME = "Error";
	
	final Label description;
	
	public WebErrorView() {
		setMargin(true);
		setSpacing(true);
		
		Label title = new Label("Page not found");
		description = new Label();
		addComponent(title);
		addComponent(description);
	}
	
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		description.setValue(String.format( "Sorry, '%s' does not exist.", event.getViewName()));
	}
}
