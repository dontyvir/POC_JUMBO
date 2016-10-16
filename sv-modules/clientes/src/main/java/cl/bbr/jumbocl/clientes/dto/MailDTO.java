package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * DTO para datos de los mails. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class MailDTO implements Serializable {

	private long id;
	private String nombre;
	
	private long fsm_id;
	private String fsm_idfrm;
	private String fsm_remite;
	private String fsm_destina;
	private String fsm_copia;
	private String fsm_subject;
	private String fsm_data;
	private String fsm_estado;
	private long fsm_stmpsave;
	private long fsm_stmpsend;	
	

	/**
	 * Constructor
	 */
	public MailDTO() {
		
		Calendar cal = new GregorianCalendar();
		
		this.fsm_idfrm = "FO";
		this.fsm_estado = "0";
		this.fsm_stmpsave = cal.getTimeInMillis();
		
	}


	public String getFsm_copia() {
		return fsm_copia;
	}


	public void setFsm_copia(String fsm_copia) {
		this.fsm_copia = fsm_copia;
	}


	public String getFsm_data() {
		return fsm_data;
	}


	public void setFsm_data(String fsm_data) {
		this.fsm_data = fsm_data;
	}


	public String getFsm_destina() {
		return fsm_destina;
	}


	public void setFsm_destina(String fsm_destina) {
		this.fsm_destina = fsm_destina;
	}


	public String getFsm_estado() {
		return fsm_estado;
	}


	public void setFsm_estado(String fsm_estado) {
		this.fsm_estado = fsm_estado;
	}


	public long getFsm_id() {
		return fsm_id;
	}


	public void setFsm_id(long fsm_id) {
		this.fsm_id = fsm_id;
	}


	public String getFsm_idfrm() {
		return fsm_idfrm;
	}


	public void setFsm_idfrm(String fsm_idfrm) {
		this.fsm_idfrm = fsm_idfrm;
	}


	public String getFsm_remite() {
		return fsm_remite;
	}


	public void setFsm_remite(String fsm_remite) {
		this.fsm_remite = fsm_remite;
	}


	public long getFsm_stmpsave() {
		return fsm_stmpsave;
	}


	public void setFsm_stmpsave(long fsm_stmpsave) {
		this.fsm_stmpsave = fsm_stmpsave;
	}


	public long getFsm_stmpsend() {
		return fsm_stmpsend;
	}


	public void setFsm_stmpsend(long fsm_stmpsend) {
		this.fsm_stmpsend = fsm_stmpsend;
	}


	public String getFsm_subject() {
		return fsm_subject;
	}


	public void setFsm_subject(String fsm_subject) {
		this.fsm_subject = fsm_subject;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}