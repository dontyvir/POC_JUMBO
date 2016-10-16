package cl.jumbo.ventaondemand.utils;

import java.io.IOException;
import java.util.Properties;
//import java.util.ResourceBundle;

public class PropertyUtil {
	
	private static Properties prop;
	
	private PropertyUtil() { }
	
	private static class SingletonHolder {
		private final static PropertyUtil INSTANCE = new PropertyUtil();
	}
	
	public static PropertyUtil getInstance() {
		return SingletonHolder.INSTANCE;
	}
		
	public Properties getProperties(){
		java.io.InputStream log4jProps = null;
		try {
			if(prop==null){
				log4jProps = this.getClass().getClassLoader().getResourceAsStream("/onDemand.properties");
				prop = new Properties();
				prop.load(log4jProps);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
            	if(log4jProps!=null){
            		log4jProps.close();
            		log4jProps = null;
            	}
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
		return prop;
	}	
}
