package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la base de datos los datos de una zona
 * @author bbr
 *
 */
public class ZonaEntity {
	private Long id_zona;
	private Long id_local;
	private String nombre;
	private String descrip;
	private int tarifa_normal_baja;
	private int tarifa_normal_media;
	private int tarifa_normal_alta;
	private int tarifa_economica;
	private int tarifa_express;
	private int estado_tarifa_economica;
	private int estado_tarifa_express;
	private String nom_local;


	
    /**
     * @return Devuelve descrip.
     */
    public String getDescrip() {
        return descrip;
    }
    /**
     * @param descrip El descrip a establecer.
     */
    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
    /**
     * @return Devuelve id_local.
     */
    public Long getId_local() {
        return id_local;
    }
    /**
     * @param id_local El id_local a establecer.
     */
    public void setId_local(Long id_local) {
        this.id_local = id_local;
    }
    /**
     * @return Devuelve id_zona.
     */
    public Long getId_zona() {
        return id_zona;
    }
    /**
     * @param id_zona El id_zona a establecer.
     */
    public void setId_zona(Long id_zona) {
        this.id_zona = id_zona;
    }
    /**
     * @return Devuelve nom_local.
     */
    public String getNom_local() {
        return nom_local;
    }
    /**
     * @param nom_local El nom_local a establecer.
     */
    public void setNom_local(String nom_local) {
        this.nom_local = nom_local;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * @return Devuelve tarifa_economica.
     */
    public int getTarifa_economica() {
        return tarifa_economica;
    }
    /**
     * @param tarifa_economica El tarifa_economica a establecer.
     */
    public void setTarifa_economica(int tarifa_economica) {
        this.tarifa_economica = tarifa_economica;
    }
    /**
     * @return Devuelve tarifa_express.
     */
    public int getTarifa_express() {
        return tarifa_express;
    }
    /**
     * @param tarifa_express El tarifa_express a establecer.
     */
    public void setTarifa_express(int tarifa_express) {
        this.tarifa_express = tarifa_express;
    }
    /**
     * @return Devuelve estado_tarifa_economica.
     */
    public int getEstado_tarifa_economica() {
        return estado_tarifa_economica;
    }
    /**
     * @param estado_tarifa_economica El estado_tarifa_economica a establecer.
     */
    public void setEstado_tarifa_economica(int estado_tarifa_economica) {
        this.estado_tarifa_economica = estado_tarifa_economica;
    }
    /**
     * @return Devuelve estado_tarifa_express.
     */
    public int getEstado_tarifa_express() {
        return estado_tarifa_express;
    }
    /**
     * @param estado_tarifa_express El estado_tarifa_express a establecer.
     */
    public void setEstado_tarifa_express(int estado_tarifa_express) {
        this.estado_tarifa_express = estado_tarifa_express;
    }
    /**
     * @return Devuelve tarifa_normal_alta.
     */
    public int getTarifa_normal_alta() {
        return tarifa_normal_alta;
    }
    /**
     * @param tarifa_normal_alta El tarifa_normal_alta a establecer.
     */
    public void setTarifa_normal_alta(int tarifa_normal_alta) {
        this.tarifa_normal_alta = tarifa_normal_alta;
    }
    /**
     * @return Devuelve tarifa_normal_baja.
     */
    public int getTarifa_normal_baja() {
        return tarifa_normal_baja;
    }
    /**
     * @param tarifa_normal_baja El tarifa_normal_baja a establecer.
     */
    public void setTarifa_normal_baja(int tarifa_normal_baja) {
        this.tarifa_normal_baja = tarifa_normal_baja;
    }
    /**
     * @return Devuelve tarifa_normal_media.
     */
    public int getTarifa_normal_media() {
        return tarifa_normal_media;
    }
    /**
     * @param tarifa_normal_media El tarifa_normal_media a establecer.
     */
    public void setTarifa_normal_media(int tarifa_normal_media) {
        this.tarifa_normal_media = tarifa_normal_media;
    }
}
