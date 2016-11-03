package edu.illinois.web;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import edu.illinois.logic.DatabaseViewerView;
import edu.illinois.util.DatabaseTable;

/**
 * Created by John Seebauer (seebaue2) on 10/16/16.
 */
public class WebDatabaseViewerView  extends AbstractWebView implements DatabaseViewerView {
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
				SQLContainer container = actionListener.requestQuery(queryBar.getValue());
				if(container != null ) {
					container.setAutoCommit(true);
					databaseGrid.removeAllColumns();
					databaseGrid.setContainerDataSource(container);
				}
			}
			
		});
		top.setSpacing(true);
		top.addComponent(queryBar);
		top.addComponent(execute);
		top.setWidth("100%");
		top.setExpandRatio(queryBar, 1.0f);
		
		databaseGrid = new Grid();
		databaseGrid.setContainerDataSource(actionListener.getConstraintBasedContainer("User"));
		databaseGrid.setSizeFull();
		
		/*DatabaseTable userTable = actionListener.getDatabaseTable("user");
		if(userTable != null) {
			Iterator<Pair<String, Class<?>>> columnIterator = userTable.getColumnIterator();
			while (columnIterator.hasNext()) {
				Pair<String, Class<?>> attribute = columnIterator.next();
				String name = attribute.getOne();
				Class<?> type = attribute.getTwo();
				
				databaseGrid.addColumn(name, type);
			}
			
			
			for(DatabaseEntry row : userTable.getRows()) {
				List<Object> rowBuilder = new ArrayList<>();
				columnIterator = userTable.getColumnIterator();
				while (columnIterator.hasNext()) {
					Pair<String, Class<?>> attribute = columnIterator.next();
					String name = attribute.getOne();
					Class<?> type = attribute.getTwo();
					rowBuilder.add(row.getAttribute(name, type));
				}
				
				databaseGrid.addRow(rowBuilder.toArray());
			}
		}*/
		
		
		
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
	
	public interface ActionListener {
		DatabaseTable getDatabaseTable(String name);
		SQLContainer requestQuery(String query);
		SQLContainer createConnectionPool(String table);
	}
}
