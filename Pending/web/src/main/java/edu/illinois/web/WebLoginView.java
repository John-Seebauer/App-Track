package edu.illinois.web;

import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import edu.illinois.logic.LoginView;
import edu.illinois.web.util.DialogBuilder;
import edu.illinois.web.util.DialogType;
import edu.illinois.web.util.YesNoCancelResult;

import java.util.function.Consumer;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public class WebLoginView extends Window implements LoginView{
	
	private final UI ui;
	private ActionListener actionListener;
	
	public WebLoginView(UI ui) {
		super("Authentication required to proceed");
		this.ui = ui;
		
		setCaption("login");
	}
	
	void init(Consumer<String> displayAfterLogin) {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setSpacing(true);
		
		HorizontalLayout loginBox = new HorizontalLayout();
		loginBox.setSizeFull();
		loginBox.setMargin(true);
		loginBox.setSpacing(true);
		TextField username = new TextField("Username");
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
			Label usernameLabel = new Label("Username");
			TextField usernameField = new TextField();
			Label passwordLabel = new Label("Password");
			PasswordField passwordField = new PasswordField("Password", "");
			passwordField.setImmediate(true);
			Label languageLabel = new Label("Language");
			TextField languageField = new TextField();
			
			
			loginLayout.addComponent(nameLabel);
			loginLayout.addComponent(nameField);
			loginLayout.addComponent(usernameLabel);
			loginLayout.addComponent(usernameField);
			loginLayout.addComponent(passwordLabel);
			loginLayout.addComponent(passwordField);
			loginLayout.addComponent(languageLabel);
			loginLayout.addComponent(languageField);
			
			new DialogBuilder(ui, loginLayout, DialogType.INFO)
				.showCancel()
				.resultConsumer( consumer -> {
					if(YesNoCancelResult.YES.equals(consumer)) {
						actionListener.addNewUser(nameField.getValue(), usernameField.getValue(),
							passwordField.getValue(), languageField.getValue());
						if(actionListener.authenticate(usernameField.getValue(), passwordField.getValue())) {
							displayAfterLogin.accept(usernameField.getValue());
							close();
							showMessage("Unknown user");
						}
					}
				})
				.display();
		});
		
		
		Button enterShortcut = new Button("Login");
		enterShortcut.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		enterShortcut.addStyleName(ValoTheme.BUTTON_PRIMARY);
		enterShortcut.addClickListener( listener -> {
			String passwordText = password.getValue();
			
			if(actionListener.authenticate(username.getValue(), passwordText)) {
				displayAfterLogin.accept(username.getValue());
				close();
			} else {
				showMessage("Unknown user");
			}
		});
		
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
}
