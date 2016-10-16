package cl.bbr.promo.lib.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.promo.lib.ctrl.PromocionesCTR;
import cl.bbr.promo.lib.dao.DAOFactory;
import cl.bbr.promo.lib.dao.PromocionesDAO;
import cl.bbr.promo.lib.dto.ClienteKccDTO;
import cl.bbr.promo.lib.dto.ClientePRDTO;
import cl.bbr.promo.lib.dto.ClienteSG6DTO;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;
import cl.bbr.promo.lib.exception.PromocionesDAOException;
import cl.bbr.promo.lib.exception.PromocionesException;

/**
 * Capa de servicios para el área de promociones
 * 
 * @author BBR e-commerce & retail
 *
 */
public class PromocionesService {

	/**
	 * Instancia para log
	 */
	Logging logger = new Logging( this );
	
	/**
	 * Constructor
	 *
	 */
	public PromocionesService() {
		this.logger.debug("New PromocionesService");
	}

	/**
	 * Direcciones de despacho para un cliente.
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @param lista_tcp		Listado de TCP para el cliente
	 * @throws SystemException 
	 * 
	 */
	//ESTE METODO CAMBIO EL ProductoDTO POR UN PRODUCTOPROMODTO
	public List getPromocionesByProductoId(long idProductoFO, long idProductoBO, String id_local, List lista_tcp) throws PromocionesException {

		PromocionesCTR dirctr = new PromocionesCTR(2);
		List list = null;

		try {
			list = dirctr.getPromocionesByProductoId(idProductoFO, idProductoBO, id_local, lista_tcp);
		} catch (PromocionesException ex) {
			logger.error( "Problemas con controles de promociones", ex);
			throw new PromocionesException(ex);
		}

		return list;

	}
	
	/**
	 * Entrega el total considerando las promociones según parámetros.
	 * 
	 * @param total		Total sin promociones
	 * @return			Total con promociones
	 * @throws SystemException
	 */
	public long getTotalPromocion( long total ) throws PromocionesException {
		try {

			return Math.round(total*0.9);
			
		} catch (Exception ex) {
			logger.error( "Problemas con controles de promociones", ex);
			throw new PromocionesException(ex);
		}
	}
	
	/**
	 * Recuperar promociones por TCP
	 * 
	 * @param tcp	Listado de TCP
	 * @return		Listado de promociones
	 * @throws PromocionesException
	 */
	public List getPromocionesByTCP(List tcp) throws PromocionesException {
		try {

			PromocionesCTR dirctr = new PromocionesCTR(2);
			return dirctr.getPromocionesByTCP( tcp );
			
		} catch (Exception ex) {
			logger.error( "Problemas con controles de promociones", ex);
			throw new PromocionesException(ex);
		}
	}

	/**
	 * Recuperar promociones por TCP
	 * 
	 * @param tcp	    Listado de TCP
	 * @param id_local	Identificador del Local
	 * @return	    	Listado de promociones
	 * @throws PromocionesException
	 */
	public List getPromocionesByTCP(List tcp, String id_local) throws PromocionesException {
		try {

			PromocionesCTR dirctr = new PromocionesCTR(2);
			return dirctr.getPromocionesByTCP(tcp, id_local );
			
		} catch (Exception ex) {
			logger.error( "Problemas con controles de promociones", ex);
			throw new PromocionesException(ex);
		}
	}
	
	/**
	 * Recalcula las promociones para un pedido en un local.
	 * Se agregan parametros para aplicar cupon de descuento (cddto, cuponProds) cdd
	 * @param recalculodto
	 * @param cddto
	 * @param cuponProds
	 * @return
	 * @throws PromocionesException
	 */
	public doRecalculoResultado doRecalculoPromocion( doRecalculoCriterio recalculodto, CuponDsctoDTO cddto, List cuponProds) throws PromocionesException {
		try {

			PromocionesCTR dirctr = new PromocionesCTR(2);
			return dirctr.doRecalculoPromocion( recalculodto, cddto, cuponProds);
			
		} catch (Exception ex) {
			logger.error( "Problemas con controles de promociones", ex);
			throw new PromocionesException(ex);
		}
	}

	/**
	 * Recupera el id de local POS
	 * 
	 * @param id_local
	 * @return
	 */
	public int getCodigoLocalPos(long id_local) throws PromocionesException {
		try {

			PromocionesCTR dirctr = new PromocionesCTR(2);
			return dirctr.getCodigoLocalPos(id_local);
			
		} catch (Exception ex) {
			logger.error( "Problemas con controles de promociones", ex);
			throw new PromocionesException(ex);
		}
	}
	
	/**
	 * Registrar datos cliente nuevo Kcc
	 * 
	 * @param dataClienteKcc
	 * @return
	 * @throws PromocionesException
	 */
    public boolean addDataClienteKcc(ClienteKccDTO dataClienteKcc) throws PromocionesException {

    	PromocionesCTR dirctr = new PromocionesCTR(2);

        try {
            return dirctr.addDataClienteKcc(dataClienteKcc);
        } catch (Exception ex) {
            logger.error( "Problemas con controles de promociones", ex);
            throw new PromocionesException(ex);
        }       
    }   
    
