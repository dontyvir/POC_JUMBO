package cl.bbr.jumbocl.pedidos.dto;


public class EmpresaTransporteDTO {
    
	private long idEmpresaTransporte;
    private String nombre;
    private String activado;
	
	public EmpresaTransporteDTO() {

	}
	
	
    /**
     * @return Devuelve activado.
     */
    public String getActivado() {
        return activado;
    }
    /**
     * @return Devuelve idEmpresaTransporte.
     */
    public long getIdEmpresaTransporte() {
        return idEmpresaTransporte;
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
     * @param idEmpresaTransporte El idEmpresaTransporte a establecer.
     */
    public void setIdEmpresaTransporte(long idEmpresaTransporte) {
        this.idEmpresaTransporte = idEmpresaTransporte;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
