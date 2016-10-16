package cl.bbr.boc.command;

import java.io.File;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.utils.Planilla;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.clientes.dto.RutConfiableDTO;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

import com.oreilly.servlet.MultipartRequest;

/**
 * Agrega los ruts de clientes confiables
 * 
 * @author imoyano
 */

public class AddRutsConfiables extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res,
            UserDTO usr) throws Exception {
        logger.debug("Comienzo AddRutsConfiables Execute");

        String paramUrl = "";
        
        String TplFile = getServletConfig().getInitParameter("TplFile");
        String rutaUpload = req.getSession().getServletContext().getRealPath("upload_ruts_eventos");
        logger.debug("rutaUpload:" + rutaUpload);
        
        logger.debug("Eliminamos archivos antiguos");
        File directorioDestino = new File(rutaUpload);
        EventosUtil.eliminarArchivosAntiguos(directorioDestino);
        
        MultipartRequest multi = new MultipartRequest(req, rutaUpload, 10000000);
        
        BizDelegate biz = new BizDelegate();
        ForwardParameters fp = new ForwardParameters();
        fp.add(req.getParameterMap());
        
        File archivo = multi.getFile("upFile");
        
        if (archivo == null) {
            logger.error("Existió un error al cargar los ruts. No se especificó el archivo.");
            res.sendRedirect( cmd_dsp_error +"?mensaje=Existió un error al cargar los ruts. No se especificó el archivo.&PagErr=1");
            return;
        }

        if (archivo.getName().length() > 4) {
            if (!archivo.getName().substring(archivo.getName().length()-4,archivo.getName().length()).equalsIgnoreCase(".xls")) {
	            logger.error("Existió un error al cargar los ruts. No es un archivo XLS.");
	            res.sendRedirect( cmd_dsp_error +"?mensaje=Existió un error al cargar los ruts. No es un archivo XLS.&PagErr=1");
	            return;
            }
        } else {
            logger.error("Existió un error al cargar los ruts. Archivo erroneo.");
            res.sendRedirect( cmd_dsp_error +"?mensaje=Existió un error al cargar los ruts. Archivo erroneo.&PagErr=1");
            return;
        }
        
        File archivoNuevo = File.createTempFile("ruts_confiables_", ".xls", directorioDestino);
        EventosUtil.copy(archivo, archivoNuevo);
        archivo.delete();
               
        String respuesta = asociaRuts(archivoNuevo, biz, usr);   
        
        fp.add("mensaje", respuesta);
        fp.add("cargo_rut", "SI");
        
        paramUrl = TplFile + fp.forward();
        
        logger.debug("Redireccionando a: " + paramUrl);
        logger.debug("Fin AddRutsConfiables Execute");
        res.sendRedirect(paramUrl);
    }

     /**
     * @param archivo
     * @return
     */
    private String asociaRuts(File archivo, BizDelegate biz, UserDTO usr) {
        String mensaje = "";
        Vector ruts = new Vector();        
        Planilla planilla = new Planilla();        
        try {
            /* creamos un HSSFWorkbook con nuestro excel */
            HSSFWorkbook xls = planilla.getWorkbook(archivo.getAbsolutePath());
            /* Elegimos la hoja que queremos leer del excel */
            HSSFSheet sheet = xls.getSheetAt(0);
            
            /* Nos recorremos las filas */
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                /* Cogemos una fila HSSFRow row=sheet.getRow(fila); */
                RutConfiableDTO rc = new RutConfiableDTO();
                long rut = 0;
                char dv = 'A';
                try {
                    HSSFRow row = sheet.getRow(j);
                                    
                    HSSFCell colRut = row.getCell(0); //Indicamos q columna hay q leer
                                    
                    rut = Long.parseLong( Long.toString((long)colRut.getNumericCellValue()) );
                    rc.setRut(rut);
                    
                    HSSFCell colDv = row.getCell(1); //Indicamos q columna hay q leer
                    if (colDv.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        String sDv = String.valueOf( (int) Double.parseDouble( String.valueOf( colDv.getNumericCellValue() ) ) );
                        if (sDv.length() == 1) {
                            dv = sDv.charAt(0);
                        }
                        
                    } else if (colDv.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        dv = (char) colDv.toString().charAt(0);
                        if (dv == 'k') {
                            dv = 'K';
                        }                        
                    }
                    
                    rc.setDv( String.valueOf(dv) );
                    ruts.add(rc);
                        
                    
                    
                } catch (Exception e) {
                    //mensaje += "<font color=red>Error en el RUT ingresado en la fila " + (j + 1) + "</font><br>";
                    logger.error("Error en el RUT ingresado en la fila " + (j + 1));
                }            
            }
            
            try {
                biz.addRutsConfiables(ruts);
                mensaje = "Se cargaron " + ruts.size() + " Rut de clientes.";
                
            } catch (Exception e) {
                mensaje = "Error al cargar los Rut de clientes";
            }
            
            try {
                String user = usr.getLogin();
                String nombre = usr.getNombre() + " " + usr.getApe_paterno();
                biz.addLogRutConfiables(user, nombre, mensaje);
                
            } catch (Exception e) {
                logger.error("Error al llenar el LOG de clientes confiables:" + e.getMessage());
            }
        
        } catch (Exception e) {
            mensaje += "<font color=red>- Ocurrió un error al cargar los Rut's. El archivo utilizado debe ser como el del ejemplo.</font>";
            
        }
        
        logger.debug("mensaje:" + mensaje);
        
        return mensaje;
    }
    
}
