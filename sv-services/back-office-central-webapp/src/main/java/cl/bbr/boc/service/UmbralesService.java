/*
 * Creado el 05-sep-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */

package cl.bbr.boc.service;

import java.util.List;

import cl.bbr.boc.ctrl.ParametrosCtrl;
import cl.bbr.boc.dto.UmbralDTO;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ElementoException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;

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
		ParametrosCtrl param = new ParametrosCtrl();
		
		try {
			return param.getParametrosByCriteria(criterio); 
		} catch (ElementoException e) {
	
			throw new ServiceException(e.getMessage());
		}
	}
	
	

	public UmbralDTO getPorcenUmbralById(long id_pedido) throws ServiceException, DAOException, BocException {
		ParametrosCtrl param = new ParametrosCtrl();
		
		try {
			return param.getPorcenUmbralById(id_pedido); 
		} catch (ElementoException e) {
	
			throw new ServiceException(e.getMessage());
		}
	}
	
	

   
	public void insertarUmbrales(int id_local,String fecha_modi, String nom_usuario, double u_unidad,double u_monto,String u_activacion) throws ServiceException {		
		ParametrosCtrl param = new ParametrosCtrl();
		try {
			param.insertarUmbrales(id_local,fecha_modi,nom_usuario,u_unidad,u_monto,u_activacion);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

	
	public void updateUmbral(int id_local,String fecha_modi, String nom_usuario, double u_unidad,double u_monto,String u_activacion) throws ServiceException {		
		ParametrosCtrl param = new ParametrosCtrl();
		try {
			param.updateUmbral(id_local,fecha_modi,nom_usuario,u_unidad,u_monto,u_activacion);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

	   public long consultaIdLocal(long id_local) throws Exception {
	   	ParametrosCtrl param = new ParametrosCtrl();
	   	long id_local_return;
		try {
			 id_local_return =param.consultaIdLocal(id_local);
			 ;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return id_local_return;
    }

}

