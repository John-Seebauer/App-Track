package edu.illinois.web;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;

import java.util.Arrays;

/**
 * Created by john on 11/8/16.
 */
public class WebGroupRecommendView extends AbstractWebView {
	
	@Override
	public void init(UI ui) {
		this.ui = ui;
		setSizeFull();
		setupView();
	}
	
	private void setupView() {
		ComboBox box = new ComboBox("Genre");
		for(String item : Arrays.asList("Comedy", "Drama", "Horror", "Romance")) {
			box.addItem(item);
		}
		box.addValueChangeListener( event -> {
			showMessage("You selected " + box.getValue() + " but recommendations are not yet implemented.");
		});
		setSpacing(true);
		setMargin(true);
		addComponent(box);
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		ui.access( () -> ui.getPage().setTitle(event.getViewName()));
	}
}
