package cl.bbr.jumbocl.pedidos.dto;


public class PilaUnidadDTO {
    
	private long idPilaUnidad;
    private String unidad; //abreviada
    private String descripcion;
	
	public PilaUnidadDTO() {
	}
	
	
    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @return Devuelve idPilaUnidad.
     */
    public long getIdPilaUnidad() {
        return idPilaUnidad;
    }
    /**
     * @return Devuelve unidad.
     */
    public String getUnidad() {
        return unidad;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @param idPilaUnidad El idPilaUnidad a establecer.
     */
    public void setIdPilaUnidad(long idPilaUnidad) {
        this.idPilaUnidad = idPilaUnidad;
    }
    /**
     * @param unidad El unidad a establecer.
     */
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
}
