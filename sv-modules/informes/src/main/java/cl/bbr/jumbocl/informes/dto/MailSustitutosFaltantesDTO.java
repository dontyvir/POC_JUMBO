package cl.bbr.jumbocl.informes.dto;

import java.io.Serializable;

/**
 * DTO para datos de los productos del carro de compras y sus categorías. 
 * 
 * @author imoyano
 *
 */
public class MailSustitutosFaltantesDTO implements Serializable {

	private long id;
	private String nombre;
	private String apellido;
	private String mail;
	private String activado;
	private String fechaIngreso;
	private String fechaModificacion;
	
		
    /**
     * @return Devuelve activado.
     */
    public String getActivado() {
        return activado;
    }
    /**
     * @return Devuelve apellido.
     */
    public String getApellido() {
        return apellido;
    }
    /**
     * @return Devuelve fechaIngreso.
     */
    public String getFechaIngreso() {
        return fechaIngreso;
    }
    /**
     * @return Devuelve fechaModificacion.
     */
    public String getFechaModificacion() {
        return fechaModificacion;
    }
    /**
     * @return Devuelve id.
     */
    public long getId() {
        return id;
    }
    /**
     * @return Devuelve mail.
     */
    public String getMail() {
        return mail;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param activado El activado a establecer.
     */
    public void setActivado(String activado) {
        this.activado = activado;
    }
    /**
     * @param apellido El apellido a establecer.
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    /**
     * @param fechaIngreso El fechaIngreso a establecer.
     */
    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    /**
     * @param fechaModificacion El fechaModificacion a establecer.
     */
    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    /**
     * @param id El id a establecer.
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * @param mail El mail a establecer.
     */
    public void setMail(String mail) {
        this.mail = mail;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}