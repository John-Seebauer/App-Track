package edu.illinois.backend;

import edu.illinois.logic.LoginModel;
import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.DatabaseTable;
import edu.illinois.util.JDBCResult;
import edu.illinois.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public class WebLoginModel extends WebCommonModel implements LoginModel {
	private final static Logger logger = Logger.getLogger(WebLoginModel.class.getName());
	private LoginModel.ActionListener actionListener;
	
	public void authenticateUser(String username, String password) {
		storageService.runSELECTquery(
				String.format(getProperty("backend.GET_PASSWORD_FOR_USER"), username), this::validUser, null, password);
	}
	
	public void validUser(final JDBCResult result) {
		if(result.getResult().isPresent()) {
			DatabaseTable response = result.getResult().get();
			Optional<Object> passwordArg = result.getOriginalQuery().getAdditionalArgs();
			if(!passwordArg.isPresent()) throw new UnsupportedOperationException("Need original password!");
			String password = (String) passwordArg.get();
			List<Pair<String, String>> possibleMatches = new ArrayList<>();
			for(DatabaseEntry pword : response.getRows()) {
				possibleMatches.add(new Pair<>(pword.getAttribute("username", String.class),
						pword.getAttribute("password", String.class)));
			}
			actionListener.authenticateUserResponse(possibleMatches, password);
		}
	}
	
	@Override
	public void addNewUser(String name, String username, String password, String language) {
		storageService.runUPDATEquery(String.format(getProperty("backend.ADD_NEW_USER"),
				name, username, password, language), false, this::newUserAccepted, null);
	}
	
	@Override
	public void newUserAccepted(final JDBCResult result) {
		if(result.getResult().isPresent()) {
			DatabaseTable response = result.getResult().get();
			actionListener.loginUser(response.getRows().iterator().next().getAttribute("username", String.class));
		}
	}
	@Override
	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
}
