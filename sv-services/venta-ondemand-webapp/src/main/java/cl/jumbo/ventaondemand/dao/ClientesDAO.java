package cl.jumbo.ventaondemand.dao;

import cl.jumbo.ventaondemand.dao.model.FOClientesEntity;
import cl.jumbo.ventaondemand.exceptions.DAOException;
import cl.jumbo.ventaondemand.web.dto.LogonDTO;

public interface ClientesDAO {
	
	/**
	 * Retorna los datos del cliente en una entidad.
	 * 
	 * @param rut del cliente a consultar
	 * @return FOClientesEntity con informacion del cliente
	 * 
	 * @throws ClientesDAOException
	 */
	public FOClientesEntity getClienteByRutFO(LogonDTO logon) throws DAOException;
}
