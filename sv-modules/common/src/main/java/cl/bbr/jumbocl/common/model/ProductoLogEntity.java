package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;

/**
 * Clase que captura desde la base de datos los datos del log de un producto
 * @author bbr
 *
 */
public class ProductoLogEntity {
	private Long id;
	private Long cod_prod;
	private Timestamp fec_crea;
	private String usuario;
	private String texto;
	private String nom_usuario;
	/**
	 * @return Returns the cod_prod.
	 */
	public Long getCod_prod() {
		return cod_prod;
	}
	/**
	 * @param cod_prod The cod_prod to set.
	 */
	public void setCod_prod(Long cod_prod) {
		this.cod_prod = cod_prod;
	}
	/**
	 * @return Returns the fec_crea.
	 */
	public Timestamp getFec_crea() {
		return fec_crea;
	}
	/**
	 * @param fec_crea The fec_crea to set.
	 */
	public void setFec_crea(Timestamp fec_crea) {
		this.fec_crea = fec_crea;
	}
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the nom_usuario.
	 */
	public String getNom_usuario() {
		return nom_usuario;
	}
	/**
	 * @param nom_usuario The nom_usuario to set.
	 */
	public void setNom_usuario(String nom_usuario) {
		this.nom_usuario = nom_usuario;
	}
	/**
	 * @return Returns the texto.
	 */
	public String getTexto() {
		return texto;
	}
	/**
	 * @param texto The texto to set.
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
