package cl.jumbo.ventaondemand.business.mapper;

import cl.jumbo.ventaondemand.dao.model.FOClientesEntity;
import cl.jumbo.ventaondemand.exceptions.OnDemandException;
import cl.jumbo.ventaondemand.web.dto.ClienteDTO;

public class ModelMapDTO {
	
	/**
	 * Trasforma de DTO a Entity los datos del cliente
	 * 
	 * @param cliente Entidad con datos del cliente
	 * @return DTO con datos del cliente
	 * @throws OnDemandException
	 */
	public static ClienteDTO mapPersonaDTO (FOClientesEntity entity)throws OnDemandException{
		ClienteDTO cliente = null;
		try {
			if (entity != null) {
				cliente = new ClienteDTO();
				cliente.setId(entity.getCli_id());
				cliente.setRut(entity.getCli_rut());
				cliente.setDv(entity.getCli_dv());
				cliente.setNombre(entity.getCli_nombre());
				cliente.setApellido_pat(entity.getCli_apellido_pat());
				cliente.setApellido_mat(entity.getCli_apellido_mat());
				cliente.setClave(entity.getCli_clave());
				cliente.setEmail(entity.getCli_email());
				cliente.setFon_cod_1(entity.getCli_fon_cod_1());
				cliente.setFon_num_1(entity.getCli_fon_num_1());
				cliente.setFon_cod_2(entity.getCli_fon_cod_2());
				cliente.setFon_num_2(entity.getCli_fon_num_2());
				cliente.setFon_cod_3(entity.getCli_fon_num_3());
				cliente.setRec_info(entity.getCli_rec_info());
				cliente.setFec_crea(entity.getCli_fec_crea());
				cliente.setFec_act(entity.getCli_fec_act());
				cliente.setEstado(entity.getCli_estado());
				cliente.setFec_nac(entity.getCli_fec_nac());
				cliente.setGenero(entity.getCli_genero());
				cliente.setPregunta(entity.getCli_pregunta());
				cliente.setRespuesta(entity.getCli_respuesta());
				cliente.setBloqueo(entity.getCli_bloqueo());
				cliente.setCli_mod_dato(entity.getCli_mod_dato());
				cliente.setFec_login(entity.getCli_fec_login());
				cliente.setIntento(entity.getCli_intento());
				cliente.setEmp_id(entity.getCli_emp_id());
				cliente.setTipo(entity.getCli_tipo());
				cliente.setEnvio_email(entity.getCli_envio_email());
				cliente.setCreate_at(entity.getCli_create_at());
				cliente.setUpdate_at(entity.getCli_update_at());
				cliente.setEnvio_sms(entity.getCli_envio_sms());
				cliente.setRecibe_encuesta(entity.getCli_recibe_encuesta());
				cliente.setContador_encuesta(entity.getCli_contador_encuesta());
				cliente.setKey_recupera_clave(entity.getCli_key_recupera_clave());
				cliente.setId_facebook(entity.getCli_id_facebook());
				cliente.setColaborador(entity.isColaborador());								
			}	
			return cliente;
		} catch (Exception ex) {			
			throw new OnDemandException("Problema Transformacion Cliente");
		}
	}
}
