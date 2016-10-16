/*
 * Created on 05-11-2012
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * @author Administrador
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProcDelAllProductCategory implements Serializable {
    
    private long id_categoria; // obligatorio
    private String usr_login;
    private String mensaje;
    
    public ProcDelAllProductCategory() {
        super();
    }
    public ProcDelAllProductCategory(long id_categoria , String usr_login , String mensaje) {
        super();
        this.id_categoria = id_categoria;
        this.usr_login = usr_login;
        this.mensaje = mensaje;
    }

    /**
     * @return Returns the id_categoria.
     */
    public long getId_categoria() {
        return id_categoria;
    }
    /**
     * @param id_categoria The id_categoria to set.
     */
    public void setId_categoria(long id_categoria) {
        this.id_categoria = id_categoria;
    }
    /**
     * @return Returns the mensaje.
     */
    public String getMensaje() {
        return mensaje;
    }
    /**
     * @param mensaje The mensaje to set.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    /**
     * @return Returns the usr_login.
     */
    public String getUsr_login() {
        return usr_login;
    }
    /**
     * @param usr_login The usr_login to set.
     */
    public void setUsr_login(String usr_login) {
        this.usr_login = usr_login;
    }
}
