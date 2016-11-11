package edu.illinois.web;

import com.vaadin.navigator.View;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public abstract class AbstractWebView extends VerticalLayout implements WebAbstractLayout, View {
	private final static Logger logger = Logger.getLogger(AbstractWebView.class.getName());
	
	protected UI ui;
	
	public void init(UI ui) {
		this.ui = ui;
	}
}
