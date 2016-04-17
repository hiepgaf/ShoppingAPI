/**
 * 
 */
package yoan.shopping.infra.config.guice;


import java.util.Map;

import org.apache.shiro.guice.web.GuiceShiroFilter;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import com.thetransactioncompany.cors.CORSFilter;

import yoan.shopping.infra.config.filter.RequestScopeFilter;
import yoan.shopping.user.User;

/**
 * Guice module to configure servlet
 * @author yoan
 */
public class ShoppingWebModule extends ServletModule {
	public static final String CONNECTED_USER = "connectedUser";
	
	@Override
	protected void configureServlets() {
		
		//using Resteasy servlet dispatcher
		bind(HttpServletDispatcher.class).in(Singleton.class);
		serve("/rest").with(HttpServletDispatcher.class);
		serve("/rest/*").with(HttpServletDispatcher.class);
		
		//allow CORS
		bind(CORSFilter.class).in(Singleton.class);
		Map<String, String> corsInitParams = ImmutableMap.of("cors.supportedMethods", "GET, POST, HEAD, PUT, DELETE, OPTIONS");
		filter("/", "/*").through(CORSFilter.class, corsInitParams);
		
		filter("/rest/api", "/rest/api/*", "/rest/auth", "/rest/auth/*", "/rest/public", "/rest/public/*").through(GuiceShiroFilter.class);
		
		//filtering to authenticate the current user
		filter("/rest/api", "/rest/api/*", "/rest/auth/authorization").through(RequestScopeFilter.class);
	}
	
	@Provides
	@Named(CONNECTED_USER)
	@RequestScoped
	User provideConnectedUser() {
		throw new IllegalStateException("Connected user should have been seeded by an authentication filter");
	}
}
