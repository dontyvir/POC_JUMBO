package cl.cencosud.util;

import org.apache.log4j.PropertyConfigurator;

public class LogUtil {

    public static void initLog4J() {

        try {
        	
        	
        	//String file = getInitParameter("log4j-init-file");
            //DOMConfigurator.configure("log4j.xml");
            PropertyConfigurator.configure("log4j.properties");
        } catch (Exception e) {
            System.out.println("No se pudo inicializar log4j:" + e.getMessage());
        }
    }

}
