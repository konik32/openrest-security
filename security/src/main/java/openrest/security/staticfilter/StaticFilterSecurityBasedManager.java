package openrest.security.staticfilter;

import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import openrest.domain.ParameterProcessor;
import openrest.httpquery.parser.TempPart;
import openrest.query.StaticFilterInclusionManager;
import openrest.query.StaticFilterWrapper;
import openrest.security.response.filter.SimpleSecurityExpressionHandler;

public class StaticFilterSecurityBasedManager implements StaticFilterInclusionManager, ParameterProcessor {

	private final SimpleSecurityExpressionHandler expressionHandler;
	private final ExpressionParser parser;

	public StaticFilterSecurityBasedManager(SimpleSecurityExpressionHandler expressionHandler) {
		Assert.notNull(expressionHandler);
		this.expressionHandler = expressionHandler;
		parser = expressionHandler.getExpressionParser();
	}

	private void processRecursively(TempPart part, EvaluationContext ctx) {
		if (part.getType().equals(TempPart.Type.LEAF))
			processTempPart(part, ctx);
		else
			for (TempPart tp : part.getParts()) {
				processRecursively(tp, ctx);
			}
	}

	private void processTempPart(TempPart part, EvaluationContext ctx) {

	}

	public boolean includeFilter(StaticFilterWrapper filterWrapper) {
		if (filterWrapper.getCondition().isEmpty())
			return true;
		return ExpressionUtils.evaluateAsBoolean(parser.parseExpression(filterWrapper.getCondition()), getContext());
	}

	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private EvaluationContext getContext() {
		return expressionHandler.createEvaluationContext(getAuthentication(), null);
	}

	@Override
	public String processFParam(String parameter) {
		return parser.parseExpression(parameter).getValue(getContext(), String.class);
	}

}
