package edu.illinois.web;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import edu.illinois.logic.DatabaseViewerView;

import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class WebDatabaseViewerView  extends AbstractWebView implements DatabaseViewerView {
	private final static Logger logger = Logger.getLogger(WebDatabaseViewerView.class.getName());
	private DatabaseViewerView.ActionListener actionListener;
	private UI ui;
	private Grid databaseGrid;
	
	
	WebDatabaseViewerView() {
		/* Created via reflection */
	}
	
	public void setActionListener(DatabaseViewerView.ActionListener actionListener) {
		this.actionListener = actionListener;
	}
	
	@Override
	public void init(UI ui) {
		this.ui = ui;
		setSizeFull();
		setupView();
	}
	
	private void setupView() {
		VerticalLayout baseContainer = new VerticalLayout();
		baseContainer.setSpacing(true);
		baseContainer.setMargin(true);
		baseContainer.setSizeFull();
		
		HorizontalLayout top = new HorizontalLayout();
		TextField queryBar = new TextField();
		queryBar.setWidth("100%");
		Button execute = new Button("Go");
		execute.addClickListener( event -> {
			if(queryBar.getValue() != null) {
				actionListener.initSearchrequst(queryBar.getValue());
			}
			
		});
		top.setSpacing(true);
		top.addComponent(queryBar);
		top.addComponent(execute);
		top.setWidth("100%");
		top.setExpandRatio(queryBar, 1.0f);
		
		databaseGrid = new Grid();
		databaseGrid.setSizeFull();
		
		
		baseContainer.addComponent(top);
		baseContainer.addComponent(databaseGrid);
		baseContainer.setExpandRatio(databaseGrid, 1.0f);
		addComponent(baseContainer);
		setExpandRatio(baseContainer, 1.0f);
	}
	
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		ui.access( () -> ui.getPage().setTitle(event.getViewName()));
	}
	
	@Override
	public void notifySELECTresponse(IndexedContainer container) {
		changeContainer(container);
	}
	
	
	private void changeContainer(IndexedContainer container) {
		if (container != null) {
			ui.access( () -> {
				databaseGrid.removeAllColumns();
				databaseGrid.setContainerDataSource(container);
			});
		}
	}
	
}
