package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class ZonaxComunaDTO implements Serializable {
    private long    id_local;
	private long 	id_comuna;
	private long 	id_zona;
	private int 	orden;
	private String  nom_local;
	private String 	nom_zona;
	private String  desc_zona;
	
	public ZonaxComunaDTO(){
		
	}

	
    /**
     * @return Devuelve desc_zona.
     */
    public String getDesc_zona() {
        return desc_zona;
    }
    /**
     * @return Devuelve id_comuna.
     */
    public long getId_comuna() {
        return id_comuna;
    }
    /**
     * @return Devuelve id_local.
     */
    public long getId_local() {
        return id_local;
    }
    /**
     * @return Devuelve id_zona.
     */
    public long getId_zona() {
        return id_zona;
    }
    /**
     * @return Devuelve nom_local.
     */
    public String getNom_local() {
        return nom_local;
    }
    /**
     * @return Devuelve nom_zona.
     */
    public String getNom_zona() {
        return nom_zona;
    }
    /**
     * @return Devuelve orden.
     */
    public int getOrden() {
        return orden;
    }
    /**
     * @param desc_zona El desc_zona a establecer.
     */
    public void setDesc_zona(String desc_zona) {
        this.desc_zona = desc_zona;
    }
    /**
     * @param id_comuna El id_comuna a establecer.
     */
    public void setId_comuna(long id_comuna) {
        this.id_comuna = id_comuna;
    }
    /**
     * @param id_local El id_local a establecer.
     */
    public void setId_local(long id_local) {
        this.id_local = id_local;
    }
    /**
     * @param id_zona El id_zona a establecer.
     */
    public void setId_zona(long id_zona) {
        this.id_zona = id_zona;
    }
    /**
     * @param nom_local El nom_local a establecer.
     */
    public void setNom_local(String nom_local) {
        this.nom_local = nom_local;
    }
    /**
     * @param nom_zona El nom_zona a establecer.
     */
    public void setNom_zona(String nom_zona) {
        this.nom_zona = nom_zona;
    }
    /**
     * @param orden El orden a establecer.
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }
}
