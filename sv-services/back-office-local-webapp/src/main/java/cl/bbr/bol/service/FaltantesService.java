/*
 * Creado el 07-nov-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.bol.service;

import java.util.HashMap;

import cl.bbr.bol.dao.DAOFactoryParametros;
import cl.bbr.bol.dao.FaltantesDAO;
import cl.bbr.bol.dto.ParametroConsultaFaltantesDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * @author Sebastian
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
	public class FaltantesService {
		public HashMap getDatosCabecera(int validacion, String fechaConsulta, long jornadaActual, long idLocal) throws ServiceException{
			try {
				FaltantesDAO faltantesDao = (FaltantesDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getFaltantesDAO();
				return faltantesDao.getDatosCabecera(validacion, fechaConsulta, jornadaActual, idLocal);
		     } catch (DAOException e) {
		        e.printStackTrace();
		         throw new ServiceException(e);
		     }
		}
		public HashMap getInformeFaltantes(ParametroConsultaFaltantesDTO parametro) throws ServiceException{
			try {
				FaltantesDAO faltantesDao = (FaltantesDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getFaltantesDAO();
				return faltantesDao.getInformeFaltantes(parametro);
			}catch (DAOException e) {
		        e.printStackTrace();
		         throw new ServiceException(e);
			}
		}
		
}
