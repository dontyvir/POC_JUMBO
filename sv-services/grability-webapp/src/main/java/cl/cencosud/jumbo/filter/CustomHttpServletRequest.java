package cl.cencosud.jumbo.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CustomHttpServletRequest extends HttpServletRequestWrapper {

	private Map customHeaderMap = null;

	public CustomHttpServletRequest(HttpServletRequest request) {
		super(request);
		customHeaderMap = new HashMap();
	}

	public void addHeader(String name, String value) {
		customHeaderMap.put(name, value);
	}

	public String getHeader(String name) {
		String paramValue = super.getHeader(name); // query Strings
		if (paramValue == null) {
			paramValue = (String) customHeaderMap.get(name);
		}
		return paramValue;
	}

}
