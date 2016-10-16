package cl.bbr.boc.command;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.CategorizacionErroneaDTO;
import cl.bbr.boc.dto.DetalleCategorizacionErroneaDTO;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.utils.Planilla;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCategoryProductDTO;
import cl.bbr.jumbocl.eventos.utils.EventosUtil;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

import com.oreilly.servlet.MultipartRequest;

/**
 * Fecha Creación: 27/10/2011
 *
 * Clase que se encarga de realizar la carga del archivo de categorización masiva 
 * de productos, esta realiza la validación y llena la lista de reporte a mostrar en caso de errores en el archivo
 * 
 */
public class CategorizacionMasivaCommand extends Command {
	
	/* Sobreescritura del metodo Execute que se encarga de realizar las operaciones de la clase
	 * @see cl.bbr.common.framework.Command#Execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, cl.bbr.jumbocl.usuarios.dto.UserDTO)
	 */
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		logger.debug("Ingreso al metodo Execute del command CategorizacionMasivaCommand");
		//1. Variables del método
		String paramUrl = "";
		String idEliminar;
		//2. recupero del web.xml la url
		paramUrl = getServletConfig().getInitParameter("UrlResponse");
		logger.debug("paramUrl: " + paramUrl);
		//3. recupero el parametro de archivo
		
		String rutaUpload = req.getSession().getServletContext().getRealPath("upload_categorizacion");
		logger.debug("rutaUpload:" + rutaUpload);
		
		logger.debug("Eliminamos archivos antiguos");
		File directorioDestino = new File(rutaUpload);
		EventosUtil.eliminarArchivosAntiguos(directorioDestino);
		
		MultipartRequest multi = new MultipartRequest(req, rutaUpload, 10000000);
		
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add(req.getParameterMap());
		
		logger.debug("Recupero el archivo");
		File archivo = multi.getFile("upFile");
		
		//realizo validación del archivo fisico
		if (archivo == null) {
			logger.error("Existio un error al realizar la categorización masiva");
			res.sendRedirect( cmd_dsp_error +"?mensaje=Existió un error al cargar la categorización masiva. No se especificó el archivo.&PagErr=1");
			return;
		}
		
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
		
		logger.debug("Construyo el archivo nuevo");
		File archivoNuevo = File.createTempFile("planilla_categorizacion_masiva_", ".xls", directorioDestino);
		EventosUtil.copy(archivo, archivoNuevo);
		archivo.delete();
		
