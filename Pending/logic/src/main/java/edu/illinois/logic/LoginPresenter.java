package edu.illinois.logic;

import java.util.logging.Logger;

/**
 * Created by john on 10/22/16.
 */
public class LoginPresenter<V extends LoginView, M extends LoginModel> extends CommonPresenter<V,M>
	implements LoginView.ActionListener {
	private final static Logger logger = Logger.getLogger(LoginPresenter.class.getName());
	@Override
	public void init(V view, M model) {
		this.view = view;
		this.model = model;
		model.init();
		view.setActionListener(this);
	}
	
	
	@Override
	public boolean authenticate(String username, String password) {
		return model.authenticateUser(username, password);
	}
	
	@Override
	public void addNewUser(String name, String username, String password, String language) {
		model.addNewUser(name, username, password, language);
	}
}
