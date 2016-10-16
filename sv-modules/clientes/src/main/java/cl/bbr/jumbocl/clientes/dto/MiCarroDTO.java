package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO para datos de los productos del carro de compras. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class MiCarroDTO implements Serializable, Comparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4379924642406666754L;
	
	private long id;
	private String pro_id;
	private double cantidad;
	private String nota;
    private boolean tieneNota;
	private String tipo_sel;
	private String nombre;
	private double precio;
	private double ppum;
	private String ppum_full;
	private long stock;
	private String unidad_tipo;
	private String unidad_nombre;
	private double inter_valor;
	private double inter_maximo;
	private String tipre;
	private String lugar_compra;
	private String tipo_producto;
	private String nom_marca;
	private String pesable;
	private long id_bo;
	private String catsap;
	private String codbarra;
    private String codsap;
    private String imagen;
    //INICIO INDRA 22-10-2012
    private long idProDesp;
    //FIN INDRA 22-10-2012
    private long id_terminal;
    private long id_intermedia;
    private long id_cabecera;
    private String nombre_terminal;
    private String nombre_intermedia;
    private String nombre_cabecera;
    private long idCriterio;
    private String descripcion;
    private String sustitutoCliente;
    private String asignoCliente;
    private boolean tienestock;
    private List ListaPromociones = null;
    //INICIO INDRA 22-10-2012
    private String estadoProducto;
    private String estadoPreciosLocales;
    // FIN INDRA 22-10-2012
    
    private int id_rubro;
    private String car_fec_crea; 

	/**
	 * Constructor
	 */
	public MiCarroDTO() {
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = Math.abs(cantidad);
	}

	public String getPro_id() {
		return pro_id;
	}

	public void setPro_id(String pro_id) {
		this.pro_id = pro_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getNombre() {
		return nombre;
	}

	public void setTipoSel(String tipo_sel) {
		this.tipo_sel = tipo_sel;
	}

	public String getTipoSel() {
		return tipo_sel;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public long getStock() {
		return stock;
	}

	public void setStock(long stock) {
		this.stock = stock;
	}

	public String getUnidad_tipo() {
		return unidad_tipo;
	}

	public void setUnidad_tipo(String unidad_tipo) {
		this.unidad_tipo = unidad_tipo;
	}

	public double getInter_maximo() {
		return inter_maximo;
	}

	public void setInter_maximo(double inter_maximo) {
		this.inter_maximo = inter_maximo;
	}

	public double getInter_valor() {
		return inter_valor;
	}

	public void setInter_valor(double inter_valor) {
		this.inter_valor = inter_valor;
	}

	public String getUnidad_nombre() {
		return unidad_nombre;
	}

	public void setUnidad_nombre(String unidad_nombre) {
		this.unidad_nombre = unidad_nombre;
	}

	public double getPpum() {
		return ppum;
	}

	public void setPpum(double ppum) {
		this.ppum = ppum;
	}

	public String getPpum_full() {
		return ppum_full;
	}

	public void setPpum_full(String ppum_full) {
		this.ppum_full = ppum_full;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getTipre() {
		return tipre;
	}

	public void setTipre(String tipre) {
		this.tipre = tipre;
	}

	public String getLugar_compra() {
		return lugar_compra;
	}

	public void setLugar_compra(String lugar_compra) {
		this.lugar_compra = lugar_compra;
	}

	public String getNom_marca() {
		return nom_marca;
	}

	public void setNom_marca(String nom_marca) {
		this.nom_marca = nom_marca;
	}

	public String getTipo_producto() {
		return tipo_producto;
	}

	public void setTipo_producto(String tipo_producto) {
		this.tipo_producto = tipo_producto;
	}

	
	/**
	 * @return Devuelve id_bo.
	 */
	public long getId_bo() {
		return id_bo;
	}
	/**
	 * @param id_bo El id_bo a establecer.
	 */
	public void setId_bo(long id_bo) {
		this.id_bo = id_bo;
	}
	/**
	 * @return Devuelve pesable.
	 */
	public String getPesable() {
		return pesable;
	}
	/**
	 * @param pesable El pesable a establecer.
	 */
	public void setPesable(String pesable) {
		this.pesable = pesable;
	}
	/**
	 * @return Devuelve catsap.
	 */
	public String getCatsap() {
		return catsap;
	}
	/**
	 * @param catsap El catsap a establecer.
	 */
	public void setCatsap(String catsap) {
		this.catsap = catsap;
	}
	/**
	 * @return Devuelve codbarra.
	 */
	public String getCodbarra() {
		return codbarra;
	}
	/**
	 * @param codbarra El codbarra a establecer.
	 */
	public void setCodbarra(String codbarra) {
		this.codbarra = codbarra;
	}

    public String getCodSap() {
        return codsap;
    }
    public void setCodSap(String codsap) {
        this.codsap = codsap;
    }
    
    /**
     * @return Devuelve tieneNota.
     */
    public boolean isTieneNota() {
        return tieneNota;
    }
    /**
     * @param tieneNota El tieneNota a establecer.
     */
    public void setTieneNota(boolean tieneNota) {
        this.tieneNota = tieneNota;
    }

    /**
     * @return Devuelve imagen.
     */
    public String getImagen() {
        return imagen;
    }
    /**
     * @param imagen El imagen a establecer.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    public long getIdTerminal() {
        return id_terminal;
    }
    
    public void setIdTerminal(long id_terminal){
        this.id_terminal = id_terminal;
    }
    
    public long getIdIntermedia() {
        return id_intermedia;
    }
    
    public void setIdIntermedia(long id_intermedia){
        this.id_intermedia = id_intermedia;
    }
    
    public String getNombreTerminal() {
        return nombre_terminal;
    }
    
    public void setNombreTerminal(String nombre_terminal){
        this.nombre_terminal = nombre_terminal;
    }
    
    public String getNombreIntermedia() {
        return nombre_intermedia;
    }
    
    public void setNombreIntermedia(String nombre_intermedia){
        this.nombre_intermedia = nombre_intermedia;
    }
    
    public long getIdCriterio() {
        return idCriterio;
    }
    
    public void setIdCriterio(long idCriterio) {
        this.idCriterio = idCriterio;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getSustitutoCliente() {
        return sustitutoCliente;
    }
    
    public void setSustitutoCliente(String sustitutoCliente){
        this.sustitutoCliente = sustitutoCliente;
    }
    
    public String getAsignoCliente() {
        return asignoCliente;
    }
    
    public void setAsignoCliente(String asignoCliente){
        this.asignoCliente = asignoCliente;
    }
    
    public boolean tieneStock() {
        return tienestock;
    }

    public void setTieneStock(boolean tienestock) {
        this.tienestock = tienestock;
    }
    
    public List getListaPromociones() {
        return ListaPromociones;
    }

    public void setListaPromociones(List listaPromociones) {
        ListaPromociones = listaPromociones;
    }
    
    // INICIO INDRA 22-10-2012
	/**
	 * @return Devuelve idProDesp.
	 */
	public long getIdProDesp() {
		return idProDesp;
	}
	/**
	 * @param idProDesp El idProDesp a establecer.
	 */
	public void setIdProDesp(long idProDesp) {
		this.idProDesp = idProDesp;
	}
	
	/**
	 * @return Devuelve estadoPreciosLocales.
	 */
	public String getEstadoPreciosLocales() {
		return estadoPreciosLocales;
	}
	/**
	 * @param estadoPreciosLocales El estadoPreciosLocales a establecer.
	 */
	public void setEstadoPreciosLocales(String estadoPreciosLocales) {
		this.estadoPreciosLocales = estadoPreciosLocales;
	}
	/**
	 * @return Devuelve estadoProducto.
	 */
	public String getEstadoProducto() {
		return estadoProducto;
	}
	/**
	 * @param estadoProducto El estadoProducto a establecer.
	 */
	public void setEstadoProducto(String estadoProducto) {
		this.estadoProducto = estadoProducto;
	}
	//FIN INDRA 22-10-2012

	public int getId_rubro() {
		return id_rubro;
	}

	public void setId_rubro(int id_rubro) {
		this.id_rubro = id_rubro;
	}
	
	public int compareTo(Object obj) {
		MiCarroDTO o = (MiCarroDTO) obj;
		return this.tipo_producto.compareTo(o.tipo_producto);
		 
	}
	
	public boolean equals(Object aThat) {
	     if (this == aThat) return true;
	     if (!(aThat instanceof MiCarroDTO)) return false;

	     MiCarroDTO that = (MiCarroDTO)aThat;
	     return
	       ( this.id_intermedia == that.id_intermedia ) &&
	  	   ( this.id_terminal == that.id_terminal ) &&
	       ( this.nombre_intermedia.equals(that.nombre_intermedia) ) &&
	       ( this.nombre_terminal.equals(that.nombre_terminal) ) &&
	       ( this.id == that.id ) &&
	       ( this.pro_id.equals(that.pro_id ) ) ;
	   }

	/**
	 * @return the id_cabecera
	 */
	public long getId_cabecera() {
		return id_cabecera;
	}

	/**
	 * @param id_cabecera the id_cabecera to set
	 */
	public void setId_cabecera(long id_cabecera) {
		this.id_cabecera = id_cabecera;
	}

	/**
	 * @return the nombre_cabecera
	 */
	public String getNombre_cabecera() {
		return nombre_cabecera;
	}

	/**
	 * @param nombre_cabecera the nombre_cabecera to set
	 */
	public void setNombre_cabecera(String nombre_cabecera) {
		this.nombre_cabecera = nombre_cabecera;
	}

	public String getCar_fec_crea() {
		return car_fec_crea;
	}

	public void setCar_fec_crea(String car_fec_crea) {
		this.car_fec_crea = car_fec_crea;
	}
	
	
	
}