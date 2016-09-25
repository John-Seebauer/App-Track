package edu.illinois.web;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * Created by John Seebauer (seebaue2) on 9/24/16.
 */
public class WebAboutView extends AbstractWebView implements View {
	private UI ui;
	
	WebAboutView() { /* Created via reflection */ }
	
	@Override
	public void init(UI ui) {
		this.ui = ui;
		setupView();
	}
	
	private void setupView() {
		HorizontalLayout baseContainer = new HorizontalLayout();
		baseContainer.setSpacing(true);
		baseContainer.setMargin(true);
		Label info = new Label(
				"This Project was developed by Erik Beitel, Claire Miller, Wyatt Richter, and John Seebauer.");
		
		baseContainer.addComponent(info);
		addComponent(baseContainer);
	}
	
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		ui.access( () -> ui.getPage().setTitle(event.getViewName()));
	}
}
