package edu.illinois.backend;

import edu.illinois.logic.LoginModel;
import edu.illinois.util.DatabaseEntry;
import edu.illinois.util.DatabaseTable;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.prng.DigestRandomGenerator;

import java.sql.SQLException;

/**
 * Created by John Seebauer (seebaue2) on 10/22/16.
 */
public class WebLoginModel implements LoginModel {
	
	private final int SALT_SIZE = 512;
	private final int HASH_ITERATIONS = 500_000;
	private StorageService service;
	private static final DigestRandomGenerator digestGenerator = new DigestRandomGenerator(new SHA3Digest(512));
	
	@Override
	public void init() {
		try {
			service = StorageService.getInstance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean authenticateUser(String username, String password) {
		DatabaseTable response = service.runSELECTquery(String.format("SELECT password FROM User WHERE username = '%s';", username));
		for(DatabaseEntry pword : response.getRows()) {
			if(pword.getAttribute("password", String.class).equals(password)) return true;
		}
		return false;
	}
	
	public void saveNewUser(String username, String password) {
		PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator();
		generator.init(PBEParametersGenerator.PKCS5PasswordToBytes(
				password.toCharArray()), generateSalt(SALT_SIZE), HASH_ITERATIONS);
		
	}
	
	private byte[] generateSalt(int size) {
		byte ret[] = new byte[size];
		digestGenerator.nextBytes(ret);
		return ret;
	}
	
	private boolean authenticateHelper(String username, char[] password) {
		DatabaseTable response = service.runSELECTquery(String.format("SELECT password FROM User WHERE username = '%s';", username));
		for(DatabaseEntry pword : response.getRows()) {
			
			if(pword.getAttribute("password", String.class).equals(password)) return true;
		}
		return false;
	}
}
