/*
 * Creado el 07-nov-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.bol.dao;

import java.util.HashMap;

import cl.bbr.bol.dto.ParametroConsultaFaltantesDTO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * @author Sebastian
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public interface FaltantesDAO {
	
	public HashMap getDatosCabecera(int validacion, String fechaConsulta, long jornadaActual, long idLocal) throws DAOException;
	public HashMap getInformeFaltantes(ParametroConsultaFaltantesDTO parametro) throws DAOException;

}
