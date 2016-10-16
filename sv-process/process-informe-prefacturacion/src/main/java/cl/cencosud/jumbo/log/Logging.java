package cl.cencosud.jumbo.log;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Logging {
    
    static {
        
        try {
            DOMConfigurator.configure("log4j.xml");
        } catch (Exception e) {
            System.out.println("Error configuring log4j.xml "+e.getMessage());
        }
        
    }

    private String clase = "---";
    private Logger logger;

    public Logging(Object obj) {

        this.clase = obj.getClass().getName();        
        this.logger = Logger.getLogger(this.clase);
    }

    public void debug( String texto ) {

        this.logger.debug(texto);
    }

    public void info( String texto ) {

        this.logger.info(texto);
    }

    public void warn( String texto ) {

        this.logger.warn(texto);
    }

    public void fatal( String texto ) {

        this.logger.fatal(texto);
    }

    public void error( String texto ) {

        this.logger.error(texto);
    }

    public void error( Exception ex ) {

        this.logger.error(ex.getMessage());
    }

    public void error( String mensaje, Exception ex ) {

        this.logger.error(mensaje + " --> " + ex.getMessage());
    }

    public void inicio_comando() {

        this.logger.info("---------------------------------------");
        this.logger.info("Inicio comando: " + getClass().getDeclaringClass().getName());
        this.logger.info("---------------------------------------");
    }

    public void inicio_comando( Object obj ) {

        this.logger.info("---------------------------------------");
        this.logger.info("Inicio comando: " + obj.getClass().getName());
        this.logger.info("---------------------------------------");
    }

    public void fin_comando() {

        this.logger.info("---------------------------------------");
        this.logger.info("Fin comando: " + getClass().getDeclaringClass().getName());
        this.logger.info("---------------------------------------");
    }

    public void fin_comando( Object obj ) {

        this.logger.info("---------------------------------------");
        this.logger.info("Fin comando: " + obj.getClass().getName());
        this.logger.info("---------------------------------------");
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.log.Logging JD-Core Version: 0.6.0
 */