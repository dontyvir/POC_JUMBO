package cl.bbr.jumbocl.common.model;

/**
 * Clase que Captura desde la Base de Datos los datos del Bins de un pedido
 * @author bbr
 *
 */
public class BinsPedidoEntity {
	private int id_bp;
	private int id_ronda;
	private int id_pedido;
	private int cod_bin;
	private int cod_sello1;
	private int cod_sello2;
	private int cod_ubicacion;
	
	/**
	 * Constructor
	 */
	public BinsPedidoEntity() {
	}

	/**
	 * @param id_bp
	 * @param id_ronda
	 * @param id_pedido
	 * @param cod_bin
	 * @param cod_sello1
	 * @param cod_sello2
	 * @param cod_ubicacion
	 */
	public BinsPedidoEntity(int id_bp, int id_ronda, int id_pedido, int cod_bin, int cod_sello1, int cod_sello2, int cod_ubicacion) {
		this.id_bp = id_bp;
		this.id_ronda = id_ronda;
		this.id_pedido = id_pedido;
		this.cod_bin = cod_bin;
		this.cod_sello1 = cod_sello1;
		this.cod_sello2 = cod_sello2;
		this.cod_ubicacion = cod_ubicacion;
	}

	/**
	 * @return Returns the cod_bin.
	 */
	public int getCod_bin() {
		return cod_bin;
	}

	/**
	 * @return Returns the cod_sello1.
	 */
	public int getCod_sello1() {
		return cod_sello1;
	}

	/**
	 * @return Returns the cod_sello2.
	 */
	public int getCod_sello2() {
		return cod_sello2;
	}

	/**
	 * @return Returns the cod_ubicacion.
	 */
	public int getCod_ubicacion() {
		return cod_ubicacion;
	}

	/**
	 * @return Returns the id_bp.
	 */
	public int getId_bp() {
		return id_bp;
	}

	/**
	 * @return Returns the id_pedido.
	 */
	public int getId_pedido() {
		return id_pedido;
	}

	/**
	 * @return Returns the id_ronda.
	 */
	public int getId_ronda() {
		return id_ronda;
	}

	/**
	 * @param cod_bin The cod_bin to set.
	 */
	public void setCod_bin(int cod_bin) {
		this.cod_bin = cod_bin;
	}

	/**
	 * @param cod_sello1 The cod_sello1 to set.
	 */
	public void setCod_sello1(int cod_sello1) {
		this.cod_sello1 = cod_sello1;
	}

	/**
	 * @param cod_sello2 The cod_sello2 to set.
	 */
	public void setCod_sello2(int cod_sello2) {
		this.cod_sello2 = cod_sello2;
	}

	/**
	 * @param cod_ubicacion The cod_ubicacion to set.
	 */
	public void setCod_ubicacion(int cod_ubicacion) {
		this.cod_ubicacion = cod_ubicacion;
	}

	/**
	 * @param id_bp The id_bp to set.
	 */
	public void setId_bp(int id_bp) {
		this.id_bp = id_bp;
	}

	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(int id_pedido) {
		this.id_pedido = id_pedido;
	}

	/**
	 * @param id_ronda The id_ronda to set.
	 */
	public void setId_ronda(int id_ronda) {
		this.id_ronda = id_ronda;
	}
	


}
