package cl.bbr.jumbocl.pedidos.dto;

public class SeccionSapDTO {
	private String id_cat;
	private String id_cat_padre;
	private String descrip;
	private int nivel;
	private String tipo;
	private String estado;
	
	
	/**
	 * @param id_cat
	 * @param id_cat_padre
	 * @param descrip
	 * @param nivel
	 * @param tipo
	 * @param estado
	 */
	public SeccionSapDTO(String id_cat, String id_cat_padre, String descrip, int nivel, String tipo, String estado) {
		super();
		this.id_cat = id_cat;
		this.id_cat_padre = id_cat_padre;
		this.descrip = descrip;
		this.nivel = nivel;
		this.tipo = tipo;
		this.estado = estado;
	}

	/**
	 * @param id_cat
	 * @param descrip
	 */
	public SeccionSapDTO(String id_cat, String descrip ,String id_cat_padre) {
		super();
		this.id_cat = id_cat;
		this.descrip = descrip;
		this.id_cat_padre = id_cat_padre;	
	}

	/**
	 * 
	 */
	public SeccionSapDTO() {
		super();
	}

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
	 * @return Returns the nivel.
	 */
	public int getNivel() {
		return nivel;
	}

	/**
	 * @param nivel The nivel to set.
	 */
	public void setNivel(int nivel) {
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
