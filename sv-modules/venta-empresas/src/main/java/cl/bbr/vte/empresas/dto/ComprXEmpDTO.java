package cl.bbr.vte.empresas.dto;

/**
 * DTO para datos comprador / empresa. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ComprXEmpDTO {

	private long	id_comprador;
	private long	id_empresa;
	private String	nom_empresa;
	
	public long getId_comprador() {
		return id_comprador;
	}
	public void setId_comprador(long id_comprador) {
		this.id_comprador = id_comprador;
	}
	public long getId_empresa() {
		return id_empresa;
	}
	public void setId_empresa(long id_empresa) {
		this.id_empresa = id_empresa;
	}
	public String getNom_empresa() {
		return nom_empresa;
	}
	public void setNom_empresa(String nom_empresa) {
		this.nom_empresa = nom_empresa;
	}
	

}
