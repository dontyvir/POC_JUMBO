/*
 * Creado el 05-sep-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */

package cl.cencosud.jumbocl.umbrales.service;

import java.util.List;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ElementoException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.cencosud.jumbocl.umbrales.ctrl.UmbralesCtrl;
import cl.cencosud.jumbocl.umbrales.dto.UmbralDTO;
import cl.cencosud.jumbocl.umbrales.exception.UmbralesException;

/**
 * @author RMI - DNT
 */
public class UmbralesService {

	
	Logging logger = new Logging(this); 
	

   /**
	 * Obtiene el listado de parametros, segun criterios ingresados
	 * 
	 * @param  criterio
	 * @return List ElementoDTO
	 * @throws ServiceException
	 * @throws DAOException
	 */
	public List getParametrosByCriteria(ElementosCriteriaDTO criterio) throws ServiceException, DAOException {
		UmbralesCtrl param = new UmbralesCtrl();
		
		try {
			return param.getParametrosByCriteria(criterio); 
		} catch (ElementoException e) {
	
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	///////
	public UmbralDTO getPorcenUmbralById(long id_pedido) throws ServiceException, DAOException, UmbralesException {
		UmbralesCtrl param = new UmbralesCtrl();
		
		try {
			return param.getPorcenUmbralById(id_pedido); 
		} catch (ElementoException e) {
	
			throw new ServiceException(e.getMessage());
		}
	}
	
	

   
	public void insertarUmbrales(int id_local,String fecha_modi, String nom_usuario, double u_unidad,double u_monto,String u_activacion) throws ServiceException {		
		UmbralesCtrl param = new UmbralesCtrl();
		try {
			param.insertarUmbrales(id_local,fecha_modi,nom_usuario,u_unidad,u_monto,u_activacion);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

	


}

