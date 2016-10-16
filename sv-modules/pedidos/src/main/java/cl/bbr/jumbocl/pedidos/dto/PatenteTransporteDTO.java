package cl.bbr.jumbocl.pedidos.dto;

public class PatenteTransporteDTO {
    
	private long idPatente;
    private String patente;
    private long cantMaxBins;
    private String activado;
    private EmpresaTransporteDTO empresaTransporte;
    private long idLocal;
	
	public PatenteTransporteDTO() {

	}
	
    /**
     * @return Devuelve activado.
     */
    public String getActivado() {
        return activado;
    }
    /**
     * @return Devuelve empresaTransporte.
     */
    public EmpresaTransporteDTO getEmpresaTransporte() {
        return empresaTransporte;
    }
    /**
     * @return Devuelve idPatente.
     */
    public long getIdPatente() {
        return idPatente;
    }
    /**
     * @return Devuelve patente.
     */
    public String getPatente() {
        return patente;
    }
    /**
     * @param activado El activado a establecer.
     */
    public void setActivado(String activado) {
        this.activado = activado;
    }
    /**
     * @param empresaTransporte El empresaTransporte a establecer.
     */
    public void setEmpresaTransporte(EmpresaTransporteDTO empresaTransporte) {
        this.empresaTransporte = empresaTransporte;
    }
    /**
     * @param idPatente El idPatente a establecer.
     */
    public void setIdPatente(long idPatente) {
        this.idPatente = idPatente;
    }
    /**
     * @param patente El patente a establecer.
     */
    public void setPatente(String patente) {
        this.patente = patente;
    }
    /**
     * @return Devuelve cantMaxBins.
     */
    public long getCantMaxBins() {
        return cantMaxBins;
    }
    /**
     * @param cantMaxBins El cantMaxBins a establecer.
     */
    public void setCantMaxBins(long cantMaxBins) {
        this.cantMaxBins = cantMaxBins;
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
}
