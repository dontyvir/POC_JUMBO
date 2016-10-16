package cl.bbr.jumbocl.pedidos.dto;

/**
 * @author imoyano
 *
 */
public class CriterioCarruselDTO {

    private long idProductoFo;
    private String fcInicio;
    private String fcTermino;
    private String fcCreacion;
    private int pag;
    private int regsperpage;
    
    public CriterioCarruselDTO(long idProductoFo, String fcInicio, String fcTermino, String fcCreacion, int pag, int regsperpage) {
        this.idProductoFo = idProductoFo;
        this.fcInicio = fcInicio;
        this.fcTermino = fcTermino;
        this.fcCreacion = fcCreacion;
        this.pag = pag;
        this.regsperpage = regsperpage;
    }    
    
    /**
     * @return Devuelve fcCreacion.
     */
    public String getFcCreacion() {
        return fcCreacion;
    }
    /**
     * @return Devuelve fcInicio.
     */
    public String getFcInicio() {
        return fcInicio;
    }
    /**
     * @return Devuelve fcTermino.
     */
    public String getFcTermino() {
        return fcTermino;
    }
    /**
     * @return Devuelve idProducto.
     */
    public long getIdProductoFo() {
        return idProductoFo;
    }
    /**
     * @return Devuelve pag.
     */
    public int getPag() {
        return pag;
    }
    /**
     * @return Devuelve regsperpage.
     */
    public int getRegsperpage() {
        return regsperpage;
    }
    /**
     * @param fcCreacion El fcCreacion a establecer.
     */
    public void setFcCreacion(String fcCreacion) {
        this.fcCreacion = fcCreacion;
    }
    /**
     * @param fcInicio El fcInicio a establecer.
     */
    public void setFcInicio(String fcInicio) {
        this.fcInicio = fcInicio;
    }
    /**
     * @param fcTermino El fcTermino a establecer.
     */
    public void setFcTermino(String fcTermino) {
        this.fcTermino = fcTermino;
    }
    /**
     * @param idProducto El idProducto a establecer.
     */
    public void setIdProductoFo(long idProductoFo) {
        this.idProductoFo = idProductoFo;
    }
    /**
     * @param pag El pag a establecer.
     */
    public void setPag(int pag) {
        this.pag = pag;
    }
    /**
     * @param regsperpage El regsperpage a establecer.
     */
    public void setRegsperpage(int regsperpage) {
        this.regsperpage = regsperpage;
    }
}
