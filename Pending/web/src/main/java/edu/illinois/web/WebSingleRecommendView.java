package edu.illinois.web;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import edu.illinois.logic.SingleRecommendView;

import java.util.Arrays;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public class WebSingleRecommendView extends AbstractWebView implements SingleRecommendView {
	
	
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
