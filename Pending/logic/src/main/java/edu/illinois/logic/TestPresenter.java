package edu.illinois.logic;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;

/**
 * Created by John Seebauer (seebaue2) on 11/6/16.
 */
public class TestPresenter<V extends TestView, M extends TestModel> extends CommonPresenter<V,M>
		implements TestView.ActionListener {
	
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
			e.printStackTrace();
		}
		
		
		
	}
}
