package cl.bbr.vte.empresas.dao;

import java.util.List;

import cl.bbr.jumbocl.common.model.SucursalesEntity;
import cl.bbr.vte.empresas.dto.SucursalCriteriaDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.empresas.exceptions.SucursalesDAOException;

/**
 * Interfaz para implementación de métodos en DAO para diferentes tipos de conexión a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public interface SucursalesDAO {

	/**
	 * Obtiene datos de la sucursal
	 * 
	 * @param  id
	 * @return SucursalesEntity
	 * @throws SucursalesDAOException
	 */	
	public SucursalesEntity getSucursalById(long id) throws SucursalesDAOException;
	
	
	/**
	 * Agrega un nueva sucursal
	 * 
	 * @param  dto
	 * @return long
	 * @throws SucursalesDAOException
	 */	
	public long insSucursal(SucursalesDTO dto) throws SucursalesDAOException;
	
	/**
	 * Modificar datos de la sucursal
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws SucursalesDAOException
	 */	
	public boolean modSucursal(SucursalesDTO dto) throws SucursalesDAOException;

	/**
	 * Solo realiza un update al campo cli_estado = E
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws SucursalesDAOException
	 */	
	public boolean delSucursal(SucursalesDTO dto) throws SucursalesDAOException;
	
	/**
	 * Obtiene el listado de compradores segun id de sucursal
	 * 
	 * @param id
	 * @return List CompradoresEntity
	 * @throws SucursalesDAOException
	 */
	public List getCompradoresBySucursalId(long id) throws SucursalesDAOException;
	
	/**
	 * Obtiene las direcciones de despacho de una sucursal, segun id de sucursal
	 * 
	 * @param  id
	 * @return List
	 * @throws SucursalesDAOException
	 */
	//public List getDirsDespachoBySucursalId(long id)  throws SucursalesDAOException;
	
	/**
	 * Obtiene el listado de sucursales que coinciden con los criterios de búsqueda ingresados
	 * 
	 * @param criterio
	 * @return List SucursalesEntity
	 * @throws SucursalesDAOException
	 */
	public List getSucursalesByCriteria(SucursalCriteriaDTO criterio) throws SucursalesDAOException;
	
	/**
	 * Obtiene la cantidad total de sucursales que coinciden con los criterios de busqueda ingresados
	 * 
	 * @param  criterio
	 * @return int
	 * @throws SucursalesDAOException
	 */
	public int getSucursalesCountByCriteria(SucursalCriteriaDTO criterio) throws SucursalesDAOException;
	
	/**
	 * Obtiene el listado de sucursales de una empresa, segun id de empresa
	 * 
	 * @param id
	 * @return List SucursalesEntity
	 * @throws SucursalesDAOException
	 */
	public List getSucursalesByEmpresaId(long id) throws SucursalesDAOException;
	
	/**
	 * Obtiene el listado de direcciones de facturacion de una sucursal, segun id de sucursal
	 * 
	 * @param  id
	 * @return List DirFacturacionEntity
	 * @throws SucursalesDAOException
	 */
	public List getDirsFacturacionBySucursalId(long id) throws SucursalesDAOException;
	
	
	/**
	 * Obtiene los datos de la sucursal, segun el rut de la sucursal
	 * 
	 * @param  rut
	 * @return SucursalesEntity
	 * @throws SucursalesDAOException
	 */
	public SucursalesEntity getSucursalByRut(long rut) throws SucursalesDAOException;
}
