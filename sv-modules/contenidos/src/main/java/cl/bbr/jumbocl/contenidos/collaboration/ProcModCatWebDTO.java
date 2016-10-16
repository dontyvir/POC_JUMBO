package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que actualiza los datos de una categoría web. 
 * @author BBR
 *
 */
public class ProcModCatWebDTO implements Serializable{
    /**
     * Nombre de la categoría. 
     */
    private String nombre; 
      
    /**
     * Descripción. 
     */
    private String descripcion; 
      
    /**
     * Estado. 
     */
    private String estado; 
    
    /**
     * Tipo de categoría. 
     */
    private String tipo; 
      
    /**
     * Fecha de modificación. 
     */
    private String fec_modif;
      
    /**
     * Usuario de modificación. 
     */
    private long usu_modif;
      
    /**
     * Orden en el listado 
     */
    private long orden; 
      
    /**
     * Ruta banner de imágenes 
     */
    private String ruta_banner; 
      
    /**
     * Id de la categoría 
     */
    private long id_categoria; // obligatorio
	
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
	public ProcModCatWebDTO() {
	}

	/**
	 * @param nombre
	 * @param descripcion
	 * @param estado
	 * @param tipo
	 * @param orden
	 * @param fec_modif
	 * @param usu_modif
	 * @param ruta_banner
	 * @param id_categoria
	 */
	public ProcModCatWebDTO(String nombre , String descripcion , String estado , String tipo , long orden , String fec_modif , long usu_modif , String ruta_banner , long id_categoria) {
		
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estado = estado;
		this.tipo = tipo;
		this.orden = orden;
		this.fec_modif = fec_modif;
		this.usu_modif = usu_modif;
		this.ruta_banner = ruta_banner;
		this.id_categoria = id_categoria;
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
	 * @return Retorna el id_categoria.
	 */
	public long getId_categoria() {
		return id_categoria;
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
	 * @param id_categoria , id_categoria a modificar.
	 */
	public void setId_categoria(long id_categoria) {
		this.id_categoria = id_categoria;
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
	 * @return Retorna el fec_modif.
	 */
	public String getFec_modif() {
		return fec_modif;
	}
	/**
	 * @param fec_modif , fec_modif a modificar.
	 */
	public void setFec_modif(String fec_modif) {
		this.fec_modif = fec_modif;
	}
	/**
	 * @return Retorna el usu_modif.
	 */
	public long getUsu_modif() {
		return usu_modif;
	}
	/**
	 * @param usu_modif , usu_modif a modificar.
	 */
	public void setUsu_modif(long usu_modif) {
		this.usu_modif = usu_modif;
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
