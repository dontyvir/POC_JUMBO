package cl.bbr.jumbocl.pedidos.dto;

public class ZonaDTO {

   private long id_zona;
   private long id_local;
   private String nombre;
   private String descripcion;
   private int tarifa_normal_alta;
   private int tarifa_normal_media;
   private int tarifa_normal_baja;
   private int tarifa_economica;
   private int tarifa_express;
   private int estado_tarifa_economica;
   private int estado_tarifa_express;
   private String mensaje_cal_despacho;
   private String nom_local;
   private int orden;
   private int regalo_clientes;
   private int estado_descuento_cat;
   private int estado_descuento_tbk;
   private int estado_descuento_par;
   private int monto_descuento_cat;
   private int monto_descuento_tbk;
   private int monto_descuento_par;
   private int monto_descuento_pc_cat;
   private int monto_descuento_pc_tbk;
   private int monto_descuento_pc_par;
   private boolean retiroLocal;

   /**
    * @param id_zona
    * @param nombre
    * @param descripcion
    * @param precio_alto
    * @param precio_medio
    * @param precio_bajo
    */
   public ZonaDTO(long id_zona, String nombre, String descripcion, int tarifa_normal_alta, int tarifa_normal_media,
         int tarifa_normal_baja, int tarifa_economica, int tarifa_express, int estado_tarifa_economica,
         int estado_tarifa_express, int regalo_clientes, int estado_descuento_cat, int estado_descuento_tbk,
         int estado_descuento_par) {
      super();
      this.id_zona = id_zona;
      this.nombre = nombre;
      this.descripcion = descripcion;
      this.tarifa_normal_alta = tarifa_normal_alta;
      this.tarifa_normal_media = tarifa_normal_media;
      this.tarifa_normal_baja = tarifa_normal_baja;
      this.tarifa_economica = tarifa_economica;
      this.tarifa_express = tarifa_express;
      this.estado_tarifa_economica = estado_tarifa_economica;
      this.estado_tarifa_express = estado_tarifa_express;
      this.regalo_clientes = regalo_clientes;
      this.estado_descuento_cat = estado_descuento_cat;
      this.estado_descuento_tbk = estado_descuento_tbk;
      this.estado_descuento_par = estado_descuento_par;
   }

   public ZonaDTO() {

   }

   /**
    * @return Devuelve descripcion.
    */
   public String getDescripcion() {
      return descripcion;
   }

   /**
    * @param descripcion
    *           El descripcion a establecer.
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
    * @param id_local
    *           El id_local a establecer.
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
    * @param id_zona
    *           El id_zona a establecer.
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
    * @param nom_local
    *           El nom_local a establecer.
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
    * @param nombre
    *           El nombre a establecer.
    */
   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   /**
    * @return Devuelve orden.
    */
   public int getOrden() {
      return orden;
   }

   /**
    * @param orden
    *           El orden a establecer.
    */
   public void setOrden(int orden) {
      this.orden = orden;
   }

   /**
    * @return Devuelve tarifa_economica.
    */
   public int getTarifa_economica() {
      return tarifa_economica;
   }

   /**
    * @param tarifa_economica
    *           El tarifa_economica a establecer.
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
    * @param tarifa_express
    *           El tarifa_express a establecer.
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
    * @param estado_tarifa_economica
    *           El estado_tarifa_economica a establecer.
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
    * @param estado_tarifa_express
    *           El estado_tarifa_express a establecer.
    */
   public void setEstado_tarifa_express(int estado_tarifa_express) {
      this.estado_tarifa_express = estado_tarifa_express;
   }

   /**
    * @return Devuelve mensaje_cal_despacho.
    */
   public String getMensaje_cal_despacho() {
      return mensaje_cal_despacho;
   }

   /**
    * @param mensaje_cal_despacho
    *           El mensaje_cal_despacho a establecer.
    */
   public void setMensaje_cal_despacho(String mensaje_cal_despacho) {
      this.mensaje_cal_despacho = mensaje_cal_despacho;
   }

   /**
    * @return Devuelve tarifa_normal_alta.
    */
   public int getTarifa_normal_alta() {
      return tarifa_normal_alta;
   }

   /**
    * @param tarifa_normal_alta
    *           El tarifa_normal_alta a establecer.
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
    * @param tarifa_normal_baja
    *           El tarifa_normal_baja a establecer.
    */
   public void setTarifa_normal_baja(int tarifa_normal_baja) {
      this.tarifa_normal_baja = tarifa_normal_baja;
   }

