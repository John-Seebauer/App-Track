package edu.illinois.web;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import edu.illinois.logic.TestView;
import edu.illinois.web.util.DialogBuilder;
import edu.illinois.web.util.DialogType;

/**
 * Created by john on 9/24/16.
 */
public class WebTestView extends AbstractWebView implements TestView {
	
	private TestView.ActionListener actionListener;
	
	public WebTestView() {
		/*Called via reflection */
	}
	
	
	@Override
	public void init(UI ui) {
		this.ui = ui;
		setMargin(true);
		setSpacing(true);
		setupView();
	}
	
	private void setupView() {
		Button popupDialog = new Button("Show popup");
		popupDialog.addClickListener( event -> {
			new DialogBuilder(ui, new Label("You opened a new dialog."), DialogType.INFO)
					.title("Title goes here")
					.showCancel()
					.withNoButton()
					.resultConsumer( yesNoCancelResult -> {
						Notification.show("Window closed!",
								String.format("You clicked %s", yesNoCancelResult.toString()),
								Notification.Type.HUMANIZED_MESSAGE);
					})
					.display();
		});
		addComponent(popupDialog);
		
		Button resetConfigButton = new Button("Reset Config");
		resetConfigButton.setClickShortcut(ShortcutAction.KeyCode.END);
		resetConfigButton.addClickListener(event -> {
			actionListener.reloadConfig();
			showMessage("Configuration reloaded from file.");
		});
		addComponent(resetConfigButton);
	}
	
	@Override
	public void setActionListener(TestView.ActionListener listener) {
		this.actionListener = listener;
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		ui.access( () -> ui.getPage().setTitle(event.getViewName()));
	}
}
