package cl.cencosud.jumbo.beans;

/**
 *Clase que captura desde la base de datos los datos de un local
 * @author bbr
 *
 */
public class BeanLocal {
	private int    id_local;
	private String cod_local;
	private String nom_local;

	
	
    /**
     * @return Devuelve cod_local.
     */
    public String getCod_local() {
        return cod_local;
    }
    /**
     * @return Devuelve id_local.
     */
    public int getId_local() {
        return id_local;
    }
    /**
     * @return Devuelve nom_local.
     */
    public String getNom_local() {
        return nom_local;
    }
    /**
     * @param cod_local El cod_local a establecer.
     */
    public void setCod_local(String cod_local) {
        this.cod_local = cod_local;
    }
    /**
     * @param id_local El id_local a establecer.
     */
    public void setId_local(int id_local) {
        this.id_local = id_local;
    }
    /**
     * @param nom_local El nom_local a establecer.
     */
    public void setNom_local(String nom_local) {
        this.nom_local = nom_local;
    }
}
