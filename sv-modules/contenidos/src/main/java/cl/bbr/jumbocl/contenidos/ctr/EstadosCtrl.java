package cl.bbr.jumbocl.contenidos.ctr;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.model.EstadoEntity;
import cl.bbr.jumbocl.contenidos.dao.DAOFactory;
import cl.bbr.jumbocl.contenidos.dao.JdbcEstadosDAO;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.contenidos.exceptions.EstadosDAOException;
import cl.bbr.jumbocl.contenidos.exceptions.EstadosException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Controlador de procesos sobre los estados, segun tipo e indicador de visibilidad.
 * @author BBR
 *
 */
public class EstadosCtrl {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Se obtiene la lista de estados
	 * 
	 * @param  tipo String 
	 * @param  visible String 
	 * @return List EstadoDTO 
	 * @throws EstadosException
	 * 
	 * */
	public  List getEstados(String tipo, String visible)throws EstadosException{
		List result= new ArrayList();
		try{
			logger.debug("en getEstados");
			logger.debug("tipo:"+tipo+", visible:"+visible);
			
			JdbcEstadosDAO estadosDAO = (JdbcEstadosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getEstadosDAO();
			
			List listaEst = new ArrayList();
			listaEst = (List) estadosDAO.getEstados(tipo, visible);
			EstadoEntity est = null;
			for (int i = 0; i < listaEst.size(); i++) {
				est = null;
				est = (EstadoEntity) listaEst.get(i); 
				EstadoDTO estDto = new EstadoDTO(est.getId().charValue(), est.getEstado());

				result.add(estDto);
			}
		}catch(EstadosDAOException ex){
			logger.debug("Problema :"+ex);
			throw new EstadosException(ex);
		}
		
		return result;
	}
	
	/**
	 * Se obtiene la lista de estados, segun el tipo de estado
	 * 
	 * @param  tipo String 
	 * @return List EstadoDTO 
	 * @throws EstadosException
	 * 
	 * */
	public  List getEstadosAll(String tipo)throws EstadosException{
		List result= new ArrayList();
		try{
			logger.debug("en getEstados");
			JdbcEstadosDAO estadosDAO = (JdbcEstadosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getEstadosDAO();
			
			List listaEst = new ArrayList();
			listaEst = (List) estadosDAO.getEstados(tipo,"");
			EstadoEntity est = null;
			for (int i = 0; i < listaEst.size(); i++) {
				est = null;
				est = (EstadoEntity) listaEst.get(i); 
				EstadoDTO estDto = new EstadoDTO(est.getId().charValue(), est.getEstado());

				result.add(estDto);
			}
		}catch(EstadosDAOException ex){
			logger.debug("Problema :"+ex);
			throw new EstadosException(ex);
		}
		
		return result;
	}
	
}
