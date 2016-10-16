package cl.bbr.jumbocl.pedidos.dto;

import java.math.BigDecimal;

/**
 * @author jdroguett
 */
public class WebpayDTO {
   private int id;
   private int TBK_ORDEN_COMPRA;
   private String TBK_TIPO_TRANSACCION;
   private int TBK_RESPUESTA;
   private int TBK_MONTO;
   private String TBK_CODIGO_AUTORIZACION;
   private String TBK_FINAL_NUMERO_TARJETA;
   private String TBK_FECHA_CONTABLE;
   private String TBK_FECHA_TRANSACCION;
   private String TBK_HORA_TRANSACCION;
   private String TBK_ID_SESION;
   private BigDecimal TBK_ID_TRANSACCION;
   private String TBK_TIPO_PAGO;
   private int TBK_NUMERO_CUOTAS;
   private int TBK_TASA_INTERES_MAX;
   private String TBK_VCI;
   private String TBK_MAC;
   private int idPedido;
   
   public WebpayDTO() {
       this.TBK_RESPUESTA = -1;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getTBK_CODIGO_AUTORIZACION() {
      return TBK_CODIGO_AUTORIZACION;
   }

   public void setTBK_CODIGO_AUTORIZACION(String tbk_codigo_autorizacion) {
      TBK_CODIGO_AUTORIZACION = tbk_codigo_autorizacion;
   }

   public String getTBK_FECHA_CONTABLE() {
      return TBK_FECHA_CONTABLE;
   }

   public void setTBK_FECHA_CONTABLE(String tbk_fecha_contable) {
      TBK_FECHA_CONTABLE = tbk_fecha_contable;
   }

   public String getTBK_FECHA_TRANSACCION() {
      return TBK_FECHA_TRANSACCION;
   }

   public void setTBK_FECHA_TRANSACCION(String tbk_fecha_transaccion) {
      TBK_FECHA_TRANSACCION = tbk_fecha_transaccion;
   }

   public String getTBK_FINAL_NUMERO_TARJETA() {
      return TBK_FINAL_NUMERO_TARJETA;
   }

   public void setTBK_FINAL_NUMERO_TARJETA(String tbk_final_numero_tarjeta) {
      TBK_FINAL_NUMERO_TARJETA = tbk_final_numero_tarjeta;
   }

   public String getTBK_HORA_TRANSACCION() {
      return TBK_HORA_TRANSACCION;
   }

   public void setTBK_HORA_TRANSACCION(String tbk_hora_transaccion) {
      TBK_HORA_TRANSACCION = tbk_hora_transaccion;
   }

   public String getTBK_ID_SESION() {
      return TBK_ID_SESION;
   }

   public void setTBK_ID_SESION(String tbk_id_sesion) {
      TBK_ID_SESION = tbk_id_sesion;
   }

   public BigDecimal getTBK_ID_TRANSACCION() {
      return TBK_ID_TRANSACCION;
   }

   public void setTBK_ID_TRANSACCION(BigDecimal tbk_id_transaccion) {
      TBK_ID_TRANSACCION = tbk_id_transaccion;
   }

   public String getTBK_MAC() {
      return TBK_MAC;
   }

   public void setTBK_MAC(String tbk_mac) {
      TBK_MAC = tbk_mac;
   }

   public int getTBK_MONTO() {
      return TBK_MONTO;
   }

   public void setTBK_MONTO(int tbk_monto) {
      TBK_MONTO = tbk_monto;
   }

   public int getTBK_NUMERO_CUOTAS() {
      return TBK_NUMERO_CUOTAS;
   }

   public void setTBK_NUMERO_CUOTAS(int tbk_numero_cuotas) {
      TBK_NUMERO_CUOTAS = tbk_numero_cuotas;
   }

   public int getTBK_ORDEN_COMPRA() {
      return TBK_ORDEN_COMPRA;
   }

   public void setTBK_ORDEN_COMPRA(int tbk_orden_compra) {
      TBK_ORDEN_COMPRA = tbk_orden_compra;
   }

   public int getTBK_RESPUESTA() {
      return TBK_RESPUESTA;
   }

   public void setTBK_RESPUESTA(int tbk_respuesta) {
      TBK_RESPUESTA = tbk_respuesta;
   }

   public int getTBK_TASA_INTERES_MAX() {
      return TBK_TASA_INTERES_MAX;
   }

   public void setTBK_TASA_INTERES_MAX(int tbk_tasa_interes_max) {
      TBK_TASA_INTERES_MAX = tbk_tasa_interes_max;
   }

   public String getTBK_TIPO_PAGO() {
      return TBK_TIPO_PAGO;
   }

   public void setTBK_TIPO_PAGO(String tbk_tipo_pago) {
      TBK_TIPO_PAGO = tbk_tipo_pago;
   }

   public String getTBK_TIPO_TRANSACCION() {
      return TBK_TIPO_TRANSACCION;
   }

   public void setTBK_TIPO_TRANSACCION(String tbk_tipo_transaccion) {
      TBK_TIPO_TRANSACCION = tbk_tipo_transaccion;
   }

   public String getTBK_VCI() {
      return TBK_VCI;
   }

   public void setTBK_VCI(String tbk_vci) {
      TBK_VCI = tbk_vci;
   }
/**
 * @return Devuelve idPedido.
 */
public int getIdPedido() {
    return idPedido;
}
/**
 * @param idPedido El idPedido a establecer.
 */
public void setIdPedido(int idPedido) {
    this.idPedido = idPedido;
}
}
