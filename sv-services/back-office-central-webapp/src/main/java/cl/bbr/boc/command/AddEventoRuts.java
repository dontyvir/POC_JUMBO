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
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.dto.RutEventoDTO;
import cl.bbr.jumbocl.eventos.utils.EventosConstants;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

import com.oreilly.servlet.MultipartRequest;

/**
 * Agrega un nuevo evento
 * 
 * @author imoyano
 */

public class AddEventoRuts extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res,
            UserDTO usr) throws Exception {
        logger.debug("Comienzo AddEventoRuts Execute");

        String paramUrl = "";
        
        String TplFile = getServletConfig().getInitParameter("TplFile");
        String rutaUpload = req.getSession().getServletContext().getRealPath("upload_ruts_eventos");
        logger.debug("rutaUpload:" + rutaUpload);
        
        logger.debug("Eliminamos archivos antiguos");
        File directorioDestino = new File(rutaUpload);
        EventosUtil.eliminarArchivosAntiguos(directorioDestino);
        
        MultipartRequest multi = new MultipartRequest(req, rutaUpload, 10000000);
        
        long idEvento = 0;
        if (multi.getParameter("id_evento") != null) {
            idEvento = Long.parseLong(multi.getParameter("id_evento"));
        }
        
        BizDelegate biz = new BizDelegate();
        ForwardParameters fp = new ForwardParameters();
        fp.add(req.getParameterMap());

        EventoDTO evento = biz.getEvento(idEvento);
        
        File archivo = multi.getFile("upFile");
        
        if (archivo == null) {
            logger.error("Existió un error al cargar los ruts. No se especificó el archivo.");
            res.sendRedirect( cmd_dsp_error +"?mensaje="+EventosConstants.MSJ_RUT_EVENTO_SIN_ARCHIVO+"&PagErr=1");
            return;
        }

        if (archivo.getName().length() > 4) {
            if (!archivo.getName().substring(archivo.getName().length()-4,archivo.getName().length()).equalsIgnoreCase(".xls")) {
	            logger.error("Existió un error al cargar los ruts. No es un archivo XLS.");
	            res.sendRedirect( cmd_dsp_error +"?mensaje="+EventosConstants.MSJ_RUT_EVENTO_SIN_ARCHIVO+"&PagErr=1");
	            return;
            }
        } else {
            logger.error("Existió un error al cargar los ruts. Archivo erroneo.");
            res.sendRedirect( cmd_dsp_error +"?mensaje="+EventosConstants.MSJ_RUT_EVENTO_ARCHIVO_EXTENSION+"&PagErr=1");
            return;
        }
        
        File archivoNuevo = File.createTempFile("ruts_eventos_", ".xls", directorioDestino);
        EventosUtil.copy(archivo, archivoNuevo);
        archivo.delete();
               
        String respuesta = asociaRuts(archivoNuevo, evento, biz);   
        
        fp.add("mensaje", respuesta);
        fp.add("cargo_rut", "SI");
        fp.add("id_evento", String.valueOf(idEvento));
        
        paramUrl = TplFile + fp.forward();
        
        logger.debug("Redireccionando a: " + paramUrl);
        logger.debug("Fin AddEventoRuts Execute");
        res.sendRedirect(paramUrl);
    }

     /**
     * @param archivo
     * @return
     */
    private String asociaRuts(File archivo, EventoDTO evento, BizDelegate biz) {
        String mensaje = "";
        Vector ruts = new Vector();
        
        long registros = 0;
        
        Planilla planilla = new Planilla();
        
        try {
            /* creamos un HSSFWorkbook con nuestro excel */
            HSSFWorkbook xls = planilla.getWorkbook(archivo.getAbsolutePath());
            /* Elegimos la hoja que queremos leer del excel */
            HSSFSheet sheet = xls.getSheetAt(0);
            
            /* Nos recorremos las filas */
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                /* Cogemos una fila HSSFRow row=sheet.getRow(fila); */
                RutEventoDTO re = new RutEventoDTO();
                long rut = 0;
                char dv = 'A';
                try {
                    HSSFRow row = sheet.getRow(j);
                                    
                    HSSFCell colRut = row.getCell(0); //Indicamos q columna hay q leer
                                    
                    rut = Long.parseLong( Long.toString((long)colRut.getNumericCellValue()) );
                    re.setCliRut(rut);
                    
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
                    
                    if (EventosUtil.validaRut(String.valueOf(rut), dv)) {
                        re.setCliDv( String.valueOf(dv) );
                        ruts.add(re);
                        
                        registros++;
                        
                    } else {
                        mensaje += "<font color=red>- El RUT ingresado en la fila " + (j + 1) + " no es válido</font><br>";
                    }
                    
                } catch (Exception e) {
                    mensaje += "<font color=red>- Error en el RUT ingresado en la fila " + (j + 1) + "</font><br>";
                    
                }            
            }
            
            mensaje += "<br>";
            
            try {
                mensaje += biz.addRutsEvento(ruts, evento);
                
            } catch (Exception e) {
                mensaje += "<font color=red>- Ocurrió un error al cargar los Rut's.</font>";
                
            }            
        
        } catch (Exception e) {
            mensaje += "<font color=red>- Ocurrió un error al cargar los Rut's. El archivo utilizado debe ser como el del ejemplo.</font>";
            
        }
        
        logger.debug("mensaje:" + mensaje);
        
        return mensaje;
    }

    /**
     * Como HSSFCell de POI no nos entrega el valor de un long, se hizo este truquito 
     * 
     * @param rutStr Rut de tipo string de la forma: 555888.0 o 9.9888777E7
     * @return
     */
    private long obtieneRut(String rutStr) {
        String newRut = "";
        if (rutStr.length() > 3) {
            newRut = rutStr.substring(0,rutStr.length()-2); 
        }
        rutStr = newRut.toString().replaceAll("\\.","");
        return 0;
    }
    
}
