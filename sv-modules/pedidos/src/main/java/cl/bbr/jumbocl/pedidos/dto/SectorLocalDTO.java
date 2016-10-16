package cl.bbr.jumbocl.pedidos.dto;

public class SectorLocalDTO {

	private long	id_sector;
	private long	id_local;
	private String	nombre;
	private String nombre_local;
	private long    max_prod;
	private long    max_op;
	private long    min_op_fill;
	private long    cant_min_prods;
	
		
	/**
	 * @return Returns the cant_min_prods.
	 */
	public long getCant_min_prods() {
		return cant_min_prods;
	}
	/**
	 * @param cant_min_prods The cant_min_prods to set.
	 */
	public void setCant_min_prods(long cant_min_prods) {
		this.cant_min_prods = cant_min_prods;
	}
	public String getNombre_local() {
		return nombre_local;
	}
	public void setNombre_local(String nombre_local) {
		this.nombre_local = nombre_local;
	}
	public long getId_local() {
		return id_local;
	}
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	public long getId_sector() {
		return id_sector;
	}
	public void setId_sector(long id_sector) {
		this.id_sector = id_sector;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public long getMax_op() {
		return max_op;
	}
	public void setMax_op(long max_op) {
		this.max_op = max_op;
	}
	public long getMax_prod() {
		return max_prod;
	}
	public void setMax_prod(long max_prod) {
		this.max_prod = max_prod;
	}
	public long getMin_op_fill() {
		return min_op_fill;
	}
	public void setMin_op_fill(long min_op_fill) {
		this.min_op_fill = min_op_fill;
	}

	
}
