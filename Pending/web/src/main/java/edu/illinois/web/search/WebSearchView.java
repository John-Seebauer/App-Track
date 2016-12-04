package edu.illinois.web.search;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import edu.illinois.logic.SearchView;
import edu.illinois.util.DatabaseEntry;
import edu.illinois.web.AbstractWebView;
import edu.illinois.web.util.DialogBuilder;
import edu.illinois.web.util.DialogType;
import edu.illinois.web.util.YesNoCancelResult;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class WebSearchView extends AbstractWebView implements SearchView {
	private final static Logger logger = Logger.getLogger(WebSearchView.class.getName());
	private UI ui;
	private Grid databaseGrid;
	private SearchView.ActionListener actionListener;
	private ProgressBar progressBar;
	
	public WebSearchView() {
		/* Created via reflection */
	}
	
	public void setActionListener(SearchView.ActionListener actionListener) {
		this.actionListener = actionListener;
	}
	
	@Override
	public void notifySELECTresponse(IndexedContainer container) {
		changeContainerWithHiddenId(container, Collections.singletonList("movie_id"));
		progressBar.setVisible(false);
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		ui.access(() -> ui.getPage().setTitle(event.getViewName()));
	}
	
	@Override
	public void init(UI ui) {
		this.ui = ui;
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		setupView();
	}
	
	private void setupView() {
		VerticalLayout baseContainer = new VerticalLayout();
		baseContainer.setSpacing(true);
		baseContainer.setMargin(true);
		baseContainer.setSizeFull();
		
		progressBar = new ProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setVisible(false);
		
		HorizontalLayout top = new HorizontalLayout();
		TextField queryBar = new TextField();
		queryBar.setWidth("100%");
		Button search = new Button("Search");
		search.addClickListener(event -> {
			if (queryBar.getValue() != null) {
				actionListener.initSearchrequst(queryBar.getValue());
				progressBar.setVisible(true);
			}
		});
		
		Button rateButton = new Button("Rate selected");
		rateButton.setVisible(false);
		rateButton.addClickListener(listener -> {
			Integer selectedRow = (Integer) databaseGrid.getSelectedRow();
			Item selectedItem = databaseGrid.getContainerDataSource().getItem(selectedRow);
			if (selectedItem != null) {
				DatabaseEntry databaseEntry = DatabaseEntry.generateFromItem(selectedItem, actionListener.getProperty("Default_Database"));
				actionListener.initRatingWindow(databaseEntry);
			}
		});
		
		
		top.setSpacing(true);
		top.addComponent(queryBar);
		top.addComponent(search);
		top.addComponent(progressBar);
		top.addComponent(rateButton);
		top.setWidth("100%");
		top.setExpandRatio(queryBar, 1.0f);
		
		databaseGrid = new Grid();
		databaseGrid.setSizeFull();
		databaseGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		
		databaseGrid.addItemClickListener( listener -> {
			if (!databaseGrid.isSelected(listener.getItemId())) {
				rateButton.setVisible(true);
				if (listener.isDoubleClick()) {
					DatabaseEntry databaseEntry = DatabaseEntry.generateFromItem(listener.getItem(), actionListener.getProperty("Default_Database"));
					actionListener.initRatingWindow(databaseEntry);
				}
			} else {
				rateButton.setVisible(false);
			}
		});
		
		baseContainer.addComponent(top);
		baseContainer.addComponent(databaseGrid);
		baseContainer.setExpandRatio(databaseGrid, 1.0f);
		addComponent(baseContainer);
		setExpandRatio(baseContainer, 1.0f);
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
	
	public void queryFailedCleanup() {
		ui.access( () -> progressBar.setVisible(false));
	}
	
	@Override
	public void displayRatingWindow(String name, String genre, String plot, Integer movie_id) {
		//Rating window
		VerticalLayout ratingWindowLayout = new VerticalLayout();
		ratingWindowLayout.setSizeFull();
		
		Label genreLabel = new Label("Genre: " + genre);
		TextArea plotArea = new TextArea();
		plotArea.setWordwrap(true);
		plotArea.setValue(plot);
		plotArea.setReadOnly(true);
		plotArea.setSizeFull();
		
		Slider rating = new Slider(0, 10, 0);
		rating.setValue(8.0);
		rating.addValueChangeListener(ratingValue -> rating.setValue(Math.ceil((Double) ratingValue.getProperty().getValue())));
		rating.setWidth("100%");
		
		ratingWindowLayout.addComponent(genreLabel);
		ratingWindowLayout.addComponent(rating);
		ratingWindowLayout.addComponent(plotArea);
		ratingWindowLayout.setSizeFull();
		ratingWindowLayout.setExpandRatio(plotArea, 1.0f);
		
		ratingWindowLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		new DialogBuilder(ui, ratingWindowLayout, DialogType.INFO)
				.title("Rating for: " + name)
				.height(50f, Unit.PERCENTAGE)
				.width(4.0f, Unit.INCH)
				.showCancel()
				.resultConsumer(yesNoCancelResult -> {
					if (YesNoCancelResult.YES.equals(yesNoCancelResult)) {
						actionListener.rateMovie(movie_id, rating.getValue(), name);
					}
				})
				.display();
	}
}
