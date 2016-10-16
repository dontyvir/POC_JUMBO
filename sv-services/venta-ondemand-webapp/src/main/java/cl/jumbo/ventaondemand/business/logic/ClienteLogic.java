package cl.jumbo.ventaondemand.business.logic;

import cl.jumbo.ventaondemand.exceptions.OnDemandException;
import cl.jumbo.ventaondemand.web.dto.ClienteDTO;
import cl.jumbo.ventaondemand.web.dto.LogonDTO;

public interface ClienteLogic {

	 /**
     * Recupera los datos del cliente
     * 
     * @param logon Objeto de Cliente 
     * @return Cliente DTO
     * @throws OnDemandException
     * @see ClienteDTO
     */
	public ClienteDTO clienteGetByRut(LogonDTO logon) throws OnDemandException;
	
}
