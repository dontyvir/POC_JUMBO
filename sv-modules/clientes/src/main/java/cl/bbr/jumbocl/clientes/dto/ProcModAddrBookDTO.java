package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

public class ProcModAddrBookDTO implements Serializable{
      private long dir_id; // obligatorio
      private long id_local; // obligatorio
      //private long id_zona;//Se asigna junto al Local
      private int  id_poligono;
      private long id_comuna;
      private long id_tipo_calle;
      private String alias;
      private String nom_calle;
      private String num_calle;
      private String num_depto;
      private String comentarios;
      private String dir_conflictiva;
      private String dir_conflictiva_comentario;
    //  private String id_estado;
	/**
	 * 
	 */
	public ProcModAddrBookDTO() {

	}
	/**
	 * @param dir_id
	 * @param id_local
	 * @param id_comuna
	 * @param id_zona
	 * @param id_tipo_calle
	 * @param alias
	 * @param nom_calle
	 * @param num_calle
	 * @param num_depto
	 * @param comentarios
	 */
	public ProcModAddrBookDTO(long dir_id, long id_comuna, int id_poligono, long id_tipo_calle, 
			String alias, String nom_calle, String num_calle, String num_depto, String comentarios,
			String dir_conflictiva, String dir_conflictiva_comentario) {
		this.dir_id = dir_id;
		//this.id_local = id_local;
		//this.id_zona = id_zona;
		this.id_poligono = id_poligono;
		this.id_comuna = id_comuna;
		this.id_tipo_calle = id_tipo_calle;
		this.alias = alias;
		this.nom_calle = nom_calle;
		this.num_calle = num_calle;
		this.num_depto = num_depto;
		this.comentarios = comentarios;
		this.dir_conflictiva = dir_conflictiva;
		this.dir_conflictiva_comentario = dir_conflictiva_comentario;
	//	this.id_estado = id_estado;
	}
	/**
	 * @return Returns the alias.
	 */
	public String getAlias() {
		return alias;
	}
	/**
	 * @return Returns the comentarios.
	 */
	public String getComentarios() {
		return comentarios;
	}
	/**
	 * @return Returns the dir_id.
	 */
	public long getDir_id() {
		return dir_id;
	}
	/**
	 * @return Returns the id_comuna.
	 */
	public long getId_comuna() {
		return id_comuna;
	}
	/**
	 * @return Returns the id_estado.
	 */
	/*public String getId_estado() {
		return id_estado;
	}*/
	/**
	 * @return Returns the id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @return Returns the id_tipo_calle.
	 */
	public long getId_tipo_calle() {
		return id_tipo_calle;
	}
	/**
	 * @return Returns the id_zona.
	 */
	/*public long getId_zona() {
		return id_zona;
	}*/
	/**
	 * @return Returns the nom_calle.
	 */
	public String getNom_calle() {
		return nom_calle;
	}
	/**
	 * @return Returns the num_calle.
	 */
	public String getNum_calle() {
		return num_calle;
	}
	/**
	 * @return Returns the num_depto.
	 */
	public String getNum_depto() {
		return num_depto;
	}
	/**
	 * @param alias The alias to set.
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	/**
	 * @param comentarios The comentarios to set.
	 */
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	/**
	 * @param dir_id The dir_id to set.
	 */
	public void setDir_id(long dir_id) {
		this.dir_id = dir_id;
	}
	/**
	 * @param id_comuna The id_comuna to set.
	 */
	public void setId_comuna(long id_comuna) {
		this.id_comuna = id_comuna;
	}
	/**
	 * @param id_estado The id_estado to set.
	 */
/*	public void setId_estado(String id_estado) {
		this.id_estado = id_estado;
	}*/
	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @param id_tipo_calle The id_tipo_calle to set.
	 */
	public void setId_tipo_calle(long id_tipo_calle) {
		this.id_tipo_calle = id_tipo_calle;
	}
	/**
	 * @param id_zona The id_zona to set.
	 */
	/*public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}*/
	/**
	 * @param nom_calle The nom_calle to set.
	 */
	public void setNom_calle(String nom_calle) {
		this.nom_calle = nom_calle;
	}
	/**
	 * @param num_calle The num_calle to set.
	 */
	public void setNum_calle(String num_calle) {
		this.num_calle = num_calle;
	}
	/**
	 * @param num_depto The num_depto to set.
	 */
	public void setNum_depto(String num_depto) {
		this.num_depto = num_depto;
	}
	
      
      
          
    /**
     * @return Devuelve dir_conflictiva.
     */
    public String getDir_conflictiva() {
        return dir_conflictiva;
    }
    /**
     * @return Devuelve dir_conflictiva_comentario.
     */
    public String getDir_conflictiva_comentario() {
        return dir_conflictiva_comentario;
    }
    /**
     * @param dir_conflictiva El dir_conflictiva a establecer.
     */
    public void setDir_conflictiva(String dir_conflictiva) {
        this.dir_conflictiva = dir_conflictiva;
    }
    /**
     * @param dir_conflictiva_comentario El dir_conflictiva_comentario a establecer.
     */
    public void setDir_conflictiva_comentario(String dir_conflictiva_comentario) {
        this.dir_conflictiva_comentario = dir_conflictiva_comentario;
    }
    /**
     * @return Devuelve id_poligono.
     */
    public int getId_poligono() {
        return id_poligono;
    }
    /**
     * @param id_poligono El id_poligono a establecer.
     */
    public void setId_poligono(int id_poligono) {
        this.id_poligono = id_poligono;
    }
}
