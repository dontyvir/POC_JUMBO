package cl.cencosud.jumbo.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.chainsaw.Main;

public class Logging {

	private String clase = "---";
	private Logger logger;

	public Logging(Object obj) {
		clase = obj.getClass().getName();
		
		PropertyConfigurator.configure(Main.class.getClassLoader()
				.getResource("log4j.properties"));
		logger = Logger.getLogger(clase);
	}

	public void debug(String texto) {
		logger.debug(texto);
	}

	public void info(String texto) {
		logger.info(texto);
	}

	public void warn(String texto) {
		logger.warn(texto);
	}

	public void fatal(String texto) {
		logger.fatal(texto);
	}

	public void error(String texto) {
		logger.error(texto);
	}

	public void error(Exception ex) {
		logger.error(ex.getMessage());
	}

	public void error(String mensaje, Exception ex) {
		logger.error(mensaje + " --> " + ex.getMessage());
	}

	public void inicio_comando() {
		logger.info("---------------------------------------");
		logger.info("Inicio comando: "
				+ getClass().getDeclaringClass().getName());
		logger.info("---------------------------------------");
	}

	public void inicio_comando(Object obj) {
		logger.info("---------------------------------------");
		logger.info("Inicio comando: " + obj.getClass().getName());
		logger.info("---------------------------------------");
	}

	public void fin_comando() {
		logger.info("---------------------------------------");
		logger.info("Fin comando: " + getClass().getDeclaringClass().getName());
		logger.info("---------------------------------------");
	}

	public void fin_comando(Object obj) {
		logger.info("---------------------------------------");
		logger.info("Fin comando: " + obj.getClass().getName());
		logger.info("---------------------------------------");
	}
}
