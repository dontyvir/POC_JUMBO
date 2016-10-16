/*
 * Creado el 03-08-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.casos.dto;

import java.io.Serializable;

import cl.bbr.jumbocl.common.dto.ObjetoDTO;

/**
 * @author imoyano
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class ProductoCasoDTO implements Serializable {
    
    private long idProducto;
    private long idCaso;
    private String ppCantidad;
    private String ppUnidad;
    private String ppDescripcion;
    private String tipoAccion;
    private String comentarioBOC;
    private String psCantidad;
    private String psUnidad;
    private String psDescripcion;
    private String comentarioBOL;
    private QuiebreCasoDTO quiebre;
    private ObjetoDTO responsable;
    private ObjetoDTO motivo;
    private String pickeador;
    private long precio;
    
    public ProductoCasoDTO() {
        this.idProducto = 0;
        this.idCaso = 0;
        this.ppCantidad = "";
        this.ppUnidad = "";
        this.ppDescripcion = "";
        this.tipoAccion = "";
        this.comentarioBOC = "";
        this.psCantidad = "";
        this.psUnidad = "";
        this.psDescripcion = "";
        this.comentarioBOL = "";
        this.quiebre = new QuiebreCasoDTO();
        this.responsable = new ObjetoDTO();
        this.motivo = new ObjetoDTO();
        this.pickeador = "";
        this.precio = 0;
    }

       
    /**
     * @return Devuelve comentarioBOC.
     */
    public String getComentarioBOC() {
        return comentarioBOC;
    }
    /**
     * @return Devuelve comentarioBOL.
     */
    public String getComentarioBOL() {
        return comentarioBOL;
    }
    /**
     * @return Devuelve idCaso.
     */
    public long getIdCaso() {
        return idCaso;
    }
    /**
     * @return Devuelve idProducto.
     */
    public long getIdProducto() {
        return idProducto;
    }
    /**
     * @return Devuelve quiebre.
     */
    public QuiebreCasoDTO getQuiebre() {
        return quiebre;
    }
    /**
     * @return Devuelve responsable.
     */
    public ObjetoDTO getResponsable() {
        return responsable;
    }
    /**
     * @param comentarioBOC El comentarioBOC a establecer.
     */
    public void setComentarioBOC(String comentarioBOC) {
        this.comentarioBOC = comentarioBOC;
    }
    /**
     * @param comentarioBOL El comentarioBOL a establecer.
     */
    public void setComentarioBOL(String comentarioBOL) {
        this.comentarioBOL = comentarioBOL;
    }
    /**
     * @param idCaso El idCaso a establecer.
     */
    public void setIdCaso(long idCaso) {
        this.idCaso = idCaso;
    }
    /**
     * @param idProducto El idProducto a establecer.
     */
    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }
    /**
     * @param quiebre El quiebre a establecer.
     */
    public void setQuiebre(QuiebreCasoDTO quiebre) {
        this.quiebre = quiebre;
    }
    /**
     * @param responsable El responsable a establecer.
     */
    public void setResponsable(ObjetoDTO responsable) {
        this.responsable = responsable;
    }
    /**
     * @return Devuelve tipoAccion.
     */
    public String getTipoAccion() {
        return tipoAccion;
    }
    /**
     * @param tipoAccion El tipoAccion a establecer.
     */
    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }
    /**
     * @return Devuelve ppCantidad.
     */
    public String getPpCantidad() {
        return ppCantidad;
    }
    /**
     * @return Devuelve ppDescripcion.
     */
    public String getPpDescripcion() {
        return ppDescripcion;
    }
    /**
     * @return Devuelve ppUnidad.
     */
    public String getPpUnidad() {
        return ppUnidad;
    }
    /**
     * @return Devuelve psCantidad.
     */
    public String getPsCantidad() {
        return psCantidad;
    }
    /**
     * @return Devuelve psDescripcion.
     */
    public String getPsDescripcion() {
        return psDescripcion;
    }
    /**
     * @return Devuelve psUnidad.
     */
    public String getPsUnidad() {
        return psUnidad;
    }
    /**
     * @param ppCantidad El ppCantidad a establecer.
     */
    public void setPpCantidad(String ppCantidad) {
        this.ppCantidad = ppCantidad;
    }
    /**
     * @param ppDescripcion El ppDescripcion a establecer.
     */
    public void setPpDescripcion(String ppDescripcion) {
        this.ppDescripcion = ppDescripcion;
    }
    /**
     * @param ppUnidad El ppUnidad a establecer.
     */
    public void setPpUnidad(String ppUnidad) {
        this.ppUnidad = ppUnidad;
    }
    /**
     * @param psCantidad El psCantidad a establecer.
     */
    public void setPsCantidad(String psCantidad) {
        this.psCantidad = psCantidad;
    }
    /**
     * @param psDescripcion El psDescripcion a establecer.
     */
    public void setPsDescripcion(String psDescripcion) {
        this.psDescripcion = psDescripcion;
    }
    /**
     * @param psUnidad El psUnidad a establecer.
     */
    public void setPsUnidad(String psUnidad) {
        this.psUnidad = psUnidad;
    }
    /**
     * @return Devuelve pickeador.
     */
    public String getPickeador() {
        return pickeador;
    }
    /**
     * @param pickeador El pickeador a establecer.
     */
    public void setPickeador(String pickeador) {
        this.pickeador = pickeador;
    }
    /**
     * @return Devuelve precio.
     */
    public long getPrecio() {
        return precio;
    }
    /**
     * @param precio El precio a establecer.
     */
    public void setPrecio(long precio) {
        this.precio = precio;
    }
    /**
     * @return Devuelve motivo.
     */
    public ObjetoDTO getMotivo() {
        return motivo;
    }
    /**
     * @param motivo El motivo a establecer.
     */
    public void setMotivo(ObjetoDTO motivo) {
        this.motivo = motivo;
    }
}
