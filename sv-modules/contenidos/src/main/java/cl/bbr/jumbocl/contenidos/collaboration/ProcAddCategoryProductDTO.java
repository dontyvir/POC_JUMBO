package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que maneja la relación entre una categoria y un producto.
 * @author BBR
 *
 */

public class ProcAddCategoryProductDTO implements Serializable {
	private final static long serialVersionUID = 1;
	
    /**
     * Código de la categoria. 
     */
    private long id_categoria; // obligatorio
    
    /**
     * Código del producto. 
     */
    private long id_producto; // obligatorio
    
    /**
     * Estado. 
     */
    private String estado;
    
    /**
     * Orden de aparición en el listado. 
     */
    private int orden;
    
    /**
     * Indicador, con pago. 
     */
    private String con_pago;

    /**
     * Mensaje que se imprime en el log. 
     */
    private String mensaje;
    
    /**
     * Login del usuario que utiliza la clase. 
     */
    private String usr_login;

	/**
	 * Constructor de la clase.
	 * @param id_categoria
	 * @param id_producto
	 * @param estado
	 * @param orden
	 * @param mensaje
	 * @param usr_login
	 */
	public ProcAddCategoryProductDTO(long id_categoria, long id_producto, String estado, int orden, 
			String mensaje, String usr_login) {
		super();
		this.id_categoria = id_categoria;
		this.id_producto = id_producto;
		this.estado = estado;
		this.orden = orden;
		this.mensaje = mensaje;
		this.usr_login = usr_login;
	}
	
	/**
	 * Constructo inicial. 
	 */
	public ProcAddCategoryProductDTO() {
		super();
	}
	
    
	/**
	 * @return Retorna el usr_login.
	 */
	public String getUsr_login() {
		return usr_login;
	}
	/**
	 * @param usr_login , usr_login a modificar.
	 */
	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}
	
	/**
	 * @return Retorna el id_categoria.
	 */
	public long getId_categoria() {
		return id_categoria;
	}
	/**
	 * @param id_categoria , id_categoria a modificar.
	 */
	public void setId_categoria(long id_categoria) {
		this.id_categoria = id_categoria;
	}
	/**
	 * @return Retorna el id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}
	/**
	 * @param id_producto , id_producto a modificar.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	/**
	 * @return Retorna el orden.
	 */
	public int getOrden() {
		return orden;
	}
	/**
	 * @param orden , orden a modificar.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
	/**
	 * @return Retorna el estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado , estado a modificar.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Retorna el mensaje.
	 */
	public String getMensaje() {
		return mensaje;
	}
	/**
	 * @param mensaje , mensaje a modificar.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return Returna con_pago.
	 */
	public String getCon_pago() {
		return con_pago;
	}

	/**
	 * @param con_pago , con_precio a modificar.
	 */
	public void setCon_pago(String con_pago) {
		this.con_pago = con_pago;
	}
	
}
