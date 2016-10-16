package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Fecha creación: 13/10/2011
 * 
 * Clase que se encarga de la serialización de los productos sustitutos que se encuentran sobre el margen de sustitución
 * esta clase inicialmente se utiliza para mostrar el reporte de productos sustitutos sobre umbral para ser removidos del bin
 * 
 **/
public class ProductoSobreMargenDTO implements Serializable {

	/**
	 * <code>idSustitutoSobreUmbral</code>
	 * Se encarga de guardar el id de llave del producto sustituto sobre umbral
	 * esta se asigna en caso de eliminacion
	 */
	private long idSustitutoSobreUmbral;
	
	/**
	 * <code>fechaOperacion</code>
	 * Fecha en la cual se realizo la operacion de sustitución de producto
	 */
	private Date fechaOperacion;
	
	/**
	 * <code>idDetalle</code>
	 * id del detallde de pedido asociado a la solicitus al momento de realizar la sustitución
	 */
	private long idDetalle;
	
	/**
	 * <code>idRonda</code>
	 * Numero de la ronda en la cual se realizo la sustitución
	 */
	private long idRonda;
	
	/**
	 * <code>nombrePickeador</code>
	 * Nombre del pickeador que realizo la sustitución
	 */
	private String nombrePickeador;
	
	/**
	 * <code>apellidoPickeador</code>
	 * Apellido del pickeador que realizo la sustitución
	 */
	private String apellidoPickeador;
	
	/**
	 * <code>descripcionProducto</code>
	 * Descripcion del producto (nombre del producto) sustituto que debe ser removido del bin
	 */
	private String descripcionProducto;
	
	/**
	 * <code>idProducto</code>
	 * id del producto sustituto que debe ser removido
	 */
	private long idProducto;
	
	/**
	 * <code>codBin</code>
	 * Codigo del bin donde se encuentra el producto sustituto
	 */
	private String codBin;
	
	/**
	 * <code>mensaje</code>
	 * mensaje a mostrar al usuario
	 */
	private String mensaje;
	
	/**
	 * <code>idPedido</code>
	 * id del pedido para el cual se realizo la sustitución de producto.
	 */
	private long idPedido;
	
	/**
	 * @return Devuelve apellidoPickeador.
	 */
	public String getApellidoPickeador() {
		return apellidoPickeador;
	}
	/**
	 * @param apellidoPickeador El apellidoPickeador a establecer.
	 */
	public void setApellidoPickeador(String apellidoPickeador) {
		this.apellidoPickeador = apellidoPickeador;
	}
	/**
	 * @return Devuelve codBin.
	 */
	public String getCodBin() {
		return codBin;
	}
	/**
	 * @param codBin El codBin a establecer.
	 */
	public void setCodBin(String codBin) {
		this.codBin = codBin;
	}
	/**
	 * @return Devuelve descripcionProducto.
	 */
	public String getDescripcionProducto() {
		return descripcionProducto;
	}
	/**
	 * @param descripcionProducto El descripcionProducto a establecer.
	 */
	public void setDescripcionProducto(String descripcionProducto) {
		this.descripcionProducto = descripcionProducto;
	}
	/**
	 * @return Devuelve fechaOperacion.
	 */
	public Date getFechaOperacion() {
		return fechaOperacion;
	}
	/**
	 * @param fechaOperacion El fechaOperacion a establecer.
	 */
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	/**
	 * @return Devuelve idDetalle.
	 */
	public long getIdDetalle() {
		return idDetalle;
	}
	/**
	 * @param idDetalle El idDetalle a establecer.
	 */
	public void setIdDetalle(long idDetalle) {
		this.idDetalle = idDetalle;
	}
	/**
	 * @return Devuelve idPedido.
	 */
	public long getIdPedido() {
		return idPedido;
	}
	/**
	 * @param idPedido El idPedido a establecer.
	 */
	public void setIdPedido(long idPedido) {
		this.idPedido = idPedido;
	}
	/**
	 * @return Devuelve idProducto.
	 */
	public long getIdProducto() {
		return idProducto;
	}
	/**
	 * @param idProducto El idProducto a establecer.
	 */
	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}
	/**
	 * @return Devuelve idRonda.
	 */
	public long getIdRonda() {
		return idRonda;
	}
	/**
	 * @param idRonda El idRonda a establecer.
	 */
	public void setIdRonda(long idRonda) {
		this.idRonda = idRonda;
	}
	/**
	 * @return Devuelve idSustitutoSobreUmbral.
	 */
	public long getIdSustitutoSobreUmbral() {
		return idSustitutoSobreUmbral;
	}
	/**
	 * @param idSustitutoSobreUmbral El idSustitutoSobreUmbral a establecer.
	 */
	public void setIdSustitutoSobreUmbral(long idSustitutoSobreUmbral) {
		this.idSustitutoSobreUmbral = idSustitutoSobreUmbral;
	}
	/**
	 * @return Devuelve mensaje.
	 */
	public String getMensaje() {
		return mensaje;
	}
	/**
	 * @param mensaje El mensaje a establecer.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	/**
	 * @return Devuelve nombrePickeador.
	 */
	public String getNombrePickeador() {
		return nombrePickeador;
	}
	/**
	 * @param nombrePickeador El nombrePickeador a establecer.
	 */
	public void setNombrePickeador(String nombrePickeador) {
		this.nombrePickeador = nombrePickeador;
	}
	
	/* implementacion del metodo toString()
	 * que se encarga de retornar los valores de los atributos del dto
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String resultado = "";
		resultado += "idSustitutoSobreUmbral: " + idSustitutoSobreUmbral+"\n";
		resultado += "idDetalle: " + idDetalle+"\n";
		resultado += "idPedido: " + idPedido+"\n";
		resultado += "idRonda: " + idRonda+"\n";
		resultado += "codBin: " + codBin+"\n";
		resultado += "descripcionProducto: " + descripcionProducto+"\n";
		resultado += "fechaOperacion: " + fechaOperacion+"\n";
		resultado += "nombrePickeador: " + nombrePickeador+"\n";
		resultado += "apellidoPickeador: " + apellidoPickeador+"\n";
		resultado += "mensaje: " + mensaje+"\n";
		return super.toString();
	}
}
