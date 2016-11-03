package edu.illinois.web;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import edu.illinois.logic.LoginView;
import edu.illinois.web.util.DialogBuilder;
import edu.illinois.web.util.DialogType;

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
		setModal(false);
		setClosable(false);
		setDraggable(false);
		setResizable(false);
		HorizontalLayout loginBox = new HorizontalLayout();
		loginBox.setSizeFull();
		loginBox.setMargin(true);
		loginBox.setSpacing(true);
		TextField username = new TextField();
		TextField password = new TextField();
		Button createNewUser = new Button("New User");
		createNewUser.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		
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
		layout.addComponent(loginBox);
		layout.addComponent(createNewUser);
		layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		setContent(layout);
		setSizeFull();
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
