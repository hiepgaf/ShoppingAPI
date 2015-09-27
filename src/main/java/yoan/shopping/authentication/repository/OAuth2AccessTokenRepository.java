package yoan.shopping.authentication.repository;

import static yoan.shopping.authentication.repository.OAuth2AccessTokenRepositoryErrorMessage.PROBLEM_INSERT_USER_ID_NULL;
import static yoan.shopping.authentication.repository.OAuth2AccessTokenRepositoryErrorMessage.PROBLEM_INVALID_ACCESS_TOKEN;
import static yoan.shopping.infra.logging.Markers.AUTHENTICATION;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * OAuth2 access token repository
 * @author yoan
 */
public abstract class OAuth2AccessTokenRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AuthorizationCodeRepository.class);
	private static final Marker AUTH_MARKER = AUTHENTICATION.getMarker();
	
	public UUID getUserIdByAccessToken(String accessToken) {
		if (StringUtils.isBlank(accessToken)) {
			LOGGER.error(AUTH_MARKER, PROBLEM_INVALID_ACCESS_TOKEN.getDevReadableMessage(accessToken, "searching user ID"));
			return null;
		}
		return processGetUserIdByAccessToken(accessToken);
	};
	
	public void insert(String accessToken, UUID userId) {
		if (StringUtils.isBlank(accessToken)) {
			LOGGER.error(AUTH_MARKER, PROBLEM_INVALID_ACCESS_TOKEN.getDevReadableMessage(accessToken, "inserting token for user : " + userId));
			return;
		}
		
		if (userId == null) {
			LOGGER.error(AUTH_MARKER, PROBLEM_INSERT_USER_ID_NULL.getDevReadableMessage(accessToken));
			return;
		}
		
		processInsert(accessToken, userId);
	}
	
	public void deleteByAccessToken(String accessToken) {
		if (StringUtils.isBlank(accessToken)) {
			LOGGER.error(AUTH_MARKER, PROBLEM_INVALID_ACCESS_TOKEN.getDevReadableMessage(accessToken, "deleting access token"));
			return;
		}
		
		processDeleteByAccessToken(accessToken);
	}
	
	protected abstract UUID processGetUserIdByAccessToken(String accessToken);
	
	protected abstract void processInsert(String accessToken, UUID userId);
	
	protected abstract void processDeleteByAccessToken(String accessToken);
}
