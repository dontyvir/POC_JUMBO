package cl.bbr.jumbocl.contenidos.ctr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.CampanaEntity;
import cl.bbr.jumbocl.common.model.ElementoEntity;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCampElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCampanaDTO;
import cl.bbr.jumbocl.contenidos.dao.DAOFactory;
import cl.bbr.jumbocl.contenidos.dao.JdbcCampanaDAO;
import cl.bbr.jumbocl.contenidos.dao.JdbcElementoDAO;
import cl.bbr.jumbocl.contenidos.dto.CampanaDTO;
import cl.bbr.jumbocl.contenidos.dto.CampanasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CampanaDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.CampanaException;
import cl.bbr.jumbocl.contenidos.exceptions.ElementoDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Controlador de procesos sobre las campanias web.
 * @author BBR Ingeniería
 *
 */
public class CampanaCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Obtiene la campania, segun el codigo
	 * 
	 * @param  id_campana
	 * @return CampanaDTO
	 * @throws CampanaException
	 */
	public CampanaDTO getCampanaById(long id_campana) throws CampanaException{
		CampanaDTO camdto = null;
		try{
			logger.debug("en getCampanaById");
			JdbcCampanaDAO campanaDAO = (JdbcCampanaDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCampanaDAO();
			
			CampanaEntity ent = campanaDAO.getCampanaById(id_campana);
			camdto = new CampanaDTO();
			camdto.setId_campana(ent.getId_campana());
			camdto.setNombre(ent.getNombre());
			camdto.setDescripcion(ent.getDescripcion());
			camdto.setEstado(ent.getEstado());
			if(ent.getFec_creacion()!=null){
				camdto.setFec_creacion(ent.getFec_creacion().toString());
			}else{
				camdto.setFec_creacion("");
			}
			//obtener el listado de elementos relacionados a la campania
			List lst_elem = campanaDAO.getElementosByCampanaId(id_campana);
			camdto.setLst_elem(lst_elem);
				
		}catch(CampanaDAOException ex){
			logger.debug("Problema:"+ex);
			throw new CampanaException(ex);
		}
		return camdto;
	}

	/**
	 * Obtiene el Listado de campanias, segun criterios ingresados
	 * 
	 * @param  criterio
	 * @return List CampanaDTO
	 * @throws CampanaException
	 */
	public List getCampanasByCriteria(CampanasCriteriaDTO criterio) throws CampanaException{
		List result = new ArrayList();
		try{
			logger.debug("en getCampanasByCriteria");
			JdbcCampanaDAO campanaDAO = (JdbcCampanaDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCampanaDAO();
			
			List lst_campana = (List) campanaDAO.getCampanasByCriteria(criterio);
			CampanaEntity ent = null;
			for (int i = 0; i < lst_campana.size(); i++) {
				ent = (CampanaEntity) lst_campana.get(i);
				CampanaDTO camdto = new CampanaDTO();
				camdto.setId_campana(ent.getId_campana());
				camdto.setNombre(ent.getNombre());
				camdto.setDescripcion(ent.getDescripcion());
				camdto.setEstado(ent.getEstado());
				if(ent.getFec_creacion()!=null){
					camdto.setFec_creacion(ent.getFec_creacion().toString());
				}else{
					camdto.setFec_creacion("");
				}
				result.add(camdto);
			}
		}catch(CampanaDAOException ex){
			logger.debug("Problema:"+ex);
			throw new CampanaException(ex);
		}
		return result;
	}

	/**
	 * Obtiene la cantidad de campanias, segun criterio ingresado
	 * 
	 * @param  criterio
	 * @return int
	 * @throws CampanaException
	 */
	public int getCountCampanasByCriteria(CampanasCriteriaDTO criterio) throws CampanaException{
		int result = 0;
		try{
			logger.debug("en getCountCampanasByCriteria");
			JdbcCampanaDAO campanaDAO = (JdbcCampanaDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCampanaDAO();
			
			result = campanaDAO.getCountCampanasByCriteria(criterio);
		}catch(CampanaDAOException ex){
			logger.debug("Problema:"+ex);
			throw new CampanaException(ex);
		}
		return result;
	}

	/**
	 * Actualiza informacion de la campana
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaException
	 */
	public boolean setModCampana(ProcModCampanaDTO dto) throws CampanaException{
		boolean result = false;
		try{
			logger.debug("en setModCampana");
			JdbcCampanaDAO campanaDAO = (JdbcCampanaDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCampanaDAO();
			
			//verifica si existe la campana
			CampanaEntity camp = campanaDAO.getCampanaById(dto.getId_campana());
			if(camp==null){
				throw new CampanaException(Constantes._EX_CAM_ID_NO_EXISTE);
			}
			result = campanaDAO.setModCampana(dto);
			
		}catch(CampanaDAOException ex){
			logger.debug("Problema:"+ex);
			throw new CampanaException(ex);
		}
		return result;
	}

	/**
	 * Modifica la relacion entre campaña y elemento, agrega o elimina.
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaException
	 * @throws SystemException
	 */
	public boolean setModCampanaElemento(ProcModCampElementoDTO dto) throws CampanaException, SystemException{
		boolean result=false;
		logger.debug("en setModCampanaElemento");
		JdbcCampanaDAO 	campanaDAO = (JdbcCampanaDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCampanaDAO();
		JdbcElementoDAO elementoDAO = (JdbcElementoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getElementoDAO();
		//Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		
		//Iniciamos trx
		try {
			trx1.begin();
		} catch (DAOException e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		} catch (SQLException e) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		
		// Marcamos los dao's con la transacción
		try {
			campanaDAO.setTrx(trx1);
		} catch (CampanaDAOException e2) {
			logger.error("Error al asignar transacción al dao Campanas");
			throw new SystemException("Error al asignar transacción al dao Campanas");
		}
		try {
			elementoDAO.setTrx(trx1);
		} catch (ElementoDAOException e2) {
			logger.error("Error al asignar transacción al dao Elementos");
			throw new SystemException("Error al asignar transacción al dao Elementos");
		}
		
		try{			
			logger.debug("accion:"+dto.getAccion());
			
			//verificar q existe campaña y elemento
			CampanaEntity camp = campanaDAO.getCampanaById(dto.getId_campana()); 
			if(camp==null){
				trx1.rollback();
				throw new CampanaException(Constantes._EX_CAM_ID_NO_EXISTE);
			}
			ElementoEntity elem = elementoDAO.getElementoById(dto.getId_elemento()); 
			if(elem==null){
				trx1.rollback();
				throw new CampanaException(Constantes._EX_ELM_ID_NO_EXISTE);
			}
			
			//si accion es agregar:
			logger.debug("agregar:"+dto.getAccion());
			if(dto.getAccion().equals("agregar")){
				//verifica que no existe la relacion entre campania y elemento
				List lst_elem = campanaDAO.getElementosByCampanaId(dto.getId_campana());
				boolean existe = false;
				for(int i=0; i<lst_elem.size(); i++){
					ElementoEntity aux = (ElementoEntity) lst_elem.get(i);
					if(aux.getId_elemento()==dto.getId_elemento()){
						existe=true;
						break;
					}
				}
				if(existe){
					throw new CampanaException(Constantes._EX_CAM_ELEM_REL_EXISTE);
				}
				//si no existe la relacion, entonces crear.
				result = campanaDAO.addCampanaElemento(dto);
				
			}else if(dto.getAccion().equals("eliminar")){
				//verifica que existe la relacion entre campania y elemento
				List lst_elem = campanaDAO.getElementosByCampanaId(dto.getId_campana());
				boolean existe = false;
				for(int i=0; i<lst_elem.size(); i++){
					ElementoEntity aux = (ElementoEntity) lst_elem.get(i);
					if(aux.getId_elemento()==dto.getId_elemento()){
						existe=true;
						break;
					}
				}
				if(!existe){
					throw new CampanaException(Constantes._EX_CAM_ELEM_REL_NO_EXISTE);
				}
				//si existe la relacion, entonces eliminar
				result = campanaDAO.delCampanaElemento(dto);
			}else if(dto.getAccion().equals("modificar")){
				//verifica que existe la relacion entre campania y elemento
				List lst_elem = campanaDAO.getElementosByCampanaId(dto.getId_campana());
				boolean existe = false;
				for(int i=0; i<lst_elem.size(); i++){
					ElementoEntity aux = (ElementoEntity) lst_elem.get(i);
					if(aux.getId_elemento()==dto.getId_elemento()){
						existe=true;
						break;
					}
				}
				if(!existe){
					throw new CampanaException(Constantes._EX_CAM_ELEM_REL_NO_EXISTE);
				}
				//modificar la informacion de la relacion
				result = campanaDAO.modCampanaElemento(dto);
			}

		}catch(CampanaDAOException ex){			
			logger.debug("Problema setModCampanaElemento:"+ex);
//			 rollback trx
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
			throw new CampanaException(ex);
		} catch(ElementoDAOException ex){			
			logger.debug("Problema setModCampanaElemento:"+ex);
//			 rollback trx
			try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
			throw new CampanaException(ex);
		} catch (DAOException e) {
			logger.error("Error al hacer rollback");
			throw new SystemException("Error al hacer rollback");
		}
		//		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
	    
		return result;
	}

	/**
	 * Agrega una nueva campaña
	 * 
	 * @param  dto
	 * @return boolean
	 */
	public boolean setAddCampana(ProcAddCampanaDTO dto) throws CampanaException{
		boolean result=false;
		try{
			logger.debug("en setAddCampana");
			JdbcCampanaDAO campanaDAO = (JdbcCampanaDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCampanaDAO();
			
			result = campanaDAO.setAddCampana(dto);
			
		}catch(CampanaDAOException ex){
			logger.debug("Problema:"+ex);
			throw new CampanaException(ex);
		}
		return result;
	}

	/**
	 * Elimina una campaña
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaException 
	 */
	public boolean setDelCampana(ProcDelCampanaDTO dto) throws CampanaException {
		boolean result=false;
		try{
			logger.debug("en setDelCampana");
			JdbcCampanaDAO campanaDAO = (JdbcCampanaDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getCampanaDAO();
			
			//verifica si existe la campaña
			if(campanaDAO.getCampanaById(dto.getId_campana())==null){
				throw new CampanaException(Constantes._EX_CAM_ID_NO_EXISTE);
			}
			result = campanaDAO.setDelCampana(dto);
			
		}catch(CampanaDAOException ex){
			logger.debug("Problema:"+ex);
			throw new CampanaException(ex);
		}
		return result;
	}
	
	
}
