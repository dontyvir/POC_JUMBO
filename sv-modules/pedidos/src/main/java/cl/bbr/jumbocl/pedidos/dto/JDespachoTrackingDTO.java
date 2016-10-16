package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class JDespachoTrackingDTO implements Serializable{

	private String	h_ini;
	private String	h_fin;
	

    /**
     * @return Devuelve h_fin.
     */
    public String getH_fin() {
        return h_fin;
    }
    /**
     * @return Devuelve h_ini.
     */
    public String getH_ini() {
        return h_ini;
    }
    /**
     * @param h_fin El h_fin a establecer.
     */
    public void setH_fin(String h_fin) {
        this.h_fin = h_fin;
    }
    /**
     * @param h_ini El h_ini a establecer.
     */
    public void setH_ini(String h_ini) {
        this.h_ini = h_ini;
    }
}
