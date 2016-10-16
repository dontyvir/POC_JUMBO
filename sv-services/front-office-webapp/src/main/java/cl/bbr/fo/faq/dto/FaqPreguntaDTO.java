package cl.bbr.fo.faq.dto;

import java.io.Serializable;

/**
 * DTO para datos de las preguntas y respuestas. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class FaqPreguntaDTO implements Serializable {

	private long pre_id;
	private long pre_cat_id;
	private String pregunta;
	private String respuesta;
	private String estado;
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public long getPre_cat_id() {
		return pre_cat_id;
	}
	public void setPre_cat_id(long pre_cat_id) {
		this.pre_cat_id = pre_cat_id;
	}
	public long getPre_id() {
		return pre_id;
	}
	public void setPre_id(long pre_id) {
		this.pre_id = pre_id;
	}
	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
	
}