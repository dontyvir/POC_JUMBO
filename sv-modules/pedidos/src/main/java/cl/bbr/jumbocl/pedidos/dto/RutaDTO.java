package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RutaDTO implements Serializable {
    
    private long idRuta;
    private FonoTransporteDTO fono;
    private ChoferTransporteDTO chofer;
    private PatenteTransporteDTO patente;
    private long cantBins;
    private String fechaSalida;
    private String horaSalida;
    private String fechaCreacion;
    private EstadoDTO estado;
    private List zonas = new ArrayList();
    private List jornadas = new ArrayList();
    private LocalDTO local;

    
    
    /**
     * @return Devuelve cantBins.
     */
    public long getCantBins() {
        return cantBins;
    }
    /**
     * @return Devuelve chofer.
     */
    public ChoferTransporteDTO getChofer() {
        return chofer;
    }
    /**
     * @return Devuelve estado.
     */
    public EstadoDTO getEstado() {
        return estado;
    }
    /**
     * @return Devuelve fechaCreacion.
     */
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    /**
     * @return Devuelve fechaSalida.
     */
    public String getFechaSalida() {
        return fechaSalida;
    }
    /**
     * @return Devuelve fono.
     */
    public FonoTransporteDTO getFono() {
        return fono;
    }
    /**
     * @return Devuelve horaSalida.
     */
    public String getHoraSalida() {
        return horaSalida;
    }
    /**
     * @return Devuelve idRuta.
     */
    public long getIdRuta() {
        return idRuta;
    }
    /**
     * @return Devuelve patente.
     */
    public PatenteTransporteDTO getPatente() {
        return patente;
    }
    /**
     * @param cantBins El cantBins a establecer.
     */
    public void setCantBins(long cantBins) {
        this.cantBins = cantBins;
    }
    /**
     * @param chofer El chofer a establecer.
     */
    public void setChofer(ChoferTransporteDTO chofer) {
        this.chofer = chofer;
    }
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(EstadoDTO estado) {
        this.estado = estado;
    }
    /**
     * @param fechaCreacion El fechaCreacion a establecer.
     */
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    /**
     * @param fechaSalida El fechaSalida a establecer.
     */
    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
    /**
     * @param fono El fono a establecer.
     */
    public void setFono(FonoTransporteDTO fono) {
        this.fono = fono;
    }
    /**
     * @param horaSalida El horaSalida a establecer.
     */
    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }
    /**
     * @param idRuta El idRuta a establecer.
     */
    public void setIdRuta(long idRuta) {
        this.idRuta = idRuta;
    }
    /**
     * @param patente El patente a establecer.
     */
    public void setPatente(PatenteTransporteDTO patente) {
        this.patente = patente;
    }
    /**
     * @return Devuelve zonas.
     */
    public List getZonas() {
        return zonas;
    }
    /**
     * @param zonas El zonas a establecer.
     */
    public void setZonas(List zonas) {
        this.zonas = zonas;
    }
    /**
     * @return Devuelve jornadas.
     */
    public List getJornadas() {
        return jornadas;
    }
    /**
     * @param jornadas El jornadas a establecer.
     */
    public void setJornadas(List jornadas) {
        this.jornadas = jornadas;
    }

    /**
     * @return Devuelve local.
     */
    public LocalDTO getLocal() {
        return local;
    }
    /**
     * @param local El local a establecer.
     */
    public void setLocal(LocalDTO local) {
        this.local = local;
    }
}
