package cl.bbr.cupondscto.service;

import java.util.List;

import cl.bbr.cupondscto.ctrl.CuponDsctoCtrl;
import cl.bbr.cupondscto.dto.CarroAbandonadoDTO;
import cl.bbr.cupondscto.exception.CuponDsctoException;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;

public class CuponDsctoService {
 
	Logging logger = new Logging( this );

    /**
     * Busca cupon de descuento en base al codigo
     * @param codigo
     * @return CuponDsctoDTO
     * @throws ServiceException
     */
    public CuponDsctoDTO getCuponDscto(String codigo) throws ServiceException, SystemException{
    	CuponDsctoCtrl cdCtrl = new CuponDsctoCtrl();
        try {
            return cdCtrl.getCuponDscto(codigo);
        } catch (CuponDsctoException ex) {
        	ex.printStackTrace();
            logger.error( "Error (Service) getCuponDscto", ex);
            throw new ServiceException(ex);
        }
    }
    
    public CuponDsctoDTO getCuponDsctoById(int idCupon) throws ServiceException, SystemException{
    	CuponDsctoCtrl cdCtrl = new CuponDsctoCtrl();
        try {
            return cdCtrl.getCuponDsctoById(idCupon);
        } catch (CuponDsctoException ex) {
        	ex.printStackTrace();
            logger.error( "Error (Service) getCuponDscto", ex);
            throw new ServiceException(ex);
        }
    }

    
    /**
     * Verifica que cupon es para el cliente
     * @param rut
     * @param id_cupon
     * @return
     * @throws ServiceException
     */
    public boolean isCuponForRut(long rut, long id_cupon) throws ServiceException {
    	CuponDsctoCtrl cdCtrl = new CuponDsctoCtrl();
        try {
            return cdCtrl.isCuponForRut(rut, id_cupon);
        } catch (Exception ex) {
            logger.error( "Error (Service) isCuponForRut:", ex);
            throw new ServiceException(ex);
        }
    }
    
    /**
     * 
     * @param id_cupon
     * @param tipo
     * @return
     * @throws ServiceException
     */
    public List getProdsCupon (long id_cupon, String tipo) throws ServiceException {
    	CuponDsctoCtrl cdCtrl = new CuponDsctoCtrl();
        try {
            return cdCtrl.getProdsCupon(id_cupon, tipo);
        } catch (Exception ex) {
            logger.error( "Error (Service) getProdsCupon:", ex);
            throw new ServiceException(ex);
        }
    }
    
    
    /**
     * 
     * @param idCupon
     * @return
     * @throws ServiceException
     */
    public boolean dsctaStockCupon (long idCupon) throws ServiceException {
    	CuponDsctoCtrl cdCtrl = new CuponDsctoCtrl();
        try {
            return cdCtrl.dsctaStockCupon(idCupon);
        } catch (Exception ex) {
            logger.error( "Error (Service) dsctaStockCupon:", ex);
            throw new ServiceException(ex);
        }
    }
    
    /**
     * 
     * @param idCupon
     * @param idPedido
     * @return
     * @throws ServiceException
     */
    public boolean setIdCuponIdPedido ( long idCupon, long idPedido ) throws ServiceException {
    	
    	CuponDsctoCtrl cdCtrl = new CuponDsctoCtrl();
        
    	try {
        
    		return cdCtrl.setIdCuponIdPedido( idCupon, idPedido );
        
    	} catch ( Exception ex ) {
         
    		logger.error( "Error (Service) setIdCuponIdPedido:", ex );
            throw new ServiceException( ex );
        
    	}
    
    }
    
    
    /**
     * Busca email de cupon de descuento en base al id de email
     * @param codigo
     * @return CuponDsctoDTO
     * @throws ServiceException
     */
    public CarroAbandonadoDTO getCuponCarroAbandonado(int codigo) throws ServiceException {
    	CuponDsctoCtrl cdCtrl = new CuponDsctoCtrl();
        try {
            return cdCtrl.getCuponCarroAbandonado(codigo);
        } catch (Exception ex) {
            logger.error( "Error (Service) getCuponCarroAbandonado", ex);
            throw new ServiceException(ex);
        }
    }

  
    
}
