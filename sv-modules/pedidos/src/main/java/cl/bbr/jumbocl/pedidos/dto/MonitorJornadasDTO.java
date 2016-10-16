package cl.bbr.jumbocl.pedidos.dto;
import java.io.Serializable;

public class MonitorJornadasDTO implements Serializable{

	private long		id_jornada;
	private long		id_local;
	private String		hinicio;
	private String		hfin;
	private long		cant_op;
	private long		cant_prods;
	private long		id_estado;
	private String		estado;
	private double		porc_av_prods;
	private double		porc_av_op;
	private String 		fecha;
	//(+) INDRA (+)
	private long		id_producto;
	private double		cant_faltan;;
	private double		cant_spick;
	private long		estado_pedido;
	//(-) INDRA (-)
	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * Constructor
	 * 
	 */
	//(+) INDRA (+)
	public MonitorJornadasDTO(long id_producto , double cant_faltan , double cant_spick ){
	
	this.id_producto =id_producto;
	this.cant_spick = cant_spick;
	this.cant_faltan = cant_faltan;	
	}
	//(-) INDRA (-)
	
	public MonitorJornadasDTO(){
		
	}
	
	public long getCant_op() {
		return cant_op;
	}
	public void setCant_op(long cant_op) {
		this.cant_op = cant_op;
	}
	public long getCant_prods() {
		return cant_prods;
	}
	public void setCant_prods(long cant_prods) {
		this.cant_prods = cant_prods;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getHfin() {
		return hfin;
	}
	public void setHfin(String hfin) {
		this.hfin = hfin;
	}
	public String getHinicio() {
		return hinicio;
	}
	public void setHinicio(String hinicio) {
		this.hinicio = hinicio;
	}
	public long getId_estado() {
		return id_estado;
	}
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}
	public long getId_jornada() {
		return id_jornada;
	}
	public void setId_jornada(long id_jornada) {
		this.id_jornada = id_jornada;
	}
	public long getId_local() {
		return id_local;
	}
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	public double getPorc_av_op() {
		return porc_av_op;
	}
	public void setPorc_av_op(double porc_av_op) {
		this.porc_av_op = porc_av_op;
	}
	public double getPorc_av_prods() {
		return porc_av_prods;
	}
	public void setPorc_av_prods(double porc_av_prods) {
		this.porc_av_prods = porc_av_prods;
	}
//(+) INDRA (+)
	/**
	 * @param estado_pedido El estado_pedido a establecer.
	 */
	public void setEstado_pedido(long estado_pedido) {
		this.estado_pedido = estado_pedido;
	}
	
	/**
	 * @return Devuelve estado_pedido.
	 */
	public long getEstado_pedido() {
		return estado_pedido;
}
	/**
	 * @param id_producto El id_producto a establecer.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}

	/**
	 * @return Devuelve id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}

	/**
	 * @param cant_faltan El cant_faltan a establecer.
	 */
	public void setCant_faltan(double cant_faltan) {
		this.cant_faltan = cant_faltan;
	}

	/**
	 * @return Devuelve cant_faltan.
	 */
	public double getCant_faltan() {
		return cant_faltan;
	}

	/**
	 * @param cant_spick El cant_spick a establecer.
	 */
	public void setCant_spick(double cant_spick) {
		this.cant_spick = cant_spick;
	}

	/**
	 * @return Devuelve cant_spick.
	 */
	public double getCant_spick() {
		return cant_spick;
	}
	//(-) INDRA (-)
	
}
