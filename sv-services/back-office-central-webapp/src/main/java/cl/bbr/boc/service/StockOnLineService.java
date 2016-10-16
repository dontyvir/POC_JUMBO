package cl.bbr.boc.service;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.boc.ctrl.StockOnLineCtrl;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * @author jolazogu
 */
public class StockOnLineService {
	
	Logging logger = new Logging(this); 
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws Exception
	 */
	public List getListaProductosPorLocal( long idLocal ) throws Exception {
		  
		StockOnLineCtrl param = new StockOnLineCtrl();
		   
		try {
				
			return param.getListaProductosPorLocal( idLocal );
				 
		} catch ( Exception e ) {
				
			logger.error( "Error getListaProductosPorLocal: ", e );
			throw new ServiceException( e );
			
		}
			
	}
	  
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws Exception
	 */
	public long[] cantidadDeProductosTendranCambios( long idLocal ) throws Exception {
		
		StockOnLineCtrl param = new StockOnLineCtrl();
	  	
		try {
			
			return param.cantidadDeProductosTendranCambios( idLocal ); 
		
		} catch ( Exception e ) {
			
			logger.error("Error cantidadDeProductosTendranCambios: ", e);
			throw new ServiceException( e );
		  
		}

	  }
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws Exception
	 */
	public long[] cantidadDeProductosActualmente( long idLocal ) throws Exception {
		
		StockOnLineCtrl param = new StockOnLineCtrl();
	  	
		try {
			
			return param.cantidadDeProductosActualmente( idLocal ); 
		
		} catch ( Exception e ) {
			
			logger.error("Error cantidadDeProductosActualmente: ", e);
			throw new ServiceException( e );
		  
		}

	  }
	
	
	public int getTotalMaestra( long idLocal ) throws Exception {
		
		StockOnLineCtrl param = new StockOnLineCtrl();
	  	
		try {
			
			return param.getTotalMaestra( idLocal ); 
		
		} catch ( Exception e ) {
			
			logger.error("Error cantidadDeProductosActualmente: ", e);
			throw new ServiceException( e );
		  
		}

	  }

	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws Exception
	 */
	public List getDetalleSemiautomatico( long idLocal ) throws Exception {
		
		StockOnLineCtrl param = new StockOnLineCtrl();
		
		List listaDetalleSemiautomatico = new ArrayList();
		
		try {
			
			listaDetalleSemiautomatico = param.getDetalleSemiautomatico( idLocal );
			 
		} catch ( Exception e ) {
			
			logger.error( "Error getDetalleSemiautomatico: ", e );
			throw new ServiceException( e );
		
		}
		
		return listaDetalleSemiautomatico;
	
	}  
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws Exception
	 */
	public List getProductosDetalleSemiautomatico( long idLocal ) throws Exception {
		
		StockOnLineCtrl param = new StockOnLineCtrl();
		
		List listaProductosDetalleSemiautomatico = new ArrayList();
		
		try {
			
			listaProductosDetalleSemiautomatico = param.getProductosDetalleSemiautomatico( idLocal );
			 
		} catch ( Exception e ) {
			
			logger.error( "Error getProductosDetalleSemiautomatico: ", e );
			throw new ServiceException( e );
		
		}
		
		return listaProductosDetalleSemiautomatico;
	
	}  
	
	/**
	 * 
	 * @param idLocal
	 * @throws Exception
	 */
	public void getLimpiarTablaStockOnlinePorLocal( long idLocal ) throws Exception {
	    
		StockOnLineCtrl param = new StockOnLineCtrl();
	   
		try {
			
			param.getLimpiarTablaStockOnlinePorLocal( idLocal );
			 
		} catch ( Exception e ) {
			
			logger.error( "Error getLimpiarTablaStockOnlinePorLocal: ", e );
			throw new ServiceException(e);
		
		}
		
    }
	
	/**
	 * 
	 * @param idLocal
	 * @throws Exception
	 */
	public void getConfirmarSemiautomaticoStockOnlinePorLocal( long idLocal ) throws Exception {
	    
		StockOnLineCtrl param = new StockOnLineCtrl();
	   
		try {
			
			param.getConfirmarSemiautomaticoStockOnlinePorLocal( idLocal );
			 
		} catch ( Exception e ) {
			
			logger.error( "Error getLimpiarTablaStockOnlinePorLocal: ", e );
			throw new ServiceException(e);
		
		}
		
    }
	
	/**
	 * 
	 * @param archivoFiltrado
	 * @param idLocal
	 * @return
	 * @throws Exception
	 */
	public boolean setInsertRegistroExcelToBD( List archivoFiltrado, long idLocal ) throws Exception {
		
	    StockOnLineCtrl param = new StockOnLineCtrl();
	   
		try {
		
			return param.setInsertRegistroExcelToBD(archivoFiltrado, idLocal);
			 
		} catch (Exception e) {
			
			logger.error( "Error setInsertRegistroExcelToBD: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	
	/**
	 * 
	 * @param cod_prod1
	 * @param uniMed
	 * @return
	 * @throws Exception
	 */
	public boolean getProductoBySkuUniMed( String cod_prod1, String uniMed ) throws Exception {
		
	    StockOnLineCtrl param = new StockOnLineCtrl();
	   
		try {
			
			return param.getProductoBySkuUniMed(cod_prod1,uniMed);
			 
		} catch (Exception e) {
			
			logger.error( "Error getProductoBySkuUniMed: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	 
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws Exception
	 */
	public boolean setEjecutaProcesoSemiAutomatico( long idLocal ) throws Exception {
		
	    StockOnLineCtrl param = new StockOnLineCtrl();
	   
		try {
			
			return param.setEjecutaProcesoSemiAutomatico(idLocal);
			 
		} catch ( Exception e ) {
			
			logger.error( "Error setEjecutaProcesoSemiAutomatico: ", e );
			throw new ServiceException( e );
		
		}
		
	}
	
}

