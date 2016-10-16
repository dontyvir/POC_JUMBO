package cl.bbr.vte.cotizaciones.dto;

/**
 * Clase que contiene información de la cotización
 * 
 * @author BBR
 *
 */
public class ProcInsDetCotizacionDTO {

	private long 	cot_id = 0;
	private long 	pro_id = 0;
	private long 	pro_id_bo = 0;
	private String 	cod_prod1 = "";
	private String 	uni_med = "";
	private double 	precio = 0.0;
	private String 	descripcion = "";
	private double 	qsolic = 0.0;
	private String 	obs = "";
	private String 	preparable = "";
	private String 	pesable = "";
	private String 	con_nota = "";
	private double	dscto_item = 0.0;
	private double	precio_lista = 0.0;
	
	public String toString() {
		String result = "";
		result += " cot_id: " + this.cot_id;
		result += " pro_id: " + this.pro_id;
		result += " pro_id_bo: " + this.pro_id_bo;
		result += " cod_prod1: " + this.cod_prod1;
		result += " uni_med: " + this.uni_med;
		result += " precio: " + this.precio;
		result += " descripcion: " + this.descripcion;
		result += " qsolic: " + this.qsolic;
		result += " obs: " + this.obs;
		result += " preparable: " + this.preparable;
		result += " pesable: " + this.pesable;
		result += " con_nota: " + this.con_nota;
		result += " dscto_item: " + this.dscto_item;
		result += " precio_lista: " + this.precio_lista;
		
		return result;
	}
	
	public long getCot_id() {
		return cot_id;
	}
	public void setCot_id(long cot_id) {
		this.cot_id = cot_id;
	}
	public String getCod_prod1() {
		return cod_prod1;
	}
	public void setCod_prod1(String cod_prod1) {
		this.cod_prod1 = cod_prod1;
	}
	public String getCon_nota() {
		return con_nota;
	}
	public void setCon_nota(String con_nota) {
		this.con_nota = con_nota;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public double getDscto_item() {
		return dscto_item;
	}
	public void setDscto_item(double dscto_item) {
		this.dscto_item = dscto_item;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public String getPesable() {
		return pesable;
	}
	public void setPesable(String pesable) {
		this.pesable = pesable;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public double getPrecio_lista() {
		return precio_lista;
	}
	public void setPrecio_lista(double precio_lista) {
		this.precio_lista = precio_lista;
	}
	public String getPreparable() {
		return preparable;
	}
	public void setPreparable(String preparable) {
		this.preparable = preparable;
	}
	public long getPro_id() {
		return pro_id;
	}
	public void setPro_id(long pro_id) {
		this.pro_id = pro_id;
	}
	public long getPro_id_bo() {
		return pro_id_bo;
	}
	public void setPro_id_bo(long pro_id_bo) {
		this.pro_id_bo = pro_id_bo;
	}
	public double getQsolic() {
		return qsolic;
	}
	public void setQsolic(double qsolic) {
		this.qsolic = qsolic;
	}
	public String getUni_med() {
		return uni_med;
	}
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
	
}
