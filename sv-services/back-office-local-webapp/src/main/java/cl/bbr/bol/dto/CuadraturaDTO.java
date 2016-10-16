/*
 * Created on 29-mar-2010
 */
package cl.bbr.bol.dto;

import java.util.Date;

/**
 * @author jdroguett
 */
public class CuadraturaDTO {
   private Date fechaDespacho;
   private int rutaId;
   private int pedidoId;
   private int cantidadBines;
   private String patente;
   private String chofer;
   private String direccionDespacho;
   private String comuna;
   private String jornadaDespacho;
   private Date horaActivacionRuta;
   private Date horaLlegadaDomicilio;
   private Date horaSalidaDomicilio;
   private int reprogramacion;
   private String responsable;
   private String motivo;

   public int getCantidadBines() {
      return cantidadBines;
   }

   public void setCantidadBines(int cantidadBines) {
      this.cantidadBines = cantidadBines;
   }

   public String getChofer() {
      return chofer;
   }

   public void setChofer(String chofer) {
      this.chofer = chofer;
   }

   public String getComuna() {
      return comuna;
   }

   public void setComuna(String comuna) {
      this.comuna = comuna;
   }

   public String getDireccionDespacho() {
      return direccionDespacho;
   }

   public void setDireccionDespacho(String direccionDespacho) {
      this.direccionDespacho = direccionDespacho;
   }

   public Date getFechaDespacho() {
      return fechaDespacho;
   }

   public void setFechaDespacho(Date fechaDespacho) {
      this.fechaDespacho = fechaDespacho;
   }

   public Date getHoraActivacionRuta() {
      return horaActivacionRuta;
   }

   public void setHoraActivacionRuta(Date horaActivacionRuta) {
      this.horaActivacionRuta = horaActivacionRuta;
   }

   public Date getHoraLlegadaDomicilio() {
      return horaLlegadaDomicilio;
   }

   public void setHoraLlegadaDomicilio(Date horaLlegadaDomicilio) {
      this.horaLlegadaDomicilio = horaLlegadaDomicilio;
   }

   public Date getHoraSalidaDomicilio() {
      return horaSalidaDomicilio;
   }

   public void setHoraSalidaDomicilio(Date horaSalidaDomicilio) {
      this.horaSalidaDomicilio = horaSalidaDomicilio;
   }

   public String getJornadaDespacho() {
      return jornadaDespacho;
   }

   public void setJornadaDespacho(String jornadaDespacho) {
      this.jornadaDespacho = jornadaDespacho;
   }

   public String getMotivo() {
      return motivo;
   }

   public void setMotivo(String motivo) {
      this.motivo = motivo;
   }

   public String getPatente() {
      return patente;
   }

   public void setPatente(String patente) {
      this.patente = patente;
   }

   public int getPedidoId() {
      return pedidoId;
   }

   public void setPedidoId(int pedidoId) {
      this.pedidoId = pedidoId;
   }

   public int getReprogramacion() {
      return reprogramacion;
   }

   public void setReprogramacion(int reprogramacion) {
      this.reprogramacion = reprogramacion;
   }

   public String getResponsable() {
      return responsable;
   }

   public void setResponsable(String responsable) {
      this.responsable = responsable;
   }
   public int getRutaId() {
      return rutaId;
   }
   public void setRutaId(int rutaId) {
      this.rutaId = rutaId;
   }
}
