package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

/**
 * DTO para datos de las últimas compras. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class UltimasComprasDTO implements Serializable {

	private long id;			// IDentificador de lista de compras
	private long fecha;			// Fecha de la compra
	private String lugar_compra;// Lugar de la compra
	private double unidades;    // Total de productos de la compra
	private String nombre;		// Alias asociado a la compra
	private String tipo;		// I: internet L: local E: Especial
    private ListaGrupoDTO grupoLista = new ListaGrupoDTO();

	/**
	 * Constructor
	 */
	public UltimasComprasDTO() {

	}

	public long getFecha() {
		return fecha;
	}

	public void setFecha(long fecha) {
		this.fecha = fecha;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLugar_compra() {
		return lugar_compra;
	}

	public void setLugar_compra(String lugar_compra) {
		this.lugar_compra = lugar_compra;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getUnidades() {
		return unidades;
	}

	public void setUnidades(double unidades) {
		this.unidades = unidades;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	
    /**
     * @return Devuelve grupoLista.
     */
    public ListaGrupoDTO getGrupoLista() {
        return grupoLista;
    }
    /**
     * @param grupoLista El grupoLista a establecer.
     */
    public void setGrupoLista(ListaGrupoDTO grupoLista) {
        this.grupoLista = grupoLista;
    }
}