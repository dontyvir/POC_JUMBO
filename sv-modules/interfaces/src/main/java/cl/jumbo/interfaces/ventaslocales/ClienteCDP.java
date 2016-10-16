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
public class ClienteCDP {

	private int rut;
	private MedioDePago medioDePago;
	
	public ClienteCDP() {
		this.medioDePago = new MedioDePago();
	}

	public ClienteCDP(int rut) {
		this.rut = rut;
		this.medioDePago = new MedioDePago();
	}

	public ClienteCDP(int rut, int codigoMedioPago) {
		this.rut = rut;
		this.medioDePago = new MedioDePago(codigoMedioPago);
	}
	public MedioDePago getMedioDePago() {
		return medioDePago;
	}
	public void setMedioDePago(MedioDePago medioDePago) {
		this.medioDePago = medioDePago;
	}
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
}
