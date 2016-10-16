package cl.bbr.jumbocl.eventos.dto;

import java.io.Serializable;

public class EventosCriterioDTO implements Serializable {
	
    private String estado;
    private String tipoEvento;
    private String fechaInicio;
    private String fechaFin;
    private int pag;
    private int regsperpage;
    
	public EventosCriterioDTO() {
		super();
		
	}
	
	public EventosCriterioDTO(String estado, String tipoEvento, String fechaInicio, String fechaFin, int pag, int regsperpage) {
		super();
		this.estado = estado;
		this.tipoEvento = tipoEvento;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.pag = pag;
	    this.regsperpage = regsperpage;	
	}

    /**
     * @return Devuelve estado.
     */
    public String getEstado() {
        return estado;
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
     * @return Devuelve pag.
     */
    public int getPag() {
        return pag;
    }
    /**
     * @return Devuelve regsperpage.
     */
    public int getRegsperpage() {
        return regsperpage;
    }
    /**
     * @return Devuelve tipoEvento.
     */
    public String getTipoEvento() {
        return tipoEvento;
    }
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(String estado) {
        this.estado = estado;
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
     * @param pag El pag a establecer.
     */
    public void setPag(int pag) {
        this.pag = pag;
    }
    /**
     * @param regsperpage El regsperpage a establecer.
     */
    public void setRegsperpage(int regsperpage) {
        this.regsperpage = regsperpage;
    }
    /**
     * @param tipoEvento El tipoEvento a establecer.
     */
    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
}
