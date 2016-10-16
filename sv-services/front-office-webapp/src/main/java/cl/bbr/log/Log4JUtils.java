/*
 * Creado el 16/08/2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author EAvendanoA
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class Log4JUtils {
	
	
	public static void initializeLog4J() {
		
		/*
		 * for logj4_app
		 */
		InputStream log4jProps = Log4JUtils.class.getResourceAsStream("/log4j_app.properties");
		
		Properties prop = new Properties();
		try {
			prop.load(log4jProps);
			PropertyConfigurator.configure(prop);			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
	}

}
