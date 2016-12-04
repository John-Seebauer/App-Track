package edu.illinois.web;

import com.vaadin.data.Validator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import edu.illinois.logic.LoginView;
import edu.illinois.web.util.DialogBuilder;
import edu.illinois.web.util.DialogType;
import edu.illinois.web.util.YesNoCancelResult;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public class WebLoginView extends Window implements LoginView {
	private final static Logger logger = Logger.getLogger(WebLoginView.class.getName());
	
	private final UI ui;
	private ActionListener actionListener;
	private Consumer<String> displayAfterLogin;
	
	public WebLoginView(UI ui) {
		super("Authentication required to proceed");
		this.ui = ui;
		
		setCaption("login");
	}
	
	void init(Consumer<String> displayAfterLogin) {
		this.displayAfterLogin = displayAfterLogin;
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setSpacing(true);
		
		HorizontalLayout loginBox = new HorizontalLayout();
		loginBox.setSizeFull();
		loginBox.setMargin(true);
		loginBox.setSpacing(true);
		TextField username = new TextField("Username");
		username.addValidator((Validator) value -> {
			for (String item : Arrays.asList(" ", "\t", "\n", "\0", ";")) {
				if (String.valueOf(value).contains(item)) {
					throw new Validator.InvalidValueException("Username contains " + item);
				}
			}
		});
		
		PasswordField password = new PasswordField("Password");
		Button createNewUser = new Button("New User");
		createNewUser.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		createNewUser.addClickListener( clickEvent -> {
			VerticalLayout loginLayout = new VerticalLayout();
			loginLayout.setMargin(true);
			loginLayout.setSpacing(true);
			loginLayout.setSizeFull();
			
			Label nameLabel = new Label("Name");
			TextField nameField = new TextField();
			nameField.addValidator((Validator) value -> {
				for (String item : Arrays.asList(" ", "\t", "\n", "\0", ";")) {
					if (String.valueOf(value).contains(item)) {
						throw new Validator.InvalidValueException("Username contains " + item);
					}
				}
			});
			Label usernameLabel = new Label("Username");
			TextField usernameField = new TextField();
			usernameField.addValidator((Validator) value -> {
				for (String item : Arrays.asList(" ", "\t", "\n", "\0", ";")) {
					if (String.valueOf(value).contains(item)) {
						throw new Validator.InvalidValueException("Username contains " + item);
					}
				}
			});
			Label passwordLabel = new Label("Password");
			PasswordField passwordField = new PasswordField();
			passwordField.setImmediate(true);
			
			loginLayout.addComponent(nameLabel);
			loginLayout.addComponent(nameField);
			loginLayout.addComponent(usernameLabel);
			loginLayout.addComponent(usernameField);
			loginLayout.addComponent(passwordLabel);
			loginLayout.addComponent(passwordField);
			
			new DialogBuilder(ui, loginLayout, DialogType.INFO)
				.showCancel()
					.acceptEnabled(() -> nameField.isValid() && usernameField.isValid())
				.resultConsumer( consumer -> {
					if(YesNoCancelResult.YES.equals(consumer)) {
						actionListener.addNewUser(nameField.getValue(), usernameField.getValue(),
							passwordField.getValue());
					}
				})
				.display();
		});
		
		
		Button enterShortcut = new Button("Login");
		enterShortcut.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		enterShortcut.addStyleName(ValoTheme.BUTTON_PRIMARY);
		enterShortcut.addClickListener( listener -> {
			if (username.isValid()) {
				String passwordText = password.getValue();
				actionListener.authenticate(username.getValue(), passwordText);
			} else {
				showMessage("Invalid username");
			}
		});
		
		username.setTabIndex(1);
		password.setTabIndex(2);
		username.focus();
		
		loginBox.addComponent(username);
		loginBox.addComponent(password);
		loginBox.addComponent(enterShortcut);
		loginBox.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
		layout.addComponent(loginBox);
		layout.addComponent(createNewUser);
		layout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
		setContent(layout);
		setSizeFull();
		setModal(false);
		setClosable(false);
		setDraggable(false);
		setResizable(false);
		setWindowMode(WindowMode.NORMAL);
	}
	
	
	@Override
	public void setActionListener(ActionListener listener) {
		this.actionListener = listener;
	}
	
	@Override
	public void loginUser(String username) {
		ui.access( () -> {
			displayAfterLogin.accept(username);
			close();
		});
	}
	
	@Override
	public void showMessage(String message) {
		new DialogBuilder(ui, new Label(message), DialogType.INFO)
				.width(20, Unit.PERCENTAGE)
				.height(15, Unit.PERCENTAGE)
				.display();
	}
	
	@Override
	public void showError(Throwable error) {
		new DialogBuilder(ui, new Label(error.getLocalizedMessage()), DialogType.ERROR).display();
	}
	
	@Override
	public void showAndLogError(Logger log, Level level, String message, Throwable error) {
		log.log(level, message, error);
		showError(error);
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		ui.access( () -> ui.getPage().setTitle(event.getViewName()));
	}
	
	@Override
	public void showNotification(String title, String description) {
		Notification.show(title, description, Notification.Type.HUMANIZED_MESSAGE);
	}
	
	@Override
	public void showNotification(String description) {
		Notification.show(description);
	}
}
