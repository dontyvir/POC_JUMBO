package cl.bbr.jumbocl.clientes.dto;

public class ZonaDTO {
	
	private long	id_zona;
	private long	id_local;
	private String	nombre;
	private String	descripcion;
	private int		tarifa_normal_alta;
	private int		tarifa_normal_media;
	private int		tarifa_normal_baja;
	private int     tarifa_economica;
	private int     tarifa_express;
	private int     estado_tarifa_economica;
	private int     estado_tarifa_express;
	private String 	nom_local;

	
	/**
	 * @param id_zona
	 * @param nombre
	 * @param descripcion
	 * @param tarifa_normal_alta
	 * @param tarifa_normal_media
	 * @param tarifa_normal_baja
	 * @param tarifa_economica
	 * @param tarifa_express
	 */
	public ZonaDTO(long id_zona, String nombre, String descripcion, int tarifa_normal_alta, int tarifa_normal_media, int tarifa_normal_baja, int tarifa_economica, int tarifa_express, int estado_tarifa_economica, int estado_tarifa_express) {
		super();
		this.id_zona = id_zona;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.tarifa_normal_alta  = tarifa_normal_alta;
		this.tarifa_normal_media = tarifa_normal_media;
		this.tarifa_normal_baja  = tarifa_normal_baja;
		this.tarifa_economica    = tarifa_economica;
		this.tarifa_express      = tarifa_express;
		this.estado_tarifa_economica= estado_tarifa_economica;
		this.estado_tarifa_express  = estado_tarifa_express;
	}


	public ZonaDTO(){
		
	}
	
	
	
    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @return Devuelve id_local.
     */
    public long getId_local() {
        return id_local;
    }
    /**
     * @param id_local El id_local a establecer.
     */
    public void setId_local(long id_local) {
        this.id_local = id_local;
    }
    /**
     * @return Devuelve id_zona.
     */
    public long getId_zona() {
        return id_zona;
    }
    /**
     * @param id_zona El id_zona a establecer.
     */
    public void setId_zona(long id_zona) {
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
