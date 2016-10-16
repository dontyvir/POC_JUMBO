package cl.bbr.vte.empresas.service;

import java.util.List;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.vte.empresas.ctrl.CompradoresCtrl;
import cl.bbr.vte.empresas.ctrl.DirFacturacionCtrl;
import cl.bbr.vte.empresas.ctrl.DireccionesCtrl;
import cl.bbr.vte.empresas.ctrl.EmpresasCtrl;
import cl.bbr.vte.empresas.ctrl.SucursalesCtrl;
import cl.bbr.vte.empresas.dto.ComprXEmpDTO;
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.CompradorCriteriaDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO;
import cl.bbr.vte.empresas.dto.EmpresaLogDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.MailDTO;
import cl.bbr.vte.empresas.dto.SolRegDTO;
import cl.bbr.vte.empresas.dto.SucursalCriteriaDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.empresas.dto.UserDTO;
import cl.bbr.vte.empresas.exceptions.CompradoresDAOException;
import cl.bbr.vte.empresas.exceptions.CompradoresException;
import cl.bbr.vte.empresas.exceptions.DirFacturacionException;
import cl.bbr.vte.empresas.exceptions.DireccionesException;
import cl.bbr.vte.empresas.exceptions.EmpresasException;
import cl.bbr.vte.empresas.exceptions.SucursalesException;


/**
 * 
 * Capa de servicios para el área de Empresas
 * 
 * @author BBR e-commerce & retail
 *
 */
