package cl.bbr.common.framework;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ForwardParameters
{
	private Map params = null;
	
	public ForwardParameters()
	{
		params = new HashMap();
	}
	
	/**
	 * Add a single parameter and value.
	 *
	 * @param paramName     Parameter name
	 * @param paramValue    Parameter value
	 */
	public void add(String paramName, String paramValue)
	{
		String[] value = {paramValue};
		params.put(paramName, value);
	}
	
	/**
	 * Add a set of parameters and values.
	 *
	 * @param paramMap  Map containing parameter names and values.
	 *
	 */
	public void add(Map paramMap)
	{
		Iterator itr = paramMap.keySet().iterator();
		while (itr.hasNext())
		{
			String paramName = (String) itr.next();
			params.put(paramName, paramMap.get(paramName));
		}
		
	}
	
	/**
	 * Add parameters to a provided ActionForward.
	 *	 
	 *
	 * @return ActionForward with parameters added to the URL.
	 */
	public String forward()
	{
		String result = "";
		
	    Iterator it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        String nombre = (String) pairs.getKey();
	        String[] valor = (String[]) pairs.getValue();
	        
	        for ( int i=0; i<valor.length; i++){
	        	result += "&"+nombre + "=" + valor[i];
	        }
	    }

	    if ( result.length() > 0 ){
	    	result = "?" + result.substring(1);
	    }
	    
	    return result;
	    
	}
	
}
