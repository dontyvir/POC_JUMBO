package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

/**
 * DTO para datos de las comunas. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ComunaDTO implements Serializable {

	private static final long serialVersionUID = -3032407996676160113L;
	
	private long id;
	private long reg_id;
	private String nombre;
    private long zona_id;
    private long local_id;

	/**
	 * Constructor
	 */
	public ComunaDTO() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getReg_id() {
		return reg_id;
	}

	public void setReg_id(long reg_id) {
		this.reg_id = reg_id;
	}
		
    /**
     * @return Devuelve local_id.
     */
    public long getLocal_id() {
        return local_id;
    }
    /**
     * @return Devuelve zona_id.
     */
    public long getZona_id() {
        return zona_id;
    }
    /**
     * @param local_id El local_id a establecer.
     */
    public void setLocal_id(long local_id) {
        this.local_id = local_id;
    }
    /**
     * @param zona_id El zona_id a establecer.
     */
    public void setZona_id(long zona_id) {
        this.zona_id = zona_id;
    }
}