package cl.bbr.jumbocl.productos.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para datos de los productos.
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ProductoDTO implements Serializable, Comparable {

   private long pro_id;

   /**
    * J: para cargar el idBo inmediatamente y no perder el tiempo después
    * cargandolo uno por uno (ver en promociones)
    */
   private int pro_id_bo;

   private String tipo_producto;
   private String descripcion;
   private String descripcionCompleta;
   private double ppum;
   private String ppum_full;
   private double precio;
   private double cantidadEnEmpaque;
   private String unidad_nombre;
   private String unidad_tipo;
   private double inter_valor;
   private double inter_maximo;
   private long stock;
   private String img_chica;
   private String img_grande;
   private String marca = "";
   private long marca_id;
   private boolean con_nota;
   private boolean en_carro;
   private double cantidad;
   private String generico;
   private String valor_diferenciador;
   private String tipre;
   private double pilaPorcion;
   private long idPilaUnidad;

   /**
    * J:Esto valores en la base de datos son numeric(10,3) se deben recuperar
    * como BigDecimal y no como dobles
    */
   private BigDecimal cantidadMinima;
   private BigDecimal cantidadMaxima;

   /**
    * J: para ordenar
    */
   private String[] stem;
   private String categoria;

   /**
    * Items
    */
   private List ProductosDTO = null;
   private String nota;
   private String codigo;

   //Variable para el orden de genericos y otros
   private double precioOrden;
   private String esParticionable;
   private long particion;

   /**
    * Promociones asociadas
    */
   private List ListaPromociones = null;

   private CriterioSustitutoDTO criterio;
   
   /**
    * Indica si el producto tiene stock en el local (segun configuracion en BOC)
    */
   private boolean tienestock;

   

   /**
    * Constructor
    */
   public ProductoDTO() {

   }

   public boolean isCon_nota() {
      return con_nota;
   }

   public void setCon_nota(boolean con_nota) {
      this.con_nota = con_nota;
   }

   public boolean isEn_carro() {
      return en_carro;
   }

   public void setEn_carro(boolean en_carro) {
      this.en_carro = en_carro;
   }

   public boolean tieneStock() {
       return tienestock;
    }

    public void setTieneStock(boolean tienestock) {
       this.tienestock = tienestock;
    }
    
   public String getImg_chica() {
      return img_chica;
   }

   public void setImg_chica(String img_chica) {
      this.img_chica = img_chica;
   }

   public String getImg_grande() {
      return img_grande;
   }

   public void setImg_grande(String img_grande) {
      this.img_grande = img_grande;
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

   public long getPro_id() {
      return pro_id;
   }

   public void setPro_id(long pro_id) {
      this.pro_id = pro_id;
   }

   public List getProductosDTO() {
      return ProductosDTO;
   }

   public void setProductosDTO(List productosDTO) {
      ProductosDTO = productosDTO;
   }

   public long getStock() {
      return stock;
   }

   public void setStock(long stock) {
      this.stock = stock;
   }

   public String getUnidad_nombre() {
      return unidad_nombre;
   }

   public void setUnidad_nombre(String unidad_nombre) {
      this.unidad_nombre = unidad_nombre;
   }

   public String getUnidad_tipo() {
      return unidad_tipo;
   }

   public void setUnidad_tipo(String unidad_tipo) {
      this.unidad_tipo = unidad_tipo;
   }

   public String getTipo_producto() {
      return tipo_producto;
   }

   public void setTipo_producto(String tipo_producto) {
      this.tipo_producto = tipo_producto;
   }

   public String getMarca() {
      return marca;
   }

   public void setMarca(String marca) {
      this.marca = marca;
   }

   public String getDescripcion() {
      return descripcion;
   }

   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }

   public double getPrecio() {
      return precio;
   }

   public void setPrecio(double precio) {
      this.precio = precio;
   }

   public double getCantidad() {
      return cantidad;
   }

   public void setCantidad(double cantidad) {
      this.cantidad = cantidad;
   }

   public String getGenerico() {
      return generico;
   }

   public void setGenerico(String generico) {
      this.generico = generico;
   }

   public String getValor_diferenciador() {
      return valor_diferenciador;
   }

   public void setValor_diferenciador(String valor_diferenciador) {
      this.valor_diferenciador = valor_diferenciador;
   }

   public double getPpum() {
      if(ppum > 0)
         return ppum;
      if(cantidadEnEmpaque != 0)
         return precio / cantidadEnEmpaque;
      return 0;
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

   public String getTipre() {
      return tipre;
   }

   public void setTipre(String tipre) {
      this.tipre = tipre;
   }

   public String getNota() {
      return nota;
   }

   public void setNota(String nota) {
      this.nota = nota;
   }

   public long getMarca_id() {
      return marca_id;
   }

   public void setMarca_id(long marca_id) {
      this.marca_id = marca_id;
   }

   public String getCodigo() {
      return codigo;
   }

   public void setCodigo(String codigo) {
      this.codigo = codigo;
   }

   /**
    * @return Devuelve precioOrden.
    */
   public double getPrecioOrden() {
      return precioOrden;
   }

   /**
    * @param precioOrden
    *           El precioOrden a establecer.
    */
   public void setPrecioOrden(double precioOrden) {
      this.precioOrden = precioOrden;
   }

   public int compareTo(Object o) {
      ProductoDTO pp = (ProductoDTO) o;
      if (this.getPrecioOrden() < pp.getPrecioOrden()) {
         return -1;
      } else if (this.getPrecioOrden() == pp.getPrecioOrden()) {
         return 0;
      } else {
         return 1;
      }
   }

   /**
    * @return Devuelve esParticionable.
    */
   public String getEsParticionable() {
      return esParticionable;
   }

   /**
    * @return Devuelve particion.
    */
   public long getParticion() {
      return particion;
   }

   /**
    * @param esParticionable
    *           El esParticionable a establecer.
    */
   public void setEsParticionable(String esParticionable) {
      this.esParticionable = esParticionable;
   }

   /**
    * @param particion
    *           El particion a establecer.
    */
   public void setParticion(long particion) {
      this.particion = particion;
   }

   /**
    * @return Devuelve listaPromociones.
    */
   public List getListaPromociones() {
      return ListaPromociones;
   }

   /**
    * @param listaPromociones
    *           El listaPromociones a establecer.
    */
   public void setListaPromociones(List listaPromociones) {
      ListaPromociones = listaPromociones;
   }

   /**
    * @return Returns the cantidadMaxima.
    */
   public BigDecimal getCantidadMaxima() {
      return cantidadMaxima;
   }

   /**
    * @param cantidadMaxima
    *           The cantidadMaxima to set.
    */
   public void setCantidadMaxima(BigDecimal cantidadMaxima) {
      this.cantidadMaxima = cantidadMaxima;
   }

   /**
    * @return Returns the cantidadMinima.
    */
   public BigDecimal getCantidadMinima() {
      return cantidadMinima;
   }

   /**
    * @param cantidadMinima
    *           The cantidadMinima to set.
    */
   public void setCantidadMinima(BigDecimal cantidadMinima) {
      this.cantidadMinima = cantidadMinima;
   }

   /**
    * @return Devuelve criterio.
    */
   public CriterioSustitutoDTO getCriterio() {
      return criterio;
   }

   /**
    * @param criterio
    *           El criterio a establecer.
    */
   public void setCriterio(CriterioSustitutoDTO criterio) {
      this.criterio = criterio;
   }

   /**
    * @return Returns the pro_id_bo.
    */
   public int getPro_id_bo() {
      return pro_id_bo;
   }

   /**
    * @param pro_id_bo
    *           The pro_id_bo to set.
    */
   public void setPro_id_bo(int pro_id_bo) {
      this.pro_id_bo = pro_id_bo;
   }

   public String[] getStem() {
      return stem;
   }

   /**
    * El string debe haber pasado por SpanishAnalizer 
    * @param stem
    */
   public void setStem(String[] stem) {
      this.stem = stem;
   }
   public String getCategoria() {
      return categoria;
   }
   public void setCategoria(String categoria) {
      this.categoria = categoria;
   }
   public String getDescripcionCompleta() {
      return descripcionCompleta;
   }
   public void setDescripcionCompleta(String descripcionCompleta) {
      this.descripcionCompleta = descripcionCompleta;
   }
   public double getCantidadEnEmpaque() {
      return cantidadEnEmpaque;
   }
   public void setCantidadEnEmpaque(double cantidadEnEmpaque) {
      this.cantidadEnEmpaque = cantidadEnEmpaque;
   }
    /**
     * @return Devuelve idPilaUnidad.
     */
    public long getIdPilaUnidad() {
        return idPilaUnidad;
    }
    /**
     * @return Devuelve pilaPorcion.
     */
    public double getPilaPorcion() {
        return pilaPorcion;
    }
    /**
     * @param idPilaUnidad El idPilaUnidad a establecer.
     */
    public void setIdPilaUnidad(long idPilaUnidad) {
        this.idPilaUnidad = idPilaUnidad;
    }
    /**
     * @param pilaPorcion El pilaPorcion a establecer.
     */
    public void setPilaPorcion(double pilaPorcion) {
        this.pilaPorcion = pilaPorcion;
    }
}