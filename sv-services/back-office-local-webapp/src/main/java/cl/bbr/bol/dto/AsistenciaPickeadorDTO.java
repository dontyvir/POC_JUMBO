/*
 * Created on 12-nov-2009
 */
package cl.bbr.bol.dto;

import java.util.Date;

/**
 * @author jdroguett
 *  
 */
public class AsistenciaPickeadorDTO {
   private int id;
   private int pickeadorId;
   private int localId;
   private Date fecha;
   private String asistencia;

   public String getAsistencia() {
      return asistencia;
   }

   public void setAsistencia(String asistencia) {
      this.asistencia = asistencia;
   }

   public Date getFecha() {
      return fecha;
   }

   public void setFecha(Date fecha) {
      this.fecha = fecha;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }
   public int getPickeadorId() {
      return pickeadorId;
   }
   public void setPickeadorId(int pickeadorId) {
      this.pickeadorId = pickeadorId;
   }
   public int getLocalId() {
      return localId;
   }
   public void setLocalId(int localId) {
      this.localId = localId;
   }
}
