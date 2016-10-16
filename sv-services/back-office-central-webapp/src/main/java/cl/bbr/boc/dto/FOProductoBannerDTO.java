/*
 * Created on 04-feb-2009
 *
 */
package cl.bbr.boc.dto;

import java.util.Date;

import cl.jumbo.common.dto.CategoriaMasvDTO;

/**
 * @author jdroguett
 * 
 */
public class FOProductoBannerDTO {
	private int id;
	private long idProducto;
	private String codSap;
	private String descripcion;
	private MarcaDTO marca;
	private LocalDTO local;
	private CategoriaMasvDTO categoria;
	private int cantidad;

	private Date fechaInicio;
	private Date fechaFinal;
	private String nombreBanner;
	private String descripcionBanner;
	private String colorBanner;

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}

	/**
	 * @return el fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio
	 *            el fechaInicio a establecer
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return el fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal
	 *            el fechaFinal a establecer
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return el nombreBanner
	 */
	public String getNombreBanner() {
		return nombreBanner;
	}

	/**
	 * @param nombreBanner
	 *            el nombreBanner a establecer
	 */
	public void setNombreBanner(String nombreBanner) {
		this.nombreBanner = nombreBanner;
	}

	/**
	 * @return el descripcionBanner
	 */
	public String getDescripcionBanner() {
		return descripcionBanner;
	}

	/**
	 * @param descripcionBanner
	 *            el descripcionBanner a establecer
	 */
	public void setDescripcionBanner(String descripcionBanner) {
		this.descripcionBanner = descripcionBanner;
	}

	public String getCodSap() {
		return codSap;
	}

	public void setCodSap(String codSap) {
		this.codSap = codSap;
	}

	public MarcaDTO getMarca() {
		return marca;
	}

	public void setMarca(MarcaDTO marca) {
		this.marca = marca;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDTO getLocal() {
		return local;
	}

	public void setLocal(LocalDTO local) {
		this.local = local;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public CategoriaMasvDTO getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaMasvDTO categoria) {
		this.categoria = categoria;
	}

	public String getColorBanner() {
		return colorBanner;
	}

	public void setColorBanner(String colorBanner) {
		this.colorBanner = colorBanner;
	}
}