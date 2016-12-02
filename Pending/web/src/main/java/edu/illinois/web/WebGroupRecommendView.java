package edu.illinois.web;

import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import edu.illinois.logic.GroupRecommendView;
import edu.illinois.logic.SearchView;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by john on 11/8/16.
 */
public class WebGroupRecommendView extends AbstractWebView {
	private final static Logger logger = Logger.getLogger(WebGroupRecommendView.class.getName());
	
	private Grid databaseGrid;
	
	private GroupRecommendView.ActionListener actionListener;
	
	public void setActionListener(GroupRecommendView.ActionListener actionListener) {
		this.actionListener = actionListener;
	}
	
	
	@Override
	public void init(UI ui) {
		this.ui = ui;
		setSizeFull();
		setupView();
	}
	
	private void setupView() {
		
		VerticalLayout vl = new VerticalLayout();
		
		Label label = new Label("Enter users separated by commas");
		
		TextField usersInput = new TextField();
		usersInput.setWidth("100%");
		Button findMoviesForAllUsers = new Button("Find movies!");
		
		findMoviesForAllUsers.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent clickEvent) {
				String usersStr = usersInput.getValue();
				String[] users = usersStr.split(",");
				
				//FreeformQuery query = new FreeformQuery("SELECT * FROM User",);
			}
			
		});
		
		HorizontalLayout firstQuery = new HorizontalLayout();
		firstQuery.setSpacing(true);
		firstQuery.addComponent(usersInput);
		firstQuery.addComponent(findMoviesForAllUsers);
		firstQuery.setExpandRatio(findMoviesForAllUsers, 1.0f);
		
		databaseGrid = new Grid();
		databaseGrid.setSizeFull();
		
		vl.addComponent(label);
		vl.addComponent(firstQuery);
		vl.addComponent(databaseGrid);
		

		
		VerticalLayout baseContainer = new VerticalLayout();
		baseContainer.setSpacing(true);
		baseContainer.setMargin(true);
		baseContainer.setSizeFull();
		
		baseContainer.addComponent(vl);
		
		addComponent(baseContainer);
		
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		ui.access( () -> ui.getPage().setTitle(event.getViewName()));
	}
}
