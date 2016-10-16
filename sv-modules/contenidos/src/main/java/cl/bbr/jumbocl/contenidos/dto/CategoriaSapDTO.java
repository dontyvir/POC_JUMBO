package cl.bbr.jumbocl.contenidos.dto;

/**
 * Clase que muestra informacion de Categoria Sap.
 * 
 * @author BBR
 *
 */
public class CategoriaSapDTO {
	
	/**
	 * Id de categoria Sap 
	 */
	private String id_cat;
	
	/**
	 * Id de categoria Sap del padre
	 */
	private String id_cat_padre;
	
	/**
	 * Id del sector. 
	 */
	private int id_sector;
	
	/**
	 * Descripcion 
	 */
	private String descrip;
	
	/**
	 * Nivel de la categoria Sap 
	 */
	private int nivel;
	
	/**
	 * Tipo de categoria 
	 */
	private String tipo;
	
	/**
	 * Estado 
	 */
	private String estado;
	
	
	/**
	 * @param id_cat
	 * @param id_cat_padre
	 * @param id_sector
	 * @param descrip
	 * @param nivel
	 * @param tipo
	 * @param estado
	 */
	public CategoriaSapDTO(String id_cat , String id_cat_padre , int id_sector , String descrip , int nivel , String tipo , String estado) {
		super();
		this.id_cat = id_cat;
		this.id_cat_padre = id_cat_padre;
		this.id_sector = id_sector;
		this.descrip = descrip;
		this.nivel = nivel;
		this.tipo = tipo;
		this.estado = estado;
	}

	/**
	 * @param id_cat
	 * @param descrip
	 * @param id_cat_padre
	 */
	public CategoriaSapDTO(String id_cat , String descrip ,String id_cat_padre) {
		super();
		this.id_cat = id_cat;
		this.descrip = descrip;
		this.id_cat_padre = id_cat_padre;	
	}

	/**
	 * Constructor inicial
	 */
	public CategoriaSapDTO() {
		super();
	}

	/**
	 * @return Retorna descrip.
	 */
	public String getDescrip() {
		return descrip;
	}

	/**
	 * @param descrip , descrip a modificar.
	 */
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado , estado a modificar.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Retorna id_cat.
	 */
	public String getId_cat() {
		return id_cat;
	}

	/**
	 * @param id_cat , id_cat a modificar.
	 */
	public void setId_cat(String id_cat) {
		this.id_cat = id_cat;
	}

	/**
	 * @return Retorna id_cat_padre.
	 */
	public String getId_cat_padre() {
		return id_cat_padre;
	}

	/**
	 * @param id_cat_padre , id_cat_padre a modificar.
	 */
	public void setId_cat_padre(String id_cat_padre) {
		this.id_cat_padre = id_cat_padre;
	}

	/**
	 * @return Retorna id_sector.
	 */
	public int getId_sector() {
		return id_sector;
	}

	/**
	 * @param id_sector , id_sector a modificar.
	 */
	public void setId_sector(int id_sector) {
		this.id_sector = id_sector;
	}

	/**
	 * @return Retorna nivel.
	 */
	public int getNivel() {
		return nivel;
	}

	/**
	 * @param nivel , nivel a modificar.
	 */
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	/**
	 * @return Retorna tipo.
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo , tipo a modificar.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
