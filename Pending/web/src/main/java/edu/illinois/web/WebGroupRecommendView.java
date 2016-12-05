package edu.illinois.web;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import edu.illinois.logic.GroupRecommendView;
import edu.illinois.web.util.DialogBuilder;
import edu.illinois.web.util.DialogType;
import edu.illinois.web.util.YesNoCancelResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

/**
 * Created by john on 11/8/16.
 */
public class WebGroupRecommendView extends AbstractWebView implements GroupRecommendView {
	private final static Logger logger = Logger.getLogger(WebGroupRecommendView.class.getName());
	
	private Grid databaseGrid;
	
	private VerticalLayout vl;
	
	private GroupRecommendView.ActionListener actionListener;
	private Collection<String> selectedUsers = new CopyOnWriteArraySet<>();
	
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
		
		HorizontalSplitPanel base = new HorizontalSplitPanel();
		base.setSizeFull();
		
		
		Label usersLabel = new Label("Users in group:");
		ListSelect usersList = new ListSelect();
		usersList.setNullSelectionAllowed(true);
		usersList.setSizeFull();
		usersList.setMultiSelect(true);
		usersList.addValueChangeListener(item -> {
			selectedUsers.clear();
			selectedUsers.addAll((Collection<String>) item.getProperty().getValue());
		});
		
		Button addUser = new Button();
		addUser.setIcon(FontAwesome.PLUS);
		addUser.addClickListener(clicked -> {
			VerticalLayout layout = new VerticalLayout();
			TextField usernameField = new TextField();
			usernameField.focus();
			usernameField.setSizeFull();
			layout.addComponent(usernameField);
			layout.setSizeFull();
			layout.setExpandRatio(usernameField, 1.0f);
			
			new DialogBuilder(ui, layout, DialogType.INFO)
					.title("Enter username:")
					.width(4.0f, Unit.INCH)
					.height(1.5f, Unit.INCH)
					.showCancel()
					.resultConsumer(consumer -> {
						if (YesNoCancelResult.YES.equals(consumer)) {
							usersList.addItem(usernameField.getValue());
						}
					})
					.display();
		});
		
		Button removeUser = new Button();
		removeUser.setIcon(FontAwesome.MINUS);
		removeUser.addClickListener(clicked -> {
			for (String user : Collections.unmodifiableCollection(selectedUsers)) {
				usersList.removeItem(user);
			}
		});
		
		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.addComponent(addUser);
		buttonBar.addComponent(removeUser);
		buttonBar.setSpacing(true);
		
		VerticalLayout top = new VerticalLayout();
		top.setMargin(true);
		top.setSpacing(true);
		top.addComponent(buttonBar);
		top.addComponent(usersLabel);
		MarginInfo marginInfo = new MarginInfo(false, true, false, true);
		top.setMargin(marginInfo);
		
		VerticalLayout leftPanel = new VerticalLayout();
		leftPanel.setSizeFull();
		leftPanel.setSpacing(true);
		leftPanel.addComponent(top);
		leftPanel.addComponent(usersList);
		leftPanel.setExpandRatio(usersList, 1.0f);
		
		base.addComponent(leftPanel);
		base.setFirstComponent(leftPanel);
		base.setSplitPosition(40f);
		
		
		
		vl = new VerticalLayout();
		
		
		Button findMoviesForAllUsers = new Button("Find movies!");
		
		findMoviesForAllUsers.addClickListener(clickEvent -> {
			

			//actionListener.setupRecommendationEngine(Arrays.asList(users));
		
		});
		
		HorizontalLayout firstQuery = new HorizontalLayout();
		firstQuery.setSpacing(true);
		firstQuery.addComponent(findMoviesForAllUsers);
		firstQuery.setExpandRatio(findMoviesForAllUsers, 1.0f);
		
		databaseGrid = new Grid();
		databaseGrid.setSizeFull();
		
		vl.addComponent(firstQuery);
		vl.addComponent(databaseGrid);
		

		
		VerticalLayout baseContainer = new VerticalLayout();
		baseContainer.setSpacing(true);
		baseContainer.setMargin(true);
		baseContainer.setSizeFull();
		
		baseContainer.addComponent(vl);
		
		base.addComponent(baseContainer);
		base.setSecondComponent(baseContainer);
		addComponent(base);
		
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		ui.access( () -> ui.getPage().setTitle(event.getViewName()));
	}
	
	
	
	public void populateUI(String[] movies){
		
		vl.removeComponent(databaseGrid);
		databaseGrid = new Grid();
		databaseGrid.setSizeFull();
		databaseGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		databaseGrid.addColumn("movie name", String.class);
		vl.addComponent(databaseGrid);
		
		for(String movieStr : movies){
			databaseGrid.addRow(movieStr);
		}
		
	}
	
}
