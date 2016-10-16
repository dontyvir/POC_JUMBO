package cl.bbr.vte.cotizaciones.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO para datos de las categorías. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CategoriaDTO implements Serializable {

	private long id;
	private long id_padre;
	private String nombre;
	private String descripcion;
	private String banner;
	private String totem;
	private String tipo;
	private long ranking;
	private List categorias = null;
	private long id_marca;
	private String nombre_marca;
	private String subcat;
	private long cant_productos = 0;
	/**
	 * Constructor
	 */
	public CategoriaDTO() {
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId_padre() {
		return id_padre;
	}
	public void setId_padre(long id_padre) {
		this.id_padre = id_padre;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getBanner() {
		return banner;
	}
	public void setBanner(String banner) {
		this.banner = banner;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public List getCategorias() {
		return categorias;
	}
	public void setCategorias(List categorias) {
		this.categorias = categorias;
	}
	public long getRanking() {
		return ranking;
	}
	public void setRanking(long ranking) {
		this.ranking = ranking;
	}
	public long getId_marca() {
		return id_marca;
	}
	public void setId_marca(long id_marca) {
		this.id_marca = id_marca;
	}
	public String getNombre_marca() {
		return nombre_marca;
	}
	public void setNombre_marca(String nombre_marca) {
		this.nombre_marca = nombre_marca;
	}
	public String getSubcat() {
		return subcat;
	}
	public void setSubcat(String subcat) {
		this.subcat = subcat;
	}
	public long getCant_productos() {
		return cant_productos;
	}
	public void setCant_productos(long cant_productos) {
		this.cant_productos = cant_productos;
	}
	public String getTotem() {
		return totem;
	}
	public void setTotem(String totem) {
		this.totem = totem;
	}
}