package cl.bbr.jumbocl.common.model;

public class ComprXSucEntity {

	private Long 	id_sucursal;
	private Long	id_comprador;
	private String	tip_comprador;
	
	public Long getId_comprador() {
		return id_comprador;
	}
	public void setId_comprador(Long id_comprador) {
		this.id_comprador = id_comprador;
	}
	public Long getId_sucursal() {
		return id_sucursal;
	}
	public void setId_sucursal(Long id_sucursal) {
		this.id_sucursal = id_sucursal;
	}
	public String getTip_comprador() {
		return tip_comprador;
	}
	public void setTip_comprador(String tip_comprador) {
		this.tip_comprador = tip_comprador;
	}
	
	
}
