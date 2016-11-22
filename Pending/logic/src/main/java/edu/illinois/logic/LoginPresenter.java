package edu.illinois.logic;

import edu.illinois.util.Pair;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public class LoginPresenter<V extends LoginView, M extends LoginModel> extends CommonPresenter<V,M>
	implements LoginView.ActionListener, LoginModel.ActionListener {
	private final static Logger logger = Logger.getLogger(LoginPresenter.class.getName());
	@Override
	public void init(V view, M model) {
		this.view = view;
		this.model = model;
		model.init();
		model.setActionListener(this);
		view.setActionListener(this);
	}
	
	
	@Override
	public void authenticate(String username, String password) {
		model.authenticateUser(username, password);
	}
	
	@Override
	public void addNewUser(String name, String username, String password, String language) {
		model.addNewUser(name, username, password, language);
	}
	
	@Override
	public void authenticateUserResponse(List<Pair<String, String>> usernamePasswordMatches, String password) {
		if(usernamePasswordMatches.size() != 1) logger.warning("Multiple entries for username " +
				usernamePasswordMatches.iterator().next().getOne());
		for(Pair<String, String> user : usernamePasswordMatches) {
			if(user.getTwo().equals(password)) {
				loginUser(user.getOne());
				return;
			}
		}
		view.showMessage("Unknown user: " + usernamePasswordMatches.iterator().next().getOne());
	}
	
	public void loginUser(String username) {
		view.loginUser(username);
	}
}
