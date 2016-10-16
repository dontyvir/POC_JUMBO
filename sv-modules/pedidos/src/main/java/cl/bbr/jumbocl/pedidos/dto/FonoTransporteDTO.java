package cl.bbr.jumbocl.pedidos.dto;

public class FonoTransporteDTO {
    
	private long idFono;
    private long codigo;
    private long numero;
    private String activado;
    private String nombre;
    private EmpresaTransporteDTO empresaTransporte;
    private long idLocal;
	
	public FonoTransporteDTO() {

	}
	
	
    /**
     * @return Devuelve activado.
     */
    public String getActivado() {
        return activado;
    }
    /**
     * @return Devuelve codigo.
     */
    public long getCodigo() {
        return codigo;
    }
    /**
     * @return Devuelve empresaTransporte.
     */
    public EmpresaTransporteDTO getEmpresaTransporte() {
        return empresaTransporte;
    }
    /**
     * @return Devuelve idFono.
     */
    public long getIdFono() {
        return idFono;
    }
    /**
     * @return Devuelve numero.
     */
    public long getNumero() {
        return numero;
    }
    /**
     * @param activado El activado a establecer.
     */
    public void setActivado(String activado) {
        this.activado = activado;
    }
    /**
     * @param codigo El codigo a establecer.
     */
    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }
    /**
     * @param empresaTransporte El empresaTransporte a establecer.
     */
    public void setEmpresaTransporte(EmpresaTransporteDTO empresaTransporte) {
        this.empresaTransporte = empresaTransporte;
    }
    /**
     * @param idFono El idFono a establecer.
     */
    public void setIdFono(long idFono) {
        this.idFono = idFono;
    }
    /**
     * @param numero El numero a establecer.
     */
    public void setNumero(long numero) {
        this.numero = numero;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
