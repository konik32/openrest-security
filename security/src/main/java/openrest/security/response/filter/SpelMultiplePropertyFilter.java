package openrest.security.response.filter;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpelMultiplePropertyFilter extends AbstractContextFilter {

	public static final String FILTER_ID = "spelMultiplePropertyFilter";

	@Autowired
	private SpelFilterSecurityExpressionHandler expressionHandler;

	private boolean getSpelValue(Object valueToFilter, HttpServletRequest request, String spelString) {

		ExpressionParser parser = expressionHandler.getExpressionParser();
		Expression expr = parser.parseExpression(spelString);
		EvaluationContext ctx = expressionHandler.createEvaluationContext(SecurityContextHolder.getContext().getAuthentication(), new SpelFilterContextWrapper(
				request, valueToFilter));
		return ExpressionUtils.evaluateAsBoolean(expr, ctx);
	}

	public void prepare(HttpServletRequest request, Object valueToFilter) {
		SpelFilter ep = AnnotationUtils.findAnnotation(valueToFilter.getClass(), SpelFilter.class);
		String spelString = ep.value();
		if (!getSpelValue(valueToFilter, request, spelString)) {
			propertiesToIgnore = new HashSet<String>(ep.properties().length);
			propertiesToIgnore.addAll(Arrays.asList(ep.properties()));
		}
	}

}
