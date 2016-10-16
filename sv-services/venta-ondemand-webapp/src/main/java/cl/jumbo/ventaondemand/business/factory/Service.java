package cl.jumbo.ventaondemand.business.factory;

import java.util.HashMap;
import java.util.Map;

import cl.jumbo.ventaondemand.business.logic.impl.ClienteLogicImpl;
import cl.jumbo.ventaondemand.business.logic.impl.ProductoLogicImpl;

public class Service {

	static Map components = initMap();
	
	private static Map initMap(){
		components = new HashMap();
		components.put("cliente", new ClienteLogicImpl());
		components.put("producto", new ProductoLogicImpl());

		return components;
	}

	public static Object getComponet(String componentName){
		return components.get(componentName);
	}
}
