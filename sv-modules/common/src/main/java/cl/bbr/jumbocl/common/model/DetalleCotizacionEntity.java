package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la Base de Datos los datos de detalle de cotizacion
 * 
 * @author BBR
 *
 */
public class DetalleCotizacionEntity {
	private long 	dcot_id;
	private long 	dcot_cot_id;
	private long 	dcot_pro_id;
	private long 	dcot_pro_id_bo;
	private String 	dcot_cod_prod1;
	private String 	dcot_uni_med;
	private double 	dcot_precio;
	private String 	dcot_descripcion;
	private double 	dcot_qsolic;
	private String 	dcot_obs;
	private String 	dcot_preparable;
	private String 	dcot_pesable;
	private String 	dcot_con_nota;
	private long    dcot_local_despacha_id;
	
	
	public String getDcot_cod_prod1() {
		return dcot_cod_prod1;
	}
	public void setDcot_cod_prod1(String dcot_cod_prod1) {
		this.dcot_cod_prod1 = dcot_cod_prod1;
	}
	public String getDcot_con_nota() {
		return dcot_con_nota;
	}
	public void setDcot_con_nota(String dcot_con_nota) {
		this.dcot_con_nota = dcot_con_nota;
	}
	public long getDcot_cot_id() {
		return dcot_cot_id;
	}
	public void setDcot_cot_id(long dcot_cot_id) {
		this.dcot_cot_id = dcot_cot_id;
	}
	public String getDcot_descripcion() {
		return dcot_descripcion;
	}
	public void setDcot_descripcion(String dcot_descripcion) {
		this.dcot_descripcion = dcot_descripcion;
	}
	public long getDcot_id() {
		return dcot_id;
	}
	public void setDcot_id(long dcot_id) {
		this.dcot_id = dcot_id;
	}
	public long getDcot_local_despacha_id() {
		return dcot_local_despacha_id;
	}
	public void setDcot_local_despacha_id(long dcot_local_despacha_id) {
		this.dcot_local_despacha_id = dcot_local_despacha_id;
	}
	public String getDcot_obs() {
		return dcot_obs;
	}
	public void setDcot_obs(String dcot_obs) {
		this.dcot_obs = dcot_obs;
	}
	public String getDcot_pesable() {
		return dcot_pesable;
	}
	public void setDcot_pesable(String dcot_pesable) {
		this.dcot_pesable = dcot_pesable;
	}
	public double getDcot_precio() {
		return dcot_precio;
	}
	public void setDcot_precio(double dcot_precio) {
		this.dcot_precio = dcot_precio;
	}
	public String getDcot_preparable() {
		return dcot_preparable;
	}
	public void setDcot_preparable(String dcot_preparable) {
		this.dcot_preparable = dcot_preparable;
	}
	public long getDcot_pro_id() {
		return dcot_pro_id;
	}
	public void setDcot_pro_id(long dcot_pro_id) {
		this.dcot_pro_id = dcot_pro_id;
	}
	public long getDcot_pro_id_bo() {
		return dcot_pro_id_bo;
	}
	public void setDcot_pro_id_bo(long dcot_pro_id_bo) {
		this.dcot_pro_id_bo = dcot_pro_id_bo;
	}
	public double getDcot_qsolic() {
		return dcot_qsolic;
	}
	public void setDcot_qsolic(double dcot_qsolic) {
		this.dcot_qsolic = dcot_qsolic;
	}
	public String getDcot_uni_med() {
		return dcot_uni_med;
	}
	public void setDcot_uni_med(String dcot_uni_med) {
		this.dcot_uni_med = dcot_uni_med;
	}
	

}
