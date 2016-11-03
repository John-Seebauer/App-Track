package edu.illinois.web.search;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import edu.illinois.web.AbstractWebView;

import java.util.Random;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class WebSearchView extends AbstractWebView implements  View {
	
	private UI ui;
	private Grid grid;
	private Random random;
	
	public WebSearchView() {
		/* Created via reflection */
	}
	
	
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		ui.access( () -> ui.getPage().setTitle(event.getViewName()));
	}
	
	@Override
	public void init(UI ui) {
		this.ui = ui;
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		setupView();
		random = new Random();
	}
	
	private void setupView() {
		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setSpacing(true);
		
		Button addButton = new Button();
		addButton.setIcon(FontAwesome.PLUS);
		addButton.addClickListener( event -> {
			showMessage("Implementation is changing to actually use a database. Please wait for the next version.");
		});
		topLayout.addComponent(addButton);
		topLayout.setComponentAlignment(addButton, Alignment.TOP_LEFT);
		
		Button removeButton = new Button();
		removeButton.setIcon(FontAwesome.MINUS);
		removeButton.addClickListener( event -> {
			showMessage("Implementation is changing to actually use a database. Please wait for the next version.");
		});
		topLayout.addComponent(removeButton);
		topLayout.setComponentAlignment(removeButton, Alignment.TOP_LEFT);
		
		TextField searchBar = new TextField();
		
		grid = new Grid();
		
		Button searchButton = new Button("Search");
		searchBar.setImmediate(true);
		searchBar.addTextChangeListener( event -> {
			//grid.applyFilter(event.getText());
		});
		
		
		
		topLayout.addComponent(searchBar);
		topLayout.setComponentAlignment(searchBar, Alignment.TOP_RIGHT);
		topLayout.setExpandRatio(searchBar, 1);
		topLayout.addComponent(searchButton);
		topLayout.setComponentAlignment(searchButton, Alignment.TOP_RIGHT);
		topLayout.setWidth("100%");
		addComponent(topLayout);
		addComponent(grid);
		setExpandRatio(grid, 1);
	}
}
