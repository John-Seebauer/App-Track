package edu.illinois.web;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public class NavigationMenu extends CssLayout {
	private final static Logger logger = Logger.getLogger(NavigationMenu.class.getName());
	private final Navigator navigator;
	private final CssLayout menuContainer;
	private final CssLayout menuLayoutList;
	private final Map<String, Button> buttonNameRefs;
	
	public NavigationMenu(Navigator navigator, String username) {
		this.navigator = navigator;
		setPrimaryStyleName(ValoTheme.MENU_ROOT);
		menuContainer = new CssLayout();
		menuContainer.addStyleName(ValoTheme.MENU_PART_LARGE_ICONS);
		
		final VerticalLayout buttonLayout = new VerticalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		buttonLayout.addStyleName(ValoTheme.MENU_TITLE);
		Label title = new Label("Movie Matcher", ContentMode.TEXT);
		title.addStyleName(ValoTheme.LABEL_H1);
		title.setSizeUndefined();
		buttonLayout.addComponent(title);
		
		Label welcome = new Label("Welcome, " + username);
		welcome.addStyleName(ValoTheme.LABEL_H3);
		welcome.setSizeUndefined();
		buttonLayout.addComponent(welcome);
		menuContainer.addComponent(buttonLayout);
		
		menuLayoutList = new CssLayout();
		menuLayoutList.setPrimaryStyleName("valo-menuitems");
		menuContainer.addComponent(menuLayoutList);
		addComponent(menuContainer);
		
		buttonNameRefs = new HashMap<>();
	}
	
	public void addView(View view, String name, Resource icon) {
		navigator.addView(name, view);
		constructViewMenuButton(name, icon);
	}
	
	private void constructViewMenuButton( String name, Resource icon ) {
		Button menuItem = new Button(name, icon);
		menuItem.addClickListener( click -> navigator.navigateTo(name));
		menuItem.setPrimaryStyleName(ValoTheme.MENU_ITEM);
		menuLayoutList.addComponent(menuItem);
		buttonNameRefs.put(name, menuItem);
	}
	
	public void setActive(String viewName) {
		buttonNameRefs.values().forEach( button -> button.removeStyleName("selected"));
		Button selected = buttonNameRefs.get(viewName);
		if (selected != null) {
			selected.addStyleName("selected");
		}
		menuContainer.removeStyleName("valo-menu-visible");
	}
	
	public Set<String> getViewNames() {
		return buttonNameRefs.keySet();
	}
}
