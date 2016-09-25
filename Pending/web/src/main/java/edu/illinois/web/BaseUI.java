package edu.illinois.web;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
public class BaseUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
	    Responsive.makeResponsive(this);
	    setLocale(vaadinRequest.getLocale());
	    getPage().setTitle("Pending");
	    addStyleName(ValoTheme.UI_WITH_MENU);
	    setContent(new MainUI(BaseUI.this));
	    getNavigator().navigateTo(getNavigator().getState());
    }
    
   public static BaseUI get() {
	   return (BaseUI) UI.getCurrent();
   }

    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = BaseUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
