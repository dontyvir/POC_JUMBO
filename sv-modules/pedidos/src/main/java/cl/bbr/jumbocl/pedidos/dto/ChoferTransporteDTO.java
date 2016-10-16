package cl.bbr.jumbocl.pedidos.dto;

public class ChoferTransporteDTO {
    
	private long idChofer;
    private String nombre;
    private long rut;
    private String dv;
    private long idLocal;
    private String activado;
    private EmpresaTransporteDTO empresaTransporte;
	
	public ChoferTransporteDTO() {

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
     * @return Devuelve idChofer.
     */
    public long getIdChofer() {
        return idChofer;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
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
     * @param idChofer El idChofer a establecer.
     */
    public void setIdChofer(long idChofer) {
        this.idChofer = idChofer;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * @return Devuelve dv.
     */
    public String getDv() {
        return dv;
    }
    /**
     * @return Devuelve idLocal.
     */
    public long getIdLocal() {
        return idLocal;
    }
    /**
     * @return Devuelve rut.
     */
    public long getRut() {
        return rut;
    }
    /**
     * @param dv El dv a establecer.
     */
    public void setDv(String dv) {
        this.dv = dv;
    }
    /**
     * @param idLocal El idLocal a establecer.
     */
    public void setIdLocal(long idLocal) {
        this.idLocal = idLocal;
    }
    /**
     * @param rut El rut a establecer.
     */
    public void setRut(long rut) {
        this.rut = rut;
    }
}
