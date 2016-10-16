package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que contiene la información de la relación (a eliminar) entre la categoría Web y el producto Web.
 * @author BBR
 *
 */
public class ProcDelCategoryProductDTO implements Serializable{
	
    /**
     * Id de categoría Web.
     */
    private long id_categoria; // obligatorio
    
    /**
     * Id del producto Web. 
     */
    private long id_producto; // obligatorio
    
    /**
     * Login del usuario. 
     */
    private String usr_login;
    
    /**
     * Mensaje a imprimir en el log. 
     */
    private String mensaje;
    
	/**
	 * Constructor inicial.
	 */
	public ProcDelCategoryProductDTO() {
		super();
	}
	
	
	/**
	 * Constructor.
	 * @param id_categoria
	 * @param id_producto
	 * @param usr_login
	 * @param mensaje
	 */
	public ProcDelCategoryProductDTO(long id_categoria , long id_producto , String usr_login , String mensaje) {
		super();
		this.id_categoria = id_categoria;
		this.id_producto = id_producto;
		this.usr_login = usr_login;
		this.mensaje = mensaje;
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

}
