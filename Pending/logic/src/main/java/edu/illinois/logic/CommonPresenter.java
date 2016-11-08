package edu.illinois.logic;

/**
 * Created by John Seebauer (seebaue2) on 9/20/16.
 */
public abstract class CommonPresenter<V extends CommonView,M extends CommonModel> {
	
	V view;
	M model;
	
	public void init(V view, M model) {
		
		this.view = view;
		this.model = model;
	}
	
	public String getProperty(String name) {
		return model.getProperty(name);
	}
	
	public void reloadConfig() {
		model.reloadConfig();
	}
}
