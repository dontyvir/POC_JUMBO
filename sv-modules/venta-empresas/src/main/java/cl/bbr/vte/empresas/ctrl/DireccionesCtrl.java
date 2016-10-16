package cl.bbr.vte.empresas.ctrl;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.ComunaEntity;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.RegionEntity;
import cl.bbr.jumbocl.common.model.TipoCalleEntity;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.vte.empresas.dao.DAOFactory;
import cl.bbr.vte.empresas.dao.JdbcDireccionesDAO;
import cl.bbr.vte.empresas.dto.ComunaDTO;
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.empresas.dto.RegionesDTO;
import cl.bbr.vte.empresas.dto.TiposCallesDTO;
import cl.bbr.vte.empresas.exceptions.CompradoresException;
import cl.bbr.vte.empresas.exceptions.DireccionesDAOException;
import cl.bbr.vte.empresas.exceptions.DireccionesException;
import cl.bbr.vte.empresas.exceptions.EmpresasException;

/**
 * <p>Clase para que controla el flujo de datos desde el reposotorio de datos DAO Direcciones a la aplicación. 
 *  
 * @author BBR ecommerce & retail
 *
 */
public class DireccionesCtrl {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Obtiene listado de tipo de calles
	 * 
	 * @return Lista de TiposCallesDTO
	 * @throws DireccionesException
	 * @throws SystemException
	 */
	public List getTiposCalleAll() throws DireccionesException, SystemException{
		List result = new ArrayList();
		
		JdbcDireccionesDAO dao = (JdbcDireccionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();
		
		try{
			List lst_tipcall = dao.getTiposCalleAll();
			TipoCalleEntity dir = null;
			for (int i = 0; i < lst_tipcall.size(); i++) {
				dir = null;
				dir = (TipoCalleEntity) lst_tipcall.get(i);
				TiposCallesDTO dto = new TiposCallesDTO();

				dto.setId(dir.getId().longValue());
				dto.setNombre(dir.getNombre());

				result.add(dto);
			}
		} catch(DireccionesDAOException ex){
			logger.debug("Problema :" + ex);
			throw new DireccionesException(ex);
			
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}

	/**
	 * Obtiene el listado de comunas 
	 * 
	 * @return Lista de comunas
	 * @throws DireccionesException
	 * @throws SystemException
	 */
	public List getComunas() throws DireccionesException, SystemException{
		List result = new ArrayList();
		
		JdbcDireccionesDAO dao = (JdbcDireccionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();
		
		try{
			List lst_tipcall = dao.getComunas();
			ComunaEntity ent = null;
			for (int i = 0; i < lst_tipcall.size(); i++) {
				ent = null;
				ent = (ComunaEntity) lst_tipcall.get(i);
				ComunaDTO dto = new ComunaDTO();

				dto.setId_comuna(ent.getId_comuna().longValue());
				dto.setId_region(ent.getId_region().longValue());
				dto.setReg_nombre(ent.getReg_nombre());
				dto.setNombre(ent.getNombre());

				result.add(dto);
			}
		} catch(DireccionesDAOException ex){
			logger.debug("Problema :" + ex);
			throw new DireccionesException(ex);
			
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}
	
	/**
	 * Elimina una direccion de despacho
	 * 
	 * @param  direccion_id		Identificador único de la dirección a eliminar
	 * @return True: éxito False: error 
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean delDirDespacho(long direccion_id) throws DireccionesException, SystemException{
		boolean result = false;
		
		JdbcDireccionesDAO dao = (JdbcDireccionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();
		
		try{
			DireccionesDTO dto = new DireccionesDTO(); 
			dto.setId(direccion_id);
			result = dao.delDirDespacho(dto);
		}catch(DireccionesDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe la direccion");
				throw new DireccionesException(Constantes._EX_REG_ID_NO_EXISTE, ex);
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
	 * Obtiene datos de la direccion de despacho según identificador único
	 * 
	 * @param  direccion_id 	Identificador único de la dirección
	 * @return DireccionesDTO 	Datos de la dirección de despacho
	 * @throws DireccionesException
	 * @throws SystemException
	 */
	public DireccionEntity getDireccionesDespByIdDesp(long direccion_id) throws DireccionesException, SystemException{
		//DireccionesDTO dto = null;
		
		JdbcDireccionesDAO dao = (JdbcDireccionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();
		DireccionEntity dir;
		try {
			dir = dao.getDireccionesDespByIdDesp(direccion_id);
			/*if(dir!=null){
				dto = new DireccionesDTO();
				dto.setId(dir.getId().longValue());
				dto.setLoc_cod(dir.getLoc_cod().longValue());
				dto.setNom_comuna(dir.getNom_comuna());
				dto.setCom_id(dir.getCom_id().longValue());
				dto.setTip_id(dir.getTip_id().longValue());
				dto.setCli_id(dir.getCli_id().longValue());
				dto.setAlias(dir.getAlias());
				dto.setCalle(dir.getCalle());
				dto.setNumero(dir.getNumero());
				dto.setDepto(dir.getDepto());
				dto.setComentarios(dir.getComentarios());
				dto.setFec_crea(Formatos.frmFechaHora(dir.getFec_crea()));
				dto.setEstado(dir.getEstado());
				dto.setId_poligono(dir.getId_poligono());
				dto.setReg_id(dir.getReg_id().longValue());
				dto.setNom_region(dir.getNom_region());
			}*/
		}catch(DireccionesDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe la direccion  " + direccion_id );
				throw new DireccionesException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return dir;
	}

	
	/**
	 * Obtiene datos de la direccion de despacho según identificador único
	 * 
	 * @param  direccion_id 	Identificador único de la dirección
	 * @return DireccionesDTO 	Datos de la dirección de despacho
	 * @throws DireccionesException
	 * @throws SystemException
	 */
	public long getLocalDireccion(long direccion_id) throws DireccionesException, SystemException{
		//DireccionesDTO dto = null;
		
		JdbcDireccionesDAO dao = (JdbcDireccionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();
		try {
		    return dao.getLocalDireccion(direccion_id);
		}catch(DireccionesDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe la direccion  " + direccion_id );
				throw new DireccionesException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}


	/**
	 * Recupera la lista de Regiones y la retorna como una lista de DTO (RegionesDTO).
	 * 
	 * @return	Lista de DTO (RegionesDTO)
	 * @throws RegionesException
	 */
	public List getRegiones() throws DireccionesException {
	
		List result = new ArrayList();
	
		try {
			JdbcDireccionesDAO datosDAO = (JdbcDireccionesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();
	
			List lista = new ArrayList();
			lista = (List) datosDAO.getRegiones();
			RegionEntity ent = null;
			RegionesDTO dto = null;
			for (int i = 0; i < lista.size(); i++) {
				ent = null;
				ent = (RegionEntity) lista.get(i);
				
				dto = new RegionesDTO();
	
				dto.setId(ent.getId().longValue());
				dto.setNombre(ent.getNombre());
				dto.setNumero(ent.getNumero().longValue());
	
				result.add(dto);
				
			}
	
		} catch (DireccionesDAOException ex) {
			logger.error("Problema", ex);
			throw new DireccionesException(ex);
		}
	
		return result;
	
	}	
	
	
	/**
	 * Recupera la lista de Comunas por region  y la retorna como una lista de DTO (ComunaDTO).
	 * 
	 * @param reg_id	identificador único de la region
	 * @return	Lista de DTO (ComunaDTO)
	 * @throws DireccionesException
	 */
	public List getAllComunas( long reg_id ) throws DireccionesException {

		List result = new ArrayList();

		try {
			JdbcDireccionesDAO datosDAO = (JdbcDireccionesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();

			List lista = new ArrayList();
			lista = (List) datosDAO.getAllComunas( reg_id );
			ComunaEntity ent = null;
			for (int i = 0; i < lista.size(); i++) {
				ent = null;
				ent = (ComunaEntity) lista.get(i);
				
				ComunaDTO dto = new ComunaDTO();
				dto.setId_comuna(ent.getId_comuna().longValue());
				dto.setNombre(ent.getNombre());
				dto.setId_region(ent.getId_region().longValue());

				result.add(dto);
				
			}

		} catch (DireccionesDAOException ex) {
			logger.error("Problema", ex);
			throw new DireccionesException(ex);
		}

		return result;

	}

	/**
	 * Obtiene el listado de direcciones de despacho relacionadas a una sucursal , segun el id de sucursal
	 * 
	 * @param id_sucursal
	 * @return List DireccionesDTO
	 * @throws DireccionesException
	 */
	public List getListDireccionDespBySucursal(long id_sucursal) throws DireccionesException {

		List result = new ArrayList();

		try {
			JdbcDireccionesDAO dao = (JdbcDireccionesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();

			result =  dao.getListDireccionDespBySucursal( id_sucursal );
			
			/*
			//segun consultado la informacion de nombre de tipocalle, region, etc en otras consultas de BD
			DireccionEntity ent = null;
			for (int i = 0; i < lista.size(); i++) {
				ent = (DireccionEntity) lista.get(i);
				dto.setId(ent.getId().longValue());
				//obtener tipo de calle
				TipoCalleEntity tip_call = dao.getTiposCalleById(ent.getTip_id().longValue());
				dto.setNom_tip_calle(tip_call.getNombre());
				dto.setCalle(ent.getCalle());
				dto.setNumero(ent.getNumero());
				dto.setDepto(ent.getDepto());
				//obtener nombre de la comuna
				ComunaEntity comuna = dao.getComunaById(ent.getCom_id().longValue());
				dto.setCom_nombre(comuna.getNombre());
				dto.setNom_comuna(comuna.getNombre());
				//obtener nombre de la region
				RegionEntity region = dao.getRegionById(ent.getReg_id().longValue());
				dto.setReg_nombre(region.getNombre());
				//obtener nombre del local
				LocalEntity local 	= dao.getLocalById(ent.getLoc_cod().longValue());
				dto.setNom_local(local.getNom_local());
				result.add(dto);
			}
			*/

		} catch (DireccionesDAOException ex) {
			logger.error("Problema", ex);
			throw new DireccionesException(ex);
		}

		return result;

	}
	
	/**
	 * Agrega una dirección de despacho a la sucursal
	 * 
	 * @param  direccion	DTO con Datos de la dirección
	 * @return True: éxito False: error
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public long insDirDespacho(DireccionesDTO direccion) throws DireccionesException, SystemException{
		long result = -1;
		
		JdbcDireccionesDAO dao = (JdbcDireccionesDAO) DAOFactory
		.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO(); 
		
		try {
			//obtener id de region
			ComunaEntity comuna = dao.getComunaById(direccion.getCom_id());
			direccion.setReg_id(comuna.getId_region().longValue());
			logger.debug("id_region = "+comuna.getId_region().longValue());
			result = dao.insDirDespacho(direccion);
			logger.debug("En insDirDespacho(), result:"+result);
			
		}catch(DireccionesDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_DUP_KEY_CODE ) ){ 
				logger.warn("Dirección ya existe." );
				throw new DireccionesException(Constantes._EX_KEY_DUPLICADA, ex);
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
	 * Modifica la direccion de despacho de una sucursal
	 * 
	 * @param  direccion
	 * @return boolean
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	public boolean modDirDespacho(DireccionesDTO direccion) throws DireccionesException, SystemException{
		boolean result = false;
		
		JdbcDireccionesDAO dao = (JdbcDireccionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();  
		//JdbcPoligonosDAO dao2 = (JdbcPoligonosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPoligonosDAO();  
		
		try {
			// Recuperar local por defecto para la comuna de la dirección
			//String local = dao.getLocalComuna( direccion.getCom_id() );
			//int id_local = dao.getLocalByPoligono((int)direccion.getId_poligono());
			/*direccion.setLoc_cod(Long.parseLong(local));
			direccion.setLoc_cod(id_local);
			if( id_local == 0 ) {
				logger.info("modDirDespacho - Recuperar local por defecto");
				throw new DireccionesException("modDirDespacho - Recuperar local por defecto");
			}*/
			result = dao.modDirDespacho(direccion);
			
		}catch(DireccionesDAOException ex){
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe comprador" );
				throw new DireccionesException(Constantes._EX_REG_ID_NO_EXISTE, ex);
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
	 * Obtiene el listado de direcciones de despacho de una sucursal
	 * 
	 * @param  sucursal_id identificador único de la sucursal
	 * @return Lista de DTO
	 * @throws EmpresasException
	 * @throws SystemException
	 */
	/*public List getListDireccionDesBySucursal(long sucursal_id) throws DireccionesException, SystemException{
		List result = new ArrayList();
		
		JdbcDireccionesDAO dao = (JdbcDireccionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO(); 
		try {
			List lst_dir = dao.getListDireccionDespBySucursal(sucursal_id);
			DireccionesDTO dto = null;
			
			for(int i=0; i<lst_dir.size(); i++){
				DireccionEntity dir = (DireccionEntity)lst_dir.get(i);
				if(dir!=null){
					dto = new DireccionesDTO();
					dto.setId(dir.getId().longValue());
					dto.setLoc_cod(dir.getLoc_cod().longValue());
					dto.setCom_nombre(dir.getCom_nombre());
					dto.setCom_id(dir.getCom_id().longValue());
					dto.setTip_id(dir.getTip_id().longValue());
					dto.setCli_id(dir.getCli_id().longValue());
					dto.setAlias(dir.getAlias());
					dto.setCalle(dir.getCalle());
					dto.setNumero(dir.getNumero());
					dto.setDepto(dir.getDepto());
					dto.setComentarios(dir.getComentarios());
					dto.setFec_crea(Formatos.frmFechaHora(dir.getFec_crea()));
					dto.setEstado(dir.getEstado());
					dto.setZona_id(dir.getZona_id().longValue());
					dto.setReg_id(dir.getReg_id().longValue());
					dto.setNom_region(dir.getNom_region());
					result.add(dto);
				}				
			}
			
		} catch(DireccionesDAOException ex) {
			logger.warn("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.warn("No existe la empresa  " + sucursal_id );
				throw new DireccionesException(Constantes._EX_REG_ID_NO_EXISTE, ex);
			}
			else{
				throw new SystemException("Error no controlado",ex);
			}
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
		return result;
	}*/
	

	/**
	 * Listado de direcciones de despacho por comprador
	 * 
	 * @param comprador_id
	 * @return List DireccionesDTO
	 * @throws DireccionesException
	 */
	public List getListDireccionDespByComprador(long comprador_id) throws DireccionesException {

		List result = new ArrayList();

		try {
			JdbcDireccionesDAO dao = (JdbcDireccionesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();

			result =  dao.getListDireccionDespByComprador( comprador_id );
			
		} catch (DireccionesDAOException ex) {
			logger.error("Problema", ex);
			throw new DireccionesException(ex);
		}

		return result;

	}
	
	/**
	 * Recupera la lista general de todas las comunas
	 * 
	 * @return	Lista de DTO (ComunaDTO)
	 * @throws DireccionesException
	 */
	public List getComunasGeneral(  ) throws DireccionesException {

		List result = new ArrayList();

		try {
			JdbcDireccionesDAO datosDAO = (JdbcDireccionesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getDireccionesDAO();

			List lista = new ArrayList();
			lista = (List) datosDAO.getComunasGeneral( );
			ComunaEntity ent = null;
			for (int i = 0; i < lista.size(); i++) {
				ent = null;
				ent = (ComunaEntity) lista.get(i);
				
				ComunaDTO dto = new ComunaDTO();
				dto.setId_comuna(ent.getId_comuna().longValue());
				dto.setNombre(ent.getNombre());
				dto.setId_region(ent.getId_region().longValue());

				result.add(dto);
				
			}

		} catch (DireccionesDAOException ex) {
			logger.error("Problema", ex);
			throw new DireccionesException(ex);
		}

		return result;

	}
	
}
