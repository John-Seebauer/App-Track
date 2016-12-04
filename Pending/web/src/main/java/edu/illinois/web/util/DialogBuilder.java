package edu.illinois.web.util;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 9/18/16.
 */
public class DialogBuilder {
	private final static Logger logger = Logger.getLogger(DialogBuilder.class.getName());
	private final UI ui;
	
	private final String DEFAULT_ACCEPT_TEXT = "OK";
	private final String DEFAULT_CANCEL_TEXT = "Cancel";
	private final String DEFAULT_NO_TEXT = "No";
	private final float DEFAULT_WIDTH = 40.0f;
	private final float DEFAULT_HEIGHT = 50.0f;
	
	private Consumer<YesNoCancelResult> consumer;
	private AbstractComponent content;
	
	private String okText = DEFAULT_ACCEPT_TEXT;
	private String cancelText = DEFAULT_CANCEL_TEXT;
	private String noText = DEFAULT_NO_TEXT;
	private boolean showCancel = false;
	private boolean showNo = false;
	private String title;
	private BooleanSupplier isCloseValid;
	private float width;
	private Sizeable.Unit widthUnit;
	private float height;
	private Sizeable.Unit heightUnit;
	private DialogType type;
	
	
	public DialogBuilder(@NotNull UI ui, @NotNull AbstractComponent content, @NotNull DialogType type) {
		this.ui = Objects.requireNonNull(ui, "Must provide a parent.");
		this.content = Objects.requireNonNull(content, "Must provide content for a dialog.");
		width = DEFAULT_WIDTH;
		widthUnit = Sizeable.Unit.PERCENTAGE;
		height = DEFAULT_HEIGHT;
		heightUnit = Sizeable.Unit.PERCENTAGE;
		this.type = type;
	}
	
	public DialogBuilder title(String title) {
		this.title = title;
		return this;
	}
	
	public DialogBuilder yesText(String yesText) {
		this.okText = yesText;
		return this;
	}
	
	public DialogBuilder withNoButton() {
		showNo = true;
		return this;
	}
	
	public DialogBuilder withNoButton(String noText) {
		this.noText = noText;
		showNo = true;
		return this;
	}
	
	public DialogBuilder showCancel() {
		showCancel = true;
		return this;
	}
	
	public DialogBuilder showCancel(String cancelText) {
		this.cancelText = cancelText;
		showCancel = true;
		return this;
	}
	
	public DialogBuilder width(float amount, Sizeable.Unit unit) {
		width = amount;
		widthUnit = unit;
		return this;
	}
	
	public DialogBuilder height(float amount, Sizeable.Unit unit) {
		height = amount;
		heightUnit = unit;
		return this;
	}
	
	public DialogBuilder acceptEnabled(BooleanSupplier isCloseValid) {
		this.isCloseValid = isCloseValid;
		return this;
	}
	
	public DialogBuilder resultConsumer(Consumer<YesNoCancelResult> consumer) {
		this.consumer = consumer;
		return this;
	}
	
	public void display() {
		PopupDialog dialog = new PopupDialog(ui, title, type, content, width, widthUnit, okText, noText, cancelText,
				height, heightUnit, showNo, showCancel, consumer, isCloseValid);
		dialog.display();
	}
	
	private class PopupDialog extends Window {
		private final UI ui;
		private final String title;
		private final DialogType type;
		private final AbstractComponent content;
		private final float width;
		private final Unit widthUnits;
		private final String yesText;
		private final String noText;
		private final String cancelText;
		private final float height;
		private final Unit heightUnits;
		private final boolean noEnabled;
		private final boolean cancelEnabled;
		private final Consumer<YesNoCancelResult> resultConsumer;
		private final BooleanSupplier isCloseValid;
		
		PopupDialog(UI ui, String title, DialogType type, AbstractComponent content, float width, Unit widthUnits,
		            String yesText, String noText, String cancelText, float height, Unit heightUnits,
		            boolean noEnabled, boolean cancelEnabled, Consumer<YesNoCancelResult> resultConsumer,
		            BooleanSupplier isCloseValid) {
			super();
			this.ui = ui;
			this.title = title;
			this.type = type;
			this.content = content;
			this.width = width;
			this.widthUnits = widthUnits;
			this.yesText = yesText;
			this.noText = noText;
			this.cancelText = cancelText;
			this.height = height;
			this.heightUnits = heightUnits;
			this.noEnabled = noEnabled;
			this.cancelEnabled = cancelEnabled;
			this.resultConsumer = resultConsumer;
			this.isCloseValid = isCloseValid;
		}
		
		void display() {
			setWidth(width, widthUnits);
			setHeight(height, heightUnits);
			setClosable(false);
			setResizable(false);
			Panel base = new Panel();
			VerticalLayout mainLayout = new VerticalLayout();
			HorizontalLayout bottomBar = new HorizontalLayout();
			bottomBar.setSpacing(true);
			mainLayout.setMargin(true);
			mainLayout.setSpacing(true);
			
			
			HorizontalLayout titleBar = new HorizontalLayout();
			Image icon = new Image();
			if(DialogType.INFO.equals(type)) {
				icon.setData(FontAwesome.INFO);
			} else if(DialogType.WARNING.equals(type)) {
				icon.setData(FontAwesome.EXCLAMATION);
			} else {
				icon.setData(FontAwesome.EXCLAMATION_TRIANGLE);
			}
			titleBar.addComponent(icon);
			titleBar.addComponent(new Label(title));
			mainLayout.addComponent(titleBar);
			
			mainLayout.addComponent(content);
			
			bottomBar.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
			
			if (noEnabled) {
				Button noButton = new Button(noText);
				noButton.addClickListener(event -> {
					this.close();
					if (resultConsumer != null) {
						resultConsumer.accept(YesNoCancelResult.NO);
					}
				});
				noButton.setWidth(10.0f, Unit.EX);
				bottomBar.addComponent(noButton);
				bottomBar.setComponentAlignment(noButton, Alignment.BOTTOM_LEFT);
				bottomBar.setExpandRatio(noButton, 1);
			}
			
			if (cancelEnabled) {
				Button cancelButton = new Button(cancelText);
				cancelButton.addClickListener(event -> {
					this.close();
					if (resultConsumer != null) {
						resultConsumer.accept(YesNoCancelResult.CANCEL);
					}
				});
				cancelButton.setWidth(10.0f, Unit.EX);
				bottomBar.addComponent(cancelButton);
				bottomBar.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
				
			}
			Button okButton = new Button(yesText);
			okButton.addClickListener(event -> {
				if (isCloseValid == null || isCloseValid.getAsBoolean()) {
					this.close();
					if (resultConsumer != null) {
						resultConsumer.accept(YesNoCancelResult.YES);
					}
				}
				
			});
			okButton.setWidth(10.0f, Unit.EX);
			okButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			okButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
			bottomBar.addComponent(okButton);
			bottomBar.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);
			
			
			bottomBar.setHeight(String.valueOf(okButton.getHeight()));
			bottomBar.setWidth(100.0f, Unit.PERCENTAGE);
			mainLayout.addComponent(bottomBar);
			mainLayout.setComponentAlignment(bottomBar, Alignment.BOTTOM_CENTER);
			mainLayout.setSizeFull();
			mainLayout.setExpandRatio(content, 1.0f);
			base.setContent(mainLayout);
			base.setSizeFull();
			setContent(base);
			center();
			setModal(true);
			
			ui.access( () -> ui.addWindow(this));
		}
	}
}
