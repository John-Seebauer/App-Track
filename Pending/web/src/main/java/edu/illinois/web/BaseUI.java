package edu.illinois.web;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import edu.illinois.backend.WebLoginModel;
import edu.illinois.logic.LoginModel;
import edu.illinois.logic.LoginPresenter;
import edu.illinois.logic.LoginView;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
public class BaseUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
	    Responsive.makeResponsive(this);
	    setLocale(vaadinRequest.getLocale());
	    getPage().setTitle("Pending");
	    addStyleName(ValoTheme.UI_WITH_MENU);
	
	    WebLoginView loginWindow = new WebLoginView(this);
	    loginWindow.init(username -> {
		    this.setContent(new MainUI(BaseUI.this, username));
		    getNavigator().navigateTo(getNavigator().getState());
	    });
	    loginWindow.center();
	    setSizeFull();
	    loginWindow.setWidth("70%");
	    loginWindow.setHeight("70%");
	    try {
		    WebLoginModel model = WebLoginModel.class.newInstance();
		    LoginPresenter<LoginView, LoginModel> presenter = new LoginPresenter<>();
		    presenter.init(loginWindow, model);
		    loginWindow.setActionListener(presenter);
	    } catch (InstantiationException | IllegalAccessException e) {
		    e.printStackTrace();
	    }
		addWindow(loginWindow);
	    
    }
    
   public static BaseUI get() {
	   return (BaseUI) UI.getCurrent();
   }

    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = BaseUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
