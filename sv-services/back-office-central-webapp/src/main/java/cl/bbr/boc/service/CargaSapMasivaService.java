package cl.bbr.boc.service;

import java.util.List;

import cl.bbr.boc.ctrl.CargaSapMasivaCtrl;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jolazogu
 */
public class CargaSapMasivaService {
	
	Logging logger = new Logging(this); 
	
	/**
	 * 
	 * @param listProductosXls
	 * @param usr
	 * @return
	 * @throws Exception
	 */
	public boolean getInsertandoBOProducto( List listProductosXls, UserDTO usr ) throws Exception {
		
	    CargaSapMasivaCtrl param = new CargaSapMasivaCtrl();
	   
		try {
			
			return param.getInsertandoBOProducto( listProductosXls, usr );
			 
		} catch (Exception e) {
			
			logger.error( "Error getInsertandoBOProducto: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	
	/**
	 * 
	 * @param productosCargaSapMasiva
	 * @return
	 * @throws ServiceException
	 */
	public List getLogByProductId(List productosCargaSapMasiva)throws ServiceException {	
		CargaSapMasivaCtrl prods = new CargaSapMasivaCtrl();
		List mrcs = null;
		try{
			mrcs = prods.getLogByProductId(productosCargaSapMasiva); 
		}catch(Exception ex){
			logger.debug( "Problemas con getLogByProductId");
			throw new ServiceException(ex);
		}
	
		return mrcs;	
	}
	
	/**
	 * 
	 * @param idGrupo
	 * @return
	 * @throws Exception
	 */
	public boolean getExisteIdGrupo( String idGrupo ) throws Exception {
		
	    CargaSapMasivaCtrl param = new CargaSapMasivaCtrl();
	   
		try {
			
			return param.getExisteIdGrupo( idGrupo );
			 
		} catch (Exception e) {
			
			logger.error( "Error getExisteIdGrupo: ", e );
			throw new ServiceException(e);
		
		}
		
	}
	
}

