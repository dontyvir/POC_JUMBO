package cl.bbr.vte.empresas.dao;

import java.util.List;

import cl.bbr.jumbocl.common.model.DirFacturacionEntity;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.exceptions.DirFacturacionDAOException;
import cl.bbr.vte.empresas.exceptions.DireccionesDAOException;

/**
 * Interfaz para implementación de métodos en DAO para diferentes tipos de conexión a repositorios.
 * 
 * @author BBR
 *
 */
public interface DirFacturacionDAO {
	
	/**
	 * Obtiene los datos de la direccion de facturación por id de dirección de facturacion

	 * @param id_dir_fact
	 * @return DirFacturacionEntity
	 * @throws DirFacturacionDAOException
	 */
	public DirFacturacionEntity getDireccionesFactByIdFact(long id_dir_fact) throws DirFacturacionDAOException;
	
	/**
	 * Obtiene el listado de direcciones de facturacion relacionadas a una sucursal, segun id de sucursal
	 * 
	 * @param  id_sucursal
	 * @return List DirFacturacionEntity
	 * @throws DirFacturacionDAOException
	 */
	public List getListDireccionFactBySucursal(long id_sucursal) throws DirFacturacionDAOException;

	/**
	 * Agrega una nueva direccion de facturacion, con la informacion ingresada en el DTO
	 * 
	 * @param  dto
	 * @return long
	 * @throws DirFacturacionDAOException
	 */
	public long insDirFacturacion(DirFacturacionDTO dto) throws DirFacturacionDAOException;
	
	/**
	 * Modifica los datos de la direccion de facturacion, segun la información contenida en el DTO
	 * 
	 * @param  dto
	 * @return boolean 
	 * @throws DirFacturacionDAOException
	 */
	public boolean modDirFacturacion(DirFacturacionDTO dto) throws DirFacturacionDAOException;
	
	/**
	 * Elimina una direccionde facturacion, cambiando el estado a 'E' Eliminado 
	 * 
	 * @param id_dir_fact
	 * @return boolean
	 * @throws DirFacturacionDAOException
	 */
	public boolean delDirFacturacion(long id_dir_fact) throws DirFacturacionDAOException;
	
	/**
	 * Listado de direcciones de facturación por comprador
	 * 
	 * @param comprador_id
	 * @return List DireccionEntity
	 * @throws DireccionesDAOException
	 */
	public List getListDireccionFactByComprador(long comprador_id) throws DireccionesDAOException;
	
}
