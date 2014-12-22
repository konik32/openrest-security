package openrest.security.response.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ContextFilterFactory implements ApplicationContextAware {

	@Autowired
	private HttpServletRequest request;
	private transient AutowireCapableBeanFactory beanFactory;

	private Map<Integer, AbstractContextFilter> filters;

	private void put(Integer hashCode, AbstractContextFilter filter) {
		if (filters == null)
			filters = new HashMap<Integer, AbstractContextFilter>();
		filters.put(hashCode, filter);
	}

	public AbstractContextFilter get(Object valueToFilter, Class<? extends AbstractContextFilter> filterType) {
		int hashCode = valueToFilter.hashCode();
		if (filters != null && filters.containsKey(hashCode)) {
			return filters.get(hashCode);
		} else {
			AbstractContextFilter filter;
			try {
				filter = filterType.newInstance();
				beanFactory.autowireBean(filter);
				filter.prepare(request, valueToFilter);
				put(hashCode, filter);
				return filter;
			} catch (InstantiationException e) {
				throw new IllegalStateException(e);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			}

		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		beanFactory = applicationContext.getAutowireCapableBeanFactory();
	}

}
