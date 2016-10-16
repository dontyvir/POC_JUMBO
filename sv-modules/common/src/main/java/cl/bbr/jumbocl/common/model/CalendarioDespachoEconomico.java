package cl.bbr.jumbocl.common.model;

import java.io.Serializable;

/**
 * Generar calendario economico
 * @author imoyano
 *
 */
public class CalendarioDespachoEconomico implements Serializable {
	
	private JornadaDespachoEntity jornada;
    private int ventanasDiarias;
	
    /**
     * @return Devuelve jornada.
     */
    public JornadaDespachoEntity getJornada() {
        return jornada;
    }
    /**
     * @return Devuelve ventanasDiarias.
     */
    public int getVentanasDiarias() {
        return ventanasDiarias;
    }
    /**
     * @param jornada El jornada a establecer.
     */
    public void setJornada(JornadaDespachoEntity jornada) {
        this.jornada = jornada;
    }
    /**
     * @param ventanasDiarias El ventanasDiarias a establecer.
     */
    public void setVentanasDiarias(int ventanasDiarias) {
        this.ventanasDiarias = ventanasDiarias;
    }
    
    public void sumaVentanas() {
        this.ventanasDiarias++;
    }
}
