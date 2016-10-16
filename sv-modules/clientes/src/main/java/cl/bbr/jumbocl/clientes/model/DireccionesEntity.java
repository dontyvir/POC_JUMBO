package cl.bbr.jumbocl.clientes.model;

import java.sql.Timestamp;


/**
 * @author BBRI
 * 
 */
public class DireccionesEntity {

	private Long id;
	private Long loc_cod;
	private Long com_id;
	private Long tip_id;
	private Long tipo_calle;
	private Long cli_id;
	private String alias;
	private String calle;
	private String numero;
	private String depto;
	private String comentarios;
	private Character estado;
	private Timestamp fec_crea;
	private Character nueva;
	private Long reg_id;

	/**
	 * Constructor
	 */
	public DireccionesEntity() {

	}

	public DireccionesEntity(Long id, Long loc_cod, Long com_id, Long tipo_calle, Long cli_id, String alias, String calle, String numero, String depto, String comentarios, Timestamp fec_crea, Character estado) {
		this.id = id;
		this.loc_cod = loc_cod;
		this.com_id = com_id;
		this.tipo_calle = tipo_calle;
		this.cli_id = cli_id;
		this.alias = alias;
		this.calle = calle;
		this.numero = numero;
		this.depto = depto;
		this.comentarios = comentarios;
		this.fec_crea = fec_crea;
		this.estado = estado;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public Long getCli_id() {
		return cli_id;
	}

	public void setCli_id(Long cli_id) {
		this.cli_id = cli_id;
	}

	public Long getCom_id() {
		return com_id;
	}

	public void setCom_id(Long com_id) {
		this.com_id = com_id;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getDepto() {
		return depto;
	}

	public void setDepto(String depto) {
		this.depto = depto;
	}

	public Character getEstado() {
		return estado;
	}

	public void setEstado(Character estado) {
		this.estado = estado;
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

	public Long getLoc_cod() {
		return loc_cod;
	}

	public void setLoc_cod(Long loc_cod) {
		this.loc_cod = loc_cod;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Long getTipo_calle() {
		return tipo_calle;
	}

	public void setTipo_calle(Long tipo_calle) {
		this.tipo_calle = tipo_calle;
	}

	public Long getTip_id() {
		return tip_id;
	}

	public void setTip_id(Long tip_id) {
		this.tip_id = tip_id;
	}

	public Character getNueva() {
		return nueva;
	}

	public void setNueva(Character nueva) {
		this.nueva = nueva;
	}

	public Long getReg_id() {
		return reg_id;
	}

	public void setReg_id(Long reg_id) {
		this.reg_id = reg_id;
	}



}