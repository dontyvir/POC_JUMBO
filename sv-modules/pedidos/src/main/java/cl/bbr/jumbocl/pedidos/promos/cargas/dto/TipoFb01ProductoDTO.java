package cl.bbr.jumbocl.pedidos.promos.cargas.dto;

import cl.bbr.jumbocl.common.utils.Formatos;

public class TipoFb01ProductoDTO {
	public static final int CODIGO_LEN = 6;
	public static final int PRODUCTO_LEN = 12;
	public static final int COD_RET_LEN = 1;
	public static final int FILLER_LEN = 1;
	
	public static final int TIPO_01_PRODUCTO_REG_LEN = 20;
	
	private String codigo;
	private String producto;
	private String cod_ret;
	private String filler; 

	public TipoFb01ProductoDTO() {
	}

	public String toMsg(){
		String out = "";
		out += Formatos.formatField(codigo, 	CODIGO_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(producto, 	PRODUCTO_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(cod_ret, 	COD_RET_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(filler, 	FILLER_LEN, 	Formatos.ALIGN_RIGHT,"0");
	
		return out;
	}
	
	
	/**
	 * @return Returns the cod_ret.
	 */
	public String getCod_ret() {
		return cod_ret;
	}

	/**
	 * @param cod_ret The cod_ret to set.
	 */
	public void setCod_ret(String cod_ret) {
		this.cod_ret = cod_ret;
	}

	/**
	 * @return Returns the codigo.
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return Returns the filler.
	 */
	public String getFiller() {
		return filler;
	}

	/**
	 * @param filler The filler to set.
	 */
	public void setFiller(String filler) {
		this.filler = filler;
	}

	/**
	 * @return Returns the producto.
	 */
	public String getProducto() {
		return producto;
	}

	/**
	 * @param producto The producto to set.
	 */
	public void setProducto(String producto) {
		this.producto = producto;
	}

}
