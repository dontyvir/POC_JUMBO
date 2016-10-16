/*
 * Creado el 03-08-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.casos.dto;

import java.io.Serializable;

/**
 * @author imoyano
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class PedidoCasoDTO implements Serializable {
    
    private long idPedido;
    private String fechaPedido;
    private String direccion;
    private String comuna;
    private long idLocal;
    private String fechaDespacho;
    private long cliRut;
    private String cliDv;
    private String cliNombre;
    private String cliFonCod1;
    private String cliFonNum1;
    private String cliFonCod2;
    private String cliFonNum2;
    private String cliFonCod3;
    private String cliFonNum3;
    private boolean existePedido;
    private long idUsuFonoCompra;
    private String usuFonoCompraNombre;
    
    public PedidoCasoDTO() {
        this.idPedido = 0;
        this.fechaPedido = "";
        this.direccion = "";
        this.comuna = "";
        this.idLocal = 0;
        this.fechaDespacho = "";
        this.cliRut = 0;
        this.cliDv = "";
        this.cliNombre = "";
        this.cliFonCod1 = "";
        this.cliFonNum1 = "";
        this.cliFonCod2 = "";
        this.cliFonNum2 = "";
        this.cliFonCod3 = "";
        this.cliFonNum3 = "";
        this.existePedido = false;
        this.idUsuFonoCompra = 0;
        this.usuFonoCompraNombre = "";
    }

   
    /**
     * @return Devuelve cliDv.
     */
    public String getCliDv() {
        return cliDv;
    }
    /**
     * @return Devuelve cliFonCod1.
     */
    public String getCliFonCod1() {
        return cliFonCod1;
    }
    /**
     * @return Devuelve cliFonCod2.
     */
    public String getCliFonCod2() {
        return cliFonCod2;
    }
    /**
     * @return Devuelve cliFonCod3.
     */
    public String getCliFonCod3() {
        return cliFonCod3;
    }
    /**
     * @return Devuelve cliFonNum1.
     */
    public String getCliFonNum1() {
        return cliFonNum1;
    }
    /**
     * @return Devuelve cliFonNum2.
     */
    public String getCliFonNum2() {
        return cliFonNum2;
    }
    /**
     * @return Devuelve cliFonNum3.
     */
    public String getCliFonNum3() {
        return cliFonNum3;
    }
    /**
     * @return Devuelve cliNombre.
     */
    public String getCliNombre() {
        return cliNombre;
    }
    /**
     * @return Devuelve cliRut.
     */
    public long getCliRut() {
        return cliRut;
    }
    /**
     * @return Devuelve comuna.
     */
    public String getComuna() {
        return comuna;
    }
    /**
     * @return Devuelve direccion.
     */
    public String getDireccion() {
        return direccion;
    }
    /**
     * @return Devuelve fechaDespacho.
     */
    public String getFechaDespacho() {
        return fechaDespacho;
    }
    /**
     * @return Devuelve fechaPedido.
     */
    public String getFechaPedido() {
        return fechaPedido;
    }
    /**
     * @return Devuelve idPedido.
     */
    public long getIdPedido() {
        return idPedido;
    }
    /**
     * @param cliDv El cliDv a establecer.
     */
    public void setCliDv(String cliDv) {
        this.cliDv = cliDv;
    }
    /**
     * @param cliFonCod1 El cliFonCod1 a establecer.
     */
    public void setCliFonCod1(String cliFonCod1) {
        this.cliFonCod1 = cliFonCod1;
    }
    /**
     * @param cliFonCod2 El cliFonCod2 a establecer.
     */
    public void setCliFonCod2(String cliFonCod2) {
        this.cliFonCod2 = cliFonCod2;
    }
    /**
     * @param cliFonCod3 El cliFonCod3 a establecer.
     */
    public void setCliFonCod3(String cliFonCod3) {
        this.cliFonCod3 = cliFonCod3;
    }
    /**
     * @param cliFonNum1 El cliFonNum1 a establecer.
     */
    public void setCliFonNum1(String cliFonNum1) {
        this.cliFonNum1 = cliFonNum1;
    }
    /**
     * @param cliFonNum2 El cliFonNum2 a establecer.
     */
    public void setCliFonNum2(String cliFonNum2) {
        this.cliFonNum2 = cliFonNum2;
    }
    /**
     * @param cliFonNum3 El cliFonNum3 a establecer.
     */
    public void setCliFonNum3(String cliFonNum3) {
        this.cliFonNum3 = cliFonNum3;
    }
    /**
     * @param cliNombre El cliNombre a establecer.
     */
    public void setCliNombre(String cliNombre) {
        this.cliNombre = cliNombre;
    }
    /**
     * @param cliRut El cliRut a establecer.
     */
    public void setCliRut(long cliRut) {
        this.cliRut = cliRut;
    }
    /**
     * @param comuna El comuna a establecer.
     */
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }
    /**
     * @param direccion El direccion a establecer.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    /**
     * @param fechaDespacho El fechaDespacho a establecer.
     */
    public void setFechaDespacho(String fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }
    /**
     * @param fechaPedido El fechaPedido a establecer.
     */
    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
    /**
     * @param idPedido El idPedido a establecer.
     */
    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }
    /**
     * @return Devuelve existePedido.
     */
    public boolean isExistePedido() {
        return existePedido;
    }
    /**
     * @param existePedido El existePedido a establecer.
     */
    public void setExistePedido(boolean existePedido) {
        this.existePedido = existePedido;
    }
    /**
     * @return Devuelve idLocal.
     */
    public long getIdLocal() {
        return idLocal;
    }
    /**
     * @param idLocal El idLocal a establecer.
     */
    public void setIdLocal(long idLocal) {
        this.idLocal = idLocal;
    }
    /**
     * @return Devuelve idUsuFonoCompra.
     */
    public long getIdUsuFonoCompra() {
        return idUsuFonoCompra;
    }
    /**
     * @return Devuelve usuFonoCompraNombre.
     */
    public String getUsuFonoCompraNombre() {
        return usuFonoCompraNombre;
    }
    /**
     * @param idUsuFonoCompra El idUsuFonoCompra a establecer.
     */
    public void setIdUsuFonoCompra(long idUsuFonoCompra) {
        this.idUsuFonoCompra = idUsuFonoCompra;
    }
    /**
     * @param usuFonoCompraNombre El usuFonoCompraNombre a establecer.
     */
    public void setUsuFonoCompraNombre(String usuFonoCompraNombre) {
        this.usuFonoCompraNombre = usuFonoCompraNombre;
    }
}
