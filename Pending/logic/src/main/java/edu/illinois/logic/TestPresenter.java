package edu.illinois.logic;

import com.vaadin.ui.Image;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 11/6/16.
 */
public class TestPresenter<V extends TestView, M extends TestModel> extends CommonPresenter<V,M>
		implements TestView.ActionListener {
	private final static Logger logger = Logger.getLogger(TestPresenter.class.getName());
	private WatchService watchService;
	
	
	@Override
	public void init(V view, M model) {
		this.view = view;
		this.model = model;
		model.init();
		view.setActionListener(this);
		
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			logger.log(Level.FINE, "Could not initialize WatchService.", e);
		}
		
	}
	
	@Override
	public Image getImage(String movie, Integer year) {
		return null;
	}
}
