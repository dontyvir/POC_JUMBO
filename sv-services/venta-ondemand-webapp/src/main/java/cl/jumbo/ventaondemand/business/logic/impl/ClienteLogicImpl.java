package cl.jumbo.ventaondemand.business.logic.impl;

import org.apache.log4j.Logger;

import cl.jumbo.ventaondemand.business.logic.ClienteLogic;
import cl.jumbo.ventaondemand.business.mapper.ModelMapDTO;
import cl.jumbo.ventaondemand.dao.ClientesDAO;
import cl.jumbo.ventaondemand.utils.Util;
import cl.jumbo.ventaondemand.web.dto.ClienteDTO;
import cl.jumbo.ventaondemand.web.dto.LogonDTO;
import cl.jumbo.ventaondemand.dao.factory.DAOFactory;
import cl.jumbo.ventaondemand.exceptions.DAOException;
import cl.jumbo.ventaondemand.exceptions.OnDemandException;
public class ClienteLogicImpl implements ClienteLogic{

	private static final Logger logger = Logger.getLogger(ClienteLogicImpl.class);
	
	public ClienteDTO clienteGetByRut(LogonDTO logon) throws OnDemandException {
		try {
			ClientesDAO clientesDAO = (ClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			ClienteDTO cliente = ModelMapDTO.mapPersonaDTO(clientesDAO.getClienteByRutFO(logon));
			cliente = validaCliente(cliente, logon); 
			return cliente;
	
		} catch (DAOException ex) {
			logger.error("Problema en Logica de Datos de Cliente(DAO) [ClienteGetByRut], ", ex);			
			throw new OnDemandException("Problema en logica de Datos de Cliente");
		} catch (Exception ex) {
			logger.error("Problema en Logica de Cliente [ClienteGetByRut], ", ex);
			throw new OnDemandException("Problema en Logica de Cliente");
		}
	}
	
	private ClienteDTO validaCliente(ClienteDTO clienteDto, LogonDTO logon) throws Exception{	
		
		if( clienteDto == null ) {			
			logger.info("Cliente NO existe");
			clienteDto.setStatusMensaje("Cliente no existe.");			
		}else{
			logger.info("Cliente Existe!!!");
			
			// Se valida si las claves coinciden
			if( clienteDto.getClave().compareTo(Util.encriptarFO(logon.getPass())) != 0 ) {
				logger.info("Clave Invalida");
				clienteDto.setStatusMensaje("Clave Invalida.");
				
			}else{
				Character c = new Character('C');
				if (clienteDto.getEstado().compareTo(c) == 0) {
					logger.info("Clave Caducada, " + clienteDto.getId());
					clienteDto.setStatusMensaje("Clave Caducada.");					
				}else{					
					logger.info("Cliente Encontrado OK " + logon.getRut()+"-"+logon.getDv());					
					clienteDto.setStatusMensaje("Cliente Encontrado.");
					clienteDto.setStatus("OK");
				}
			}
		}
		return clienteDto;
	}
}
