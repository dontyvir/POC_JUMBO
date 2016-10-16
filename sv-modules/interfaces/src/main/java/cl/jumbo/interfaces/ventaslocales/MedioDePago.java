/*
 * Created on Jun 16, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.jumbo.interfaces.ventaslocales;

/**
 * @author jvillalobos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MedioDePago {
	
	static public int FIDELIZACION = 8;

	private int codigo;
	private int importe;
	
	public MedioDePago() {
	}

	public MedioDePago(int codigo, int importe) {
		this.codigo = codigo;
		this.importe = importe;
	}

	public MedioDePago(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public int getImporte() {
		return importe;
	}
	public void setImporte(int importe) {
		this.importe = importe;
	}
}
