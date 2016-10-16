package cl.bbr.boc.dto;

public class BOSeccionDTO {
	
	private int id_seccion;
	private String nombre;
	
	public BOSeccionDTO() {
	}

	public BOSeccionDTO(int id_seccion, String nombre) {
		this.id_seccion = id_seccion;
		this.nombre = nombre;
	}
	
	public int getId_seccion() {
		return id_seccion;
	}
	
	public void setId_seccion(int id_seccion) {
		this.id_seccion = id_seccion;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String toString() {
		return id_seccion + " " + nombre;
	}
}
