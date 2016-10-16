/*
 * Created on 20-may-2010
 */
package cl.bbr.boc.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jdroguett
 */
public class BOProductoDTO {
   private int id;
   private String codSap;
   private String descripcion;
   private String unidad;
   private List precios;
   private String descripcion_corta;
   private String descripcion_larga;
   
   
   
   /**
 * @return el descripcion_corta
 */
public String getDescripcion_corta() {
	return descripcion_corta;
}

/**
 * @param descripcion_corta el descripcion_corta a establecer
 */
public void setDescripcion_corta(String descripcion_corta) {
	this.descripcion_corta = descripcion_corta;
}

/**
 * @return el descripcion_larga
 */
public String getDescripcion_larga() {
	return descripcion_larga;
}

/**
 * @param descripcion_larga el descripcion_larga a establecer
 */
public void setDescripcion_larga(String descripcion_larga) {
	this.descripcion_larga = descripcion_larga;
}

public BOProductoDTO() {
      this.precios = new ArrayList();
   }

   public String getCodSap() {
      return codSap;
   }

   public void setCodSap(String codSap) {
      this.codSap = codSap;
   }

   public String getDescripcion() {
      return descripcion;
   }

   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public List getPrecios() {
      return precios;
   }

   public void setPrecios(List precios) {
      this.precios = precios;
   }

   public void addPrecio(BOPrecioDTO precio) {
      this.precios.add(precio);
   }

   public String getUnidad() {
      return unidad;
   }

   public void setUnidad(String unidad) {
      this.unidad = unidad;
   }

}
