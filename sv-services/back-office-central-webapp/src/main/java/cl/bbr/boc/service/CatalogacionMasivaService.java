package cl.bbr.boc.service;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.boc.ctrl.CatalogacionMasivaCtrl;
import cl.bbr.boc.ctrl.StockOnLineCtrl;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jolazogu
 */
public class CatalogacionMasivaService {
	
	Logging logger = new Logging(this); 
	

	/**
	 * 
	 * @param marca
	 * @return
	 * @throws Exception
	 */
	public boolean getExisteMarcaProducto( String marca ) throws Exception {
		
	    CatalogacionMasivaCtrl param = new CatalogacionMasivaCtrl();
	   
		try {
			
			return param.getExisteMarcaProducto( marca );
			 
		} catch (Exception e) {
			
			logger.error( "Error getExisteMarcaProducto: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	
	/**
	 * 
	 * @param unidadMedidaPPM
	 * @return
	 * @throws Exception
	 */
	public boolean getExisteUnidadMedidaPPM( String unidadMedidaPPM ) throws Exception {
		
	    CatalogacionMasivaCtrl param = new CatalogacionMasivaCtrl();
	   
		try {
			
			return param.getExisteUnidadMedidaPPM( unidadMedidaPPM );
			 
		} catch (Exception e) {
			
			logger.error( "Error getExisteUnidadMedidaPPM: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	
	/**
	 * 
	 * @param sectorPicking
	 * @return
	 * @throws Exception
	 */
	public boolean getExisteSectorPicking( String sectorPicking ) throws Exception {
		
	    CatalogacionMasivaCtrl param = new CatalogacionMasivaCtrl();
	   
		try {
			
			return param.getExisteSectorPicking( sectorPicking );
			 
		} catch (Exception e) {
			
			logger.error( "Error getExisteSectorPicking: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	
	/**
	 * 
	 * @param codLocal
	 * @return
	 * @throws Exception
	 */
	public String getLocalNoExiste( String codLocal ) throws Exception {
		
	    CatalogacionMasivaCtrl param = new CatalogacionMasivaCtrl();
	   
		try {
			
			return param.getLocalNoExiste( codLocal );
			 
		} catch (Exception e) {
			
			logger.error( "Error getLocalNoExiste: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	
	/**
	 * 
	 * @param cod_prod1
	 * @param unidadMedida
	 * @return
	 * @throws Exception
	 */
	public String getValidandoDatosBO( String cod_prod1, String unidadMedida ) throws Exception {
		
	    CatalogacionMasivaCtrl param = new CatalogacionMasivaCtrl();
	   
		try {
			
			return param.getValidandoDatosBO( cod_prod1, unidadMedida );
			 
		} catch (Exception e) {
			
			logger.error( "Error getValidandoDatosBO: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	
	/**
	 * 
	 * @param listProductosXls
	 * @param usr
	 * @return
	 * @throws Exception
	 */
	public boolean getAgregandoProductosAlMix( List listProductosXls, UserDTO usr ) throws Exception {
		
	    CatalogacionMasivaCtrl param = new CatalogacionMasivaCtrl();
	   
		try {
			
			return param.getAgregandoProductosAlMix( listProductosXls, usr );
			 
		} catch (Exception e) {
			
			logger.error( "Error getAgregandoProductosAlMix: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	
	/**
	 * 
	 * @param listProductosXls
	 * @param usr
	 * @return
	 * @throws Exception
	 */
	public boolean getCatalogarProducto( List listProductosXls, UserDTO usr ) throws Exception {
		
	    CatalogacionMasivaCtrl param = new CatalogacionMasivaCtrl();
	   
		try {
			
			return param.getCatalogarProducto( listProductosXls, usr );
			 
		} catch (Exception e) {
			
			logger.error( "Error getCatalogarProducto: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	
	/**
	 * 
	 * @param listaCategorias
	 * @return
	 * @throws Exception
	 */
	public String getExisteCategoria( List listaCategorias ) throws Exception {
		
	    CatalogacionMasivaCtrl param = new CatalogacionMasivaCtrl();
	   
		try {
			
			return param.getExisteCategoria( listaCategorias );
			 
		} catch (Exception e) {
			
			logger.error( "Error getExisteCategoria: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	
	public List getLogByProductId(List productosCatalogacionMasiva)throws ServiceException {	
		CatalogacionMasivaCtrl prods = new CatalogacionMasivaCtrl();
		List mrcs = null;
		try{
			mrcs = prods.getLogByProductId(productosCatalogacionMasiva); 
		}catch(Exception ex){
			logger.debug( "Problemas con getLogByProductId");
			throw new ServiceException(ex);
		}
	
		return mrcs;	
	}
	
}

