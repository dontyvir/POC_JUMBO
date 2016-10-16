package cl.cencosud.util;

import java.net.URL;

import org.apache.log4j.PropertyConfigurator;

public class LogUtil {

    public static void initLog4J() {

        try {
        	//String file = getInitParameter("log4j-init-file");
            //DOMConfigurator.configure("log4j.xml");           
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
    		URL url = loader.getResource("log4j.properties");
    		PropertyConfigurator.configure(url);
        } catch (Exception e) {
            System.out.println("No se pudo inicializar log4j:" + e.getMessage());
        }
    }
}
