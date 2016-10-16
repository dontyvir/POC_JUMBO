/*
 * Creado el 29-03-2005
 *
 * Para cambiar la plantilla para este archivo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
package cl.cencosud.jumbo.beans;

/**
 * @author rbelmar
 *
 * Para cambiar la plantilla para este comentario de tipo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
public class XMLEstadoTarea
{
	private String id;
	private String estado;
	private String detalle;


	public XMLEstadoTarea()
	{
		id = "";
		estado = "";
		detalle = "";
	}


	/**
	 * @return
	 */
	public String getDetalle() {
		return detalle;
	}

	/**
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param string
	 */
	public void setDetalle(String string) {
		detalle = string;
	}

	/**
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

}
