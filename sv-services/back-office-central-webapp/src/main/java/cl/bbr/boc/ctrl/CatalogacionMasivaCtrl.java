package cl.bbr.boc.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cl.bbr.boc.dao.DAOFactoryParametros;
import cl.bbr.boc.dao.jdbc.JdbcCatalogacionMasivaDAO;
import cl.bbr.boc.dao.jdbc.JdbcStockOnLineDAO;
import cl.bbr.boc.dto.BOCatalogacionMasivaDTO;
import cl.bbr.boc.dto.BOProductoDTO;
import cl.bbr.boc.dto.FOProductoDTO;
import cl.bbr.boc.dto.MarcaDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.ProductoLogEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dao.DAOFactory;
import cl.bbr.jumbocl.contenidos.dao.JdbcProductosDAO;
import cl.bbr.jumbocl.contenidos.dto.CatalogacionMasivaLogDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;
import cl.bbr.jumbocl.contenidos.dto.UnidadMedidaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CatalogacionMasivaDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosException;
import cl.bbr.jumbocl.pedidos.dto.BarraAuditoriaSustitucionDTO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * 
 * @author jolazogu
 * 
 */
public class CatalogacionMasivaCtrl {

	Logging logger = new Logging(this);

	
	
	/**
	 * 
	 * @param marca
	 * @return
	 * @throws DAOException
	 */
	public boolean getExisteMarcaProducto( String marca ) throws DAOException {
		
		JdbcCatalogacionMasivaDAO cataMasivoDAO = ( JdbcCatalogacionMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCatalogacionMasivaDAO();
		
		try {
		
			return cataMasivoDAO.getExisteMarcaProducto( marca );
		
		} catch ( Exception e ) {
		
			logger.error( "Error getExisteMarcaProducto: ", e );
			throw new DAOException( e );
		
		}
	
	}
	
	/**
	 * 
	 * @param unidadMedidaPPM
	 * @return
	 * @throws DAOException
	 */
	public boolean getExisteUnidadMedidaPPM( String unidadMedidaPPM ) throws DAOException {
		
		JdbcCatalogacionMasivaDAO cataMasivoDAO = ( JdbcCatalogacionMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCatalogacionMasivaDAO();
		
		try {
		
			return cataMasivoDAO.getExisteUnidadMedidaPPM( unidadMedidaPPM );
		
		} catch ( Exception e ) {
		
			logger.error( "Error getExisteUnidadMedidaPPM: ", e );
			throw new DAOException( e );
		
		}
	
	}
	
	/**
	 * 
	 * @param sectorPicking
	 * @return
	 * @throws DAOException
	 */
	public boolean getExisteSectorPicking( String sectorPicking ) throws DAOException {
		
		JdbcCatalogacionMasivaDAO cataMasivoDAO = ( JdbcCatalogacionMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCatalogacionMasivaDAO();
		
		try {
		
			return cataMasivoDAO.getExisteSectorPicking( sectorPicking );
		
		} catch ( Exception e ) {
		
			logger.error( "Error getExisteSectorPicking: ", e );
			throw new DAOException( e );
		
		}
	
	}
	
	/**
	 * 
	 * @param codLocal
	 * @return
	 * @throws DAOException
	 */
	public String getLocalNoExiste( String codLocal ) throws DAOException {
		
		JdbcCatalogacionMasivaDAO cataMasivoDAO = ( JdbcCatalogacionMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCatalogacionMasivaDAO();
		
		try {
		
			return cataMasivoDAO.getLocalNoExiste( codLocal );
		
		} catch ( Exception e ) {
		
			logger.error( "Error getLocalNoExiste: ", e );
			throw new DAOException( e );
		
		}
	
	}
	

	/**
	 * 
	 * @param cod_prod1
	 * @param unidadMedida
	 * @return
	 * @throws DAOException
	 * @throws SystemException 
	 */
	public String getValidandoDatosBO( String cod_prod1, String unidadMedida ) throws DAOException {
		
		JdbcCatalogacionMasivaDAO cataMasivoDAO = ( JdbcCatalogacionMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCatalogacionMasivaDAO();
		
		try {
			
			return cataMasivoDAO.getValidandoDatosBO( cod_prod1, unidadMedida );
		
		} catch ( Exception e ) {
		
			logger.error( "Error getValidandoDatosBO: ", e );
			throw new DAOException( e );
		
		}
	
	}
	
	/**
	 * 
	 * @param listProductosXls
	 * @param usr
	 * @return
	 * @throws DAOException
	 * @throws SystemException 
	 * @throws ProductosException 
	 */

	public boolean getAgregandoProductosAlMix( List listProductosXls, UserDTO usr ) throws DAOException, SystemException, ProductosException {
	
	JdbcCatalogacionMasivaDAO cataMasivoDAO = ( JdbcCatalogacionMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCatalogacionMasivaDAO();
	
	boolean cargaExitosa = false;
	int totalProductoCargados = 0;
	boolean respPS = false;
	

	//	 Creamos trx
	JdbcTransaccion trx1 = new JdbcTransaccion();
			
	// Iniciamos trx
	try {
		trx1.begin();
	} catch (DAOException e1) {
		logger.error("Error al iniciar transacción");
		throw new SystemException("Error al iniciar transacción");
	} catch (SQLException e) {
		// TODO Bloque catch generado automáticamente
		e.printStackTrace();
		logger.error("Error al iniciar transacción");
		throw new SystemException("Error al iniciar transacción");
	}
	
	// Marcamos los dao's con la transacción
	try {
		cataMasivoDAO.setTrx(trx1);
	} catch (CatalogacionMasivaDAOException e2) {
		logger.error("Error al asignar transacción al dao Productos");
		throw new SystemException("Error al asignar transacción al dao Productos");
	}
	
	try {
	
			Iterator it = listProductosXls.iterator();
			
		    while ( it.hasNext() ) {
 		    	
		    	BOCatalogacionMasivaDTO catalogacionMasiva = ( BOCatalogacionMasivaDTO )it.next();
		
		    	logger.info( "Se valida si el producto existe en FO_PRODUCTO y el campo PRO_ESTADO es igual A...." );
				
		    	BOProductoDTO producto = cataMasivoDAO.getBoProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
		    	
		    	logger.info( "Se obtiene los siguientes atributos del producto...." );
		    	logger.info( "ID_PRODUCTO : " +producto.getId() );
		    	logger.info( "DES_CORTA : " +producto.getDescripcion_corta());
		    	logger.info( "UNI_MED : " +producto.getUnidad());
		    	logger.info( "COD_PROD1 : " +producto.getCodSap());
		    	logger.info( "DES_LARGA : " +producto.getDescripcion_larga());
		    	
		    	MarcasDTO marca = cataMasivoDAO.getFOMarca(catalogacionMasiva.getMarcaProducto());
		    	
		    	logger.info( "Se obtiene los siguientes atributos del producto...." );
		    	logger.info( "MAR_ID : " +marca.getId() );
		    	
		    	UnidadMedidaDTO unidadMedida = cataMasivoDAO.getFOUnidadMedidaPPM(catalogacionMasiva.getUnidadMedidaPPM());
		    	
		    	logger.info( "Se obtiene los siguientes atributos del producto...." );
		    	logger.info( "MAR_ID : " +marca.getId() );
		    	
		    	String idProducto = cataMasivoDAO.getIdProductoFOProductos(catalogacionMasiva.getCodigoSap());
			    	
			    logger.info( "idProducto... "+idProducto );	
			    	
			    logger.info( "Se obtiene una lista de los precios y locales en BO_PRECIOS..." );	
			    	
			    List precioLocal = cataMasivoDAO.getPrecioLocalBOPrecios(catalogacionMasiva.getCodigoSap());
			    
			    logger.info( "Se obtuvieron en total " + precioLocal.size() + " precios y locales en BO_PRECIOS..." );
	    	
				if ( cataMasivoDAO.getExisteSkuFOProducto( catalogacionMasiva )) {
					
					if ( cataMasivoDAO.getProEstadoProducto( catalogacionMasiva ) ) {
						
						logger.info( "El producto existe y el pro_estado es igual A..." );	
						
						logger.info( "::::::::::::::  Se inicia la actualizacion del producto : " + catalogacionMasiva.getCodigoSap() +" " + catalogacionMasiva.getUnidadMedida() +"  ::::::::::::::::::::" );	
				    	
						logger.info( "Se valida si el producto tiene sector de picking en BO_SECTOR...." );
				    	
				    	if ( !cataMasivoDAO.getExisteProductoSector( catalogacionMasiva.getSectorPicking(), producto.getId() ) ) {
				    	
				    		logger.info( "El producto no tiene sector de picking...." );
				    		
					    	logger.info( "Se procede agregar sector de picking en BO_SECTOR..." );	
					    	
					    	respPS = cataMasivoDAO.setInsertarProductoSector( catalogacionMasiva, producto.getId() );
					    	
					    	if (!respPS) {
								
								trx1.rollback();
								throw new ProductosException("Error al agregar el producto al sector de picking.");
							
							}
					    	
					    	logger.info( "Se agrega sector de picking en BO_SECTOR..." );	
					    	
					    	logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
					    	
					    	if (respPS) {
					    	
					    		FOProductoDTO productoFO = cataMasivoDAO.getFOProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
						    	
								CatalogacionMasivaLogDTO log = new CatalogacionMasivaLogDTO(); 
								log.setCod_prod(productoFO.getId());
								log.setCod_prod_bo(Long.parseLong(producto.getCodSap()));
								log.setFec_crea(Formatos.getFecHoraActual());
								log.setUsuario(usr.getLogin());
								log.setTexto("El producto SAP " +producto.getCodSap()+ " " +producto.getUnidad()+ " se agrego al sector de picking.");
								int resLog = cataMasivoDAO.agregaLogProducto(log);
							
							}
					    	
				    	}
				    	else {
				    		
				    		logger.info( "El producto tiene sector de picking...." );
				    		
				    		logger.info( "Se procede actualizar sector de picking en BO_SECTOR..." );	
					    	
				    		respPS = cataMasivoDAO.setModificarProductoSector( catalogacionMasiva, producto.getId() );

					    	if (!respPS) {
								
								trx1.rollback();
								throw new ProductosException("Error al actualizar el producto al sector de picking.");
							
							}
					    	
					    	logger.info( "Se actualizo sector de picking en BO_SECTOR..." );	
					    	
					    	logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
					    	
					    	if (respPS) {
					    	
					    		FOProductoDTO productoFO = cataMasivoDAO.getFOProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
						    	
								CatalogacionMasivaLogDTO log = new CatalogacionMasivaLogDTO(); 
								log.setCod_prod(productoFO.getId());
								log.setCod_prod_bo(Long.parseLong(producto.getCodSap()));
								log.setFec_crea(Formatos.getFecHoraActual());
								log.setUsuario(usr.getLogin());
								log.setTexto("El producto SAP " +producto.getCodSap()+ " " +producto.getUnidad()+ " se actualizo en el sector de picking.");
								int resLog = cataMasivoDAO.agregaLogProducto(log);
								
					    	}
					    	
				    	}
						
						
				    	logger.info( "Se procede actualizar el producto en FO_PRODUCTOS.." );	
				    	
				    	boolean respFO = cataMasivoDAO.setModificarFOProducto( catalogacionMasiva, producto, usr, marca.getId(), unidadMedida.getId() );
				    	
						if (!respFO) {
							
							trx1.rollback();
							throw new ProductosException("Error al actualizar el producto en FO.");
						
						}
						
						logger.info( "Se actualiza el producto en FO_PRODUCTOS.." );	
						
						logger.info( "Se procede actualizar el producto en BO_PRODUCTOS el campo MIX_WEB asigandole S.." );	
						
						boolean respBO = cataMasivoDAO.setModificarBOProducto( catalogacionMasiva );
						
						if (!respBO) {
							
							trx1.rollback();
							throw new ProductosException("Error al actualizar el producto en BO.");
							
						}
						logger.info( "Se actualiza el producto en BO_PRODUCTOS el campo MIX_WEB asigandole S.." );	
						
							
					    logger.info( "Se procede actualizar los precios y locales en FO_PRECIOS_LOCALES.." );	
					    
					    boolean respPL = cataMasivoDAO.setModificarFOPreciosLocales( catalogacionMasiva, idProducto, precioLocal );
					    	
					    if (!respPL) {
								
							trx1.rollback();
							throw new ProductosException("Error al agregar el precio al producto.");
							
						}
					    
					    logger.info( "Se agregan los precios y locales en FO_PRECIOS_LOCALES.." );	
					    
						logger.info( "::::::::::::::  Se actualizo con exito el producto  ::::::::::::::::::::" );	
						
						logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
				    	
						if (respFO && respBO && respPL && respPS) {
						
							FOProductoDTO productoFO = cataMasivoDAO.getFOProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
					    	
					    	CatalogacionMasivaLogDTO log = new CatalogacionMasivaLogDTO(); 
							log.setCod_prod(productoFO.getId());
							log.setCod_prod_bo(Long.parseLong(producto.getCodSap()));
							log.setFec_crea(Formatos.getFecHoraActual());
							log.setUsuario(usr.getLogin());
							log.setTexto("El producto SAP " +producto.getCodSap()+ " " +producto.getUnidad()+ " se actualizo en FO y el MIX.");
							int resLog = cataMasivoDAO.agregaLogProducto(log);
						
							totalProductoCargados++;
							
						}
						
					}else {
				
						logger.info( "El producto existe pero esta eliminado..." );	
						
						FOProductoDTO productoFO = cataMasivoDAO.getFOProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
				    	
				    	CatalogacionMasivaLogDTO log = new CatalogacionMasivaLogDTO(); 
						log.setCod_prod(productoFO.getId());
						log.setCod_prod_bo(Long.parseLong(producto.getCodSap()));
						log.setFec_crea(Formatos.getFecHoraActual());
						log.setUsuario(usr.getLogin());
						log.setTexto("<b>El producto " +producto.getCodSap()+ " " +producto.getUnidad()+ " esta eliminado.</b>");
						int resLog = cataMasivoDAO.agregaLogProducto(log);
					
						totalProductoCargados++;
					}
					
			    }else if ( !cataMasivoDAO.getExisteSkuFOProducto( catalogacionMasiva ) ) {
					
			    	logger.info( "El producto no existe...." );	
					
				    logger.info( "::::::::::::::  Se inicia la insercion del producto : " + catalogacionMasiva.getCodigoSap() +" " + catalogacionMasiva.getUnidadMedida() +"  ::::::::::::::::::::" );	
				    	
				    logger.info( "Se valida si el producto tiene sector de picking en BO_SECTOR...." );
			    	
			    	if ( !cataMasivoDAO.getExisteProductoSector( catalogacionMasiva.getSectorPicking(), producto.getId() ) ) {				    
			    	
		    		logger.info( "El producto no tiene sector de picking...." );
		    		
			    	logger.info( "Se procede agregar sector de picking en BO_SECTOR..." );	
			    	
			    	respPS = cataMasivoDAO.setInsertarProductoSector( catalogacionMasiva, producto.getId() );
			    	
			    	if (!respPS) {
						
						trx1.rollback();
						throw new ProductosException("Error al agregar el producto al sector de picking.");
					
					}
			    	
			    	logger.info( "Se agrega sector de picking en BO_SECTOR..." );	
			    	
			    	logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
			    	
			    	if (respPS) {
			    	
			    		FOProductoDTO productoFO = cataMasivoDAO.getFOProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
				    	
						CatalogacionMasivaLogDTO log = new CatalogacionMasivaLogDTO(); 
						log.setCod_prod(producto.getId());
						log.setCod_prod_bo(Long.parseLong(producto.getCodSap()));
						log.setFec_crea(Formatos.getFecHoraActual());
						log.setUsuario(usr.getLogin());
						log.setTexto("El producto SAP " +producto.getCodSap()+ " " +producto.getUnidad()+ " se agrego al sector de picking.");
						int resLog = cataMasivoDAO.agregaLogProducto(log);
					
					}
			    	
		    	}
		    	else {
		    		
		    		logger.info( "El producto tiene sector de picking...." );
		    		
		    		logger.info( "Se procede actualizar sector de picking en BO_SECTOR..." );	
			    	
		    		respPS = cataMasivoDAO.setModificarProductoSector( catalogacionMasiva, producto.getId() );

			    	if (!respPS) {
						
						trx1.rollback();
						throw new ProductosException("Error al actualizar el producto al sector de picking.");
					
					}
			    	
			    	logger.info( "Se actualizo sector de picking en BO_SECTOR..." );	
			    	
			    	logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
			    	
			    	if (respPS) {
			    	
			    		FOProductoDTO productoFO = cataMasivoDAO.getFOProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
				    	
						CatalogacionMasivaLogDTO log = new CatalogacionMasivaLogDTO(); 
						log.setCod_prod(producto.getId());
						log.setCod_prod_bo(Long.parseLong(producto.getCodSap()));
						log.setFec_crea(Formatos.getFecHoraActual());
						log.setUsuario(usr.getLogin());
						log.setTexto("El producto SAP " +producto.getCodSap()+ " " +producto.getUnidad()+ " se actualizo en el sector de picking.");
						int resLog = cataMasivoDAO.agregaLogProducto(log);
						
			    	}
			    	
		    	}
				    
			    	logger.info( "Se procede agregar el producto en FO_PRODUCTOS.." );	
				    
				    boolean respFO = cataMasivoDAO.setInsertarFOProducto( catalogacionMasiva, producto, usr, marca.getId(), unidadMedida.getId() );
				    	
				    if (!respFO) {
							
						trx1.rollback();
						throw new ProductosException("Error al agregar el producto en FO.");
						
					}
				    
				    logger.info( "Se agrega el producto en FO_PRODUCTOS" );	
				    
				    idProducto = cataMasivoDAO.getIdProductoFOProductos(catalogacionMasiva.getCodigoSap());
			    	
				    logger.info( "idProducto... "+idProducto );	
				    
				    logger.info( "Se procede agregar los precios y locales en FO_PRECIOS_LOCALES.." );	
				    
				    boolean respPL = cataMasivoDAO.setInsertarFOPreciosLocales( catalogacionMasiva, idProducto, precioLocal );
				    	
				    if (!respPL) {
							
						trx1.rollback();
						throw new ProductosException("Error al agregar el precio al producto.");
						
					}
				    
				    logger.info( "Se agregan los precios y locales en FO_PRECIOS_LOCALES.." );	
				    	
				    BarraAuditoriaSustitucionDTO codigoBarra = cataMasivoDAO.getCodigoBarraBOCodBarra(catalogacionMasiva, producto.getId());
				    
				    logger.info( "Se obtiene los siguientes atributos del codigo barra...." );
				    logger.info( "COD_BARRA  : " +codigoBarra.getCod_barra() );
				    logger.info( "TIP_CODBAR : " +codigoBarra.getTip_codbar());
				    	
				    logger.info( "Se procede agregar códigos de barra en FO_COD_BARRA..." );	
				    
				    boolean respCB = cataMasivoDAO.setInsertarFoCodBarra( codigoBarra, idProducto);

				    if (!respCB) {
						
				    	trx1.rollback();
						throw new ProductosException("Error al agregar codigo barra al producto.");
						
					}
				    
				    logger.info( "Se agrega códigos de barra en FO_COD_BARRA..." );	
				    	
				    logger.info( "Se procede actualizar el producto en BO_PRODUCTOS el campo MIX_WEB asigandole S.." );	
				    
				    boolean respBO = cataMasivoDAO.setModificarBOProducto( catalogacionMasiva );
				    	
				    if (!respBO) {
							
						trx1.rollback();
						throw new ProductosException("Error al actualizar el producto en BO.");
						
					}
				    
				    logger.info( "Se actualiza el producto en BO_PRODUCTOS el campo MIX_WEB asigandole S.." );	
						
					logger.info( "::::::::::::::  Se agrega con exito el producto  ::::::::::::::::::::" );	
						
				    logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
				    	
				    if (respFO && respPL && respCB && respBO) {
				    		
				    	FOProductoDTO productoFO = cataMasivoDAO.getFOProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
					    	
						CatalogacionMasivaLogDTO log = new CatalogacionMasivaLogDTO(); 
						log.setCod_prod(productoFO.getId());
						log.setCod_prod_bo(Long.parseLong(producto.getCodSap()));
						log.setFec_crea(Formatos.getFecHoraActual());
						log.setUsuario(usr.getLogin());
						log.setTexto("El producto SAP " +producto.getCodSap()+ " " +producto.getUnidad()+ " se agrego en FO y actualizo en el MIX.");
						int resLog = cataMasivoDAO.agregaLogProducto(log);
			
						 totalProductoCargados++;
				    }
				    
			    }
		    	 
		    }
	
		    logger.info( "Total Productos cargados: " +totalProductoCargados+ " de " +listProductosXls.size() );	
		    if( totalProductoCargados == listProductosXls.size() )
		    	cargaExitosa= true;
		
	} catch ( Exception e ) {
	
		try {
			trx1.rollback();
		} catch (DAOException e1) {
			logger.error(e1);
			throw new SystemException("Error al hacer rollback");
		}
		
		logger.error( "Error getAgregandoProductosAlMix: ", e );
		throw new DAOException( e );
	
	}
	
	if(!cargaExitosa){
		trx1.rollback();
		throw new ProductosException("La carga catalogacion masiva no fue finalizada.");
	}
	
	// cerramos trx
	try {
		trx1.end();
	} catch (DAOException e) {
		logger.error(e);
		throw new SystemException("Error al finalizar transacción");
	}
			
	return cargaExitosa;

}

	/**
	 * 
	 * @param listProductosXls
	 * @param usr
	 * @return
	 * @throws DAOException
	 * @throws SystemException 
	 * @throws ProductosException 
	 */
public boolean getCatalogarProducto( List listProductosXls, UserDTO usr ) throws DAOException, SystemException, ProductosException {
	
	JdbcCatalogacionMasivaDAO cataMasivoDAO = ( JdbcCatalogacionMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCatalogacionMasivaDAO();
	
	boolean cargaExitosa = false;
	int totalProductoCargados = 0;
	int totalCategoriaCargadas = 0;
	

	//	 Creamos trx
	JdbcTransaccion trx1 = new JdbcTransaccion();
			
	// Iniciamos trx
	try {
		trx1.begin();
	} catch (DAOException e1) {
		logger.error("Error al iniciar transacción");
		throw new SystemException("Error al iniciar transacción");
	} catch (SQLException e) {
		// TODO Bloque catch generado automáticamente
		e.printStackTrace();
		logger.error("Error al iniciar transacción");
		throw new SystemException("Error al iniciar transacción");
	}
	
	// Marcamos los dao's con la transacción
	try {
		cataMasivoDAO.setTrx(trx1);
	} catch (CatalogacionMasivaDAOException e2) {
		logger.error("Error al asignar transacción al dao Productos");
		throw new SystemException("Error al asignar transacción al dao Productos");
	}
	
	try {
		
		Iterator it = listProductosXls.iterator();
			
		while ( it.hasNext() ) {
 		    	
		    	BOCatalogacionMasivaDTO catalogacionMasiva = ( BOCatalogacionMasivaDTO )it.next();
		    	
		    	/*logger.info( "Se valida si el producto tiene sector de picking en BO_SECTOR...." );
		    	
		    	if ( !cataMasivoDAO.getExisteProductoSector( catalogacionMasiva.getSectorPicking(), productoBO.getId() ) ) {
		    	
		    		logger.info( "El producto no tiene sector de picking...." );
		    		
			    	logger.info( "Se procede agregar sector de picking en BO_SECTOR..." );	
			    	
			    	boolean respPS = cataMasivoDAO.setInsertarProductoSector( catalogacionMasiva, productoBO.getId() );
			    	
			    	if (!respPS) {
						
						trx1.rollback();
						throw new ProductosException("Error al agregar el producto al sector de picking.");
					
					}
			    	
			    	logger.info( "Se agrega sector de picking en BO_SECTOR..." );	
			    	
			    	logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
			    	
			    	if (respPS) {
			    	
			    		FOProductoDTO productoFO = cataMasivoDAO.getFOProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
				    	
						CatalogacionMasivaLogDTO log = new CatalogacionMasivaLogDTO(); 
						log.setCod_prod(productoFO.getId());
						log.setCod_prod_bo(Long.parseLong(productoBO.getCodSap()));
						log.setFec_crea(Formatos.getFecHoraActual());
						log.setUsuario(usr.getLogin());
						log.setTexto("El producto SAP " +productoBO.getCodSap()+ " " +productoBO.getUnidad()+ " se agrego al sector de picking.");
						int resLog = cataMasivoDAO.agregaLogProducto(log);
					
						totalProductoCargados++;
				    	
			    	}
			    	
		    	}
		    	else {
		    		
		    		logger.info( "El producto tiene sector de picking...." );
		    		
		    		logger.info( "Se procede actualizar sector de picking en BO_SECTOR..." );	
			    	
		    		boolean respPS = cataMasivoDAO.setModificarProductoSector( catalogacionMasiva, productoBO.getId() );

			    	if (!respPS) {
						
						trx1.rollback();
						throw new ProductosException("Error al actualizar el producto al sector de picking.");
					
					}
			    	
			    	logger.info( "Se actualizo sector de picking en BO_SECTOR..." );	
			    	
			    	logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
			    	
			    	if (respPS) {
			    	
			    		FOProductoDTO productoFO = cataMasivoDAO.getFOProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
				    	
						CatalogacionMasivaLogDTO log = new CatalogacionMasivaLogDTO(); 
						log.setCod_prod(productoFO.getId());
						log.setCod_prod_bo(Long.parseLong(productoBO.getCodSap()));
						log.setFec_crea(Formatos.getFecHoraActual());
						log.setUsuario(usr.getLogin());
						log.setTexto("El producto SAP " +productoBO.getCodSap()+ " " +productoBO.getUnidad()+ " se actualizo en el sector de picking.");
						int resLog = cataMasivoDAO.agregaLogProducto(log);
						
						totalProductoCargados++;
			    	}
			    	
		    	}*/
		    	

		    	if ( cataMasivoDAO.getExisteSkuFOProducto( catalogacionMasiva )) {
					
					if ( cataMasivoDAO.getProEstadoProducto( catalogacionMasiva ) ) {
						
						logger.info( "::::::::::::::  Se inicia insercion y/o modificacion de asociacion : " + catalogacionMasiva.getCodigoSap() +" " + catalogacionMasiva.getUnidadMedida() +"  ::::::::::::::::::::" );	
						   
				    	BOProductoDTO productoBO = cataMasivoDAO.getBoProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
				    	logger.info( "Se obtiene los siguientes atributos del producto...." );
				    	logger.info( "ID_PRODUCTO : " +productoBO.getId() );
				    	logger.info( "DES_CORTA : " +productoBO.getDescripcion_corta());
				    	logger.info( "UNI_MED : " +productoBO.getUnidad());
				    	logger.info( "COD_PROD1 : " +productoBO.getCodSap());
				    	logger.info( "DES_LARGA : " +productoBO.getDescripcion_larga());
		    	
		    			Iterator itc = catalogacionMasiva.getCategorias().iterator();
		    	
						while(itc.hasNext()) {
		    		
			    		String idCategoria = (String) itc.next();
			    	
			    		FOProductoDTO productoFO = cataMasivoDAO.getFOProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
		    		
			    		logger.info( "Se obtiene los siguientes atributos del producto...." );
		    			
			    		logger.info( "PRO_ID : " +productoFO.getId() );
		    			    	
			    		logger.info( "Se valida si el idCategoria : " +idCategoria+ " esta asociado al Producto : " +productoFO.getId()+ " en FO_PRODUCTOS_CATEGORIAS...." );
			    				
			    		if ( !cataMasivoDAO.getExisteProductoCategoria( idCategoria, productoFO.getId() )) {
			    			    	
			    			logger.info( "El producto no esta asociado...." );
			    			
			    			logger.info( "Se procede agregar asociacion de producto a categoria..." );
			    			
			    			boolean respPC = cataMasivoDAO.setInsertarProductoCategoria( idCategoria, productoFO.getId() );
			    					
			    			if (!respPC) {
			    						
			    				trx1.rollback();
			    				throw new ProductosException("Error al agregar el producto al una categoria.");
			    					
			    			}
			    					
			    			logger.info( "Se agrego sector de picking en BO_SECTOR..." );	
			    					
			    			logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
			    			    	
			    			if(respPC) {
			    					
			    			
			    				CatalogacionMasivaLogDTO log = new CatalogacionMasivaLogDTO(); 
		    					log.setCod_prod(productoFO.getId());
		    					log.setCod_prod_bo(Long.parseLong(productoBO.getCodSap()));
		    					log.setFec_crea(Formatos.getFecHoraActual());
		    					log.setUsuario(usr.getLogin());
		    					log.setTexto("El producto SAP " +productoBO.getCodSap()+ " " +productoBO.getUnidad()+ " se agrego a una categoria.");
		    					int resLog = cataMasivoDAO.agregaLogProducto(log);
		    					
			    				totalCategoriaCargadas++;
				    			
			    			}
			    					
			    		}else {
			    		    		
			    		    logger.info( "El producto esta asociado...." );
			    					
			    			logger.info( "Se procede actualizar asociacion de producto a categoria..." );
			    			
			    			boolean respPC = cataMasivoDAO.setModificarProductoCategoria( idCategoria, productoFO.getId() );
	
			    			if (!respPC) {
			    						
			    				trx1.rollback();
			    				throw new ProductosException("Error al actualizar el producto a una categoria.");
			    					
			    			}
	
			    			logger.info( "Se actualizo sector de picking en BO_SECTOR..." );	
			    					
			    			logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
			    			    	
			    			if (respPC) {
			    						
			    						
			    				CatalogacionMasivaLogDTO log = new CatalogacionMasivaLogDTO(); 
		    					log.setCod_prod(productoFO.getId());
		    					log.setCod_prod_bo(Long.parseLong(productoBO.getCodSap()));
		    					log.setFec_crea(Formatos.getFecHoraActual());
		    					log.setUsuario(usr.getLogin());
		    					log.setTexto("El producto SAP " +productoBO.getCodSap()+ " " +productoBO.getUnidad()+ " se actualizo la categoria.");
		    					int resLog = cataMasivoDAO.agregaLogProducto(log);
		    					
		    					totalCategoriaCargadas++;
		    					
			    			}
		    			
			    		}
		    		
		    			logger.info( "Total Categorias cargadas: " +totalCategoriaCargadas+ " de " +catalogacionMasiva.getCategorias().size() );
			    	 
			    		if(totalCategoriaCargadas == catalogacionMasiva.getCategorias().size()) {
			    	
			    		logger.info( "::::::::::::::  Se agrega y/o modifica asociacion con exito el producto  ::::::::::::::::::::" );	
			    		totalCategoriaCargadas = 0;
			    		totalProductoCargados++;
			    		cargaExitosa = true;
			    	
			    		}
		    		
		    			}
		    	
					}
					
					totalProductoCargados++;
		    	
		    	}
		
		}
		
		//logger.info( "Total Productos cargados: " +totalProductoCargados+ " de " +listProductosXls.size() );	
		  
		//if ( totalProductoCargados == listProductosXls.size())
			//cargaExitosa = true;
		
	
		
	} catch ( Exception e ) {
	
		try {
			trx1.rollback();
		} catch (DAOException e1) {
			logger.error(e1);
			throw new SystemException("Error al hacer rollback");
		}
		
		logger.error( "Error getCatalogarProducto: ", e );
		throw new DAOException( e );
	
	}
	
	if(!cargaExitosa){
		trx1.rollback();
		throw new ProductosException("La carga catalogacion masiva no fue finalizada.");
	}
	
	// cerramos trx
	try {
		trx1.end();
	} catch (DAOException e) {
		logger.error(e);
		throw new SystemException("Error al finalizar transacción");
	}

	return cargaExitosa;
}

/**
 * 
 * @param listaCategorias
 * @return
 * @throws DAOException
 */
public String getExisteCategoria( List listaCategorias ) throws DAOException {
	
	JdbcCatalogacionMasivaDAO cataMasivoDAO = ( JdbcCatalogacionMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCatalogacionMasivaDAO();

	try {
	
		return cataMasivoDAO.getExisteCategoria( listaCategorias ); 
		
		
	} catch ( Exception e ) {
	
		logger.error( "Error getExisteCategoria: ", e );
		throw new DAOException( e );
	
	}

}

public List getLogByProductId(List productosCatalogacionMasiva) throws ProductosException{
	List logs = new ArrayList();
	try{
		logger.debug("en getLogByProductId");
		JdbcCatalogacionMasivaDAO cataMasivoDAO = ( JdbcCatalogacionMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCatalogacionMasivaDAO();
		
		List listaLog= new ArrayList();
		listaLog = (List) cataMasivoDAO.getLogByProductId(productosCatalogacionMasiva);
		ProductoLogEntity log = null;
		for (int i = 0; i < listaLog.size(); i++) {
			log = null;
			log = (ProductoLogEntity) listaLog.get(i);
			String fecha = "";
			if(log.getFec_crea()!=null)
				fecha = log.getFec_crea().toString();
			CatalogacionMasivaLogDTO logdto = new CatalogacionMasivaLogDTO(log.getId().longValue(), log.getCod_prod().longValue(),
					fecha, log.getUsuario(), log.getTexto());
			logs.add(logdto);
		}
		
	}catch(CatalogacionMasivaDAOException ex){
		logger.debug("Problema getLogByProductId:"+ex);
		throw new ProductosException(ex);
	}
	return logs;
}

}
