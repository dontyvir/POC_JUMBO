package cl.bbr.vte.cotizaciones.dto;

/**
 * DTO para datos de los productos de una cotización. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ProductosCotizacionesDTO {
	private long detcot_id;
	private long detcot_cot_id;
	private long detcot_proId;
	private long detcot_pro_id_bo;
	private String detcot_codSap;
	private String detcot_umed;
	private String detcot_desc;
	private double detcot_precio;
	private double  detcot_cantidad;
	private double detcot_dscto_item;
	private double detcot_precio_lista;
	private String detcot_obs;
	private String detcot_preparable;
	private String detcot_pesable;
	private String detcot_con_nota;
	private String detcot_usuario;
	private String 	id_pedido;
	private double	cant_pedido;
	//id del producto en FO
	private long 	detcot_id_fo;
	//campos de fo_productos
	private String pro_tipre;
	private String pro_tipo_producto;
	private String uni_desc;
	private String mar_nombre;
	private double	pre_costo;
	private double intervalo;
	
	
	
	
	/**
	 * @return Returns the pre_costo.
	 */
	public double getPre_costo() {
		return pre_costo;
	}
	/**
	 * @param pre_costo The pre_costo to set.
	 */
	public void setPre_costo(double pre_costo) {
		this.pre_costo = pre_costo;
	}
	/**
	 * @return Returns the detcot_cantidad.
	 */
	public double getDetcot_cantidad() {
		return detcot_cantidad;
	}
	/**
	 * @return Returns the detcot_codSap.
	 */
	public String getDetcot_codSap() {
		return detcot_codSap;
	}
	/**
	 * @return Returns the detcot_desc.
	 */
	public String getDetcot_desc() {
		return detcot_desc;
	}
	/**
	 * @return Returns the detcot_id.
	 */
	public long getDetcot_id() {
		return detcot_id;
	}
	/**
	 * @return Returns the detcot_precio.
	 */
	public double getDetcot_precio() {
		return detcot_precio;
	}
	/**
	 * @return Returns the detcot_proId.
	 */
	public long getDetcot_proId() {
		return detcot_proId;
	}
	/**
	 * @return Returns the detcot_umed.
	 */
	public String getDetcot_umed() {
		return detcot_umed;
	}
	/**
	 * @param detcot_cantidad The detcot_cantidad to set.
	 */
	public void setDetcot_cantidad(double detcot_cantidad) {
		this.detcot_cantidad = detcot_cantidad;
	}
	/**
	 * @param detcot_codSap The detcot_codSap to set.
	 */
	public void setDetcot_codSap(String detcot_codSap) {
		this.detcot_codSap = detcot_codSap;
	}
	/**
	 * @param detcot_desc The detcot_desc to set.
	 */
	public void setDetcot_desc(String detcot_desc) {
		this.detcot_desc = detcot_desc;
	}
	/**
	 * @param detcot_id The detcot_id to set.
	 */
	public void setDetcot_id(long detcot_id) {
		this.detcot_id = detcot_id;
	}
	/**
	 * @param detcot_precio The detcot_precio to set.
	 */
	public void setDetcot_precio(double detcot_precio) {
		this.detcot_precio = detcot_precio;
	}
	/**
	 * @param detcot_proId The detcot_proId to set.
	 */
	public void setDetcot_proId(long detcot_proId) {
		this.detcot_proId = detcot_proId;
	}
	/**
	 * @param detcot_umed The detcot_umed to set.
	 */
	public void setDetcot_umed(String detcot_umed) {
		this.detcot_umed = detcot_umed;
	}
	/**
	 * @return Returns the detcot_dscto_item.
	 */
	public double getDetcot_dscto_item() {
		return detcot_dscto_item;
	}
	/**
	 * @return Returns the detcot_precio_lista.
	 */
	public double getDetcot_precio_lista() {
		return detcot_precio_lista;
	}
	/**
	 * @param detcot_dscto_item The detcot_dscto_item to set.
	 */
	public void setDetcot_dscto_item(double detcot_dscto_item) {
		this.detcot_dscto_item = detcot_dscto_item;
	}
	/**
	 * @param detcot_precio_lista The detcot_precio_lista to set.
	 */
	public void setDetcot_precio_lista(double detcot_precio_lista) {
		this.detcot_precio_lista = detcot_precio_lista;
	}
	/**
	 * @return Returns the detcot_obs.
	 */
	public String getDetcot_obs() {
		return detcot_obs;
	}
	/**
	 * @param detcot_obs The detcot_obs to set.
	 */
	public void setDetcot_obs(String detcot_obs) {
		this.detcot_obs = detcot_obs;
	}
	/**
	 * @return Returns the detcot_con_nota.
	 */
	public String getDetcot_con_nota() {
		return detcot_con_nota;
	}
	/**
	 * @return Returns the detcot_pesable.
	 */
	public String getDetcot_pesable() {
		return detcot_pesable;
	}
	/**
	 * @return Returns the detcot_preparable.
	 */
	public String getDetcot_preparable() {
		return detcot_preparable;
	}
	/**
	 * @return Returns the detcot_pro_id_bo.
	 */
	public long getDetcot_pro_id_bo() {
		return detcot_pro_id_bo;
	}
	/**
	 * @param detcot_con_nota The detcot_con_nota to set.
	 */
	public void setDetcot_con_nota(String detcot_con_nota) {
		this.detcot_con_nota = detcot_con_nota;
	}
	/**
	 * @param detcot_pesable The detcot_pesable to set.
	 */
	public void setDetcot_pesable(String detcot_pesable) {
		this.detcot_pesable = detcot_pesable;
	}
	/**
	 * @param detcot_preparable The detcot_preparable to set.
	 */
	public void setDetcot_preparable(String detcot_preparable) {
		this.detcot_preparable = detcot_preparable;
	}
	/**
	 * @param detcot_pro_id_bo The detcot_pro_id_bo to set.
	 */
	public void setDetcot_pro_id_bo(long detcot_pro_id_bo) {
		this.detcot_pro_id_bo = detcot_pro_id_bo;
	}
	/**
	 * @return Returns the detcot_usuario.
	 */
	public String getDetcot_usuario() {
		return detcot_usuario;
	}
	/**
	 * @param detcot_usuario The detcot_usuario to set.
	 */
	public void setDetcot_usuario(String detcot_usuario) {
		this.detcot_usuario = detcot_usuario;
	}
	/**
	 * @return Returns the detcot_cot_id.
	 */
	public long getDetcot_cot_id() {
		return detcot_cot_id;
	}
	/**
	 * @param detcot_cot_id The detcot_cot_id to set.
	 */
	public void setDetcot_cot_id(long detcot_cot_id) {
		this.detcot_cot_id = detcot_cot_id;
	}
	/**
	 * @return Returns the cant_pedido.
	 */
	public double getCant_pedido() {
		return cant_pedido;
	}
	/**
	 * @return Returns the detcot_id_fo.
	 */
	public long getDetcot_id_fo() {
		return detcot_id_fo;
	}
	/**
	 * @return Returns the id_pedido.
	 */
	public String getId_pedido() {
		return id_pedido;
	}
	/**
	 * @param cant_pedido The cant_pedido to set.
	 */
	public void setCant_pedido(double cant_pedido) {
		this.cant_pedido = cant_pedido;
	}
	/**
	 * @param detcot_id_fo The detcot_id_fo to set.
	 */
	public void setDetcot_id_fo(long detcot_id_fo) {
		this.detcot_id_fo = detcot_id_fo;
	}
	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(String id_pedido) {
		this.id_pedido = id_pedido;
	}
	/**
	 * @return Returns the mar_nombre.
	 */
	public String getMar_nombre() {
		return mar_nombre;
	}
	/**
	 * @param mar_nombre The mar_nombre to set.
	 */
	public void setMar_nombre(String mar_nombre) {
		this.mar_nombre = mar_nombre;
	}
	/**
	 * @return Returns the pro_tipo_producto.
	 */
	public String getPro_tipo_producto() {
		return pro_tipo_producto;
	}
	/**
	 * @param pro_tipo_producto The pro_tipo_producto to set.
	 */
	public void setPro_tipo_producto(String pro_tipo_producto) {
		this.pro_tipo_producto = pro_tipo_producto;
	}
	/**
	 * @return Returns the pro_tipre.
	 */
	public String getPro_tipre() {
		return pro_tipre;
	}
	/**
	 * @param pro_tipre The pro_tipre to set.
	 */
	public void setPro_tipre(String pro_tipre) {
		this.pro_tipre = pro_tipre;
	}
	/**
	 * @return Returns the uni_desc.
	 */
	public String getUni_desc() {
		return uni_desc;
	}
	/**
	 * @param uni_desc The uni_desc to set.
	 */
	public void setUni_desc(String uni_desc) {
		this.uni_desc = uni_desc;
	}
	/**
	 * @return Returns the intervalo.
	 */
	public double getIntervalo() {
		return intervalo;
	}
	/**
	 * @param intervalo The intervalo to set.
	 */
	public void setIntervalo(double intervalo) {
		this.intervalo = intervalo;
	}
	
	
	
}
