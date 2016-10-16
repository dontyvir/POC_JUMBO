package cl.bbr.jumbocl.pedidos.dto;


public class PilaNutricionalDTO {
    
	private long idPila;
    private String nutriente;
    private PilaUnidadDTO unidad;
	
	public PilaNutricionalDTO() {

	}
	
	
    /**
     * @return Devuelve idPila.
     */
    public long getIdPila() {
        return idPila;
    }
    /**
     * @return Devuelve nutriente.
     */
    public String getNutriente() {
        return nutriente;
    }
    /**
     * @return Devuelve unidad.
     */
    public PilaUnidadDTO getUnidad() {
        return unidad;
    }
    /**
     * @param idPila El idPila a establecer.
     */
    public void setIdPila(long idPila) {
        this.idPila = idPila;
    }
    /**
     * @param nutriente El nutriente a establecer.
     */
    public void setNutriente(String nutriente) {
        this.nutriente = nutriente;
    }
    /**
     * @param unidad El unidad a establecer.
     */
    public void setUnidad(PilaUnidadDTO unidad) {
        this.unidad = unidad;
    }
}