		logger.debug("Realizo la carga del archivo");
		try{
			CategorizacionErroneaDTO reporte = cargarArchivo(archivoNuevo, biz, usr);
			logger.debug("Asigno reporte en la session");
			HttpSession session = req.getSession(true);
			session.setAttribute("repCategorizacionMasiva",reporte);
		}catch (Exception e) {
			logger.debug("Error al cargar el archivo " + e.getMessage());
			res.sendRedirect( cmd_dsp_error +"?mensaje="+e.getMessage()+".&PagErr=1");
		}
		//4. Redirecciona salida
		res.sendRedirect(paramUrl);
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
	private CategorizacionErroneaDTO cargarArchivo(File archivo, BizDelegate biz, UserDTO usr) throws BocException {
		logger.debug("Ingreso al metodo cargarArchivo");
		String mensaje = "";  
		ArrayList listadoReporte = new ArrayList();
		Planilla planilla = new Planilla(); 
		
		CategorizacionErroneaDTO reportePostCarga = new CategorizacionErroneaDTO();
		try {
			/* creamos un HSSFWorkbook con nuestro excel */
			HSSFWorkbook xls = planilla.getWorkbook(archivo.getAbsolutePath());
			/* Elegimos la hoja que queremos leer del excel */
			HSSFSheet sheet = xls.getSheetAt(0);
			logger.debug("Recorro las filas del archivo");
			// valido que el nombre de la primera fila sea el de mi planilla
			if(sheet != null){
				HSSFRow titulos = sheet.getRow(0);
				if(titulos != null){
					HSSFCell tituloProducto = titulos.getCell(0);
					HSSFCell tituloCategoria = titulos.getCell(1);
					//20121008 SBERNAL	
					HSSFCell tituloOrden = titulos.getCell(2);
					
					if(!"ID Producto".equals(tituloProducto.toString().trim()) || !"ID Categoria".equals(tituloCategoria.toString().trim()) || !"Orden".equals(tituloOrden.toString().trim())){
					//-20121008 SBERNAL	
						throw new Exception("Planilla incorrecta, Los titutlos de la planilla ingresada no concuerdan con los de la planilla que se entrega al usuario");
					}
				}
			}
			if(sheet.getLastRowNum() > 505){
				mensaje = "Error al procesar la información, el excel excede la cantidad de filas permitidas (500).";
				throw new Exception(mensaje);
			}
			/* Nos recorremos las filas */
			for (int j = 1; j <= sheet.getLastRowNum(); j++) {
				/* Cogemos una fila HSSFRow row=sheet.getRow(fila); */
				DetalleCategorizacionErroneaDTO detalleError = new DetalleCategorizacionErroneaDTO();
				long id_producto = -1;
				long id_categoria = -1;
				//20121008 SBERNAL
				int orden = -1;
				//-20121008 SBERNAL
				boolean filaCorrecta = true;
				mensaje = "";
				try {
					HSSFRow row = sheet.getRow(j);
					HSSFCell colIdProducto = row.getCell(0); //Indicamos q columna hay q leer
					// recuperamos el id de categoria del excel
					HSSFCell colIdCategoria = row.getCell(1); //Indicamos q columna hay q leer	
					//20121008 SBERNAL
					HSSFCell colOrden = row.getCell(2);
					
					// si las columnas son distintas de vacio entonces la tomo como una columna valida
					if(!"".equals(colIdProducto.toString().trim()) || !"".equals(colIdCategoria.toString().trim())|| !"".equals(colOrden.toString().trim()) ){
                    //-20121008 SBERNAL	
						// aumento la cantidad de filas procesadas
						reportePostCarga.setCantidadFilasProcesadas(reportePostCarga.getCantidadFilasProcesadas() + 1);
						
						// recuperamos el id de producto del excel
						try{
							id_producto = (long)Double.parseDouble(colIdProducto.toString());
						}catch (Exception e) {
							logger.debug("La fila " + j + " columna 0 tiene un valor incorrecto no numerico");
							filaCorrecta = false;
							mensaje += "Valor de producto incorrecto, valor no num&eacute;rico \n";
						}
						
						// recupero el id de categoria del excel
						try{
							id_categoria = (long)Double.parseDouble(colIdCategoria.toString());
						}catch (Exception e) {
							logger.debug("La fila " + j + " columna 1 tiene un valor incorrecto no numerico");
							filaCorrecta = false;
							mensaje += "Valor de categoria incorrecto, valor no num&eacute;rico \n"; 
						}
						
						//20121008 SBERNAL	
						//recupero el orden desde el excel
						try{
							orden = (int)Double.parseDouble(colOrden.toString());
						}catch (Exception e) {
							logger.debug("La fila " + j + " columna 2 tiene un valor incorrecto no numerico");
							filaCorrecta = false;
							mensaje += "Valor de orden incorrecto, valor no num&eacute;rico \n"; 
						}
						//-20121008 SBERNAL	
						
						logger.debug(j + " Valido si es que la fila es correcta y la agrego a mi lista");
						logger.debug(j + " filaCorrecta " + filaCorrecta);
						if(filaCorrecta){
							// Proceso para insertar cada una de las categorias que pasaron correctamente
							logger.debug(j + " Realizo la insercion de los datos validados");
							mensaje = "";
							try{
								String mensAsoc = "Carga masiva de mix";
								boolean result; 
								//20121008 SBERNAL	
								ProcAddCategoryProductDTO param = new ProcAddCategoryProductDTO(id_categoria, id_producto, "A", orden, mensAsoc, 
										usr.getLogin());
								//-20121008 SBERNAL
								
								//20121030 SBERNAL
								param.setCon_pago("S");	
								//-20121030 SBERNAL
								result = biz.setAddCategoryProduct(param);
								logger.debug(j + " resultado final: "+result);
								// aumento el valor de la cantidad cargada
								reportePostCarga.setCantidadCargada(reportePostCarga.getCantidadCargada() + 1);
							}catch(BocException e) {
								logger.debug(j + " Controlando excepción del AddProdcatweb: " + e.getMessage());
								
								if (  e.getMessage().indexOf(Constantes._EX_CAT_ID_NO_EXISTE) != -1){
									logger.debug(j + " El código de la categoría ingresado no existe");
									mensaje = "El c&oacute;digo de categor&iacute;a ingresado no existe";
								} else if ( e.getMessage().indexOf(Constantes._EX_PROD_ID_NO_EXISTE) != -1){
									logger.debug(j + " El código de producto ingresado no existe");
									mensaje = "El c&oacute;digo de producto ingresado no existe";
								} else if ( e.getMessage().indexOf(Constantes._EX_CAT_PROD_REL_EXISTE) != -1){
									logger.debug(j + " La relacion entre categoria y producto ya existe.");
									mensaje = "La relaci&oacute;n entre categor&iacute;a y producto ya existe";
								} else if ( e.getMessage().indexOf(Constantes._EX_CAT_NO_ES_TERMINAL) != -1){
									logger.debug(j + " La categoria no es terminal y no se puede asociar al producto.");
									mensaje = "La categor&iacute;a no es terminal";
								} else {
									logger.debug(j + " Controlando excepción: " + e.getMessage());
									mensaje = "Error no detectado " + e.getMessage();
								}
								//asigno valores al reporte
								detalleError.setId_categoria(Long.toString(id_categoria));
								detalleError.setId_producto(Long.toString(id_producto));

								//20121008 SBERNAL
								detalleError.setOrden(Integer.toString(orden));
								//-20121008 SBERNAL
								
								detalleError.setMensajeError(mensaje);
								listadoReporte.add(detalleError);
								// aumento la cantidad fallida
								reportePostCarga.setCantidadFallida(reportePostCarga.getCantidadFallida() + 1);
							} catch(Exception e){
								logger.debug(j + " Controlando excepción desconocida: " + e.getMessage());
								mensaje = "Error desconocido " + e.getMessage();
								detalleError.setId_categoria(Long.toString(id_categoria));
								detalleError.setId_producto(Long.toString(id_producto));
								
								//20121008 SBERNAL
								detalleError.setOrden(Integer.toString(orden));
								//-20121008 SBERNAL
								
								detalleError.setMensajeError(mensaje);
								listadoReporte.add(detalleError);
								//aumento la cantidad fallida
								reportePostCarga.setCantidadFallida(reportePostCarga.getCantidadFallida() + 1);
							}
						}else{
							logger.debug(j + " Fila incorrecta por lo cual lo agrego al listado de reporte");
							if(colIdCategoria.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
								detalleError.setId_categoria(NumberFormat.getInstance().format(colIdCategoria.getNumericCellValue()));
							}else{
								detalleError.setId_categoria(colIdCategoria.toString());	
							}
							if(colIdProducto.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
								detalleError.setId_producto(NumberFormat.getInstance().format(colIdProducto.getNumericCellValue()));
							}else{
								detalleError.setId_producto(colIdProducto.toString());
							}
							//20121008 SBERNAL
							if(colOrden.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
								detalleError.setOrden(NumberFormat.getInstance().format(colOrden.getNumericCellValue()));
							}else{
								detalleError.setOrden(colOrden.toString());
							}
							//-20121008 SBERNAL
							detalleError.setMensajeError(mensaje);
							listadoReporte.add(detalleError);
							reportePostCarga.setCantidadFallida(reportePostCarga.getCantidadFallida() + 1);
						}
						
					}
				} catch (Exception e) {
					logger.debug("Error " + e.getMessage());
				}            
			}
			reportePostCarga.setDetalle(listadoReporte);
			
		} catch (Exception e) {
			logger.debug("Error " + e.getMessage());
			mensaje = e.getMessage();
			throw new BocException(mensaje);
		}
		logger.debug("Retorno el reporte");
		return reportePostCarga;
	}
	
}
