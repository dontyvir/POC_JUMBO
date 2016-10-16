package cl.bbr.boc.ctrl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cl.bbr.boc.dao.DAOFactoryParametros;
import cl.bbr.boc.dao.jdbc.JdbcStockOnLineDAO;
import cl.bbr.boc.dto.BOStockONLineDTO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * 
 * @author jolazogu
 * 
 */
public class StockOnLineCtrl {

	Logging logger = new Logging(this);

	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws DAOException
	 */
	public List getListaProductosPorLocal( long idLocal ) throws DAOException {
		
		JdbcStockOnLineDAO stockDAO = ( JdbcStockOnLineDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getStockOnLineDAO();
		
		try {
			
			List stockOnLine = new ArrayList();
			
			// lista todos los productos de la tabla FO_PRECIOS_LOCALES
			List productosFO = stockDAO.getListaProductosFoPreciosLocalesPorLocal( idLocal );
			
			// lista todos los productos de la tabla BO_STOCK_ONLINE	
			List productosBO = stockDAO.getListaProductosBoStockOnLinePorLocal( idLocal );
			
			// recorremos las lista, comparamos y obtener la lista real a exportar
			Iterator itf = productosFO.iterator();
			
			while (itf.hasNext()) {
				
				BOStockONLineDTO stockOnLineFO = (BOStockONLineDTO)itf.next();
				
				Iterator itb = productosBO.iterator();
				
				while (itb.hasNext()) {
				
					BOStockONLineDTO stockOnLineBO = (BOStockONLineDTO)itb.next();
						
					if ( stockOnLineFO.getIdCatProd().equals(stockOnLineBO.getIdCatProd()) && 
						 stockOnLineFO.getIdProducto() == stockOnLineBO.getIdProducto() && 
						 stockOnLineFO.getSkuProducto().equals(stockOnLineBO.getSkuProducto() ) ) {
						
						stockOnLineFO.setEstado( stockOnLineBO.getEstado() );
						stockOnLineFO.setStockMinimo( stockOnLineBO.getStockMinimo() );
						stockOnLineFO.setModo( stockOnLineBO.getModo() );
						
					}
					
				}
				
				stockOnLine.add(stockOnLineFO);
				
			}
			
			return stockOnLine;
			
		} catch ( Exception e ) {
			
			logger.error( "Error getListaProductosPorLocal: ", e );
			throw new DAOException( e );
			
		}
	
	}
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws DAOException
	 */
	public long[] cantidadDeProductosTendranCambios( long idLocal ) throws DAOException {
		
		JdbcStockOnLineDAO stockDAO = ( JdbcStockOnLineDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getStockOnLineDAO();
		
		try {
			 
			List productos = stockDAO.getProductosPorLocal( idLocal );
			
			return stockDAO.cantidadDeProductosTendranCambios( idLocal, productos );
		
		} catch ( Exception e ) {
			
			logger.error( "Error cantidadDeProductosTendranCambios: ", e );
			throw new DAOException( e );
		
		 }
		
	 }
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws DAOException
	 */
	public long[] cantidadDeProductosActualmente( long idLocal ) throws DAOException {
		
		JdbcStockOnLineDAO stockDAO = ( JdbcStockOnLineDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getStockOnLineDAO();
		
		try {
			 
			return stockDAO.cantidadDeProductosActualmente( idLocal );
		
		} catch ( Exception e ) {
			
			logger.error( "Error cantidadDeProductosTendranCambios: ", e );
			throw new DAOException( e );
		
		 }
		
	 }
	

	public int getTotalMaestra( long idLocal ) throws DAOException {
		
		JdbcStockOnLineDAO stockDAO = ( JdbcStockOnLineDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getStockOnLineDAO();
		
		try {
			 
			return stockDAO.getTotalMaestra( idLocal );
		
		} catch ( Exception e ) {
			
			logger.error( "Error cantidadDeProductosTendranCambios: ", e );
			throw new DAOException( e );
		
		 }
		
	 }
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws DAOException
	 */
	public List getDetalleSemiautomatico( long idLocal ) throws DAOException {
		
		JdbcStockOnLineDAO stockDAO = ( JdbcStockOnLineDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getStockOnLineDAO();
		
	 	List listaDetalleSemiautomatico = new ArrayList();
		
	 	try {
	 		
			listaDetalleSemiautomatico = stockDAO.getDetalleSemiautomatico( idLocal );
		
	 	} catch ( Exception e ) {
		
	 		logger.error( "Error getDetalleSemiautomatico: ", e );
			throw new DAOException( e );
		
	 	}
		
		return listaDetalleSemiautomatico;
	
	}
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws DAOException
	 */
	public List getProductosDetalleSemiautomatico( long idLocal ) throws DAOException {
		
		JdbcStockOnLineDAO stockDAO = ( JdbcStockOnLineDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getStockOnLineDAO();
		
	 	List listaProductosDetalleSemiautomatico = new ArrayList();
		
	 	try {
	 		
	 		List productos = stockDAO.getProductosPorLocal( idLocal );
	 		
	 		listaProductosDetalleSemiautomatico = stockDAO.getProductosDetalleSemiautomatico( idLocal, productos );
		
	 	} catch ( Exception e ) {
		
	 		logger.error( "Error getProductosDetalleSemiautomatico: ", e );
			throw new DAOException( e );
		
	 	}
		
		return listaProductosDetalleSemiautomatico;
	
	}


	/**
	 * 
	 * @param idLocal
	 * @throws DAOException
	 */
	public void getLimpiarTablaStockOnlinePorLocal( long idLocal ) throws DAOException {
		
		JdbcStockOnLineDAO stockDAO = ( JdbcStockOnLineDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getStockOnLineDAO();
		
		try {
			
			stockDAO.getLimpiarTablaStockOnlinePorLocal( idLocal );
		
		} catch ( Exception e ) {
			
			logger.error( "Error getLimpiarTablaStockOnlinePorLocal: ", e );
			throw new DAOException(e);
		
		}
	
	}
	
	/**
	 * 
	 * @param idLocal
	 * @throws DAOException
	 */
	public void getConfirmarSemiautomaticoStockOnlinePorLocal( long idLocal ) throws DAOException {
		
		JdbcStockOnLineDAO stockDAO = ( JdbcStockOnLineDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getStockOnLineDAO();
		
		try {
			
			stockDAO.getConfirmarSemiautomaticoStockOnlinePorLocal( idLocal );
		
		} catch ( Exception e ) {
			
			logger.error( "Error getLimpiarTablaStockOnlinePorLocal: ", e );
			throw new DAOException(e);
		
		}
	
	}
	
	/**
	 * 
	 * @param archivoFiltrado
	 * @param idLocal
	 * @return
	 * @throws DAOException
	 */
	public boolean setInsertRegistroExcelToBD( List archivoFiltrado, long idLocal ) throws DAOException {
		
		JdbcStockOnLineDAO stockDAO = ( JdbcStockOnLineDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getStockOnLineDAO();
		
		try {
			
			return stockDAO.setInsertRegistroExcelToBD(archivoFiltrado, idLocal);
		
		} catch (Exception e) {
			
			logger.error("Error setInsertRegistroExcelToBD: ", e);
			throw new DAOException(e);
		
		}
	
	}
	
	/**
	 * 
	 * @param cod_prod1
	 * @param uniMed
	 * @return
	 * @throws DAOException
	 */
	public boolean getProductoBySkuUniMed( String cod_prod1, String uniMed ) throws DAOException {
		
		JdbcStockOnLineDAO stockDAO = ( JdbcStockOnLineDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getStockOnLineDAO();
		
		try {
		
			return stockDAO.getProductoBySkuUniMed( cod_prod1, uniMed );
		
		} catch ( Exception e ) {
		
			logger.error( "Error getProductoBySkuUniMed: ", e );
			throw new DAOException( e );
		
		}
	
	}
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws DAOException
	 */
	public boolean setEjecutaProcesoSemiAutomatico( long idLocal ) throws DAOException {
		
		JdbcStockOnLineDAO stockDAO = ( JdbcStockOnLineDAO ) DAOFactoryParametros.getDAOFactory( DAOFactoryParametros.JDBC ).getStockOnLineDAO();
		
		try {
			
			return stockDAO.setEjecutaProcesoSemiAutomatico( idLocal );
		
		} catch ( Exception e ) {
			
			logger.error( "Error getConfiguracionStockOnLineSKU: ", e );
			throw new DAOException( e );
		
		}
	
	}
	
}
