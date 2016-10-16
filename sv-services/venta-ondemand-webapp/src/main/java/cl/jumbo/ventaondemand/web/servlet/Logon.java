package cl.jumbo.ventaondemand.web.servlet;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import cl.jumbo.ventaondemand.business.factory.Service;
import cl.jumbo.ventaondemand.business.logic.ClienteLogic;
import cl.jumbo.ventaondemand.exceptions.JsonException;
import cl.jumbo.ventaondemand.exceptions.OnDemandException;
//import cl.jumbo.ventaondemand.log.Logging;
import cl.jumbo.ventaondemand.web.command.Command;
import cl.jumbo.ventaondemand.web.dto.ClienteDTO;
import cl.jumbo.ventaondemand.web.dto.LogonDTO;
import cl.jumbo.ventaondemand.web.response.ResponseJson;

public class Logon extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Logon.class);
	 
	protected void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		logger.debug("Entro a validar [Logon]");						
		LogonDTO logon = null;
		
		try{
			response = ResponseJson.getHeader(response, request);
			
			if ("GET".equals(request.getMethod().toUpperCase())) {
				throw new JsonException("Metodo GET invalido");
			}
			
			if ( !validateInputParameters(request) ) {
				logger.error("Faltan parametros minimos [Logon]");				
				throw new JsonException("Faltan parametros");
			}

			logon = new LogonDTO.Builder()
					.rut(request.getParameter("rut").trim())
					.pass(request.getParameter("clave").trim())
					.build();

			logger.info("Intento de conexion, RUT["+logon.getRut()+"]");
			
			ClienteLogic clientLogic = (ClienteLogic)Service.getComponet("cliente");			
			ClienteDTO clienteDto = clientLogic.clienteGetByRut(logon);

			JSONObject objOut = createObjectJson(response, clienteDto);
			//ResponseJson.outPutJson(objOut, response);

		}catch(JsonException ode){
			logger.error("Error, JsonException [Logon], " + ode);
			objOut = ResponseJson.createObjOutResponseJSON( ResponseJson.createObjResponseJSON(ode.getMessage(), "NOK") );
			ResponseJson.outPutJson(objOut, response);
						
		}catch(OnDemandException ode){
			logger.error("Error, OnDemandException [Logon], " + ode);
			objOut = ResponseJson.createObjOutResponseJSON( ResponseJson.createObjResponseJSON(ode.getMessage(), "NOK") );
			ResponseJson.outPutJson(objOut, response);			

		}catch(Exception ex){
			logger.error("Error, Exception [Logon], " + ex);
			objOut = ResponseJson.createObjOutResponseJSON( ResponseJson.createObjResponseJSON(ex.getMessage(), "NOK") );
			ResponseJson.outPutJson(objOut, response);			
		}
	}
	
	private boolean validateInputParameters(HttpServletRequest request)throws OnDemandException{
		if ( request.getParameter("rut") == null || StringUtils.isEmpty(request.getParameter("rut").trim())) {
			throw new OnDemandException("Parametro [rut] es nulo o vacio.");
		}		
		if ( request.getParameter("clave") == null || StringUtils.isEmpty(request.getParameter("clave").trim())) {
			throw new OnDemandException("Parametro [clave] es nulo o vacio.");
		}
		
		ArrayList campos = new ArrayList();
		campos.add("rut");
		campos.add("clave");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (request.getParameter(campo) == null || request.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parametro: " + campo + " en [Logon]");
				return false;
			}
		}
		return true;
	}
	
	public JSONObject createObjectJson(HttpServletResponse response, ClienteDTO clienteDTO)throws Exception{
		try {
			objResponse = ResponseJson.createObjResponseJSON(clienteDTO.getStatusMensaje(), clienteDTO.getStatus());						
			objResponse.put("datosCliente", createObjResumenJSON(clienteDTO));
			objOut = ResponseJson.createObjOutResponseJSON( objResponse );
			ResponseJson.outPutJson(objOut, response);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return objOut;
	}
	
	public JSONObject createObjResumenJSON(ClienteDTO clienteDTO)throws Exception{
		JSONObject objResumen = new JSONObject() ;
		
		if(!"NOK".equals(clienteDTO.getStatus())){		
			objResumen.put("id", String.valueOf(clienteDTO.getId()));
			objResumen.put("rut", String.valueOf(clienteDTO.getRut()));
			objResumen.put("dv", clienteDTO.getDv());
			objResumen.put("nombre", clienteDTO.getNombre());
			objResumen.put("paterno", clienteDTO.getApellido_pat());
			objResumen.put("materno", clienteDTO.getApellido_mat());
			
			objResumen.put("mail", clienteDTO.getEmail());
			objResumen.put("fonoCod1", clienteDTO.getFon_cod_1());
			objResumen.put("fonoNum1", clienteDTO.getFon_num_1());
			objResumen.put("fonoCod2", clienteDTO.getFon_cod_2());
			objResumen.put("fonoNum2", clienteDTO.getFon_num_2());
			objResumen.put("fonoCod3", clienteDTO.getFon_cod_2());
			objResumen.put("fonoNum3", clienteDTO.getFon_num_2());
			objResumen.put("recInfo", String.valueOf(clienteDTO.getRec_info()));
			objResumen.put("fechaCreacion", clienteDTO.getFec_crea());
			objResumen.put("fechaActualizacion", clienteDTO.getFec_act());
			objResumen.put("estado", clienteDTO.getEstado());
			objResumen.put("fechNac", dateFormatYYYMMDD.format(clienteDTO.getFec_nac()));
			objResumen.put("genero", clienteDTO.getGenero());
		}
		return objResumen;
	}
}
