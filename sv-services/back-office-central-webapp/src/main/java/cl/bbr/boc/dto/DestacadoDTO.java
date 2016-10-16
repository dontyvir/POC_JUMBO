/*
 * Created on 30-ene-2009
 *
 */
package cl.bbr.boc.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cl.bbr.jumbocl.common.utils.DString;

/**
 * @author jdroguett
 *  
 */
public class DestacadoDTO {
   private int id;

   private String descripcion;

   private Date fechaHoraIni;

   private Date fechaHoraFin;

   private String imagen;

   private List locales;

   private List productos;

   private int cantidadProductos;

   public DestacadoDTO() {
      locales = new ArrayList();
      productos = new ArrayList();
   }

   public String getDescripcion() {
      return descripcion;
   }

   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }

   public Date getFechaHoraFin() {
      return fechaHoraFin;
   }

   public void setFechaHoraFin(Date fechaHoraFin) {
      this.fechaHoraFin = fechaHoraFin;
   }

   public Date getFechaHoraIni() {
      return fechaHoraIni;
   }

   public void setFechaHoraIni(Date fechaHoraIni) {
      this.fechaHoraIni = fechaHoraIni;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getImagen() {
      return imagen;
   }

   public void setImagen(String imagen) {
      this.imagen = imagen;
   }

   public List getLocales() {
      return locales;
   }

   public void addLocal(LocalDTO local) {
      this.locales.add(local);
   }

   public List getProductos() {
      return productos;
   }

   public void addProducto(FOProductoDTO producto) {
      this.productos.add(producto);
   }

   /**
    * Hay casos donde sólo se necesita conocer la cantidad de producto y no es
    * necesario cargar la lista "productos"
    * 
    * @return
    */
   public int getCantidadProductos() {
      return (productos.size() == 0 ? cantidadProductos : productos.size());
   }

   public void setCantidadProductos(int cantidadProductos) {
      this.cantidadProductos = cantidadProductos;
   }

   public String getLocalesString() {
      StringBuffer sb = new StringBuffer();
      for (Iterator iter = locales.iterator(); iter.hasNext();) {
         LocalDTO local = (LocalDTO) iter.next();
         sb.append(local.getNombre());
         if (iter.hasNext())
            sb.append(", ");
      }
      return sb.toString();
   }

   public String getCodLocalesString() {
      StringBuffer sb = new StringBuffer();
      for (Iterator iter = locales.iterator(); iter.hasNext();) {
         LocalDTO local = (LocalDTO) iter.next();
         //sb.append(local.getCodigo());
         sb.append(DString.abreviar(local.getNombre()) );
         if (iter.hasNext())
            sb.append(", ");
      }
      return sb.toString();
   }

   
   public void setLocales(List locales) {
      this.locales = locales;
   }

   public void setProductos(List productos) {
      this.productos = productos;
   }
}
