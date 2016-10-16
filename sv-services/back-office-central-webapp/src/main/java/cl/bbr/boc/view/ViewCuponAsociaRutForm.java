package cl.bbr.boc.view;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.utils.Planilla;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.model.CuponPorRut;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

import com.oreilly.servlet.MultipartRequest;
/**
 * formulario para el ingreso de nuevos productos al monitor de productos
 * @author BBRI
 */
public class ViewCuponAsociaRutForm extends Command {
	private final static long serialVersionUID = 1;


	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		String paramUrl = getServletConfig().getInitParameter("UrlResponse");
		logger.debug("paramUrl: " + paramUrl);
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parámetros
		int id_cup_dto = 0;
		int cargando = 0;
		
		BizDelegate biz = new BizDelegate();
		
		if(!req.getParameter("cargando").equals(""))
			cargando = Integer.parseInt(req.getParameter("cargando"));
		else
			cargando = 0;
		
		if (req.getParameter("id_cup_dto") != null) {
			
			id_cup_dto = Integer.parseInt(req.getParameter("id_cup_dto"));
		
			top.setVariable("{id_cup_dto}"	,String.valueOf(id_cup_dto));
			top.setVariable("{mensaje}"	,"");
			
			if(cargando == 1) {
				
				String rutaUpload = req.getSession().getServletContext().getRealPath("upload_categorizacion");
				logger.debug("rutaUpload:" + rutaUpload);
				
				logger.debug("Eliminamos archivos antiguos");
				File directorioDestino = new File(rutaUpload);
				EventosUtil.eliminarArchivosAntiguos(directorioDestino);
				
				MultipartRequest multi = new MultipartRequest(req, rutaUpload, 10000000);
				
				logger.debug("Recupero el archivo");
				File archivo = multi.getFile("upFile");
				
				
				//realizo validación del archivo fisico
				if (archivo == null) {
					logger.error("Existio un error al realizar la rut masiva");
					res.sendRedirect( cmd_dsp_error +"?mensaje=Existió un error al cargar la categorización masiva. No se especificó el archivo.&PagErr=1");
					return;
				}
				else{
					
				if (archivo.getName().length() > 4) {
					if (!archivo.getName().substring(archivo.getName().length()-4,archivo.getName().length()).equalsIgnoreCase(".xls")) {
						logger.error("Existió un error al cargar la categorización masiva. No es un archivo XLS.");
						res.sendRedirect( cmd_dsp_error +"?mensaje=Existió un error al cargar la categorización masiva. No es un archivo XLS.&PagErr=1");
						return;
					}
				} else {
					logger.error("Existió un error al cargar la categorización. Archivo erroneo.");
					res.sendRedirect( cmd_dsp_error +"?mensaje=Existió un error al cargar la categorización masiva. Archivo erroneo.&PagErr=1");
					return;
				}
				
				logger.debug("Realizo la carga del archivo");
				try{
					if(cargarArchivo(archivo, id_cup_dto, usr.getId_usuario())){
						top.setVariable("{id_cup_dto}"	,String.valueOf(id_cup_dto));
						top.setVariable("{mensaje}"	,"Se realizo con exito, la carga de rut");
						
					}else{
						top.setVariable("{id_cup_dto}"	,String.valueOf(id_cup_dto));
						top.setVariable("{mensaje}"	,"se producto un error en la carga de rut");
					}
					
				}catch (Exception e) {
					logger.debug("Error al cargar el archivo " + e.getMessage());
					res.sendRedirect( cmd_dsp_error +"?mensaje="+e.getMessage()+".&PagErr=1");
				}
				
				}
				
			}
			
			
			top.setVariable("{cantidad_rut_asociado}"	,String.valueOf(biz.getCantidadRutAsociado(id_cup_dto)));
			
			// 6. Setea variables del template
			// variables del header
			top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
			Date now = new Date();
			top.setVariable("{hdr_fecha}"	,now.toString());
			
			String result = tem.toString(top);
			
			salida.setHtmlOut(result);
			salida.Output();		
			
		}
		
