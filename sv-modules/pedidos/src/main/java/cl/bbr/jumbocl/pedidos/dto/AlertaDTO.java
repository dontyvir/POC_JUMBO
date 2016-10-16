package cl.bbr.jumbocl.pedidos.dto;

public class AlertaDTO {
	private long id_alerta;
	private String nombre;
	private String descripcion;
	private String tipo;
	private int orden;
	private String activo;
	private String nom_tipo;
	/**
	 * @return Returns the activo.
	 */
	public String getActivo() {
		return activo;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Returns the id_alerta.
	 */
	public long getId_alerta() {
		return id_alerta;
	}
	/**
	 * @param id_alerta The id_alerta to set.
	 */
	public void setId_alerta(long id_alerta) {
		this.id_alerta = id_alerta;
	}
	/**
	 * @return Returns the nom_tipo.
	 */
	public String getNom_tipo() {
		return nom_tipo;
	}
	/**
	 * @param nom_tipo The nom_tipo to set.
	 */
	public void setNom_tipo(String nom_tipo) {
		this.nom_tipo = nom_tipo;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Returns the orden.
	 */
	public int getOrden() {
		return orden;
	}
	/**
	 * @param orden The orden to set.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
	/**
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
