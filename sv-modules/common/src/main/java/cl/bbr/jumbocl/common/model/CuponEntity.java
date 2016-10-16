package cl.bbr.jumbocl.common.model;


/**
 * Clase que captura desde la base de datos los datos de la unidad de medida
 * @author bbr
 *
 */
public class CuponEntity {
	
	private long id_cup_dto;
    private long id_usuario;
    private String codigo;
    private String tipo;
    private int descuento;
    private int cantidad;
    private int despacho;
    private int publico;
    private int medio_pago;
    private String fecha_ini;
    private String fecha_fin;
    private String fecha_ins;
    private String fecha_upd;
    private int stock;
    
    private String dato_tipo_desc;
    private String dato_tipo_cod;
    private String nombre_usuario;
    
    
   
	/**
	 * @return el nombre_usuario
	 */
	public String getNombre_usuario() {
		return nombre_usuario;
	}
	/**
	 * @param nombre_usuario el nombre_usuario a establecer
	 */
	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}
	/**
	 * @return el stock
	 */
	public int getStock() {
		return stock;
	}
	/**
	 * @param stock el stock a establecer
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}
	/**
	 * @return el dato_tipo_cod
	 */
	public String getDato_tipo_cod() {
		return dato_tipo_cod;
	}
	/**
	 * @param dato_tipo_cod el dato_tipo_cod a establecer
	 */
	public void setDato_tipo_cod(String dato_tipo_cod) {
		this.dato_tipo_cod = dato_tipo_cod;
	}
	/**
	 * @return el dato_tipo_desc
	 */
	public String getDato_tipo_desc() {
		return dato_tipo_desc;
	}
	/**
	 * @param dato_tipo_desc el dato_tipo_desc a establecer
	 */
	public void setDato_tipo_desc(String dato_tipo_desc) {
		this.dato_tipo_desc = dato_tipo_desc;
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
	/**
	 * @return el id_usuario
	 */
	public long getId_usuario() {
		return id_usuario;
	}
	/**
	 * @param id_usuario el id_usuario a establecer
	 */
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}
	/**
	 * @return el codigo
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo el codigo a establecer
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return el tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo el tipo a establecer
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return el descuento
	 */
	public int getDescuento() {
		return descuento;
	}
	/**
	 * @param descuento el descuento a establecer
	 */
	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}
	/**
	 * @return el cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad el cantidad a establecer
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @return el despacho
	 */
	public int getDespacho() {
		return despacho;
	}
	/**
	 * @param despacho el despacho a establecer
	 */
	public void setDespacho(int despacho) {
		this.despacho = despacho;
	}
	/**
	 * @return el publico
	 */
	public int getPublico() {
		return publico;
	}
	/**
	 * @param publico el publico a establecer
	 */
	public void setPublico(int publico) {
		this.publico = publico;
	}
	/**
	 * @return el medio_pago
	 */
	public int getMedio_pago() {
		return medio_pago;
	}
	/**
	 * @param medio_pago el medio_pago a establecer
	 */
	public void setMedio_pago(int medio_pago) {
		this.medio_pago = medio_pago;
	}
	/**
	 * @return el fecha_ini
	 */
	public String getFecha_ini() {
		return fecha_ini;
	}
	/**
	 * @param fecha_ini el fecha_ini a establecer
	 */
	public void setFecha_ini(String fecha_ini) {
		this.fecha_ini = fecha_ini;
	}
	/**
	 * @return el fecha_fin
	 */
	public String getFecha_fin() {
		return fecha_fin;
	}
	/**
	 * @param fecha_fin el fecha_fin a establecer
	 */
	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	/**
	 * @return el fecha_ins
	 */
	public String getFecha_ins() {
		return fecha_ins;
	}
	/**
	 * @param fecha_ins el fecha_ins a establecer
	 */
	public void setFecha_ins(String fecha_ins) {
		this.fecha_ins = fecha_ins;
	}
	/**
	 * @return el fecha_upd
	 */
	public String getFecha_upd() {
		return fecha_upd;
	}
	/**
	 * @param fecha_upd el fecha_upd a establecer
	 */
	public void setFecha_upd(String fecha_upd) {
		this.fecha_upd = fecha_upd;
	}
   
    
	
	
	
	

}
