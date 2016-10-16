package cl.bbr.jumbocl.productos.dto;

import java.io.Serializable;

public class MarcaDTO implements Serializable, Comparable {

	private long mar_id;
	private String mar_nombre;
	
	/**
	 * Constructor
	 */
	public MarcaDTO() {
	}

	public long getMar_id() {
		return mar_id;
	}

	public void setMar_id(long mar_id) {
		this.mar_id = mar_id;
	}

	public String getMar_nombre() {
		return mar_nombre;
	}

	public void setMar_nombre(String mar_nombre) {
		this.mar_nombre = mar_nombre;
	}

    /**
     *(J)Para ordenar por nombre de marca
     */
    public int compareTo(Object o) {
        return this.mar_nombre.compareTo(((MarcaDTO)o).getMar_nombre());
    }
}