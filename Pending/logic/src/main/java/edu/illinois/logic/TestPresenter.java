package edu.illinois.logic;

/**
 * Created by John Seebauer (seebaue2) on 11/6/16.
 */
public class TestPresenter<V extends TestView, M extends TestModel> extends CommonPresenter<V,M>
		implements TestView.ActionListener {
	
	@Override
	public void init(V view, M model) {
		this.view = view;
		this.model = model;
		model.init();
		view.setActionListener(this);
	}
}
