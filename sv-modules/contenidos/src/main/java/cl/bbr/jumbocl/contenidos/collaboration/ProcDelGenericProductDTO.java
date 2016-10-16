package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;

/**
 * Clase que contiene la información de eliminar un producto genérico.
 * @author BBR
 *
 */
public class ProcDelGenericProductDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
    /**
     * Id del producto. 
     */
    private long id_producto; // obligatorio
    
    /**
     * Clase que contiene el log del producto 
     */
    private ProductoLogDTO log;
    
    /**
     * Mensaje de eliminación que se muestra en el log. 
     */
    private String men_elim;
    
    /**
     * Login del usuario. 
     */
    private String usr_login;

     
	/**
	 * @param id_producto
	 * @param log
	 * @param men_elim
	 * @param usr_login
	 */
	public ProcDelGenericProductDTO(long id_producto , ProductoLogDTO log , String men_elim , String usr_login) {
		super();
		this.id_producto = id_producto;
		this.log = log;
		this.men_elim = men_elim;
		this.usr_login = usr_login;
	}

	/**
	 * @return Retorna el men_elim.
	 */
	public String getMen_elim() {
		return men_elim;
	}

	/**
	 * @param men_elim , men_elim a modificar.
	 */
	public void setMen_elim(String men_elim) {
		this.men_elim = men_elim;
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
	 * 
	 */
	public ProcDelGenericProductDTO() {
	}

	/**
	 * @param id_producto
	 */
	public ProcDelGenericProductDTO(long id_producto) {
		this.id_producto = id_producto;
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
	 * @return Retorna el log.
	 */
	public ProductoLogDTO getLog() {
		return log;
	}

	/**
	 * @param log , log a modificar.
	 */
	public void setLog(ProductoLogDTO log) {
		this.log = log;
	}
     
	
}
