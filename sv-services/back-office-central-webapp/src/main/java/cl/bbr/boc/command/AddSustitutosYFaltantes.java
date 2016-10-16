package cl.bbr.boc.command;

import java.io.File;
import java.util.ArrayList;

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
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.informes.dto.OriginalesYSustitutosDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

import com.oreilly.servlet.MultipartRequest;

/**
 * Agrega un nuevo evento
 * 
 * @author imoyano
 */

public class AddSustitutosYFaltantes extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res,
            UserDTO usr) throws Exception {
        logger.debug("Comienzo AddSustitutosYFaltantes Execute");

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
            logger.error("Existió un error al cargar los códigos de productos. No se especificó el archivo.");
            res.sendRedirect( cmd_dsp_error +"?mensaje=No se especificó el archivo&PagErr=1");
            return;
        }

        if (archivo.getName().length() > 4) {
            if (!archivo.getName().substring(archivo.getName().length()-4,archivo.getName().length()).equalsIgnoreCase(".xls")) {
	            logger.error("Existió un error al cargar los códigos de productos. No es un archivo excel.");
	            res.sendRedirect( cmd_dsp_error +"?mensaje=No se especificó el archivo&PagErr=1");
	            return;
            }
        } else {
            logger.error("Existió un error al cargar los ruts. Archivo erroneo.");
            res.sendRedirect( cmd_dsp_error +"?mensaje=El archivo especificado no es válido&PagErr=1");
            return;
        }
        
        File archivoNuevo = File.createTempFile("sustitutos_", ".xls", directorioDestino);
        EventosUtil.copy(archivo, archivoNuevo);
        archivo.delete();
               
        String respuesta = actualizaSustitutos(archivoNuevo, biz);   
        
        fp.add("mensaje", respuesta);
        fp.add("cargo_syf", "SI");
        
        paramUrl = TplFile + fp.forward();
        
        logger.debug("Redireccionando a: " + paramUrl);
        logger.debug("Fin AddSustitutosYFaltantes Execute");
        res.sendRedirect(paramUrl);
    }

     /**
     * @param archivo
     * @return
     */
    private String actualizaSustitutos(File archivo, BizDelegate biz) {
        String mensaje = "";
        ArrayList sustitutos = new ArrayList();
        
        long registros = 0;
        int intentos = 0;
        
        Planilla planilla = new Planilla();
        
        try {
            /* creamos un HSSFWorkbook con nuestro excel */
            HSSFWorkbook xls = planilla.getWorkbook(archivo.getAbsolutePath());
            /* Elegimos la hoja que queremos leer del excel */
            HSSFSheet sheet = xls.getSheetAt(0);
            
            /* Nos recorremos las filas */
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                /* Cogemos una fila HSSFRow row=sheet.getRow(fila); */
                OriginalesYSustitutosDTO syf = new OriginalesYSustitutosDTO();

                try {
                    boolean valido = true;
                    
                    HSSFRow row = sheet.getRow(j);
                                    
                    HSSFCell colCodOri = row.getCell(0); //Indicamos q columna hay q leer                    
                    if (colCodOri.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        syf.setCodProdOriginal(String.valueOf( (int) Double.parseDouble( String.valueOf( colCodOri.getNumericCellValue() ))));
                    } else if (colCodOri.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        syf.setCodProdOriginal( colCodOri.toString() );
                    } else {
                        valido = false;
                    }
                    
                    HSSFCell colUniOri = row.getCell(1); //Indicamos q columna hay q leer                    
                    if (colUniOri.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        syf.setUniMedProdOriginal(String.valueOf( (int) Double.parseDouble( String.valueOf( colUniOri.getNumericCellValue() ))));
                    } else if (colUniOri.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        syf.setUniMedProdOriginal( colUniOri.toString() );
                    } else {   
                        valido = false;
                    }
                    
                    HSSFCell colCodSus = row.getCell(2); //Indicamos q columna hay q leer                    
                    if (colCodSus.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        syf.setCodProdSustituto(String.valueOf( (int) Double.parseDouble( String.valueOf( colCodSus.getNumericCellValue() ))));
                    } else if (colCodSus.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        syf.setCodProdSustituto( colCodSus.toString() );
                    } else {
                        valido = false;
                    }
                    
                    HSSFCell colUniSus = row.getCell(3); //Indicamos q columna hay q leer                    
                    if (colUniSus.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        syf.setUniMedProdSustituto(String.valueOf( (int) Double.parseDouble( String.valueOf( colUniSus.getNumericCellValue() ))));
                    } else if (colUniSus.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        syf.setUniMedProdSustituto( colUniSus.toString() );
                    } else {
                        valido = false;
                    }
                    
                    if (valido) {
                        sustitutos.add(syf);
                        registros++;
                    } else {
                        intentos++;
                        if (intentos >= 4) {
                            continue;
                        }
                        mensaje += "<font color=red>- Los códigos ingresados en la fila " + (j + 1) + " no son válidos</font><br>";
                    }                    
                    
                } catch (Exception e) {
                    intentos++;
                    if (intentos >= 4) {
                        continue;
                    }
                    mensaje += "<font color=red>- Error en los códigos ingresados en la fila " + (j + 1) + "</font><br>";
                    
                }            
            }
            
            mensaje += "<br>";
            
            try {
                mensaje += biz.addCodigosSyF(sustitutos);
                
            } catch (Exception e) {
                mensaje = "<font color=red>Ocurrió un error al cargar los códigos de Sustitutos.</font>";
                
            }            
        
        } catch (Exception e) {
            mensaje = "<font color=red>Ocurrió un error al cargar los códigos de Sustitutos. El archivo utilizado no es correcto.</font>";
        }
        logger.debug("mensaje:" + mensaje);
        
        return mensaje;
    }
    
}
