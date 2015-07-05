/**
 * 
 */
package yoan.shopping.user.repository;

import static yoan.shopping.infra.rest.error.Level.ERROR;
import static yoan.shopping.infra.util.error.CommonErrorCode.APPLICATION_ERROR;
import static yoan.shopping.user.repository.UserRepositoryErrorMessage.PROBLEM_PASSWORD_VALIDITY;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yoan.shopping.infra.config.guice.ShiroSecurityModule;
import yoan.shopping.infra.util.error.ApplicationException;
import yoan.shopping.user.SecuredUser;
import yoan.shopping.user.User;

/**
 * User repository focused on security information
 * @author yoan
 */
public abstract class SecuredUserRepository {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(SecuredUserRepository.class);
	
	/**
	 * Create a new User
	 * @param userToCreate
	 */
	public void create(User userToCreate, String password) {
		if (userToCreate == null) {
			LOGGER.warn("Secured user creation asked with null user");
			return;
		}
		ensurePasswordValidity(password);
		
		SecuredUser securedUserToCreate = generateSecuredUser(userToCreate, password);
		processCreate(securedUserToCreate);
	}
	
	private SecuredUser generateSecuredUser(User userToCreate, String password) {
		Object salt = generateSalt();
		String hashedPassword = hashPassword(password, salt);
		return SecuredUser.Builder.createFrom(userToCreate)
								   .withPassword(hashedPassword)
								   .withSalt(salt)
								   .build();
	}
	
	protected Object generateSalt() {
		return UUID.randomUUID().toString();
	}
	
	protected void ensurePasswordValidity(String password) {
		if (!checkPasswordValidity(password)) {
			String message = PROBLEM_PASSWORD_VALIDITY.getDevReadableMessage();
			LOGGER.error(message);
			throw new ApplicationException(ERROR, APPLICATION_ERROR, message);
		}
	}
	
	protected boolean checkPasswordValidity(String password) {
		return StringUtils.isNotBlank(password);
	}
	
	public String hashPassword(String password, Object salt) {
		return new Sha256Hash(password, salt, ShiroSecurityModule.NB_HASH_ITERATION).toBase64();
	}
	
	/**
	 * Get a user by its Id
	 * @param userId
	 */
	public final SecuredUser getById(UUID userId) {
		if (userId == null) {
			return null;
		}
		return processGetById(userId);
	}
	
	/**
	 * Create a new SecuredUser
	 * @param userToCreate
	 */
	protected abstract void processCreate(SecuredUser userToCreate);
	
	/**
	 * Get a user by its Id
	 * @param userId
	 */
	protected abstract SecuredUser processGetById(UUID userId);
}