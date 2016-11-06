package edu.illinois.web;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import edu.illinois.backend.WebCommonModel;
import edu.illinois.backend.databaseViewer.WebDatabaseViewerModel;
import edu.illinois.backend.search.WebSearchModel;
import edu.illinois.logic.CommonPresenter;
import edu.illinois.logic.DatabaseViewerPresenter;
import edu.illinois.logic.SearchPresenter;
import edu.illinois.web.search.WebSearchView;
import edu.illinois.web.util.UncaughtExceptionDialog;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class MainUI extends HorizontalLayout {
	private final BaseUI ui;
	
	enum ViewTypes {
		SEARCH (WebSearchView.class, SearchPresenter.class, WebSearchModel.class,
				"Search", FontAwesome.SEARCH),
		ABOUT (WebAboutView.class, null, null,
				"About", FontAwesome.INFO),
		DATABASE(WebDatabaseViewerView.class, DatabaseViewerPresenter.class, WebDatabaseViewerModel.class,
				"Database Viewer", FontAwesome.DATABASE),
		TEST(WebTestView.class, null, null,
				"Test", FontAwesome.BUG );
		
		final Resource icon;
		final String caption;
		final Class viewType;
		final Class presenterType;
		final Class modelType;
		
		ViewTypes(Class viewType, Class presenterType, Class modelType, String caption, Resource icon) {
			this.viewType = viewType;
			this.presenterType = presenterType;
			this.modelType = modelType;
			this.caption = caption;
			this.icon = icon;
		}
	}
	
	
	private final NavigationMenu menu;
	
	public MainUI(BaseUI ui, String username) {
		this.ui = ui;
		
		setStyleName("main-screen");
		
		CssLayout navContainer = new CssLayout();
		navContainer.setSizeFull();
		navContainer.addStyleName("valo-content");
		
		final Navigator menuNavigator = new Navigator(ui, navContainer);
		menuNavigator.setErrorView(WebErrorView.class);
		UncaughtExceptionDialog.ui = ui;
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionDialog());
		menu = new NavigationMenu(menuNavigator);
		try {
			createViews(menu);
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		
		menuNavigator.addViewChangeListener(new ViewChangeListener() {
			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				return true;
			}
			
			@Override
			public void afterViewChange(ViewChangeEvent event) {
				menu.setActive(event.getViewName());
			}
		});
		
		
		
		addComponent(menu);
		addComponent(navContainer);
		setExpandRatio(navContainer, 1);
		setSizeFull();
		menuNavigator.navigateTo("Search");
	}
	
	private void createViews(NavigationMenu menu) throws IllegalAccessException, InstantiationException {
		for(ViewTypes item : ViewTypes.values()) {
			WebAbstractLayout view = (WebAbstractLayout) item.viewType.newInstance();
			WebCommonModel model = null;
			if(item.modelType != null) {
				model = (WebCommonModel) item.modelType.newInstance();
				CommonPresenter presenter = (CommonPresenter) item.presenterType.newInstance();
				presenter.init(view, model);
			}
			
			view.init(ui);
			menu.addView(view, item.caption, item.icon);
		}
	}
}
