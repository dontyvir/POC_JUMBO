package cl.bbr.jumbocl.contenidos.ctr;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.ElementoEntity;
import cl.bbr.jumbocl.common.model.TipoElementoEntity;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModElementoDTO;
import cl.bbr.jumbocl.contenidos.dao.DAOFactory;
import cl.bbr.jumbocl.contenidos.dao.JdbcElementoDAO;
import cl.bbr.jumbocl.contenidos.dto.ElementoDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.TipoElementoDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ElementoDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.ElementoException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Controlador de procesos sobre los elementos web.
 * @author BBR Ingeniería
 *
 */
public class ElementoCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Obtiene el elemento, segun el codigo
	 * 
	 * @param  id_elemento
	 * @return ElementoDTO
	 * @throws ElementoException
	 */
	public ElementoDTO getElementoById(long id_elemento) throws ElementoException{
		ElementoDTO elemdto = null;
		try{
			logger.debug("en getElementoById");
			JdbcElementoDAO elementoDAO = (JdbcElementoDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getElementoDAO();
			
			ElementoEntity ent = elementoDAO.getElementoById(id_elemento);
			elemdto = new ElementoDTO();
			elemdto.setId_elemento(ent.getId_elemento());
			elemdto.setId_tipo_elem(ent.getId_tipo_elem());
			elemdto.setNombre(ent.getNombre());
			elemdto.setDescripcion(ent.getDescripcion());
			elemdto.setEstado(ent.getEstado());
			if(ent.getFec_creacion()!=null){
				elemdto.setFec_creacion(ent.getFec_creacion().toString());
			}else{
				elemdto.setFec_creacion("");
			}
			logger.debug("url:"+ent.getUrl_destino());
			elemdto.setUrl_destino(ent.getUrl_destino());
			
			//obtener el listado de elementos relacionados a la campania
			List lst_camp = elementoDAO.getCampanasByElementoId(id_elemento);
			elemdto.setLst_campanas(lst_camp);
				
		}catch(ElementoDAOException ex){
			logger.debug("Problema:"+ex);
			throw new ElementoException(ex);
		}
		return elemdto;
	}

	/**
	 * Obtiene el Listado de elementos, segun criterios ingresados
	 * 
	 * @param  criterio
	 * @return List ElementoDTO
	 * @throws ElementoException
	 */
	public List getElementosByCriteria(ElementosCriteriaDTO criterio) throws ElementoException{
		List result = new ArrayList();
		try{
			logger.debug("en getElementosByCriteria");
			JdbcElementoDAO elementoDAO = (JdbcElementoDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getElementoDAO();
			
			List lst_campana = (List) elementoDAO.getElementosByCriteria(criterio);
			ElementoEntity ent = null;
			for (int i = 0; i < lst_campana.size(); i++) {
				ent = (ElementoEntity) lst_campana.get(i);
				ElementoDTO elemdto = new ElementoDTO();
				elemdto = new ElementoDTO();
				elemdto.setId_elemento(ent.getId_elemento());
				elemdto.setId_tipo_elem(ent.getId_tipo_elem());
				elemdto.setNombre(ent.getNombre());
				elemdto.setDescripcion(ent.getDescripcion());
				elemdto.setEstado(ent.getEstado());
				if(ent.getFec_creacion()!=null){
					elemdto.setFec_creacion(ent.getFec_creacion().toString());
				}else{
					elemdto.setFec_creacion("");
				}
				result.add(elemdto);
			}
		}catch(ElementoDAOException ex){
			logger.debug("Problema:"+ex);
			throw new ElementoException(ex);
		}
		return result;
	}

	/**
	 * Obtiene la cantidad de elementos, segun criterio ingresado
	 * 
	 * @param  criterio
	 * @return int
	 * @throws ElementoException
	 */
	public int getCountElementosByCriteria(ElementosCriteriaDTO criterio) throws ElementoException{
		int result = 0;
		try{
			logger.debug("en getCountElementosByCriteria");
			JdbcElementoDAO elementoDAO = (JdbcElementoDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getElementoDAO();
			
			result = elementoDAO.getCountElementosByCriteria(criterio);
		}catch(ElementoDAOException ex){
			logger.debug("Problema:"+ex);
			throw new ElementoException(ex);
		}
		return result;
	}

	/**
	 * Actualiza la informacion del elemento
	 * 
	 * @param  dto
	 * @return boolean
	 */
	public boolean setModElemento(ProcModElementoDTO dto) throws ElementoException{
		boolean result = false;
		try{
			logger.debug("en setModElemento");
			JdbcElementoDAO elementoDAO = (JdbcElementoDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getElementoDAO();
			
			//verifica si existe el elemento
			if(elementoDAO.getElementoById(dto.getId_elemento())==null){
				throw new  ElementoException(Constantes._EX_ELM_ID_NO_EXISTE);
			}
			result = elementoDAO.setModElemento(dto);
		}catch(ElementoDAOException ex){
			logger.debug("Problema:"+ex);
			throw new ElementoException(ex);
		}
		return result;
	}

	/**
	 * Obtiene el listado de tipos de elemento
	 * 
	 * @return List TipoElementoDTO
	 * @throws ElementoException
	 */
	public List getLstTipoElementos() throws ElementoException {
		List result = new ArrayList();
		try{
			logger.debug("en getLstTipoElementos");
			JdbcElementoDAO elementoDAO = (JdbcElementoDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getElementoDAO();
			
			List lst_tipos = (List) elementoDAO.getLstTipoElementos();
			TipoElementoEntity ent = null;
			for (int i = 0; i < lst_tipos.size(); i++) {
				ent = (TipoElementoEntity) lst_tipos.get(i);
				TipoElementoDTO elemdto = new TipoElementoDTO();
				elemdto.setId_tipo(ent.getId_tipo());
				elemdto.setNombre(ent.getNombre());
				elemdto.setEstado(ent.getEstado());
				result.add(elemdto);
			}
		}catch(ElementoDAOException ex){
			logger.debug("Problema:"+ex);
			throw new ElementoException(ex);
		}
		return result;
		
	}

	/**
	 * Agregar un nuevo elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ElementoException
	 */
	public long setAddElemento(ProcAddElementoDTO dto) throws ElementoException{
		long result = -1;
		try{
			logger.debug("en setAddElemento");
			JdbcElementoDAO elementoDAO = (JdbcElementoDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getElementoDAO();
			
			result = elementoDAO.setAddElemento(dto);
		}catch(ElementoDAOException ex){
			logger.debug("Problema:"+ex);
			throw new ElementoException(ex);
		}
		return result;
	}

	/**
	 * Eliminar un elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ElementoException 
	 */
	public boolean setDelElemento(ProcDelElementoDTO dto) throws ElementoException {
		boolean result = false;
		try{
			logger.debug("en setDelElemento");
			JdbcElementoDAO elementoDAO = (JdbcElementoDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getElementoDAO();
			
			//verifica si el elemento existe
			if(elementoDAO.getElementoById(dto.getId_elemento())==null){
				throw new ElementoException(Constantes._EX_ELM_ID_NO_EXISTE);
			}
				
			result = elementoDAO.setDelElemento(dto);
		}catch(ElementoDAOException ex){
			logger.debug("Problema:"+ex);
			throw new ElementoException(ex);
		}
		return result;
	}
	
	
}