   /**
    * @return Devuelve tarifa_normal_medio.
    */
   public int getTarifa_normal_media() {
      return tarifa_normal_media;
   }

   /**
    * @param tarifa_normal_media
    *           El tarifa_normal_media a establecer.
    */
   public void setTarifa_normal_media(int tarifa_normal_media) {
      this.tarifa_normal_media = tarifa_normal_media;
   }

   /**
    * @return Devuelve regalo_clientes.
    */
   public int getRegalo_clientes() {
      return regalo_clientes;
   }

   /**
    * @param regalo_clientes
    *           El regalo_clientes a establecer.
    */
   public void setRegalo_clientes(int regalo_clientes) {
      this.regalo_clientes = regalo_clientes;
   }

   /**
    * @return Devuelve estado_descuento_cat.
    */
   public int getEstado_descuento_cat() {
      return estado_descuento_cat;
   }

   /**
    * @return Devuelve estado_descuento_par.
    */
   public int getEstado_descuento_par() {
      return estado_descuento_par;
   }

   /**
    * @return Devuelve estado_descuento_tbk.
    */
   public int getEstado_descuento_tbk() {
      return estado_descuento_tbk;
   }

   /**
    * @return Devuelve monto_descuento_cat.
    */
   public int getMonto_descuento_cat() {
      return monto_descuento_cat;
   }

   /**
    * @return Devuelve monto_descuento_par.
    */
   public int getMonto_descuento_par() {
      return monto_descuento_par;
   }

   /**
    * @return Devuelve monto_descuento_tbk.
    */
   public int getMonto_descuento_tbk() {
      return monto_descuento_tbk;
   }

   /**
    * @param estado_descuento_cat
    *           El estado_descuento_cat a establecer.
    */
   public void setEstado_descuento_cat(int estado_descuento_cat) {
      this.estado_descuento_cat = estado_descuento_cat;
   }

   /**
    * @param estado_descuento_par
    *           El estado_descuento_par a establecer.
    */
   public void setEstado_descuento_par(int estado_descuento_par) {
      this.estado_descuento_par = estado_descuento_par;
   }

   /**
    * @param estado_descuento_tbk
    *           El estado_descuento_tbk a establecer.
    */
   public void setEstado_descuento_tbk(int estado_descuento_tbk) {
      this.estado_descuento_tbk = estado_descuento_tbk;
   }

   /**
    * @param monto_descuento_cat
    *           El monto_descuento_cat a establecer.
    */
   public void setMonto_descuento_cat(int monto_descuento_cat) {
      this.monto_descuento_cat = monto_descuento_cat;
   }

   /**
    * @param monto_descuento_par
    *           El monto_descuento_par a establecer.
    */
   public void setMonto_descuento_par(int monto_descuento_par) {
      this.monto_descuento_par = monto_descuento_par;
   }

   /**
    * @param monto_descuento_tbk
    *           El monto_descuento_tbk a establecer.
    */
   public void setMonto_descuento_tbk(int monto_descuento_tbk) {
      this.monto_descuento_tbk = monto_descuento_tbk;
   }

   public int getMonto_descuento_pc_cat() {
      return monto_descuento_pc_cat;
   }

   public void setMonto_descuento_pc_cat(int monto_descuento_pc_cat) {
      this.monto_descuento_pc_cat = monto_descuento_pc_cat;
   }

   public int getMonto_descuento_pc_par() {
      return monto_descuento_pc_par;
   }

   public void setMonto_descuento_pc_par(int monto_descuento_pc_par) {
      this.monto_descuento_pc_par = monto_descuento_pc_par;
   }

   public int getMonto_descuento_pc_tbk() {
      return monto_descuento_pc_tbk;
   }

   public void setMonto_descuento_pc_tbk(int monto_descuento_pc_tbk) {
      this.monto_descuento_pc_tbk = monto_descuento_pc_tbk;
   }
/**
 * @return Devuelve retiroLocal.
 */
public boolean isRetiroLocal() {
    return retiroLocal;
}
/**
 * @param retiroLocal El retiroLocal a establecer.
 */
public void setRetiroLocal(boolean retiroLocal) {
    this.retiroLocal = retiroLocal;
}
}
