package openrest.security.response.filter;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import lombok.Data;

@Data
public class SpelFilterContextWrapper {
	private final HttpServletRequest request;
	private final Object filteredObject;
}
