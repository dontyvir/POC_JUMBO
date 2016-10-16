package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la Base de Datos los datos de cotizacion
 * 
 * @author BBR
 *
 */
public class CotizacionesEntity {
	
	private long cot_id;
	private long cot_emp_id;
	private long cot_cli_id;
	private long cot_dir_id;
	private long cot_diafac_id;
	private long cot_comprador_id;
	private char cot_tipo_comprador;
	private String cot_fec_ingreso;
	private String cot_fec_acordada;
	private double cot_monto_total;
	private double cot_costo_desp;
	private String cot_mpago;
	private String cot_num_mpago;
	private String cot_clave_mpago;
	private String cot_fec_expira;
	private double cot_descuento;
	private String cot_obs;
	private String cot_estado;
	private String cot_fec_vencimiento;
	/**
	 * @return Returns the cot_clave_mpago.
	 */
	public String getCot_clave_mpago() {
		return cot_clave_mpago;
	}
	/**
	 * @return Returns the cot_cli_id.
	 */
	public long getCot_cli_id() {
		return cot_cli_id;
	}
	/**
	 * @return Returns the cot_comprador_id.
	 */
	public long getCot_comprador_id() {
		return cot_comprador_id;
	}
	/**
	 * @return Returns the cot_costo_desp.
	 */
	public double getCot_costo_desp() {
		return cot_costo_desp;
	}
	/**
	 * @return Returns the cot_descuento.
	 */
	public double getCot_descuento() {
		return cot_descuento;
	}
	/**
	 * @return Returns the cot_diafac_id.
	 */
	public long getCot_diafac_id() {
		return cot_diafac_id;
	}
	/**
	 * @return Returns the cot_dir_id.
	 */
	public long getCot_dir_id() {
		return cot_dir_id;
	}
	/**
	 * @return Returns the cot_emp_id.
	 */
	public long getCot_emp_id() {
		return cot_emp_id;
	}
	/**
	 * @return Returns the cot_estado.
	 */
	public String getCot_estado() {
		return cot_estado;
	}
	/**
	 * @return Returns the cot_fec_acordada.
	 */
	public String getCot_fec_acordada() {
		return cot_fec_acordada;
	}
	/**
	 * @return Returns the cot_fec_expira.
	 */
	public String getCot_fec_expira() {
		return cot_fec_expira;
	}
	/**
	 * @return Returns the cot_fec_ingreso.
	 */
	public String getCot_fec_ingreso() {
		return cot_fec_ingreso;
	}
	/**
	 * @return Returns the cot_id.
	 */
	public long getCot_id() {
		return cot_id;
	}
	/**
	 * @return Returns the cot_monto_total.
	 */
	public double getCot_monto_total() {
		return cot_monto_total;
	}
	/**
	 * @return Returns the cot_mpago.
	 */
	public String getCot_mpago() {
		return cot_mpago;
	}
	/**
	 * @return Returns the cot_num_mpago.
	 */
	public String getCot_num_mpago() {
		return cot_num_mpago;
	}
	/**
	 * @return Returns the cot_obs.
	 */
	public String getCot_obs() {
		return cot_obs;
	}
	/**
	 * @return Returns the cot_tipo_comprador.
	 */
	public char getCot_tipo_comprador() {
		return cot_tipo_comprador;
	}
	/**
	 * @param cot_clave_mpago The cot_clave_mpago to set.
	 */
	public void setCot_clave_mpago(String cot_clave_mpago) {
		this.cot_clave_mpago = cot_clave_mpago;
	}
	/**
	 * @param cot_cli_id The cot_cli_id to set.
	 */
	public void setCot_cli_id(long cot_cli_id) {
		this.cot_cli_id = cot_cli_id;
	}
	/**
	 * @param cot_comprador_id The cot_comprador_id to set.
	 */
	public void setCot_comprador_id(long cot_comprador_id) {
		this.cot_comprador_id = cot_comprador_id;
	}
	/**
	 * @param cot_costo_desp The cot_costo_desp to set.
	 */
	public void setCot_costo_desp(double cot_costo_desp) {
		this.cot_costo_desp = cot_costo_desp;
	}
	/**
	 * @param cot_descuento The cot_descuento to set.
	 */
	public void setCot_descuento(double cot_descuento) {
		this.cot_descuento = cot_descuento;
	}
	/**
	 * @param cot_diafac_id The cot_diafac_id to set.
	 */
	public void setCot_diafac_id(long cot_diafac_id) {
		this.cot_diafac_id = cot_diafac_id;
	}
	/**
	 * @param cot_dir_id The cot_dir_id to set.
	 */
	public void setCot_dir_id(long cot_dir_id) {
		this.cot_dir_id = cot_dir_id;
	}
	/**
	 * @param cot_emp_id The cot_emp_id to set.
	 */
	public void setCot_emp_id(long cot_emp_id) {
		this.cot_emp_id = cot_emp_id;
	}
	/**
	 * @param cot_estado The cot_estado to set.
	 */
	public void setCot_estado(String cot_estado) {
		this.cot_estado = cot_estado;
	}
	/**
	 * @param cot_fec_acordada The cot_fec_acordada to set.
	 */
	public void setCot_fec_acordada(String cot_fec_acordada) {
		this.cot_fec_acordada = cot_fec_acordada;
	}
	/**
	 * @param cot_fec_expira The cot_fec_expira to set.
	 */
	public void setCot_fec_expira(String cot_fec_expira) {
		this.cot_fec_expira = cot_fec_expira;
	}
	/**
	 * @param cot_fec_ingreso The cot_fec_ingreso to set.
	 */
	public void setCot_fec_ingreso(String cot_fec_ingreso) {
		this.cot_fec_ingreso = cot_fec_ingreso;
	}
	/**
	 * @param cot_id The cot_id to set.
	 */
	public void setCot_id(long cot_id) {
		this.cot_id = cot_id;
	}
	/**
	 * @param cot_monto_total The cot_monto_total to set.
	 */
	public void setCot_monto_total(double cot_monto_total) {
		this.cot_monto_total = cot_monto_total;
	}
	/**
	 * @param cot_mpago The cot_mpago to set.
	 */
	public void setCot_mpago(String cot_mpago) {
		this.cot_mpago = cot_mpago;
	}
	/**
	 * @param cot_num_mpago The cot_num_mpago to set.
	 */
	public void setCot_num_mpago(String cot_num_mpago) {
		this.cot_num_mpago = cot_num_mpago;
	}
	/**
	 * @param cot_obs The cot_obs to set.
	 */
	public void setCot_obs(String cot_obs) {
		this.cot_obs = cot_obs;
	}
	/**
	 * @param cot_tipo_comprador The cot_tipo_comprador to set.
	 */
	public void setCot_tipo_comprador(char cot_tipo_comprador) {
		this.cot_tipo_comprador = cot_tipo_comprador;
	}
	/**
	 * @return Returns the cot_fec_vencimiento.
	 */
	public String getCot_fec_vencimiento() {
		return cot_fec_vencimiento;
	}
	/**
	 * @param cot_fec_vencimiento The cot_fec_vencimiento to set.
	 */
	public void setCot_fec_vencimiento(String cot_fec_vencimiento) {
		this.cot_fec_vencimiento = cot_fec_vencimiento;
	}
	
	
	
	
}
