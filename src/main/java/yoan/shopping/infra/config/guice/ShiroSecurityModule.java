/**
 * 
 */
package yoan.shopping.infra.config.guice;

import javax.servlet.ServletContext;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.guice.web.ShiroWebModule;

import yoan.shopping.authentication.realm.UserRealm;

import com.google.inject.name.Names;

/**
 * Guice module to configure Shiro
 * @author yoan
 */
public class ShiroSecurityModule extends ShiroWebModule {
	public static final String NO_SECURITY = "NO_SECURITY";
	public static final String SHA256 = "SHA-256";
	public static final int NB_HASH_ITERATION = 2;
	
	public ShiroSecurityModule(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void configureShiroWeb() {
		bind(CacheManager.class).to(MemoryConstrainedCacheManager.class);
		//using expose because it is a private module
		expose(CacheManager.class);
		
		bind(HashedCredentialsMatcher.class).annotatedWith(Names.named(SHA256)).toInstance(getHashedCredentialsMatcher(SHA256));
		expose(HashedCredentialsMatcher.class).annotatedWith(Names.named(SHA256));
		
		bind(HashedCredentialsMatcher.class).annotatedWith(Names.named(NO_SECURITY)).toInstance(getNoSecurityCredentialsMatcher());
		expose(HashedCredentialsMatcher.class).annotatedWith(Names.named(NO_SECURITY));
		
		bindRealm().to(UserRealm.class);
		
		addFilterChain("/rest/api/**", config(NO_SESSION_CREATION, "true"), AUTHC_BASIC);
	}
	
	private HashedCredentialsMatcher getHashedCredentialsMatcher(String algorithmName) {
		HashedCredentialsMatcher credentialMatcher = new HashedCredentialsMatcher(algorithmName);
		credentialMatcher.setHashIterations(NB_HASH_ITERATION);
		return credentialMatcher;
	}
	
	/**
	 * /!\ local dev usage only /!\
	 * Allow to use API without security
	 */
	private HashedCredentialsMatcher getNoSecurityCredentialsMatcher() {
		HashedCredentialsMatcher credentialMatcher = new HashedCredentialsMatcher(SHA256) {
			@Override
			public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
				return true;
			}
		};
		return credentialMatcher;
	}
}