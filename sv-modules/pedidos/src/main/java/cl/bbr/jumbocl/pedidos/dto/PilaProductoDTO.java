package cl.bbr.jumbocl.pedidos.dto;


public class PilaProductoDTO {
    
	private long idProductoFO;
    private PilaNutricionalDTO pila;
    private double nutrientePorPorcion;
    private double porcentaje; //Dosis diaria
	
	public PilaProductoDTO() {
	}
	
	
    /**
     * @return Devuelve idProductoFO.
     */
    public long getIdProductoFO() {
        return idProductoFO;
    }
    /**
     * @return Devuelve nutrientePorPorcion.
     */
    public double getNutrientePorPorcion() {
        return nutrientePorPorcion;
    }
    /**
     * @return Devuelve pila.
     */
    public PilaNutricionalDTO getPila() {
        return pila;
    }
    /**
     * @return Devuelve porcentaje.
     */
    public double getPorcentaje() {
        return porcentaje;
    }
    /**
     * @param idProductoFO El idProductoFO a establecer.
     */
    public void setIdProductoFO(long idProductoFO) {
        this.idProductoFO = idProductoFO;
    }
    /**
     * @param nutrientePorPorcion El nutrientePorPorcion a establecer.
     */
    public void setNutrientePorPorcion(double nutrientePorPorcion) {
        this.nutrientePorPorcion = nutrientePorPorcion;
    }
    /**
     * @param pila El pila a establecer.
     */
    public void setPila(PilaNutricionalDTO pila) {
        this.pila = pila;
    }
    /**
     * @param porcentaje El porcentaje a establecer.
     */
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
}