		/*
		 *  ejecutar carga masiva
		 */
			
			
			
		
			
		
	}
	
	
	/**
	 * Metodo que se encarga de leer el archivo de categorización masiva
	 * por cada una de las lineas del archivo validará si es correcto para posteriormente cargarlo
	 * ingresa en caso de error al reporte que se mostrará posteriormente al usuario.
	 * @param archivo
	 * @param biz
	 * @param usr
	 * @return
	 */
	private boolean cargarArchivo(File archivo, int id_cupon, long id_usuario) throws BocException {
		logger.debug("Ingreso al metodo cargarArchivo");
		
		CuponPorRut cpr = null;
		boolean isCargo = false;
		BizDelegate biz = new BizDelegate();
		String mensaje = "";  
		Planilla planilla = new Planilla(); 
		
		try {
			/* creamos un HSSFWorkbook con nuestro excel */
			HSSFWorkbook xls = planilla.getWorkbook(archivo.getAbsolutePath());
			/* Elegimos la hoja que queremos leer del excel */
			HSSFSheet sheet = xls.getSheetAt(0);
			logger.debug("Recorro las filas del archivo");
			// valido que el nombre de la primera fila sea el de mi planilla
			
			if(sheet.getLastRowNum() > 1000){
				mensaje = "Error al procesar la información, el excel excede la cantidad de filas permitidas (1000).";
				throw new Exception(mensaje);
			}
			/* Recorremos la fila */
			for (int j = 1; j <= sheet.getLastRowNum(); j++) {
				/* Cogemos una fila HSSFRow row=sheet.getRow(fila); */
				
				cpr = new CuponPorRut();
				
				String rut = "";
				//-20121008 SBERNAL
				boolean filaCorrecta = true;
				mensaje = "";
				try {
					HSSFRow row = sheet.getRow(j);
					HSSFCell colRut = row.getCell(0); //Indicamos q columna hay q leer
					
					
					// si las columnas son distintas de vacio entonces la tomo como una columna valida
					if(!"".equals(colRut.toString().trim())){
                    //-20121008 SBERNAL	
						//recupero el rut desde el excel
						try{
							rut = validarRut(colRut);
							
						}catch (Exception e) {
							logger.debug("La fila " + j + " columna 2 tiene un valor incorrecto no numerico");
							filaCorrecta = false;
							mensaje += "Valor de orden incorrecto, valor no num&eacute;rico \n"; 
						}
						//-20121008 SBERNAL	
						
						logger.debug(j + " Valido si es que la fila es correcta y la agrego a mi lista");
						logger.debug(j + " filaCorrecta " + filaCorrecta);
						if(filaCorrecta){
							// Proceso para crear el objeto a cada una de los rut que pasaron correctamente
							logger.debug(j + " Realizo la insercion de los datos validados");
							mensaje = "";
							try{
								
								cpr.setRut(Integer.parseInt(rut));
								cpr.setId_cupon(id_cupon);
								
								if(biz.setCargaRutMasiva(cpr, id_usuario)){
									isCargo=true;
								}
								
								
							}catch(Exception e){
								logger.debug(j + " Controlando excepción desconocida: " + e.getMessage());
							
							}
						}else{
							logger.debug(j + " Fila incorrecta por lo cual lo agrego al listado de reporte");
							
						}
						
					}
				} catch (Exception e) {
					logger.debug("Error " + e.getMessage());
				}            
			}
			
			
			
		} catch (Exception e) {
			logger.debug("Error " + e.getMessage());
			mensaje = e.getMessage();
			throw new BocException(mensaje);
		}
		logger.debug("Retorno el reporte");
		return isCargo;
	}
	
	
	
	public String validarRut(HSSFCell colRut){

		int rut = 0;		
		int rutInt = (int) colRut.getNumericCellValue();
    	String RutString = Integer.toString(rutInt).trim();
		
		    for (int i = 0; i < RutString.length(); i++) {
		    	
		    	if (RutString.charAt(i) != ' ') {
		        	rut++;
		        }
		    }
		
		if(rut == 8 || rut == 7){
			return RutString;
		}
		else
		{
			RutString = colRut.getStringCellValue();
			
		}
			
		return RutString;		
	}
	
	

}
