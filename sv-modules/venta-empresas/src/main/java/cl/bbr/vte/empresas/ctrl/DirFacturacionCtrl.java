package cl.bbr.vte.empresas.ctrl;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.ComunaEntity;
import cl.bbr.jumbocl.common.model.DirFacturacionEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.vte.empresas.dao.DAOFactory;
import cl.bbr.vte.empresas.dao.JdbcDirFacturacionDAO;
import cl.bbr.vte.empresas.dao.JdbcDireccionesDAO;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.exceptions.DirFacturacionDAOException;
import cl.bbr.vte.empresas.exceptions.DirFacturacionException;
import cl.bbr.vte.empresas.exceptions.DireccionesDAOException;
import cl.bbr.vte.empresas.exceptions.DireccionesException;

/**
 * 
 * <p>Clase para que controla el flujo de datos desde el reposotorio de datos DAO Direcciones de facturación a la aplicación.
 * 
 * @author BBR ecommerce & retail
 *
 */
public class DirFacturacionCtrl {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Obtiene el listado de direcciones de facturación relacionadas a una sucursal, 
	 * segun el id de sucursal
	 * 
	 * @param  id_sucursal
	 * @return List DirFacturacionDTO
	 * @throws DirFacturacionException
	 * @throws SystemException
	 */
	public List getListDireccionFactBySucursal(long id_sucursal) throws DirFacturacionException, SystemException {
		List result = new ArrayList();
		
		JdbcDirFacturacionDAO dao = (JdbcDirFacturacionDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDirFacturacionDAO();
		try {
			List lst_ent = dao.getListDireccionFactBySucursal(id_sucursal);
			DirFacturacionDTO dto = null;
			
			for(int i=0; i<lst_ent.size(); i++){
				DirFacturacionEntity ent = (DirFacturacionEntity)lst_ent.get(i);
				dto = new DirFacturacionDTO();
				dto.setDfac_id(ent.getDfac_id().longValue());
				dto.setNom_tip_calle(ent.getNom_tip_calle());
				dto.setDfac_calle(ent.getDfac_calle());
				dto.setDfac_numero(ent.getDfac_numero());
				dto.setDfac_depto(ent.getDfac_depto());
				dto.setNom_comuna(ent.getNom_comuna());
				dto.setNom_region(ent.getNom_region());
				dto.setDfac_alias(ent.getDfac_alias());
				//dto.setNom_local(ent.getNom_local());
				result.add(dto);
			}
			
		}catch(DirFacturacionDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe la sucursal  " + id_sucursal );
				throw new DirFacturacionException(Constantes._EX_SUC_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Agrega una nueva direccion de facturacion, segun los datos del DTO
	 * 
	 * @param  dto , contiene datos de direccion de facturacion
	 * @return id de la nueva direccion de facturacion
	 * @throws DirFacturacionException
	 * @throws SystemException
	 */
	public long insDirFacturacion(DirFacturacionDTO dto) throws DirFacturacionException, SystemException {
		long result = -1;

		JdbcDirFacturacionDAO dao = (JdbcDirFacturacionDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDirFacturacionDAO();
		try {
			result = dao.insDirFacturacion(dto);
			
		}catch(DirFacturacionDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new DirFacturacionException(ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Actualiza la información de una direccion de facturacion, segun los datos del DTO
	 * 
	 * @param  dto
	 * @return boolean, "true" si se inserto, "false" si hubo error
	 * @throws DirFacturacionException
	 * @throws SystemException
	 */
	public boolean modDirFacturacion(DirFacturacionDTO dto) throws DirFacturacionException, SystemException {
		boolean result = false;

		JdbcDirFacturacionDAO dao = (JdbcDirFacturacionDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDirFacturacionDAO();
		try {
			result = dao.modDirFacturacion(dto);
			
		}catch(DirFacturacionDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new DirFacturacionException(ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Elimina una direccion de facturacion, cambiando el estado a 'E' Eliminado
	 * 
	 * @param id_dir_fact
	 * @return boolean, "true" si se inserto, "false" si hubo error
	 * @throws DirFacturacionException
	 * @throws SystemException
	 */
	public boolean delDirFacturacion(long id_dir_fact) throws DirFacturacionException, SystemException {
		boolean result = false;

		JdbcDirFacturacionDAO dao = (JdbcDirFacturacionDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDirFacturacionDAO();
		try {
			result = dao.delDirFacturacion(id_dir_fact);
			
		}catch(DirFacturacionDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new DirFacturacionException(ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene informacion de la direccion de facturacion, segun el id de direccion de facturacion
	 * 
	 * @param  id_dir_fact
	 * @return DirFacturacionDTO
	 * @throws DirFacturacionException
	 * @throws SystemException
	 */
	public DirFacturacionDTO getDireccionesFactByIdFact(long id_dir_fact) throws DirFacturacionException, SystemException {
		DirFacturacionDTO result = null;

		JdbcDirFacturacionDAO dao = (JdbcDirFacturacionDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDirFacturacionDAO();
		JdbcDireccionesDAO daoD = (JdbcDireccionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();
		try {
			DirFacturacionEntity ent = dao.getDireccionesFactByIdFact(id_dir_fact);
			if(ent != null){
				result = new DirFacturacionDTO ();
				result.setDfac_id(ent.getDfac_id().longValue());
				result.setDfac_tip_id(ent.getDfac_tip_id().longValue());
				result.setDfac_cli_id(ent.getDfac_cli_id().longValue());
				result.setDfac_com_id(ent.getDfac_com_id().longValue());
				result.setDfac_alias(ent.getDfac_alias());
				result.setDfac_calle(ent.getDfac_calle());
				result.setDfac_numero(ent.getDfac_numero());
				result.setDfac_depto(ent.getDfac_depto());
				result.setDfac_comentarios(ent.getDfac_comentarios());
				result.setDfac_estado(ent.getDfac_estado());
				result.setDfac_ciudad(ent.getDfac_ciudad());
				result.setDfac_fax(ent.getDfac_fax());
				result.setDfac_nom_contacto(ent.getDfac_nom_contacto());
				result.setDfac_cargo(ent.getDfac_cargo());
				result.setDfac_email(ent.getDfac_email());
				result.setDfac_fono1(ent.getDfac_fono1());
				result.setDfac_fono2(ent.getDfac_fono2());
				result.setDfac_fono3(ent.getDfac_fono3());
				result.setDfac_fec_crea(Formatos.frmFechaHora(ent.getDfac_fec_crea()));
				ComunaEntity com = daoD.getComunaFactById(ent.getDfac_com_id().longValue());
				result.setDfac_reg_id(com.getId_region().longValue());
				result.setNom_comuna(com.getNombre());
			}
			
		}catch(DirFacturacionDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				throw new DirFacturacionException(ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}
	
	/**
	 * Listado de direcciones de facturacion por comprador
	 * 
	 * @param comprador_id
	 * @return List DireccionesDTO
	 * @throws DireccionesException
	 */
	public List getListDireccionFactByComprador(long comprador_id) throws DireccionesException {

		List result = new ArrayList();

		try {
			JdbcDirFacturacionDAO dao = (JdbcDirFacturacionDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDirFacturacionDAO();
			
			result =  dao.getListDireccionFactByComprador( comprador_id );
			
		} catch (DireccionesDAOException ex) {
			logger.error("Problema", ex);
			throw new DireccionesException(ex);
		}

		return result;

	}		
	
}
