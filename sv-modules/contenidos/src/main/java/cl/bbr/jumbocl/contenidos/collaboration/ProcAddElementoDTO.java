package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;


/**
 * Clase que maneja información de un nueva elemento.
 * @author BBR
 *
 */
public class ProcAddElementoDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
    /**
     * Nombre del elemento 
     */
    private String nombre;
    
    /**
     * Descripción del elemento  
     */
    private String descripcion; 
    
    /**
     * Estado del elemento
     */
    private String estado;
    
    /**
     * Tipo de elemento
     */
    private long tipo;
    
    /**
     * Url destino del elemento 
     */
    private String url_dest;
    
    /**
     * Fecha de creación del elemento 
     */
    private String fec_crea;
    
	/**
	 * Constructor inicial. 
	 */
	public ProcAddElementoDTO() {
	}

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * @return Returns the fec_crea.
	 */
	public String getFec_crea() {
		return fec_crea;
	}

	/**
	 * @param fec_crea The fec_crea to set.
	 */
	public void setFec_crea(String fec_crea) {
		this.fec_crea = fec_crea;
	}

	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return Returns the tipo.
	 */
	public long getTipo() {
		return tipo;
	}

	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(long tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return Returns the url_dest.
	 */
	public String getUrl_dest() {
		return url_dest;
	}

	/**
	 * @param url_dest The url_dest to set.
	 */
	public void setUrl_dest(String url_dest) {
		this.url_dest = url_dest;
	}

	
}
