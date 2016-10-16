package cl.bbr.jumbocl.pedidos.dto;

/**
 * @author imoyano
 *
 */
public class ProductoSinStockDTO {

    private long idProductoFo;
    private String nombreLocal;
    
    public ProductoSinStockDTO() {
    }
    
    /**
     * @return Devuelve idProductoFo.
     */
    public long getIdProductoFo() {
        return idProductoFo;
    }
    /**
     * @return Devuelve nombre local.
     */
    public String getNombreLocal() {
        return nombreLocal;
    }
    /**
     * @param idProductoFo El idProductoFo a establecer.
     */
    public void setIdProductoFo(long idProductoFo) {
        this.idProductoFo = idProductoFo;
    }
    /**
     * @param nombreLocal El nombre del local a establecer.
     */
    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }
}
