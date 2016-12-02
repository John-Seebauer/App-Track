package edu.illinois.web;

import com.vaadin.annotations.Push;
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
import edu.illinois.web.util.UncaughtExceptionDialog;

import javax.servlet.annotation.WebServlet;
import java.util.logging.Level;
import java.util.logging.Logger;

@Theme("mytheme")
@Push
public class BaseUI extends UI {
	private final static Logger logger = Logger.getLogger(BaseUI.class.getName());
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
	    Responsive.makeResponsive(this);
	    setLocale(vaadinRequest.getLocale());
	    getPage().setTitle("Pending");
	    addStyleName(ValoTheme.UI_WITH_MENU);
	
	    WebLoginView loginWindow = new WebLoginView(this);
	    loginWindow.init(username -> {
		    getUI().access( () -> {
			    this.setContent(new MainUI(BaseUI.this, username));
			    getNavigator().navigateTo(getNavigator().getState());
			    logger.info("Base created the Main UI.");
		    });
	    });
	    loginWindow.center();
	    setSizeFull();
	    loginWindow.setWidth("70%");
	    loginWindow.setHeight("70%");
	    UI.getCurrent().setErrorHandler(new UncaughtExceptionDialog());
	    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
		    //Run thread when exiting
		    logger.log(Level.SEVERE, "System is exiting!");
	    }));
	    try {
		    WebLoginModel model = WebLoginModel.class.newInstance();
		    LoginPresenter<LoginView, LoginModel> presenter = new LoginPresenter<>();
		    presenter.init(loginWindow, model);
		    loginWindow.setActionListener(presenter);
	    } catch (InstantiationException | IllegalAccessException e) {
		    logger.log(Level.SEVERE, "Could not create Login page!", e);
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
