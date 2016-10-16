package cl.bbr.vte.empresas.dao;

import java.util.List;

import cl.bbr.jumbocl.common.model.CompradoresEntity;
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.CompradorCriteriaDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.UserDTO;
import cl.bbr.vte.empresas.exceptions.CompradoresDAOException;

/**
 * Interfaz para implementación de métodos en DAO para diferentes tipos de conexión a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public interface CompradoresDAO {

	/**
	 * Obtiene datos de comprador, segun el rut
	 * 
	 * @param  rut	RUT del comprador a buscar
	 * @return CompradoresEntity
	 * @throws CompradoresDAOException
	 */
	public CompradoresEntity getCompradoresByRut(long rut) throws CompradoresDAOException;
	
	/**
	 * Agrega un nuevo comprador
	 * 
	 * @param  dto
	 * @return id del nuevo comprador
	 * @throws CompradoresDAOException
	 */	
	public long setCreaComprador(CompradoresDTO dto) throws CompradoresDAOException;
	
	/**
	 * Modificar datos del comprador
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CompradoresDAOException
	 */
	public boolean setModComprador(CompradoresDTO dto) throws CompradoresDAOException;
	
	/**
	 * Se realiza eun update del campo cpr_estado = E
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CompradoresDAOException
	 */
	public boolean delComprador(CompradoresDTO dto) throws CompradoresDAOException;	
	
	/**
	 * Obtiene el listado de compradores relacionados a una sucursal , segun el id de sucursal.
	 * 
	 * @param  id_sucursal
	 * @return List CompradoresEntity
	 * @throws CompradoresDAOException
	 */
	public List getListCompradoresBySucursalId(long id_sucursal) throws CompradoresDAOException;

	/**
	 * Obtiene el listado de compradores relacionados a una empresa, segun el id de la empresa.
	 * 
	 * @param  id_empresa Identificador de la empresa
	 * @return List CompradoresEntity
	 * @throws CompradoresDAOException
	 */
	public List getListCompradoresByEmpresalId(long id_empresa) throws CompradoresDAOException;	
	
	/**
	 * Obtiene informacion del comprador, segun el id de comprador
	 * 
	 * @param  comprador_id
	 * @return CompradoresEntity 
	 * @throws CompradoresDAOException
	 */
	public CompradoresEntity getCompradoresById(long comprador_id) throws CompradoresDAOException;
	
	/**
	 * Obtiene el listado de compradores que coinciden con los criterios de busqueda ingresados
	 *  
	 * @param  dtoC
	 * @return List CompradoresEntity
	 * @throws CompradoresDAOException
	 */
	public List getCompradoresByCriteria(CompradorCriteriaDTO dtoC) throws CompradoresDAOException;
	
	/**
	 * Obtiene la cantidad de compradores que coinciden con los criterios de busqueda ingresados
	 * 
	 * @param  criterio
	 * @return int
	 * @throws CompradoresDAOException
	 */
	public int getCompradoresCountByCriteria(CompradorCriteriaDTO criterio) throws CompradoresDAOException ;
	
	/**
	 * Agregar la relacion entre el comprador y la sucursal
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CompradoresDAOException
	 */
	public boolean setRelCompradorSucursal(CompradoresDTO dto) throws CompradoresDAOException;

	/**
	 * Agregar la relacion entre el comprador y la empresa para su administración
	 * 
	 * @param  dto Datos del comprador
	 * @return boolean
	 * @throws CompradoresDAOException
	 */
	public boolean setRelCompradorEmpresa(CompradoresDTO dto) throws CompradoresDAOException;	
	
	/**
	 * Obtiene el listado de sucursales relacionadas a un comprador
	 * 
	 * @param  id_comprador
	 * @return List  ComprXSucDTO
	 * @throws CompradoresDAOException
	 */
	public List getListSucursalesByCompradorId(long id_comprador) throws CompradoresDAOException;

	/**
	 * Obtiene el listado de sucursales relacionadas a un comprador para las empresas que el administrador tiene acceso
	 * 
	 * @param  id_comprador, id_administrador
	 * @return List  ComprXSucDTO
	 * @throws CompradoresDAOException
	 */
	public List getListSucursalesByCompradorId(long id_comprador, long id_administrador) throws CompradoresDAOException;	
	
	/**
	 * Elimina la relación comprador empresa para administración  
	 * 
	 * @param dto
	 * @return
	 * @throws CompradoresDAOException
	 */
	public boolean delRelCompradorEmpresa(CompradoresDTO dto) throws CompradoresDAOException;
	
	/**
	 * Agrega la relacion entre sucursal y comprador
	 * 
	 * @param dto
	 * @return boolean
	 * @throws CompradoresDAOException
	 */
	public boolean addRelSucursalComprador(ComprXSucDTO dto) throws CompradoresDAOException;
	
	/**
	 * Elimina la relacion entre sucursal y comprador
	 * 
	 * @param dto
	 * @return boolean
	 * @throws CompradoresDAOException
	 */
	public boolean delRelSucursalComprador(ComprXSucDTO dto) throws CompradoresDAOException;
	
	
	/**
	 * Obtiene el listado de sucursales no asociados al comprador
	 * 
	 * @param id_comprador Identificador único del comprador
	 * @param id_administrador Identificador único del comprador administrador
	 * @return Listado con las sucursales (SucursalesDTO)
	 * @throws CompradoresDAOException
	 */
	public List getListSucursalesNoAsocComprador(long id_administrador, long id_comprador) throws CompradoresDAOException;
	
	/**
	 * Obtiene el tipo de comprador, segun id de comprador y id de sucursal
	 * 
	 * @param id_comprador
	 * @param id_sucursal
	 * @return tipo de comprador
	 * @throws CompradoresDAOException
	 */
	public String getComprXSuc(long id_comprador, long id_sucursal) throws CompradoresDAOException;
	
	/**
	 * Obtiene el listado de Empresas en la cual el comprador es administrador
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List  Lista con datos (EmpresasDTO)
	 * @throws CompradoresDAOException
	 */
	public List getListAdmEmpresasByCompradorId(long id_comprador) throws CompradoresDAOException;	

	/**
	 * Obtiene el listado de sucursales para las empresas a las cuales el comprador es administrador.
	 * 
	 * @param id_comprador	Identificador único del comprador
	 * @return Lista con datos (SucursalesDTO)
	 * @throws CompradoresDAOException
	 */
	public List getListAdmSucursalesByCompradorId(long id_comprador) throws CompradoresDAOException;
	
	/**
	 * Obtiene el listado de compradores para las sucursales que el administrador tiene acceso.
	 * 
	 * @param id_administrador
	 * @return
	 * @throws CompradoresDAOException
	 */
	public List getListAdmCompradoresByAdministradorId(long id_administrador) throws CompradoresDAOException;

	
	/**
	 * Obtiene el listado de Empresas en la cual el comprador puede comprar
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List  Lista con datos (EmpresasDTO)
	 * @throws CompradoresDAOException
	 */
	public List getListEmpresasByCompradorId(long id_comprador) throws CompradoresDAOException;
	
	/**
	 * Obtiene los datos del ejecutivo fono empresa
	 * @param  login
	 * @return UserDTO
	 * @throws CompradoresDAOException
	 */	
	public UserDTO getEjecutivoGetByRut(String login ) throws	CompradoresDAOException;
	
	/**
	 * Permite saber si un comprador es administrador de una sucursal
	 * @param id_comprador
	 * @param id_sucursal
	 * @return boolean
	 * @throws CompradoresDAOException
	 */
	public boolean esAdministrador(long id_comprador, long id_sucursal)throws	CompradoresDAOException;
	
	/**
	 * Modifica los datos password - estado de un comprador
	 * 
	 * @param  comprador CompradoresDTO con datos del comprador
	 * @throws VteException, SystemException
	 */
	public boolean compradorChangePass(CompradoresDTO comprador) throws CompradoresDAOException;	
	
	
	/**
	 * Modifica la cantidad de intentos de logeo de un comprador
	 * 
	 * @param comprador_id 	Identificador único del comprador
	 * @param accion		Acción a gatillar 1: Aumentar, 0: Reset
	 * @throws VteException 
	 */	
	public boolean updateIntentos(long comprador_id, long accion) throws	CompradoresDAOException;
	
}
