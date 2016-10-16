package cl.bbr.jumbocl.eventos.dao;

import java.util.List;

import cl.bbr.jumbocl.eventos.dto.EventosCriterioDTO;
import cl.bbr.jumbocl.eventos.exceptions.EventosDAOException;


/**
 * Permite las operaciones en base de datos sobre los Casos.
 * 
 * @author imoyano
 *
 */
public interface EventosDAO { 
    
    /**
	 * Lista casos por criterio
	 * El cirterio esta dado por :CasosCriterioDTO
	 * 
	 * @param  criterio CasosCriterioDTO 
	 * @return List MonitorCasosDTO
	 * @throws CasosDAOException 
	 * 
	 */
	public List getEventosByCriterio(EventosCriterioDTO criterio) throws EventosDAOException;
	
	

}
