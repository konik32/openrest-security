package openrest.security.config;

import java.util.List;

import openrest.domain.PartTreeSpecificationBuilder;
import openrest.query.StaticFilterFactory;
import openrest.security.response.filter.ContextFilterFactory;
import openrest.security.response.filter.ContextFilterProvider;
import openrest.security.response.filter.RequestBasedFilterIntrospector;
import openrest.security.response.filter.SimpleSecurityExpressionHandler;
import openrest.security.response.filter.SpelFilterSecurityExpressionHandler;
import openrest.security.response.filter.SpelMultiplePropertyFilter;
import openrest.security.staticfilter.StaticFilterSecurityBasedManager;
import openrest.webmvc.ParsedRequestFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ser.FilterProvider;

@Configuration
public class OpenRestSecurityConfiguration {

	@Autowired(required = false)
	private List<PermissionEvaluator> permissionEvaluators;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired(required = false)
	private RoleHierarchy roleHierarchy;

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public ContextFilterFactory contextFilterFactory() {
		ContextFilterFactory factory = new ContextFilterFactory();
		factory.setApplicationContext(applicationContext);
		return factory;
	}

	@Bean
	public FilterProvider boostFilterProvider() {
		ContextFilterProvider provider = new ContextFilterProvider();
		provider.addContextFilter(SpelMultiplePropertyFilter.FILTER_ID, SpelMultiplePropertyFilter.class);
		return provider;
	}

	@Bean
	public RequestBasedFilterIntrospector introspector() {
		return new RequestBasedFilterIntrospector();
	}

	@Bean
	public SpelFilterSecurityExpressionHandler spelFilterSecurityExpressionHandler() {
		Assert.notNull(applicationContext);
		SpelFilterSecurityExpressionHandler expressionHandler = new SpelFilterSecurityExpressionHandler();
		if (permissionEvaluators != null && permissionEvaluators.size() == 1)
			expressionHandler.setPermissionEvaluator(permissionEvaluators.get(0));
		expressionHandler.setApplicationContext(applicationContext);
		expressionHandler.setRoleHierarchy(roleHierarchy);
		return expressionHandler;

	}

	@Bean
	public SimpleSecurityExpressionHandler simpleSecurityExpressionHandler() {
		Assert.notNull(applicationContext);
		SimpleSecurityExpressionHandler expressionHandler = new SimpleSecurityExpressionHandler();
		if (permissionEvaluators != null && permissionEvaluators.size() == 1)
			expressionHandler.setPermissionEvaluator(permissionEvaluators.get(0));
		expressionHandler.setApplicationContext(applicationContext);
		expressionHandler.setRoleHierarchy(roleHierarchy);
		return expressionHandler;

	}

	@Bean
	public StaticFilterSecurityBasedManager staticFilterSecurityBasedManager() {
		return new StaticFilterSecurityBasedManager(simpleSecurityExpressionHandler());
	}

	@Autowired
	public void setStaticFilterSecurityBasedManager(StaticFilterFactory factory) {
		factory.setInclusionManager(staticFilterSecurityBasedManager());
	}

	@Autowired
	public void setStaticFilterSecurityBasedManager(ParsedRequestFactory parsedRequestFactory) {
		parsedRequestFactory.setParameterProcessor(staticFilterSecurityBasedManager());
	}
}
