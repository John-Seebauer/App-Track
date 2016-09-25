package edu.illinois.web;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import edu.illinois.web.search.WebSearchView;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class MainUI extends HorizontalLayout {
	private final BaseUI ui;
	
	enum ViewTypes {
		SEARCH (WebSearchView.class, "Search", FontAwesome.SEARCH),
		ABOUT (WebAboutView.class, "About", FontAwesome.INFO),
		TEST(WebTestView.class, "Test", FontAwesome.BUG );
		
		final Resource icon;
		final String caption;
		final Class type;
		
		ViewTypes(Class type, String caption, Resource icon) {
			this.type = type;
			this.caption = caption;
			this.icon = icon;
		}
	}
	
	
	private final NavigationMenu menu;
	
	public MainUI(BaseUI ui) {
		this.ui = ui;
		
		setStyleName("main-screen");
		
		CssLayout navContainer = new CssLayout();
		navContainer.setSizeFull();
		navContainer.addStyleName("valo-content");
		
		final Navigator menuNavigator = new Navigator(ui, navContainer);
		menuNavigator.setErrorView(WebErrorView.class);
		menu = new NavigationMenu(menuNavigator);
		try {
			createViews(menu);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
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
			WebAbstractLayout view = (WebAbstractLayout) item.type.newInstance();
			view.init(ui);
			menu.addView(view, item.caption, item.icon);
		}
	}
}
