package cl.bbr.boc.command;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

import com.oreilly.servlet.MultipartRequest;

/**
 * Agrega un nuevo evento
 * 
 * @author imoyano
 */

public class CargaListasEspeciales extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res,
            UserDTO usr) throws Exception {
        logger.debug("Comienzo CargaListasEspeciales Execute");

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
            logger.error("Existió un error al cargar las listas. No se especificó el archivo.");
            res.sendRedirect( cmd_dsp_error +"?mensaje=No se pudieron cargar las listas (No existe archivo).");
            return;
        }

        if (archivo.getName().length() > 4) {
            if (!archivo.getName().substring(archivo.getName().length()-4,archivo.getName().length()).equalsIgnoreCase(".xls")) {
	            logger.error("Existió un error al cargar las listas. No es un archivo XLS.");
	            res.sendRedirect( cmd_dsp_error +"?mensaje=No se pudieron cargar las listas (No existe archivo XLS).");
	            return;
            }
        } else {
            logger.error("Existió un error al cargar las listas. Archivo erroneo.");
            res.sendRedirect( cmd_dsp_error +"?mensaje=Existió un error al cargar las listas. Archivo erroneo.");
            return;
        }
        
        File archivoNuevo = File.createTempFile("listas_especiales_", ".xls", directorioDestino);
        EventosUtil.copy(archivo, archivoNuevo);
        archivo.delete();
               
        String respuesta = cargaListas(archivoNuevo, biz);   
        
        fp.add("mensaje", respuesta);
        fp.add("cargo_listas", "SI");
        
        paramUrl = TplFile + fp.forward();
        
        logger.debug("Fin CargaListasEspeciales Execute");
        res.sendRedirect(paramUrl);
    }

     /**
     * @param archivo
     * @return
     */
    private String cargaListas(File archivo, BizDelegate biz) {
        String mensaje = "";
        Hashtable hash = new Hashtable();
        
        Planilla planilla = new Planilla();
        
        try {
            /* creamos un HSSFWorkbook con nuestro excel */
            HSSFWorkbook xls = planilla.getWorkbook(archivo.getAbsolutePath());
            /* Elegimos la hoja que queremos leer del excel */
            HSSFSheet sheet = xls.getSheetAt(0);
            
            /* Nos recorremos las filas */
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                /* Cogemos una fila HSSFRow row=sheet.getRow(fila); */
                try {
                    HSSFRow row = sheet.getRow(j);                                    
                    HSSFCell colAlias = row.getCell(0); //Indicamos q columna hay q leer
                    String alias = colAlias.toString();
                    alias = alias.length() < 64 ? alias : alias.substring(1, 63);
                    
                    List lista = (List) hash.get(alias);
                    if (lista == null)
                        lista = new ArrayList();
                    ProductosPedidoDTO producto = new ProductosPedidoDTO();
                    HSSFCell colProId = row.getCell(2); //Indicamos q columna hay q leer
                    producto.setId_producto((long)colProId.getNumericCellValue());
                    HSSFCell colCant = row.getCell(3); //Indicamos q columna hay q leer
                    producto.setCantidad(new BigDecimal(colCant.getNumericCellValue()));
                    lista.add(producto);
                    hash.put(alias, lista);
                    
                } catch (Exception e) {
                    mensaje += "<font color=red>- Error en el producto ingresado en la fila " + (j + 1) + "</font><br>";                    
                }            
            }            
            mensaje += "<br>";
            
            try {
                mensaje += biz.addListasEspeciales(hash);
                
            } catch (Exception e) {
                mensaje += "<font color=red>- Ocurrió un error al cargar las listas.</font>";                
            }
        
        } catch (Exception e) {
            mensaje += "<font color=red>- Ocurrió un error al cargar las listas. El archivo utilizado debe ser como el del ejemplo.</font>";
            
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
