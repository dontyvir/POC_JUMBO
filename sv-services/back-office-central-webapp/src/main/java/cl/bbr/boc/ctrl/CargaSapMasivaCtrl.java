package cl.bbr.boc.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cl.bbr.boc.dao.DAOFactoryParametros;
import cl.bbr.boc.dao.jdbc.JdbcCargaSapMasivaDAO;
import cl.bbr.boc.dto.BOCargaSapMasivaDTO;
import cl.bbr.boc.dto.BOProductoDTO;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.ProductoLogEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.CargaSapMasivaLogDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CargaSapMasivaDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * 
 * @author jolazogu
 * 
 */
public class CargaSapMasivaCtrl {

	Logging logger = new Logging(this);

	
	/**
	 * 
	 * @param idGrupo
	 * @return
	 * @throws DAOException
	 */
	public boolean getExisteIdGrupo( String idGrupo ) throws DAOException {
		
		JdbcCargaSapMasivaDAO cargaSapMasivoDAO = ( JdbcCargaSapMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCargaSapMasivaDAO();
		
		try {
		
			return cargaSapMasivoDAO.getExisteIdGrupo( idGrupo );
		
		} catch ( Exception e ) {
		
			logger.error( "Error getExisteIdGrupo: ", e );
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
	public boolean getInsertandoBOProducto( List listProductosXls, UserDTO usr ) throws DAOException, SystemException, ProductosException {
	
	JdbcCargaSapMasivaDAO cargaSapMasivoDAO = ( JdbcCargaSapMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCargaSapMasivaDAO();
	
	boolean cargaExitosa = false;
	int totalProductoCargados = 0;
	int totalProductoNoCargados = 0;
	
	boolean resp = false;
	

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
		cargaSapMasivoDAO.setTrx(trx1);
	} catch (CargaSapMasivaDAOException e2) {
		logger.error("Error al asignar transacción al dao Productos");
		throw new SystemException("Error al asignar transacción al dao Productos");
	}
	
	try {
	
		Iterator it = listProductosXls.iterator();
			
		while ( it.hasNext() ) {
 		    
			String mensajeLog = "";
			
			BOCargaSapMasivaDTO cargaSapMasiva = ( BOCargaSapMasivaDTO )it.next();
		    	
	    	logger.info( "::::::::::::::   Se valida si existe el producto :" + cargaSapMasiva.getCodigoSap() +" " + cargaSapMasiva.getUnidadMedida() +"  ::::::::::::::::::::" );	

	    	if ( !cargaSapMasivoDAO.getExisteSkuBOProductoPrecioLocal( cargaSapMasiva ) ) {
					
	    		logger.info( "El producto no tiene precios asociados a locales...." );	
					
			    
			    if ( !cargaSapMasivoDAO.getExisteSkuBOProducto( cargaSapMasiva ) ) {
			   
			    	logger.info( "::::::::::::::  Se inicia la insercion del producto : " + cargaSapMasiva.getCodigoSap() +" " + cargaSapMasiva.getUnidadMedida() +"  ::::::::::::::::::::" );	
					    
					logger.info( "Se procede agregar producto en BO_PRODUCTOS..." );	
					   
			    	resp = cargaSapMasivoDAO.setInsertarBOProducto( cargaSapMasiva );
			    	
			    	if (!resp) {
							
			    		trx1.rollback();
			    		throw new ProductosException("Error al agregar el producto al BO_PRODUCTOS.");
						
			    	}
			    	
			    	if (resp) {
			    		
			    		BOProductoDTO productoBO = cargaSapMasivoDAO.getBoProducto(cargaSapMasiva.getCodigoSap(), cargaSapMasiva.getUnidadMedida());
			    		
			    		mensajeLog = "SAP " +productoBO.getCodSap()+ " " +productoBO.getUnidad()+ ", informa lo siguiente:<br>";
			    		
			    		mensajeLog = mensajeLog + "- El producto se creo con exito.<br>";
			    	
			    	}
			    
			    }else {
			    	
			    	BOProductoDTO productoBO = cargaSapMasivoDAO.getBoProducto(cargaSapMasiva.getCodigoSap(), cargaSapMasiva.getUnidadMedida());
				    	
				    mensajeLog = "SAP " +productoBO.getCodSap()+ " " +productoBO.getUnidad()+ ", informa lo siguiente:<br>";
			    		
			    	mensajeLog = mensajeLog + "- El producto existe.<br>";
			    	
			    }
		    	
		    	
			    BOProductoDTO productoBO = cargaSapMasivoDAO.getBoProducto(cargaSapMasiva.getCodigoSap(), cargaSapMasiva.getUnidadMedida());
			    
			    if ( !cargaSapMasivoDAO.getExisteBOCodBarra( cargaSapMasiva, productoBO.getId() ) ){
			    	
			    	logger.info( "::::::::::::::  Se inicia la insercion del codigo barra : " + cargaSapMasiva.getCodBarra() + "  ::::::::::::::::::::" );	
				    
				    logger.info( "Se procede agregar codigo barra en BO_CODBARRA..." );	
			    	
				    resp = cargaSapMasivoDAO.setInsertarBOCodBarra( cargaSapMasiva, productoBO.getId() );
			    	
				    if (!resp) {
							
				    	trx1.rollback();
				    	throw new ProductosException("Error al agregar codigo barra al BO_CODBARRA.");
					
				    }
				    
				    mensajeLog = mensajeLog + "- El codigo barra se creo con exito.<br>";
			    
			    }else {
			    	
			    	mensajeLog = mensajeLog + "- El producto tiene Codigo Barra.<br>";
			    	resp = true;
			    }
			    
			    	
			    if (resp)
			    	
		    		logger.info( "::::::::::::::  Se inicia la insercion de precios : " + cargaSapMasiva.getPrecio() + "  ::::::::::::::::::::" );	
			    
			    	logger.info( "Se procede agregar precios en BO_PRECIOS..." );	
		    	
			    	Iterator cs = cargaSapMasiva.getLocalesON().entrySet().iterator();
				
			    	while (cs.hasNext()) {
				    	
			    		Map.Entry e = (Map.Entry)cs.next();
				    	
			    		int idLocal = cargaSapMasivoDAO.getIdLocal(String.valueOf(e.getKey()));
			    	
			    		resp = cargaSapMasivoDAO.setInsertarBOPrecios( cargaSapMasiva, productoBO.getId(), idLocal, String.valueOf(e.getKey()) );
			    	
			    		if (!resp) {
							
			    			trx1.rollback();
			    			throw new ProductosException("Error al agregar codigo barra al BO_CODBARRA.");
						
			    		}
				    	
			    		resp = cargaSapMasivoDAO.setConPrecio( cargaSapMasiva, productoBO.getId() );
				    	
			    		if (!resp) {
							
			    			trx1.rollback();
			    			throw new ProductosException("Error al actualizar CON_PRECIO en BO_PRODUCTOS.");
						
			    		}
				    	
					}
				    
				if (resp)  {
			    	
					mensajeLog = mensajeLog + "- Los precios se crearon con exito en los locales seleccionados.<br>";
			    
					logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
			    	
				    CargaSapMasivaLogDTO log = new CargaSapMasivaLogDTO(); 
						
				    log.setCod_prod(productoBO.getId());
				    log.setCod_prod_bo(Long.parseLong(productoBO.getCodSap()));
				    log.setFec_crea(Formatos.getFecHoraActual());
				    log.setUsuario(usr.getLogin());
				    log.setTexto(mensajeLog);
				    int resLog = cargaSapMasivoDAO.agregaLogProducto(log);
			    	
				}
				
		    }else {
		    		
		    	logger.info( "El producto existe...." );	
		    		
		    	BOProductoDTO productoBO = cargaSapMasivoDAO.getBoProducto(cargaSapMasiva.getCodigoSap(), cargaSapMasiva.getUnidadMedida());
		    	
	    		mensajeLog = "SAP " +productoBO.getCodSap()+ " " +productoBO.getUnidad()+ ", se informa:<br>";
	    		
		    	mensajeLog = mensajeLog + "- El producto existe.<br>";
		    	
		    	mensajeLog = mensajeLog + "- El producto tiene precios asociados a locales.<br>";
		    	
		    	logger.info( "::::::::::::::  Se agrega Log  ::::::::::::::::::::" );	
		    	
			    CargaSapMasivaLogDTO log = new CargaSapMasivaLogDTO(); 
					
			    log.setCod_prod(productoBO.getId());
			    log.setCod_prod_bo(Long.parseLong(productoBO.getCodSap()));
			    log.setFec_crea(Formatos.getFecHoraActual());
			    log.setUsuario(usr.getLogin());
			    log.setTexto(mensajeLog);
			    int resLog = cargaSapMasivoDAO.agregaLogProducto(log);
				
	    	}
	    	
		}
				 
		logger.info( "Total Productos cargados: " +totalProductoCargados+ " de " +listProductosXls.size() );
		logger.info( "Total Productos no cargados: " +totalProductoNoCargados+ " de " +listProductosXls.size() );
		
		   // if (listProductosXls.size() == (totalProductoCargados + totalProductoNoCargados))
				cargaExitosa= true;
	
	} catch ( Exception e ) {
	
		try {
			trx1.rollback();
		} catch (DAOException e1) {
			logger.error(e1);
			throw new SystemException("Error al hacer rollback");
		}
		
		logger.error( "Error getInsertandoBOProducto: ", e );
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
	 * @param productosCargaSapMasiva
	 * @return
	 * @throws ProductosException
	 */
	public List getLogByProductId(List productosCargaSapMasiva) throws ProductosException{
	
		List logs = new ArrayList();
	
		try{
		
			logger.debug("en getLogByProductId");
			JdbcCargaSapMasivaDAO cargaSapMasivoDAO = ( JdbcCargaSapMasivaDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getCargaSapMasivaDAO();
		
			List listaLog= new ArrayList();
			listaLog = (List) cargaSapMasivoDAO.getLogByProductId(productosCargaSapMasiva);
			ProductoLogEntity log = null;
			
			for (int i = 0; i < listaLog.size(); i++) {
				
				log = null;
				log = (ProductoLogEntity) listaLog.get(i);
				String fecha = "";
				
				if(log.getFec_crea()!=null)
					fecha = log.getFec_crea().toString();
			
				CargaSapMasivaLogDTO logdto = new CargaSapMasivaLogDTO(log.getId().longValue(), log.getCod_prod().longValue(),
						fecha, log.getUsuario(), log.getTexto());
			
				logs.add(logdto);
		
			}
		
		}catch(CargaSapMasivaDAOException ex){
		
			logger.debug("Problema getLogByProductId:"+ex);
			throw new ProductosException(ex);
	
		}
	
		return logs;

	}

}
