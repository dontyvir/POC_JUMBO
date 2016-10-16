package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class FacturasDTO implements Serializable{
	
	private int    orden;
	private long   id_pedido = 0L;
	private long   id_trxmp = 0L;
	private long   num_doc = 0L;
	private String fingreso;
    private String login;
    private String accion;

    
	public FacturasDTO() {
	}


	
    /**
     * @return Devuelve accion.
     */
    public String getAccion() {
        return accion;
    }
    /**
     * @return Devuelve fingreso.
     */
    public String getFingreso() {
        return fingreso;
    }
    /**
     * @return Devuelve id_pedido.
     */
    public long getId_pedido() {
        return id_pedido;
    }
    /**
     * @return Devuelve id_trxmp.
     */
    public long getId_trxmp() {
        return id_trxmp;
    }
    /**
     * @return Devuelve login.
     */
    public String getLogin() {
        return login;
    }
    /**
     * @return Devuelve num_doc.
     */
    public long getNum_doc() {
        return num_doc;
    }
    /**
     * @return Devuelve orden.
     */
    public int getOrden() {
        return orden;
    }
    /**
     * @param accion El accion a establecer.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
    /**
     * @param fingreso El fingreso a establecer.
     */
    public void setFingreso(String fingreso) {
        this.fingreso = fingreso;
    }
    /**
     * @param id_pedido El id_pedido a establecer.
     */
    public void setId_pedido(long id_pedido) {
        this.id_pedido = id_pedido;
    }
    /**
     * @param id_trxmp El id_trxmp a establecer.
     */
    public void setId_trxmp(long id_trxmp) {
        this.id_trxmp = id_trxmp;
    }
    /**
     * @param login El login a establecer.
     */
    public void setLogin(String login) {
        this.login = login;
    }
    /**
     * @param num_doc El num_doc a establecer.
     */
    public void setNum_doc(long num_doc) {
        this.num_doc = num_doc;
    }
    /**
     * @param orden El orden a establecer.
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }
    
}
