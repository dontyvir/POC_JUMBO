package cl.cencosud.jumbocl.hdp.service;

import java.util.List;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.cencosud.jumbocl.hdp.ctrl.HubDePagosCtrl;
import cl.cencosud.jumbocl.hdp.exceptions.HubDePagosException;

public class FopsService {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	//Logging logger = new Logging(this);	

	/**
	 * Obtiene los estados del pedido
	 * 
	 * @return List EstadosDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.EstadoDTO
	 */
	public List getFops() throws ServiceException{
		
		HubDePagosCtrl oHDP = new HubDePagosCtrl();
		try {
			return oHDP.getFops();
		} catch (HubDePagosException e) {
			throw new ServiceException(e);
		}
	}
	
}
