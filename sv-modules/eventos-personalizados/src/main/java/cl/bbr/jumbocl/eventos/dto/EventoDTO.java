/*
 * Creado el 13-11-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.eventos.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author imoyano
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class EventoDTO {
    
    private long idEvento;
    private String fechaCreacion;
    private String fechaModificacion;
    private long idUsuarioCreador;
    private long idUsuarioEditor;
    private String nombre;
    private String descripcion;
    private TipoEventoDTO tipoEvento;
    private String fechaInicio;
    private String fechaFin;
    private long ocurrencia;
    private String nombreArchivo;
    private long orden;
    private String activo;
    private long rutsTotal;
    private long rutsEvento;
    private List pasos;
    private long ocurrenciaPorRut;
    private String titulo;
    private String validacionManual;
	
	public EventoDTO() {
	    this.idEvento = 0;
	    this.fechaCreacion = "";
	    this.fechaModificacion = "";
	    this.idUsuarioCreador = 0;
	    this.idUsuarioEditor = 0;
	    this.nombre = "";
	    this.descripcion = "";
	    this.tipoEvento = new TipoEventoDTO();
	    this.fechaInicio = "";
	    this.fechaFin = "";
	    this.ocurrencia = 0;
	    this.nombreArchivo = "";
	    this.orden = 0;
	    this.activo = "";
	    this.rutsTotal = 0;
	    this.rutsEvento = 0;
	    this.ocurrenciaPorRut = 0;
	    this.pasos  = new ArrayList();
	    this.titulo = "";
        this.validacionManual = "";
	}

    /**
     * @return Devuelve activo.
     */
    public String getActivo() {
        return activo;
    }
    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @return Devuelve fechaCreacion.
     */
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    /**
     * @return Devuelve fechaFin.
     */
    public String getFechaFin() {
        return fechaFin;
    }
    /**
     * @return Devuelve fechaInicio.
     */
    public String getFechaInicio() {
        return fechaInicio;
    }
    /**
     * @return Devuelve fechaModificacion.
     */
    public String getFechaModificacion() {
        return fechaModificacion;
    }
    /**
     * @return Devuelve idEvento.
     */
    public long getIdEvento() {
        return idEvento;
    }
    /**
     * @return Devuelve idUsuarioEditor.
     */
    public long getIdUsuarioEditor() {
        return idUsuarioEditor;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @return Devuelve nombreArchivo.
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }
    /**
     * @return Devuelve ocurrencia.
     */
    public long getOcurrencia() {
        return ocurrencia;
    }
    /**
     * @return Devuelve orden.
     */
    public long getOrden() {
        return orden;
    }
    /**
     * @param activo El activo a establecer.
     */
    public void setActivo(String activo) {
        this.activo = activo;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @param fechaCreacion El fechaCreacion a establecer.
     */
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    /**
     * @param fechaFin El fechaFin a establecer.
     */
    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
    /**
     * @param fechaInicio El fechaInicio a establecer.
     */
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    /**
     * @param fechaModificacion El fechaModificacion a establecer.
     */
    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    /**
     * @param idEvento El idEvento a establecer.
     */
    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }
    /**
     * @param idUsuarioEditor El idUsuarioEditor a establecer.
     */
    public void setIdUsuarioEditor(long idUsuarioEditor) {
        this.idUsuarioEditor = idUsuarioEditor;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * @param nombreArchivo El nombreArchivo a establecer.
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    /**
     * @param ocurrencia El ocurrencia a establecer.
     */
    public void setOcurrencia(long ocurrencia) {
        this.ocurrencia = ocurrencia;
    }
    /**
     * @param orden El orden a establecer.
     */
    public void setOrden(long orden) {
        this.orden = orden;
    }
    /**
     * @return Devuelve tipoEvento.
     */
    public TipoEventoDTO getTipoEvento() {
        return tipoEvento;
    }
    /**
     * @param tipoEvento El tipoEvento a establecer.
     */
    public void setTipoEvento(TipoEventoDTO tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
    /**
     * @return Devuelve rutsEvento.
     */
    public long getRutsEvento() {
        return rutsEvento;
    }
    /**
     * @return Devuelve rutsTotal.
     */
    public long getRutsTotal() {
        return rutsTotal;
    }
    /**
     * @param rutsEvento El rutsEvento a establecer.
     */
    public void setRutsEvento(long rutsEvento) {
        this.rutsEvento = rutsEvento;
    }
    /**
     * @param rutsTotal El rutsTotal a establecer.
     */
    public void setRutsTotal(long rutsTotal) {
        this.rutsTotal = rutsTotal;
    }
    public String descripcionEstado() {
        if (activo.equalsIgnoreCase("S")) {
            return "Activado";
        }
        return "Desactivado";
    }
    /**
     * @return Devuelve idUsuarioCreador.
     */
    public long getIdUsuarioCreador() {
        return idUsuarioCreador;
    }
    /**
     * @param idUsuarioCreador El idUsuarioCreador a establecer.
     */
    public void setIdUsuarioCreador(long idUsuarioCreador) {
        this.idUsuarioCreador = idUsuarioCreador;
    }
    /**
     * @return Devuelve pasos.
     */
    public List getPasos() {
        return pasos;
    }
    /**
     * @param pasos El pasos a establecer.
     */
    public void setPasos(List pasos) {
        this.pasos = pasos;
    }
    /**
     * @return Devuelve ocurrenciaPorRut.
     */
    public long getOcurrenciaPorRut() {
        return ocurrenciaPorRut;
    }
    /**
     * @param ocurrenciaPorRut El ocurrenciaPorRut a establecer.
     */
    public void setOcurrenciaPorRut(long ocurrenciaPorRut) {
        this.ocurrenciaPorRut = ocurrenciaPorRut;
    }
    /**
     * @return Devuelve titulo.
     */
    public String getTitulo() {
        return titulo;
    }
    /**
     * @param titulo El titulo a establecer.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    /**
     * @return Devuelve validacionManual.
     */
    public String getValidacionManual() {
        return validacionManual;
    }
    /**
     * @param validacionManual El validacionManual a establecer.
     */
    public void setValidacionManual(String validacionManual) {
        this.validacionManual = validacionManual;
    }
}
