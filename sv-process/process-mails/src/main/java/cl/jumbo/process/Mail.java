package cl.jumbo.process;

import java.sql.Clob;

public class Mail {
	
	private int idMail;
	private String destinatario;
	private String remitente;
	private String copiaDestinatario;
	private String subject;
	private Clob data;
	
	public String getCopiaDestinatario() {
		return copiaDestinatario;
	}
	public void setCopiaDestinatario(String copiaDestinatario) {
		this.copiaDestinatario = copiaDestinatario;
	}
	public Clob getData() {
		return data;
	}
	public void setData(Clob data) {
		this.data = data;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public int getIdMail() {
		return idMail;
	}
	public void setIdMail(int idMail) {
		this.idMail = idMail;
	}
	public String getRemitente() {
		return remitente;
	}
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

}
