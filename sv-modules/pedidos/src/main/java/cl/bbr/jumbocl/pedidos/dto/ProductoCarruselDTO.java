package cl.bbr.jumbocl.pedidos.dto;

/**
 * @author imoyano
 *
 */
public class ProductoCarruselDTO {

    private long idProductoCarrusel;
    private long idProductoFo;
    private String nombre;
    private String descripcion;
    private String descPrecio;
    private String imagen;
    private String fcInicio;
    private String fcTermino;
    private String fcCreacion;
    private String conCriterio;
    private String idCodigoSAP;
    private String tipoProducto;
    private String estado;
    private String linkDestino;
    private String descripcionProducto;
    private String precioProducto;
    private String imagenProducto;
    private String marcaProducto;
    private String tipre;
    private String esParticionable;
    private long particion;

    public ProductoCarruselDTO() {
    }
    /**
     * @return Devuelve conCriterio.
     */
    public String getConCriterio() {
        return conCriterio;
    }
    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @return Devuelve fcCreacion.
     */
    public String getFcCreacion() {
        return fcCreacion;
    }
    /**
     * @return Devuelve fcInicio.
     */
    public String getFcInicio() {
        return fcInicio;
    }
    /**
     * @return Devuelve fcTermino.
     */
    public String getFcTermino() {
        return fcTermino;
    }
    /**
     * @return Devuelve idProductoCarrusel.
     */
    public long getIdProductoCarrusel() {
        return idProductoCarrusel;
    }
    /**
     * @return Devuelve idProductoFo.
     */
    public long getIdProductoFo() {
        return idProductoFo;
    }
    /**
     * @return Devuelve imagen.
     */
    public String getImagen() {
        return imagen;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @return Devuelve precio.
     */
    public String getDescPrecio() {
        return descPrecio;
    }
    /**
     * @param conCriterio El conCriterio a establecer.
     */
    public void setConCriterio(String conCriterio) {
        this.conCriterio = conCriterio;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @param fcCreacion El fcCreacion a establecer.
     */
    public void setFcCreacion(String fcCreacion) {
        this.fcCreacion = fcCreacion;
    }
    /**
     * @param fcInicio El fcInicio a establecer.
     */
    public void setFcInicio(String fcInicio) {
        this.fcInicio = fcInicio;
    }
    /**
     * @param fcTermino El fcTermino a establecer.
     */
    public void setFcTermino(String fcTermino) {
        this.fcTermino = fcTermino;
    }
    /**
     * @param idProductoCarrusel El idProductoCarrusel a establecer.
     */
    public void setIdProductoCarrusel(long idProductoCarrusel) {
        this.idProductoCarrusel = idProductoCarrusel;
    }
    /**
     * @param idProductoFo El idProductoFo a establecer.
     */
    public void setIdProductoFo(long idProductoFo) {
        this.idProductoFo = idProductoFo;
    }
    /**
     * @param imagen El imagen a establecer.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * @param precio El precio a establecer.
     */
    public void setDescPrecio(String descPrecio) {
        this.descPrecio = descPrecio;
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
	 * @return Returns the tipoProducto.
	 */
	public String getTipoProducto() {
		return tipoProducto;
	}
	/**
	 * @param tipoProducto The tipoProducto to set.
	 */
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	/**
	 * @return Returns the linkDestino.
	 */
	public String getLinkDestino() {
		return linkDestino;
	}
	/**
	 * @param linkDestino The linkDestino to set.
	 */
	public void setLinkDestino(String linkDestino) {
		this.linkDestino = linkDestino;
	}

	/**
	 * @return Returns the idCodigoSAP.
	 */
	public String getIdCodigoSAP() {
		return idCodigoSAP;
	}
	/**
	 * @param idCodigoSAP The idCodigoSAP to set.
	 */
	public void setIdCodigoSAP(String idCodigoSAP) {
		this.idCodigoSAP = idCodigoSAP;
	}

	/**
	 * @return Returns the descripcionProducto.
	 */
	public String getDescripcionProducto() {
		return descripcionProducto;
	}
	/**
	 * @param descripcionProducto The descripcionProducto to set.
	 */
	public void setDescripcionProducto(String descripcionProducto) {
		this.descripcionProducto = descripcionProducto;
	}
	/**
	 * @return Returns the precioProducto.
	 */
	public String getPrecioProducto() {
		return precioProducto;
	}
	/**
	 * @param precioProducto The precioProducto to set.
	 */
	public void setPrecioProducto(String precioProducto) {
		this.precioProducto = precioProducto;
	}

	/**
	 * @return Returns the imagenProducto.
	 */
	public String getImagenProducto() {
		return imagenProducto;
	}
	/**
	 * @param imagenProducto The imagenProducto to set.
	 */
	public void setImagenProducto(String imagenProducto) {
		this.imagenProducto = imagenProducto;
	}


	/**
	 * @return Returns the marcaProducto.
	 */
	public String getMarcaProducto() {
		return marcaProducto;
	}
	/**
	 * @param marcaProducto The marcaProducto to set.
	 */
	public void setMarcaProducto(String marcaProducto) {
		this.marcaProducto = marcaProducto;
	}
	/**
	 * @return Returns the particion.
	 */
	public long getParticion() {
		return particion;
	}
	/**
	 * @param particion The particion to set.
	 */
	public void setParticion(long particion) {
		this.particion = particion;
	}
	/**
	 * @return Returns the particionable.
	 */
	public String getEsParticionable() {
		return esParticionable;
	}
	/**
	 * @param particionable The particionable to set.
	 */
	public void setEsParticionable(String esParticionable) {
		this.esParticionable = esParticionable;
	}
	/**
	 * @return Returns the tipoPrecioProducto.
	 */
	public String getTipre() {
		return tipre;
	}
	/**
	 * @param tipoPrecioProducto The tipoPrecioProducto to set.
	 */
	public void setTipre(String tipre) {
		this.tipre = tipre;
	}
//-20120322coh
}
