package cl.bbr.vte.empresas.dao;

import java.util.List;

import cl.bbr.jumbocl.common.model.EmpresasEntity;
import cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.MailDTO;
import cl.bbr.vte.empresas.dto.SolRegDTO;
import cl.bbr.vte.empresas.exceptions.EmpresasDAOException;

/**
 * Interfaz para implementación de métodos en DAO para diferentes tipos de conexión a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public interface EmpresasDAO {

	/**
	 * Obtiene datos de la empresa
	 * 
	 * @param  id
	 * @return EmpresasEntity
	 * @throws EmpresasDAOException
	 */	
	public EmpresasEntity getEmpresaById(long id) throws EmpresasDAOException;
	
	/**
	 * Obtiene el listado de empresas segun los criterios de busqueda seleccionados
	 * 
	 * @param criterio
	 * @return List EmpresasEntity
	 * @throws EmpresasDAOException
	 */
	public List getEmpresasByCriteria(EmpresaCriteriaDTO criterio) throws EmpresasDAOException;
	
	/**
	 * Obtiene el listado de todas las empresas
	 * 
	 * @return List EmpresasEntity
	 * @throws EmpresasDAOException
	 */
	public List getEmpresasByCriteria() throws EmpresasDAOException;
	
	/**
	 * Obtiene la cantidad de empresas, segun los criterios de búsqueda
	 * 
	 * @param  criterio
	 * @return int, cantidad
	 * @throws EmpresasDAOException
	 */
	public int getEmpresasCountByCriteria(EmpresaCriteriaDTO criterio) throws EmpresasDAOException;
	
	/**
	 * Agrega un nueva empresa
	 * 
	 * @param  dto
	 * @return long
	 * @throws EmpresasDAOException
	 */	
	public long setCreaEmpresa(EmpresasDTO dto) throws EmpresasDAOException;
	
	/**
	 * Modificar datos de la empresa
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws EmpresasDAOException
	 */	
	public boolean modEmpresa(EmpresasDTO dto) throws EmpresasDAOException;

	/**
	 * Solo realiza un update al campo emp_estado = E
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws EmpresasDAOException
	 */	
	public boolean delEmpresa(EmpresasDTO dto) throws EmpresasDAOException;
	
	/**
	 * Agrega registro de mail para ser enviado al ejecutivo
	 * 
	 * @param  mail
	 * @return boolean
	 * @throws EmpresasDAOException
	 */
	public boolean insMail(MailDTO mail) throws EmpresasDAOException;
	
	/**
	 * Obtiene el listado de sucursales de una empresa, segun id de empresa
	 * 
	 * @param id
	 * @return List SucursalesEntity
	 * @throws EmpresasDAOException
	 */
	public List getSucursalesByEmpresaId(long id) throws EmpresasDAOException;
	
	/**
	 * Obtiene listado de estados, segun el tipo
	 * 
	 * @param tipo
	 * @return List EstadoDTO
	 * @throws EmpresasDAOException
	 */
	public List getEstadosByTipo(String tipo) throws EmpresasDAOException;
	

	/**
	 * Obtiene informacion de la empresa, segun rut de la empresa
	 * 
	 * @param  rut
	 * @return EmpresasEntity
	 * @throws EmpresasDAOException
	 */
	public EmpresasEntity getEmpresaByRut(long rut) throws EmpresasDAOException;
	
	
	/**
	 * Obtiene identificador de la zona y del local, segun la comuna
	 *  
	 * @param  comuna
	 * @return String 
	 * @throws EmpresasDAOException
	 */
	//public String getLocalComuna( long comuna ) throws EmpresasDAOException;

	/**
	 * Inserta datos de solicitud de registro
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws EmpresasDAOException
	 */
	public long insSolReg(SolRegDTO dto) throws EmpresasDAOException;	
	
	/**
	 * Obtiene el saldo pendiente actualizado de la Empresa 
	 * pedidos pendientes (sin tomar en cuenta los anulados)
	 * 
	 * @param  empresa 
	 * @return double saldo_pendiente  
	 * @throws EmpresasDAOException
	 */
	public double getSaldoActualPendiente( long empresa ) throws EmpresasDAOException;
	
	
}
