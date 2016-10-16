package cl.bbr.jumbocl.bolsas.service;

import java.util.List;

import cl.bbr.jumbocl.bolsas.ctrl.BolsasCtrl;
import cl.bbr.jumbocl.bolsas.dto.BolsaDTO;
import cl.bbr.jumbocl.bolsas.exceptions.BolsasException;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Permite manejar información y operaciones sobre Casos. 
 * 
 * @author imoyano
 *
 */
public class BolsasService {

    
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	
	public List getStockBolsasRegalo(String cod_sucursal) throws ServiceException {
		BolsasCtrl bolsas = new BolsasCtrl();
		List result = null;
		try {
			result = bolsas.getStockBolsasRegalo(cod_sucursal);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return result;
    }
	
	
    public List getBitacoraBolsasRegalo(String cod_sucursal) throws ServiceException {
		BolsasCtrl bolsas = new BolsasCtrl();
		List result = null;
		try {
			result = bolsas.getBitacoraBolsasRegalo(cod_sucursal);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return result;
    }


    /**
     * @param m
     */
    public void actualizarStockBolsa(String cod_bolsa, String cod_sucursal, int stock) throws ServiceException {		
		BolsasCtrl bolsas = new BolsasCtrl();
		try {
			bolsas.actualizarStockBolsa(cod_bolsa, cod_sucursal, stock);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }
    
    
    
    public void insertarRegistroBitacoraBolsas(String desc_operacion, String usuario, String cod_sucursal) throws ServiceException {		
		BolsasCtrl bolsas = new BolsasCtrl();
		try {
			bolsas.insertarRegistroBitacoraBolsas(desc_operacion, usuario, cod_sucursal);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }
    
    public void asignacionBolsaCliente(String rut_cliente, String cod_bolsa) throws ServiceException {		
		BolsasCtrl bolsas = new BolsasCtrl();
		try {
			bolsas.asignacionBolsaCliente(rut_cliente, cod_bolsa);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }
    
    public void crearBolsaRegalo(BolsaDTO bolsa) throws ServiceException {		
		BolsasCtrl bolsas = new BolsasCtrl();
		try {
			bolsas.crearBolsaRegalo(bolsa);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }
    
    
    public void activaBolsa() throws ServiceException {		
		BolsasCtrl bolsas = new BolsasCtrl();
		try {
			bolsas.activaBolsa();
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }
    
    public void eliminarBolsaRegalo(BolsaDTO bolsa) throws ServiceException {		
		BolsasCtrl bolsas = new BolsasCtrl();
		try {
			bolsas.eliminarBolsaRegalo(bolsa);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }
    
    public void actualizarBolsa(BolsaDTO bolsa) throws ServiceException {		
		BolsasCtrl bolsas = new BolsasCtrl();
		try {
			bolsas.actualizarBolsa(bolsa);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }
    
    public BolsaDTO getBolsaRegalo(String cod_bolsa) throws ServiceException {		
		BolsasCtrl bolsas = new BolsasCtrl();
		try {
			return bolsas.getBolsaRegalo(cod_bolsa);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }
	
    public List getAsignacionesBolsas() throws ServiceException {
		BolsasCtrl bolsas = new BolsasCtrl();
		List result = null;
		try {
			result = bolsas.getAsignacionesBolsas();
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return result;
    }
    
    public boolean validaCodSap(String cod_sap) throws ServiceException {
		BolsasCtrl bolsas = new BolsasCtrl();
		boolean result = false;
		try {
			result = bolsas.validaCodSap(cod_sap);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return result;
    }
    
    public boolean validaCodBolsa(String cod_bolsa) throws ServiceException {
		BolsasCtrl bolsas = new BolsasCtrl();
		boolean result = false;
		try {
			result = bolsas.validaCodBolsa(cod_bolsa);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return result;
    }

    public boolean validaCodBolsaSap(String cod_bolsa, String cod_sap) throws ServiceException {
		BolsasCtrl bolsas = new BolsasCtrl();
		boolean result = false;
		try {
			result = bolsas.validaCodBolsaSap(cod_bolsa, cod_sap);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return result;
    }    
    
    public long getIdProdBO(String cod_sap) throws ServiceException {
		BolsasCtrl bolsas = new BolsasCtrl();
		long result = 0;
		try {
			result = bolsas.getIdProdBO(cod_sap);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return result;
    }
    
    public long getIdProdFO(String cod_sap) throws ServiceException {
		BolsasCtrl bolsas = new BolsasCtrl();
		long result = 0;
		try {
			result = bolsas.getIdProdFO(cod_sap);
		} catch (BolsasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return result;
    }
    
    
    public ProductoEntity getProductoByIdProd(long idProd) throws ServiceException {
		BolsasCtrl bolsas = new BolsasCtrl();
		ProductoEntity result = new ProductoEntity();
		try {
			result = bolsas.getProductoByIdProd(idProd);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return result;
    }
	
}
