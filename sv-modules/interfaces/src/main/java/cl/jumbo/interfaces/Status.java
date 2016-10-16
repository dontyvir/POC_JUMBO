/*
 * Created on Jun 1, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.jumbo.interfaces;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jvillalobos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Status implements Serializable {

	private long code = 0;
	private String description = "";
	private Date date = new Date();

	/**
	 * 
	 */
	public Status() {
	}

	/**
	 * 
	 */
	public Status(long code) {
		this.code = code;
	}

	/**
	 * 
	 */
	public Status(long code, String descripcion) {
		this.code = code;
		this.description = descripcion;
	}

	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
