package cl.bbr.promo.lib.dao;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cl.bbr.promo.lib.dto.ClienteKccDTO;
import cl.bbr.promo.lib.dto.ClientePRDTO;
import cl.bbr.promo.lib.dto.ClienteSG6DTO;
import cl.bbr.promo.lib.dto.MedioPagoNormalizadoDTO;
import cl.bbr.promo.lib.dto.PrioridadPromosDTO;
import cl.bbr.promo.lib.dto.PromocionCriterio;
import cl.bbr.promo.lib.exception.PromocionesDAOException;

/**
 * Interfaz para implementación de métodos en DAO para diferentes tipos de conexión a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */

public interface PromocionesDAO {

	/**
	 * Recupera la lista de promociones para un producto.
	 * 
	 * @param	Criterio	Criterio con datos para la búsquda
	 * @return	Lista de DTO
	 * @throws PromocionesDAOException
	 */	
	public List getPromocionesByProductoId( PromocionCriterio criterio ) throws PromocionesDAOException;

	/**
	 * Recupera el identificador del backoffice para el producto
	 * 
	 * @param	Criterio	Criterio con datos para la búsquda
	 * @return	Identificador único BO
	 * @throws PromocionesDAOException
	 */
	public long getIdBoProductoId( PromocionCriterio criterio ) throws PromocionesDAOException;

	/**
	 * Recupera el listado de promociones para los TCP informados
	 * 
	 * @param tcp	Listado con TCP a buscar
	 * @return		Listado con promociones
	 * @throws PromocionesDAOException
	 */
	public List getPromocionesByTCP( List tcp ) throws PromocionesDAOException;

	/**
	 * Recupera el listado de promociones para los TCP informados
	 * 
	 * @param tcp	    Listado con TCP a buscar
	 * @param id_local	Identificador del Local
	 * @return		    Listado con promociones
	 * @throws PromocionesDAOException
	 */
	public List getPromocionesByTCP( List tcp, String id_local ) throws PromocionesDAOException;

	/**
	 * Recupera las promociones por producto
	 * 
	 * @param id_producto	Identificador del producto
	 * @param id_local		Identificador del local
	 * @return				DTO con datos de la promoción
	 * @throws PromocionesDAOException
	 */
	public PrioridadPromosDTO getPromosPrioridadProducto(long id_producto, long id_local) throws PromocionesDAOException;
	
	/**
	 * Recupera los medios de pago normalizados
	 * 
	 * @param mp	Medio de pago a buscar
	 * @return		Medio de pago con información
	 * @throws PromocionesDAOException
	 */
	public MedioPagoNormalizadoDTO getMedioPAgoNormalizado( MedioPagoNormalizadoDTO mp ) throws PromocionesDAOException;
	
	/**
	 * Recupera el id de local POS
	 * 
	 * @param id_local
	 * @return
	 * @throws PromocionesDAOException
	 */
	public int getCodigoLocalPos(long id_local) throws PromocionesDAOException;

    /**
     * @param listaIdBo
     * @param idLocal
     * @param lista_tcp
     * @return
     */
    public Hashtable getPromociones(String listaIdBo, int idLocal, List lista_tcp) throws PromocionesDAOException;
	
    /**
     * @param listaIdProd
     * @param idLocal
     * @param lista_tcp
     * @return
     */
    public Hashtable getPromocionesBanner(String listaIdProd, int idLocal, List lista_tcp) throws PromocionesDAOException;
    
	public String getProductoDescripcion(long cod_barra) throws PromocionesDAOException;
	
    /**
     * 
     * @param dataClienteKcc
     * @return
     * @throws PromocionesDAOException
     */
    public boolean addDataClienteKcc(ClienteKccDTO dataClienteKcc) throws PromocionesDAOException;
    
    public boolean getClientePRByRut(String rut, String dv) throws PromocionesDAOException;
    
    public boolean addDataClientePR(ClientePRDTO dataClientePR) throws PromocionesDAOException;
    /**
     * 
     * @param rut
     * @param dv
     * @return
     * @throws PromocionesDAOException
     */
    public boolean getClienteKccByRut(String rut, String dv) throws PromocionesDAOException;
    
    /**
     * 
     * @param cliente
     * @return
     * @throws PromocionesDAOException
     */
	public ClienteSG6DTO getClienteByRut(ClienteSG6DTO cliente) throws PromocionesDAOException;
	/**
	 * 
	 * @param llave
	 * @return
	 * @throws PromocionesDAOException
	 */
	public ArrayList getModelosSamsung(String llave) throws PromocionesDAOException;

	public int getReservasSamsungCliente(ClienteSG6DTO cliente) throws PromocionesDAOException;

	public boolean registrarReservaSamsung(ClienteSG6DTO cliente) throws PromocionesDAOException;

	public ClienteSG6DTO getDireccionCliente(ClienteSG6DTO cliente) throws PromocionesDAOException;
    
}