package cl.bbr.jumbocl.clientes.model;

import java.sql.Timestamp;

/**
 * @author BBRI
 *  
 */
public class CarroComprasEntity {

	private Long id;
	private String pro_id;
	private Long cli_id;
	private Long cantidad;
	private String nota;
	private Timestamp fec_crea;
	private Character tipo_sel;

	/**
	 * Constructor
	 */
	public CarroComprasEntity() {

	}

	public Long getCantidad() {
		return cantidad;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}

	public Long getCli_id() {
		return cli_id;
	}

	public void setCli_id(Long cli_id) {
		this.cli_id = cli_id;
	}

	public Timestamp getFec_crea() {
		return fec_crea;
	}

	public void setFec_crea(Timestamp fec_crea) {
		this.fec_crea = fec_crea;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getPro_id() {
		return pro_id;
	}

	public void setPro_id(String pro_id) {
		this.pro_id = pro_id;
	}

	public Character getTipo_sel() {
		return tipo_sel;
	}

	public void setTipo_sel(Character tipo_sel) {
		this.tipo_sel = tipo_sel;
	}
	
}