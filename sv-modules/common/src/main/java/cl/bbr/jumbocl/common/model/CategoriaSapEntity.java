package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la Base de Datos los datos de una Categoria SAP
 * @author bbr
 *
 */
public class CategoriaSapEntity {
	private String id_cat;
	private String id_cat_padre;
	private Integer id_sector;
	private String descrip;
	private Integer nivel;
	private String tipo;
	private String estado;
	/**
	 * @return Returns the descrip.
	 */
	public String getDescrip() {
		return descrip;
	}
	/**
	 * @param descrip The descrip to set.
	 */
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the id_cat.
	 */
	public String getId_cat() {
		return id_cat;
	}
	/**
	 * @param id_cat The id_cat to set.
	 */
	public void setId_cat(String id_cat) {
		this.id_cat = id_cat;
	}
	/**
	 * @return Returns the id_cat_padre.
	 */
	public String getId_cat_padre() {
		return id_cat_padre;
	}
	/**
	 * @param id_cat_padre The id_cat_padre to set.
	 */
	public void setId_cat_padre(String id_cat_padre) {
		this.id_cat_padre = id_cat_padre;
	}
	/**
	 * @return Returns the id_sector.
	 */
	public Integer getId_sector() {
		return id_sector;
	}
	/**
	 * @param id_sector The id_sector to set.
	 */
	public void setId_sector(Integer id_sector) {
		this.id_sector = id_sector;
	}
	/**
	 * @return Returns the nivel.
	 */
	public Integer getNivel() {
		return nivel;
	}
	/**
	 * @param nivel The nivel to set.
	 */
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
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
