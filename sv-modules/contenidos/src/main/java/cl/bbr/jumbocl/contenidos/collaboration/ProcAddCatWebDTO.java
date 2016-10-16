package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;


/**
 * Clase que maneja información de una nueva categoría Web.
 * @author BBR
 *
 */
public class ProcAddCatWebDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
    /**
     * Nombre de la categoría 
     */
    private String nombre;
    
    /**
     * Descripción de la categoría  
     */
    private String descripcion; 
    
    /**
     * Estado de la categoría
     */
    private String estado;
    
    /**
     * Tipo de categoría 
     */
    private String tipo; 
    
    /**
     * Orden de la categoría.  
     */
    private long orden;
    
    /**
     * Fecha de creación de la categoría 
     */
    private String fec_crea;
    
    /**
     * Ruta banner de la categoría 
     */
    private String ruta_banner;
    
    /**
     * Totem de categoria 
     */
    private String totem;

    /**
     * Imagen de categoria 
     */
    private String imagen;
    
    /**
     * Urlo asociada a la imagen de categoria intermedia o terminal
     */
    private String urlBanner;

    
	/**
	 * Constructor inicial. 
	 */
	public ProcAddCatWebDTO() {
	}
	
	/**
	 * @param nombre
	 * @param descripcion
	 * @param estado
	 * @param tipo
	 * @param orden
	 * @param fec_crea
	 * @param ruta_banner
	 */
	public ProcAddCatWebDTO(String nombre, String descripcion, String estado, String tipo, long orden, String fec_crea, String ruta_banner) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estado = estado;
		this.tipo = tipo;
		this.orden = orden;
		this.fec_crea = fec_crea;
		this.ruta_banner = ruta_banner;
	}
	
	/**
	 * @return Retorna el descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	
	/**
	 * @return Retorna el estado.
	 */
	public String getEstado() {
		return estado;
	}
	
	/**
	 * @return Retorna el nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * @return Retorna el orden.
	 */
	public long getOrden() {
		return orden;
	}
	
	/**
	 * @return Retorna el ruta_banner.
	 */
	public String getRuta_banner() {
		return ruta_banner;
	}
	
	/**
	 * @return Retorna el tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	
	/**
	 * @param descripcion , descripcion a modificar.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	/**
	 * @param estado , estado a modificar.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @param nombre , nombre a modificar.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * @param orden , orden a modificar.
	 */
	public void setOrden(long orden) {
		this.orden = orden;
	}
	
	/**
	 * @param ruta_banner , ruta_banner a modificar.
	 */
	public void setRuta_banner(String ruta_banner) {
		this.ruta_banner = ruta_banner;
	}
	
	/**
	 * @param tipo , tipo a modificar.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	} 
	
	/**
	 * @return Retorna fec_crea.
	 */
	public String getFec_crea() {
		return fec_crea;
	}
	
	/**
	 * @param fec_crea , fec_crea a modificar.
	 */
	public void setFec_crea(String fec_crea) {
		this.fec_crea = fec_crea;
	}

	public String getTotem() {
		return totem;
	}

	public void setTotem(String totem) {
		this.totem = totem;
	}

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
	
	public String getUrlBanner() {
		return urlBanner;
	}

	public void setUrlBanner(String urlBanner) {
		this.urlBanner = urlBanner;
	}
}
