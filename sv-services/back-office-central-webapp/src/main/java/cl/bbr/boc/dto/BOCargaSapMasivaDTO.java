/*
 * Created on 20-may-2010
 */
package cl.bbr.boc.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author hs
 */
public class BOCargaSapMasivaDTO {
	
   private String  codigoSap;
   private String  unidadMedida;   
   private String  descripcion;
   private String  idGrupo;
   private String  codBarra;
   private BigDecimal precio;
   private Map localesON;
   private Map localesOFF;
   
   

   
/**
 * @return el localesOFF
 */
public Map getLocalesOFF() {
	return localesOFF;
}
/**
 * @param localesOFF el localesOFF a establecer
 */
public void setLocalesOFF(Map localesOFF) {
	this.localesOFF = localesOFF;
}
/**
 * @return el localesON
 */
public Map getLocalesON() {
	return localesON;
}
/**
 * @param localesON el localesON a establecer
 */
public void setLocalesON(Map localesON) {
	this.localesON = localesON;
}
/**
 * @return el codigoSap
 */
public String getCodigoSap() {
	return codigoSap;
}
/**
 * @param codigoSap el codigoSap a establecer
 */
public void setCodigoSap(String codigoSap) {
	this.codigoSap = codigoSap;
}
/**
 * @return el unidadMedida
 */
public String getUnidadMedida() {
	return unidadMedida;
}
/**
 * @param unidadMedida el unidadMedida a establecer
 */
public void setUnidadMedida(String unidadMedida) {
	this.unidadMedida = unidadMedida;
}
/**
 * @return el descripcion
 */
public String getDescripcion() {
	return descripcion;
}
/**
 * @param descripcion el descripcion a establecer
 */
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
/**
 * @return el idGrupo
 */
public String getIdGrupo() {
	return idGrupo;
}
/**
 * @param idGrupo el idGrupo a establecer
 */
public void setIdGrupo(String idGrupo) {
	this.idGrupo = idGrupo;
}
/**
 * @return el codBarra
 */
public String getCodBarra() {
	return codBarra;
}
/**
 * @param codBarra el codBarra a establecer
 */
public void setCodBarra(String codBarra) {
	this.codBarra = codBarra;
}
/**
 * @return el precio
 */
public BigDecimal getPrecio() {
	return precio;
}
/**
 * @param precio el precio a establecer
 */
public void setPrecio(BigDecimal precio) {
	this.precio = precio;
}
   
   
   
}
