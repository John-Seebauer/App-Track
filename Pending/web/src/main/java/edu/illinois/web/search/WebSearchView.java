package edu.illinois.web.search;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import edu.illinois.logic.SearchView;
import edu.illinois.web.AbstractWebView;

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
		changeContainer(container);
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
		
		TextField movieInput = new TextField("Movie");
		movieInput.setWidth("100%");
		Button getActors = new Button("Get actors for movie");
		getActors.addClickListener(event -> {
			if (movieInput.getValue() != null) {
				actionListener.initSearchrequst(String.format(
						actionListener.getProperty("backend.GET_ACTORS_FOR_MOVIE"),
						movieInput.getValue()));
			}
		});
		
		HorizontalLayout firstQuery = new HorizontalLayout();
		firstQuery.setSpacing(true);
		firstQuery.addComponent(movieInput);
		firstQuery.addComponent(getActors);
		firstQuery.setExpandRatio(getActors, 1.0f);
		
		TextField actorInput = new TextField("Actor name");
		actorInput.setWidth("100%");
		Button moviesForActor = new Button("Get movies actor is in");
		moviesForActor.addClickListener(event -> {
			if (actorInput.getValue() != null) {
				String firstname = actorInput.getValue().split(" ")[0];
				String lastname = actorInput.getValue().split(" ")[1];
				actionListener.initSearchrequst(String.format(
						actionListener.getProperty("backend.GET_MOVIES_FOR_ACTOR"),
						lastname, firstname));
			}
		});
		firstQuery.addComponent(actorInput);
		firstQuery.addComponent(moviesForActor);
		firstQuery.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
		firstQuery.setExpandRatio(actorInput, 1.0f);
		
		HorizontalLayout secondQuery = new HorizontalLayout();
		secondQuery.setSpacing(true);
		secondQuery.addComponent(actorInput);
		secondQuery.addComponent(moviesForActor);
		secondQuery.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
		secondQuery.setExpandRatio(actorInput, 1.0f);
		
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
		
		top.setSpacing(true);
		top.addComponent(queryBar);
		top.addComponent(search);
		top.addComponent(progressBar);
		top.setWidth("100%");
		top.setExpandRatio(queryBar, 1.0f);
		
		databaseGrid = new Grid();
		databaseGrid.setSizeFull();
		
		baseContainer.addComponent(firstQuery);
		baseContainer.addComponent(secondQuery);
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
	
	public void queryFailedCleanup() {
		ui.access( () -> progressBar.setVisible(false));
	}
}
