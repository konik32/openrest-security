package openrest.security.response.filter;

import javax.servlet.http.HttpServletRequest;

import lombok.Data;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

@Data
public class SpelFilterSecurityExpressionRoot extends SecurityExpressionRoot {

	public SpelFilterSecurityExpressionRoot(Authentication authentication, Object filteredObject, HttpServletRequest request) {
		super(authentication);
		this.filteredObject = filteredObject;
		this.request = request;
		// TODO Auto-generated constructor stub
	}

	private final Object filteredObject;
	private final HttpServletRequest request;

}
