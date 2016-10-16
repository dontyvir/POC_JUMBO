package cl.bbr.jumbocl.common.model;

public class CuponPorProducto {
	
	private long id_cup_dto;
	private long id_prodRubSecc;
	
	private long id_producto;
	private long id_rubro;
	private long id_seccion;
	
	private String nombreProdRubSecc;
	
	
	
	
	
	/**
	 * @return el id_producto
	 */
	public long getId_producto() {
		return id_producto;
	}
	/**
	 * @param id_producto el id_producto a establecer
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	/**
	 * @return el id_rubro
	 */
	public long getId_rubro() {
		return id_rubro;
	}
	/**
	 * @param id_rubro el id_rubro a establecer
	 */
	public void setId_rubro(long id_rubro) {
		this.id_rubro = id_rubro;
	}
	/**
	 * @return el id_seccion
	 */
	public long getId_seccion() {
		return id_seccion;
	}
	/**
	 * @param id_seccion el id_seccion a establecer
	 */
	public void setId_seccion(long id_seccion) {
		this.id_seccion = id_seccion;
	}
	/**
	 * @return el id_prodRubSecc
	 */
	public long getId_prodRubSecc() {
		return id_prodRubSecc;
	}
	/**
	 * @param id_prodRubSecc el id_prodRubSecc a establecer
	 */
	public void setId_prodRubSecc(long id_prodRubSecc) {
		this.id_prodRubSecc = id_prodRubSecc;
	}
	/**
	 * @return el nombreProdRubSecc
	 */
	public String getNombreProdRubSecc() {
		return nombreProdRubSecc;
	}
	/**
	 * @param nombreProdRubSecc el nombreProdRubSecc a establecer
	 */
	public void setNombreProdRubSecc(String nombreProdRubSecc) {
		this.nombreProdRubSecc = nombreProdRubSecc;
	}
	/**
	 * @return el id_cup_dto
	 */
	public long getId_cup_dto() {
		return id_cup_dto;
	}
	/**
	 * @param id_cup_dto el id_cup_dto a establecer
	 */
	public void setId_cup_dto(long id_cup_dto) {
		this.id_cup_dto = id_cup_dto;
	}
	
	
	
	

}
