package edu.illinois.logic;

import edu.illinois.util.Pair;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public class LoginPresenter<V extends LoginView, M extends LoginModel> extends CommonPresenter<V, M>
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
		try {
			model.authenticateUser(username, generateHash(password));
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.SEVERE, "Could not find hash algorithm!", e);
		}
	}
	
	@Override
	public void addNewUser(String name, String username, String password) {
		try {
			model.addNewUser(name, username, generateHash(password));
		} catch (NoSuchAlgorithmException e) {
			view.showAndLogError(logger, Level.SEVERE, "Could not find hash algorithm!", e);
		}
	}
	
	@Override
	public void authenticateUserResponse(List<Pair<String, String>> usernamePasswordMatches, String password) {
		if (usernamePasswordMatches.size() != 1) logger.warning("Multiple entries for username ");
		
		
		for (Pair<String, String> user : usernamePasswordMatches) {
			if (user.getTwo().equals(password)) {
				loginUser(user.getOne());
				return;
			}
		}
		
		view.showMessage("Unknown user.");
	}
	
	public void loginUser(String username) {
		view.loginUser(username);
	}
	
	private String generateHash(String password) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance(model.getProperty("PASSWORD_HASH_ALGORITHM"));
		byte[] hashedBytes = digest.digest(password.getBytes());
		
		return Base64.getEncoder().encodeToString(hashedBytes);
	}
}
