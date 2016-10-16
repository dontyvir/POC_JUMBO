package cl.bbr.jumbocl.common.model;

/**
 * Clase que Captura desde la Base de Datos los datos del Bins de un pedido
 * @author bbr
 *
 */
public class DiaDespachoEconomico {
	private int precio;
	private boolean pasado;
	private boolean diaActual;
	private String fecha;
	private boolean mostrar;
	/**
	 * Constructor
	 */
	public DiaDespachoEconomico() {
	    this.precio 	= -1;
	    this.pasado 	= false;
	    this.diaActual 	= false;
	    this.fecha		= "";
	    this.mostrar	= true;
	}


    /**
     * @return Devuelve diaActual.
     */
    public boolean isDiaActual() {
        return diaActual;
    }
    /**
     * @return Devuelve pasado.
     */
    public boolean isPasado() {
        return pasado;
    }
    /**
     * @return Devuelve precio.
     */
    public int getPrecio() {
        return precio;
    }
    /**
     * @param diaActual El diaActual a establecer.
     */
    public void setDiaActual(boolean diaActual) {
        this.diaActual = diaActual;
    }
    /**
     * @param pasado El pasado a establecer.
     */
    public void setPasado(boolean pasado) {
        this.pasado = pasado;
    }
    /**
     * @param precio El precio a establecer.
     */
    public void setPrecio(int precio) {
        this.precio = precio;
    }
    /**
     * @return Devuelve fecha.
     */
    public String getFecha() {
        return fecha;
    }
    /**
     * @param fecha El fecha a establecer.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    /**
     * @return Devuelve mostrar.
     */
    public boolean isMostrar() {
        return mostrar;
    }
    /**
     * @param mostrar El mostrar a establecer.
     */
    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }
}
