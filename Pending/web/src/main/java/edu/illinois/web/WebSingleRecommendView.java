package edu.illinois.web;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import edu.illinois.logic.SingleRecommendView;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public class WebSingleRecommendView extends AbstractWebView implements SingleRecommendView {
	private final static Logger logger = Logger.getLogger(WebSingleRecommendView.class.getName());

	private SingleRecommendView.ActionListener actionListener;
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
		Button getRecsButton = new Button("Recommend something for me!");
		getRecsButton.addClickListener(clickEvent -> {
			actionListener.setupRecommendationEngine();
		});
		setSpacing(true);
		setMargin(true);
		addComponent(box);
		addComponent(getRecsButton);
	}
	
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		ui.access( () -> ui.getPage().setTitle(event.getViewName()));
	}

	@Override
	public void setActionListener(ActionListener actionListener) {
		this.actionListener=actionListener;
	}
}
