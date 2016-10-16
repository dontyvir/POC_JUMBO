/*
 * Creado el 22-oct-2008
 *
 */
package cl.bbr.jumbocl.pedidos.dto;

/**
 * @author imoyano
 *
 */
public class CriterioSustitutoDTO {

    private long idProducto;
    private int idCriterio;
    private String descCriterio;
    private String sustitutoCliente;
    
    public CriterioSustitutoDTO() {
        this.idCriterio = 1;
        this.descCriterio = "";
    }
    
    /**
     * @return Devuelve descCriterio.
     */
    public String getDescCriterio() {
        return descCriterio;
    }
    /**
     * @return Devuelve idCriterio.
     */
    public int getIdCriterio() {
        return idCriterio;
    }
    /**
     * @param descCriterio El descCriterio a establecer.
     */
    public void setDescCriterio(String descCriterio) {
        this.descCriterio = descCriterio;
    }
    /**
     * @param idCriterio El idCriterio a establecer.
     */
    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }
    /**
     * @return Devuelve idProducto.
     */
    public long getIdProducto() {
        return idProducto;
    }
    /**
     * @return Devuelve sustitutoCliente.
     */
    public String getSustitutoCliente() {
        return sustitutoCliente;
    }
    /**
     * @param idProducto El idProducto a establecer.
     */
    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }
    /**
     * @param sustitutoCliente El sustitutoCliente a establecer.
     */
    public void setSustitutoCliente(String sustitutoCliente) {
        this.sustitutoCliente = sustitutoCliente;
    }
}
