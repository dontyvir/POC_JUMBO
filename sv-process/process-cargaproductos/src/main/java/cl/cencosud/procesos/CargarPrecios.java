package cl.cencosud.procesos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cl.cencosud.beans.Precio;
import cl.cencosud.constantes.ConstantesSQL;
import cl.cencosud.excel.impl.XlsFileImpl;
import cl.cencosud.facade.CargarPreciosFacade;
import cl.cencosud.rmi.ModificarPrecios;
import cl.cencosud.util.FileUtil;
import cl.cencosud.util.LogUtil;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett y RiFFoNeiToR
 * 
 * Carga de precios parciales (carga periodicamente las actualizaciones
 * de precios que vienen en los archivos JCP
 * 
 */
public class CargarPrecios {
	
	static {
		LogUtil.initLog4J();
	}
	
	private static Logger logger = Logger.getLogger(CargarPrecios.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS a 'del' dd/MM/yyyy ");
	private static SimpleDateFormat sdfHours = new SimpleDateFormat("HH:mm:ss.SSS a");
	private static Date startDate = null;
	
	public static void main(String[] args) {
		/*
		 * Lo primero es ver si hay archivos que cargar ya que la idea es que
		 * este programa se ejecute en periodos lo más cortos posibles. Entonces
		 * debe ocupar en cada momento los mínimos recursos necesarios.
		 */
		logger.info("***** INICIO PROCESO ACTUALIZACION DE NOVEDADES A LAS " + sdf.format(new Date())+" *****");
		//logger.info("Ambiente:" + Parametros.getString("AMBIENTE")+", url:"+ Parametros.getString("URL"));
		
		String codigoLocal = null;
		CargarPrecios cp = new CargarPrecios();	
				
		String path = Parametros.getString("PATH_ARCHIVOS");
		String[] archivos = FileUtil.nombresArchivos(path);
		
		try {
						
			if (archivos.length > 0) {
				logger.debug("*** Paso 1 - Inicio actualizacion de novedades por local ***");
				
				/*Se envia email de alerta */
				//cp.sendMailAlertFileGZ(path);
												
				List listaPreciosNoValidas = new ArrayList();
				List listaPreciosNoActualizados = new ArrayList();
				
				for (int i = 0; i < archivos.length; i++) {
					startDate = new Date();
					codigoLocal = FileUtil.local(archivos[i]);
					logger.debug(" Carga Interfaz ["+archivos[i]+"] a las " + sdfHours.format(startDate));
					/*
					 * Se guarda una lista con todas las unidades de medidada no validad de los archivos
					 * */

					//listaPreciosNoValidas.add(cp.establecerPreciosModificar(codigoLocal, archivos[i]));
					Map mapPrecios = cp.establecerPreciosModificar(codigoLocal, archivos[i]);
					listaPreciosNoValidas.add((List) mapPrecios.get("listaPreciosNoValidas"));
					listaPreciosNoActualizados.add((List) mapPrecios.get("listaPreciosNoActualizados"));

					logger.debug("Finaliza Interfaz ["+archivos[i]+"] a las " + sdfHours.format(startDate));
				}				
				logger.debug("*** Paso 1 - Fin actualizacion de novedades por local ***");
				
				/* Se envia email de alerta */
				//cp.sendMailAlertUnitsNovalidas(listaPreciosNoValidas);
				/* Se envia email de alerta de productos que no lograron actualizarse */
				//cp.sendMailAlertPreciosNoActualizados(listaPreciosNoActualizados);

				cp.createExcelAndSendMail(path, listaPreciosNoValidas, listaPreciosNoActualizados);
				
				/* 
				 * Empieza a validar en Local Maestro
				 * */
				cp.establecerLocalMaestro(archivos);

				for (int j = 0; j < archivos.length; j++) {
					Archivo.borrar(archivos[j]);
				}				
			}
		} catch (Exception e) {
			logger.error("Ocurrieron problemas al procesar actualizacion de novedades: " + e);
		}finally{
			logger.info("***** FINALIZA PROCESO ACTUALIZACION DE NOVEDADES A LAS " + sdf.format(new Date())+" *****");
		}		
	}

	/**
	 * Se encarga de llenar el objeto 'listPrecioModificar' con los precios de las interfaz 
	 * 
	 * @param codigoLocal, Codigo del local
	 * @param archivo, nombre del archivo (interfaz)
	 * 
	 * @throws Exception
	 */
	private Map establecerPreciosModificar(String codigoLocal, String archivo)throws Exception {
		List listPecioModificar = new ArrayList();
				
		HashMap mapListaPrecios = CargarPreciosFacade.getListaPreciosMapList(archivo);
		List listaPrecios = (List) mapListaPrecios.get("listaPreciosOk");
		List listaPreciosNoValidas = (List) mapListaPrecios.get("listaPreciosNoValidas");
		List listaPreciosNoActualizados = new ArrayList();
		//Map que guarada 2 listas, listaPreciosNoValidas y listaPreciosNoActualizados
		Map mapPrecios = new HashMap();

		Connection con = DbCarga.conexion(Parametros.getString("USER"), Parametros.getString("PASSWORD"), Parametros.getString("DRIVER"), Parametros.getString("URL"));
		PreparedStatement ps = null;

		try {
			logger.debug("Codigo Local:"+codigoLocal);
			
			int localId = DbCarga.getLocalId(DbCarga.conexion(Parametros.getString("USER"), Parametros.getString("PASSWORD"), Parametros.getString("DRIVER"), Parametros.getString("URL")), codigoLocal);
			logger.debug("Id Local DB:"+localId);
									
			ps = DbCarga.preparedStatement(con, ConstantesSQL.CP_SEL_DB);

			for (int i = 0; i < listaPrecios.size(); i++) {
				Precio pFile = (Precio) listaPrecios.get(i);
				
				Precio pDb = DbCarga.getPrecio(ps, localId, pFile.getCodigoProducto(), pFile.getUnidadMedida(), pFile.getNombreArchivo());

				if (pDb != null && pFile.getPrecio() != pDb.getPrecio() && !pDb.isFueraDeRango(pFile)) {
					pFile.setIdProducto(pDb.getIdProducto());
					pFile.setIdFoProducto(pDb.getIdFoProducto());
					pFile.setIdLocal(localId);
					pFile.setPrecioActual(pDb.getPrecio());
					listPecioModificar.add(pFile);

				}else{
					if (pDb != null){
						pFile.setPrecioActual(pDb.getPrecio());
						logger.debug("Precio DB, Precio NO agregado a lista, " +
								" Precio Interfaz:"+pFile.getPrecio() + ", Precio Db:"+pDb.getPrecio() +
								", FueraDeRango:"+pDb.isFueraDeRango(pFile)+ ", idProducto db:"+pDb.getIdProducto() + 
								", unidad file:"+ pFile.getUnidadMedida() + ", idlocal db:"+pDb.getIdLocal()+
								", idProducto file:" + pFile.getIdProducto() +", idFoProducto file:"+pFile.getIdFoProducto() +
								", codigo Producto file:" + pFile.getCodigoProducto() +", idFoProducto file:"+pFile.getIdFoProducto() +
								", en interfaz ["+archivo+"]");	
					}else{
						logger.debug("Precio DB es null - Precio NO agregado a lista, " +
								" Precio Interfaz:"+pFile.getPrecio() + ", unidad file:"+pFile.getUnidadMedida() + 
								", iProducto file:" + pFile.getIdProducto() +", idFoProducto file:"+pFile.getIdFoProducto() +
								", codigo Producto file:" + pFile.getCodigoProducto() +
								", idlocal file:"+pFile.getIdLocal()+ ", en interfaz ["+archivo+"]");
					}
					
					//Agregar alerta de precios que no se actualizan
					listaPreciosNoActualizados.add(pFile);
				}				
			}
		} finally {
			if (ps != null)DbCarga.close(ps);				
			if (con != null)DbCarga.close(con);
						
		}
		logger.debug("Cantidad de precios a modificar:" + listPecioModificar.size());

		if (listPecioModificar.size() > 0) {
			// obtiene conexión a clase remota que actuliza precios en base de
			// datos e
			// indice lucene
			// String ip = Parametros.getString("IP_RMI");
			// String port = Parametros.getString("PUERTO_RMI");
			// IServidorRMI remota = (IServidorRMI) java.rmi.Naming.lookup("//"
			// + ip + ":" + port + "/Servidor");
			// remota.modificarPrecios(preciosAModificar);

			// para probar desde pc
			ModificarPrecios mp = new ModificarPrecios();
			mp.preparaPreciosActualizar(listPecioModificar);
		}

		//Se agregan las listas al map		
		mapPrecios.put("listaPreciosNoValidas", listaPreciosNoValidas);
		mapPrecios.put("listaPreciosNoActualizados", listaPreciosNoActualizados);

		return mapPrecios;
	}

	
	/**
	 * 
	 * Cambia los precios cargados diariamnete a traves de archivos sap, le pone
	 * el precio de local maestro a todos los locales si es que existe
	 * producto en ese local.
	 * 	
	 * @param archivos, array con la lista de archivos (interfaz) 
	 * 
	 * */
	private void establecerLocalMaestro(String[] archivos) throws Exception {		
		logger.debug("*** Paso 2 - Inicio establecer Local Maestro a las " + sdfHours.format(startDate) +"***");
		
		int idLocalMaestro = Integer.parseInt(Parametros.getString("LOCAL"));
		if (idLocalMaestro != 0) {
			List precios = new ArrayList();
			HashMap hash = new HashMap();
						
			if (archivos.length > 0) {				
				for (int i = 0; i < archivos.length; i++) {			
					logger.debug(" Carga Interfaz ["+archivos[i]+"]");
					precios = CargarPreciosFacade.getListaPrecios(archivos[i]);
					for (int j = 0; j < precios.size(); j++) {
						Precio pFile = (Precio) precios.get(j);						
						hash.put(pFile.getCodigoProducto(), pFile);
					}
				}
			}

			ModificarPrecios mp = new ModificarPrecios();
			mp.preparaPrecioLocalMaestro(hash, idLocalMaestro);
			
		} else {
			logger.info("No se estandarizan los precios, NO hay local maestro.");
		}
		logger.debug("*** Paso 2 - Fin establecer Local Maestro a las " + sdfHours.format(startDate) +"***");		
	}
	
	
	/** 
	 * Lista y valida archivos con extension .gz y  envia email de alerta, 
	 * con archivos corruptos
	 *  
	 * @param path, ruta deonde se encuentran las interfaces
	 *  
	 * */
	/*
	private void sendMailAlertFileGZ(String path){
		try{
			SendMail sendMail = new SendMail();		
			List lista = FileUtil.validaArchivosGZ(FileUtil.listaArchivosExtGZ(path), path);				
			if (lista.size() > 0) {			
				sendMail.sendMailAlertFileGZ(lista);
			}	
		}catch(Exception e){
			logger.info("Error al enviar mail de alerta.");
		}
	}
	*/
	
	/** 
	 * Envia email de alerta con las unidades no validas
	 *  
	 *  @param path, ruta deonde se encuentran las interfaces
	 *  
	 * */
	/*
	private void sendMailAlertUnitsNovalidas(List listaPreciosNoValidas){
		try{
			SendMail sendMail = new SendMail();	
			if (listaPreciosNoValidas.size() > 0) {				
				sendMail.sendMailAlertUnitsNovalidas(listaPreciosNoValidas);
			}
		}catch(Exception e){	
			logger.info("Error al enviar mail de alerta de unidades.");
		}
	}
	*/
	/**
	 * Envia email de alerta con los productos que no lograron actualizarse
	 */
	/*
	private void sendMailAlertPreciosNoActualizados(List listaPreciosNoActualizados) {
		try {
			SendMail sendMail = new SendMail();
			if (listaPreciosNoActualizados.size() > 0) {
				XlsFileImpl xls = new XlsFileImpl();
				//xls.createFilePreciosSinActualizar("", listaPreciosNoActualizados);
			//	sendMail.sendMailAlertPreciosNoActualizados(listaPreciosNoActualizados);
			}
		} catch(Exception e) {
			logger.info("Error al enviar mail de precios no actualizados.");
		}
	}
	*/
	
	private void createExcelAndSendMail(String path, List listaPreciosNoValidas, List listaPreciosNoActualizados) {
		try {
			XlsFileImpl xls = new XlsFileImpl();
			xls.createFilePreciosSinActualizar(path, listaPreciosNoValidas,listaPreciosNoActualizados);
		} catch(Exception e) {
			logger.error("Error:" + e);
		}
	}
	
}