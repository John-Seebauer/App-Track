package edu.illinois.backend;

import edu.illinois.logic.LoginModel;
import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.DatabaseTable;
import edu.illinois.util.JDBCResult;
import edu.illinois.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
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
	public void addNewUser(String name, String username, String password) {
		storageService.runUPDATEquery(String.format(getProperty("backend.ADD_NEW_USER"),
				name, username, password), false, this::newUserAccepted, this::newUserRejected,
				Arrays.asList(username, password));
	}
	
	@Override
	public void newUserAccepted(final JDBCResult result) {
		if(!result.hadFailure()) {
			Optional<Object> usernamePasswordListOpt = result.getOriginalQuery().getAdditionalArgs();
			if(usernamePasswordListOpt.isPresent()) {
				List<String> usernamePasswordList = (List<String>) usernamePasswordListOpt.get();
				authenticateUser(usernamePasswordList.get(0), usernamePasswordList.get(1));
			}
		}
	}
	
	public void newUserRejected(JDBCResult result) {
		if (!result.hadFailure()) {
			logger.log(Level.CONFIG, "Failure called but not set in result", result);
		} else {
			result.getOriginalQuery().getAdditionalArgs().ifPresent(name -> logger.log(Level.FINEST, "Unable to create user " + ((List) name).get(0)));
			result.getOriginalQuery().getAdditionalArgs().ifPresent(name -> actionListener.unableToCreateUser((String) ((List) name).get(0)));
		}
	}
	
	
	@Override
	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
}
