package cl.bbr.jumbocl.contenidos.dto;

/**
 * Clase que muestra informacion de motivos de despublicacion.
 * 
 * @author BBR
 *
 */
public class PedidosDTO {

	/**
	 * Id del pedido 
	 */
	private int id_pedido;
	
	/**
	 * Id del local 
	 */
	private int id_local;
	
	/**
	 * Nombre del local
	 */
	private String nom_local;
	
	/**
	 * Fecha de creacion
	 */
	private String fec_crea;
	
	/**
	 * Id de estado
	 */
	private int id_estado;
	
	/**
	 * Nombre de estado
	 */
	private String nom_est;
	
	/**
	 * Id del motivo
	 */
	private int id_mot;
	
	/**
	 * Nombre del motivo
	 */
	private String nom_mot;
	
	/**
	 * Monto del pedido
	 */
	private double monto_ped;
	
	/**
	 * Total de productos
	 */
	private int tot_prod;
	
	/**
	 * Nombre del cliente
	 */
	private  String nom_cliente;
	
	/**
	 * Direccion
	 */
	private String direccion;
	
	/**
	 * Telefono
	 */
	private String fono;
	
	/**
	 * Telefono 2
	 */
	private String fono2;
	
	/**
	 * Indicacion
	 */
	private String indicacion;
	
	/**
	 * Fecha de despacho
	 */
	private String fec_des;
	
	/**
	 * Costo de despacho
	 */
	private double costo_desp;
	
	/**
	 * Id del usuario
	 */
	private int id_usuario;
	
	/**
	 * Medio de pago
	 */
	private String medio_pago;
	
	/**
	 * Número de medio de pago
	 */
	private String num_mp;
	
	
	/**
	 * @return costo_desp
	 */
	public double getCosto_desp() {
		return costo_desp;
	}
	
	/**
	 * @return direccion
	 */
	public String getDireccion() {
		return direccion;
	}
	
	/**
	 * @return fec_crea
	 */
	public String getFec_crea() {
		return fec_crea;
	}
	
	/**
	 * @return fec_des
	 */
	public String getFec_des() {
		return fec_des;
	}
	
	/**
	 * @return fono
	 */
	public String getFono() {
		return fono;
	}
	
	/**
	 * @return fono2
	 */
	public String getFono2() {
		return fono2;
	}
	
	/**
	 * @return id_estado
	 */
	public int getId_estado() {
		return id_estado;
	}
	
	/**
	 * @return id_local
	 */
	public int getId_local() {
		return id_local;
	}
	
	/**
	 * @return id_mot
	 */
	public int getId_mot() {
		return id_mot;
	}
	
	/**
	 * @return id_pedido
	 */
	public int getId_pedido() {
		return id_pedido;
	}
	
	/**
	 * @return id_usuario
	 */
	public int getId_usuario() {
		return id_usuario;
	}
	
	/**
	 * @return indicacion
	 */
	public String getIndicacion() {
		return indicacion;
	}
	
	/**
	 * @return medio_pago
	 */
	public String getMedio_pago() {
		return medio_pago;
	}
	
	/**
	 * @return monto_ped
	 */
	public double getMonto_ped() {
		return monto_ped;
	}
	
	/**
	 * @return nom_cliente
	 */
	public String getNom_cliente() {
		return nom_cliente;
	}
	
	/**
	 * @return nom_est
	 */
	public String getNom_est() {
		return nom_est;
	}
	
	/**
	 * @return nom_local
	 */
	public String getNom_local() {
		return nom_local;
	}
	
	/**
	 * @return nom_mot
	 */
	public String getNom_mot() {
		return nom_mot;
	}
	
	/**
	 * @return num_mp
	 */
	public String getNum_mp() {
		return num_mp;
	}
	
	/**
	 * @return tot_prod
	 */
	public int getTot_prod() {
		return tot_prod;
	}
	
	/**
	 * @param costo_desp , costo_desp a modificar.
	 */
	public void setCosto_desp(double costo_desp) {
		this.costo_desp = costo_desp;
	}
	
	/**
	 * @param direccion , direccion a modificar.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	/**
	 * @param fec_crea , fec_crea a modificar.
	 */
	public void setFec_crea(String fec_crea) {
		this.fec_crea = fec_crea;
	}
	
	/**
	 * @param fec_des , fec_des a modificar.
	 */
	public void setFec_des(String fec_des) {
		this.fec_des = fec_des;
	}
	
	/**
	 * @param fono , fono a modificar.
	 */
	public void setFono(String fono) {
		this.fono = fono;
	}
	
	/**
	 * @param fono2 , fono2 a modificar.
	 */
	public void setFono2(String fono2) {
		this.fono2 = fono2;
	}
	
	/**
	 * @param id_estado , id_estado a modificar.
	 */
	public void setId_estado(int id_estado) {
		this.id_estado = id_estado;
	}
	
	/**
	 * @param id_local , id_local a modificar.
	 */
	public void setId_local(int id_local) {
		this.id_local = id_local;
	}
	
	/**
	 * @param id_mot , id_mot a modificar.
	 */
	public void setId_mot(int id_mot) {
		this.id_mot = id_mot;
	}
	
	/**
	 * @param id_pedido , id_pedido a modificar.
	 */
	public void setId_pedido(int id_pedido) {
		this.id_pedido = id_pedido;
	}
	
	/**
	 * @param id_usuario , id_usuario a modificar.
	 */
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}
	
	/**
	 * @param indicacion , indicacion a modificar.
	 */
	public void setIndicacion(String indicacion) {
		this.indicacion = indicacion;
	}
	
	/**
	 * @param medio_pago , medio_pago a modificar.
	 */
	public void setMedio_pago(String medio_pago) {
		this.medio_pago = medio_pago;
	}
	
	/**
	 * @param monto_ped , monto_ped a modificar.
	 */
	public void setMonto_ped(double monto_ped) {
		this.monto_ped = monto_ped;
	}
	
	/**
	 * @param nom_cliente , nom_cliente a modificar.
	 */
	public void setNom_cliente(String nom_cliente) {
		this.nom_cliente = nom_cliente;
	}
	
	/**
	 * @param nom_est , nom_est a modificar.
	 */
	public void setNom_est(String nom_est) {
		this.nom_est = nom_est;
	}
	
	/**
	 * @param nom_local , nom_local a modificar.
	 */
	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}
	
	/**
	 * @param nom_mot , nom_mot a modificar.
	 */
	public void setNom_mot(String nom_mot) {
		this.nom_mot = nom_mot;
	}
	
	/**
	 * @param num_mp , num_mp a modificar.
	 */
	public void setNum_mp(String num_mp) {
		this.num_mp = num_mp;
	}
	
	/**
	 * @param tot_prod , tot_prod a modificar.
	 */
	public void setTot_prod(int tot_prod) {
		this.tot_prod = tot_prod;
	}
		
}
