package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class SemanaJornadaPedidoDTO implements Serializable{
	
	private long	id_semana;
	private int 	n_semana;
	private int 	ano;
	private String 	fec_ini;
	private long	id_jdespacho;
	private int 	dia_semana;
	private String 	fec_jdespacho;
	private long	id_pedido;
	private long	id_estado_pedido;
	
	/**
	 * @return Returns the id_semana.
	 */
	public long getId_semana() {
		return id_semana;
	}
	/**
	 * @param id_semana The id_semana to set.
	 */
	public void setId_semana(long id_semana) {
		this.id_semana = id_semana;
	}
	/**
	 * @return Returns the ano.
	 */
	public int getAno() {
		return ano;
	}
	/**
	 * @param ano The ano to set.
	 */
	public void setAno(int ano) {
		this.ano = ano;
	}
	/**
	 * @return Returns the dia_semana.
	 */
	public int getDia_semana() {
		return dia_semana;
	}
	/**
	 * @param dia_semana The dia_semana to set.
	 */
	public void setDia_semana(int dia_semana) {
		this.dia_semana = dia_semana;
	}
	/**
	 * @return Returns the fec_ini.
	 */
	public String getFec_ini() {
		return fec_ini;
	}
	/**
	 * @param fec_ini The fec_ini to set.
	 */
	public void setFec_ini(String fec_ini) {
		this.fec_ini = fec_ini;
	}
	/**
	 * @return Returns the fec_jdespacho.
	 */
	public String getFec_jdespacho() {
		return fec_jdespacho;
	}
	/**
	 * @param fec_jdespacho The fec_jdespacho to set.
	 */
	public void setFec_jdespacho(String fec_jdespacho) {
		this.fec_jdespacho = fec_jdespacho;
	}
	/**
	 * @return Returns the id_estado_pedido.
	 */
	public long getId_estado_pedido() {
		return id_estado_pedido;
	}
	/**
	 * @param id_estado_pedido The id_estado_pedido to set.
	 */
	public void setId_estado_pedido(long id_estado_pedido) {
		this.id_estado_pedido = id_estado_pedido;
	}
	/**
	 * @return Returns the id_jdespacho.
	 */
	public long getId_jdespacho() {
		return id_jdespacho;
	}
	/**
	 * @param id_jdespacho The id_jdespacho to set.
	 */
	public void setId_jdespacho(long id_jdespacho) {
		this.id_jdespacho = id_jdespacho;
	}
	/**
	 * @return Returns the id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	/**
	 * @return Returns the n_semana.
	 */
	public int getN_semana() {
		return n_semana;
	}
	/**
	 * @param n_semana The n_semana to set.
	 */
	public void setN_semana(int n_semana) {
		this.n_semana = n_semana;
	}

	
}
