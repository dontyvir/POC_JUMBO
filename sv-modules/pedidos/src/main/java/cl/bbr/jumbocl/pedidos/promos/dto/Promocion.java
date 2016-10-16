/*
 * Created on 23-mar-2010
 */
package cl.bbr.jumbocl.pedidos.promos.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cl.bbr.jumbocl.common.utils.DString;

/**
 * @author jdroguett
 */
public class Promocion {
   private int codigo;
   private int tipo;
   private String descripcion;
   private Date fechaIni;
   private Date fechaFin;
   private List locales;
   
   public Promocion(){
      this.locales = new ArrayList();
   }
   
   
   public String getCodLocalesString() {
      StringBuffer sb = new StringBuffer();
      for (Iterator iter = locales.iterator(); iter.hasNext();) {
         String local = (String) iter.next();
         sb.append(DString.abreviar(local));
         if (iter.hasNext())
            sb.append(", ");
      }
      return sb.toString();
   }

   
   public int getCodigo() {
      return codigo;
   }
   public void setCodigo(int codigo) {
      this.codigo = codigo;
   }
   public String getDescripcion() {
      return descripcion;
   }
   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }
   public Date getFechaFin() {
      return fechaFin;
   }
   public void setFechaFin(Date fechaFin) {
      this.fechaFin = fechaFin;
   }
   public Date getFechaIni() {
      return fechaIni;
   }
   public void setFechaIni(Date fechaIni) {
      this.fechaIni = fechaIni;
   }
   public List getLocales() {
      return locales;
   }
   public void addLocal(String local) {
      this.locales.add(local);
   }
   public int getTipo() {
      return tipo;
   }
   public void setTipo(int tipo) {
      this.tipo = tipo;
   }
}
