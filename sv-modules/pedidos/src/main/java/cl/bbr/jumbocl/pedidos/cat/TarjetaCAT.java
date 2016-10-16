/*
 * Created on 08-jul-2009
 *
 */
package cl.bbr.jumbocl.pedidos.cat;

/**
 * @author jdroguett
 *  
 */
public class TarjetaCAT {
   private int error;
   private String errorMsg;
   private String apellidoPaterno;
   private String apellidoMaterno;
   private String nombre;
   private String direccion;
   private String comuna;
   private int region;
   private String telefono;
   private int fechaModificacion;
   private String direccion2;
   private String ciudad;
   private String status;
   private String status2;
   private String bloqueo;
   private String bloqueo1;
   private String bloqueo2;
   private int cupoCompraPesos;
   private int cupoPesosAutorizador;
   private int disponibleCompraPesos;
   private int disponibleCompraPesosTarj;
   private int fechaBloqueo1;
   private int fechaBloqueo2;

   private String statusTjta;

   private String numeroTarjeta;

   public String toString() {
      return error + "\n" + errorMsg + "\n" + apellidoPaterno + "\n" + apellidoMaterno + "\n" + nombre + "\n"
            + direccion + "\n" + comuna + "\n" + region + "\n" + telefono + "\n" + fechaModificacion + "\n"
            + direccion2 + "\n" + ciudad + "\n" + "status: " + status + "\n" + "status2: " + status2 + "\n"
            + "bloqueo: " + bloqueo + "\n" + "bloqueo1: " + bloqueo1 + "\n" + "bloqueo2: " + bloqueo2 + "\n"
            + "saldoPesos: " + cupoCompraPesos + "\n" + "saldoPesosAutorizado: " + cupoPesosAutorizador + "\n"
            + "disponibleCompraPesos: " + disponibleCompraPesos + "\n" + "disponibleCompraPesosTarj: "
            + disponibleCompraPesosTarj + "\n" + fechaBloqueo1 + "\n" + fechaBloqueo2 + "\n" + "statusTjta: "
            + statusTjta + "numeroTarjeta: " + numeroTarjeta + "\n";
   }

   public String getApellidoMaterno() {
      return apellidoMaterno;
   }

   public void setApellidoMaterno(String apellidoMaterno) {
      this.apellidoMaterno = apellidoMaterno;
   }

   public String getApellidoPaterno() {
      return apellidoPaterno;
   }

   public void setApellidoPaterno(String apellidoPaterno) {
      this.apellidoPaterno = apellidoPaterno;
   }

   public String getBloqueo() {
      return bloqueo + bloqueo1 + bloqueo2;
   }

   public void setBloqueo(String bloqueo) {
      this.bloqueo = bloqueo;
   }

   public String getBloqueo1() {
      return bloqueo1;
   }

   public void setBloqueo1(String bloqueo1) {
      this.bloqueo1 = bloqueo1;
   }

   public String getBloqueo2() {
      return bloqueo2;
   }

   public void setBloqueo2(String bloqueo2) {
      this.bloqueo2 = bloqueo2;
   }

   public String getCiudad() {
      return ciudad;
   }

   public void setCiudad(String ciudad) {
      this.ciudad = ciudad;
   }

   public String getComuna() {
      return comuna;
   }

   public void setComuna(String comuna) {
      this.comuna = comuna;
   }

   public int getCupoCompraPesos() {
      return cupoCompraPesos;
   }

   public void setCupoCompraPesos(int cupoCompraPesos) {
      this.cupoCompraPesos = cupoCompraPesos;
   }

   public int getCupoPesosAutorizador() {
      return cupoPesosAutorizador;
   }

   public void setCupoPesosAutorizador(int cupoPesosAutorizador) {
      this.cupoPesosAutorizador = cupoPesosAutorizador;
   }

   public String getDireccion() {
      return direccion;
   }

   public void setDireccion(String direccion) {
      this.direccion = direccion;
   }

   public String getDireccion2() {
      return direccion2;
   }

   public void setDireccion2(String direccion2) {
      this.direccion2 = direccion2;
   }

   public int getDisponibleCompraPesos() {
      return disponibleCompraPesos;
   }

   public void setDisponibleCompraPesos(int disponibleCompraPesos) {
      this.disponibleCompraPesos = disponibleCompraPesos;
   }

   public int getDisponibleCompraPesosTarj() {
      return disponibleCompraPesosTarj;
   }

   public void setDisponibleCompraPesosTarj(int disponibleCompraPesosTarj) {
      this.disponibleCompraPesosTarj = disponibleCompraPesosTarj;
   }

   public int getError() {
      return error;
   }

   public void setError(int error) {
      this.error = error;
   }

   public String getErrorMsg() {
      return errorMsg;
   }

   public void setErrorMsg(String errorMsg) {
      this.errorMsg = errorMsg;
   }

   public int getFechaBloqueo1() {
      return fechaBloqueo1;
   }

   public void setFechaBloqueo1(int fechaBloqueo1) {
      this.fechaBloqueo1 = fechaBloqueo1;
   }

   public int getFechaBloqueo2() {
      return fechaBloqueo2;
   }

   public void setFechaBloqueo2(int fechaBloqueo2) {
      this.fechaBloqueo2 = fechaBloqueo2;
   }

   public int getFechaModificacion() {
      return fechaModificacion;
   }

   public void setFechaModificacion(int fechaModificacion) {
      this.fechaModificacion = fechaModificacion;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public int getRegion() {
      return region;
   }

   public void setRegion(int region) {
      this.region = region;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getStatus2() {
      return status2;
   }

   public void setStatus2(String status2) {
      this.status2 = status2;
   }

   public String getStatusTjta() {
      return statusTjta;
   }

   public void setStatusTjta(String statusTjta) {
      this.statusTjta = statusTjta;
   }

   public String getTelefono() {
      return telefono;
   }

   public void setTelefono(String telefono) {
      this.telefono = telefono;
   }

   public String getNumeroTarjeta() {
      return numeroTarjeta;
   }

   public void setNumeroTarjeta(String numeroTarjeta) {
      this.numeroTarjeta = numeroTarjeta;
   }
}
