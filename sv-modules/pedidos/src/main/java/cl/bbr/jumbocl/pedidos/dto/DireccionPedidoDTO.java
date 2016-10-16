package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class DireccionPedidoDTO implements Serializable{	
   private int id_dir;
   private String alias;
   private String calle;
   private String numero;
   private String depto;
   private String comentarios;
   private String estado;
   private String fec_crea;
   private String fnueva;
   private String cuadrante;
   private String comuna;
   private long id_cliente;
   private String nom_local;
   private String nom_tipocalle;

   private long id_loc;
   private long id_com;
   private long id_zon;
   private TipoCalleDTO tipoCalle;

   public DireccionPedidoDTO() {}
   
	public DireccionPedidoDTO(int id, long id_cliente, String alias, String calle, String comuna, String depto, 
			String comentarios, String est, String fec_crea, String numero, String nom_local, String nom_tipocalle,
			String fnueva, String cuadrante,
			long id_loc, long id_com, long id_zon, TipoCalleDTO tipoCalleDto) {
		this.id_dir = id;
		this.id_cliente = id_cliente;
		this.alias = alias;
		this.calle = calle;
		this.comuna = comuna;
		this.depto = depto;
		this.estado = est;
		this.comentarios = comentarios;
		this.fec_crea = fec_crea;
		this.numero = numero;
		this.nom_local = nom_local;
		this.nom_tipocalle = nom_tipocalle;
		this.fnueva = fnueva;
		this.cuadrante = cuadrante;
		this.id_loc = id_loc;
		this.id_com = id_com;
		this.id_zon = id_zon;
		this.tipoCalle = tipoCalleDto;
		
	}
   
	public String getAlias() {
		return alias;
	}
	public String getCalle() {
		return calle;
	}
	public String getComentarios() {
		return comentarios;
	}
	public String getComuna() {
		return comuna;
	}
	public String getDepto() {
		return depto;
	}
	public String getEstado() {
		return estado;
	}
	public String getFec_crea() {
		return fec_crea;
	}
	public long getId_cliente() {
		return id_cliente;
	}
	public int getId_dir() {
		return id_dir;
	}
	public String getNom_local() {
		return nom_local;
	}
	public String getNom_tipocalle() {
		return nom_tipocalle;
	}
	public String getNumero() {
		return numero;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public void setDepto(String depto) {
		this.depto = depto;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setFec_crea(String fec_crea) {
		this.fec_crea = fec_crea;
	}
	public void setId_cliente(long id_cliente) {
		this.id_cliente = id_cliente;
	}
	public void setId_dir(int id_dir) {
		this.id_dir = id_dir;
	}
	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}
	public void setNom_tipocalle(String nom_tipocalle) {
		this.nom_tipocalle = nom_tipocalle;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @return Returns the id_com.
	 */
	public long getId_com() {
		return id_com;
	}

	/**
	 * @param id_com The id_com to set.
	 */
	public void setId_com(long id_com) {
		this.id_com = id_com;
	}

	/**
	 * @return Returns the id_loc.
	 */
	public long getId_loc() {
		return id_loc;
	}

	/**
	 * @param id_loc The id_loc to set.
	 */
	public void setId_loc(long id_loc) {
		this.id_loc = id_loc;
	}

	/**
	 * @return Returns the tipoCalle.
	 */
	public TipoCalleDTO getTipoCalle() {
		return tipoCalle;
	}

	/**
	 * @param tipoCalle The tipoCalle to set.
	 */
	public void setTipoCalle(TipoCalleDTO tipoCalle) {
		this.tipoCalle = tipoCalle;
	}

	/**
	 * @return Returns the cuadrante.
	 */
	public String getCuadrante() {
		return cuadrante;
	}

	/**
	 * @param cuadrante The cuadrante to set.
	 */
	public void setCuadrante(String cuadrante) {
		this.cuadrante = cuadrante;
	}

	/**
	 * @return Returns the fnueva.
	 */
	public String getFnueva() {
		return fnueva;
	}

	/**
	 * @param fnueva The fnueva to set.
	 */
	public void setFnueva(String fnueva) {
		this.fnueva = fnueva;
	}

	/**
	 * @return Returns the id_zon.
	 */
	public long getId_zon() {
		return id_zon;
	}

	/**
	 * @param id_zon The id_zon to set.
	 */
	public void setId_zon(long id_zon) {
		this.id_zon = id_zon;
	}
	   
}
