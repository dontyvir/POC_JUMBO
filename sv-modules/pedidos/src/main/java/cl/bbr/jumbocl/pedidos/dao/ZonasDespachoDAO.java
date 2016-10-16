package cl.bbr.jumbocl.pedidos.dao;

import java.util.List;

import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.LocalDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.ZonasDespachoDAOException;

/**
 * Permite las operaciones en base de datos sobre las Zonas de Despacho.
 * 
 * @author BBR
 *
 */
public interface ZonasDespachoDAO {

	/**
	 * Retorna un listado con las Zonas de Despacho de un Local
	 *  
	 * @param  id_local
	 * @return List ZonaDTO
	 * @throws ZonasDespachoDAOException
	 */
	public List getZonasDespachoLocal(long id_local)
		throws ZonasDespachoDAOException;

	/**
	 * Obtiene el detalle de una zona de despacho
	 * 
	 * @param  id_zona
	 * @return ZonaDTO
	 * @throws ZonasDespachoDAOException
	 */
	public ZonaDTO getZonaDespachoById(long id_zona)
		throws ZonasDespachoDAOException;
	
	/**
	 * Inserta registro en tabla BO_ZONAS
	 * 
	 * @param  zona
	 * @return long
	 * @throws ZonasDespachoDAOException
	 */
	public long doAgregaZonaDespacho(ZonaDTO zona)
		throws ZonasDespachoDAOException;

	/**
	 * Actualiza registro en tabla BO_ZONAS
	 * 
	 * @param  zona
	 * @throws ZonasDespachoDAOException
	 */
	public void doModZonaDespacho(ZonaDTO zona)
		throws ZonasDespachoDAOException;
	
	/**
	 * Obtiene listado de comunas de una zona de despacho
	 * 
	 * @param  id_zona
	 * @return List ComunaDTO
	 * @throws LocalDAOException
	 */
	public List getComunasByIdZonaDespacho(long id_zona)
		throws LocalDAOException;
	
	/**
	 * Obtiene listado de todas las comunas
	 * 
	 * @return List ComunaDTO
	 * @throws LocalDAOException
	 */
	public List getComunasAll()
		throws LocalDAOException;
	
	/**
	 * Borra asociación de comuna a zona de despacho
	 * 
	 * @param  id_zona
	 * @param  id_comuna
	 * @throws ZonasDespachoDAOException
	 */
	/*public void doBorraComunaZonaDespacho(long id_zona, long id_comuna)
		throws ZonasDespachoDAOException;*/
	
	/**
	 * Agrega comuna a zona de despacho (relación)
	 * 
	 * @param  id_zona
	 * @param  id_comuna
	 * @param  orden
	 * @throws ZonasDespachoDAOException
	 */
	/*public void doAgregaComunaZonaDespacho(long id_zona, long id_comuna, int orden)
		throws ZonasDespachoDAOException;*/
	
	/**
	 * Busca las zonas de una ronda
	 * @param id_ronda
	 * @throws ZonasDespachoDAOException
	 */
	public List doBuscaZonaByRonda(long id_ronda) throws ZonasDespachoDAOException;
	
}