public class EmpresasService {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);	
	
	/*  ***********************************************************************************
	 *  METODOS DE EMPRESAS 
	 *  ***********************************************************************************
	 *  */
	/**
	 * Obtiene informacion de la empresa y listado de sucursales, segun id
	 * 
	 * @param  id	Identificador único de la empresa
	 * @return Datos de la empresa (EmpresasDTO) 
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public EmpresasDTO getEmpresaById(long id) throws ServiceException, SystemException{
	
		EmpresasCtrl empresas = new EmpresasCtrl();
		try {
			return empresas.getEmpresaById(id);
		} catch (EmpresasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene datos de la empresa, según el RUT
	 * 
	 * @param rut	RUT de empresa
	 * @return Datos de la empresa (EmpresasDTO)
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public EmpresasDTO getEmpresaByRut(long rut) throws ServiceException, SystemException{
		
		EmpresasCtrl empresas = new EmpresasCtrl();
		try {
			return empresas.getEmpresaByRut(rut);
		} catch (EmpresasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de empresas segun el criterio de busqueda
	 * 
	 * @param criterio	Criterio de búqueda
	 * @return List EmpresasDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getEmpresasByCriteria(EmpresaCriteriaDTO criterio) throws ServiceException, SystemException{
		EmpresasCtrl empresas = new EmpresasCtrl();
		try {
			return empresas.getEmpresasByCriteria(criterio);
		} catch (EmpresasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene la cantidad de empresas, segun el criterio de búsqueda
	 * 
	 * @param criterio	Criterio de búqueda
	 * @return Cantidad de empresas
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public int getEmpresasCountByCriteria(EmpresaCriteriaDTO criterio) throws ServiceException, SystemException{
		EmpresasCtrl empresas = new EmpresasCtrl();
		try {
			return empresas.getEmpresasCountByCriteria(criterio);
		} catch (EmpresasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Inserta nueva empresa
	 * 
	 * @param  dto	Datos de la empresa (EmpresasDTO)
	 * @return True: éxito False: error
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long setCreaEmpresa(EmpresasDTO dto)throws ServiceException, SystemException{
		
		EmpresasCtrl empresas = new EmpresasCtrl();
		try {
			return empresas.setCreaEmpresa(dto);
		} catch (EmpresasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Modifica informacion de la empresa
	 * 
	 * @param  dto	Datos de la empresa (EmpresasDTO)
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setModEmpresa(EmpresasDTO dto)throws ServiceException, SystemException{
		
		EmpresasCtrl empresas = new EmpresasCtrl();
		try {
			return empresas.modEmpresa(dto);
		} catch (EmpresasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Elimina informacion de la empresa
	 * 
	 * @param  dto	Datos de la empresa (EmpresasDTO)
	 * @return True: éxito False: error
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean delEmpresa(EmpresasDTO dto) throws ServiceException, SystemException{
		
		EmpresasCtrl empresas = new EmpresasCtrl();
		try {
			return empresas.delEmpresa(dto);
		} catch (EmpresasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	

	/**
	 *  Agrega registro de mail para ser enviado al ejecutivo
	 *  
	 * @param  mail
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean insMail(MailDTO mail) throws ServiceException, SystemException{
		
		EmpresasCtrl empresas = new EmpresasCtrl();
		try {
			return empresas.insMail(mail);
		} catch (EmpresasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de estados de empresa, segun el tipo
	 * 
	 * @param tipo	Tipo
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getEstadosByTipo(String tipo) throws ServiceException, SystemException{
		
		EmpresasCtrl empresas = new EmpresasCtrl();
		try {
			return empresas.getEstadosByTipo(tipo);
		} catch (EmpresasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	

	/*  ***********************************************************************************
	 *  METODOS DE SUCURSALES 
	 *  ***********************************************************************************
	 *  */
	/**
	 * Obtiene la información de la sucursal , segun el código
	 * 
	 * @param id	Identificador único para la sucursal
	 * @return 		SucursalesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public SucursalesDTO getSucursalById(long id) throws ServiceException, SystemException{
		
		SucursalesCtrl sucursales = new SucursalesCtrl();
		try {
			return sucursales.getSucursalById(id);
		} catch (SucursalesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de los sucursales, segun los criterios de búsqueda ingresados
	 * @param criterio
	 * @return List SucursalesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getSucursalesByCriteria(SucursalCriteriaDTO criterio) throws ServiceException, SystemException{
		SucursalesCtrl sucursales = new SucursalesCtrl();
		try {
			return sucursales.getSucursalesByCriteria(criterio);
		} catch (SucursalesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el total de sucursales que coinciden con los criterios de búsqueda ingresados
	 * 
	 * @param criterio
	 * @return int, cantidad
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public int getSucursalesCountByCriteria(SucursalCriteriaDTO criterio) throws ServiceException, SystemException{
		SucursalesCtrl sucursales = new SucursalesCtrl();
		try {
			return sucursales.getSucursalesCountByCriteria(criterio);
		} catch (SucursalesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de sucursales de una empresa, segun el Id
	 * 
	 * @param id_empresa
	 * @return List SucursalesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getSucursalesByEmpresaId(long id_empresa)throws ServiceException, SystemException{
		SucursalesCtrl sucursales = new SucursalesCtrl();
		try {
			return sucursales.getSucursalesByEmpresaId(id_empresa);
		} catch (SucursalesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Permite la creación de una nueva sucursal, con los datos ingresados en el formulario.
	 * 
	 * @param  prm , contiene la información ingresada en el formulario
	 * @return long
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long setCreaSucursal(SucursalesDTO prm) throws ServiceException, SystemException{
		SucursalesCtrl sucursales = new SucursalesCtrl();
		try {
			return sucursales.setCreaSucursal(prm);
		} catch (SucursalesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Permite la modificación de datos de una sucursal, segun los datos ingresados 
	 * en el formulario de la sucursal
	 * 
	 * @param  prm
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setModSucursal(SucursalesDTO prm) throws ServiceException, SystemException{
		SucursalesCtrl sucursales = new SucursalesCtrl();
		try {
			return sucursales.setModSucursal(prm);
		} catch (SucursalesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*	***********************************************************************************
	 *  METODOS DE COMPRADORES 
	 *  ***********************************************************************************
	 *  */
	/**
	 * Obtiene informacion del comprador, segun el Id
	 * 
	 * @param  comprador_id
	 * @return CompradoresDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public CompradoresDTO getCompradoresById(long comprador_id) throws ServiceException, SystemException{
		
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getCompradoresById(comprador_id);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene informacion del comprador, segun el Rut
	 * 
	 * @param  rut	RUT comprador a buscar
	 * @return CompradoresDTO	DTO con datos del comprador
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public CompradoresDTO getCompradoresByRut(long rut) throws ServiceException, SystemException{
		
		/**
		 * Instacia del controler Compradores
		 */
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getCompradoresByRut(rut);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Inserta la informacion del nuevo comprador
	 * 
	 * @param  comprador
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long setCreaComprador(CompradoresDTO comprador) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.setCreaComprador(comprador);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Modifica la informacion del comprador.
	 * 
	 * @param  comprador
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setModComprador(CompradoresDTO comprador) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.setModComprador(comprador);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Asocia comprador con la sucursal y con la empresa si es administrador de esta
	 * 
	 * @param  comprador
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setSucEmpComprador(CompradoresDTO comprador) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.setSucEmpComprador(comprador);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	
	
	/**
	 * Elimina un comprador segun el id
	 * 
	 * @param  comprador_id
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean delCompradores(long comprador_id) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.delCompradores(comprador_id);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Obtiene el listado de compradores asociados a una sucursal, segun el código de sucursal
	 * 
	 * @param id_sucursal
	 * @return List CompradoresDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getListCompradoresBySucursalId(long id_sucursal) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getListCompradoresBySucursalId(id_sucursal);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de compradores asociados a una sucursal, segun el código de sucursal
	 * 
	 * @param id_sucursal
	 * @return List CompradoresDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getListCompradoresBySucursalId(long id_sucursal, String TipoAcceso, long comprador_id) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getListCompradoresBySucursalId(id_sucursal, TipoAcceso, comprador_id);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de compradores que coinciden con los criterios de busqueda ingresados
	 * 
	 * @param  dto
	 * @return List CompradoresDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getCompradoresByCriteria(CompradorCriteriaDTO dto) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getCompradoresByCriteria(dto);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene la cantidad total de compradores que cumple los criterios de busqueda ingresados
	 * 
	 * @param  dto
	 * @return int
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public int getCompradoresCountByCriteria(CompradorCriteriaDTO dto) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getCompradoresCountByCriteria(dto);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de las sucursales asociadas a un comprador
	 * 
	 * @param id_comprador
	 * @return List ComprXSucDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getListSucursalesByCompradorId(long id_comprador) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getListSucursalesByCompradorId(id_comprador);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	

	/**
	 * Obtiene el listado de las sucursales asociadas a un usuario
	 * 
	 * @param id_comprador
	 * @return List ComprXSucDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getListSucursalesByUser(long id_comprador) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getListSucursalesByUser(id_comprador);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	
	/**
	 * Obtiene el listado de las sucursales asociadas a un usuario
	 * 
	 * @param id_comprador
	 * @return List ComprXSucDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public String getListSucursalesByUser2(long id_comprador) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getListSucursalesByUser2(id_comprador);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	
	/**
	 * Obtiene el listado de sucursales relacionadas a un comprador para las empresas que el administrador tiene acceso
	 * 
	 * @param id_comprador Identificador único del comprador
	 * @param  id_administrador Identificador único del administrador
	 * @return List ComprXSucDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getListSucursalesByCompradorId(long id_comprador, long id_administrador) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getListSucursalesByCompradorId(id_comprador, id_administrador);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	
	
	/**
	 * Agrega la relacion entre sucursal y comprador
	 * 
	 * @param  prm
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean addRelSucursalComprador(ComprXSucDTO prm) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.addRelSucursalComprador(prm);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Elimina la relacion entre sucursal y comprador
	 * 
	 * @param  prm
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean delRelSucursalComprador(ComprXSucDTO prm) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.delRelSucursalComprador(prm);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de sucursales que no se encuentran asociadas al comprador
	 *  
	 * @param id_comprador Identificador único del comprador
	 * @param id_administrador Identificador único del comprador administrador
	 * @return List SucursalesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getListSucursalesNoAsocComprador(long id_administrador, long id_comprador) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getListSucursalesNoAsocComprador(id_administrador, id_comprador);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene informacion del comprador, y el tipo de comprador segun id de sucursal
	 * 
	 * @param id_comprador
	 * @param id_sucursal
	 * @return CompradoresDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public CompradoresDTO getCompradorXSucursal(long id_comprador, long id_sucursal) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.getCompradorXSucursal(id_comprador, id_sucursal);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Agrega la relacion entre empresa y comprador. donde el comprador sera el administrador
	 * 
	 * @param prm
	 * @return true, si se agrego con exito, false en caso contrario
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean addRelEmpresaComprador(ComprXEmpDTO prm) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.addRelEmpresaComprador(prm);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Elimina la relacion entre empresa y comprador,  donde el comprador sera el administrador
	 * 
	 * @param prm
	 * @return true, si se elimino con exito, false en caso contrario
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean delRelEmpresaComprador(ComprXEmpDTO prm) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.delRelEmpresaComprador(prm);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/*  ***********************************************************************************
	 *  METODOS DE DIRECCIONES DE DESPACHO
	 *  ***********************************************************************************
	 *  */
	/**
	 * Obtiene el listado de tipos de calle
	 * 
	 * @return List
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getTiposCalleAll() throws ServiceException, SystemException{
		
		DireccionesCtrl direcciones = new DireccionesCtrl();
		try {
			return direcciones.getTiposCalleAll();
		} catch (DireccionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de comunas
	 * 
	 * @return Lista de comunas
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getComunas() throws ServiceException, SystemException{
		
		DireccionesCtrl direcciones = new DireccionesCtrl();
		try {
			return direcciones.getComunas();
		} catch (DireccionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Elimina una direccion segun el id
	 * 
	 * @param  direccion_id
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean delDirDespacho(long direccion_id) throws ServiceException, SystemException{
		DireccionesCtrl direccion = new DireccionesCtrl();
		try {
			return direccion.delDirDespacho(direccion_id);
		} catch (DireccionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	
	
	/**
	 * Inserta direccion de despacho de una sucursal
	 * 
	 * @param dto
	 * @return id de nueva direccion de despacho
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long insDirDespacho(DireccionesDTO dto) throws ServiceException, SystemException{
		DireccionesCtrl direccion = new DireccionesCtrl();
		try {
			return direccion.insDirDespacho(dto);
		} catch (DireccionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	
	
	/**
	 * Modifica informacion de una direccion de despacho de una sucursal, segun los datos que contiene el DTO
	 * 
	 * @param dto
	 * @return boolean, "true" si tuvo exito, "false" en caso contrario
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean modDirDespacho(DireccionesDTO dto) throws ServiceException, SystemException{
		DireccionesCtrl direccion = new DireccionesCtrl();
		try {
			return direccion.modDirDespacho(dto);
		} catch (DireccionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Obtiene informacion del comprador, segun el Id
	 * 
	 * @param  direccion_id
	 * @return DireccionesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public DireccionEntity getDireccionesDespByIdDesp(long direccion_id) throws ServiceException, SystemException{
		
		DireccionesCtrl direcciones = new DireccionesCtrl();
		try {
			return direcciones.getDireccionesDespByIdDesp(direccion_id);
		} catch (DireccionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	


	/**
	 * Obtiene informacion del comprador, segun el Id
	 * 
	 * @param  direccion_id
	 * @return DireccionesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long getLocalDireccion(long direccion_id) throws ServiceException, SystemException{
		
		DireccionesCtrl direcciones = new DireccionesCtrl();
		try {
			return direcciones.getLocalDireccion(direccion_id);
		} catch (DireccionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	
	

	/**
	 * Recupera la lista de Regiones y la retorna como una lista de DTO (RegionesDTO).
	 * 
	 * @return	Lista de DTO (RegionesDTO)
	 * @throws ServiceException
	 */

	
	public List getAllRegiones() throws ServiceException, SystemException{
		
		DireccionesCtrl ctr = new DireccionesCtrl();
		List list = null;
		try {
			list = ctr.getRegiones();
		} catch (DireccionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return list;
	}	
	
	
	/**
	 * Recupera la lista de Comunas por region  y la retorna como una lista de DTO (ComunaDTO).
	 * 
	 * @param reg_id	identificador único de la region
	 * @return	Lista de DTO (ComunaDTO)
	 * @throws ServiceException
	 */
	public List getAllComunas( long reg_id ) throws ServiceException {

		DireccionesCtrl ctr = new DireccionesCtrl();
		List list = null;

		try {
			list = ctr.getAllComunas(  reg_id  );
		} catch (DireccionesException ex) {
			logger.error( "Problemas con controles de regiones", ex);
			throw new ServiceException(ex.getMessage());
		}

		return list;

	}
	
	/**
	 * Obtiene el listado de direcciones de despacho relacionadas a una sucursal, segun id de sucursal
	 * 
	 * @param  id_sucursal
	 * @return List DireccionesDTO
	 * @throws ServiceException
	 */
	public List getListDireccionDespBySucursal(long id_sucursal) throws ServiceException {

		DireccionesCtrl ctr = new DireccionesCtrl();
		try {
			return ctr.getListDireccionDespBySucursal(  id_sucursal  );
		} catch (DireccionesException ex) {
			logger.error( "Problemas con controles de Dir Despacho", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	/**
	 * Listado de direcciones de despacho por comprador
	 * 
	 * @param  comprador_id
	 * @return List DireccionesDTO
	 * @throws ServiceException
	 */
	public List getListDireccionDespByComprador(long comprador_id) throws ServiceException {

		DireccionesCtrl ctr = new DireccionesCtrl();
		try {
			return ctr.getListDireccionDespByComprador(  comprador_id  );
		} catch (DireccionesException ex) {
			logger.error( "Problemas con controles de Dir Despacho", ex);
			throw new ServiceException(ex.getMessage());
		}
	}	
	
	/*  ***********************************************************************************
	 *  METODOS DE DIRECCIONES DE FACTURACION
	 *  ***********************************************************************************
	 *  */

	/**
	 * Obtiene los datos de la direccion de facturación	por id de dirección de facturacion
	 * 
	 * @param  id_dir_fact
	 * @return DirFacturacionDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public  DirFacturacionDTO getDireccionesFactByIdFact(long id_dir_fact) throws ServiceException, SystemException {

		DirFacturacionCtrl ctr = new DirFacturacionCtrl();
		try {
			return ctr.getDireccionesFactByIdFact(  id_dir_fact  );
		} catch (DirFacturacionException ex) {
			logger.error( "Problemas con controles de Dir Facturacion", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
          
	/**
	 * Obtiene el listado de direcciones de facturacion de una sucursal, segun el id de sucursal
	 * 
	 * @param  id_sucursal
	 * @return List DirFacturacionDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getListDireccionFactBySucursal(long id_sucursal) throws ServiceException, SystemException {

		DirFacturacionCtrl ctr = new DirFacturacionCtrl();
		try {
			return ctr.getListDireccionFactBySucursal(  id_sucursal  );
		} catch (DirFacturacionException ex) {
			logger.error( "Problemas con controles de Dir Facturacion", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	/**
	 * Agrega una nueva direccion de facturacion , segun los datos contenidos en el dto
	 * @param  dto
	 * @return long, id de la nueva direccion de facturacion
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long insDirFacturacion(DirFacturacionDTO dto )throws ServiceException, SystemException {

		DirFacturacionCtrl ctr = new DirFacturacionCtrl();
		try {
			return ctr.insDirFacturacion(  dto  );
		} catch (DirFacturacionException ex) {
			logger.error( "Problemas con controles de Dir Facturacion", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	/**
	 * Modifica una direccion de facturacion , actualizando segun los datos contenidos en el dto
	 * 
	 * @param  dto
	 * @return boolean, "true" si tuvo exito, "false" en caso contrario
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean modDirFacturacion(DirFacturacionDTO dto )throws ServiceException, SystemException {

		DirFacturacionCtrl ctr = new DirFacturacionCtrl();
		try {
			return ctr.modDirFacturacion(  dto  );
		} catch (DirFacturacionException ex) {
			logger.error( "Problemas con controles de Dir Facturacion", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	/**
	 * Elimina una direccion de facturacion , cambiando el estado
	 * 
	 * @param  id_dir_fact
	 * @return boolean, "true" si tuvo exito, "false" en caso contrario
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean delDirFacturacion(long id_dir_fact )throws ServiceException, SystemException {

		DirFacturacionCtrl ctr = new DirFacturacionCtrl();
		try {
			return ctr.delDirFacturacion(  id_dir_fact  );
		} catch (DirFacturacionException ex) {
			logger.error( "Problemas con controles de Dir Facturacion", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/**
	 * Obtiene el listado de Empresas en la cual el comprador es administrador
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List  Lista con datos (EmpresasDTO)
	 * @throws CompradoresException, SystemException
	 */
	public List getListAdmEmpresasByCompradorId(long id_comprador) throws ServiceException, SystemException {

		CompradoresCtrl ctr = new CompradoresCtrl();
		try {
			return ctr.getListAdmEmpresasByCompradorId(  id_comprador  );
		} catch (CompradoresException ex) {
			logger.error( "Problemas con controles de Compradores", ex);
			throw new ServiceException(ex.getMessage());
		}
	} 
	
	/**
	 * Obtiene el listado de sucursales para las empresas a las cuales el comprador es administrador.
	 * 
	 * @param id_comprador	Identificador único del comprador
	 * @return Lista con datos (SucursalesDTO)
	 * @throws CompradoresException, SystemException
	 */
	public List getListAdmSucursalesByCompradorId(long id_comprador) throws ServiceException, SystemException {

		CompradoresCtrl ctr = new CompradoresCtrl();
		try {
			return ctr.getListAdmSucursalesByCompradorId(  id_comprador  );
		} catch (CompradoresException ex) {
			logger.error( "Problemas con controles de Compradores", ex);
			throw new ServiceException(ex.getMessage());
		}
	} 
	
	/**
	 * Obtiene el listado de compradores relacionados a una empresa, segun el id de la empresa.
	 * 
	 * @param  id_empresa Identificador de la empresa
	 * @return List CompradoresEntity
	 * @throws CompradoresException, SystemException
	 */
	public List getListCompradoresByEmpresalId(long id_empresa) throws ServiceException, SystemException {

		CompradoresCtrl ctr = new CompradoresCtrl();
		try {
			return ctr.getListCompradoresByEmpresalId(  id_empresa  );
		} catch (CompradoresException ex) {
			logger.error( "Problemas con controles de Compradores", ex);
			throw new ServiceException(ex.getMessage());
		}
	} 	
	
	/**
	 * 
	 * Obtiene el listado de compradores para las sucursales que el administrador tiene acceso.
	 * 
	 * @param id_administrador Identificador único del administrador
	 * @return Listado con datos de los compradores
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getListAdmCompradoresByAdministradorId(long id_administrador) throws ServiceException, SystemException {

		CompradoresCtrl ctr = new CompradoresCtrl();
		try {
			return ctr.getListAdmCompradoresByAdministradorId( id_administrador );
		} catch (CompradoresException ex) {
			logger.error( "Problemas con controles de Compradores", ex);
			throw new ServiceException(ex.getMessage());
		}
	} 	

	/**
	 * Inserta datos de solicitud de registro
	 * 
	 * @param  dto
	 * @return boolean, "true" si tuvo exito, "false" en caso contrario
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long insSolReg(SolRegDTO dto)throws ServiceException, SystemException {

		EmpresasCtrl ctr = new EmpresasCtrl();
		try {
			return ctr.insSolReg(dto);
		} catch (EmpresasException ex) {
			logger.error( "Problemas con controles de Empresas", ex);
			throw new ServiceException(ex.getMessage());
		}
	}	

	/**
	 * Inserta  mail a ejecutivo y datos para solicitud de registro
	 * @param solreg : DTO con los datos del formulario  solicitud  de registro 
	 * @return Id: Numero de solicitud de registro
	 * @throws ServiceException, SystemException
	 */
	
	public long setMailSolRegistro(SolRegDTO solreg) throws ServiceException, SystemException {
		EmpresasCtrl ctr = new EmpresasCtrl();
		try {
			return ctr.setMailSolRegistro(solreg);
		} catch (EmpresasException ex) {
			logger.error( "Problemas con controles de Empresas", ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	/**
	 * Listado de direcciones de facturacion por comprador
	 * 
	 * @param  comprador_id
	 * @return List DirFacturacionDTO
	 * @throws ServiceException
	 */
	public List getListDireccionFactByComprador(long comprador_id) throws ServiceException {

		DirFacturacionCtrl ctr = new DirFacturacionCtrl();
		try {
			return ctr.getListDireccionFactByComprador(  comprador_id  );
		} catch (DireccionesException ex) {
			logger.error( "Problemas con controles de Dir Despacho", ex);
			throw new ServiceException(ex.getMessage());
		}
	}		

	/**
	 * Obtiene el listado de Empresas en la cual el comprador puede comprar
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List  Lista con datos (EmpresasDTO)
	 * @throws CompradoresException, SystemException
	 */
	public List getListEmpresasByCompradorId(long id_comprador) throws ServiceException, SystemException {

		CompradoresCtrl ctr = new CompradoresCtrl();
		try {
			return ctr.getListEmpresasByCompradorId(  id_comprador  );
		} catch (CompradoresException ex) {
			logger.error( "Problemas con controles de Compradores", ex);
			throw new ServiceException(ex.getMessage());
		}
	} 	

	/**
	 * Obtiene el listado de Empresas en la cual el comprador puede comprar
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List  Lista con datos (EmpresasDTO)
	 * @throws CompradoresException, SystemException
	 */
	public List getListEmpresasByUser(long id_comprador) throws ServiceException, SystemException {

		CompradoresCtrl ctr = new CompradoresCtrl();
		try {
			return ctr.getListEmpresasByUser(  id_comprador  );
		} catch (CompradoresException ex) {
			logger.error( "Problemas con controles de Compradores", ex);
			throw new ServiceException(ex.getMessage());
		}
	} 	
	
	
	/**
	 * Obtiene los datos del ejecutivo fono empresa
	 * @param  login
	 * @return UserDTO
	 * @throws CompradoresDAOException
	 */	
	public UserDTO getEjecutivoGetByRut(String login ) throws ServiceException, SystemException {
		CompradoresCtrl ctr = new CompradoresCtrl();
		try {
			return ctr.getEjecutivoGetByRut(  login  );
		} catch (CompradoresException ex) {
			logger.error( "Problemas con controles de Compradores", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/** Realiza un cambio de estado al campo cli_estado a 'E'
	 * @param prm : SucursalesDTO
	 * @return : True o False
	 * @throws SucursalesException
	 * @throws SystemException
	 */
	public boolean delSucursal(SucursalesDTO prm) throws ServiceException, SystemException{
		SucursalesCtrl sucursal = new SucursalesCtrl();
		try {
			return sucursal.delSucursal(prm);
		} catch (SucursalesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	
	
	/**
	 * Recupera el monto de los pedidos por cobrar, para rectificar el saldo de la línea de crédito.
	 * 
	 * @param id_empresa Identificador único de la empresa
	 * @return			 Monto por cobrar
	 * @throws ServiceException, SystemException
	 */	
	public double getSaldoActualPendiente(long id_empresa) 
		throws ServiceException, SystemException {
		
		EmpresasCtrl empresa = new EmpresasCtrl(); 
		try {
			return empresa.getSaldoActualPendiente(id_empresa);
		} catch (EmpresasException ex) {
			logger.error( "Problemas con controles de Empresas", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	/**
	 * Verifica si un comprador es administrador 
	 * @param id_comprador
	 * @param id_sucursal
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean esAdministrador(long id_comprador,long id_sucursal) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.esAdministrador(id_comprador,id_sucursal);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Modifica los datos password - estado de un comprador
	 * 
	 * @param  comprador CompradoresDTO con datos del comprador
	 * @throws VteException, SystemException
	 */
	public boolean compradorChangePass(CompradoresDTO comprador) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.compradorChangePass(comprador);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Modifica la cantidad de intentos de logeo de un comprador
	 * 
	 * @param comprador_id 	Identificador único del comprador
	 * @param accion		Acción a gatillar 1: Aumentar, 0: Reset
	 * @throws VteException 
	 */
	public boolean updateIntentos(long comprador_id, long accion) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.updateIntentos(comprador_id, accion);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	

	/**
	 * Ejecucion del Wizard
	 * 
	 * @param  comprador CompradoresDTO con datos del comprador
	 * @param  registros_suc Lista con datos de la sucursal
	 * @param  registros_emp Lista DTO con datos de la empresa
	 * @throws VteException
	 */
	public boolean setExecWizard(CompradoresDTO comprador, List registros_suc, List registros_emp) throws ServiceException, SystemException{
		CompradoresCtrl compradores = new CompradoresCtrl();
		try {
			return compradores.setExecWizard(comprador, registros_suc, registros_emp);
		} catch (CompradoresException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	 	
	
	/**
	 * Listado general de comunas
	 * 
	 * @return Lista de ComunasDTO
	 * @throws SystemException
	 */
	public List getComunasGeneral( ) throws ServiceException, SystemException{
		DireccionesCtrl ctr = new DireccionesCtrl();
		List list = null;
		try {
			list = ctr.getComunasGeneral();
		} catch (DireccionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return list;
	}

    /**
     * @param prm
     * @return
     */
    public boolean setModEmpresaLinea(EmpresasDTO prm) throws ServiceException, SystemException {        
        EmpresasCtrl empresas = new EmpresasCtrl();
        try {
            return empresas.modEmpresaLinea(prm);
        } catch (EmpresasException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param log
     */
    public void setEmpresaLineaLog(EmpresaLogDTO log) throws ServiceException, SystemException {        
        EmpresasCtrl empresas = new EmpresasCtrl();
        try {
            empresas.setEmpresaLineaLog(log);
        } catch (EmpresasException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }
}
