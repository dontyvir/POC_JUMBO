package cl.cencosud.jumbo.home;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.cencosud.jumbo.beans.BeanLocal;
import cl.cencosud.jumbo.beans.BeanParamConfig;
import cl.cencosud.jumbo.log.Logging;

public class Facturacion {

    private final String CONFIG_BUNDLE_NAME = "Config";
    PropertyResourceBundle configBundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle(CONFIG_BUNDLE_NAME);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS");
    private final SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM yyyy");

    Logging logger = new Logging(this);

    BeanParamConfig getParamProperties() {        

        BeanParamConfig param = new BeanParamConfig();
        param.setNombreArchivo(this.configBundle.getString("NombreArchivo"));

        param.setPuerto(this.configBundle.getString("mail.puerto"));
        param.setServer(this.configBundle.getString("mail.server"));
        param.setFrom(this.configBundle.getString("mail.from"));
        param.setTo(parseaParam(this.configBundle.getString("mail.to")));
        param.setCc(parseaParam(this.configBundle.getString("mail.cc")));
        param.setCco(parseaParam(this.configBundle.getString("mail.cco")));
        param.setSubject(this.configBundle.getString("mail.subject"));
        param.setMensaje(this.configBundle.getString("mail.mensaje"));
        param.setEliminarArchivosTemporales(this.configBundle.getString("EliminarArchivosTemporales"));

        return param;
    }

    private Map parseaParam( String listaHorarios ) {

        Map parametros = new LinkedHashMap();
        for ( StringTokenizer stringtokenizer = new StringTokenizer(listaHorarios, ",", false); stringtokenizer.hasMoreTokens(); ) {
            String valor = stringtokenizer.nextToken();
            parametros.put(valor, valor);
        }
        return parametros;
    }

    public void inicia() {
       
        try {
            Home home = Home.getHome();
            BeanParamConfig param = getParamProperties();

            logger.debug("\n\nFecha y Hora de Ejecución del Informe de S&F: " + this.dateFormat.format(new Date()));
            String Path = "";
            try {
                File fd = new File("temp");
                Path = fd.getCanonicalPath() + "/";
                logger.debug("Path (temp):" + Path);
            } catch (IOException e) {
                logger.error("Error inicializando file "+e.getMessage());
            }
            if ( param.getEliminarArchivosTemporales().equalsIgnoreCase("S") ) {
                home.EliminaArchivosXLS(Path);
            }

            Calendar cal = Calendar.getInstance();
            cal.add(5, -1);
            int diasMes = cal.getActualMaximum(5);
            int dia = cal.get(5);

            logger.debug("Fecha: " + this.dateFormat.format(cal.getTime()));

            List loc = home.listadoLocales();
            for ( int i = 0; i < loc.size(); i++ ) {
                BeanLocal local = (BeanLocal) loc.get(i);

                String Fecha = this.dateFormat2.format(cal.getTime()).toUpperCase(new Locale("es", "ES", ""));

                if ( ( local.getId_local() == 2 ) || ( local.getId_local() == 3 ) ) {
                    if ( dia == diasMes )
                        Fecha = "2Q " + Fecha;
                    else if ( dia == 15 ) {
                        Fecha = "1Q " + Fecha;
                    }
                }

                String filename = param.getNombreArchivo() + local.getNom_local() + " (" + Fecha + ").xls";
                String RutaArchivoXLS = Path + filename;
                logger.debug("RutaArchivoXLS: " + RutaArchivoXLS);

                List ListadoOP = home.getListadoOP(cal, local.getId_local());
                if ( ( ListadoOP != null ) && ( ListadoOP.size() > 0 ) ) {
                    HSSFWorkbook wb = new HSSFWorkbook();
                    wb = home.JumboCL(wb, ListadoOP, local, param, cal);
                    wb = home.JumboVa(wb, ListadoOP, local, param, cal);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(RutaArchivoXLS);
                        wb.write(fos);
                        fos.close();
                    } catch (FileNotFoundException e) {
                        logger.error("ERROR: " + getClass() + ".Facturacion.inicia() : " + e, e);                        
                    } catch (IOException e) {
                        logger.error("ERROR: " + getClass() + ".Facturacion.inicia() : " + e, e);                        
                    } finally {
                        fos = null;
                    }

                    logger.debug("EXCEL generado con Exito.");
                    home.CreaZipXLS(Path, filename);
                    logger.debug("COMPRESION A ZIP generado con Exito.");
                }
            }

            HashMap archivos = home.RecuperaListaArchivos(Path);
            if ( ( archivos != null ) && ( archivos.size() > 0 ) ) {
                home.EnviaEMail(param, archivos);
                logger.debug("CORREO Enviado con Exito.");
            }
        } catch (Throwable theException) {
            logger.error("SchedulerTaskServlet.EjecutaTarea: ERROR:" + theException, (Exception)theException);            
        }
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.home.Facturacion JD-Core Version: 0.6.0
 */