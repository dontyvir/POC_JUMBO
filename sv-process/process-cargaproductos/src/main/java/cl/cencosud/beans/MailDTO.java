package cl.cencosud.beans;

import java.io.Serializable;
import java.sql.Clob;
import java.util.List;

public class MailDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int idMail;
	private String destinatario;
	private String remitente;
	private String copiaDestinatario;
	private String subject;
	private String bodyHtml;
	private Clob data;
	private List fileAttach;
	
	public int getIdMail() {
		return idMail;
	}
	public void setIdMail(int idMail) {
		this.idMail = idMail;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getRemitente() {
		return remitente;
	}
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}
	public String getCopiaDestinatario() {
		return copiaDestinatario;
	}
	public void setCopiaDestinatario(String copiaDestinatario) {
		this.copiaDestinatario = copiaDestinatario;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBodyHtml() {
		return bodyHtml;
	}
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}
	public Clob getData() {
		return data;
	}
	public void setData(Clob data) {
		this.data = data;
	}
	public List getFileAttach() {
		return fileAttach;
	}
	public void setFileAttach(List fileAttach) {
		this.fileAttach = fileAttach;
	}	
}
