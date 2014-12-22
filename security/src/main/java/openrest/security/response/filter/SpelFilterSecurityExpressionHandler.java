package openrest.security.response.filter;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

public class SpelFilterSecurityExpressionHandler extends SimpleSecurityExpressionHandler {

	@Override
	protected SecurityExpressionRoot getRoot(Authentication authentication, Object object) {
		SpelFilterContextWrapper wrapper = (SpelFilterContextWrapper) object;
		return new SpelFilterSecurityExpressionRoot(authentication, wrapper.getFilteredObject(), wrapper.getRequest());
	}

}