    public boolean addDataClientePR(ClientePRDTO dataClientePR) throws PromocionesException {

    	PromocionesCTR dirctr = new PromocionesCTR(2);

        try {
            return dirctr.addDataClientePR(dataClientePR);
        } catch (Exception ex) {
            logger.error( "Problemas con controles de promociones", ex);
            throw new PromocionesException(ex);
        }       
    }  
    
    public boolean getClientePRByRut(String rut, String dv) throws PromocionesException {

    	PromocionesCTR dirctr = new PromocionesCTR(2);

        try {
            return dirctr.getClientePRByRut(rut, dv);
        } catch (Exception ex) {
            logger.error( "Problemas con controles de promociones", ex);
            throw new PromocionesException(ex);
        }       
    } 
    
    /**
     * Valida Cliente kcc existente
     * 
     * @param rut
     * @param dv
     * @return
     * @throws PromocionesException
     */
    public boolean getClienteKccByRut(String rut, String dv) throws PromocionesException {

    	PromocionesCTR dirctr = new PromocionesCTR(2);

        try {
            return dirctr.getClienteKccByRut(rut, dv);
        } catch (Exception ex) {
            logger.error( "Problemas con controles de promociones", ex);
            throw new PromocionesException(ex);
        }       
    } 

    /**
     * 
     * @param cliente
     * @return
     * @throws PromocionesException
     */
	public ClienteSG6DTO getClienteByRut(ClienteSG6DTO cliente) throws PromocionesException {
		PromocionesCTR dirctr = new PromocionesCTR(2);

        try {
            return dirctr.getClienteByRut(cliente);
        } catch (Exception ex) {
            logger.error( "Problemas con controles de promociones", ex);
            throw new PromocionesException(ex);
        }       
	}
	
	/**
	 * 
	 * @param llave
	 * @return
	 * @throws PromocionesException
	 */
	public ArrayList getModelosSamsung(String llave) throws PromocionesException {
		PromocionesCTR dirctr = new PromocionesCTR(2);

        try {
            return dirctr.getModelosSamsung(llave);
        } catch (Exception ex) {
            logger.error( "Problemas con controles de promociones", ex);
            throw new PromocionesException(ex);
        }       
	}
	/**
	 * 
	 * @param cliente
	 * @return
	 * @throws PromocionesException
	 */
	public int getReservasSamsungCliente(ClienteSG6DTO cliente) throws PromocionesException {
		PromocionesCTR dirctr = new PromocionesCTR(2);

        try {
            return dirctr.getReservasSamsungCliente(cliente);
        } catch (Exception ex) {
            logger.error( "Problemas con controles de promociones", ex);
            throw new PromocionesException(ex);
        }       
	}
	/**
	 * 
	 * @param cliente
	 * @return
	 * @throws PromocionesException
	 */
	public boolean registrarReservaSamsung(ClienteSG6DTO cliente) throws PromocionesException {
		PromocionesCTR dirctr = new PromocionesCTR(2);

        try {
            return dirctr.registrarReservaSamsung(cliente);
        } catch (Exception ex) {
            logger.error( "Problemas con controles de promociones", ex);
            throw new PromocionesException(ex);
        }       
	}
	/**
	 * 
	 * @param id
	 * @return
	 * @throws PromocionesException
	 */
	public ClienteSG6DTO getDireccionCliente(ClienteSG6DTO cliente) throws PromocionesException {
		PromocionesCTR dirctr = new PromocionesCTR(2);

        try {
            return dirctr.getDireccionCliente(cliente);
        } catch (Exception ex) {
            logger.error( "Problemas con controles de promociones", ex);
            throw new PromocionesException(ex);
        }       
	} 
	
	
	public Hashtable getPromociones(String listaIdBo, int idLocal, List lista_tcp) throws PromocionesException {
		
		PromocionesCTR dirctr = new PromocionesCTR(2);

        try {
            return dirctr.getPromociones(listaIdBo, idLocal, lista_tcp);
        } catch (Exception ex) {
            logger.error( "Problemas con controles de promociones", ex);
            throw new PromocionesException(ex);
        }  
	}
	
    /**
     * @param listaProductos
     * @param string
     * @param lista_tcp
     * @return
     * @throws PromocionesException
     */
    public Hashtable getPromocionesBanner(String listaIdProd, int idLocal, List lista_tcp) throws PromocionesException {
    	
		PromocionesCTR dirctr = new PromocionesCTR(2);

        try {
            return dirctr.getPromocionesBanner(listaIdProd, idLocal, lista_tcp);
        } catch (Exception ex) {
            logger.error( "Problemas con controles de promociones", ex);
            throw new PromocionesException(ex);
        } 
	}

}
