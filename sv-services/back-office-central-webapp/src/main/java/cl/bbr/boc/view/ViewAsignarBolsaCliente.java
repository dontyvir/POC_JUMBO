package cl.bbr.boc.view;

import java.io.File;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.oreilly.servlet.MultipartRequest;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.utils.Planilla;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.eventos.utils.EventosConstants;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega monitor de jornadas, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author jsepulveda
 */
public class ViewAsignarBolsaCliente extends Command {
	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		String respuesta = new String();
		try{
			// 1. Parámetros de inicialización servlet
			BizDelegate bizDelegate = new BizDelegate();
			
			// 2. cargar archivo
			String rutaUpload = req.getSession().getServletContext().getRealPath("upload_ruts_eventos");
			logger.debug("rutaUpload:" + rutaUpload);
			
			logger.debug("Eliminamos archivos antiguos");
	        File directorioDestino = new File(rutaUpload);
	        EventosUtil.eliminarArchivosAntiguos(directorioDestino);
	        
	        MultipartRequest multi = new MultipartRequest(req, rutaUpload, 10000000);
	        
	        ForwardParameters fp = new ForwardParameters();
	        fp.add(req.getParameterMap());
			
	        File archivo = multi.getFile("export_bolsas_cliente");
	        
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
	        
	        
	        File archivoNuevo = File.createTempFile("carga_bolsas_clientes_", ".xls", directorioDestino);
	        EventosUtil.copy(archivo, archivoNuevo);
	        archivo.delete();
	        
	        /*respuesta += "<fieldset style='width: 80%;'>" +
	        		"<legend>Validaci&oacute;n carga maestra de bolsas</legend>" +
	        		"<table width='95%'  border='0' cellspacing='2' cellpadding='0'>" +
	        		"<tr><td width='50%'><br/>";*/
	        
	        respuesta += cargarBolsas(archivoNuevo, bizDelegate);
	      
	        //respuesta += "<br/><br/></td></tr></table></fieldset>";

	        bizDelegate.insertarRegistroBitacoraBolsas("Asignación maestra de bolsas ", 
	        		usr.getLogin(), usr.getId_local()+"");
		}catch(Exception e){
			e.printStackTrace();
		}
        
        String paramUrl = "ViewMonitorBolsas?respuesta_ok=Se actualizó tabla maestra de bolsas exitosamente"
        		+"&result_validacion="+respuesta;
        
		res.sendRedirect(paramUrl);
	}
	
	
	private String cargarBolsas(File archivo, BizDelegate biz){
		String respuesta = new String();
		Vector registros = new Vector();
		Planilla planilla = new Planilla();
		int j = 1;		
		int z = 0;
		int x = 0;
		try{
			/* creamos un HSSFWorkbook con nuestro excel */
            HSSFWorkbook xls = planilla.getWorkbook(archivo.getAbsolutePath());
            /* Elegimos la hoja que queremos leer del excel */
            HSSFSheet sheet = xls.getSheetAt(0);
            
            /* Nos recorremos las filas */
            for (j = 1; j <= sheet.getLastRowNum(); j++) {
            	String cod_bolsa = new String();
            	String rut_cliente = new String();
            	cod_bolsa = "N";
            	HSSFRow row = sheet.getRow(j);
            	
            	//columna 1 rut cliente
            	HSSFCell col2 = row.getCell(0);
            	if( col2.toString() != null ){
             		rut_cliente = col2.toString();
                	if(EventosUtil.validarRut(rut_cliente)){
        				//columna 2 código bolsa
                    	HSSFCell col1 = row.getCell(1);
                    	if(col1.toString() != null || !col1.toString().equalsIgnoreCase("")){
                			cod_bolsa = col1.toString();                			
                			if(!cod_bolsa.equals("")){
                				String tmp[] = cod_bolsa.toString().split(".0");
                				biz.asignacionBolsaCliente(rut_cliente, tmp[0] );
                				z++;
                    			respuesta += "<font color='green'>Fila : " + (j+1) + " - Registro OK : "+rut_cliente+"</font><br>";	
                			}else{
                				x++;
                    			respuesta += "<font color='red'>Fila : " + (j+1) + " - Error columna código de BOLSA.</font><br>";
                				continue;
                			}		
                    	}else{
                    		x++;
                			respuesta += "<font color='red'>Fila : " + (j+1) + " - Error columna código de BOLSA.</font><br>";
                			continue;
                		}                    	                    	
                	}else{
                		x++;
                		respuesta += "<font color='red'>Fila : " + (j+1) + " - El RUT ingresado no es válido ó es nulo.</font><br>";
                		continue;
                	}             		
             	}else {
             		x++;
             		respuesta += "<font color='red'>Fila : " + (j+1) + " - Error en la columna rut cliente</font><br>";
 					continue;
             	}            	           	
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		respuesta += "<br><br><hr>Total Registros Procesados : "+(j-1)+"<font color='green'>    [ Correctos : "+z+" ]</font>  <font color='red'>  [ Erroneos : "+x+" ]</font>";
		return respuesta;
	}
	
	
}
