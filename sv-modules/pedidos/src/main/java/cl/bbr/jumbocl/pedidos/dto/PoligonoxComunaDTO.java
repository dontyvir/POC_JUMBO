package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class PoligonoxComunaDTO implements Serializable {

	private int    id_poligono;
	private int    id_zona;
	private int    id_comuna;
	private int    id_local;
	private int    num_poligono;
	private String desc_poligono;
	private String nom_local;
	private String nom_zona;
	private String nom_comuna;
	
	public PoligonoxComunaDTO(){
		
	}


    /**
     * @return Devuelve desc_poligono.
     */
    public String getDesc_poligono() {
        return desc_poligono;
    }
    /**
     * @return Devuelve id_comuna.
     */
    public int getId_comuna() {
        return id_comuna;
    }
    /**
     * @return Devuelve id_local.
     */
    public int getId_local() {
        return id_local;
    }
    /**
     * @return Devuelve id_poligono.
     */
    public int getId_poligono() {
        return id_poligono;
    }
    /**
     * @return Devuelve id_zona.
     */
    public int getId_zona() {
        return id_zona;
    }
    /**
     * @return Devuelve nom_comuna.
     */
    public String getNom_comuna() {
        return nom_comuna;
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
     * @return Devuelve num_poligono.
     */
    public int getNum_poligono() {
        return num_poligono;
    }
    /**
     * @param desc_poligono El desc_poligono a establecer.
     */
    public void setDesc_poligono(String desc_poligono) {
        this.desc_poligono = desc_poligono;
    }
    /**
     * @param id_comuna El id_comuna a establecer.
     */
    public void setId_comuna(int id_comuna) {
        this.id_comuna = id_comuna;
    }
    /**
     * @param id_local El id_local a establecer.
     */
    public void setId_local(int id_local) {
        this.id_local = id_local;
    }
    /**
     * @param id_poligono El id_poligono a establecer.
     */
    public void setId_poligono(int id_poligono) {
        this.id_poligono = id_poligono;
    }
    /**
     * @param id_zona El id_zona a establecer.
     */
    public void setId_zona(int id_zona) {
        this.id_zona = id_zona;
    }
    /**
     * @param nom_comuna El nom_comuna a establecer.
     */
    public void setNom_comuna(String nom_comuna) {
        this.nom_comuna = nom_comuna;
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
     * @param num_poligono El num_poligono a establecer.
     */
    public void setNum_poligono(int num_poligono) {
        this.num_poligono = num_poligono;
    }
}
