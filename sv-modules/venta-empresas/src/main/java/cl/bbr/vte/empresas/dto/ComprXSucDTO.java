package cl.bbr.vte.empresas.dto;

/**
 * DTO para datos comprador / sucursal 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ComprXSucDTO {

	private long 	id_sucursal;
	private long	id_comprador;
	private long	id_empresa;
	private String	nom_sucursal;
	private String	nom_empresa;
	private String  tipo_acceso;
	
    /**
     * @return Devuelve tipo_acceso.
     */
    public String getTipo_acceso() {
        return tipo_acceso;
    }
    /**
     * @param tipo_acceso El tipo_acceso a establecer.
     */
    public void setTipo_acceso(String tipo_acceso) {
        this.tipo_acceso = tipo_acceso;
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
	public String getNom_sucursal() {
		return nom_sucursal;
	}
	public void setNom_sucursal(String nom_sucursal) {
		this.nom_sucursal = nom_sucursal;
	}
	public long getId_comprador() {
		return id_comprador;
	}
	public void setId_comprador(long id_comprador) {
		this.id_comprador = id_comprador;
	}
	public long getId_sucursal() {
		return id_sucursal;
	}
	public void setId_sucursal(long id_sucursal) {
		this.id_sucursal = id_sucursal;
	}

}
