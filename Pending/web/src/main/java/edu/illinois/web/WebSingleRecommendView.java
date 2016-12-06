package edu.illinois.web;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import edu.illinois.logic.SingleRecommendView;

import java.util.Collection;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 11/8/16.
 */
public class WebSingleRecommendView extends AbstractWebView implements SingleRecommendView {
	private final static Logger logger = Logger.getLogger(WebSingleRecommendView.class.getName());
	
	private Grid databaseGrid;
	private VerticalLayout baseContainer;
	
	
	private SingleRecommendView.ActionListener actionListener;
	@Override
	public void init(UI ui) {
		this.ui = ui;
		setSizeFull();
		setupView();
	}
	
	private void setupView() {
		
		baseContainer = new VerticalLayout();
		baseContainer.setSizeFull();
		baseContainer.setSpacing(true);
		baseContainer.setSizeFull();
		baseContainer.setSpacing(true);
		baseContainer.setSizeFull();
		baseContainer.setSpacing(true);
		
		Button getRecsButton = new Button("Recommend something for me!");
		getRecsButton.addClickListener(clickEvent -> {
			actionListener.setupRecommendationEngine();
			
		});
		
		databaseGrid = new Grid();
		databaseGrid.setSizeFull();
		databaseGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		databaseGrid.addColumn("movie name", String.class);
		
		
		
		baseContainer.addComponent(getRecsButton);
		baseContainer.addComponent(databaseGrid);
		baseContainer.setExpandRatio(databaseGrid, 1.0f);
		
		setSpacing(true);
		setMargin(true);
		addComponent(baseContainer);
	}
	
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		ui.access( () -> ui.getPage().setTitle(event.getViewName()));
	}

	@Override
	public void setActionListener(ActionListener actionListener) {
		this.actionListener=actionListener;
	}
	
	
	public void populateUI(String[] movies){
		
		
		
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty("movie", String.class, "");
		for(String movie : movies){
			Object itemID = container.addItem();
			
			Item item = container.getItem(itemID);
			
			item.getItemProperty("movie").setValue(movie);
		}
		
		ui.access( () -> {
			databaseGrid.removeAllColumns();
			databaseGrid.setContainerDataSource(container);
		});
		


	}
	
	
	private void changeContainer(IndexedContainer container) {
		if (container != null) {
			ui.access( () -> {
				databaseGrid.removeAllColumns();
				databaseGrid.setContainerDataSource(container);
			});
		}
	}
	
	private void changeContainerWithHiddenId(IndexedContainer container, Collection<String> hiddenTitles) {
		if (container != null) {
			ui.access( () -> {
				databaseGrid.removeAllColumns();
				databaseGrid.setContainerDataSource(container);
				for (Grid.Column column : databaseGrid.getColumns()) {
					if(hiddenTitles.contains(column.getPropertyId())) {
						column.setHidden(true);
						column.setHidable(false);
					}
				}
			});
		}
	}
}
