package cl.bbr.vte.empresas.dao;

import java.util.List;

import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.ComunaEntity;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.LocalEntity;
import cl.bbr.jumbocl.common.model.RegionEntity;
import cl.bbr.jumbocl.common.model.TipoCalleEntity;
import cl.bbr.jumbocl.common.model.ZonaEntity;
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.empresas.exceptions.DireccionesDAOException;
import cl.bbr.vte.empresas.exceptions.EmpresasDAOException;

/**
 * Interfaz para implementación de métodos en DAO para diferentes tipos de conexión a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public interface DireccionesDAO {

	/**
	 * Obtiene el listado de tipos de calle
	 * 
	 * @return List TipoCalleEntity
	 * @throws DireccionesDAOException
	 */
	public List getTiposCalleAll() throws DireccionesDAOException;
	
	/**
	 * Obtiene el listado de comunas
	 * 
	 * @return List ComunaEntity
	 * @throws DireccionesDAOException
	 */
	public List getComunas() throws DireccionesDAOException;

	/**
	 * Retorna la zona y el local para la comuna..
	 * 
	 * @param comuna	Identificador único de la comuna
	 * @return			Identificador único de la zona y el identificador único del local en formato id_zona-id_local
	 * @throws EmpresasDAOException
	 */	
	public String getLocalComuna( long comuna )	throws DireccionesDAOException;

	/**
	 * Agrega una dirección de despacho a la sucursal
	 * 
	 * @param  direccion  DTO con datos de la dirección
	 * @return long, id de nueva direccion
	 * @throws EmpresasDAOException
	 */
	public long insDirDespacho(DireccionesDTO direccion) throws	DireccionesDAOException;


	/**
	 * Modifica dirección de despacho de una sucursal
	 * 
	 * @param  direccion		DTO con datos de la dirección a modificar
	 * @return True: éxito False: error
	 * @throws SystemException
	 * @see DireccionesDTO
	 */	
	public boolean modDirDespacho(DireccionesDTO direccion)	throws	DireccionesDAOException;
	
	/**
	 * Se realiza un update del campo dir_estado = E
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CompradoresDAOException
	 */
	public boolean delDirDespacho(DireccionesDTO dto) throws	DireccionesDAOException;

	/**
	 * Obtiene los datos de la direccion de facturacion
	 * 
	 * @param direccion_id	Identificador único para la dirección
	 * @return DireccionEntity	Entidad para las direcciones
	 * @throws DireccionesDAOException
	 */	
	public DireccionEntity getDireccionesDespByIdDesp(long direccion_id) throws DireccionesDAOException;
	
	/**
	 * Obtiene el listado de regiones
	 * 
	 * @return List RegionEntity
	 * @throws DireccionesDAOException
	 */	
	public List getRegiones() throws	DireccionesDAOException;
	
	/**
	 * Recupera la lista de Comunas por region  y la retorna como una lista de DTO (ComunaDTO).
	 * 
	 * @param reg_id	identificador único de la region
	 * @return	Lista de DTO (ComunaDTO)
	 * @throws DireccionesDAOException
	 */	
	public List getAllComunas( long reg_id ) throws	DireccionesDAOException;
	
	/**
	 * Obtiene los datos de tipo de calle, segun el id del tipo de calle
	 * 
	 * @param id_tipo_calle
	 * @return informacion del tipo de calle en un entity
	 * @throws DireccionesDAOException
	 */
	public TipoCalleEntity getTiposCalleById(long id_tipo_calle) throws DireccionesDAOException;
	
	/**
	 * Obtiene los datos de una comuna, segun el id de comuna
	 * 
	 * @param id_comuna
	 * @return informacion de la comuna en un entity
	 * @throws DireccionesDAOException
	 */
	public ComunaEntity getComunaById(long id_comuna) throws DireccionesDAOException;
	
	/**
	 * Obtiene los datos de una region, segun el id de region
	 * 
	 * @param id_region
	 * @return informacion de la region en un entity
	 * @throws DireccionesDAOException
	 */
	public RegionEntity getRegionById(long id_region) throws DireccionesDAOException;
	
	/**
	 * Obtiene los datos de un local, segun el id de local
	 * 
	 * @param id_local
	 * @return informacion del local en un entity
	 * @throws DireccionesDAOException
	 */
	public LocalEntity getLocalById(long id_local) throws DireccionesDAOException;
	
	/**
	 * Obtiene el listado de direcciones de despacho de una sucursal
	 * 
	 * @param id_sucursal
	 * @return List DirFacturacionEntity
	 * @throws DireccionesDAOException
	 */
	public List getListDireccionDespBySucursal(long id_sucursal) throws DireccionesDAOException;
	
	/**
	 * Obtiene los datos de la zona. segun el id de zona
	 * 
	 * @param id_zona
	 * @return informacion de la zona en un entity
	 * @throws DireccionesDAOException
	 */
	public ZonaEntity getZonaById(long id_zona) throws DireccionesDAOException;
	
	/**
	 * Listado de direcciones de despacho por comprador
	 * 
	 * @param comprador_id
	 * @return List DireccionEntity
	 * @throws DireccionesDAOException
	 */
	public List getListDireccionDespByComprador(long comprador_id) throws DireccionesDAOException;
	
	
	/**
	 * Recupera la lista general de comunas
	 * 
	 * @return	Lista de DTO (ComunaDTO)
	 * @throws DireccionesDAOException
	 */	
	public List getComunasGeneral(  ) throws	DireccionesDAOException;
	
	
	/**
	 * Recupera el ID del Local asociado al poligono
	 * 
	 * @return	int id_local
	 * @throws DireccionesDAOException
	 */	
	public int getLocalByPoligono(int id_poligono) throws DireccionesDAOException;
}
