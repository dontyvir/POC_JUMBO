package cl.bbr.jumbocl.pedidos.dto;

public class RondaDetalleResumenDTO {
	private long id_pedido;
	private String nom_sector;
	private double cant_asignada;
	/**
	 * @return Returns the cant_asignada.
	 */
	public double getCant_asignada() {
		return cant_asignada;
	}
	/**
	 * @return Returns the id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @return Returns the nom_sector.
	 */
	public String getNom_sector() {
		return nom_sector;
	}
	/**
	 * @param cant_asignada The cant_asignada to set.
	 */
	public void setCant_asignada(double cant_asignada) {
		this.cant_asignada = cant_asignada;
	}
	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	/**
	 * @param nom_sector The nom_sector to set.
	 */
	public void setNom_sector(String nom_sector) {
		this.nom_sector = nom_sector;
	}
	
	}
